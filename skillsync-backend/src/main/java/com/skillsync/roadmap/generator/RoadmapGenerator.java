package com.skillsync.roadmap.generator;

import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates an ordered learning roadmap from the user's skill progress.
 *
 * Estimated days are calculated based on how much practice time remains
 * and how many minutes per day the user has available.
 *
 * Base minutes needed per skill tier:
 *  - 0%    progress  → 840 min (14h to start from scratch)
 *  - 1–29% progress  → 600 min (10h to reach beginner level)
 *  - 30–69% progress → 420 min (7h  to reach intermediate)
 *  - 70–99% progress → 180 min (3h  to polish to mastery)
 *  - 100%  progress  → skipped (already mastered)
 *
 * estimatedDays = ceil(baseMinutes / dailyMinutesAvailable)
 * Minimum 1 day always returned.
 */
@Component
public class RoadmapGenerator {

    private static final int DEFAULT_DAILY_MINUTES = 60;

    public List<RoadmapStep> generate(User user,
                                      List<UserSkillProgress> progressList) {
        return generate(user, progressList, DEFAULT_DAILY_MINUTES);
    }

    public List<RoadmapStep> generate(User user,
                                      List<UserSkillProgress> progressList,
                                      int dailyMinutesAvailable) {

        // Guard against zero/negative to avoid division by zero
        int dailyMinutes = (dailyMinutesAvailable > 0)
                ? dailyMinutesAvailable
                : DEFAULT_DAILY_MINUTES;

        List<RoadmapStep> steps = new ArrayList<>();

        // Sort: incomplete skills, lowest progress first (weakest areas first)
        List<UserSkillProgress> filtered = progressList.stream()
                .filter(p -> p.getProgressPercentage() < 100.0)
                .sorted(Comparator.comparingDouble(
                        UserSkillProgress::getProgressPercentage))
                .toList();

        int order = 1;
        for (UserSkillProgress p : filtered) {

            double progress = p.getProgressPercentage();

            String  title;
            String  description;
            int     baseMinutes;

            if (progress == 0.0) {
                title       = "Start: " + p.getSkill().getName();
                description = "You haven't touched this skill yet. "
                        + "Begin with the basics and build a solid foundation.";
                baseMinutes = 840;

            } else if (progress < 30.0) {
                title       = "Focus: " + p.getSkill().getName();
                description = "Your progress is low. "
                        + "Dedicate focused daily sessions to move past the beginner stage.";
                baseMinutes = 600;

            } else if (progress < 70.0) {
                title       = "Build: " + p.getSkill().getName();
                description = "Good foundation! "
                        + "Work on intermediate topics and tackle practice problems.";
                baseMinutes = 420;

            } else {
                title       = "Polish: " + p.getSkill().getName();
                description = "Almost there! "
                        + "Practise advanced problems and review edge cases to reach mastery.";
                baseMinutes = 180;
            }

            // Scale remaining work by how far along the user already is
            // e.g. if progress = 50% and base = 420min → only 210min remain
            double remainingFraction = 1.0 - (progress / 100.0);
            int    remainingMinutes  = (int) Math.ceil(baseMinutes * remainingFraction);
            int    estimatedDays     = Math.max(1,
                    (int) Math.ceil((double) remainingMinutes / dailyMinutes));

            steps.add(RoadmapStep.builder()
                    .user(user)
                    .skill(p.getSkill())
                    .stepOrder(order++)
                    .title(title)
                    .description(description)
                    .estimatedDays(estimatedDays)
                    .completed(false)
                    .build());
        }

        return steps;
    }
}
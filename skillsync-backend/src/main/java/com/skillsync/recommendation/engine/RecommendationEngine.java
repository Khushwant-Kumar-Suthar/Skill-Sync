package com.skillsync.recommendation.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.skillsync.recommendation.dto.RecommendationDTO;
import com.skillsync.skill.progress.entity.UserSkillProgress;

/**
 * Rule-based recommendation engine.
 *
 * Priority rules (evaluated top-to-bottom):
 *  1. Never practiced (lastPracticedAt == null)  → HIGH   "Start Practicing"
 *  2. progress < 30%                             → HIGH   "Needs Attention"
 *  3. progress 30–69%                            → MEDIUM "Keep Going"
 *  4. progress 70–99%                            → LOW    "Polish It"
 *  5. progress == 100%                           → excluded (mastered)
 */
@Component
public class RecommendationEngine {

    public List<RecommendationDTO> recommend(
            List<UserSkillProgress> progressList) {

        List<RecommendationDTO> recommendations = new ArrayList<>();

        for (UserSkillProgress p : progressList) {

            // FIX: mastered skills excluded — they don't need recommending
            if (p.getProgressPercentage() >= 100.0) {
                continue;
            }

            String reason;
            String priority;

            if (p.getLastPracticedAt() == null) {
                reason   = "You haven't started practicing this skill yet. Begin today!";
                priority = "HIGH";

            } else if (p.getProgressPercentage() < 30.0) {
                reason   = "Your progress is low. Focus on this skill to build a solid foundation.";
                priority = "HIGH";

            } else if (p.getProgressPercentage() < 70.0) {
                reason   = "Good start! Keep practicing consistently to reach proficiency.";
                priority = "MEDIUM";

            } else {
                reason   = "Almost there! Tackle advanced topics to reach mastery.";
                priority = "LOW";
            }

            recommendations.add(RecommendationDTO.builder()
                    .skillId(p.getSkill().getId())
                    .skillName(p.getSkill().getName())
                    .currentProgress(p.getProgressPercentage())
                    .currentScore(p.getScore())
                    .reason(reason)
                    .priority(priority)
                    .lastPracticedAt(p.getLastPracticedAt() != null
                            ? p.getLastPracticedAt().toString()
                            : "Never")
                    .build());
        }

        // FIX: removed unchecked raw casts — replaced with typed Comparator
        recommendations.sort(
                Comparator.comparingInt(
                        (RecommendationDTO r) -> priorityOrder(r.getPriority()))
                .thenComparingDouble(RecommendationDTO::getCurrentProgress)
        );

        return recommendations;
    }

    private int priorityOrder(String priority) {
        return switch (priority) {
            case "HIGH"   -> 0;
            case "MEDIUM" -> 1;
            case "LOW"    -> 2;
            default       -> 3;
        };
    }
}
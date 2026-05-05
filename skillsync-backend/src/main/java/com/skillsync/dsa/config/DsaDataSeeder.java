package com.skillsync.dsa.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.common.TagType;
import com.skillsync.dsa.plan.entity.DsaPlan;
import com.skillsync.dsa.plan.entity.DsaPlanItem;
import com.skillsync.dsa.plan.repository.DsaPlanItemRepository;
import com.skillsync.dsa.plan.repository.DsaPlanRepository;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.dsa.problem.repository.ProblemRepository;
import com.skillsync.dsa.tag.entity.Tag;
import com.skillsync.dsa.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

/**
 * Seeds ~300 DSA problems for development/demo.
 *
 * Only runs when the "dev" Spring profile is active.
 * Idempotent: skips seeding if any problems already exist.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DsaDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DsaDataSeeder.class);

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final DsaPlanRepository planRepository;
    private final DsaPlanItemRepository planItemRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (problemRepository.count() > 0) {
            log.info("DsaDataSeeder: problems already exist — skipping seed.");
            return;
        }

        log.info("DsaDataSeeder: seeding ~300 DSA problems...");

        // Topics (TOPIC) + patterns (PATTERN) are the main drivers for recommendations.
        List<String> topics = List.of(
                "Arrays", "Strings", "Hashing", "Two Pointers", "Sliding Window",
                "Stacks", "Queues", "Linked List", "Binary Search", "Trees",
                "Heaps", "Backtracking", "Greedy", "Dynamic Programming", "Graphs"
        );

        List<String> patterns = List.of(
                "Prefix Sum", "Monotonic Stack", "BFS", "DFS", "Union Find",
                "Topological Sort", "Dijkstra", "Binary Lifting", "Bitmask DP", "Kadane"
        );

        List<String> companies = List.of(
                "Google", "Amazon", "Microsoft", "Meta", "Apple"
        );

        Map<String, Tag> topicTags = new HashMap<>();
        for (String t : topics) topicTags.put(t, upsertTag(TagType.TOPIC, t));

        Map<String, Tag> patternTags = new HashMap<>();
        for (String p : patterns) patternTags.put(p, upsertTag(TagType.PATTERN, p));

        Map<String, Tag> companyTags = new HashMap<>();
        for (String c : companies) companyTags.put(c, upsertTag(TagType.COMPANY, c));

        List<Problem> problems = new ArrayList<>(320);
        Set<String> usedSlugs = new HashSet<>(400);

        // Create 300 problems by mixing topics and patterns.
        // Titles are intentionally generic (no copying proprietary lists).
        int idx = 1;
        Random rnd = new Random(42); // deterministic seed

        while (problems.size() < 300) {
            String topic = topics.get(rnd.nextInt(topics.size()));
            String pattern = patterns.get(rnd.nextInt(patterns.size()));
            String company = companies.get(rnd.nextInt(companies.size()));

            Difficulty diff = difficultyForIndex(idx);

            String title = switch (diff) {
                case EASY -> topic + " Basics " + idx;
                case MEDIUM -> topic + " Challenge " + idx;
                case HARD -> topic + " Mastery " + idx;
            };

            String slug = uniqueSlug(slugify(title), usedSlugs);
            String sourceUrl = "https://leetcode.com/problemset/" + slug + "/";

            Set<Tag> tags = new HashSet<>();
            tags.add(topicTags.get(topic));
            // Add 1–2 extra tags
            if (rnd.nextBoolean()) tags.add(patternTags.get(pattern));
            if (rnd.nextInt(100) < 35) tags.add(companyTags.get(company));

            problems.add(Problem.builder()
                    .title(title)
                    .slug(slug)
                    .difficulty(diff)
                    .description("Practice problem for " + topic + " (" + diff + ").")
                    .sourceUrl(sourceUrl)
                    .tags(tags)
                    .build());

            idx++;
        }

        problemRepository.saveAll(problems);

        // Plans: Normal (all), Top 75, Top 150
        seedPlans(problems);

        log.info("DsaDataSeeder: seeded {} problems, {} tags, {} plans.",
                problems.size(), tagRepository.count(), planRepository.count());
    }

    private Tag upsertTag(TagType type, String name) {
        return tagRepository.findByTypeAndNameIgnoreCase(type, name)
                .orElseGet(() -> tagRepository.save(Tag.builder()
                        .type(type)
                        .name(name)
                        .build()));
    }

    private Difficulty difficultyForIndex(int i) {
        // ~55% EASY, ~35% MEDIUM, ~10% HARD
        int mod = i % 20;
        if (mod < 11) return Difficulty.EASY;
        if (mod < 18) return Difficulty.MEDIUM;
        return Difficulty.HARD;
    }

    private String slugify(String s) {
        return s.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private String uniqueSlug(String base, Set<String> used) {
        String slug = base;
        int n = 2;
        while (!used.add(slug)) {
            slug = base + "-" + n;
            n++;
        }
        return slug;
    }

    private void seedPlans(List<Problem> seededProblems) {
        DsaPlan normal = planRepository.save(DsaPlan.builder()
                .name("Normal (All Problems)")
                .slug("normal")
                .description("All available problems in the library.")
                .build());

        DsaPlan top75 = planRepository.save(DsaPlan.builder()
                .name("Top 75")
                .slug("top-75")
                .description("A focused set of 75 problems for interview preparation.")
                .build());

        DsaPlan top150 = planRepository.save(DsaPlan.builder()
                .name("Top 150")
                .slug("top-150")
                .description("A comprehensive set of 150 problems for strong fundamentals.")
                .build());

        // Use a stable ordering: by difficulty then title (deterministic).
        List<Problem> ordered = seededProblems.stream()
                .sorted(Comparator
                        .comparing(Problem::getDifficulty)
                        .thenComparing(p -> p.getTitle().toLowerCase(Locale.ROOT)))
                .toList();

        List<DsaPlanItem> items = new ArrayList<>();
        int idx = 1;
        for (Problem p : ordered) {
            items.add(DsaPlanItem.builder()
                    .plan(normal)
                    .problem(p)
                    .orderIndex(idx++)
                    .build());
        }

        idx = 1;
        for (Problem p : ordered.stream().limit(75).toList()) {
            items.add(DsaPlanItem.builder()
                    .plan(top75)
                    .problem(p)
                    .orderIndex(idx++)
                    .build());
        }

        idx = 1;
        for (Problem p : ordered.stream().limit(150).toList()) {
            items.add(DsaPlanItem.builder()
                    .plan(top150)
                    .problem(p)
                    .orderIndex(idx++)
                    .build());
        }

        planItemRepository.saveAll(items);
    }
}


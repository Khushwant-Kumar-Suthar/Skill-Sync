package com.skillsync.common.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.activity.entity.ActivityLog;
import com.skillsync.activity.repository.ActivityLogRepository;
import com.skillsync.common.constant.ActivityType;
import com.skillsync.common.constant.Difficulty;
import com.skillsync.common.constant.Role;
import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.roadmap.repository.RoadmapStepRepository;
import com.skillsync.skill.category.entity.SkillCategory;
import com.skillsync.skill.category.repository.SkillCategoryRepository;
import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.skill.repository.SkillRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds demo data on application startup.
 *
 * Only runs when the "dev" Spring profile is active.
 * Add --spring.profiles.active=dev to your run configuration,
 * or set it in application-dev.properties.
 *
 * Safe to re-run — checks if data already exists before inserting.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository              userRepository;
    private final SkillCategoryRepository     categoryRepository;
    private final SkillRepository             skillRepository;
    private final UserSkillProgressRepository progressRepository;
    private final ActivityLogRepository       activityLogRepository;
    private final RoadmapStepRepository       roadmapStepRepository;
    private final PasswordEncoder             passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if (userRepository.count() > 0) {
            log.info("DataSeeder: data already exists — skipping seed.");
            return;
        }

        log.info("DataSeeder: seeding demo data...");

        // ── 1. Users ─────────────────────────────────────────────────────────
        User admin   = saveUser("Admin User",       "admin@skillsync.com", "Admin@123", Role.ADMIN);
        User khush   = saveUser("Khushwant Suthar", "khush@skillsync.com", "User@123",  Role.USER);
        User priya   = saveUser("Priya Sharma",     "priya@skillsync.com", "User@123",  Role.USER);
        User rahul   = saveUser("Rahul Verma",      "rahul@skillsync.com", "User@123",  Role.USER);

        // ── 2. Categories ─────────────────────────────────────────────────────
        SkillCategory dsa      = saveCategory("Data Structures & Algorithms", "Arrays, Trees, Graphs, Sorting");
        SkillCategory backend  = saveCategory("Backend Development",          "APIs, Databases, Spring Boot");
        SkillCategory frontend = saveCategory("Frontend Development",         "HTML, CSS, JavaScript, React");
        SkillCategory devops   = saveCategory("DevOps & Cloud",               "Docker, Linux, CI/CD, AWS");
        SkillCategory coreCs   = saveCategory("Core Computer Science",        "OS, Networks, DBMS");

        // ── 3. Skills ─────────────────────────────────────────────────────────
        // DSA
        Skill arrays  = saveSkill("Arrays & Strings",       dsa,      Difficulty.BEGINNER,     "Sliding window, two pointers, prefix sums");
        Skill lists   = saveSkill("Linked Lists",           dsa,      Difficulty.BEGINNER,     "Singly/doubly linked lists, fast & slow pointers");
        Skill stacks  = saveSkill("Stacks & Queues",        dsa,      Difficulty.BEGINNER,     "Stack/queue ops, monotonic stack, deque");
        Skill trees   = saveSkill("Trees & Binary Search",  dsa,      Difficulty.INTERMEDIATE, "BST, AVL, BFS/DFS traversals");
        Skill dp      = saveSkill("Dynamic Programming",    dsa,      Difficulty.ADVANCED,     "Memoisation, tabulation, classical DP problems");
        Skill graphs  = saveSkill("Graphs",                 dsa,      Difficulty.ADVANCED,     "BFS, DFS, Dijkstra, Union-Find");
        // Backend
        Skill spring  = saveSkill("Spring Boot",            backend,  Difficulty.INTERMEDIATE, "REST APIs, Security, JPA, Actuator");
        Skill mysql   = saveSkill("MySQL & SQL",            backend,  Difficulty.BEGINNER,     "DDL/DML, joins, indexing, transactions");
        Skill sysdes  = saveSkill("System Design",          backend,  Difficulty.ADVANCED,     "Load balancing, caching, microservices");
        Skill restApi = saveSkill("REST API Design",        backend,  Difficulty.INTERMEDIATE, "HTTP methods, versioning, pagination");
        // Frontend
        Skill html    = saveSkill("HTML & CSS",             frontend, Difficulty.BEGINNER,     "Flexbox, Grid, responsive design");
        Skill js      = saveSkill("JavaScript",             frontend, Difficulty.INTERMEDIATE, "ES6+, async/await, closures, event loop");
        Skill react   = saveSkill("React",                  frontend, Difficulty.INTERMEDIATE, "Hooks, state management, lifecycle");
        // DevOps
        Skill docker  = saveSkill("Docker",                 devops,   Difficulty.INTERMEDIATE, "Images, containers, Dockerfile, compose");
        Skill linux   = saveSkill("Linux & Shell Scripting",devops,   Difficulty.BEGINNER,     "File system, permissions, cron, bash");
        // Core CS
        Skill os      = saveSkill("Operating Systems",      coreCs,   Difficulty.INTERMEDIATE, "Processes, threads, memory management");
        Skill nets    = saveSkill("Computer Networks",      coreCs,   Difficulty.INTERMEDIATE, "TCP/IP, HTTP, DNS, OSI model");
        Skill dbms    = saveSkill("DBMS Concepts",          coreCs,   Difficulty.INTERMEDIATE, "Normalisation, ACID, indexing");

        // ── 4. Progress – Khushwant (DSA + Backend focused) ──────────────────
        saveProgress(khush, arrays, 100.0, 42.0, daysAgo(1));
        saveProgress(khush, lists,   85.0, 34.0, daysAgo(2));
        saveProgress(khush, stacks,  70.0, 28.0, daysAgo(3));
        saveProgress(khush, trees,   55.0, 22.0, daysAgo(5));
        saveProgress(khush, dp,      20.0,  8.0, daysAgo(10));
        saveProgress(khush, graphs,  10.0,  4.0, daysAgo(15));
        saveProgress(khush, spring,  65.0, 26.0, daysAgo(4));
        saveProgress(khush, mysql,   80.0, 32.0, daysAgo(2));
        saveProgress(khush, os,      40.0, 16.0, daysAgo(7));
        saveProgress(khush, nets,    35.0, 14.0, daysAgo(8));

        // ── 4. Progress – Priya (Frontend focused) ────────────────────────────
        saveProgress(priya, html,    100.0, 40.0, daysAgo(1));
        saveProgress(priya, js,       75.0, 30.0, daysAgo(2));
        saveProgress(priya, react,    50.0, 20.0, daysAgo(3));
        saveProgress(priya, arrays,   45.0, 18.0, daysAgo(6));
        saveProgress(priya, mysql,    60.0, 24.0, daysAgo(4));
        saveProgress(priya, restApi,  30.0, 12.0, daysAgo(9));
        saveProgress(priya, dbms,     25.0, 10.0, daysAgo(12));

        // ── 4. Progress – Rahul (DevOps focused) ─────────────────────────────
        saveProgress(rahul, linux,    90.0, 36.0, daysAgo(1));
        saveProgress(rahul, docker,   55.0, 22.0, daysAgo(3));
        saveProgress(rahul, mysql,    70.0, 28.0, daysAgo(2));
        saveProgress(rahul, arrays,   25.0, 10.0, daysAgo(14));
        saveProgress(rahul, os,       40.0, 16.0, daysAgo(5));
        saveProgress(rahul, nets,     50.0, 20.0, daysAgo(4));

        // ── 5. Activity Logs – Khushwant ──────────────────────────────────────
        saveActivity(khush, arrays, ActivityType.PRACTICE,  60, "Solved 5 LeetCode array problems",              daysAgo(1));
        saveActivity(khush, arrays, ActivityType.PRACTICE,  90, "Two-pointer & sliding window patterns",          daysAgo(3));
        saveActivity(khush, lists,  ActivityType.READING,   45, "Read chapter on linked list reversal",           daysAgo(2));
        saveActivity(khush, lists,  ActivityType.PRACTICE,  60, "Implemented doubly linked list from scratch",    daysAgo(4));
        saveActivity(khush, stacks, ActivityType.PRACTICE,  45, "Monotonic stack problems",                       daysAgo(3));
        saveActivity(khush, trees,  ActivityType.COURSE,    90, "Watched tree traversal video series",            daysAgo(5));
        saveActivity(khush, trees,  ActivityType.PRACTICE,  60, "Solved BFS/DFS tree problems",                  daysAgo(6));
        saveActivity(khush, dp,     ActivityType.READING,   30, "Read DP intro, understood memoisation",          daysAgo(10));
        saveActivity(khush, dp,     ActivityType.PRACTICE,  45, "Attempted coin change problem",                  daysAgo(11));
        saveActivity(khush, graphs, ActivityType.READING,   30, "Graph representation: adjacency list vs matrix", daysAgo(15));
        saveActivity(khush, spring, ActivityType.PROJECT,  120, "Built REST API with Spring Boot for portfolio",  daysAgo(4));
        saveActivity(khush, spring, ActivityType.PRACTICE,  60, "Implemented JWT auth with Spring Security",      daysAgo(5));
        saveActivity(khush, mysql,  ActivityType.PRACTICE,  45, "Practised complex JOIN queries",                 daysAgo(2));
        saveActivity(khush, mysql,  ActivityType.REVISION,  30, "Revised indexing and query optimisation",        daysAgo(3));
        saveActivity(khush, os,     ActivityType.READING,   60, "Read about process scheduling algorithms",       daysAgo(7));
        saveActivity(khush, nets,   ActivityType.READING,   45, "Studied TCP/IP and HTTP deep dive",              daysAgo(8));

        // ── 5. Activity Logs – Priya ──────────────────────────────────────────
        saveActivity(priya, html,   ActivityType.PRACTICE,  60, "Built responsive landing page with Flexbox",    daysAgo(1));
        saveActivity(priya, html,   ActivityType.PROJECT,   90, "Converted Figma design to HTML/CSS",            daysAgo(2));
        saveActivity(priya, js,     ActivityType.COURSE,    60, "Completed async/await section of JS course",    daysAgo(2));
        saveActivity(priya, js,     ActivityType.PRACTICE,  45, "Solved closure and hoisting exercises",         daysAgo(3));
        saveActivity(priya, react,  ActivityType.COURSE,    90, "Followed React hooks tutorial",                 daysAgo(3));
        saveActivity(priya, react,  ActivityType.PROJECT,  120, "Built a todo app with useState and useEffect",  daysAgo(4));
        saveActivity(priya, arrays, ActivityType.PRACTICE,  45, "Solved easy array problems on LeetCode",        daysAgo(6));
        saveActivity(priya, mysql,  ActivityType.PRACTICE,  60, "Practised SQL joins on HackerRank",             daysAgo(4));
        saveActivity(priya, restApi,ActivityType.READING,   30, "Read REST best practices and status codes",     daysAgo(9));
        saveActivity(priya, dbms,   ActivityType.READING,   45, "Studied database normalisation (1NF–3NF)",      daysAgo(12));

        // ── 5. Activity Logs – Rahul ──────────────────────────────────────────
        saveActivity(rahul, linux,  ActivityType.PRACTICE,  60, "Wrote bash scripts for file automation",        daysAgo(1));
        saveActivity(rahul, linux,  ActivityType.COURSE,    90, "Completed Linux permission and cron section",   daysAgo(2));
        saveActivity(rahul, docker, ActivityType.PRACTICE,  60, "Dockerised a Spring Boot app",                  daysAgo(3));
        saveActivity(rahul, docker, ActivityType.READING,   45, "Read Docker networking documentation",          daysAgo(4));
        saveActivity(rahul, mysql,  ActivityType.PRACTICE,  60, "Practised SQL aggregation queries",             daysAgo(2));
        saveActivity(rahul, arrays, ActivityType.PRACTICE,  30, "Attempted easy array problems — needs work",    daysAgo(14));
        saveActivity(rahul, os,     ActivityType.READING,   45, "Read chapter on memory management",             daysAgo(5));
        saveActivity(rahul, nets,   ActivityType.COURSE,    60, "Watched OSI model and TCP/IP video",            daysAgo(4));

        // ── 6. Roadmap Steps – Khushwant ──────────────────────────────────────
        saveStep(khush, graphs, 1, "Start: Graphs",              "Begin with BFS, DFS and graph representation.",              14, false);
        saveStep(khush, dp,     2, "Focus: Dynamic Programming", "Start with recursion → memoisation → tabulation.",           10, false);
        saveStep(khush, trees,  3, "Build: Trees",               "Work on AVL trees and advanced BFS/DFS problems.",            5, false);
        saveStep(khush, stacks, 4, "Build: Stacks & Queues",     "Tackle monotonic stack and deque patterns.",                  4, false);
        saveStep(khush, spring, 5, "Build: Spring Boot",         "Add caching, exception handling and unit tests.",             5, false);
        saveStep(khush, os,     6, "Build: Operating Systems",   "Study synchronisation, deadlocks and virtual memory.",        8, false);
        saveStep(khush, nets,   7, "Build: Computer Networks",   "Study TLS, HTTP/2, websockets and network security.",         9, false);
        saveStep(khush, lists,  8, "Polish: Linked Lists",       "Tackle hard linked list and in-place reversal problems.",     2, true);
        saveStep(khush, mysql,  9, "Polish: MySQL & SQL",        "Focus on query optimisation and stored procedures.",          3, false);

        // ── 6. Roadmap Steps – Priya ──────────────────────────────────────────
        saveStep(priya, dbms,   1, "Focus: DBMS Concepts",     "Study normalisation, ACID and indexing thoroughly.",           10, false);
        saveStep(priya, restApi,2, "Build: REST API Design",   "Learn versioning, HATEOAS and rate limiting.",                  7, false);
        saveStep(priya, arrays, 3, "Build: Arrays & Strings",  "Move to medium/hard two-pointer and sliding window problems.",  6, false);
        saveStep(priya, react,  4, "Build: React",             "Learn Redux, React Query and performance optimisation.",        7, false);
        saveStep(priya, mysql,  5, "Build: MySQL & SQL",       "Practice window functions and subqueries.",                    5, false);
        saveStep(priya, js,     6, "Polish: JavaScript",       "Study the event loop deeply and design patterns.",             3, false);
        saveStep(priya, html,   7, "Polish: HTML & CSS",       "Already mastered! Consider CSS animations next.",              1, true);

        // ── 6. Roadmap Steps – Rahul ──────────────────────────────────────────
        saveStep(rahul, arrays, 1, "Focus: Arrays & Strings",       "Start solving easy array problems daily.",                10, false);
        saveStep(rahul, os,     2, "Build: Operating Systems",      "Study synchronisation primitives and virtual memory.",     8, false);
        saveStep(rahul, nets,   3, "Build: Computer Networks",      "Study TLS, HTTP/2 and network security.",                 7, false);
        saveStep(rahul, docker, 4, "Build: Docker",                 "Learn docker-compose, networking and multi-stage builds.", 6, false);
        saveStep(rahul, mysql,  5, "Build: MySQL & SQL",            "Tackle window functions and query optimisation.",          4, false);
        saveStep(rahul, linux,  6, "Polish: Linux & Shell Scripting","Write complex bash scripts and study systemd.",           2, false);

        log.info("DataSeeder: demo data seeded successfully. " +
                 "Users: 4 | Categories: 5 | Skills: 18 | " +
                 "Progress records: 23 | Activities: 40 | Roadmap steps: 22");
    }

    // ── Builder helpers ───────────────────────────────────────────────────────

    private User saveUser(String name, String email, String password, Role role) {
        return userRepository.save(User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build());
    }

    private SkillCategory saveCategory(String name, String description) {
        return categoryRepository.save(SkillCategory.builder()
                .name(name)
                .description(description)
                .build());
    }

    private Skill saveSkill(String name, SkillCategory category,
                            Difficulty difficulty, String description) {
        return skillRepository.save(Skill.builder()
                .name(name)
                .category(category)
                .difficulty(difficulty)
                .description(description)
                .build());
    }

    private void saveProgress(User user, Skill skill,
                              double progress, double score,
                              LocalDateTime lastPracticed) {
        progressRepository.save(UserSkillProgress.builder()
                .user(user)
                .skill(skill)
                .progressPercentage(progress)
                .score(score)
                .lastPracticedAt(lastPracticed)
                .build());
    }

    private void saveActivity(User user, Skill skill, ActivityType type,
                              int minutes, String notes,
                              LocalDateTime createdAt) {
        ActivityLog log = ActivityLog.builder()
                .user(user)
                .skill(skill)
                .activityType(type)
                .timeSpentMinutes(minutes)
                .notes(notes)
                .build();
        // Set createdAt manually to simulate historical data
        log.setCreatedAt(createdAt);
        log.setUpdatedAt(createdAt);
        activityLogRepository.save(log);
    }

    private void saveStep(User user, Skill skill, int order,
                          String title, String description,
                          int estimatedDays, boolean completed) {
        roadmapStepRepository.save(RoadmapStep.builder()
                .user(user)
                .skill(skill)
                .stepOrder(order)
                .title(title)
                .description(description)
                .estimatedDays(estimatedDays)
                .completed(completed)
                .build());
    }

    private LocalDateTime daysAgo(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}
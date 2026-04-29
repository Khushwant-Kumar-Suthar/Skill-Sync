# SkillSync product vision

SkillSync’s goal is to become a “LeetCode-style” **practice + tracking + coaching** platform:

- **Practice**: DSA problem sets (by topic, difficulty, pattern)
- **Track**: solved count, streaks, time-to-solve, tags, confidence, notes
- **Learn**: curated learning paths for skills (frontend, backend, devops, etc.)
- **Recommend**: personalized next problems + next skills based on gaps and goals

## Core pillars

### 1) DSA / LeetCode mode

- Problem library with metadata: difficulty, tags, patterns, companies, constraints
- User attempts: status (todo/attempted/solved), runtime/space notes, language, code snippet, reflections
- Collections: “Top 150”, “Blind 75”, “Graphs week”, “Interview Prep”

### 2) Skill graph

- Skills modeled as a DAG (prerequisites)
- Evidence: projects, quizzes, reading, commits, solved problems mapped to skills
- Levels: beginner → intermediate → advanced (with measurable criteria)

### 3) Recommendations

Two output types:

- **Next practice**: problems to solve next (spaced repetition + weaknesses)
- **Next learning**: what to study/build next (prerequisite-aware)

## MVP milestones (suggested)

- **MVP-1**: CRUD for skills + DSA problems + attempts + simple recommendations
- **MVP-2**: learning paths + tagging/patterns + better analytics dashboards
- **MVP-3**: imports (LeetCode/Codeforces/GitHub), smarter ranking, streaks, reminders


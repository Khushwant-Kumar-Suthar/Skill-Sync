# SkillSync – Complete API Reference

Base URL: `http://localhost:8080`

All protected endpoints require:
```
Authorization: Bearer <token>
```

---

## 1. Auth  `/api/auth`

| Method | Endpoint            | Auth | Description          |
|--------|---------------------|------|----------------------|
| POST   | `/api/auth/register`   | ❌   | Register new user    |
| POST   | `/api/auth/login`   | ❌   | Login, get JWT token |

### Register
```json
POST /api/auth/register
{
  "name": "Khushwant",
  "email": "khush@example.com",
  "password": "secret123"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "khush@example.com",
  "password": "secret123"
}
// Response:
{
  "token": "eyJ...",
  "type": "Bearer",
  "email": "khush@example.com",
  "role": "USER"
}
```

---

## 2. User Profile  `/api/user`

| Method | Endpoint                    | Auth | Description              |
|--------|-----------------------------|------|--------------------------|
| GET    | `/api/user/profile`            | ✅   | Get own profile          |
| PUT    | `/api/user/profile`            | ✅   | Update display name      |
| PUT    | `/api/user/change-password`     | ✅   | Change password          |

### Update Profile
```json
PUT /api/user/profile
{ "name": "Khushwant Suthar" }
```

### Change Password
```json
PUT /api/user/change-password
{
  "currentPassword": "secret123",
  "newPassword": "newSecret456",
  "confirmPassword": "newSecret456"
}
```

---

## 3. Skills  `/api/skills`  *(ADMIN only)*

| Method | Endpoint                                   | Auth        | Description             |
|--------|--------------------------------------------|-------------|-------------------------|
| POST   | `/api/skills/createSkill`                      | ADMIN only  | Create a new skill      |
| GET    | `/api/skills/getAllSkills`                     | ADMIN only  | Paginated skill list    |

### Create Skill
```json
POST /api/skills/createSkill
{
  "name": "Data Structures",
  "categoryId": 1,
  "difficulty": "INTERMEDIATE",
  "description": "Arrays, LinkedLists, Trees, Graphs"
}
// difficulty: BEGINNER | INTERMEDIATE | ADVANCED
```

### Get All Skills
```
GET /api/skills/getAllSkills?keyword=data&page=0&size=5&sortBy=name
```

---

## 4. Categories  `/api/categories`  *(ADMIN only)*

| Method | Endpoint                          | Auth       | Description                  |
|--------|-----------------------------------|------------|------------------------------|
| POST   | `/api/categories/createCategory`  | ADMIN only | Create a skill category      |
| GET    | `/api/categories/getAllCategories` | ADMIN only | All categories with skills   |
| GET    | `/api/categories/with-skills`     | ADMIN only | Same as above                |

### Create Category
```json
POST /api/categories/createCategory
{ "name": "DSA", "description": "Data Structures and Algorithms" }
```

---

## 5. Activities  `/api/activities`

| Method | Endpoint               | Auth | Description            |
|--------|------------------------|------|------------------------|
| POST   | `/api/activities/logBook` | ✅   | Log a practice session |

### Log Activity
```json
POST /api/activities/logBook
{
  "skillId": 1,
  "timeSpentMinutes": 45
}
// Automatically updates UserSkillProgress for that skill.
// Score += timeSpent * 0.1
// Progress += timeSpent * 0.05  (capped at 100%)
```

---

## 6. Progress  `/api/progress`

| Method | Endpoint                 | Auth | Description                    |
|--------|--------------------------|------|--------------------------------|
| GET    | `/api/progress/getProgress` | ✅   | All skill progress for the user |

### Response
```json
{
  "success": true,
  "message": "Progress fetched successfully",
  "data": [
    {
      "skillId": 1,
      "skillName": "Data Structures",
      "progressPercentage": 42.5,
      "score": 18.0,
      "lastPracticedAt": "2026-04-19T14:30:00"
    }
  ]
}
```

---

## 7. Recommendations  `/api/recommendations`

| Method | Endpoint             | Auth | Description                          |
|--------|----------------------|------|--------------------------------------|
| GET    | `/api/recommendations` | ✅   | Prioritised skill recommendations    |

### Response
```json
{
  "data": [
    {
      "skillId": 2,
      "skillName": "System Design",
      "currentProgress": 0.0,
      "currentScore": 0.0,
      "reason": "You haven't started practicing this skill yet. Begin today!",
      "priority": "HIGH",
      "lastPracticedAt": "Never"
    }
  ]
}
// priority: HIGH | MEDIUM | LOW
// Sorted: HIGH first, then by lowest progress
```

---

## 8. Roadmap  `/api/roadmap`

| Method | Endpoint                              | Auth | Description                       |
|--------|---------------------------------------|------|-----------------------------------|
| GET    | `/api/roadmap`                        | ✅   | Get / auto-generate roadmap       |
| PATCH  | `/api/roadmap/steps/{stepId}/complete`| ✅   | Mark a roadmap step as completed  |

### Get Roadmap Response
```json
{
  "data": {
    "userName": "Khushwant",
    "totalSteps": 4,
    "completedSteps": 1,
    "overallProgressPercent": 25.0,
    "steps": [
      {
        "stepId": 1,
        "stepOrder": 1,
        "skillId": 2,
        "skillName": "System Design",
        "title": "Start: System Design",
        "description": "You haven't touched this skill yet...",
        "estimatedDays": 14,
        "completed": false
      }
    ]
  }
}
```

### Mark Step Complete
```
PATCH /api/roadmap/steps/1/complete
// No body needed
```

---

## 9. Dashboard  `/api/dashboard`

| Method | Endpoint          | Auth | Description                       |
|--------|-------------------|------|-----------------------------------|
| GET    | `/api/dashboard`  | ✅   | Full analytics snapshot for user  |

### Response
```json
{
  "data": {
    "userName": "Khushwant",
    "totalSkillsTracked": 5,
    "skillsMastered": 1,
    "skillsInProgress": 3,
    "skillsNotStarted": 1,
    "totalScore": 94.5,
    "averageProgress": 48.2,
    "totalActivitiesLogged": 12,
    "totalMinutesPracticed": 540,
    "topSkillName": "Java",
    "topSkillProgress": 88.0,
    "skillBreakdown": [...]
  }
}
```

---

## 10. Admin Panel  `/api/admin`  *(ADMIN only)*

| Method | Endpoint                           | Auth       | Description                  |
|--------|------------------------------------|------------|------------------------------|
| GET    | `/api/admin/users`                 | ADMIN only | Paginated user list + stats  |
| GET    | `/api/admin/stats`                 | ADMIN only | Platform-wide stats          |
| PATCH  | `/api/admin/users/{userId}/promote`| ADMIN only | Promote user to ADMIN        |
| DELETE | `/api/admin/users/{userId}`        | ADMIN only | Delete a user                |

### Get All Users
```
GET /api/admin/users?page=0&size=10&sortBy=name
```

### Platform Stats Response
```json
{
  "data": {
    "totalUsers": 42,
    "totalSkills": 18,
    "totalCategories": 5,
    "totalActivitiesLogged": 310,
    "totalMinutesPracticed": 14200,
    "platformAverageProgress": 37.6
  }
}
```

---

## Error Response Format

All errors return the same shape:
```json
{
  "success": false,
  "message": "Invalid email or password",
  "errorCode": "INVALID_CREDENTIALS",
  "timestamp": "2026-04-20T10:30:00"
}
```

| HTTP Status | errorCode              | Meaning                          |
|-------------|------------------------|----------------------------------|
| 400         | `EMAIL_ALREADY_EXISTS` | Register with existing email     |
| 400         | `VALIDATION_ERROR`     | Request body validation failed   |
| 400         | `INVALID_PASSWORD`     | Wrong current password           |
| 400         | `PASSWORD_MISMATCH`    | New vs confirm password mismatch |
| 400         | `SAME_PASSWORD`        | New = current password           |
| 400         | `ALREADY_ADMIN`        | User already has ADMIN role      |
| 401         | `INVALID_CREDENTIALS`  | Wrong email or password on login |
| 403         | `ACCESS_DENIED`        | Insufficient role for endpoint   |
| 404         | `USER_NOT_FOUND`       | User does not exist              |
| 404         | `SKILL_NOT_FOUND`      | Skill ID does not exist          |
| 404         | `CATEGORY_NOT_FOUND`   | Category ID does not exist       |
| 404         | `STEP_NOT_FOUND`       | Roadmap step does not exist      |
| 500         | `INTERNAL_ERROR`       | Unexpected server error          |

---

## Complete Module Structure

```
<<<<<<< HEAD
com.skillsync.backend
=======
com.skillsync
>>>>>>> 70dc019 (logging and audit technique implemented using log folder)
├── auth/                      ← Register & Login
├── user/                      ← Profile, change password
├── skill/                     ← Skill CRUD (admin)
│   ├── category/              ← Skill categories (admin)
│   └── progress/              ← UserSkillProgress entity & repo
├── activity/                  ← Log practice sessions
├── recommendation/            ← Rule-based recommendations
├── roadmap/                   ← Auto-generated learning roadmap
├── dashboard/                 ← Analytics snapshot
├── admin/                     ← Admin panel
└── common/
    ├── config/SecurityConfig  ← JWT + endpoint auth rules
    ├── security/              ← JwtUtil, JwtFilter, UserDetails
    ├── exception/             ← Global error handler
    ├── response/ApiResponse   ← Unified response wrapper
    └── util/                  ← ResponseUtil, BaseEntity
```

---

## 📘 Smart Recipe Generator — Backend (v1)

### 🚀 Overview

This is the **backend service** for the Smart Recipe Generator project.
It’s built with **Spring Boot + MongoDB Atlas** and provides APIs for:

* 🥗 Ingredient-based recipe suggestions
* 🔍 Filtering recipes by diet, difficulty, and cooking time
* ⚙️ Easily extendable for future AI integration (Gemini, etc.)

---

## 🏗️ Tech Stack

| Component             | Technology                      |
| --------------------- | ------------------------------- |
| **Backend Framework** | Spring Boot (v3.5.x)            |
| **Database**          | MongoDB Atlas (Cloud)           |
| **Build Tool**        | Maven                           |
| **Language**          | Java 17+                        |
| **IDE**               | IntelliJ IDEA Community Edition |
| **Testing Tool**      | Postman                         |

---

## 📂 Project Structure

```
recipe-backend/
 ┣ src/
 ┃ ┣ main/
 ┃ ┃ ┣ java/com/example/recipe/
 ┃ ┃ ┃ ┣ controller/     → REST API controllers
 ┃ ┃ ┃ ┣ dto/            → Request/response DTOs
 ┃ ┃ ┃ ┣ model/          → MongoDB document models
 ┃ ┃ ┃ ┣ repository/     → MongoRepository interfaces
 ┃ ┃ ┃ ┣ service/        → Business logic layer
 ┃ ┃ ┃ ┗ RecipeBackendApplication.java
 ┃ ┃ ┣ resources/
 ┃ ┃ ┃ ┣ application.properties
 ┃ ┃ ┃ ┗ (static/templates - optional)
 ┣ pom.xml               → Project dependencies
 ┗ README.md              → Documentation (this file)
```

---

## ⚙️ Current Features (as of now)

### ✅ 1. MongoDB Integration

* Connected to **MongoDB Atlas Cloud Cluster**
* Configured via `application.properties`
* Auto-seeds 15 sample recipes on startup (`SeedData.java`)

### ✅ 2. Ingredient-Based Recipe Matching API

**Endpoint:**

```
POST /api/recipes/find
```

**Request Body Options:**

```json
{
  "ingredients": ["tomato", "egg"]
}
```

or

```json
{
  "ingredientsText": "tomato, egg"
}
```

**Response Example:**

```json
[
  {
    "recipe": { "name": "Egg Bhurji", "difficulty": "easy", "calories": 210 },
    "score": 0.33
  }
]
```

**Features:**

* Matches recipes based on common ingredients
* Returns top N matches (default: 5)
* Supports both list & text input

---

### ✅ 3. Recipe Filtering API

**Endpoint:**

```
GET /api/recipes/filter
```

**Query Params (optional):**

| Param        | Type    | Example                     | Description                      |
| ------------ | ------- | --------------------------- | -------------------------------- |
| `diet`       | String  | vegetarian / non-vegetarian | Filter by diet tag               |
| `difficulty` | String  | easy / medium / hard        | Filter by recipe difficulty      |
| `maxTime`    | Integer | 20                          | Filter by cooking time (minutes) |

**Examples:**

```
GET /api/recipes/filter?diet=vegetarian
GET /api/recipes/filter?difficulty=easy&maxTime=20
GET /api/recipes/filter?diet=non-vegetarian&difficulty=medium&maxTime=30
```

**Response Example:**

```json
[
  {
    "name": "Simple Omelette",
    "dietTags": ["non-vegetarian"],
    "difficulty": "easy",
    "timeMinutes": 10
  }
]
```

---

### ✅ 4. Health Check Endpoint

```
GET /api/recipes/ping
```

Response:

```
recipe service alive
```

---

## 🧠 Architecture Summary

* **Controller Layer:** Handles HTTP requests/responses
* **Service Layer:** Business logic (matching & filtering)
* **Repository Layer:** MongoDB operations using `MongoRepository`
* **Model Layer:** Recipe document structure
* **Seed Layer:** Auto-generates sample data at startup

---

## 🧪 Tested API Endpoints

| Method | Endpoint              | Description                            |
| ------ | --------------------- | -------------------------------------- |
| `GET`  | `/api/recipes/ping`   | Health check                           |
| `POST` | `/api/recipes/find`   | Find recipes by ingredients            |
| `GET`  | `/api/recipes/filter` | Filter recipes by diet/difficulty/time |

---

## 📦 Future Scope

✅ Integrate **Gemini API** for natural language recipe suggestions
✅ Add **image-to-ingredient recognition**
✅ User Authentication (Google OAuth planned)
✅ Save & rate recipes (user preferences)

---

## 🧰 Run Locally

### Prerequisites:

* Java 17+
* Maven
* MongoDB Atlas connection string in `application.properties`

### Start the app:

```bash
mvn spring-boot:run
```

or via IntelliJ → ▶ `RecipeBackendApplication`

---

## 🌐 Example Local URLs

* Health Check → [http://localhost:8080/api/recipes/ping](http://localhost:8080/api/recipes/ping)
* Filter → [http://localhost:8080/api/recipes/filter?difficulty=easy](http://localhost:8080/api/recipes/filter?difficulty=easy)
* Ingredient Match → POST [http://localhost:8080/api/recipes/find](http://localhost:8080/api/recipes/find)

---
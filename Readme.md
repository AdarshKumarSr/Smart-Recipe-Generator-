
---

#  **Smart Recipe Generator - Backend (v2)**

### **Spring Boot + MongoDB + Gemini AI + Google OAuth + Docker**

🔗 **Live API Base URL:**
👉 [https://smart-recipe-generator-qs0a.onrender.com/](https://smart-recipe-generator-qs0a.onrender.com/)

This is the backend of **Smart Recipe Generator** — an AI-powered cooking assistant that helps users discover, filter, and generate recipes intelligently.

It includes:

* 🥗 Ingredient-based smart matching
* 🔍 Advanced recipe filtering
* 🤖 AI-powered recipe generator (Gemini Fallback System)
* 🔐 Google OAuth Login + JWT
* 🐳 Dockerized for easy cloud deployment
* ☁️ Fully deployed on **Render**

---

# 🏗️ **Tech Stack**

| Component  | Technology                  |
| ---------- | --------------------------- |
| Backend    | **Spring Boot 3.5.x**       |
| Database   | **MongoDB Atlas**           |
| AI         | **Google Gemini 2.0 Flash** |
| Auth       | **Google OAuth + JWT**      |
| Build Tool | **Maven**                   |
| JVM        | **Java 17**                 |
| Deployment | **Docker + Render**         |
| Cloud DB   | MongoDB Atlas               |

---

# 📦 **Core Features**

### ✅ Ingredient-Based Recipe Matching

Find best recipe matches using score-based similarity.

### ✅ AI Recipe Generator (Auto-Fallback)

If DB has no match → Gemini generates a structured recipe.

### ✅ Advanced Filtering

Supports:

* diet
* difficulty
* cuisine
* maxTime
* tags
* minRating

### ✅ Google Sign-In + JWT

Secure authentication using Google ID token.

### ✅ Full Docker Support

Production-ready Dockerfile for Render, AWS, DigitalOcean, etc.

---

# 📁 **Project Structure**

```
recipe-backend/
 ┣ controller/
 ┣ dto/
 ┣ model/
 ┣ repository/
 ┣ service/
 ┣ SeedData.java
 ┣ Dockerfile
 ┣ pom.xml
 ┗ README.md
```

---

# 🌐 **API Endpoints**

---

## 🔹 1. Health Check

**GET** `/api/recipes/ping`

Response:

```
recipe service alive ✅
```

---

## 🔹 2. Ingredient-Based Recipe Matching

DB → returns **list**
AI → returns **single object**

**POST** `/api/recipes/find`

### Request Body

```json
{
  "ingredients": ["tomato", "egg"]
}
```

### DB Response Example

```json
[
  {
    "recipe": { "name": "Egg Bhurji", ... },
    "score": 0.52
  }
]
```

### AI Response Example

```json
{
  "recipe": { ... },
  "score": 0.64
}
```

---

## 🔹 3. AI Recipe Generator (Direct)

**POST** `/api/recipes/ai-recipe`

Request:

```json
{
  "ingredients": ["pineapple", "cheese", "coriander"]
}
```

Response:

```json
{
  "recipe": {
    "id": "generated-123",
    "name": "Pineapple Fusion Curry",
    ...
  },
  "score": 0.88
}
```

---

## 🔹 4. Filter Recipes

**GET** `/api/recipes/filter`

Example:

```
/api/recipes/filter?diet=vegetarian&difficulty=easy&maxTime=20
```

---

## 🔹 5. Google OAuth Login

**POST** `/api/auth/google`

Request:

```json
{ "token": "GOOGLE_ID_TOKEN" }
```

---

# 🤖 **AI Output Schema**

Gemini always returns this guaranteed JSON format:

```json
{
  "recipe": {
    "id": "string",
    "name": "string",
    "ingredients": ["string"],
    "timeMinutes": 0,
    "difficulty": "easy",
    "dietTags": ["string"],
    "calories": 0,
    "protein": 0,
    "instructions": "string",
    "imageUrl": "string",
    "youtubeLink": "string",
    "cuisine": "string",
    "rating": 0,
    "reviewsCount": 0,
    "tags": ["string"],
    "prepTime": "string",
    "servingSize": "string"
  },
  "score": 0.0
}
```

---

# 🧰 **Local Development**

### Build

```bash
./mvnw clean package
```

### Run

```bash
java -jar target/recipe-backend-0.0.1-SNAPSHOT.jar
```

or

```bash
mvn spring-boot:run
```

---

# 🐳 **Docker (Production Ready)**

### Build Image

```bash
docker build -t recipe-api .
```

### Run Container

```bash
docker run -p 8080:8080 recipe-api
```

---

# 🌱 **Seed Data**

`SeedData.java` automatically loads **20+ real-world recipes** into MongoDB at startup.

---

# 🎯 **Future Enhancements**

* 🍽️ Weekly meal planner
* 📸 Upload image → detect ingredients (CV)
* ⭐ User favorites & rating system
* 🧬 Personalized AI recommendations

---

# ❤️ Author

Built with love, frustration, coffee, and countless debugging sessions ☕🔥
**— Adarsh**

---

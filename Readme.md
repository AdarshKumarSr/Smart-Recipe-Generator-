
---

# **Smart Recipe Generator Backend (v2.0)**

### **Spring Boot + MongoDB + Gemini AI + Google OAuth + Docker**

🔗 **Live Backend API:**
👉 [https://smart-recipe-generator.up.railway.app](https://smart-recipe-generator.up.railway.app)

🔗 **Live Frontend App:**
👉 [https://smart-recipe-generator-frontend-zoy3.onrender.com](https://smart-recipe-generator-frontend-zoy3.onrender.com)

👨‍💻 **Developer Portfolio:**
👉 [https://portfolio-e7gt.onrender.com/](https://portfolio-e7gt.onrender.com/)

---

# 🍳 **Overview**

The **Smart Recipe Generator Backend** powers an AI–driven cooking assistant that intelligently:

* Finds matching recipes using scoring logic
* Generates recipes with **Gemini AI** when no match is found
* Filters recipes using advanced parameters
* Handles **Google OAuth login** and JWT authentication
* Provides a **fast, production-ready REST API**
* Is fully containerized using **Docker**

---

# 🏗️ **Tech Stack**

| Component  | Technology                                         |
| ---------- | -------------------------------------------------- |
| Backend    | **Spring Boot 3.x**                                |
| Database   | **MongoDB Atlas**                                  |
| AI Model   | **Google Gemini 2.0 Flash (JSON Structured Mode)** |
| Auth       | **Google OAuth + JWT**                             |
| Build Tool | Maven                                              |
| Deployment | **Railway / Render**                               |
| Docker     | Containerized production build                     |
| Java       | **Java 17**                                        |

---

# ⭐ **Key Backend Features**

### 🚀 1. **Ingredient-Based Matching with Score**

Matches user input to recipes using similarity & scoring logic.

### 🤖 2. **AI Recipe Generator (Gemini)**

If DB cannot find a good match → auto-fallback to Gemini AI.

### 🎚️ 3. **Advanced Filtering**

Supports:

* diet
* difficulty
* cuisine
* tags
* maxTime
* minRating
* ingredient text parsing

### 🔐 4. **Google OAuth Login + JWT**

Secure user authentication system.

### 🛠️ 5. **Clean DTO-based API**

Structured responses & safe parsing of AI output.

### 🐳 6. **Dockerized**

Portable & production-ready.

---

# 📁 **Project Structure**

```
recipe-backend/
 ┣ controller/
 ┣ dto/
 ┣ model/
 ┣ repository/
 ┣ service/
 ┣ config/
 ┣ SeedData.java
 ┣ Dockerfile
 ┣ pom.xml
 ┗ README.md
```

---

# 🌐 **API Endpoints**

## 🩺 1. Health Check

**GET** `/api/recipes/ping`
Response:

```
recipe service alive
```

---

## 🍲 2. Unified Ingredient Matching

**POST:** `/api/recipes/find`

### **Request**

```json
{
  "ingredientsText": "egg tomato onion",
  "diet": "vegetarian",
  "difficulty": "easy",
  "cuisine": "indian",
  "tag": "spicy",
  "maxTime": 20,
  "minRating": 3
}
```

### **DB Response**

```json
[
  { "recipe": {...}, "score": 0.72 }
]
```

### **AI Fallback Response**

```json
{
  "aiSuggested": true
}
```

---

## 🤖 3. AI Recipe Generator

**POST:** `/api/recipes/ai-recipe`

Request:

```json
{ "ingredients": ["chicken", "butter", "garlic"] }
```

Response:

```json
{
  "recipe": { ... },
  "score": 0.88,
  "ai": true
}
```

---

## 🔎 4. Filter Recipes

**GET:** `/api/recipes/filter?diet=vegan&difficulty=easy&maxTime=15`

---

## 📚 5. Fetch All Recipes

**GET:** `/api/recipes`

---

# 🤖 **AI Output Schema (Guaranteed Format)**

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

# 🧪 **Local Development**

### Run Backend

```bash
mvn spring-boot:run
```

### Build JAR

```bash
mvn clean package
```

### Run JAR

```bash
java -jar target/*.jar
```

---

# 🐳 **Docker Deployment**

### Build Image

```bash
docker build -t recipe-backend .
```

### Run Container

```bash
docker run -p 8080:8080 recipe-backend
```

---

# 🌱 **Seed Data**

`SeedData.java` auto-populates **20+ curated recipes** into MongoDB.

---

# 🚧 **Future Enhancements**

* 📸 Ingredient detection from images
* ⭐ User favorites & rating system
* 🧬 Personalized AI recommendations
* 🍽️ Weekly meal planner

---

# 👨‍💻 **Author**

Built with ⚡ passion, ☕ coffee & 🧠 curiosity.

### **[Adarsh Kumar](https://portfolio-e7gt.onrender.com/)**

🔗 Portfolio & Projects
💼 SoftWare Developer (Java + React)

---


---
# Smart Recipe Generator Backend (v2.0)

**Tech Stack:** Spring Boot • MongoDB Atlas • Gemini AI • Google OAuth • Docker • Java 17

### Live Services
- **Backend API:** https://smart-recipe-generator.up.railway.app
- **Frontend Web App:** https://smart-recipe-generator-frontend-zoy3.onrender.com
- **Developer Portfolio:** https://portfolio-e7gt.onrender.com

---

## Overview

The **Smart Recipe Generator Backend** provides APIs for an AI-assisted recipe platform capable of:

- Searching recipes based on ingredient similarity and scoring
- Automatically generating recipes using **Gemini AI** when suitable results are unavailable
- Supporting recipe filtering through multiple search parameters
- Handling secure authentication via **Google OAuth + JWT**
- Delivering a production-ready, containerized backend

---

## Technology Overview

| Area        | Technology                                         |
|-------------|----------------------------------------------------|
| Backend     | Spring Boot 3.x                                    |
| Database    | MongoDB Atlas                                      |
| AI Model    | Gemini 2.0 Flash (Structured JSON Output)          |
| Auth        | Google OAuth + JWT                                 |
| Build Tool  | Maven                                              |
| Deployment  | Railway / Render                                   |
| Container   | Docker                                             |
| Language    | Java 17                                            |

---

## Key Features

1. **Ingredient-based recipe matching** using similarity scoring  
2. **AI-generated recipes** via Gemini fallback logic  
3. **Advanced filtering** (diet, difficulty, cuisine, tags, time, rating, etc.)  
4. **Google OAuth authentication** with JWT-based session management  
5. **DTO-driven API design** with validated structured responses  
6. **Containerized deployment** using Docker for scalability and portability  

---

## Project Structure

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

## API Endpoints

### 1️⃣ Health Check
**GET** `/api/recipes/ping`  
Response:
```

recipe service alive

````

---

### 2️⃣ Ingredient-Based Matching
**POST** `/api/recipes/find`  
**Request**
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
````

**Response (DB match)**

```json
[
  { "recipe": { ... }, "score": 0.72 }
]
```

**Response (AI fallback)**

```json
{ "aiSuggested": true }
```

---

### 3️⃣ AI Recipe Generator

**POST** `/api/recipes/ai-recipe`
**Request**

```json
{ "ingredients": ["chicken", "butter", "garlic"] }
```

**Response**

```json
{
  "recipe": { ... },
  "score": 0.88,
  "ai": true
}
```

---

### 4️⃣ Filter Recipes

**GET** `/api/recipes/filter?diet=vegan&difficulty=easy&maxTime=15`

### 5️⃣ Get All Recipes

**GET** `/api/recipes`

---

## AI Output Schema

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

## Local Development

### Run Application

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

## Docker Usage

### Build Image

```bash
docker build -t recipe-backend .
```

### Run Container

```bash
docker run -p 8080:8080 recipe-backend
```

---

## Seed Data

`SeedData.java` includes predefined sample recipes for initial database population.

---

## Planned Enhancements

* Image-based ingredient detection
* User favorites, reviews, and rating system
* Personalized AI recommendations
* Meal planning and nutrition tracking

---

## Author  © <a href="https://portfolio-e7gt.onrender.com/" target="_blank"><strong>Adarsh Kumar</strong></a>


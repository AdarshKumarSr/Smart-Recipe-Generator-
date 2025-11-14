package com.example.recipe;

import com.example.recipe.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class SeedData {
    public static List<Recipe> createSeed() {
        List<Recipe> list = new ArrayList<>();

        list.add(new Recipe(null, "Tomato Egg Curry",
                List.of("tomato","egg","onion","garlic","oil","salt"),
                25,"easy",List.of("non-vegetarian"),280,14,
                "Boil eggs, sauté onion & garlic in oil, add tomatoes and spices, mix eggs in curry and simmer 10 min.",
                "",
                "https://www.youtube.com/watch?v=Zg3CIC3-724",
                "Indian",4.6,124,
                List.of("spicy","home-style","protein-rich"),
                "10 min prep, 15 min cook","2 servings"
        ));

        list.add(new Recipe(null,"Simple Omelette",
                List.of("egg","salt","pepper","butter","onion"),
                10,"easy",List.of("non-vegetarian"),190,12,
                "Whisk eggs with salt & pepper, cook in butter with onions until set.",
                "https://www.allrecipes.com/thmb/m2T9GxkmoFNe-fqUtjW8WQjZ2aM=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/AR-basic-omelet-ddmfs-3x2-44df86da9d0643d0a285ff2b1e4a3d56.jpg",
                "https://www.youtube.com/watch?v=KJfLR1crY3o",
                "French",4.8,210,
                List.of("quick","breakfast","simple"),
                "5 min prep, 5 min cook","1 serving"
        ));

        list.add(new Recipe(null,"Tomato Soup",
                List.of("tomato","onion","garlic","butter","salt","pepper"),
                20,"easy",List.of("vegetarian"),150,4,
                "Cook tomatoes, onion & garlic, blend smooth, simmer with butter & seasoning.",
                "https://www.cookwithmanali.com/wp-content/uploads/2021/01/Tomato-Soup-500x500.jpg",
                "https://www.youtube.com/watch?v=ojkZE8mTSgo",
                "Continental",4.5,300,
                List.of("comfort","vegan","light"),
                "10 min prep, 10 min cook","2 servings"
        ));

        list.add(new Recipe(null,"Vegetable Fried Rice",
                List.of("rice","carrot","peas","onion","soy sauce","egg"),
                30,"medium",List.of("vegetarian"),350,8,
                "Sauté vegetables, add rice & soy sauce, stir-fry 5 min, add scrambled egg.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/05/veg-fried-rice.jpg",
                "https://www.youtube.com/watch?v=I29Dk0wSdaY",
                "Asian",4.7,500,
                List.of("quick","street-style","family-favorite"),
                "10 min prep, 20 min cook","3 servings"
        ));

        list.add(new Recipe(null,"Egg Bhurji",
                List.of("egg","onion","tomato","turmeric","salt","oil"),
                15,"easy",List.of("non-vegetarian"),210,13,
                "Sauté onion & tomato with turmeric, scramble eggs in mix, cook until soft.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/07/egg-bhurji-recipe.jpg",
                "https://www.youtube.com/watch?v=1dkLwFJiXq4",
                "Indian",4.9,890,
                List.of("spicy","street-food","quick"),
                "5 min prep, 10 min cook","2 servings"
        ));

        list.add(new Recipe(null,"Pasta with Tomato Sauce",
                List.of("pasta","tomato","garlic","olive oil","salt"),
                25,"easy",List.of("vegetarian"),400,10,
                "Boil pasta, cook garlic & tomato sauce, mix together & serve with cheese.",
                "https://www.loveandlemons.com/wp-content/uploads/2019/06/tomato-sauce.jpg",
                "https://www.youtube.com/watch?v=YNUnP-r0OW4",
                "Italian",4.7,650,
                List.of("classic","quick-dinner","comfort"),
                "10 min prep, 15 min cook","2 servings"
        ));

        list.add(new Recipe(null,"Paneer Butter Masala",
                List.of("paneer","tomato","cream","butter","spices","onion"),
                35,"medium",List.of("vegetarian"),420,16,
                "Cook tomato-onion gravy & blend, add butter, cream & paneer cubes.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/01/paneer-butter-masala-1.jpg",
                "https://www.youtube.com/watch?v=ztxeh7WZ7AI",
                "Indian",4.9,1100,
                List.of("rich","restaurant-style","north-indian"),
                "10 min prep, 25 min cook","3 servings"
        ));

        list.add(new Recipe(null,"Poha",
                List.of("flattened rice","onion","peanuts","mustard","turmeric"),
                15,"easy",List.of("vegetarian"),270,6,
                "Sauté onions & peanuts, add washed poha & spices, mix well & serve with lemon.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/03/poha-recipe-1.jpg",
                "https://www.youtube.com/watch?v=IfPZ7P1fA5g",
                "Indian",4.8,730,
                List.of("breakfast","mild","healthy"),
                "5 min prep, 10 min cook","2 servings"
        ));

        list.add(new Recipe(null,"Masala Maggi",
                List.of("noodles","onion","tomato","capsicum","spices"),
                10,"easy",List.of("vegetarian"),320,8,
                "Boil Maggi, cook veggies & spices, mix together & serve hot.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2022/09/maggi-noodles.jpg",
                "https://www.youtube.com/watch?v=K1vK7xQUemQ",
                "Indian",4.6,1500,
                List.of("instant","spicy","snack"),
                "5 min prep, 5 min cook","1 serving"
        ));

        list.add(new Recipe(null,"Aloo Paratha",
                List.of("wheat flour","potato","onion","spices","butter"),
                25,"medium",List.of("vegetarian"),380,9,
                "Stuff spiced mashed potato into dough, roll & cook with butter on tawa.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/04/aloo-paratha-recipe-1.jpg",
                "https://www.youtube.com/watch?v=2xgY3pO0O3w",
                "Indian",4.9,1400,
                List.of("stuffed","breakfast","punjabi"),
                "10 min prep, 15 min cook","2 servings"
        ));

        list.add(new Recipe(null, "Chicken Curry",
                List.of("chicken","onion","tomato","garlic","ginger","spices","oil"),
                40, "medium", List.of("non-vegetarian"), 480, 35,
                "Marinate chicken with spices, sauté onion, garlic, and tomato, add chicken, and simmer till cooked.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/07/chicken-curry-recipe.jpg",
                "https://www.youtube.com/watch?v=1t3NAi6Z2io",
                "Indian", 4.8, 2100,
                List.of("spicy", "gravy", "family-dinner"),
                "15 min prep, 25 min cook", "3 servings"
        ));

        list.add(new Recipe(null, "Veg Biryani",
                List.of("basmati rice","vegetables","spices","yogurt","onion","saffron"),
                45, "hard", List.of("vegetarian"), 420, 10,
                "Layer cooked rice and spiced veggies, steam on low flame until aromatic.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/07/vegetable-biryani-recipe.jpg",
                "https://www.youtube.com/watch?v=5rJx0Z9KdpI",
                "Indian", 4.9, 1800,
                List.of("fragrant","spicy","royal"),
                "15 min prep, 30 min cook", "3 servings"
        ));

        list.add(new Recipe(null, "Dal Tadka",
                List.of("lentils","onion","tomato","garlic","spices","ghee"),
                25, "easy", List.of("vegetarian","gluten-free"), 290, 12,
                "Boil lentils, temper with onion, tomato, garlic & spices in ghee.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/08/dal-tadka.jpg",
                "https://www.youtube.com/watch?v=RIFiYqR1nCw",
                "Indian", 4.8, 950,
                List.of("comfort","protein-rich","dhaba-style"),
                "10 min prep, 15 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Chole Masala",
                List.of("chickpeas","tomato","onion","garlic","spices"),
                35, "medium", List.of("vegetarian","gluten-free"), 360, 15,
                "Soak and boil chickpeas, prepare spicy tomato-onion gravy, mix and simmer.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/05/chole-recipe-1.jpg",
                "https://www.youtube.com/watch?v=yws7TjVZ2sQ",
                "Indian", 4.9, 2100,
                List.of("north-indian","spicy","vegan"),
                "10 min prep, 25 min cook", "3 servings"
        ));

        list.add(new Recipe(null, "Pancakes",
                List.of("flour","milk","egg","sugar","butter"),
                15, "easy", List.of("vegetarian"), 310, 8,
                "Mix batter, cook on pan till golden, serve with syrup or fruits.",
                "https://www.allrecipes.com/thmb/XtIrPYovl0bskYywhGnKKhgRt1Y=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/AR-basic-pancakes-ddmfs-3x2-41cf81a944a94b5dbe210ba3770d3db3.jpg",
                "https://www.youtube.com/watch?v=2u6A9cD3yFQ",
                "American", 4.8, 540,
                List.of("breakfast","sweet","fluffy"),
                "5 min prep, 10 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Grilled Cheese Sandwich",
                List.of("bread","cheese","butter","pepper"),
                10, "easy", List.of("vegetarian"), 280, 10,
                "Butter bread slices, fill cheese, grill until golden brown.",
                "https://www.seriouseats.com/thmb/Z7tUABuZbCh1d-3AGfUQKqMNjGw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/20220622-Grilled-Cheese-Amanda-Suarez-hero-76e27ff0d7af4f3c87e18a7c9b6484c8.jpg",
                "https://www.youtube.com/watch?v=2o4Q4m3w9xE",
                "American", 4.7, 850,
                List.of("quick","snack","cheesy"),
                "3 min prep, 7 min cook", "1 serving"
        ));

        list.add(new Recipe(null, "Aloo Gobi",
                List.of("potato","cauliflower","tomato","onion","spices"),
                25, "easy", List.of("vegetarian"), 240, 7,
                "Cook potato and cauliflower with tomato, onion & Indian spices.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/07/aloo-gobi-recipe-1.jpg",
                "https://www.youtube.com/watch?v=K3qQ0T2mHws",
                "Indian", 4.6, 1200,
                List.of("dry-curry","tiffin","home-style"),
                "10 min prep, 15 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Veg Burger",
                List.of("burger bun","potato patty","lettuce","cheese","sauce"),
                20, "easy", List.of("vegetarian"), 420, 11,
                "Assemble toasted buns, veggie patty, cheese, lettuce, and sauce.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/06/veg-burger.jpg",
                "https://www.youtube.com/watch?v=l9GEOMeyK7U",
                "American", 4.5, 1800,
                List.of("fast-food","cheesy","snack"),
                "10 min prep, 10 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Paneer Tikka",
                List.of("paneer","curd","spices","capsicum","onion"),
                25, "medium", List.of("vegetarian","gluten-free"), 370, 18,
                "Marinate paneer cubes in yogurt & spices, grill with veggies.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/08/paneer-tikka-1.jpg",
                "https://www.youtube.com/watch?v=wZfKnG2Sg4s",
                "Indian", 4.9, 1400,
                List.of("starter","grilled","protein-rich"),
                "10 min prep, 15 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Egg Fried Rice",
                List.of("rice","egg","soy sauce","spring onion","carrot"),
                20, "easy", List.of("non-vegetarian"), 360, 14,
                "Stir-fry rice with scrambled egg, veggies, soy sauce & spring onions.",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/07/egg-fried-rice.jpg",
                "https://www.youtube.com/watch?v=GvDwwwcf7mY",
                "Chinese", 4.7, 900,
                List.of("quick","street-style","kids-favorite"),
                "10 min prep, 10 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Vegetable Pulao",
                List.of("rice","peas","carrot","beans","spices"),
                30, "easy", List.of("vegetarian","gluten-free"), 320, 8,
                "Cook rice with veggies, whole spices, and ghee until aromatic.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/06/veg-pulao-recipe-1.jpg",
                "https://www.youtube.com/watch?v=EqpHKeJpU_o",
                "Indian", 4.8, 1700,
                List.of("mild","lunch","fragrant"),
                "10 min prep, 20 min cook", "2 servings"
        ));

        list.add(new Recipe(null, "Upma",
                List.of("semolina","onion","mustard","chili","ghee","spices"),
                20, "easy", List.of("vegetarian"), 270, 5,
                "Roast rava, cook with water, sauté onion & mustard, combine and serve.",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2021/03/upma-recipe-1.jpg",
                "https://www.youtube.com/watch?v=2W_TZL2cXpg",
                "Indian", 4.7, 600,
                List.of("breakfast","light","south-indian"),
                "5 min prep, 15 min cook", "2 servings"
        ));

        return list;
    }
}

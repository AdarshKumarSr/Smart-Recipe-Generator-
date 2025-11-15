package com.example.recipe;

import com.example.recipe.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class SeedData {
    public static List<Recipe> createSeed() {
        List<Recipe> list = new ArrayList<>();

        // 1. Creamy Butter Chicken
        list.add(new Recipe(null, "Creamy Butter Chicken",
                List.of("chicken","tomato puree","cream","butter","garlic","ginger","kasuri methi","garam masala","salt"),
                40, "medium", List.of("non-vegetarian"), 480, 32,
                "Marinate chicken, grill lightly, then simmer in a rich tomato-butter-cream gravy with aromatic spices until silky and smooth.",
                "https://www.adayinthekitchen.com/wp-content/uploads/2017/10/butter-chicken-2-1000x1000a.jpg",
                "https://www.youtube.com/watch?v=W1pEDnJQ7r4",
                "Indian", 4.9, 2100,
                List.of("creamy","restaurant-style","rich"),
                "15 min prep, 25 min cook",
                "3 servings"
        ));


// 2. Hakka Noodles
        list.add(new Recipe(null, "Hakka Noodles",
                List.of("noodles","soy sauce","vinegar","carrot","capsicum","cabbage","garlic","chili"),
                22, "easy", List.of("vegetarian"), 380, 9,
                "Boil noodles al dente, toss with stir-fried veggies on high flame, add sauces and chili oil for a smoky Indo-Chinese flavor.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdEFnbIbYL_GlhreXg_cN35Ws8nFZTvQ_9ObcRYL7gqfGztsJCoVYMHg4XPZx6e4uAWG7O1KX8GwFPcOrlOavgWQtCxSb6HiDLV6dUp20R&s=10",
                "https://www.youtube.com/watch?v=J3Qv8yA6RVA",
                "Indo-Chinese", 4.8, 1200,
                List.of("street-style","spicy","quick"),
                "10 min prep, 12 min cook",
                "2 servings"
        ));


// 3. Egg Masala Fry
        list.add(new Recipe(null, "Egg Masala Fry",
                List.of("egg","onion","tomato","garlic","curry leaves","chili","oil","turmeric"),
                15, "easy", List.of("non-vegetarian"), 260, 18,
                "Boiled eggs are pan-fried and then coated in a spicy onion-tomato masala with curry leaves for a quick protein-rich dish.",
                "https://www.simplyrecipes.com/thmb/K7ZFpvv5dtyf4519gxG7KwOIkAw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simple-Recipes-Egg-Masala-Curry-Lead-02-2c19ed89302e4bf9a87fda620ae45711.jpg",
                "https://www.youtube.com/watch?v=I-3Q2qkq8M0",
                "Indian", 4.7, 800,
                List.of("spicy","quick","protein-rich"),
                "5 min prep, 10 min cook",
                "2 servings"
        ));


// 4. Creamy Pesto Pasta
        list.add(new Recipe(null, "Creamy Pesto Pasta",
                List.of("pasta","pesto","cream","parmesan","garlic","olive oil"),
                20, "easy", List.of("vegetarian"), 450, 12,
                "Pasta tossed in basil pesto blended with cream and parmesan, creating a silky, luxurious Italian-style sauce.",
                "https://littlesunnykitchen.com/wp-content/uploads/2022/11/Chicken-Pesto-Pasta-1.jpg",
                "https://www.youtube.com/watch?v=sVrQXXVxvL8",
                "Italian", 4.7, 650,
                List.of("creamy","italian","quick"),
                "5 min prep, 15 min cook",
                "2 servings"
        ));


// 5. Chicken Biryani
        list.add(new Recipe(null, "Chicken Biryani",
                List.of("chicken","basmati rice","yogurt","spices","fried onions","ghee","saffron"),
                45, "hard", List.of("non-vegetarian"), 520, 28,
                "Layer marinated chicken with half-cooked rice, saffron milk, and fried onions; seal and cook on dum for rich aroma.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPcdlgGqTXb-qtbAK_WgscTy2TimgPjzn9fw&s",
                "https://www.youtube.com/watch?v=uygb9O4N8jY",
                "Indian", 4.9, 3400,
                List.of("royal","aromatic","festival"),
                "15 min prep, 30 min cook",
                "3 servings"
        ));


// 6. Veg Lasagna
        list.add(new Recipe(null, "Veg Lasagna",
                List.of("lasagna sheets","tomato sauce","béchamel","cheese","zucchini","spinach"),
                40, "medium", List.of("vegetarian"), 430, 14,
                "Pasta sheets layered with roasted veggies, creamy béchamel, and cheese, baked until golden and bubbly.",
                "https://cookieandkate.com/images/2017/12/vegetable-lasagna-recipe-4.jpg",
                "https://www.youtube.com/watch?v=WcR7S-zfHwg",
                "Italian", 4.6, 900,
                List.of("baked","cheesy","comfort"),
                "10 min prep, 30 min cook",
                "3 servings"
        ));


// 7. Shahi Paneer
        list.add(new Recipe(null, "Shahi Paneer",
                List.of("paneer","cashew","cream","onion","tomato","spices"),
                30, "medium", List.of("vegetarian"), 410, 15,
                "Paneer cubes cooked in a creamy cashew-cream gravy with gentle sweetness and Mughlai spices.",
                "https://indianshealthyrecipes.com/wp-content/uploads/2024/07/ShahiPaneer-3.jpg",
                "https://www.youtube.com/watch?v=xIuA8G1tM8A",
                "Indian", 4.9, 1800,
                List.of("creamy","royal","north-indian"),
                "10 min prep, 20 min cook",
                "3 servings"
        ));


// 8. Chicken Shawarma Wrap
        list.add(new Recipe(null, "Chicken Shawarma Wrap",
                List.of("chicken","pita","garlic sauce","lettuce","pickles","paprika"),
                25, "easy", List.of("non-vegetarian"), 450, 27,
                "Spiced grilled chicken wrapped with garlic toum, lettuce, and pickles in warm pita for a classic Middle Eastern flavor.",
                "https://foxeslovelemons.com/wp-content/uploads/2023/06/Chicken-Shawarma-8.jpg",
                "https://www.youtube.com/watch?v=q_Bslj7Y2H4",
                "Middle Eastern", 4.8, 1600,
                List.of("street-food","tangy","grilled"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 9. Thai Green Curry
        list.add(new Recipe(null, "Thai Green Curry",
                List.of("coconut milk","green curry paste","basil","bamboo shoots","veggies"),
                30, "medium", List.of("vegetarian"), 360, 13,
                "A fragrant coconut-based curry cooked with green curry paste, vegetables, and Thai basil for fresh aroma.",
                "https://www.kitchensanctuary.com/wp-content/uploads/2019/06/Thai-Green-Curry-square-FS.jpg",
                "https://www.youtube.com/watch?v=Ke4d3lKx-zA",
                "Thai", 4.7, 900,
                List.of("fragrant","coconut","spicy"),
                "10 min prep, 20 min cook",
                "2 servings"
        ));


// 10. Avocado Toast with Poached Egg
        list.add(new Recipe(null, "Avocado Toast with Poached Egg",
                List.of("avocado","egg","bread","chili flakes","lemon"),
                10, "easy", List.of("vegetarian"), 290, 12,
                "Toasted sourdough topped with seasoned avocado mash and a perfectly runny poached egg.",
                "https://www.aberdeenskitchen.com/wp-content/uploads/2019/05/Avocado-Egg-Breakfast-Toast-FI-Thumbnail-1200X1200.jpg",
                "https://www.youtube.com/watch?v=0fK9QJw1QCs",
                "American", 4.6, 650,
                List.of("healthy","breakfast","quick"),
                "5 min prep, 5 min cook",
                "1 serving"
        ));


// 11. Chana Masala
        list.add(new Recipe(null, "Chana Masala",
                List.of("chickpeas","tomato","onion","garlic","spices"),
                30, "easy", List.of("vegetarian","vegan","gluten-free"), 320, 14,
                "Chickpeas simmered in a spiced tomato-onion gravy with cumin, coriander, and garam masala.",
                "https://cdn77-s3.lazycatkitchen.com/wp-content/uploads/2020/01/easy-chana-masala-pan-1024x1536.jpg",
                "https://www.youtube.com/watch?v=1J2oA6cCBo8",
                "Indian", 4.9, 2400,
                List.of("vegan","spicy","north-indian"),
                "10 min prep, 20 min cook",
                "3 servings"
        ));


// 12. Veg Sushi Rolls
        list.add(new Recipe(null, "Veg Sushi Rolls",
                List.of("nori","sushi rice","vinegar","cucumber","avocado","tofu"),
                25, "medium", List.of("vegetarian","gluten-free"), 280, 6,
                "Japanese-style sushi filled with cucumber, avocado, and tofu, rolled tightly and served with soy sauce.",
                "https://foodbyjonister.com/wp-content/uploads/2018/02/IMG_5906-2.jpg",
                "https://www.youtube.com/watch?v=I1UDS2kgqY8",
                "Japanese", 4.7, 1100,
                List.of("fresh","light","japanese"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 13. Peri-Peri Grilled Chicken
        list.add(new Recipe(null, "Peri-Peri Grilled Chicken",
                List.of("chicken","peri-peri sauce","lemon","garlic"),
                30, "easy", List.of("non-vegetarian"), 300, 30,
                "Marinated chicken flame-grilled with tangy and spicy peri-peri sauce for smoky flavor.",
                "https://www.allrecipes.com/thmb/0SFDFGQUAAiJPbx5kDq7bSsiFuE=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/7507145PeriPeriChickenChefJohn4x3-6eed97091b5e485592fac34fcb77d501.jpg",
                "https://www.youtube.com/watch?v=V_E1B5nMDX4",
                "African-Portuguese", 4.8, 1300,
                List.of("spicy","grilled","tangy"),
                "10 min prep, 20 min cook",
                "2 servings"
        ));


// 14. Creamy Mushroom Soup
        list.add(new Recipe(null, "Creamy Mushroom Soup",
                List.of("mushrooms","cream","onion","garlic","butter"),
                25, "easy", List.of("vegetarian"), 250, 6,
                "Sautéed mushrooms blended into a velvety smooth soup simmered with cream and garlic.",
                "https://www.allrecipes.com/thmb/PKh_MtthZMtG1flNmud0MNgRK7w=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/13096-Cream-of-Mushroom-Soup-ddmfs-4x3-293-b505e37374d74e81807e8a93bcdd7bab.jpg",
                "https://www.youtube.com/watch?v=FjJXh6C5HMI",
                "Continental", 4.6, 700,
                List.of("creamy","cozy","soup"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 15. Mexican Burrito Bowl
        list.add(new Recipe(null, "Mexican Burrito Bowl",
                List.of("rice","beans","chicken","salsa","corn","avocado","cheese"),
                25, "easy", List.of("non-vegetarian"), 480, 28,
                "A layered bowl of spiced chicken, beans, rice, salsa, corn, cheese, and guacamole.",
                "https://cdn.loveandlemons.com/wp-content/uploads/2023/03/burrito-bowl.jpg",
                "https://www.youtube.com/watch?v=-CYawE1FJus",
                "Mexican", 4.8, 1600,
                List.of("spicy","filling","bowl-meal"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 16. Falafel Wrap
        list.add(new Recipe(null, "Falafel Wrap",
                List.of("chickpeas","herbs","pita","tahini","garlic"),
                20, "easy", List.of("vegetarian","vegan"), 390, 13,
                "Crispy chickpea falafel wrapped with tahini sauce and vegetables inside warm pita.",
                "https://www.mindful.sodexo.com/wp-content/uploads/2024/08/Mindful-Fall-Recipes_0007_MDF_FallHarvestFalafelWrap.jpg",
                "https://www.youtube.com/watch?v=Zp2QThH9u2Q",
                "Middle Eastern", 4.7, 900,
                List.of("vegan","street-food","crispy"),
                "10 min prep, 10 min cook",
                "2 servings"
        ));


// 17. Garlic Butter Shrimp
        list.add(new Recipe(null, "Garlic Butter Shrimp",
                List.of("shrimp","butter","garlic","parsley","lemon"),
                12, "easy", List.of("non-vegetarian"), 230, 20,
                "Juicy shrimp cooked in rich garlic butter with herbs and lemon for a flavorful quick dish.",
                "https://www.jocooks.com/wp-content/uploads/2021/09/garlic-butter-shrimp-1-10.jpg",
                "https://www.youtube.com/watch?v=7P8RdA4c1bA",
                "American", 4.8, 800,
                List.of("buttery","seafood","quick"),
                "3 min prep, 9 min cook",
                "2 servings"
        ));


// 18. Chocolate Pancakes
        list.add(new Recipe(null, "Chocolate Pancakes",
                List.of("flour","cocoa powder","milk","egg","sugar","butter"),
                15, "easy", List.of("vegetarian"), 350, 8,
                "Rich chocolate pancakes served warm with chocolate chips or syrup.",
                "https://supermancooks.com/wp-content/uploads/2023/03/chocolate-pancakes-featured.jpg",
                "https://www.youtube.com/watch?v=9b0Fqv0q3iE",
                "American", 4.7, 950,
                List.of("sweet","breakfast","fluffy"),
                "5 min prep, 10 min cook",
                "2 servings"
        ));


// 19. Veg Momos
        list.add(new Recipe(null, "Veg Momos",
                List.of("flour","cabbage","carrot","onion","soy sauce","ginger"),
                20, "easy", List.of("vegetarian"), 210, 4,
                "Steamed dumplings stuffed with finely chopped vegetables and mild seasoning.",
                "https://passion2cook.com/wp-content/uploads/2023/03/paneer-momos-2-1024x576.jpg",
                "https://www.youtube.com/watch?v=8x2cLxkS1Dc",
                "Tibetan", 4.8, 1800,
                List.of("street-food","light","snack"),
                "10 min prep, 10 min cook",
                "2 servings"
        ));


// 20. Lemon Herb Salmon
        list.add(new Recipe(null, "Lemon Herb Salmon",
                List.of("salmon","lemon","garlic","olive oil","herbs"),
                15, "easy", List.of("non-vegetarian","gluten-free"), 390, 34,
                "Oven-baked salmon flavored with lemon, garlic, and herbs for a healthy and protein-rich meal.",
                "https://www.themediterraneandish.com/wp-content/uploads/2024/09/TMD-Baked-Lemon-Garlic-Salmon-Leads-03-Vertical.jpg",
                "https://www.youtube.com/watch?v=P7dFzvVY4zc",
                "American", 4.9, 1200,
                List.of("healthy","protein-rich","quick"),
                "5 min prep, 10 min cook",
                "2 servings"
        ));

        // 1. Korean Bibimbap
        list.add(new Recipe(null, "Korean Bibimbap",
                List.of("rice","spinach","mushroom","carrot","egg","gochujang","sesame oil"),
                25, "medium", List.of("non-vegetarian"), 410, 16,
                "Warm rice topped with sautéed vegetables, sesame oil, and a fried egg, served with spicy gochujang paste.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Dolsot-bibimbap.jpg/1200px-Dolsot-bibimbap.jpg",
                "https://www.youtube.com/watch?v=WT9Nf0qbG0A",
                "Korean", 4.8, 1200,
                List.of("spicy","bowl-meal","asian"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 2. French Onion Soup
        list.add(new Recipe(null, "French Onion Soup",
                List.of("onion","butter","broth","thyme","baguette","cheese"),
                40, "medium", List.of("vegetarian"), 320, 10,
                "Slow-caramelized onions cooked in broth, topped with toasted baguette slices and melted cheese.",
                "https://www.familyfoodonthetable.com/wp-content/uploads/2025/01/French-onion-soup-square-1200.jpg",
                "https://www.youtube.com/watch?v=VnWq9S6M2eY",
                "French", 4.7, 980,
                List.of("warm","classic","comfort"),
                "10 min prep, 30 min cook",
                "2 servings"
        ));


// 3. Greek Chicken Gyro
        list.add(new Recipe(null, "Greek Chicken Gyro",
                List.of("chicken","yogurt","pita","cucumber","garlic","herbs"),
                30, "easy", List.of("non-vegetarian"), 450, 33,
                "Marinated grilled chicken wrapped inside pita with tzatziki, onions, and tomatoes.",
                "https://www.aheadofthyme.com/wp-content/uploads/2020/07/greek-chicken-gyros-with-homemade-tzatziki.jpg",
                "https://www.youtube.com/watch?v=q0cH5GqBl1c",
                "Greek", 4.8, 1650,
                List.of("tangy","grilled","mediterranean"),
                "10 min prep, 20 min cook",
                "2 servings"
        ));


// 4. Spinach Corn Sandwich
        list.add(new Recipe(null, "Spinach Corn Sandwich",
                List.of("spinach","corn","cheese","butter","garlic","bread"),
                12, "easy", List.of("vegetarian"), 330, 9,
                "Creamy spinach and sweet corn filling grilled between buttery toasted bread.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTErS9PFEg9hWO-p0ECsYncp8GuVToFVCfNPA&s",
                "https://www.youtube.com/watch?v=GvXa3Iu9B_I",
                "Continental", 4.6, 900,
                List.of("cheesy","snack","kids-friendly"),
                "5 min prep, 7 min cook",
                "1 serving"
        ));


// 5. Teriyaki Chicken Bowl
        list.add(new Recipe(null, "Teriyaki Chicken Bowl",
                List.of("chicken","soy sauce","sugar","ginger","garlic","rice"),
                20, "easy", List.of("non-vegetarian"), 480, 29,
                "Chicken glazed in sweet-savory teriyaki sauce served over warm rice.",
                "https://somuchfoodblog.com/wp-content/uploads/2023/09/chicken-teriyaki-bowls4.jpg",
                "https://www.youtube.com/watch?v=Iu9LQbX2BpQ",
                "Japanese", 4.7, 1880,
                List.of("sweet","bowl","asian"),
                "8 min prep, 12 min cook",
                "2 servings"
        ));


// 6. Lebanese Hummus Platter
        list.add(new Recipe(null, "Lebanese Hummus Platter",
                List.of("chickpeas","tahini","garlic","lemon","olive oil","pita"),
                10, "easy", List.of("vegetarian","vegan"), 260, 8,
                "Smooth creamy hummus drizzled with olive oil and served with warm pita bread.",
                "https://feelgoodfoodie.net/wp-content/uploads/2017/10/Hummus-with-Ground-Beef-4.jpg",
                "https://www.youtube.com/watch?v=J9UQ1Wc9KDk",
                "Middle Eastern", 4.8, 1500,
                List.of("vegan","dip","healthy"),
                "5 min prep, 5 min cook",
                "2 servings"
        ));


// 7. Mushroom Risotto
        list.add(new Recipe(null, "Mushroom Risotto",
                List.of("arborio rice","mushroom","broth","parmesan","onion","butter"),
                35, "medium", List.of("vegetarian"), 420, 12,
                "Creamy risotto gently cooked with mushrooms, butter, and parmesan cheese.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiGzDPZQS2rBVrCrXqMmo30JWLIsol72z7sw&s",
                "https://www.youtube.com/watch?v=Z_4O7f9B0q8",
                "Italian", 4.7, 1300,
                List.of("creamy","gourmet","italian"),
                "10 min prep, 25 min cook",
                "2 servings"
        ));


// 8. Fish Tacos
        list.add(new Recipe(null, "Fish Tacos",
                List.of("fish","tortilla","cabbage","lime","mayo","spices"),
                18, "easy", List.of("non-vegetarian"), 390, 22,
                "Crispy fish served in tortillas with tangy lime mayo and fresh cabbage slaw.",
                "https://bellyfull.net/wp-content/uploads/2024/07/Grilled-Fish-Tacos-blog-1.jpg",
                "https://www.youtube.com/watch?v=M-p7Va2x3V4",
                "Mexican", 4.8, 1700,
                List.of("seafood","tangy","fresh"),
                "8 min prep, 10 min cook",
                "2 servings"
        ));


// 9. Creamy Broccoli Pasta
        list.add(new Recipe(null, "Creamy Broccoli Pasta",
                List.of("pasta","broccoli","cream","garlic","butter"),
                20, "easy", List.of("vegetarian"), 410, 11,
                "Pasta tossed in a garlic cream sauce with soft-cooked broccoli florets.",
                "https://sixhungryfeet.com/wp-content/uploads/2022/06/Broccoli-Pasta-Green-Pasta-5-1.jpg",
                "https://www.youtube.com/watch?v=S3vLrF6WnGo",
                "American", 4.7, 1500,
                List.of("creamy","comfort","quick"),
                "5 min prep, 15 min cook",
                "2 servings"
        ));


// 10. Moroccan Chickpea Stew
        list.add(new Recipe(null, "Moroccan Chickpea Stew",
                List.of("chickpeas","tomato","cinnamon","cumin","garlic","onion"),
                30, "medium", List.of("vegetarian","vegan"), 330, 14,
                "Warm stew with chickpeas, tomatoes, and Moroccan spices like cumin and cinnamon.",
                "https://choosingchia.com/jessh-jessh/uploads/2022/10/moroccan-chickpea-stew-1.jpg",
                "https://www.youtube.com/watch?v=2y2gqQU8HS4",
                "Moroccan", 4.9, 1600,
                List.of("vegan","warm","exotic"),
                "10 min prep, 20 min cook",
                "2 servings"
        ));


// 11. Cajun Spicy Chicken
        list.add(new Recipe(null, "Cajun Spicy Chicken",
                List.of("chicken","cajun seasoning","lemon","garlic","oil"),
                25, "easy", List.of("non-vegetarian"), 350, 32,
                "Chicken marinated in cajun seasoning and grilled for smoky heat and bold flavor.",
                "https://thedefineddish.com/wp-content/uploads/2025/01/Cajun-Spiced-Chicken-Wings-4-scaled.jpg",
                "https://www.youtube.com/watch?v=2hN6rJgnP-4",
                "American", 4.7, 1100,
                List.of("grilled","spicy","smoky"),
                "10 min prep, 15 min cook",
                "2 servings"
        ));


// 12. Caprese Salad
        list.add(new Recipe(null, "Caprese Salad",
                List.of("tomato","mozzarella","basil","olive oil","balsamic"),
                5, "easy", List.of("vegetarian","gluten-free"), 220, 9,
                "Fresh tomatoes layered with mozzarella and basil, drizzled with balsamic reduction.",
                "https://natashaskitchen.com/wp-content/uploads/2019/08/Caprese-Salad-6.jpg",
                "https://www.youtube.com/watch?v=fsmS4N06gE8",
                "Italian", 4.8, 900,
                List.of("fresh","light","summer"),
                "3 min prep, 2 min cook",
                "1 serving"
        ));


// 13. Vietnamese Banh Mi
        list.add(new Recipe(null, "Vietnamese Banh Mi",
                List.of("baguette","chicken","carrot","radish","cilantro","chili","mayo"),
                20, "medium", List.of("non-vegetarian"), 480, 26,
                "Crispy baguette filled with grilled chicken, pickled veggies, chili, and creamy mayo.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPUeHXnNotDtZfLYfdgiv4GMvTYjCu5E0E0Q&s",
                "https://www.youtube.com/watch?v=zWrpV8K7t90",
                "Vietnamese", 4.9, 2000,
                List.of("street-food","tangy","asian"),
                "10 min prep, 10 min cook",
                "1 serving"
        ));


// 14. Butter Garlic Naan
        list.add(new Recipe(null, "Butter Garlic Naan",
                List.of("flour","yogurt","garlic","butter","salt"),
                15, "easy", List.of("vegetarian"), 300, 7,
                "Soft naan cooked on tawa or tandoor, finished with garlic-infused butter.",
                "https://lentillovingfamily.com/wp-content/uploads/2024/11/garlic-naan-1.jpg",
                "https://www.youtube.com/watch?v=Y9MIX2yjZpI",
                "Indian", 4.8, 1700,
                List.of("bread","buttery","garlic"),
                "5 min prep, 10 min cook",
                "2 servings"
        ));


// 15. Spicy Ramen Bowl
        list.add(new Recipe(null, "Spicy Ramen Bowl",
                List.of("ramen","broth","chili paste","spring onion","egg"),
                18, "easy", List.of("non-vegetarian"), 420, 15,
                "Ramen noodles simmered in chili garlic broth topped with soft-boiled egg and spring onions.",
                "https://www.seriouseats.com/thmb/iD62ByO0qeACNjfCV5eZEqkBxhQ=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/__opt__aboutcom__coeus__resources__content_migration__serious_eats__seriouseats.com__recipes__images__2014__01__20140120-beef-ramyun-homemade-recipe-15-014423f0ac734abc8c0d826e54857afe.jpg",
                "https://www.youtube.com/watch?v=Vd3rpKb5L64",
                "Japanese", 4.7, 2100,
                List.of("spicy","soupy","comfort"),
                "5 min prep, 13 min cook",
                "1 serving"
        ));


// 16. Honey Chili Potatoes
        list.add(new Recipe(null, "Honey Chili Potatoes",
                List.of("potato","honey","soy sauce","chili sauce","sesame"),
                20, "easy", List.of("vegetarian"), 350, 4,
                "Crispy fried potatoes tossed in sweet-spicy honey chili sauce topped with sesame seeds.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz9hLXxuGYMPZasr2iE_vqBU6ZxyyeAIhoWg&s",
                "https://www.youtube.com/watch?v=PbbdwN4IC2A",
                "Indo-Chinese", 4.8, 1400,
                List.of("sweet-spicy","crispy","snack"),
                "8 min prep, 12 min cook",
                "2 servings"
        ));


// 17. Lemon Butter Chicken
        list.add(new Recipe(null, "Lemon Butter Chicken",
                List.of("chicken","lemon","butter","garlic","herbs"),
                22, "easy", List.of("non-vegetarian"), 410, 33,
                "Tender chicken cooked in a tangy and buttery garlic-lemon sauce.",
                "https://www.wellplated.com/wp-content/uploads/2019/05/Lemon-Butter-Chicken-no-cream.jpg",
                "https://www.youtube.com/watch?v=fXHnQ0N4iLA",
                "Continental", 4.8, 1250,
                List.of("tangy","buttery","juicy"),
                "8 min prep, 14 min cook",
                "2 servings"
        ));


// 18. Red Sauce Penne
        list.add(new Recipe(null, "Red Sauce Penne",
                List.of("penne","tomato","basil","olive oil","garlic"),
                20, "easy", List.of("vegetarian"), 360, 10,
                "Classic tomato basil pasta simmered in rich red sauce and tossed with herbs.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpVriPhJdWEJtNkZAkqXFB8dKTvKWnPsUbYw&s",
                "https://www.youtube.com/watch?v=u0V3N1s-VCY",
                "Italian", 4.6, 1000,
                List.of("classic","italian","vegetarian"),
                "7 min prep, 13 min cook",
                "2 servings"
        ));


// 19. Turkish Menemen
        list.add(new Recipe(null, "Turkish Menemen",
                List.of("egg","tomato","pepper","olive oil","salt"),
                12, "easy", List.of("vegetarian"), 280, 16,
                "Soft-scrambled eggs cooked slowly with tomatoes, peppers, and olive oil.",
                "https://www.alphafoodie.com/wp-content/uploads/2021/08/Menemen-Square.jpeg",
                "https://www.youtube.com/watch?v=r1JcZ8J2TnE",
                "Turkish", 4.7, 800,
                List.of("breakfast","soft-eggs","mediterranean"),
                "5 min prep, 7 min cook",
                "1 serving"
        ));


// 20. Sri Lankan Coconut Sambal with Roti
        list.add(new Recipe(null, "Coconut Sambal with Roti",
                List.of("coconut","chili","lime","salt","flour"),
                18, "easy", List.of("vegetarian"), 320, 6,
                "Fresh coconut chili sambal blended with lime, served with soft coconut roti.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj3iw7hWDNwv8pwDpWsDUXDs7t2B-zciUddg&s",
                "https://www.youtube.com/watch?v=OxDRJjdsRpo",
                "Sri Lankan", 4.8, 1400,
                List.of("spicy","fresh","island-style"),
                "8 min prep, 10 min cook",
                "2 servings"
        ));



        return list;
    }
}

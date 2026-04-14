public class Recipe {
    private int recipeId;
    private String name;
    private String description;
    private String dateCreated;
    private int prepTime;
    private int cookTime;
    private String difficulty;
    private int userId;
    private String authorName;

    // Constructor
    public Recipe(int recipeId, String name, String description, String dateCreated,
                  int prepTime, int cookTime, String difficulty, int userId, String authorName) {
        this.recipeId = recipeId;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.difficulty = difficulty;
        this.userId = userId;
        this.authorName = authorName;
    }

    // Getters
    public int getRecipeId() { return recipeId; }
    public String getName() { return name; }
    public String getDescription() { 
        return description != null ? description : "No description"; 
    }
    public String getDateCreated() { return dateCreated; }
    public int getPrepTime() { return prepTime; }
    public int getCookTime() { return cookTime; }
    public String getDifficulty() { 
        return difficulty != null ? difficulty : "Not specified"; 
    }
    public int getUserId() { return userId; }
    public String getAuthorName() { return authorName; }
    
    public int getTotalTime() { 
        return prepTime + cookTime; 
    }
}
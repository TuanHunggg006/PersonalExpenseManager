package personalexpensemanager.model;

public class Category {
    private String categoryId;
    private String categoryName;
    private String type; // Income hoặc Expense

    public Category() {
    }

    public Category(String categoryId, String categoryName, String type) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
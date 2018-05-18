package hackathon.sg.potf.FirebaseClass;

import java.util.ArrayList;

public class Category {
    private String category;
    private ArrayList<Product> productList;

    public Category(){}
    public Category(String category, ArrayList<Product> productList){
        this.category = category;
        this.productList = productList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}

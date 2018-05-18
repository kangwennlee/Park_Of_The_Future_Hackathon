package hackathon.sg.potf.FirebaseClass;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Product> carts;
    private Double total;

    public Cart(){}
    public Cart(ArrayList<Product>carts, Double total){
        this.setCarts(carts);
        this.setTotal(total);
    }

    public ArrayList<Product> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Product> carts) {
        this.carts = carts;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

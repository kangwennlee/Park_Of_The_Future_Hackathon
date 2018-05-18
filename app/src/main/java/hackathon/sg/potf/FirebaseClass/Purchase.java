package hackathon.sg.potf.FirebaseClass;

public class Purchase {
    private String purchaseId;
    private Double balanceBefore;
    private Double balanceAfter;
    private Cart productPurchased;
    private Double purchaseAmount;

    public Purchase(){}
    public Purchase(String purchaseId,Double balanceBefore, Double balanceAfter,Cart productPurchased, Double purchaseAmount){
        this.setPurchaseId(purchaseId);
        this.setBalanceBefore(balanceBefore);
        this.setBalanceAfter(balanceAfter);
        this.setProductPurchased(productPurchased);
        this.setPurchaseAmount(purchaseAmount);
    }


    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Double getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Double balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Cart getProductPurchased() {
        return productPurchased;
    }

    public void setProductPurchased(Cart productPurchased) {
        this.productPurchased = productPurchased;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}

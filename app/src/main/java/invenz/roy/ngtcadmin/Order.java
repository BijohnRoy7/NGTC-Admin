package invenz.roy.ngtcadmin;

public class Order {

    private int orderId;
    private String itemName, buyerName, city, quantity, contactNo, itemId;

    public Order() {
    }

    public Order(int orderId, String itemName, String buyerName, String city, String quantity, String contactNo, String itemId) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.buyerName = buyerName;
        this.city = city;
        this.quantity = quantity;
        this.contactNo = contactNo;
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}

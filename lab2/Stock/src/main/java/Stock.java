public class Stock {
    private String name;
    private int quantity;

    public Stock(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int _quantity){
        this.quantity = _quantity;
    }
}

package model;

public class Item {
    String itemName;
    String type;
    int levelReq;
    int level;
    int price;

    public Item(String itemName, String type, int levelReq, int level, int price){
        this.itemName = itemName;
        this.type = type;
        this.levelReq = levelReq;
        this.level = level;
        this.price = price;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setLevelReq(int levelReq){
        this.levelReq = levelReq;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setPrice(int price){
        this.price = price;
    }
    
    public String getItemName(){
        return this.itemName;
    }
    public String getType(){
        return this.type;
    }
    public int getLevelReq(){
        return this.levelReq;
    }
    public int getLevel(){
        return this.level;
    }
    public int getPrice(){
        return this.price;
    }

    public void printItemData() {
        System.out.println("Item Name: " + getItemName());
        System.out.println("Type: " + getType());
        System.out.println("Level: " + getLevelReq());
        System.out.println("Level: " + getLevel());
        System.out.println("Price: " + getPrice());
    }
}

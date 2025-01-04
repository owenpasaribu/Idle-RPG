package model;

public class Potion extends Item{
    int hpBoost;
    int atkBoost;
    int defBoost;
    String effect;

    public Potion(String name, String type, int levelReq, int level, int price, int hpBoost, int atkBoost, int defBoost, String effect){
        super(name, "Potion", levelReq, level, price);
        this.hpBoost = hpBoost;
        this.atkBoost = atkBoost;
        this.defBoost = defBoost;
        this.effect = effect;
    }

    public void setHpBoost(int hpBoost){
        this.hpBoost = hpBoost;
    }
    public void setAtkBoost(int atkBoost){
        this.atkBoost = atkBoost;
    }
    public void setDefBoost(int defBoost){
        this.defBoost = defBoost;
    }
    public void setEffect(String effect){
        this.effect = effect;
    }
    
    public int getHpBoost(){
        return this.hpBoost;
    }
    public int getAtkBoost(){
        return this.atkBoost;
    }
    public int getDefBoost(){
        return this.defBoost;
    }
    public String getEffect(){
        return this.effect;
    }
}

package model;

public class Weapon extends Item{
    String tier;
    int damage;
    int accuracy;
    String weaponSkill;
    int cooldownSkill;
    String effect;

    public Weapon(String itemName, String type,int levelReq, int level, int price, String tier, int damage, int accuracy, String weaponSkill, int cooldownSkill, String effect) {
        super(itemName, "Weapon", levelReq, level, price);
        this.tier = tier;
        this.damage = damage;
        this.accuracy = accuracy;
        this.weaponSkill = weaponSkill;
        this.cooldownSkill = cooldownSkill;
        this.effect = effect;
    }

    public void setTier(String tier){
        this.tier = tier;
    }
    public void setDamage(int  damage){
        this.damage = damage;
    }
    public void setAccuracy(int accuracy){
        this.accuracy = accuracy;
    }
    public void setWeaponSkill(String weaponSkill){
        this.weaponSkill = weaponSkill;
    }
    public void setCooldownSkill(int cooldownSkill){
        this.cooldownSkill = cooldownSkill;
    }
    public void setEffect(String effect){
        this.effect = effect;
    }

    public String getTier(){
        return this.tier;
    }
    public int getDamage(){
        return this.damage;
    }
    public int getAccuracy(){
        return this.accuracy;
    }
    public String getWeaponSkill(){
        return this.weaponSkill;
    }
    public int getCooldownSkill(){
        return this.cooldownSkill;
    }
    public String getEffect(){
        return this.effect;
    }
}   

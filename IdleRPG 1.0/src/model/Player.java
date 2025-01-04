package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private String password;
    private int level;
    private int exp;
    private int hp;
    private int atk;
    private int def;
    private int gold;
    private int fragment;
    private Item equippedWeapon;
    private List<Item> inventory;

    // Konstruktor
    public Player(String username, String password, int level, int exp, int gold, int fragment) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.exp = exp;
        this.hp = (100+(25*(level-1)));
        this.atk = (20+(15*(level-1)));
        this.def = (10+(8*(level-1)));
        this.gold = gold;
        this.fragment = fragment;
        this.inventory = new ArrayList<>();
    }

    // Getter dan Setter untuk username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    // Getter dan Setter untuk username
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter dan Setter untuk level
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // Getter dan Setter untuk exp
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    // Getter dan Setter untuk hp
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // Getter dan Setter untuk atk
    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    // Getter dan Setter untuk def
    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    // Getter dan Setter untuk gold
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // Getter dan Setter untuk fragment
    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }

    // Getter dan Setter untuk items
    public List<Item> getInventory() {
        return this.inventory;
    }

    public void addItemToInventory(Item item){
        inventory.add(item);
    }
    public void removeItemFromInventory(Item item){
        inventory.remove(item);
    }
    
    public void setEquippedWeapon(Item equippedWeapon){
        this.equippedWeapon = equippedWeapon;
    }

    public Item getEquippedWeapon(){
        return equippedWeapon;
    }

    public void gainExp(int expLoot){
        this.exp += expLoot;
        while (this.exp >= (100+(50*(this.level-1)))) {
            this.exp=this.exp-(100 + (50*(this.level-1)));
            levelUp();
        }
    }

    public void levelUp() {
        this.level++;
        this.hp+=25;
        this.atk+=15;
        this.def+=8;
        System.out.println("Congrats! You leveled up to level" + this.level);
    }
    
    public void updateStats(int hp, int atk, int def) {
        this.hp+=hp;
        this.atk+=atk;
        this.def+=def;
    }

    public void resetStats() {
        this.hp = (100+(25*(level-1)));
        this.atk = (20+(15*(level-1)));
        this.def = (10+(8*(level-1)));
    }

    // Menampilkan informasi pemain
    public void displayPlayerInfo() {
        System.out.println("----- Player Info -----");
        System.out.println("Username: " + this.username);
        System.out.println("Level: " + this.level);
        System.out.println("EXP: " + this.exp);
        System.out.println("HP: " + this.hp);
        System.out.println("Attack: " + this.atk);
        System.out.println("Defense: " + this.def);
        System.out.println("Gold: " + this.gold);
        System.out.println("Fragments: " + this.fragment);
        if (this.getEquippedWeapon() != null) {
            System.out.println("Equipped Weapon: " + this.equippedWeapon.getItemName());
        }else{
            System.out.println("Equipped Weapon: -");
        }
        // System.out.println("Items: " + formatItems(player.getItems()));
        System.out.println("------------------------");
    }
}
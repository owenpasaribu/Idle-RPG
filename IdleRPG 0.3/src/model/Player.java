package model;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Player {
    private String username;
    private String password;
    private int level;
    private int exp;
    private int hp;
    private int atk;
    private int def;
    private int money;
    private int fragment;
    private List<Item> inventory;

    // Konstruktor
    public Player(String username, String password, int level, int exp, int money, int fragment) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.exp = exp;
        this.hp = (100+(25*(level-1)));
        this.atk = (20+(15*(level-1)));
        this.def = (10+(8*(level-1)));
        this.money = money;
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

    // Getter dan Setter untuk money
    public int getGold() {
        return money;
    }

    public void setGold(int money) {
        this.money = money;
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

    public void gainExp(int expLoot) {
        this.exp += expLoot;
    
        // Rumus pengalaman yang diperlukan untuk naik level
        int requiredExpForNextLevel = 100 + (50 * (this.level - 1));
        
        while (this.exp >= requiredExpForNextLevel) {
            this.exp -= requiredExpForNextLevel; // Kurangi EXP yang dipakai untuk naik level
            levelUp();
            requiredExpForNextLevel = 100 + (50 * (this.level - 1)); // Update untuk level berikutnya
        }
    }
    

    public void levelUp() {
        this.level++;
        this.hp += 25;
        this.atk += 15;
        this.def += 8;
        System.out.println("Congrats! You leveled up to Level " + this.level + "!");
    }
    
    // Menambahkan method getName() untuk mendapatkan nama pemain
    public String getName() {
        return this.username;
    }

    // Menambahkan method attack() untuk menghitung damage serangan pemain
    public int attack() {
        return this.atk;  // Hanya mengembalikan nilai ATK untuk sementara
    }
    // Menambahkan method useItem() untuk menggunakan item
    public void useItem(Scanner scanner) {
    
    // Implementasi penggunaan item, misalnya:
    System.out.println("Which item would you like to use?");
    // Lakukan logika sesuai item yang ada di inventory
    }

    // Menambahkan method addMoney() untuk menambah uang pemain
    public void addMoney(int amount) {
        this.money += amount;
    }

    // Menambahkan method addExp() untuk menambah EXP pemain
    public void addExp(int amount) {
    this.exp += amount;
    // Tambahkan logika untuk naik level jika EXP melebihi batas level
    int requiredExp = 100 + (50 * (this.level - 1)); // Sesuaikan rumus EXP
    while (this.exp >= requiredExp) {
        this.exp -= requiredExp;
        levelUp(); // Panggil method levelUp()
        }
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
        System.out.println("Equipped Weapon: " + this.equippedWeapon.getItemName());
        // System.out.println("Items: " + formatItems(player.getItems()));
        System.out.println("------------------------");
    }
}

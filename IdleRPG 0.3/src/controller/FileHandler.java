package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Item;
import model.Monster;
import model.Player;
import model.Potion;
import model.Weapon;


public class FileHandler {
    private final String PLAYERS_FILE = "src/data/players_data.csv";
    private final String MONSTERS_FILE = "src/data/monsters_data.csv";
    private final String WEAPONS_FILE = "src/data/weapons_data.csv";
    private final String POTIONS_FILE = "src/data/potions_data.csv";


    public List<Player> loadPlayers(List<Item> items){
        List<Player> players = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(PLAYERS_FILE))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String username = data[0];
                String password = data[1];
                int level = Integer.parseInt(data[2]);
                int exp = Integer.parseInt(data[3]);
                int money = Integer.parseInt(data[4]);
                int fragment = Integer.parseInt(data[5]);
                
                Player player = new Player(username, password, level, exp, money, fragment);
                
                if (!data[6].equals("null")) {
                    Item equippedWeapon = findItemByName(items, data[6]);
                    player.setEquippedWeapon(equippedWeapon);
                } else {
                    player.setEquippedWeapon(null);
                }
                
                for (int i = 7; i < data.length; i++) {
                    Item item = findItemByName(items, data[i]);
                    player.addItemToInventory(item);
                }
                
                players.add(player);
                // player.displayPlayerInfo();
            }
        } catch (Exception e) {
            System.out.println("Error reading players data file: " + e.getMessage());
        }
        return players;
    }

    public void savePlayers(List<Player> players){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PLAYERS_FILE))) {
            for (Player player : players) {
                bw.write(player.getUsername() + "," + player.getPassword() + "," + player.getLevel() + "," + player.getExp() + "," + player.getGold() + "," + player.getFragment());

                bw.write("," + (player.getEquippedWeapon() != null ? player.getEquippedWeapon().getItemName() : "null"));

                // Fungsi buat save item dari inventory ke file
                for (Item item : player.getInventory()) {
                    bw.write("," + item.getItemName());
                }

                bw.newLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error saving player data: " + e.getMessage());
        }
    }

    public void registerPlayer(Scanner scanner, List<Player> players){
        while (true) {
            System.out.println("Enter username\t: ");
            String username = scanner.nextLine();

            if (players.stream().anyMatch(player -> player.getUsername().equals(username))) {
                System.out.println("username already taken. Please choose other username.");
                continue;
            } else {
                System.out.println("Enter password\t: ");
                String password = scanner.nextLine();
                Player newPlayer = new Player(username, password, 1, 0, 50, 0);
                players.add(newPlayer);
                savePlayers(players);
                System.out.println("Register successful!\nYour Username : " + username + "\nYour Password : " + password);
                break;
            }
        }
    }

    public static Player loginPlayer(Scanner scanner, List<Player> players){
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Player player : players) {
            if (player.getUsername().equals(username) && player.getPassword().equals(password)) {
                System.out.println("Login successful. Welcome " + player.getUsername() + "!");
                return player;
            }
        }
        System.out.println("Invalid username or password. Try again.");
        return null;
    }

    public List<Monster> loadMonsters(){
        List<Monster> monsters = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE))){
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String monsterName = data[0];
                String type = data[1];
                int levelRequirement = Integer.parseInt(data[2]);
                int hp = Integer.parseInt(data[3]);
                int atk = Integer.parseInt(data[4]);
                int def = Integer.parseInt(data[5]);
                String hpIndicator = data[6];
                String atkIndicator = data[7];
                String defIndicator = data[8];
                int accuracy = Integer.parseInt(data[9]);
                int escapePrecentage = Integer.parseInt(data[10]);
                int moneyLoot = Integer.parseInt(data[11]);
                int fragmentLoot = Integer.parseInt(data[12]);
                int expLoot = Integer.parseInt(data[13]);
                String uniqueSkill = data[14];
                int cooldownSkill = Integer.parseInt(data[15]);

                Monster monster = new Monster(monsterName, type, levelRequirement, hp, atk, def, hpIndicator, atkIndicator, defIndicator, accuracy, escapePrecentage, moneyLoot, fragmentLoot, expLoot, uniqueSkill, cooldownSkill);
                monsters.add(monster);
                // monster.printMonsterDetails();
                
            }
        } catch (Exception e) {
            System.out.println("Error reading monsters data file: " + e.getMessage());
        }
        return monsters;
    }

    public List<Weapon> loadWeapons(){
        List<Weapon> weapons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(WEAPONS_FILE))){
            String line;
            line = br.readLine();
            while ((line = br.readLine())!=null) {
                String[] data = line.split(",");

                String name = data[0];
                String type = data[1];
                int level = Integer.parseInt(data[2]);
                int levelReq = Integer.parseInt(data[3]);
                int price = Integer.parseInt(data[4]);
                String tier = data[5];
                int damage = Integer.parseInt(data[6]);
                int accuracy = Integer.parseInt(data[7]);
                String weaponSkill = data[8];
                int cooldownSkill = Integer.parseInt(data[9]);
                String effect = data[10];

                Weapon weapon = new Weapon(name, type, levelReq, level, price, tier, damage, accuracy, weaponSkill, cooldownSkill, effect);
                weapons.add(weapon);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error reading weapons data file: " + e.getMessage());
        }
        return weapons;
    }
    
    public List<Potion> loadPotions(){
        List<Potion> potions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(POTIONS_FILE))){
            String line;
            line = br.readLine();
            while ((line = br.readLine())!=null) {
                String[] data = line.split(",");

                String name = data[0];
                String type = data[1];
                int levelReq = Integer.parseInt(data[2]);
                int level = Integer.parseInt(data[3]);
                int price = Integer.parseInt(data[4]);
                int hpBoost = Integer.parseInt(data[5]);
                int atkBoost = Integer.parseInt(data[6]);
                int defBoost = Integer.parseInt(data[7]);
                String effect = data[8];
                
                Potion potion = new Potion(name, type, levelReq, level, price, hpBoost, atkBoost, defBoost,effect);
                potions.add(potion);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error reading potions data file: " + e.getMessage());
        }
        return potions;
    }

    public static Item findItemByName(List<Item> items, String name){
        for (Item item : items) {
            if (item.getItemName().equals(name)) {
                return item;
            }
        }
        return null;
    }
    
    public static Weapon findWeaponByName(List<Weapon> weapons, String name){
        for (Weapon weapon : weapons) {
            if (weapon.getItemName().equals(name)) {
                return weapon;
            }
        }
        return null;
    }
    
    public static Potion findPotionByName(List<Potion> potions, String name){
        for (Potion potion : potions) {
            if (potion.getItemName().equals(name)) {
                return potion;
            }
        }
        return null;
    }
}

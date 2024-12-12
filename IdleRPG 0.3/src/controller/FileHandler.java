package controller;

import model.Monster;
import model.Player;
import model.Item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    private final String PLAYERS_FILE = "src/data/players_data.csv";
    private final String MONSTERS_FILE = "src/data/monsters_data.csv";
    private final String ITEMS_FILE = "src/data/items_data.csv";


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
                
               // Fungsi buat load kemudian cari + tambahin item ke inventory
                for (int i = 6; i < data.length; i++) {
                    Item item = findItemByName(items, data[i]);
                    player.addItemToInventory(item);
                }
                
                players.add(player);
                //player.displayPlayerInfo();
            }
        } catch (Exception e) {
            System.out.println("Error reading players file: " + e.getMessage());
        }
        return players;
    }

    public void savePlayers(List<Player> players){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PLAYERS_FILE))) {
            for (Player player : players) {
                bw.write(player.getUsername() + "," + player.getPassword() + "," + player.getLevel() + "," + player.getExp() + "," + player.getGold() + "," + player.getFragment());

                // Fungsi buat save item dari inventory ke file
                for (Item item : player.getInventory()) {
                    bw.write("," + item.getItemName());
                }

                bw.newLine();
            }
        } catch (Exception e) {
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

    public List<Monster> loadMonsters() {
        List<Monster> monsters = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE))) {
            String line;
            br.readLine();  // Untuk melewati header (jika ada)
            
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
    
                // Membuat objek monster baru berdasarkan data yang dimuat
                Monster monster = new Monster(monsterName, type, levelRequirement, hp, atk, def, hpIndicator, atkIndicator, defIndicator, accuracy, escapePrecentage, moneyLoot, fragmentLoot, expLoot, uniqueSkill, cooldownSkill);
                monsters.add(monster);  // Menambahkan monster ke dalam daftar
            }
        } catch (Exception e) {
            System.out.println("Error reading monsters file: " + e.getMessage());
        }
        return monsters;
    }
    

    public List<Item> loadItems(){
        List<Item> items = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(ITEMS_FILE))){
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String itemName = data[0];
                String type = data[1];
                int level = Integer.parseInt(data[2]);
                int price = Integer.parseInt(data[3]);

                Item item = new Item(itemName, type, level, price);
                items.add(item);
                //item.printItemData();
            }
        } catch (Exception e) {
            System.out.println("Error reading items file: " + e.getMessage());
        }
        return items;
    }

    public static Item findItemByName(List<Item> items, String name){
        for (Item item : items) {
            if (item.getItemName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}

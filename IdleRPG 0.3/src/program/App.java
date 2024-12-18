package program;

import controller.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.*;
import view.ViewLogin;
import view.ViewMenu;
import view.ViewStats;

public class App {
    
    public static void main(String[] args) {
        Player currentPlayer = null;
        Scanner scanner = new Scanner(System.in);

        FileHandler fileHandler = new FileHandler();
        
        ViewLogin viewLogin = new ViewLogin();
        ViewMenu viewMenu = new ViewMenu();
        ViewStats viewStats = new ViewStats();

        List<Monster> monsters = fileHandler.loadMonsters();
        List<Potion> potions = fileHandler.loadPotions();
        List<Weapon> weapons = fileHandler.loadWeapons();
        List<Item> items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(potions);

        List<Player> players = fileHandler.loadPlayers(items);
        
        BattleSystem battleSystem = new BattleSystem(scanner, weapons, potions);

        while (currentPlayer == null) {
            viewLogin.Login();
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    currentPlayer = FileHandler.loginPlayer(scanner, players);
                    break;
                case "2":
                    fileHandler.registerPlayer(scanner, players);
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }

        while (true) {
            viewMenu.Menu();
            String choice = scanner.nextLine();

            // for (int i = 0; i < i; i++) {
            //     try {
            //         setTimeout(FileHandler.savePlayers(), 9999, currentPlayer);
            //     } catch (Exception e) {
            //     }
            // }
            
            switch (choice) {
                case "1":
                boolean exitSubMenu = false; // Variabel untuk keluar dari sub-menu
                do {
                    currentPlayer.displayPlayerInfo();
                    System.out.println("Pilihan:");
                    System.out.println("1. Inventory");
                    System.out.println("2. Main Menu");
                    System.out.print("Masukkan pilihan: ");
                    String Stats = scanner.nextLine();
    
                    switch (Stats) {
                        case "1":
                            Inventory inventory = new Inventory(currentPlayer);
                            inventory.manageInventory(scanner);
                            break;
                        case "2":
                            System.out.println("Kembali ke menu utama...");
                            exitSubMenu = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid. Coba lagi.");
                    }
                } while (!exitSubMenu);
                break;
                case "2":
                    // Battle biasa hanya dengan monster biasa
                    List<Monster> normalMonsters = monsters.stream()
                        .filter(m -> !m.getType().equalsIgnoreCase("Boss"))
                        .collect(Collectors.toList());

                    if (!normalMonsters.isEmpty()) {
                        // Memilih monster acak dari list monster biasa
                        Monster monster = normalMonsters.get(new Random().nextInt(normalMonsters.size()));
                        battleSystem.startBattle(currentPlayer, List.of(monster)); // Mengubah monster menjadi List
                    } else {
                        System.out.println("No normal monster available for battle!");
                    }
                    break;
                case "3":
                    battleSystem.startBossBattle(currentPlayer, monsters); // Melakukan Boss Battle
                    break;
                case "4":
                    Shop shop = new Shop(currentPlayer, items);
                    shop.openShop(scanner);
                    break;
                case "5":
                    Inventory inventory = new Inventory(currentPlayer);
                    inventory.manageInventory(scanner);
                    break;
                case "0":
                    fileHandler.savePlayers(players);
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}

package program;

import controller.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import model.*;
import view.ViewLogin;
import view.ViewMenu;
//import view.ViewStats;

public class App {
    
    public static void main(String[] args) {
        Player currentPlayer = null;
        Scanner scanner = new Scanner(System.in);

        FileHandler fileHandler = new FileHandler();
        
        ViewLogin viewLogin = new ViewLogin();
        ViewMenu viewMenu = new ViewMenu();
        //ViewStats viewStats = new ViewStats();
        BattleLog battleLog = new BattleLog();
        List<Monster> monsters = fileHandler.loadMonsters();
        List<Potion> potions = fileHandler.loadPotions();
        List<Weapon> weapons = fileHandler.loadWeapons();
        List<Item> items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(potions);

        List<Player> players = fileHandler.loadPlayers(items);
        

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

            //ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            // Schedule the task to run every 5 seconds
            // scheduler.scheduleAtFixedRate(() -> autoSave(fileHandler, currentPlayer), 0, 5, TimeUnit.SECONDS);
            
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
                            Inventory inventory = new Inventory(scanner, currentPlayer);
                            inventory.manageInventory();
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
                        BattleSystem battleSystem = new BattleSystem(scanner, currentPlayer, List.of(monster), weapons, potions);
                        battleSystem.startBattle(); // Mengubah monster menjadi List
                    } else {
                        System.out.println("No normal monster available for battle!");
                    }
                    break;
                case "3":
                    BattleSystem battleSystem = new BattleSystem(scanner, currentPlayer, monsters, weapons, potions);
                    battleSystem.startBossBattle(currentPlayer, monsters); // Melakukan Boss Battle
                    break;
                case "4":
                    Shop shop = new Shop(scanner, currentPlayer, items);
                    shop.openShop();
                    break;
                case "5":
                    battleLog.displayBattleLog();
                    break;
                case "0":
                    fileHandler.savePlayers(players);
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // public static void autoSave(FileHandler fileHandler, Player player) {
    //     System.out.println("Auto-saving data... " + System.currentTimeMillis());
    //     fileHandler.savePlayers(player);
    // }
    
}

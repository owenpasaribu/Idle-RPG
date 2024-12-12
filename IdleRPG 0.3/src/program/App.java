package program;

import model.*;
import controller.*;
import view.*;

import java.util.List;
import java.util.Scanner;

public class App {
    
    public static void main(String[] args) {
        Player currentPlayer = null;
        FileHandler fileHandler = new FileHandler();
        ViewLogin viewLogin = new ViewLogin();
        ViewMenu viewMenu = new ViewMenu();
        List<Item> items = fileHandler.loadItems();
        List<Monster> monsters = fileHandler.loadMonsters();
        List<Player> players = fileHandler.loadPlayers(items);
        
        BattleSystem battleSystem = new BattleSystem();
        
        Scanner scanner = new Scanner(System.in);
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
                    fileHandler.savePlayers(players);
                    System.exit(0);
                default:
                System.out.println("Invalid option.");
            }
        }

        while (true) {
            viewMenu.Menu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    do {
                        currentPlayer.displayPlayerInfo();
                        System.out.println("Pilihan:");
                        System.out.println("1. Inventory");
                        System.out.println("2. Main Menu");
                        String Stats = scanner.nextLine();
                        switch (Stats) {
                            case "1":
                                Inventory inventory = new Inventory(currentPlayer);
                                inventory.manageInventory(scanner);
                            break;
                            case "2":
                                System.out.println("        ");
                                break;
                            default:
                                System.out.println("Invalid choice. Try again.");
                        }
                    } while (!choice.equals("3"));
                    break;
                case "2":
                    battleSystem.startBattle(currentPlayer, monsters);
                    break;
                case "3":
                    System.out.println("Coming soon . . .");
                    break;
                case "4":
                    Shop shop = new Shop(currentPlayer, items);
                    shop.displayShop(scanner);
                    break;
                case "5":
                System.out.println("Coming soon . . .");
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

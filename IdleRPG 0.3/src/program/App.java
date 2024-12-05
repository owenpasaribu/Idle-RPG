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

        List<Monster> monsters = fileHandler.loadMonsters();
        List<Player> players = fileHandler.loadPlayers();
        
        BattleSystem battleSystem = new BattleSystem();
        
        Scanner scanner = new Scanner(System.in);

        while (currentPlayer == null) {
            System.out.println("=== Login Menu === ");
            System.out.println("1. Login Account");
            System.out.println("2. Register Account");
            System.out.println("0. Exit");
            System.out.print("Choose an option >> ");
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    currentPlayer = fileHandler.loginPlayer(scanner, players);
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
            System.out.println("=== Main Menu ===");
            System.out.println("1. View Statistics");
            System.out.println("2. Battle");
            System.out.println("3. BOSS Battle");
            System.out.println("4. Shop");
            System.out.println("5. Inventory");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    currentPlayer.displayPlayerInfo();
                    break;
                case 2:
                    battleSystem.startBattle(currentPlayer, monsters);
                    break;
                case 3:
                    System.out.println("Coming soon . . .");
                    break;
                case 4:
                System.out.println("Coming soon . . .");
                    break;
                case 5:
                System.out.println("Coming soon . . .");
                    break;
                case 0:
                fileHandler.savePlayers(players);
                System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}

package controller;

import java.util.Scanner;
import model.Item;
import model.Player;


public class Inventory {
    private Player player;

    public Inventory(Player player) {
        this.player = player;
    }

    public void manageInventory(Scanner scanner){
        while (true) {
            System.out.println("\n=== Inventory ===");
            System.out.println("1. View Items");
            System.out.println("0. Exit Inventory");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    displayItems();
                    break;
                
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void displayItems() {
        System.out.println("\nYour Items:");
        for (Item item : player.getInventory()) {
            System.out.println("- " + item.getItemName() + " ( " + item.getType() + " )");
        }
    }
}

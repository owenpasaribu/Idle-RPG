package controller;

import java.util.Scanner;
import model.Player;
import view.InventoryView;

public class Inventory {
    private Player player;
    private InventoryView inventoryView;

    public Inventory(Player player) {
        this.player = player;
        this.inventoryView = new InventoryView();
    }

    public void manageInventory(Scanner scanner) {
        while (true) {
            inventoryView.displayMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayItems();
                    break;

                case "0":
                    return;

                default:
                    inventoryView.displayInvalidOption();
            }
        }
    }

    private void displayItems() {
        inventoryView.displayItems(player.getInventory());
    }
}

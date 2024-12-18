package view;

import model.Item;
import java.util.List;

public class InventoryView {

    public void displayMenu() {
        System.out.println("\n=== Inventory ===");
        System.out.println("1. View Items");
        System.out.println("2. Equip Weapon");
        System.out.println("0. Exit Inventory");
        System.out.print("Select an option: ");
    }

    public void displayItems(List<Item> items) {
        if (items.isEmpty()) {
            System.out.println("\nYour inventory is empty.");
            return;
        }
        
        System.out.println("\nYour Items:");
        for (Item item : items) {
            System.out.println((items.indexOf(item)+1) + "- " + item.getItemName() + " (" + item.getType() + ")");
        }
    }

    // Menampilkan pesan kesalahan jika input tidak valid
    public void displayInvalidOption() {
        System.out.println("Invalid option. Try again.");
    }
}

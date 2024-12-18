package controller;

import java.util.List;
import java.util.Scanner;
import model.Item;
import model.Player;
import view.InventoryView;


public class Inventory {
    private Player player;
    private InventoryView inventoryView;

    public Inventory(Player player) {
        this.player = player;
        this.inventoryView = new InventoryView();
    }

    public void manageInventory(Scanner scanner){
        while (true) {
            inventoryView.displayMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    displayItems();
                    break;

                case "2":
                    equipWeapon(scanner);
                
                case "0":
                    return;
                    
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void displayItems() {
        inventoryView.displayItems(player.getInventory());
    }

    public void equipWeapon(Scanner scanner){ 
        List<Item> availableWeapons = player.getInventory().stream()
                    .filter(item -> item.getType().equals("Weapon"))
                    .toList();
        inventoryView.displayItems(availableWeapons);
        System.out.print("Choose Item to Equip\n>> ");
        int itemIndex = scanner.nextInt();
        scanner.nextLine();
        if (itemIndex == 0) {
            return;
        } else if (itemIndex > 0 && itemIndex <= availableWeapons.size()) {
            Item equippedWeapon = availableWeapons.get(itemIndex-1);
            if (equippedWeapon.getType().equals("Weapon")) {
                
                if (!player.getEquippedWeapon().getItemName().equals("null")) {
                    player.addItemToInventory(player.getEquippedWeapon());
                }
                player.removeItemFromInventory(equippedWeapon);
                player.setEquippedWeapon(equippedWeapon);
                
                System.out.println("You equipped " + availableWeapons.get(itemIndex - 1).getItemName() + "!");
            }
            else {
                System.out.println("Item type you want to equip is not Weapon");
            }
        }
        else {
            System.out.println("Invalid item choice");
        }
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

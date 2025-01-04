package view;

import java.util.List;
import java.util.Scanner;
import model.Item;
import model.Player;

public class ShopView {

    // Menampilkan menu utama Shop dan mendapatkan pilihan pengguna
    public String getShopMenuChoice(Scanner scanner, Player player) {
        System.out.println("Welcome to the shop!");
        System.out.println("You have " + player.getGold() + " gold.");
        System.out.println("1. Buy Item");
        System.out.println("2. Change Fragment");
        System.out.println("0. Exit Shop");
        System.out.print("Select menu: ");
        return scanner.nextLine();
    }

    // Menampilkan daftar item dan mendapatkan pilihan pengguna
    public int getItemSelection(Scanner scanner, List<Item> items, Player player) {
        System.out.println("Gold: " + player.getGold());
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println((i + 1) + " - " + item.getItemName() + " (" + item.getType() + ")\t| Price: " + item.getPrice());
        }
        System.out.println("0 - Back");
        System.out.print("Select an option: ");
        return scanner.nextInt();
    }

    public void displayPurchaseSuccess(String itemName) {
        System.out.println("You bought " + itemName + "!");
    }

    public void displayInsufficientGold() {
        System.out.println("You don't have enough money to buy this item.");
    }

    public void displayInvalidOption() {
        System.out.println("Invalid option. Try again.");
    }

    public void displayChangeFragment() {
        System.out.println("Coming soon . . .");
    }
}
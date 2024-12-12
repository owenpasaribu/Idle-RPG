package view;

import model.Item;
import model.Player;
import java.util.List;

public class ShopView {

    // Menampilkan menu utama Shop
    public void displayShopMenu(Player player) {
        System.out.println("Welcome to the shop!");
        System.out.println("You have " + player.getGold() + " gold.");
        System.out.println("1. Buy Item");
        System.out.println("2. Change Fragment");
        System.out.println("0. Exit Shop");
        System.out.print("Select menu: ");
    }

    // Menampilkan daftar item yang dijual
    public void displayItems(List<Item> items, Player player) {
        System.out.println("Gold: " + player.getGold());
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println((i + 1) + " - " + item.getItemName() + " (" + item.getType() + ")\t| Price: " + item.getPrice());
        }
        System.out.println("0 - Back");
        System.out.print("Select an option: ");
    }

    // Menampilkan pesan pembelian berhasil
    public void displayPurchaseSuccess(String itemName) {
        System.out.println("You bought " + itemName + "!");
    }

    // Menampilkan pesan jika gold tidak mencukupi
    public void displayInsufficientGold() {
        System.out.println("You don't have enough money to buy this item.");
    }

    // Menampilkan pesan pilihan tidak valid
    public void displayInvalidOption() {
        System.out.println("Invalid option. Try again.");
    }

    // Menampilkan pesan untuk fitur yang belum tersedia
    public void displayChangeFragment() {
        System.out.println("Coming soon . . .");
    }
}
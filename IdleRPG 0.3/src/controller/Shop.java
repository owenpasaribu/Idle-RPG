package controller;

import java.util.List;
import java.util.Scanner;
import model.Item;
import model.Player;
import view.ShopView;

public class Shop {
    private Player player;
    private List<Item> items;
    private ShopView shopView;

    public Shop(Player player, List<Item> items) {
        this.player = player;
        this.items = items;
        this.shopView = new ShopView();
    }

    public void displayShop(Scanner scanner) {
        while (true) {
            shopView.displayShopMenu(player);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleBuyItem(scanner);
                    break;

                case "2":
                    changeFragment(scanner);
                    break;

                case "0":
                    return;

                default:
                    shopView.displayInvalidOption();
            }
        }
    }

    private void handleBuyItem(Scanner scanner) {
        while (true) {
            shopView.displayItems(items, player);

            int itemIndex = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer

            if (itemIndex == 0) {
                return; // Kembali ke menu utama Shop
            } else if (itemIndex > 0 && itemIndex <= items.size()) {
                boolean success = buyItem(itemIndex);
                if (!success) {
                    continue; // Jika gagal beli, tampilkan item lagi
                }
            } else {
                shopView.displayInvalidOption();
            }
        }
    }

    public boolean buyItem(int itemIndex) {
        Item item = items.get(itemIndex - 1);
        if (player.getGold() >= item.getPrice()) {
            player.addItemToInventory(item);
            player.setGold(player.getGold() - item.getPrice());
            shopView.displayPurchaseSuccess(item.getItemName());
            return true;
        } else {
            shopView.displayInsufficientGold();
            return false;
        }
    }

    public void changeFragment(Scanner scanner) {
        shopView.displayChangeFragment();
    }
}

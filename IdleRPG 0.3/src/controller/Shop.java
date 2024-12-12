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

    public void openShop(Scanner scanner) {
        boolean exitShop = false;

        while (!exitShop) {
            String choice = shopView.getShopMenuChoice(scanner, player);

            switch (choice) {
                case "1":
                    handleBuyItem(scanner);
                    break;
                case "2":
                    changeFragment();
                    break;
                case "0":
                    exitShop = true;
                    break;
                default:
                    shopView.displayInvalidOption();
            }
        }
    }

    private void handleBuyItem(Scanner scanner) {
        while (true) {
            int itemIndex = shopView.getItemSelection(scanner, items, player);

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

    private boolean buyItem(int itemIndex) {
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

    private void changeFragment() {
        shopView.displayChangeFragment();
    }
}

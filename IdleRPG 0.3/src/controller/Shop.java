package main.controller;

import main.model.Item;
import main.model.ShopModel;
import main.view.ShopView;
import java.util.Scanner;

public class ShopController {
    private ShopModel model;
    private ShopView view;

    public ShopController(ShopModel model, ShopView view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        view.displayShop(model.getItems());
        view.showFragment(model.getFragment());
    }

    public void buyItems(Item item, int gold) {
        view.showBuyConfirmation(item);
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.next();
        if (confirmation.equalsIgnoreCase("Ya")) {
            if (model.buyItems(item, gold)) {
                System.out.println("Pembelian sukses!");
            }
        } else {
            System.out.println("Pembelian dibatalkan.");
        }
    }

    public void convertFragment() {
        model.convertFragment();
    }
}


package controller;

import java.util.List;
import java.util.Scanner;
import model.Item;
import model.Player;

public class Shop {
    private Player player;
    private List<Item> items;

    public Shop(Player player, List<Item> items) {
        this.player = player;
        this.items = items;
    }

    public void displayShop(Scanner scanner){
        System.out.println("Welcome to the shop!");
        System.out.println("You have " + player.getGold() + " gold.");

        System.out.println("1. Buy Item");
        System.out.println("2. Change Fragment");
        System.out.println("0. Exit Shop");
        System.out.print("Select menu: ");
        String choice = scanner.nextLine();
            
        switch (choice) {
            case "1":
            System.out.println("Gold : " + player.getGold());
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.println((i+1) + " - " + item.getItemName() + " ( " + item.getType() + " )\t| Price : " + item.getPrice());
            }
            System.out.println("0 - Back");
    
            int itemIndex = scanner.nextInt();
            scanner.nextLine();
            if(itemIndex > 0 && itemIndex <= items.size()){
                buyItem(itemIndex);
            }
            else if (itemIndex == 0) {
                break;
            }
            else{
                System.out.println("Invalid option. Try again.");
            }
                break;
            
            case "2":
            changeFragment(scanner);
            break;

            case "0":
            return;
        
            default:
                System.out.println("Invalid choice");
            break;
        }
    }
    
    public void buyItem(int itemIndex){
        Item item = items.get(itemIndex - 1);
            if (player.getGold()>=item.getPrice()) {
                player.addItemToInventory(item);
                player.setGold(player.getGold()-item.getPrice());
                System.out.println("You bought " + item.getItemName() + "!");
            } else {
                System.out.println("You don't have enough money to buy this item.");
            }
        
    }

    public void changeFragment(Scanner scanner){
        System.out.println("Coming soon . . .");
    }
}

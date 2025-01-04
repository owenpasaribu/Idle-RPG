package view;

import model.Player;

public class ViewStats {
    // Metode statis untuk mencetak informasi pemain
    public void displayPlayerInfo(Player player) {
        System.out.println("----- Player Info -----");
        System.out.println("Username: " + player.getUsername());
        System.out.println("Level: " + player.getLevel());
        System.out.println("EXP: " + player.getExp());
        System.out.println("HP: " + player.getHp());
        System.out.println("Attack: " + player.getAtk());
        System.out.println("Defense: " + player.getDef());
        System.out.println("Money: " + player.getGold());
        System.out.println("Fragments: " + player.getFragment());
        System.out.println("------------------------");
    }
}
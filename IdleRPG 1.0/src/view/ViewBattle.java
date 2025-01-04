package view;

public class ViewBattle {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public void WinBattle() {
    System.out.println("========================================");
    System.out.println(ANSI_GREEN + "                 Victory                " + ANSI_RESET);
    System.out.println("========================================");
    }

    public void LoseBattle() {
        System.out.println("========================================");
        System.out.println(ANSI_RED + "                 Defeat                " + ANSI_RESET);
        System.out.println("========================================");
        }
}
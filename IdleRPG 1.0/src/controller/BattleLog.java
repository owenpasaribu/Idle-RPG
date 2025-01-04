package controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BattleLog {

    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private Scanner scanner;
    
        public BattleLog() {
            scanner = new Scanner(System.in);
        }

    // Player class (simplified)
    static class Player {
        private int hp;
        private int atk;
        private final String name;

        public Player(String name, int hp, int atk) {
            this.name = name;
            this.hp = hp;
            this.atk = atk;
        }

        public int getHp() {
            return hp;
        }

        public int getAtk() {
            return atk;
        }

        public String getName() {
            return name;
        }

        public void takeDamage(int damage) {
            hp -= damage;
        }
    }

    // Monster class
    static class Monster {
        private final String name;
        private int hp;
        private final int atk;

        public Monster(String name, int hp, int atk) {
            this.name = name;
            this.hp = hp;
            this.atk = atk;
        }

        public String getName() {
            return name;
        }

        public int getHp() {
            return hp;
        }

        public int getAtk() {
            return atk;
        }

        public void takeDamage(int damage) {
            hp -= damage;
        }
    }

    // Data log untuk setiap turn
    static class TurnLog {
        int turn;
        int playerHp;
        int monsterHp;
        int damage;
        int gold;
        int exp;
        int fragment;
        String monsterName;
        String attacker;
        String target;
        int hpTarget;

        public TurnLog(int turn, String attacker, String target, int playerHp, int monsterHp, int damage, int gold, int exp, int fragment, String monsterName, int hpTarget) {
            this.turn = turn;
            this.attacker = attacker;
            this.target = target;
            this.playerHp = playerHp;
            this.monsterHp = monsterHp;
            this.damage = damage;
            this.gold = gold;
            this.exp = exp;
            this.fragment = fragment;
            this.monsterName = monsterName;
            this.hpTarget = hpTarget;
        }
    }

    // Membaca log dari beberapa file CSV
    public static List<TurnLog> readBattleLogs(String... fileNames) {
        List<TurnLog> allLogs = new ArrayList<>();
        for (String fileName : fileNames) {
            try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
                String line;
                boolean isHeader = true; // Flag untuk mengabaikan header
                while ((line = br.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false; // Abaikan header
                        continue;
                    }
                    String[] values = line.split(",");
                    if (values.length != 11) {
                        System.out.println("Skipping malformed line in " + fileName + ": " + line);
                        continue;
                    }
                    try {
                        int turn = Integer.parseInt(values[0]);
                        String attacker = values[1];
                        String target = values[2];
                        int damage = Integer.parseInt(values[3]);
                        int playerHp = Integer.parseInt(values[4]);
                        int monsterHp = Integer.parseInt(values[5]);
                        String monsterName = values[6];
                        int gold = Integer.parseInt(values[7]);
                        int exp = Integer.parseInt(values[8]);
                        int fragment = Integer.parseInt(values[9]);
                        int hpTarget = Integer.parseInt(values[10]);
                        allLogs.add(new TurnLog(turn, attacker, target, playerHp, monsterHp, damage, gold, exp, fragment, monsterName,hpTarget));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping malformed numeric value in " + fileName + ": " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading battle log from " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        return allLogs;
    }

    public void startBattleFromLog(Player player, Monster monster, List<TurnLog> logs) {
        System.out.println("==============================================");
        System.out.println("                 Battle Log");
        System.out.println("==============================================");
        System.out.println("-----------------" + ANSI_GREEN + "Battle Start" + ANSI_RESET + "-----------------");

        for (TurnLog log : logs) {
            if (log.monsterHp > 0) {
            System.out.println("[TURN " + log.turn + "]");
            System.out.println(log.attacker + " attacks " + log.target + " and deals " + log.damage + " damage.");
            System.out.println(log.target + "'s HP: " + log.hpTarget);
            } else {
                System.out.println(log.monsterName + " is defeated!");
                break;
            }
        }

        System.out.println("------------------" + ANSI_RED + "Battle End" + ANSI_RESET + "------------------");
        System.out.println("==============================================");
        printSummary(logs);
    }

    public void printSummary(List<TurnLog> logs) {
        TurnLog lastTurn = logs.get(logs.size() - 1);
        if (lastTurn.monsterHp == 0){
        System.out.println("==============================================");
        System.out.println("                Battle Summary");
        System.out.println("==============================================");
        System.out.println("Result          : " + ANSI_GREEN + "WIN" + ANSI_RESET);
        System.out.println("Total Turns     : " + lastTurn.turn);
        System.out.println("Loot            : " + lastTurn.gold + " Gold, " + lastTurn.exp + " EXP, and " + lastTurn.fragment + " Fragments");
        System.out.println("==============================================");
        }
        else {
        System.out.println("==============================================");
        System.out.println("                Battle Summary");
        System.out.println("==============================================");
        System.out.println("Result          : " + ANSI_RED + "LOSE" + ANSI_RESET);
        System.out.println("Total Turns     : " + lastTurn.turn);
        System.out.println("Loot            : " + "0 Gold, " + " 0 EXP, and " + "0 Fragments");
        System.out.println("==============================================");
        }
    }
    
    public void displayBattleLog() {
    
        try {
            while (true) {
                System.out.println("==============================================");
                System.out.println("                   Battle Log");
                System.out.println("==============================================");
                List<TurnLog> logs = readBattleLogs("src/data/battle_log_1.csv","src/data/battle_log_2.csv","src/data/battle_log_3.csv");
                if (logs.isEmpty()) {
                    System.out.println("No battle logs available.");
                } else {
                    int index = 1;
                    for (TurnLog log : logs) {
                        if (log.playerHp == 0 || log.monsterHp == 0) {
                            String result = log.monsterHp <= 0 ? "Win" : "Lose";
                            System.out.println("    " + index + ". Player VS " + log.monsterName + " (" + result + ")");
                            index++;
                        }
                    }
                }
    
                System.out.println("    0. Kembali ke menu utama");
                System.out.print("\nChoose an option: ");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "0":
                        System.out.println("Exiting to menu...");
                        return;
                    case "1":
                    String fileName = "src/data/battle_log_" + choice + ".csv";
                        List<TurnLog> selectedLogs = readBattleLogs(fileName);
                        startBattleFromLog(new Player("Player", 100, 10), new Monster("Monster", 50, 5), selectedLogs);
                        break;
                    case "2":
                    String Logs = "src/data/battle_log_" + choice + ".csv";
                        List<TurnLog> terpilih = readBattleLogs(Logs);
                        startBattleFromLog(new Player("Player", 100, 10), new Monster("Monster", 50, 5), terpilih);
                        break;
                    case "3":
                        String halo = "src/data/battle_log_" + choice + ".csv";
                        List<TurnLog> apakabar = readBattleLogs(halo);
                        startBattleFromLog(new Player("Player", 100, 10), new Monster("Monster", 50, 5), apakabar);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                } 
            } 
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    @Override
    @Deprecated
        protected void finalize() throws Throwable {
            try {
                if (scanner != null) {
                    System.out.println("Finalizing resources...");
                    scanner.close();
                }
            } finally {
                super.finalize(); // Pastikan finalize dari parent class tetap dipanggil
            }
            }
    }



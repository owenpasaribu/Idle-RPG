package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.Item;
import model.Monster;
import model.Player;
import model.Potion;
import model.Weapon;
import view.InventoryView;
import view.ViewBattle;

public class BattleSystem {
    private static final Random random = new Random();
    Scanner scanner;

    private final Player player;
    List<Monster> monsters;
    List<Weapon> weapons;
    List<Potion> potions;

    InventoryView inventoryView;
    ViewBattle viewBattle;

    public BattleSystem(Scanner scanner, Player player, List<Monster> monsters, List<Weapon> weapons, List<Potion> potions) {
        this.scanner = scanner;
        this.player = player;
        this.monsters = monsters;
        this.weapons = weapons;
        this.potions = potions;
        this.inventoryView = new InventoryView();
        this.viewBattle = new ViewBattle();
    }

    public void startBattle(){
        upgradeMonsterStats(player, monsters);
        Monster currentMonster = BattleSystem.generateMonster(player.getLevel(), monsters);
        System.out.println("Found Monster");
        currentMonster.printMonsterDetails();
        
        
        System.out.println("Do you want to fight? (Y/N)");
        String choice = scanner.nextLine();
        
        switch (choice.toUpperCase()) {
            case "N":
            if(escaped(currentMonster.getEscapePrecentage())){
                System.out.println("You chose to run away from the battle.");
                break;
            } else{
                System.out.println("You can't run away from the battle.");
            }

            case "Y":
            if (player.getEquippedWeapon() != null) {
                int weaponDamage = useWeapon(player.getEquippedWeapon()).getDamage();
                player.updateStats(0, weaponDamage, 0);
                System.out.println("You are using " + player.getEquippedWeapon().getItemName() + "! (+" + weaponDamage + " Damage)");
            }else{
                System.out.println("You are not equip any weapon right now.");
            }

                System.out.println("Do you want to use potion? (Y/N)");
                String itemChoice = scanner.nextLine();
                while (itemChoice.equalsIgnoreCase("Y"))
                {
                    List<Item> availablePotions = player.getInventory().stream()
                    .filter(item -> item.getType().equals("Potion"))
                    .toList();
                    inventoryView.displayItems(availablePotions);
                    System.out.print("Choose Item to Consume\n>> ");
                    int itemIndex = scanner.nextInt();
                    scanner.nextLine();
                    if (itemIndex == 0) {
                        break;
                    } else if (itemIndex > 0 && itemIndex <= availablePotions.size()) {
                        Item potionConsumed = availablePotions.get(itemIndex-1);
                        if (potionConsumed.getType().equals("Potion")) {
                            
                            Potion potion = usePotion(potionConsumed);
                            player.removeItemFromInventory(potionConsumed);

                            player.updateStats(potion.getHpBoost(), potion.getAtkBoost(), potion.getDefBoost());
                            
                            System.out.println("You have consumed " + availablePotions.get(itemIndex - 1).getItemName() + "!");
                            System.out.println("+" + potion.getHpBoost() + " HP\n+" + potion.getAtkBoost() + " ATK\n+" + potion.getDefBoost() + " DEF");
                        }
                        else {
                            System.out.println("Item type you want to equip is not Potion");
                        }
                    }
                    else {
                        System.out.println("Invalid item choice");
                    }
                    System.out.println("Your Stats: ");
                    player.displayPlayerInfo();
                    System.out.println("Do you want to use potion again?");
                    itemChoice = scanner.nextLine();
                }

                int playerHP = player.getHp();
                int monsterHP = currentMonster.getHp();
                List<String[]> battleLogs = new ArrayList<>();
                battleLogs.add(new String[]{"Turn", "Attacker", "Target", "Damage", "Player HP", "Monster HP", "Monster Name", "Gold", "EXP", "Fragment", "Hp Target"});
                int turn = 1;
                while (playerHP > 0 && monsterHP > 0) {
                    if ("N".equals(choice.toUpperCase())) {
                        playerHP = monsterTurn(turn++, playerHP, monsterHP, player, currentMonster, battleLogs);
                        choice = "Y";
                    }
                    monsterHP = playerTurn(turn++, monsterHP, playerHP, player, currentMonster, battleLogs);
                    
                    if (monsterHP <= 0) {
                        viewBattle.WinBattle();
                        getLoot(player, currentMonster);
                        player.resetStats();
                        break;
                    }
                    playerHP = monsterTurn(turn++, playerHP, monsterHP, player, currentMonster, battleLogs);
                    if (playerHP <= 0) {
                        viewBattle.LoseBattle();
                        LosePunishment(player, currentMonster);
                        player.resetStats();
                        break;
                    }
                }
                saveBattleLog(battleLogs);
                break;
            
            default:
                System.out.println("Invalid Option.");
        }
        
    }
    
    // Method untuk Boss Battle
    public void startBossBattle(Player currentPlayer, List<Monster> monsters) {
        //cek apakah level pemain adalah kelipatan 10
        if (currentPlayer.getLevel() % 10 != 0) {
            System.out.println("Increase your level, your level is not enough to fight the boss monster");
            return ; // Kembali ke menu utama jika level tidak memenuhi syarat
        }

        Monster boss = generateboss(currentPlayer.getLevel(), monsters);
        
        if (boss == null) {
            System.out.println("No Boss available at the moment!");
            return;
        }
        System.out.println("Battle Start!");
        
        int bossCooldown = boss.getCooldownSkill();
        
            // Menampilkan informasi boss monster
            System.out.println("Found Boss Monster");
            System.out.println("Name    : " + boss.getMonsterName());
            System.out.println("Level   : " + boss.getLevelRequirement());
            System.out.println("HP      : " + boss.getHpIndicator());
            System.out.println("ATK     : " + boss.getAtkIndicator());
            System.out.println("DEF     : " + boss.getDefIndicator());
            System.out.println("Skill   :");
            System.out.println("- " + boss.getUniqueSkill());
            System.out.println("( CD: " + boss.getCooldownSkill() + " )");
            System.out.println("Escape Chance : " + boss.getEscapePrecentage() + "%");

            System.out.println("\nPrepare Your Self!");
            System.out.println("1. Battle");
            System.out.println("2. Use Item");
            System.out.println("3. Escape");
            System.out.print("Choose an action : ");
            String bossAction = scanner.nextLine();
            
            // Implementasi aksi yang dipilih pemain (Battle, Use Item, Escape)
            switch (bossAction) {
                case "3":
                if(escaped(boss.getEscapePrecentage())){
                    System.out.println("You chose to run away from the battle.");
                    break;
                } else{
                    System.out.println("You can't run away from the battle.");
                }

                case "2":int weaponDamage = useWeapon(currentPlayer.getEquippedWeapon()).getDamage();
                currentPlayer.updateStats(0, weaponDamage, 0);
                System.out.println("You are using " + currentPlayer.getEquippedWeapon().getItemName() + "! (+" + weaponDamage + " Damage)");
    
                    System.out.println("Do you want to use potion? (Y/N)");
                    String choice = scanner.nextLine();
                    while (choice.equalsIgnoreCase("Y"))
                    {
                        List<Item> availablePotions = currentPlayer.getInventory().stream()
                        .filter(item -> item.getType().equals("Potion"))
                        .toList();
                        inventoryView.displayItems(availablePotions);
                        System.out.print("Choose Item to Consume\n>> ");
                        int itemIndex = scanner.nextInt();
                        scanner.nextLine();
                        if (itemIndex == 0) {
                            break;
                        } else if (itemIndex > 0 && itemIndex <= availablePotions.size()) {
                            Item potionConsumed = availablePotions.get(itemIndex-1);
                            if (potionConsumed.getType().equals("Potion")) {
                                
                                Potion potion = usePotion(potionConsumed);
                                currentPlayer.removeItemFromInventory(potionConsumed);
    
                                currentPlayer.updateStats(potion.getHpBoost(), potion.getAtkBoost(), potion.getDefBoost());
                                
                                System.out.println("You have consumed " + availablePotions.get(itemIndex - 1).getItemName() + "!");
                                System.out.println("+" + potion.getHpBoost() + " HP\n+" + potion.getAtkBoost() + " ATK\n+" + potion.getDefBoost() + " DEF");
                            }
                            else {
                                System.out.println("Item type you want to equip is not Potion");
                            }
                        }
                        else {
                            System.out.println("Invalid item choice");
                        }
                        System.out.println("Your Stats: ");
                        currentPlayer.displayPlayerInfo();
                        System.out.println("Do you want to use potion again?");
                        choice = scanner.nextLine();
                    }
                    
                case "1":
                    int playerHP = currentPlayer.getHp();
                    int monsterHP = boss.getHp();
                    List<String[]> battleLogs = new ArrayList<>();
                    battleLogs.add(new String[]{"Turn", "Attacker", "Target", "Damage", "Player HP", "Monster HP", "Monster Name", "Gold", "EXP", "Fragment", "Hp Target"});

                    int turn = 1;
                    while (playerHP > 0 && monsterHP > 0) {
                        monsterHP = playerTurn(turn++, monsterHP, playerHP, currentPlayer, boss, battleLogs);
                    if (monsterHP <= 0) {
                            viewBattle.WinBattle();
                            getLoot(currentPlayer, boss);
                            player.resetStats();
                            break;
                        }
                        playerHP = monsterTurn(turn++, playerHP, monsterHP, currentPlayer, boss, battleLogs);
                        if (playerHP <= 0) {
                            viewBattle.LoseBattle();
                            LosePunishment(currentPlayer, boss);
                            player.resetStats();
                            return;
                        }
                        saveBattleLog(battleLogs);
                    }
                    break;
                default:
                    System.out.println("Invalid action. Try again.");
            }
        
    
                /*if (bossCooldown == 0) {
                    System.out.println("Boss uses " + boss.getUniqueSkill() + "!");
                    int skillDamage = boss.getAtk() * 2 - player.getDef();
                    player.setHp(player.getHp() - skillDamage);
                    System.out.println("Boss dealt " + skillDamage + " damage to you!");
                    bossCooldown = boss.getCooldownSkill(); // Reset cooldown
                } else {
                    int bossDamage = boss.getAtk() - player.getDef();
                    bossDamage = Math.max(bossDamage, 0);
                    player.setHp(player.getHp() - bossDamage);
                    System.out.println("Boss attacks and deals " + bossDamage + " damage!");
                    bossCooldown--; // Kurangi cooldown
                }*/
        
        // Cek hasil pertarungan
        if (currentPlayer.getHp() <= 0) {
            System.out.println("You have been defeated...");
        } else if (boss.getHp() <= 0) {
            System.out.println("Congratulations! You defeated the boss!");
            getLoot(currentPlayer, boss);
        }
    }

    // Method untuk menghasilkan boss
    protected static Monster generateboss(int playerLevel, List<Monster> bosses) {
        List<Monster> bossMonsters = bosses.stream()
        .filter(m -> m.getType().equalsIgnoreCase("Boss"))
        .collect(Collectors.toList());

    if (!bossMonsters.isEmpty()) {
        // Mengecek apakah level pemain kelipatan 10
        if (playerLevel % 10 == 0) {
            Monster boss = bossMonsters.get(new Random().nextInt(bossMonsters.size())); // Memilih boss acak
            return boss;
        } else {
            System.out.println("You must be at a level that is a multiple of 10 to fight the Boss.");
        }
        } else {
        System.out.println("No Boss available at the moment!");
        }
        return null;
    }

    protected static Monster generateMonster(int playerLevel, List<Monster> monsters){
        List<Monster> filteredMonsters = monsters.stream().filter(monster->monster.getLevelRequirement() <= playerLevel).collect(Collectors.toList());
        if (filteredMonsters.isEmpty()) {
            throw new RuntimeException("No monsters found.");
        }
        int randomIndex = random.nextInt(filteredMonsters.size());
        return filteredMonsters.get(randomIndex);
    }

    public void upgradeMonsterStats(Player currentPlayer, List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.getType().equalsIgnoreCase("Boss")) {
                // Jika monster adalah boss monster
                // Cek apakah pemain telah melewati level kelipatan 10
                if (currentPlayer.getLevel() > monster.getLevelRequirement() && currentPlayer.getLevel() % 10 == 1) {
                    monster.setLevelRequirement((currentPlayer.getLevel() / 10) + 1);
                if (currentPlayer.getLevel() >= monster.getLevelRequirement() && currentPlayer.getLevel() % 10 == 1) {
                    // Tingkatkan level requirement boss monster
                    monster.setLevelRequirement(monster.getLevelRequirement() + 1);
    
                    // Tingkatkan statistik boss monster sebesar 35%
                    monster.setHp((int) (monster.getHp() * 1.35));
                    monster.setAtk((int) (monster.getAtk() * 1.35));
                    monster.setDef((int) (monster.getDef() * 1.35));
                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15)); // Naikkan EXP 15%
    
                    // Naikkan EXP loot sebesar 15%
                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15));
                    System.out.println("Boss Monster stats increased by 35% and EXP increased by 15%");
                }
            } else {
                // Jika monster adalah monster biasa
                // Cek apakah level pemain adalah kelipatan 3
                if (currentPlayer.getLevel() % 3 == 0) {
                    monster.setLevelRequirement((currentPlayer.getLevel() / 3) + 1);
                    // Tingkatkan level requirement monster biasa
                    monster.setLevelRequirement(monster.getLevelRequirement() + 1);
    
                    // Tingkatkan statistik monster biasa sebesar 20%
                    monster.setHp((int) (monster.getHp() * 1.20));
                    monster.setAtk((int) (monster.getAtk() * 1.20));
                    monster.setDef((int) (monster.getDef() * 1.20));
                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15)); // Naikkan EXP 15%
    
                    // Naikkan EXP loot sebesar 15%
                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15));
                    System.out.println("Normal Monster stats increased by 20% and EXP increased by 15%");
                }
            }
        }
    }
    }

    protected int playerTurn(int turn, int monsterHP, int playerHP, Player currentPlayer, Monster currentMonster, List<String[]> battleLogs) {
    int playerDamage = calculateDamage(currentPlayer.getAtk(), currentMonster.getDef());
    monsterHP = Math.max(0, monsterHP - playerDamage);
    String log = "Player attacks " + currentMonster.getMonsterName() + " for " + playerDamage + " damage. Monster HP: " + monsterHP;
    System.out.println(log);

    battleLogs.add(new String[]{
            String.valueOf(turn),
            "Player",
            currentMonster.getMonsterName(),
            String.valueOf(playerDamage),
            String.valueOf(playerHP),
            String.valueOf(monsterHP),
            currentMonster.getMonsterName(),
            String.valueOf(currentMonster.getMoneyLoot()),
            String.valueOf(currentMonster.getExpLoot()),
            String.valueOf(currentMonster.getFragmentLoot()),
            String.valueOf(monsterHP)
    });
    return monsterHP;
}

   protected int monsterTurn(int turn, int playerHP, int monsterHP, Player currentPlayer, Monster currentMonster, List<String[]> battleLogs) {
        int monsterDamage = calculateDamage(currentMonster.getAtk(), currentPlayer.getDef());
        playerHP = Math.max(0, playerHP - monsterDamage);
        String log = currentMonster.getMonsterName() + " attacks Player for " + monsterDamage + " damage. Player HP: " + playerHP;
        System.out.println(log);

        battleLogs.add(new String[]{
                String.valueOf(turn),
                currentMonster.getMonsterName(),
                "Player",
                String.valueOf(monsterDamage),
                String.valueOf(playerHP),
                String.valueOf(monsterHP),
                currentMonster.getMonsterName(),
                String.valueOf(currentMonster.getMoneyLoot()),
                String.valueOf(currentMonster.getExpLoot()),
                String.valueOf(currentMonster.getFragmentLoot()),
                String.valueOf(playerHP)
        });
        return playerHP;
    }

    protected int calculateDamage(int attack, int defense){
        int actualDefense = random.nextInt(defense);
        return Math.max(0, attack - actualDefense );
    }

    // Method untuk mendapatkan loot
    public void getLoot(Player currentPlayer, Monster currentMonster) {
        // Jika monster adalah boss (termasuk Curse Boss) di battle boss
        if (currentMonster.getType().equalsIgnoreCase("Boss")) {
            // Tidak ada pengurangan pada EXP atau gold di boss battle
            currentPlayer.gainExp(currentMonster.getExpLoot());
            currentPlayer.setGold(currentPlayer.getGold() + currentMonster.getMoneyLoot());
            currentPlayer.setFragment(currentPlayer.getFragment() + currentMonster.getFragmentLoot());
            System.out.println("Loot received:");
            System.out.println(" - EXP        : " + currentMonster.getExpLoot() + " Points");
            System.out.println(" - Money      : " + currentMonster.getMoneyLoot() + " Gold");
            System.out.println(" - Fragments  : " + currentMonster.getFragmentLoot() + " Fragments");
        } else {
            // Jika monster adalah monster biasa, periksa level pemain
            if (currentPlayer.getLevel() % 10 == 0) {
                // Jika level kelipatan 10, tidak dapat EXP pada battle biasa
                System.out.println("You are at level " + currentPlayer.getLevel() + ". No EXP gained in normal battles.");
                currentPlayer.setGold(currentPlayer.getGold() + currentMonster.getMoneyLoot());
                currentPlayer.setFragment(currentPlayer.getFragment() + currentMonster.getFragmentLoot());
                System.out.println(" - Money      : " + currentMonster.getMoneyLoot() + " Gold");
                System.out.println(" - Fragments  : " + currentMonster.getFragmentLoot() + " Fragments");
            } else {
                // Jika level bukan kelipatan 10, tetap dapat EXP, Gold, dan Fragment
                currentPlayer.gainExp(currentMonster.getExpLoot());
                currentPlayer.setGold(currentPlayer.getGold() + currentMonster.getMoneyLoot());
                currentPlayer.setFragment(currentPlayer.getFragment() + currentMonster.getFragmentLoot());
                System.out.println("Loot received:");
                System.out.println(" - EXP        : " + currentMonster.getExpLoot() + " Points");
                System.out.println(" - Money      : " + currentMonster.getMoneyLoot() + " Gold");
                System.out.println(" - Fragments  : " + currentMonster.getFragmentLoot() + " Fragments");
            }
        }
    }

    public void LosePunishment(Player currentPlayer, Monster currentMonster){
        currentPlayer.setGold(currentPlayer.getGold()-2*currentMonster.getMoneyLoot());
        currentPlayer.setGold(Math.max(0, currentPlayer.getGold()-2*currentMonster.getMoneyLoot()));
        System.out.println("Your gold has reduced " + 2*currentMonster.getMoneyLoot());
    }

    public boolean escaped(int escapePrecentage){
        int value = random.nextInt(100) + 1;
        return value <= escapePrecentage;
    }
    
    public Weapon useWeapon(Item equippedWeapon){
        Weapon weaponUsed = findWeaponByName(weapons, equippedWeapon.getItemName());
        return weaponUsed;
    }

    public Potion usePotion(Item potion){
        Potion potionUsed = findPotionByName(potions, potion.getItemName());
        return potionUsed;
    }

    public static Weapon findWeaponByName(List<Weapon> weapons, String name){
        for (Weapon weapon : weapons) {
            if (weapon.getItemName().equals(name)) {
                return weapon;
            }
        }
        return null;
    }
    
    public static Potion findPotionByName(List<Potion> potions, String name){
        for (Potion potion : potions) {
            if (potion.getItemName().equals(name)) {
                return potion;
            }
        }
        return null;
    }

    private void saveBattleLog(List<String[]> battleLogs) {
        try {
            // File paths for the battle logs
            String file1 = "src/data/battle_log_1.csv";
            String file2 = "src/data/battle_log_2.csv";
            String file3 = "src/data/battle_log_3.csv";

            // Delete the oldest log (file3)
            File fileToDelete = new File(file3);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }

            // Move existing logs down the stack
            if (new File(file2).exists()) {
                Files.move(Paths.get(file2), Paths.get(file3), StandardCopyOption.REPLACE_EXISTING);
            }
            if (new File(file1).exists()) {
                Files.move(Paths.get(file1), Paths.get(file2), StandardCopyOption.REPLACE_EXISTING);
            }

            // Save the new battle log to file1
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file1))) {
                for (String[] log : battleLogs) {
                    writer.write(String.join(",", log));
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error saving battle log: " + e.getMessage());
        }
    }
}

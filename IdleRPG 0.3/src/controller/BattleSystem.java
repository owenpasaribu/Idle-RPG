package controller;

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
    private static Random random = new Random();
    Scanner scanner;

    List<Weapon> weapons;
    List<Potion> potions;

    InventoryView inventoryView;
    ViewBattle viewBattle = new ViewBattle();

    public BattleSystem(Scanner scanner, List<Weapon> weapons, List<Potion> potions) {
        this.weapons = weapons;
        this.potions = potions;
        this.scanner = scanner;
        this.inventoryView = new InventoryView();
    }

    public void startBattle(Player currentPlayer, List<Monster> monsters){
        // Upgrade semua monster jika level pemain kelipatan 3
        upgradeMonsterStats(currentPlayer, monsters);
        
        Monster currentMonster = BattleSystem.generateMonster(currentPlayer.getLevel(), monsters);
        System.out.println("Found Monster");
        currentMonster.printMonsterDetails();
        
        
        System.out.println("Do you want to fight? (Y/N)");
        String choice = scanner.nextLine();
        
        switch (choice.toUpperCase()) {
            case "N":
            if (escaped(currentMonster.getEscapePrecentage())) {
                System.out.println("You chose to run away from the battle.");
                break;
            } else {
                System.out.println("You can't run away from the battle.");
                
                // Periksa apakah ingin menggunakan potion
                System.out.println("Do you want to use potion? (Y/N)");
                choice = scanner.nextLine();
                while (choice.equalsIgnoreCase("Y")) {
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
                        Item potionConsumed = availablePotions.get(itemIndex - 1);
                        Potion potion = usePotion(potionConsumed);
                        currentPlayer.removeItemFromInventory(potionConsumed);
                        currentPlayer.updateStats(potion.getHpBoost(), potion.getAtkBoost(), potion.getDefBoost());
                        System.out.println("You have consumed " + potionConsumed.getItemName() + "!");
                        System.out.println("+" + potion.getHpBoost() + " HP\n+" + potion.getAtkBoost() + " ATK\n+" + potion.getDefBoost() + " DEF");
                    } else {
                        System.out.println("Invalid item choice");
                    }
                    System.out.println("Do you want to use potion again? (Y/N)");
                    choice = scanner.nextLine();
                }

                // Periksa apakah senjata tidak dilengkapi
                if (currentPlayer.getEquippedWeapon() != null) {
                    int weaponDamage = useWeapon(currentPlayer.getEquippedWeapon()).getDamage();
                    currentPlayer.updateStats(0, weaponDamage, 0);
                    System.out.println("You are using " + currentPlayer.getEquippedWeapon().getItemName() + "! (+" + weaponDamage + " Damage)");
                } else {
                    System.out.println("You are not equipped with any weapon right now.");
                }

                // Tampilkan statistik pemain
                System.out.println("Your Stats: ");
                currentPlayer.displayPlayerInfo();

                // Mulai battle dengan monster menyerang terlebih dahulu
                int playerHP = currentPlayer.getHp();
                int monsterHP = currentMonster.getHp();
                while (playerHP > 0 && monsterHP > 0) {
                    playerHP = monsterTurn(playerHP, currentPlayer, currentMonster);
                    if (playerHP <= 0) {
                        System.out.println("You Lose!");
                        LosePunishment(currentPlayer, currentMonster);
                        currentPlayer.resetStats();
                        return;
                    }

                    monsterHP = playerTurn(monsterHP, currentPlayer, currentMonster);
                    if (monsterHP <= 0) {
                        System.out.println("You win!");
                        getLoot(currentPlayer, currentMonster);
                        currentPlayer.resetStats();
                        break;
                    }
                }
            }
            break;

            case "Y":
            if (currentPlayer.getEquippedWeapon() != null) {
                int weaponDamage = useWeapon(currentPlayer.getEquippedWeapon()).getDamage();
                currentPlayer.updateStats(0, weaponDamage, 0);
                System.out.println("You are using " + currentPlayer.getEquippedWeapon().getItemName() + "! (+" + weaponDamage + " Damage)");
            }else{
                System.out.println("You are not equip any weapon right now.");
            }

                System.out.println("Do you want to use potion? (Y/N)");
                choice = scanner.nextLine();
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
                    System.out.println("Do you want to use potion again?");
                    choice = scanner.nextLine();
                }
                
                System.out.println("Your Stats: ");
                currentPlayer.displayPlayerInfo();
                
                
                int playerHP = currentPlayer.getHp();
                int monsterHP = currentMonster.getHp();
                while (playerHP > 0 && monsterHP > 0) {
                    monsterHP = playerTurn(monsterHP, currentPlayer, currentMonster);
                    if (monsterHP <= 0) {
                        System.out.println("You win!");
                        getLoot(currentPlayer, currentMonster);
                        currentPlayer.resetStats();
                        break;
                    }
                    playerHP = monsterTurn(playerHP, currentPlayer, currentMonster);
                    if (playerHP <= 0) {
                        System.out.println("You Lose!");
                        LosePunishment(currentPlayer, currentMonster);
                        currentPlayer.resetStats();
                        return;
                    }
                }
                break;
            
            default:
                System.out.println("Invalid Option.");
        }
        
    }

    protected static Monster generateMonster(int playerLevel, List<Monster> monsters){
        List<Monster> filteredMonsters = monsters.stream().filter(monster->monster.getLevelRequirement() <= playerLevel).collect(Collectors.toList());
        if (filteredMonsters.isEmpty()) {
            throw new RuntimeException("No monsters found.");
        }
        int randomIndex = random.nextInt(filteredMonsters.size());
        return filteredMonsters.get(randomIndex);
    }

    protected int playerTurn(int monsterHP, Player currentPlayer, Monster currentMonster){
        int playerDamage = calculateDamage(currentPlayer.getAtk(), currentMonster.getDef());

        monsterHP = Math.max(0, (monsterHP - playerDamage));
            System.out.println("You dealt " + playerDamage + " damage. Enemy HP: " + monsterHP);
            
        return monsterHP;
    }

    protected int monsterTurn(int playerHP, Player currentPlayer, Monster currentMonster){
        int monsterDamage = calculateDamage(currentMonster.getAtk(), currentPlayer.getDef());

        playerHP = Math.max(0, (playerHP - monsterDamage));
            System.out.println(currentMonster.getMonsterName() + " dealt "  + monsterDamage + " damage. Player HP: " + playerHP);
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
        int punishmentGold = 2 * currentMonster.getMoneyLoot();
        int remainingGold = currentPlayer.getGold() - punishmentGold;
    
        // Menjamin pemain tidak kehilangan lebih banyak emas daripada yang mereka miliki
        if (remainingGold < 0) {
            remainingGold = 0;
        }
    
        currentPlayer.setGold(remainingGold);
        System.out.println("Your gold has reduced by " + punishmentGold + ". You now have " + remainingGold + " gold left.");
    }

    public boolean escaped(int escapePrecentage){
        int value = random.nextInt(100) + 1;
        return value <= escapePrecentage;
    }
    
    // Method untuk Boss Battle
    public void startBossBattle(Player currentPlayer, List<Monster> monsters) {
        // Upgrade semua stats boss monster jika level pemain melewati level 10 (11,21,31,dst..)
        upgradeMonsterStats(currentPlayer, monsters);
        
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
                        System.out.println("Do you want to use potion again?");
                        choice = scanner.nextLine();
                    }
                    
                    System.out.println("Your Stats: ");
                    currentPlayer.displayPlayerInfo();
                    break;
                case "1":
                    int playerHP = currentPlayer.getHp();
                    int monsterHP = boss.getHp();
                    
                    while (playerHP > 0 && monsterHP > 0) {
                        monsterHP = playerTurn(monsterHP, currentPlayer, boss);
                        if (monsterHP <= 0) {
                            viewBattle.WinBattle();
                            getLoot(currentPlayer, boss);
                            break;
                        }
                        playerHP = monsterTurn(playerHP, currentPlayer, boss);
                        if (playerHP <= 0) {
                            viewBattle.LoseBattle();
                            return;
                        }
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
    
    public void upgradeMonsterStats(Player currentPlayer, List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.getType().equalsIgnoreCase("Boss")) {
                // Jika monster adalah boss monster
                // Cek apakah pemain telah melewati level kelipatan 10
                if (currentPlayer.getLevel() > monster.getLevel() && currentPlayer.getLevel() % 10 == 1) {
                    monster.setLevel((currentPlayer.getLevel() / 10) + 1);
                    // Tingkatkan statistik boss monster sebesar 35%
                    monster.setHp((int) (monster.getHp() * 1.35));
                    monster.setAtk((int) (monster.getAtk() * 1.35));
                    monster.setDef((int) (monster.getDef() * 1.35));

                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15)); // Naikkan EXP 15%
                    System.out.println("Boss Monster stats increased by 35% and EXP increased by 15%");
                }
            } else {
                // Jika monster adalah monster biasa
                // Cek apakah level pemain adalah kelipatan 5
                if (currentPlayer.getLevel() % 3 == 0) {
                    monster.setLevel((currentPlayer.getLevel() / 3) + 1);
                    // Tingkatkan statistik monster biasa sebesar 20%
                    monster.setHp((int) (monster.getHp() * 1.20));
                    monster.setAtk((int) (monster.getAtk() * 1.20));
                    monster.setDef((int) (monster.getDef() * 1.20));
                    monster.setExpLoot((int) (monster.getExpLoot() * 1.15)); // Naikkan EXP 15%
                    System.out.println("Normal Monster stats increased by 20% and EXP increased by 15%");
                }
            }
        }
    }

    // public boolean useSkill(){}

    // public int checkSkill(String skillName){
    //     int skillDamage = 0;
    //     switch (skillName) {
    //         case "Poison":
                
    //             break;
    //         case "Paralyze":
                
    //             break;
    //         case "Bleeding":
                
    //             break;
    //         case "Drain":
                
    //             break;
        
    //         default:
    //             break;
    //     }

    //     return skillDamage;
    // }
    
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
}

package controller;

import model.Monster;
import model.Player;
import view.ViewBattle;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BattleSystem {
    private static Random random = new Random();
    ViewBattle viewBattle = new ViewBattle();
    Scanner scanner = new Scanner(System.in);

    // Method untuk battle biasa
    public void startBattle(Player currentPlayer, List<Monster> monsters) {
        Monster currentMonster = generateMonster(currentPlayer.getLevel(), monsters);
        
        // Cek jika monster adalah boss, jika iya jangan lanjutkan
        if (currentMonster.getType().equalsIgnoreCase("Boss")) {
            System.out.println("You can't fight a boss here! Only available in Boss Battle.");
            return;
        }
        
        System.out.println("Found Monster");
        currentMonster.printMonsterDetails();
        System.out.println("Do you want to fight? (Y/N)");
        String choice = scanner.nextLine();
        
        switch (choice.toUpperCase()) {
            case "Y":
                int playerHP = currentPlayer.getHp();
                int monsterHP = currentMonster.getHp();

                while (playerHP > 0 && monsterHP > 0) {
                    monsterHP = playerTurn(monsterHP, currentPlayer, currentMonster);
                    if (monsterHP <= 0) {
                        viewBattle.WinBattle();
                        getLoot(currentPlayer, currentMonster);
                        break;
                    }
                    playerHP = monsterTurn(playerHP, currentPlayer, currentMonster);
                    if (playerHP <= 0) {
                        viewBattle.LoseBattle();
                        return;
                    }
                }
                break;

            case "N":
                if (escaped(currentMonster.getEscapePrecentage())) {
                    System.out.println("You chose to run away from the battle.");
                    return;
                } else {
                    System.out.println("You can't run away from the battle.");
                }
                
                playerHP = currentPlayer.getHp();
                monsterHP = currentMonster.getHp();
                
                while (playerHP > 0 && monsterHP > 0) {
                    monsterHP = playerTurn(monsterHP, currentPlayer, currentMonster);
                    if (monsterHP <= 0) {
                        viewBattle.WinBattle();
                        getLoot(currentPlayer, currentMonster);
                        break;
                    }
                    playerHP = monsterTurn(playerHP, currentPlayer, currentMonster);
                    if (playerHP <= 0) {
                        viewBattle.LoseBattle();
                        return;
                    }
                }
                break;

            default:
                System.out.println("Invalid Option.");
        }
    }

    // Method untuk menghasilkan monster
    protected static Monster generateMonster(int playerLevel, List<Monster> monsters) {
        List<Monster> filteredMonsters = monsters.stream().filter(monster -> monster.getLevelRequirement() <= playerLevel).collect(Collectors.toList());
        if (filteredMonsters.isEmpty()) {
            throw new RuntimeException("No monsters found.");
        }
        int randomIndex = random.nextInt(filteredMonsters.size());
        return filteredMonsters.get(randomIndex);
    }

    // Method untuk battle player
    protected int playerTurn(int monsterHP, Player currentPlayer, Monster currentMonster) {
        int playerDamage = calculateDamage(currentPlayer.getAtk(), currentMonster.getDef());

        monsterHP = Math.max(0, (monsterHP - playerDamage));
        System.out.println("You dealt " + playerDamage + " damage. Monster HP: " + monsterHP);
        
        return monsterHP;
    }

    // Method untuk turn monster
    protected int monsterTurn(int playerHP, Player currentPlayer, Monster currentMonster) {
        int monsterDamage = calculateDamage(currentMonster.getAtk(), currentPlayer.getDef());

        playerHP = Math.max(0, (playerHP - monsterDamage));
        System.out.println(currentMonster.getMonsterName() + " dealt " + monsterDamage + " damage. Your HP: " + playerHP);
        
        return playerHP;
    }

    // Method untuk perhitungan damage
    protected int calculateDamage(int attack, int defense) {
        int actualDefense = random.nextInt(defense);
        return Math.max(0, attack - actualDefense);
    }

    // Method untuk mendapatkan loot
    public void getLoot(Player currentPlayer, Monster currentMonster) {
        currentPlayer.gainExp(currentMonster.getExpLoot());
        currentPlayer.setGold(currentPlayer.getGold() + currentMonster.getMoneyLoot());
        currentPlayer.setFragment(currentPlayer.getFragment() + currentMonster.getFragmentLoot());
        System.out.println("Loot received:");
        System.out.println(" - EXP        : " + currentMonster.getExpLoot() + " Points");
        System.out.println(" - Money      : " + currentMonster.getMoneyLoot() + " Gold");
        System.out.println(" - Fragments  : " + currentMonster.getFragmentLoot() + " Fragments");
    }

    // Method untuk Boss Battle
    public void startBossBattle(Player currentPlayer, List<Monster> monsters) {
        Monster boss = generateboss(currentPlayer.getLevel(), monsters);
        
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
                case "2":
                    // Implementasi penggunaan item (belum ditentukan lebih lanjut)
                    System.out.println("You decided to use an item...");
                    break;
                case "3":
                    // Implementasi Escape (belum ditentukan lebih lanjut)
                    System.out.println("You decided to escape from the battle...");
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
            currentPlayer.addMoney(boss.getMoneyLoot());
            currentPlayer.addExp(boss.getExpLoot());
            System.out.println("You earned " + boss.getMoneyLoot() + " money and " + boss.getExpLoot() + " EXP!");
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

    // Method untuk memeriksa apakah player bisa kabur
    public boolean escaped(int escapePercentage) {
        int value = random.nextInt(100) + 1;
        return value <= escapePercentage;  // Jika nilai lebih kecil atau sama dengan persen kabur, kabur berhasil
    }
}

package controller;

import model.Monster;
import model.Player;
import view.ViewBattle;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import view.ViewMonster;

public class BattleSystem {
    private static Random random = new Random();
    ViewBattle viewBattle = new ViewBattle();
    Scanner scanner = new Scanner(System.in);


    public void startBattle(Player currentPlayer, List<Monster> monsters){
        Monster currentMonster = BattleSystem.generateMonster(currentPlayer.getLevel(), monsters);
        System.out.println("Found Monster");
        viewMonster.printMonsterDetails(currentMonster);
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
            if(escaped(currentMonster.getEscapePrecentage())){
                System.out.println("You chose to run away from the battle.");
                return;
            } else{
                System.out.println("You can't run away from the battle.");
                
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
            }
            
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

    public void useItem(){}

    protected int playerTurn(int monsterHP, Player currentPlayer, Monster currentMonster){
        int playerDamage = calculateDamage(currentPlayer.getAtk(), currentMonster.getDef());

        monsterHP = Math.max(0, (monsterHP - playerDamage));
            System.out.println(currentMonster.getMonsterName() + " dealt " + playerDamage + " damage. Enemy HP: " + monsterHP);
            
        return monsterHP;
    }

    protected int monsterTurn(int playerHP, Player currentPlayer, Monster currentMonster){
        int monsterDamage = calculateDamage(currentMonster.getAtk(), currentPlayer.getDef());

        playerHP = Math.max(0, (playerHP - monsterDamage));
            System.out.println("You dealt " + monsterDamage + " damage. Player HP: " + playerHP);
            
        return playerHP;
    }

    protected int calculateDamage(int attack, int defense){
        int actualDefense = random.nextInt(defense);
        return Math.max(0, attack - actualDefense );
    }

    public void getLoot(Player currentPlayer, Monster currentMonster){
        currentPlayer.setGold(currentPlayer.getGold() + currentMonster.getMoneyLoot());
        currentPlayer.setFragment(currentPlayer.getFragment() + currentMonster.getFragmentLoot());
        System.out.println("Hasil Jarahan:");
        System.out.println(" - EXP        : " + currentMonster.getExpLoot() + " Point");
        System.out.println(" - Money      : " + currentMonster.getMoneyLoot() + " Gold");
        System.out.println(" - Fragment   : " + currentMonster.getFragmentLoot() + " Fragment");
        currentPlayer.gainExp(currentMonster.getExpLoot());
    }



    public boolean escaped(int escapePrecentage){
        int value = random.nextInt(100) + 1;
        if (value >= escapePrecentage) {
            return false;
        } else {
            return true;
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
}

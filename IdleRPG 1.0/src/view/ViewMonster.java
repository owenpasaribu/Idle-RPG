package view;

import model.Monster;

public class ViewMonster {

    // Metode statis untuk mencetak detail monster
    public void printMonsterDetails(Monster monster) {
        System.out.println("Name\t: " + monster.getMonsterName());
        System.out.println("Level\t: " + monster.getLevelRequirement());
        System.out.println("HP\t: " + monster.getHpIndicator());
        System.out.println("ATK\t: " + monster.getAtkIndicator());
        System.out.println("DEF\t: " + monster.getDefIndicator());
        System.out.println("Skill\t:\n- " + monster.getUniqueSkill());
        System.out.println("( CD: " + monster.getCooldownSkill() + " )");
        System.out.println("Escape Chance : " + monster.getEscapePrecentage() + "%\n\n");
    }
}
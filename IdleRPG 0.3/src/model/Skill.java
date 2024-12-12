package model;

public class Skill {
    String skillName;
    String ability;
    String effect;

    public Skill(String skillName, String ability, String effect){
        this.skillName = skillName;
        this.ability = ability;
        this.effect = effect;
    }

    public void setSkillName(String skillName){
        this.skillName = skillName;
    }
    public void setAbility(String ability){
        this.ability = ability;
    }
    public void setEffect(String effect){
        this.effect = effect;
    }
    
    public String getSkillName(){
        return this.skillName;
    }
    public String getAbility(){
        return this.ability;
    }
    public String getEffect(){
        return this.effect;
    }
}

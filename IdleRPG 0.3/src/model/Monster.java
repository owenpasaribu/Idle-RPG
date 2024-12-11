package model;

public class Monster {
    
        private String monsterName;
        private String type;
        private int levelRequirement;
        private int hp;
        private int atk;
        private int def;
        private String hpIndicator;
        private String atkIndicator;
        private String defIndicator;
        private int accuracy;
        private int escapePrecentage;
        private int moneyLoot;
        private int fragmentLoot;
        private int expLoot;
        private String uniqueSkill;
        private int cooldownSkill;
    
        public Monster(String monsterName, String type, int levelRequirement, int hp, int atk, int def, String hpIndicator, String atkIndicator, String defIndicator, int accuracy, int escapePrecentage, int moneyLoot, int fragmentLoot, int expLoot, String uniqueSkill, int cooldownSkill){
            this.monsterName = monsterName;
            this.type = type;
            this.levelRequirement = levelRequirement;
            this.hp = hp;
            this.atk = atk;
            this.def = def;
            this.hpIndicator = hpIndicator;
            this.atkIndicator = atkIndicator;
            this.defIndicator = defIndicator;
            this.accuracy = accuracy;
            this.escapePrecentage = escapePrecentage;
            this.moneyLoot = moneyLoot;
            this.fragmentLoot = fragmentLoot;
            this.expLoot = expLoot;
            this.uniqueSkill = uniqueSkill;
            this.cooldownSkill = cooldownSkill;
        }
    
        public void setMonsterName(String monsterName){
            this.monsterName = monsterName;
        }
        public void setType(String type){
            this.type = type;
        }
        public void setLevelRequirement(int levelRequirement){
            this.levelRequirement = levelRequirement;
        }
        public void setHp(int hp){
            this.hp = hp;
        }
        public void setAtk(int atk){
            this.atk = atk;
        }
        public void setDef(int def){
            this.def = def;
        }
        public void setHpIndicator(String hpIndicator){
            this.hpIndicator = hpIndicator;
        }
        public void setAtkIndicator(String atkIndicator){
            this.atkIndicator = atkIndicator;
        }
        public void setDefIndicator(String defIndicator){
            this.defIndicator = defIndicator;
        }
        public void setAccuracy(int accuracy){
            this.accuracy = accuracy;
        }
        public void setEscapePrecentage(int escapePrecentage){
            this.escapePrecentage = escapePrecentage;
        }
        public void setMoneyLoot(int moneyLoot){
            this.moneyLoot = moneyLoot;
        }
        public void setFragmentLoot(int fragmentLoot){
            this.fragmentLoot = fragmentLoot;
        }
        public void setExpLoot(int expLoot){
            this.expLoot = expLoot;
        }
        public void setUniqueSkill(String uniqueSkill){
            this.uniqueSkill = uniqueSkill;
        }
        public void setCooldownSkill(int cooldownSkill){
            this.cooldownSkill = cooldownSkill;
        }
        
        public String getMonsterName(){
            return this.monsterName;
        }
        public String getType(){
            return this.type;
        }
        public int getLevelRequirement(){
            return this.levelRequirement;
        }
        public int getHp(){
            return this.hp;
        }
        public int getAtk(){
            return this.atk;
        }
        public int getDef(){
            return this.def;
        }
        public String getHpIndicator(){
            return this.hpIndicator;
        }
        public String getAtkIndicator(){
            return this.atkIndicator;
        }
        public String getDefIndicator(){
            return this.defIndicator;
        }
        public int getAccuracy(){
            return this.accuracy;
        }
        public int getEscapePrecentage(){
            return this.escapePrecentage;
        }
        public int getMoneyLoot(){
            return this.moneyLoot;
        }
        public int getFragmentLoot(){
            return this.fragmentLoot;
        }
        public int getExpLoot(){
            return this.expLoot;
        }
        public String getUniqueSkill(){
            return this.uniqueSkill;
        }
        public int getCooldownSkill(){
            return this.cooldownSkill;
        }

        public void useSkill(){}

    public void itemDrop(){}

    public void cdSkill(){}

    public void printMonsterDetails(){
        System.out.println("Name\t: " + this.monsterName);
        System.out.println("Level\t: " + this.levelRequirement);
        System.out.println("HP\t: " + this.hpIndicator);
        System.out.println("ATK\t: " + this.atkIndicator);
        System.out.println("DEF\t: " + this.defIndicator);
        
        
        System.out.println("Skill\t:\n- " + this.uniqueSkill);
        System.out.println("( CD: " + this.cooldownSkill + " )");
        System.out.println("Escape Chance : " + this.escapePrecentage + "%\n\n");
    }
}

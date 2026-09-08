package cn.campsg.practical.bubble.entity;

public class Star {
    public Position position;
    public StarType type;
    public Star(Position position, StarType type) {
        this.position = position;
        this.type = type;
    }
    public Position getPosition(){
        return position;
    }
    public StarType getType(){
        return type;
    }
    public void setPosition(Position position){
        this.position = position;
    }

    public void setType(StarType type){
        this.type = type;
    }
    public Star(){
        this.position = new Position(0, 0);
        this.type = StarType.BLUE;
    }

    @Override
    public String toString() {
        return position.toString() + ",type:" + type;
    }

    public enum StarType {
        BLUE(0),
        GREEN(1),
        YELLOW(2),
        RED(3),
        PURPLE(4);
        private int value;

        StarType(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
        public static StarType valueOf(int value){
            switch (value) {
                case 0: return BLUE;
                case 1: return GREEN;
                case 2: return YELLOW;
                case 3: return RED;
                case 4: return PURPLE;
                default: throw new IllegalArgumentException("Invalid value: " + value);
            }
        }


    }
}
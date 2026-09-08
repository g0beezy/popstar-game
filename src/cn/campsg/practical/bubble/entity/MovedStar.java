package cn.campsg.practical.bubble.entity;

public class MovedStar extends Star {
    private Position unmovedPosition = new Position();

    public Position getUnmovedPosition(){
        return unmovedPosition;
    }
    public void setUnmovedPosition(Position position){
        this.unmovedPosition = position;
    }
    public MovedStar(Position position, StarType type, Position unmovedPosition){
        super(position, type);
        this.unmovedPosition = unmovedPosition;
    }

    @Override
    public String toString() {
        return super.toString() + "\nnew " + unmovedPosition.toString();
    }

    public static void main(String args[]){
        MovedStar star = new MovedStar(new Position(0, 0), StarType.RED, new Position(1, 1));
        System.out.println(star);
    }
}
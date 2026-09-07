package cn.campsg.practical.bubble.entity;

import java.util.ArrayList;
import java.util.List;

public class StarList {
    private List<Star> stars = new ArrayList<>();

    public void add(Star star) {
        stars.add(star);
    }

    public Star get(int index) {
        return stars.get(index);
    }

    public int size() {
        return stars.size();
    }

    public Star getStar(int row, int column){
        for(int i = 0;i<stars.size();i++){
            Star s = stars.get(i);
            if(s.getPosition().getRow() == row && s.getPosition().getColumn() == column){
                return s;
            }
        }
        return null;

        
    }

    public void removeStar(int row, int column){
        for(int i =0;i<stars.size();i++){
            Star s = stars.get(i);
            if(s.getPosition().getRow() == row && s.getPosition().getColumn() == column){
                stars.remove(i);
                return;
            }

        }
    }

    public boolean contains(Star star){
        for(int i=0;i<stars.size();i++){
            if(stars.get(i).getPosition().getRow() == star.getPosition().getRow() && stars.get(i).getPosition().getColumn() == star.getPosition().getColumn()){
                return true;
            }
                
        }
        return false;
        
    }
}

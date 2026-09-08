package cn.campsg.practical.bubble.service;

import cn.campsg.practical.bubble.entity.Position;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.entity.MovedStar;
import java.util.Random;
/**
 * 
 * 泡泡糖业务逻辑实现类，主要提供以下功能：<br>
 * 1. 生成游戏界面的泡泡糖<br>
 * 2. 过关判断<br>
 * 3. 消除泡泡糖及计算得分<br>
 * 4. 对剩余泡泡糖的得分结算<br>
 * 
 * @author Frank.Chen
 * @version 2.5
 *
 */
public class StarServiceImpl implements StarService {

/**
 * 生成游戏界面的泡泡糖（10 * 10）
 * 
 * @return 泡泡糖列表-以集合表示
 */

@Override
public StarList createStars(){
    StarList starlist = new StarList();
    Random random = new Random();
    for (int row = 0; row < MAX_ROW_SIZE;row++){
        for (int column = 0; column < MAX_COLUMN_SIZE; column++){
            int r = random.nextInt(STAR_TYPES);
            starlist.add(new Star(new Position(row,column), Star.StarType.valueOf(r)));
        }
    }
    return starlist;
}
public StarList tobeClearedStars(Star base, StarList currentStarList){
    StarList clearedList = new StarList();
    clearedList.add(base);
    lookupByPath(base,currentStarList,clearedList);
    if(clearedList.size() < 2){
        return new StarList();

    }
    return clearedList;
}

private void lookupByPath(Star base, StarList currentStarList, StarList clearedStars){
    int row = base.getPosition().getRow();
    int column = base.getPosition().getColumn();
    Star left = currentStarList.getStar(row, column - 1);
    if(left != null && left.getType() == base.getType() && !clearedStars.contains(left)){
        clearedStars.add(left);
        lookupByPath(left,currentStarList,clearedStars);

    }
    Star right = currentStarList.getStar(row, column+1);
    if(right != null && right.getType() == base.getType() && !clearedStars.contains(right)){
        clearedStars.add(right);
        lookupByPath(right,currentStarList,clearedStars);
    }
    Star up = currentStarList.getStar(row - 1 , column);
    if(up != null && up.getType() == base.getType() && !clearedStars.contains(up)){
        clearedStars.add(up);
        lookupByPath(up,currentStarList,clearedStars);
    }
    Star down = currentStarList.getStar(row + 1 , column);
    if(down != null && down.getType() == base.getType() && !clearedStars.contains(down)){
        clearedStars.add(down);
        lookupByPath(down,currentStarList,clearedStars);
    }
}


@Override
public StarList getYMovedStars(StarList clearStars, StarList currentStarList) {
        StarList movedStar = new StarList();
        for(int i = 0;i<currentStarList.size();i++){
        Star s = currentStarList.get(i);
        if(s == null){
            continue;
        }
        int row = s.getPosition().getRow();
        int col = s.getPosition().getColumn();
        int emptycount = 0;
        for(int r = row +1;r< MAX_ROW_SIZE;r++){
            if(currentStarList.getStar(r, col) == null){
                emptycount ++;
            }

        }
        
        if(emptycount > 0){
           Position newPos = new Position(row + emptycount, col);
           Position oldPos = s.getPosition();
           movedStar.add(new MovedStar(newPos, s.getType(),oldPos));

    }
    
   }
   return movedStar;
     // 空实现，以后再做移动功能
}

/**
 * 消除泡泡糖后，获取水平移动的泡泡糖列表（仅水平方向移动）<br>
 * 该功能固定在垂直方向移动之后执行
 * 
 * @param currentStarList
 *            当前游戏界面的泡泡糖列表（已被消除的泡泡糖以null表示）
 * 
 * @return 水平移动的泡泡糖列表
 */
public StarList getXMovedStars(StarList currentStarList) {
    return null;
}

/**
 * 判断是否还存在未被消除的泡泡糖
 * 
 * @param currentStarList
 *            当前游戏界面的泡泡糖列表（已被消除的泡泡糖以null表示）
 * @return true:依然存在未消除的泡泡糖, false:没有未消除的泡泡糖
 * 
 */
@Override
public boolean tobeEliminated(StarList currentStarList) {
return false;
}

/**
 * 获取无法消除的泡泡糖列表
 * 
 * @param curretStars
 *            当前游戏界面的泡泡糖列表（已被消除的泡泡糖以null表示）
 * @return 无法消除的泡泡糖列表
 * */
public StarList getAwardStarList(StarList curretStars) {
return null;
}

}
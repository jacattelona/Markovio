package agents.CattelonaCampbell;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import java.util.Arrays;

public class BehaviorTree {
    boolean jumpFlag = false;
    Tasks tasks;

    public BehaviorTree(){
        tasks = new Tasks();
    }

    public boolean[] chooseActions(MarioForwardModel model, MarioTimer timer){

        //default walk right
        boolean actions[] = new boolean[]{false, true, false, false, false};
        int info[][] = model.getScreenCompleteObservation(0, 0);
        int pos[] = model.getMarioScreenTilePos();

        //Obstacle or Gap
        if (tasks.ObstacleAhead(model) || ((info[pos[0] + 1][15] != 17 || info[pos[0] + 2][15] != 17))){

            if (tasks.ObstacleAhead(model))
                actions = tasks.JumpOnObstacle(model, timer);

            // else if gap jump across
            else if ((info[pos[0] + 1][15] != 17 || info[pos[0] + 2][15] != 17)
                    && info[pos[0] + 1][pos[1] + 1] == 0){
                actions[4] = true;
            }
        }



        //Enemy or Powerup
        else if (!Arrays.equals(new int[]{-1, -1}, tasks.findClosestEnemy(model, 3)) ||
                (!Arrays.equals(new int[]{-1, -1}, tasks.findClosestPowerup(model, 8)))) {

            if (!Arrays.equals(new int[]{-1, -1}, tasks.findClosestEnemy(model, 3)))
                actions = tasks.HopOnEnemy(model, timer, 3);

            else if (!Arrays.equals(new int[]{-1, -1}, tasks.findClosestPowerup(model, 8))) {
                actions = tasks.GetPowerup(model, timer, 8);
            }
        }


        // else if question block punch it
        else if (tasks.HitQuestionBlock(model, timer) != null){
            actions = tasks.HitQuestionBlock(model, timer);
        }


        //reset jump flat
        if (jumpFlag && model.isMarioOnGround()){
            jumpFlag = false;
            actions[4] = false;
        }

        if (!jumpFlag && actions[4]){
            jumpFlag = true;
        }

        return actions;
    }
}

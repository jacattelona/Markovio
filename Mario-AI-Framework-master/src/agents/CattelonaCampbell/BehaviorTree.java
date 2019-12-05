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

        boolean actions[] = new boolean[]{false, true, false, false, false};
        int info[][] = model.getScreenCompleteObservation(0, 0);
        int pos[] = model.getMarioScreenTilePos();

        if (tasks.ObstacleAhead(model)){
            actions = tasks.JumpOnObstacle(model, timer);
        }
        // TODO: else if gap jump across
        else if (!Arrays.equals(new int[]{-1, -1}, tasks.findClosestEnemy(model, 5))) {
            actions = tasks.HopOnEnemy(model, timer, 5);
        }
        // TODO: else if power up on screen, catch it
        // TODO: else if question block punch it

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

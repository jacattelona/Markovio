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
        /*
        if (once){
            int go[][] = model.getScreenCompleteObservation(0, 0);
            for (int y = 0; y < go.length; y++){
                for (int x = 0; x < go[y].length; x++){
                    System.out.print(go[x][y]);
                    if (go[x][y] < 9)
                        System.out.print("  ");
                    else
                        System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println("------------");

            int go2[][] = model.getMarioCompleteObservation(0, 0);
            for (int y = 0; y < go2.length; y++){
                for (int x = 0; x < go2[y].length; x++){
                    System.out.print(go2[x][y]);
                    if (go2[x][y] < 9)
                        System.out.print("  ");
                    else
                        System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println("------------");
            once = false;
        }
        */

        boolean actions[] = new boolean[]{false, true, false, false, false};
        int info[][] = model.getScreenCompleteObservation(0, 0);
        int pos[] = model.getMarioScreenTilePos();

        if (info[pos[0]+2][pos[1]] != 0){
            actions[4] = true;
            actions[1] = false;

            if (!model.isMarioOnGround()) actions[4] = true;
        }
        else if (info[pos[0]+1][15] == 0) actions[4] = true;
        else{

            if (!Arrays.equals(tasks.findClosestEnemy(model, 5), new int[]{-1, -1})){
                actions = tasks.HopOnEnemy(model, timer, 5);
            }
            else{
                actions = tasks.HitQuestionBlock(model, timer);
            }
            */
        }

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

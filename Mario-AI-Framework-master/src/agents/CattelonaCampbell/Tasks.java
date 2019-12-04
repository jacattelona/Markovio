package agents.CattelonaCampbell;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class Tasks {

    public static boolean HopOnEnemy(MarioForwardModel model, MarioTimer timer){
        return true;
    }

    public static boolean HitQuestionBlock(MarioForwardModel model, MarioTimer timer){
        // get the info of what's on screen
        int info[][] = model.getScreenCompleteObservation(0, 0);
        // get Mario's location on screen
        int pos[] = model.getMarioScreenTilePos();

        // check space above Mario for ? Block
        boolean q_block = false;
        for (int i = 1; i < 4; i++){
            if (info[pos[0] + 1][pos[1] - i] == 24 || info[pos[0]][pos[1] - i] == 22){
                q_block = true;
            }
        }

        // if it's there, return true so he can jump for it
        if (q_block){
            return true;
        }
        return false;
    }
}

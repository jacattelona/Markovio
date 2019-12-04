package agents.CattelonaCampbell;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class Tasks {

    public Tasks() {
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

    private double getDistance(int[] pointA, int[] pointB){
        double x = Math.pow(pointB[0] - pointA[0], 2);
        double y = Math.pow(pointB[1] - pointA[1], 2);
        return Math.abs(Math.sqrt(x + y));
    }

    public int[] findClosestEnemy(MarioForwardModel model, int radius){
        int closestEnemy[] = new int[]{-1, -1};
        double closestDistance = 1000;
        int enemyInfo[][] = model.getScreenEnemiesObservation(0);
        int pos[] = model.getMarioScreenTilePos();

        for (int y = pos[1] - radius; y < pos[1] + radius; y++){
            for (int x = pos[0] - radius; x < pos[1] + radius; x++){
                if ((y >= 0 && x >= 0) && (y < enemyInfo[0].length && x < enemyInfo.length)){
                    if (enemyInfo[x][y] != 0){
                        int enemyPos[] = new int[]{x, y};
                        double newDistance = getDistance(pos, enemyPos);
                        if (newDistance < closestDistance){
                            closestDistance = newDistance;
                            closestEnemy = enemyPos;
                        }
                    }
                }

            }
        }
        return closestEnemy;
    }

    public boolean[] HopOnEnemy(MarioForwardModel model, MarioTimer timer, int radius){
        boolean actions[] = new boolean[]{false, false, false, false, false};

        int pos[] = model.getMarioScreenTilePos();

        int closestEnemy[] = findClosestEnemy(model, radius);

        if (closestEnemy[0] > pos[0]) actions[1] = true;
        if (closestEnemy[0] <= pos[0]){
            actions[0] = true;
            actions[3] = true;
        }


        if (Math.abs(closestEnemy[0] - pos[0]) < radius && (pos[1] > closestEnemy[1]-1)){
            actions[4] = true;
        }

        return actions;
    }

    public boolean[] HopOverObstacle(MarioForwardModel model, MarioTimer timer){
        boolean actions[] = new boolean[]{false, false, false, false, false};
        // get the info of what's on screen
        int info[][] = model.getScreenSceneObservation(0);
        // get Mario's location on screen
        int pos[] = model.getMarioScreenTilePos();


        return actions;
    }
}

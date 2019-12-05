package agents.CattelonaCampbell;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class Tasks {
    private int obstacleEdge = 0;

    public Tasks() {
    }

    // returns actions needed for Mario to hit a question block
    public boolean[] HitQuestionBlock(MarioForwardModel model, MarioTimer timer){
        boolean actions[] = new boolean[]{false, true, false, false, false};   // initialize array of Mario actions
        int info[][] = model.getScreenCompleteObservation(0, 0);    // get the info of what's on screen
        int pos[] = model.getMarioScreenTilePos();  // get Mario's location on screen

        // check space above Mario for ? Block
        boolean q_block = false;
        for (int i = 1; i < 4; i++){
            if (pos[1] - i > 0 && pos[1] - i < info.length && pos[0]-1 >= 0 && pos[0]+1 <= info.length) {
                if (info[pos[0] - 1][pos[1] - i] == 24) {
                    q_block = true;
                    actions[0] = true;
                }
                else if (info[pos[0]][pos[1] - i] == 24) {
                    q_block = true;
                    actions[0] = true;
                }
                else if (info[pos[0] + 1][pos[1] - i] == 24) {
                    q_block = true;
                    actions[1] = true;
                }
            }
        }

        // if it's there, return actions so he can jump for it
        if (q_block){
            actions[4] = true;
            return actions;
        }
        return null;
    }

    private double getDistance(int[] pointA, int[] pointB){
        double x = Math.pow(pointB[0] - pointA[0], 2);
        double y = Math.pow(pointB[1] - pointA[1], 2);
        return Math.abs(Math.sqrt(x + y));
    }


    public int[] findClosestEnemy(MarioForwardModel model, int radius){
        int closestEnemy[] = new int[]{-1, -1};
        double closestDistance = 1000;
        int enemyInfo[][] = model.getScreenEnemiesObservation(1);
        int pos[] = model.getMarioScreenTilePos();

        for (int y = pos[1]; y < pos[1] + radius; y++){
            for (int x = pos[0] - radius + 2; x < pos[1] + radius; x++){
                if ((y >= 0 && x >= 0) && (y < enemyInfo[0].length && x < enemyInfo.length)){
                    if (enemyInfo[x][y] == MarioForwardModel.OBS_STOMPABLE_ENEMY || enemyInfo[x][y] == MarioForwardModel.OBS_NONSTOMPABLE_ENEMY){
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

    public int[] findClosestPowerup(MarioForwardModel model, int radius){
        int closestPowerup[] = new int[]{-1, -1};
        double closestDistance = 1000;
        int enemyInfo[][] = model.getScreenEnemiesObservation(0);
        int pos[] = model.getMarioScreenTilePos();

        for (int y = pos[1] - 1; y < pos[1] + radius; y++){
            for (int x = pos[0] - radius; x < pos[1] + radius; x++){
                if ((y >= 0 && x >= 0) && (y < enemyInfo[0].length && x < enemyInfo.length)){
                    if (enemyInfo[x][y] == MarioForwardModel.OBS_MUSHROOM){
                        int powerupPos[] = new int[]{x, y};
                        double newDistance = getDistance(pos, powerupPos);
                        if (newDistance < closestDistance){
                            closestDistance = newDistance;
                            closestPowerup = powerupPos;
                        }
                    }
                }

            }
        }

        return closestPowerup;
    }

    public boolean[] GetPowerup(MarioForwardModel model, MarioTimer timer, int radius){
        boolean actions[] = new boolean[]{false, false, false, false, false};

        int pos[] = model.getMarioScreenTilePos();

        int closestPowerup[] = findClosestPowerup(model, radius);

        if (closestPowerup[0] > pos[0]) actions[1] = true;
        if (closestPowerup[0] <= pos[0]){
            actions[0] = true;
            //actions[3] = true;
        }


        if (Math.abs(closestPowerup[0] - pos[0]) < radius && (pos[1] > closestPowerup[1]-1)){
            actions[4] = true;
        }

        return actions;
    }

    public boolean[] HopOnEnemy(MarioForwardModel model, MarioTimer timer, int radius){
        boolean actions[] = new boolean[]{false, false, false, false, false};
        int enemyInfo[][] = model.getScreenEnemiesObservation(1);
        int pos[] = model.getMarioScreenTilePos();
        int closestEnemy[] = findClosestEnemy(model, radius);

        //if enemy to the right, move right
        if (closestEnemy[0] > pos[0]){
            actions[1] = true;

            /*
            if (closestEnemy[1] == pos[1]){
                int enemyDetail[][] = model.getScreenEnemiesObservation(0);
                if (enemyDetail[closestEnemy[0]][closestEnemy[1]] == 8){
                    System.out.println("die");
                    actions[1] = false;
                    return actions;
                }

            }
            if (closestEnemy[1] == pos[1]+1){
                actions[4] = true;
            }
            else{
                actions[1] = true;
            }
            */

        }

        //if enemy to the left, move left, unless unstompable, then move right
        if (closestEnemy[0] <= pos[0]){

            //if in array bounds
            if (closestEnemy[0] >= 0 && closestEnemy[0] < enemyInfo.length && closestEnemy[1] >= 0 && closestEnemy[1] < enemyInfo.length){

                //if stompable, move left
                if (enemyInfo[closestEnemy[0]][closestEnemy[1]] == MarioForwardModel.OBS_STOMPABLE_ENEMY){
                    actions[0] = true;
                }

                //otherwise, move right
                else{
                    actions[1] = true;
                }
            }
        }



        if (Math.abs(closestEnemy[0] - pos[0]) < radius && (pos[1] > closestEnemy[1]-1)){
            actions[4] = true;
        }

        return actions;
    }


    public boolean ObstacleAhead(MarioForwardModel model){
        int info[][] = model.getScreenSceneObservation(1);
        int pos[] = model.getMarioScreenTilePos();

        for (int y = pos[1]; y >= pos[1] - 2; y--){
            for (int x = pos[0]; x <= pos[0] + 3; x++){
                if (y >= 0 && y < model.obsGridHeight && x >= 0 && x < model.obsGridWidth){
                    //int blockVal = MarioForwardModel.getBlockValueGeneralization(info[x][y], 1);
                    int blockVal = info[x][y];
                    if (blockVal == MarioForwardModel.OBS_SOLID || blockVal == MarioForwardModel.OBS_CANNON || blockVal == MarioForwardModel.OBS_PIPE){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean[] JumpOnObstacle(MarioForwardModel model, MarioTimer timer){
        boolean actions[] = new boolean[]{false, false, false, false, false};
        int info[][] = model.getScreenSceneObservation(1);
        int pos[] = model.getMarioScreenTilePos();

        actions[4] = true;

        boolean above = false;
        for (int y = pos[1]; y < pos[1] + 3; y++){
            if (y < info[pos[0]].length){
                if (
                        info[pos[0]][y] == MarioForwardModel.OBS_SOLID ||
                        info[pos[0]][y] == MarioForwardModel.OBS_PLATFORM ||
                        info[pos[0]][y] == MarioForwardModel.OBS_PIPE
                        ){
                    above = true;

                }
            }
        }

        if (!above)
            actions[1] = true;
        /*
        if (above){
            obstacleEdge++;
            if (obstacleEdge % 3 == 0){
                obstacleEdge = 0;
                actions[0] = true;
            }
            else{
                actions[0] = false;
            }
        }
        */

        return actions;
    }
}

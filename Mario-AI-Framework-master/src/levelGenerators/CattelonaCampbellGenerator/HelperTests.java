package levelGenerators.CattelonaCampbellGenerator;

import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class HelperTests {

    void generatePipePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(10) + 10;
        int currentY = yLoc;

        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(xLoc + width, y, MarioLevelModel.NORMAL_BRICK);
        }

        for (int x = xLoc; x < xLoc + width - 2; x += r.nextInt(5)+3){
            int heightDiff = r.nextInt(7)-3;
            currentY += heightDiff;
            model.setRectangle(x, currentY, 2, 2, MarioLevelModel.PIPE);

        }
    }

    void generateEnemies(int xLoc, int yLoc, MarioLevelModel model){

    }



}

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

    void generateEnemies(int xLoc, int yLoc, int number, char type, MarioLevelModel model){
        for (int i = 0; i < number; i++){
            model.setBlock(xLoc + i*3, yLoc - 1, type);
        }
    }


    void generateRandomEnemies(int xLoc, int yLoc, int number, MarioLevelModel model){
        Random r = new Random();
        for (int i = 0; i < number; i++){
            char type;
            int choice = 0;

            switch (choice){
                case 1:
                    type = MarioLevelModel.GOOMBA_WINGED;
                    break;
            }

            model.setBlock(xLoc + i*3, yLoc - 1, type);
        }
    }

    void generatePipe(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int height = r.nextInt(4)+1;
        model.setRectangle(xLoc, yLoc-height, 2, height, MarioLevelModel.PIPE);
    }



}

package levelGenerators.CattelonaCampbellGenerator;

import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{
    Random rand;

    private int groundHeight = 12;

    public LevelGenerator(){
   
    }

    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){
        // generate ground
        for (int x = 0; x < model.getWidth(); x++){
            if (x == 30 || x == 90){
                //x += generatePipePit(x, groundHeight, model);
            }
            else if (x == 60){
                //x += generatePit(x, groundHeight, model);
            }
            else{
                for (int y = groundHeight; y < model.getHeight(); y++){
                    model.setBlock(x, y, MarioLevelModel.GROUND);
                }
            }
        }

        // generate platforms
        for (int x = 0; x < model.getWidth(); x++){
            if (x == 5){
                generatePlatform(x, groundHeight-4, 5, model);
                x+=5;
            }
            if (x == 15){
                generatePlatform(x, groundHeight-4, 5, model);
                generatePlatform(x+2, groundHeight-7, 1, model);
                x+=5;
            }
            if (x == 25){
                generatePlatform(x, groundHeight-4, 3, model);
                generatePlatform(x+5, groundHeight-8, 8, model);
                x+=12;
            }
            if (x == 45){
                x+= generatePyramid(x, groundHeight-1, 4, true, true, model);
            }


        }
        model.setBlock(1, 5, MarioLevelModel.MARIO_START);
        return model.getMap();
    }

    public String getGeneratorName(){
        return "CattelonaCampbellGenerator";
    }

    int generatePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(4)+3;
        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(xLoc + width, y, MarioLevelModel.NORMAL_BRICK);
        }
        return width;
    }

    void generatePlatform(int xLoc, int yLoc, int width, MarioLevelModel model){
        for (int x = 0; x < width; x++){
            model.setBlock(xLoc+x, yLoc, MarioLevelModel.NORMAL_BRICK);
        }
    }

    int generatePipePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(15) + 15;
        int currentY = yLoc;

        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(xLoc + width, y, MarioLevelModel.NORMAL_BRICK);
        }

        for (int x = xLoc + 3; x < xLoc + width - 2; x += r.nextInt(3)+3){
            int heightDiff = r.nextInt(7)-3;

            currentY += heightDiff;
            if (currentY > 15)
                currentY = 15;

            model.setRectangle(x, currentY, 2, model.getHeight() - currentY, MarioLevelModel.PIPE);

        }

        return width;
    }

    int generatePyramid(int xLoc, int yLoc, int size, boolean mirror, boolean pit, MarioLevelModel model){
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size - i; j++){
                model.setBlock(xLoc+j+i, yLoc-i, MarioLevelModel.PYRAMID_BLOCK);
            }
        }

        if(mirror) {
            if(pit){
                for (int i = groundHeight; i < model.getHeight(); i++){
                    model.setBlock(xLoc+size, i, MarioLevelModel.EMPTY);
                    model.setBlock(xLoc+size+1, i, MarioLevelModel.EMPTY);
                }
            }
            for (int i = 0; i < size; i++){
                for (int j = 0; j < size - i; j++){
                    model.setBlock(xLoc+size+2+j, yLoc-i, MarioLevelModel.PYRAMID_BLOCK);
                }
            }
            return (size * 2) + 2;
        }

        return size;
    }
}

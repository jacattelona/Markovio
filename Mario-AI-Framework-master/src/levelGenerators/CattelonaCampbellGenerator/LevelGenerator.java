package levelGenerators.CattelonaCampbellGenerator;

import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{
    private int groundHeight = 12;

    public LevelGenerator(){

    }

    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){
        // generate ground
        for (int x = 0; x < model.getWidth(); x++){
            if (x == 30){
                generatePit(x, groundHeight, 5, model);
                x+=5;
            }
            else
                model.setBlock(x, groundHeight, MarioLevelModel.NORMAL_BRICK);
        }

        // generate platforms
        for (int x = 0; x < model.getWidth(); x++){
            if (x%5 == 0){
                generatePlatform(x, groundHeight-4, 5, model);
                x+=5;
            }
        }

        model.setBlock(1, 5, MarioLevelModel.MARIO_START);
        return model.getMap();
    }

    public String getGeneratorName(){
        return "CattelonaCampbellGenerator";
    }

    void generatePit(int xLoc, int yLoc, int width, MarioLevelModel model){
        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(xLoc + width, y, MarioLevelModel.NORMAL_BRICK);
        }
    }

    void generatePlatform(int xLoc, int yLoc, int width, MarioLevelModel model){
        for (int x = 0; x < width; x++){
            model.setBlock(xLoc+x, yLoc, MarioLevelModel.NORMAL_BRICK);
        }
    }
}

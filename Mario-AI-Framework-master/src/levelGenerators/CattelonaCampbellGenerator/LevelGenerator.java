package levelGenerators.CattelonaCampbellGenerator;

import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{

    public LevelGenerator(){

    }

    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){

        for (int x = 0; x < model.getWidth(); x++){
            if (x == 30){
                generatePit(x, 12, 5, model);
                x+=5;
            }
            else
                model.setBlock(x, 12, MarioLevelModel.NORMAL_BRICK);
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
}

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
            model.setBlock(x, 15, MarioLevelModel.NORMAL_BRICK);
        }

        model.setBlock(1, 5, MarioLevelModel.MARIO_START);
        return model.getMap();
    }

    public String getGeneratorName(){
        return "CattelonaCampbellGenerator";
    }
}

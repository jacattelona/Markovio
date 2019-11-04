package levelGenerators.CattelonaCampbellGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{
    private double CHANCE_BLOCK_COIN = 0.3;
    private double CHANCE_BLOCK_POWER_UP = 0.1;
    Random rand;
    private int groundHeight = 12;
    private String levelAnalysis = "level-analysis/notchparam-level-analysis.csv";

    public LevelGenerator() {
        readCSV();
    }

    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){
        // generate ground
        for (int x = 0; x < model.getWidth(); x++){
            if (x == 90){
                x += generatePlatformPit(x, groundHeight, model);
            }
            else if (x > 0 && x%30 == 0){
                x += generatePit(x, groundHeight, model);
            }
            else{
                for (int y = groundHeight; y < model.getHeight(); y++){
                    model.setBlock(x, y, MarioLevelModel.GROUND);
                }
            }

            if (x > 0 && x%20 == 0){
                generateRandomEnemies(x, groundHeight, 2, model);
            }
        }

        // generate platforms
        for (int x = 0; x < model.getWidth(); x++){
            if (x > 0 && x%15 == 0)
                x+= generateRandomPlatforms(x, groundHeight-4, model);
        }
        model.setBlock(1, 5, MarioLevelModel.MARIO_START);
        return model.getMap();
    }

    public String getGeneratorName(){
        return "CattelonaCampbellGenerator";
    }

    private int generatePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(4)+3;
        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.GROUND);
            model.setBlock(xLoc + width, y, MarioLevelModel.GROUND);
        }
        return width;
    }

    private void generatePlatform(int xLoc, int yLoc, int width, MarioLevelModel model){
        rand = new Random();
        for (int x = 0; x < width; x++){
            if (rand.nextDouble() < CHANCE_BLOCK_POWER_UP) {
                if (rand.nextDouble() < 0.2)
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.SPECIAL_BRICK);
                else
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.SPECIAL_QUESTION_BLOCK);
            } else if (rand.nextDouble() < CHANCE_BLOCK_COIN) {
                if (rand.nextDouble() < 0.4)
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.COIN_BRICK);
                else
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.COIN_QUESTION_BLOCK);
            } else {
                model.setBlock(xLoc+x, yLoc, MarioLevelModel.NORMAL_BRICK);
            }
        }
    }

    private int generatePipePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(15) + 15;
        int currentY = yLoc;

        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.GROUND);
            model.setBlock(xLoc + width, y, MarioLevelModel.GROUND);
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

    private int generatePyramid(int xLoc, int yLoc, int size, boolean mirror, boolean pit, MarioLevelModel model){
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

    private int generatePlatformPit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int width = r.nextInt(15) + 15;
        int currentY = yLoc;

        for (int y = yLoc; y < model.getHeight(); y++){
            model.setBlock(xLoc, y, MarioLevelModel.GROUND);
            model.setBlock(xLoc + width, y, MarioLevelModel.GROUND);
        }

        for (int x = xLoc + 3; x < xLoc + width - 2; x += r.nextInt(3)+3){
            int heightDiff = r.nextInt(7)-3;

            currentY += heightDiff;
            if (currentY > 15)
                currentY = 15;

            int plat_width = r.nextInt(8)+2;
            model.setRectangle(x, currentY, plat_width, 1, MarioLevelModel.PLATFORM);
            model.setRectangle(x, currentY+1, plat_width, model.getHeight() - currentY, MarioLevelModel.PLATFORM_BACKGROUND);
        }

        return width;
    }

    private int generatePipe(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        int height = r.nextInt(3)+2;
        model.setRectangle(xLoc, yLoc-height, 2, height, MarioLevelModel.PIPE);

        return 2;
    }

    private void generateEnemies(int xLoc, int yLoc, int number, char type, MarioLevelModel model){
        for (int i = 0; i < number; i++){
            model.setBlock(xLoc + i*3, yLoc - 1, type);
        }
    }

    private void generateRandomEnemies(int xLoc, int yLoc, int number, MarioLevelModel model){
        Random r = new Random();
        for (int i = 0; i < number; i++){
            char type;
            int choice = r.nextInt(8);

            switch (choice){
                case 1:
                    type = MarioLevelModel.GOOMBA_WINGED;
                    break;
                case 2:
                    type = MarioLevelModel.GREEN_KOOPA;
                    break;
                case 3:
                    type = MarioLevelModel.GREEN_KOOPA_WINGED;
                    break;
                case 4:
                    type = MarioLevelModel.RED_KOOPA;
                    break;
                case 5:
                    type = MarioLevelModel.RED_KOOPA_WINGED;
                    break;
                case 6:
                    type = MarioLevelModel.SPIKY;
                    break;
                case 7:
                    type = MarioLevelModel.SPIKY_WINGED;
                    break;
                default:
                    type = MarioLevelModel.GOOMBA;
                    break;
            }

            model.setBlock(xLoc + i*3, yLoc - 1, type);
        }
    }

    private int generateRandomPlatforms(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        yLoc -= r.nextInt(3);
        int choice = r.nextInt(8);
        switch (choice) {
            case 1:
                generatePlatform(xLoc, yLoc, 5, model);
                break;
            case 2:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc+2, yLoc-3, 1, model);
                break;
            case 3:
                generatePlatform(xLoc, yLoc, 3, model);
                generatePlatform(xLoc+5, yLoc-4, 8, model);
                break;
            case 4:
                generatePlatform(xLoc, yLoc, 1, model);
                generatePlatform(xLoc+2, yLoc, 1, model);
                generatePlatform(xLoc+4, yLoc, 1, model);
                generatePlatform(xLoc+2, yLoc-3, 1, model);
                break;
            case 5:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc, yLoc-3, 5, model);
                break;
            default:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc+1, yLoc-3, 3, model);
                break;
        }
        return 0;
    }

    private void readCSV() {
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(levelAnalysis));
            String row = "";
            while ((row = csvReader.readLine()) != null){
                String[] levelData = row.split(",");
                //for (int i = 0; i < levelData.length; i++) {System.out.println(levelData[i]);}
            }
        }
        catch (FileNotFoundException e){ }
        catch (IOException e){ }
    }
}

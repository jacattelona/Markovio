package levelGenerators.CattelonaCampbellGenerator;

import java.io.*;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{

    final String NOTCH = "level-analysis/notchparam-level-analysis-formatted.csv";
    final String ORIGINAL = "level-analysis/original-level-analysis-formatted.csv";

    Random rand;

    private double CHANCE_BLOCK_COIN = 0.3;                                             // chance a coin block is placed in a block platform
    private double CHANCE_BLOCK_POWER_UP = 0.1;                                         // chance a power-up is placed in a block platform

    private int groundHeight = 12;                                                      // height of ground-level in the level; increase number to decrease ground height, and vice versa
    private String levelAnalysis = ORIGINAL;      // relative filepath of the level analysis csv used to populate the probability table; can be changed to get different output

    private double[][] probs = new double[8][7];                                        // probability table used in level generation
    private int currentState = 0;                                                       // tracks current state in level generation

    // constructor for level generator; parses level analysis csv upon creation
    public LevelGenerator() {
        readCSV();
    }

    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){

        //Place to start after generating initial ground
        int xLocation = 10;

        //Generate initial ground for player to stand on
        for (int x = 0; x < xLocation; x++){
            for (int y = groundHeight; y < model.getHeight(); y++){
                model.setBlock(x, y, MarioLevelModel.GROUND);
            }
        }

        //while we aren't in a finish state
        while (currentState != 8){
            //Remember old starting location
            int oldX = xLocation;
            //Set new starting location
            xLocation = MarkovChain(oldX, model);

            //if we've reached farther than 130 blocks, we are in a finish state
            if (xLocation >= 130)
                currentState = 8;
        }

        //generate the final blocks for the finish
        for (int x = xLocation; x < 150; x++){
            for (int y = groundHeight; y < model.getHeight(); y++){
                model.setBlock(x, y, MarioLevelModel.GROUND);
            }
        }

        //set start
        model.setBlock(1, 5, MarioLevelModel.MARIO_START);
        return model.getMap();
    }


    // gets the name of the level generator
    public String getGeneratorName(){
        return "CattelonaCampbellGenerator";
    }

    // creates a standard pit in a level at the given x and y location
    private int generatePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();

        // width is random between 3 and 6
        int width = r.nextInt(4)+3;

        // return width of the pit to jump ahead in the level generation loop
        return width;
    }

    // creates a platform of specified length at the given x and y location
    // used in generateRandomPlatforms
    private void generatePlatform(int xLoc, int yLoc, int width, MarioLevelModel model){
        rand = new Random();
        for (int x = 0; x < width; x++){
            // randomly decide whether to use a power-up block in the platform
            if (rand.nextDouble() < CHANCE_BLOCK_POWER_UP) {
                // randomly decide if that block is a question block or regular brick
                if (rand.nextDouble() < 0.2)
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.SPECIAL_BRICK);
                else
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.SPECIAL_QUESTION_BLOCK);
            }
            // randomly decide whether to use a coin block in the platform
            else if (rand.nextDouble() < CHANCE_BLOCK_COIN) {
                if (rand.nextDouble() < 0.4)
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.COIN_BRICK);
                else
                    model.setBlock(xLoc+x, yLoc, MarioLevelModel.COIN_QUESTION_BLOCK);
            }
            // if not using a power-up or coin block, use regular brick
            else {
                model.setBlock(xLoc+x, yLoc, MarioLevelModel.NORMAL_BRICK);
            }
        }
    }

    // generates a pit of random width with pipes as platforms for crossing it
    private int generatePipePit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();

        // width randomly generated between 15 and 30
        int width = r.nextInt(15) + 15;
        int currentY = yLoc;

        // randomly place pipes in the pit
        // pipes are a random distance apart, but always close enough together for Mario to make the jump
        for (int x = xLoc + 3; x < xLoc + width - 2; x += r.nextInt(3)+3){
            // pipes are random height, but always short enough for Mario to make the jump
            int heightDiff = r.nextInt(7)-3;

            currentY += heightDiff;
            if (currentY > 15)
                currentY = 15;

            // place a pipe extending from the bottom of the screen up to the specified y location
            model.setRectangle(x, currentY, 2, model.getHeight() - currentY, MarioLevelModel.PIPE);

        }

        // return length of the pit for use in increasing the count of the level generation loop
        return width;
    }

    // generates a pyramid structure from pyramid blocks of specified width/height
    // includes parameters to mirror the pyramid, as well as put a pit between the mirrored pyramid
    private int generatePyramid(int xLoc, int yLoc, int size, boolean mirror, boolean pit, MarioLevelModel model){
        // creates the first chunk of the pyramid
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size - i; j++){
                model.setBlock(xLoc+j+i, yLoc-i, MarioLevelModel.PYRAMID_BLOCK);
            }
        }
        for (int x = xLoc; x < xLoc+size; x++){
            for (int y = yLoc+1; y < model.getHeight(); y++){
                model.setBlock(x, y, MarioLevelModel.GROUND);
            }
        }
        // creates the mirror of the pyramid as needed
        if(mirror) {
            // if there is meant to be a pit between pyramid halves, create one
            if(pit){
                for (int i = groundHeight; i < model.getHeight(); i++){
                    model.setBlock(xLoc+size, i, MarioLevelModel.EMPTY);
                    model.setBlock(xLoc+size+1, i, MarioLevelModel.EMPTY);
                }
            }
            // build the other half of the pyramid
            for (int i = 0; i < size; i++){
                for (int j = 0; j < size - i; j++){
                    model.setBlock(xLoc+size+2+j, yLoc-i, MarioLevelModel.PYRAMID_BLOCK);
                }
            }
            for (int x = xLoc+size+2; x < xLoc+size*2+2; x++){
                for (int y = yLoc+1; y < model.getHeight(); y++){
                    model.setBlock(x, y, MarioLevelModel.GROUND);
                }
            }

            // return length of the two pyramid pieces and space between them for use in increasing the count of the level generation loop
            return (size * 2) + 2;
        }

        // return length of the pyramid for use in increasing the count of the level generation loop
        return size;
    }

    // generates a pit of random length with a random number of platforms for crossing the pit
    private int generatePlatformPit(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        // length of the pit is random between 15 and 30
        int width = r.nextInt(15) + 15;
        int currentY = yLoc;

        // make sure platforms are close enough in height that Mario can always make the jump
        for (int x = xLoc + 3; x < xLoc + width - 2; x += r.nextInt(3)+3){
            int heightDiff = r.nextInt(7)-3;

            currentY += heightDiff;
            if (currentY > 14)
                currentY = 14;

            // platform width is random between 2 and 8
            int plat_width = r.nextInt(7)+2;
            // create a platform of given width
            model.setRectangle(x, currentY, plat_width, 1, MarioLevelModel.PLATFORM);
            // create the background of the platform
            model.setRectangle(x, currentY+1, plat_width, model.getHeight() - currentY, MarioLevelModel.PLATFORM_BACKGROUND);
        }

        // return the width of the platform pit for increase of level generation loop
        return width;
    }

    // creates a pipe of random height at the given location
    private int generatePipe(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        // make sure the pipe is short enough for Mario to jump over
        int height = r.nextInt(3)+2;
        model.setRectangle(xLoc, yLoc-height, 2, height, MarioLevelModel.PIPE);

        // return width of pipe for proper increase of level generation loop count
        return 2;
    }

    // place an enemy of the given type at the given location
    // used in generateRandomEnemies
    private void generateEnemies(int xLoc, int yLoc, int number, char type, MarioLevelModel model){
        for (int i = 0; i < number; i++){
            model.setBlock(xLoc + i*3, yLoc - 1, type);
        }
    }

    // creates a specified number of random enemies at the specified location
    // enemies generated can be of different types
    private void generateRandomEnemies(int xLoc, int yLoc, int number, MarioLevelModel model){
        Random r = new Random();
        for (int i = 0; i < number; i++){
            char type;
            // randomly choose what the enemies will be generated
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

    // generates a random platform layout at the given location
    // platforms layouts are selected from a preset collection
    private int generateRandomPlatforms(int xLoc, int yLoc, MarioLevelModel model){
        Random r = new Random();
        // randomly adjust the height of the platform set by a minor amount for the sake of variety; Mario can still make the jump
        yLoc -= r.nextInt(3)-1;
        // randomly decide which layout to use
        int width = 0;
        int choice = r.nextInt(8);
        switch (choice) {
            case 1:
                generatePlatform(xLoc, yLoc, 5, model);
                width = 5;
                break;
            case 2:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc+2, yLoc-3, 1, model);
                width = 5;
                break;
            case 3:
                generatePlatform(xLoc, yLoc, 3, model);
                generatePlatform(xLoc+5, yLoc-4, 8, model);
                width = 12;
                break;
            case 4:
                generatePlatform(xLoc, yLoc, 1, model);
                generatePlatform(xLoc+2, yLoc, 1, model);
                generatePlatform(xLoc+4, yLoc, 1, model);
                generatePlatform(xLoc+2, yLoc-3, 1, model);
                width = 5;
                break;
            case 5:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc, yLoc-3, 5, model);
                width = 5;
                break;
            default:
                generatePlatform(xLoc, yLoc, 5, model);
                generatePlatform(xLoc+1, yLoc-3, 3, model);
                width = 5;
                break;
        }
        return width;
    }

    private void readCSV() {
        System.out.println("READING CSV");
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(levelAnalysis));
            String row = "";
            int rowNum = 0;
            for (int i = 0; i < 8; i++){
                row = csvReader.readLine();

                String[] levelData = row.split(",");
                for(int column = 0; column < 7; column++){
                    probs[rowNum][column] = Double.parseDouble(levelData[column]);
                    //System.out.print(probs[rowNum][column]);
                }
                //System.out.println();
                rowNum++;
            }

        }
        catch (FileNotFoundException e){ }
        catch (IOException e){ }
    }


    // for use in level generation
    int MarkovChain(int xStart, MarioLevelModel model){
        Random r = new Random();
        int newX = xStart;

        //odds of choosing a specific transition in the Markov Chain
        double chance = r.nextDouble();


        //Generate Pipe
        if (chance <= probs[currentState][0]){
            currentState = 1;
            generatePipe(xStart, groundHeight, model);

            newX += r.nextInt(2)+2;
            for (int x = xStart; x < newX; x++){
                for (int y = groundHeight; y < model.getHeight(); y++){
                    model.setBlock(x, y, MarioLevelModel.GROUND);
                }
            }

            double enemyChance = r.nextDouble();
            if (enemyChance < .2){
                int numEnemies = r.nextInt(3)+1;
                generateRandomEnemies(xStart, groundHeight -2, numEnemies, model);
            }
        }

        //Generate Pipe Pit
        else if (chance > probs[currentState][0] && chance <= probs[currentState][1]){
            currentState = 2;
            newX += generatePipePit(xStart, groundHeight, model);
            System.out.println("PIPE PIT");
        }

        //Generate Pit
        else if (chance > probs[currentState][1] && chance <= probs[currentState][2]){
            currentState = 3;
            newX += generatePit(xStart, groundHeight, model);
            System.out.println("PIT,");
        }

        //Generate Platform Pit
        else if (chance > probs[currentState][2] && chance <= probs[currentState][3]){
            currentState = 4;
            newX += generatePlatformPit(xStart, groundHeight, model);
            double enemyChance = r.nextDouble();
            if (enemyChance < .2){
                int numEnemies = r.nextInt(3)+1;
                generateRandomEnemies(xStart, groundHeight -2, numEnemies, model);
            }
        }

        //Generate Mirrored Pyramid
        else if (chance > probs[currentState][3] && chance <= probs[currentState][4]){
            currentState = 5;
            int size = r.nextInt(4)+3;
            newX += generatePyramid(xStart, groundHeight-1, size, true, true, model);
        }

        //Generate Pyramid
        else if (chance > probs[currentState][4] && chance <= probs[currentState][5]){
            currentState = 6;
            int size = r.nextInt(4)+3;
            newX += generatePyramid(xStart, groundHeight-1, size, true, true, model);

        }

        //Generate Blocks
        else if (chance > probs[currentState][5] && chance <= probs[currentState][6]){
            currentState = 7;
            newX += generateRandomPlatforms(xStart, groundHeight-3, model);

            for (int x = xStart; x < newX; x++){
                for (int y = groundHeight; y < model.getHeight(); y++){
                    model.setBlock(x, y, MarioLevelModel.GROUND);
                }
            }
            double enemyChance = r.nextDouble();
            if (enemyChance < .2){
                int numEnemies = r.nextInt(3)+1;
                generateRandomEnemies(xStart, groundHeight -2, numEnemies, model);
            }
        }



        return newX;
    }
}

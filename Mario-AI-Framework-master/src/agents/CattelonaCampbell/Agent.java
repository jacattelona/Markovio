package agents.CattelonaCampbell;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent{

    public void initialize(MarioForwardModel model, MarioTimer timer){

    }

    public boolean[] getActions(MarioForwardModel model, MarioTimer timer){
        boolean a[] = {false};

        return a;
    }

    public String getAgentName(){
        return "CattelonaCampbell Agent";
    }
}

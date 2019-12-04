package agents.CattelonaCampbell;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent{
    private boolean[] actions;
    private BehaviorTree tree;

    public void initialize(MarioForwardModel model, MarioTimer timer){
        actions = new boolean[MarioActions.numberOfActions()];
        tree = new BehaviorTree();
    }

    public boolean[] getActions(MarioForwardModel model, MarioTimer timer){
        actions = tree.chooseActions(model, timer);
        return actions;
    }

    public String getAgentName(){
        return "CattelonaCampbellAgent";
    }
}

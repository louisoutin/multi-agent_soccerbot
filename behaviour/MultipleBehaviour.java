
package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import java.util.ArrayList;
import java.util.List;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;


public abstract class MultipleBehaviour extends Behaviour{

    
    List<Behaviour> listBehaviours;
    
    public MultipleBehaviour(SoccerMap soccerMap){
        super(soccerMap);
        listBehaviours=new ArrayList<>();
    }
    
    public abstract SoccerAction defaultAction();
    
    @Override
    public SoccerAction action() {
        for (Behaviour behaviour:listBehaviours) {
            
            if (behaviour.isTriggered()) {
                return behaviour.action();
            }
        }
        
        return defaultAction();      
    }
    
    public List<Behaviour> getListBehaviours() {
        return listBehaviours;
    }

    public void setListBehaviours(List<Behaviour> listBehaviours) {
        this.listBehaviours = listBehaviours;
    }
    
    public void addNextBehaviour(Behaviour behaviour){
        this.listBehaviours.add(behaviour);
    }
    
}

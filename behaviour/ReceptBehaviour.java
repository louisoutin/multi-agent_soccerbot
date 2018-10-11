package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement de reception de passe
 */
public class ReceptBehaviour extends Behaviour {

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public ReceptBehaviour(SoccerMap soccerMap) {
        super(soccerMap);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        if (map.getCptPass() > 0 && map.getCptPass() < 70){
            return true;
        }
        map.setCptPass(0);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        //System.out.println("Player "+map.numPlayer+"; Behaviour = "+this.getClass().toString());
        map.getAbstract_robot().setDisplayString("recept");
        Vec2 aR=new Vec2(map.getLeadBallDipoleAR());
        return new SoccerAction(map, aR, false);
    }
}

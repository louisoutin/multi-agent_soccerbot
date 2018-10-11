package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement du n°10, leader de l'équipe.
 */
public class LeaderBehaviour extends MultipleBehaviour {


    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public LeaderBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return !map.isTeammateNearestToBall();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction defaultAction() {
        
        map.getAbstract_robot().setDisplayString("leader");
        Vec2 aR=new Vec2(map.getLeadBallDipoleAR());
        return new SoccerAction(map, aR, false);
    }
}

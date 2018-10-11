package lakradiOutinTeam.behaviour;

import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement de "degagement"
 */
public class ClearingBehaviour extends Behaviour {

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public ClearingBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return map.getDistanceToOurGoal(map.getBall()).r<SoccerMap.getMAX_DIST_PASS() && !map.isTeammateNearestToOurgoal();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        map.getAbstract_robot().setDisplayString("clearing");
        return new SoccerAction(map, map.getOurgoalBallDipoleAR(), false);
    }
}

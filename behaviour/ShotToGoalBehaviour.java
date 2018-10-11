package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;
import lakradiOutinTeam.SoccerUtils;

/**
 * Comportement de tir vers les buts adverses
 */
public class ShotToGoalBehaviour extends Behaviour {
    protected Vec2 opponentGoalImpact;
    
    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public ShotToGoalBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return !map.isTeammateNearestToBall() && canShotToGoal();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        System.out.println("player "+map.getNumPlayer()+" can Shot To Goal");
        System.out.println("player "+map.getNumPlayer()
                          +"; opponentGoalImpact pos = "+SoccerUtils.displayVec2(opponentGoalImpact));
        System.out.println("AbsOpponentGoalImpact pos = "+SoccerUtils.displayVec2(map.getAbsolutePosition(opponentGoalImpact)));
        
        map.getAbstract_robot().setDisplayString("shootGoal");
        return new SoccerAction(map, map.getBallDipoleAR(opponentGoalImpact), true);
        //return new SoccerAction(map, map.getLeadBallDipoleAR(), true);
    }
    
    /**
     * Indique si le robot peut frapper pour un but
     *
     * @return boolean
     */
    public boolean canShotToGoal() {
        
        if (map.canKick()) {
            opponentGoalImpact=map.getOpponentGoalImpact(map.getSteerHeading());
            
            if (opponentGoalImpact!=null){
                return !(opponentGoalImpact.r>SoccerMap.MAX_DIST_PASS || map.isOpponentsOnPath(opponentGoalImpact, 0.01));
            }
        }
        return false;
    }
}
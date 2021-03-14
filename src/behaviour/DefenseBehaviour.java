package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement défensif
 */
public class DefenseBehaviour extends MultipleBehaviour {

    protected double teammatesFactor=0.2;
    protected double opponentsFactor=0.05;
    protected double ourGoalFactor=1.7;
    protected double sideBoundFactor=0.9;


    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public DefenseBehaviour(SoccerMap soccerMap) {
        super(soccerMap);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return isDefense();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction defaultAction() {
        
        Vec2 aR=new Vec2(map.getOurgoalBallDipoleAR());

        Vec2 teammatesAR=map.getTeammatesRepulsion(teammatesFactor);
        aR.add(teammatesAR);
        
        Vec2 opponentsAR=map.getOpponentsRepulsion(opponentsFactor);
        aR.add(opponentsAR);
        
        Vec2 sideBoundAR=map.getSideBoundRepulsion();
        sideBoundAR.setr(sideBoundAR.r*sideBoundFactor);
        aR.add(sideBoundAR);
        
        Vec2 goalAR=new Vec2(map.getOurGoal());
        goalAR.setr(goalAR.r*ourGoalFactor);
        aR.add(goalAR);
        
        map.getAbstract_robot().setDisplayString("defense");
        return new SoccerAction(map, aR, false);
    }
    
    /**
     * Le robot se positionne-t'il en defense
     *
     * @return true s'il est en defense, false sinon.
     */
    public boolean isDefense() {
        Vec2[] teammates = map.getTeammates();
        int bestToGoal=0;
        for (Vec2 teammate:teammates) {
            if (map.getDistanceToOurGoal(teammate).r<map.getOurGoal().r) {
                bestToGoal++;
            }
        }
        if (bestToGoal<SoccerMap.NB_DEFENSE) {
            return true;
        }
        return false;
    }
}

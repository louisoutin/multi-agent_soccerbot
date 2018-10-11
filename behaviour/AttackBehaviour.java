package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement d'attaque des robots.
 */
public class AttackBehaviour extends Behaviour {
    /*
    double teammatesFactor=1.4;
    double opponentsFactor=0.4;
    double opponentGoalFactor=1.8;
    double opponentBoundFactor=2.5;
    double sideBoundFactor=1.0;
    */
    
    // topo/mattei : 3/0
    
    protected double teammatesFactor=1.5;
    protected double opponentsFactor=0.3;
    protected double opponentGoalFactor=1.6;
    protected double opponentBoundFactor=2.4;
    protected double sideBoundFactor=1.2;

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public AttackBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return isAttack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        Vec2 aR=new Vec2(map.getLeadBallDipoleAR());

        Vec2 teammatesAR=map.getTeammatesRepulsion(teammatesFactor);
        aR.add(teammatesAR);
        
        Vec2 opponentsAR=map.getOpponentsRepulsion(opponentsFactor);
        aR.add(opponentsAR);
        

        Vec2 goalAR=new Vec2(map.getOpponentGoal());
        goalAR.setr(goalAR.r);
        Vec2 ballAR=new Vec2(map.getBall());
        goalAR.setr(goalAR.r);
        Vec2 oppGoalBallAR=new Vec2(goalAR);
        oppGoalBallAR.add(ballAR);
        oppGoalBallAR.setr(oppGoalBallAR.r*opponentGoalFactor);
        aR.add(oppGoalBallAR);
        
        
        Vec2 oppBoundAR=map.getOpponentBoundRepulsion();
        oppBoundAR.setr(oppBoundAR.r*opponentBoundFactor);
        aR.add(oppBoundAR);
        
        Vec2 sideBoundAR=map.getSideBoundRepulsion();
        sideBoundAR.setr(sideBoundAR.r*sideBoundFactor);
        aR.add(sideBoundAR);
        
        map.getAbstract_robot().setDisplayString("attack");
        return new SoccerAction(map, aR, false);
    }
    
    /**
     * Le robot se positionne-t'il en attaque
     *
     * @return true si le robot est en attaque, false sinon
     */
    public boolean isAttack() {
        Vec2[] teammates = map.getTeammates();
        int bestToGoal=0;
        for (Vec2 teammate:teammates) {
            if (map.getDistanceToOpponentGoal(teammate).r<map.getOpponentGoal().r) {
                bestToGoal++;
            }
        }
        if (bestToGoal<SoccerMap.NB_ATTACK) {
            return true;
        }
        return false;
    }
}

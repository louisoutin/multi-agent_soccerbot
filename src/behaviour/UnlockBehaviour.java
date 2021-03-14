package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;
import lakradiOutinTeam.SoccerUtils;

/**
 * Comportement de "débloquage" de situation
 */
public class UnlockBehaviour extends Behaviour {

    protected boolean panic;

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public UnlockBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
        this.panic=false;
    }

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     * @param panic
     */
    public UnlockBehaviour(SoccerMap soccerMap, boolean panic) {
        super(soccerMap);
        this.panic=panic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        if (map.getLongLock()>SoccerMap.PANICLOCK+50) {
            map.resetLongLock();
        }
        
        // si la balle est assez loin de notre but
        if (SoccerUtils.getDistance(map.getBall(), map.getOurGoal()).r>0.6) {
            if (panic) {
                map.setCptPass(0);
                return map.isPanicLock();
            }
            else {
                return map.isLock();
            }
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        if (panic) {
            System.out.println("Player "+map.getNumPlayer()+" : PANIC UNLOCK !!");
        }
        
        System.out.println("UNLOCKING !!!");
        
        Vec2 aR=new Vec2(0,0);
        
        Vec2 teammatesAR=map.getTeammatesRepulsion(0.4);
        aR.add(teammatesAR);

        Vec2 opponentsAR=map.getOpponentsRepulsion(1.0);
        aR.add(opponentsAR);
        
        map.getAbstract_robot().setDisplayString("unlock");
        return new SoccerAction(map, aR, false);
    }
}

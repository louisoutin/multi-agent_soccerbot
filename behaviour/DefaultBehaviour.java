package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement par défaut.
 */
public class DefaultBehaviour extends Behaviour {

    protected double teammatesFactor=0.4;
    protected double opponentsFactor=1.0;
    protected double centerFactor=-1.0;

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public DefaultBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        Vec2 aR=new Vec2(map.getBallDipoleAR());
        
        Vec2 teammatesAR=map.getTeammatesRepulsion(teammatesFactor);
        aR.add(teammatesAR);

        Vec2 opponentsAR=map.getOpponentsRepulsion(opponentsFactor);
        aR.add(opponentsAR);
        
        Vec2 centerAR=new Vec2(map.getPosition());
        centerAR.setr(centerAR.r*centerFactor);
        
        aR.add(centerAR);
        
        map.getAbstract_robot().setDisplayString("default");
        return new SoccerAction(map, aR, false);
    }
}

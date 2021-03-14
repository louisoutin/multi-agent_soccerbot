package lakradiOutinTeam.behaviour;

import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;

/**
 * Classe abstraite principale des différents comportements
 */
public abstract class Behaviour {

    protected SoccerMap map;
    protected Behaviour nextBehavior;

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public Behaviour(SoccerMap soccerMap) {
        this.map=soccerMap;
    }

    public SoccerMap getSoccerMap() {
        return map;
    }
    
    public Behaviour getNextBehavior() {
        return nextBehavior;
    }

    public void setNextBehavior(Behaviour nextBehavior) {
        this.nextBehavior = nextBehavior;
    }

    /**
     * Le comportement est-il déclenchable
     *
     * @return true si le comportement est declenchable, false sinon
     */
    public abstract boolean isTriggered();

    /**
     * Effectue l'action correspondant au comportement declenché
     *
     * @return
     */
    public abstract SoccerAction action();
}

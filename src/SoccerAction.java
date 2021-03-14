package lakradiOutinTeam;

import static EDU.gatech.cc.is.abstractrobot.ControlSystemS.CSSTAT_OK;
import EDU.gatech.cc.is.util.Vec2;

/**
 * Effecteurs des robots dans la map de champs de potentiels
 */
public class SoccerAction {

    protected SoccerMap map;
    protected Vec2 steerHeading;
    protected boolean kick;

    /**
     * Constructeur logique.
     *
     * @param map
     * @param steerHeading
     * @param kick
     */
    public SoccerAction(SoccerMap map, Vec2 steerHeading, boolean kick) {
        this.map=map;
        this.steerHeading=steerHeading;
        this.kick=kick;
    }

    /**
     * Appele a chaque step
     *
     * @return valeur de CSSTAT_OK
     */
    public int doIt() {
        
        map.abstract_robot.setSteerHeading(map.curr_time, steerHeading.t);
        
        map.abstract_robot.setSpeed(map.curr_time, 1);
        
        if (kick) {
            if (map.abstract_robot.canKick(map.curr_time))
                map.abstract_robot.kick(map.curr_time);
        }

        return(CSSTAT_OK);
    }

}

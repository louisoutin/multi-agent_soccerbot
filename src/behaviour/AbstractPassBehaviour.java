package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerMap;

/**
 * Comportement abstrait de passe entre agents.
 */
public abstract class AbstractPassBehaviour extends Behaviour {

    protected Vec2 bestTeammate;

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public AbstractPassBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }
    
    /**
     * Trouve le meilleur joueur de l'equipe pour une passe
     */
    public void findBestTeammatePass() {
        bestTeammate=null;
        double distanceToPass=Double.MAX_VALUE;
        
        for (Vec2 teammate:map.getTeammates()) {
            // equipier pas trop loin ?
            if (teammate.r>SoccerMap.MAX_DIST_PASS) {
                continue;
            }
            // equipier pas trop pres ?
            if (teammate.r<SoccerMap.MIN_DIST_PASS) {
                continue;
            }
            // equipier dans le bon angle de tir ?
            if (!map.isBetweenAngle(teammate, 0.25)) {
                continue;
            }
            // adversaires sur la route ?
            if (map.isOpponentsOnPath(teammate, 0.15)) {
                continue;
            }

            if (map.isWestTeam()) {
                // Zone plus en avant (permet plus de tir en retrait)
                if (map.getPosition().x>0.8) {
                    if (teammate.x>-0.3) {
                        distanceToPass=setBestTeammate(teammate, distanceToPass);
                    }
                }
                // Zone plus en arrière (permet moins de tir en retrait)
                else if (map.getPosition().x>-0.5) {
                    if (teammate.x>-0.1) {
                        distanceToPass=setBestTeammate(teammate, distanceToPass);
                    }
                }
            }
            // Pareil pour east side
            else {
                if (map.getPosition().x<-0.8) {
                    if (teammate.x<0.3) {
                        distanceToPass=setBestTeammate(teammate, distanceToPass);
                    }
                }
                else if (map.getPosition().x<0.5) {
                    if (teammate.x<0.1) {
                        distanceToPass=setBestTeammate(teammate, distanceToPass);
                    }
                }
            }
            
            
        }
    }
    
    private double setBestTeammate(Vec2 teammate, double distanceToPass) {
        // -0.3 pour pas qu'il soit trop proche et que la position ideale soit a 0.3
        double distTmp=Math.abs(teammate.r-0.3);

        if (distanceToPass==Double.MAX_VALUE) {
            bestTeammate=teammate;
            return distTmp;
        }
        else if (distanceToPass>distTmp) {
            bestTeammate=teammate;
            return distTmp;
        }
        
        return distanceToPass;
    }
}

package lakradiOutinTeam.behaviour;

import EDU.gatech.cc.is.communication.CommunicationException;
import EDU.gatech.cc.is.util.Vec2;
import lakradiOutinTeam.SoccerAction;
import lakradiOutinTeam.SoccerMap;
import lakradiOutinTeam.SoccerUtils;
import lakradiOutinTeam.TeammateMessage;

/**
 * Comportement de passe vers l'avant
 */
public class PassForwardBehaviour extends AbstractPassBehaviour {

    /**
     * Constructeur logique.
     *
     * @param soccerMap
     */
    public PassForwardBehaviour(SoccerMap soccerMap) {
        super(soccerMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTriggered() {
        // eviter le contre
        if (map.getOurGoal().r<0.7) {
            return false;
        }
        if (!map.canKick()) {
            return false;
        }
        findBestTeammatePass();
        
        return bestTeammate!=null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoccerAction action() {
        System.out.println("player "+map.getNumPlayer()
                          +"; bestTeammate pos pour passe  = "+SoccerUtils.displayVec2(bestTeammate));
        
        sendPassMessage();
        
        map.getAbstract_robot().setDisplayString("pass");
        return new SoccerAction(map, map.getBallDipoleAR(bestTeammate), true);
    }

    /**
     * Envoie d'un message pour les passes
     */
    public void sendPassMessage() {
        // on trouve le num de bestTeammate 
        int numToSend=-1;
        for (Integer numT:map.getRecvdTeammatePos().keySet()) {
            if (numToSend==-1) {
                numToSend=numT;
            }
            else {
                if (getDistanceToPass(map.getRecvdTeammatePos().get(numT))<getDistanceToPass(map.getRecvdTeammatePos().get(numToSend))) {
                    numToSend=numT;
                }
            }
        }

        if (numToSend>=0) {
            System.out.println("Player "+map.getNumPlayer()+" passe a "+numToSend+" a la pos ="+SoccerUtils.displayVec2(map.getRecvdTeammatePos().get(numToSend)));
            // on envoie le message
            try {
                map.getAbstract_robot().unicast(numToSend, new TeammateMessage(TeammateMessage.PASS, map.getNumPlayer()));
            } catch (CommunicationException ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("Behaviour = "+this.getClass().toString()+"\n"
                               +"bestTeammate pos = "+SoccerUtils.displayVec2(bestTeammate)+"\n"
                               +"Player "+map.getNumPlayer()+" -> pas de num trouvé!");
        }
    }
    
    /**
     *
     *
     * @param teammate
     * @return distance d'un equipier a la frappe
     */
    public double getDistanceToPass(Vec2 teammate) {
        return SoccerUtils.getDistance(bestTeammate, teammate).r;
    }
}

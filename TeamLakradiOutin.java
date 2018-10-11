package lakradiOutinTeam;

import EDU.gatech.cc.is.abstractrobot.ControlSystemSS;
import java.util.Enumeration;
import lakradiOutinTeam.behaviour.Behaviour;
import lakradiOutinTeam.behaviour.AttackBehaviour;
import lakradiOutinTeam.behaviour.ClearingBehaviour;
import lakradiOutinTeam.behaviour.DefaultBehaviour;
import lakradiOutinTeam.behaviour.DefenseBehaviour;
import lakradiOutinTeam.behaviour.LeaderBehaviour;
import lakradiOutinTeam.behaviour.PassForwardBehaviour;
import lakradiOutinTeam.behaviour.UnlockBehaviour;
import lakradiOutinTeam.behaviour.ShotToGoalBehaviour;

/**
 * Classe principal qui controle l'agent,
 * ordonancement des comportements par priorité,
 * gestion des communications.
 */
public class TeamLakradiOutin extends ControlSystemSS {
    
    SoccerAction action;
    SoccerMap map;
    Behaviour start;
    
    Enumeration messagesIn;
    
    UnlockBehaviour panicLockBehaviour;
    UnlockBehaviour unlockBehaviour;
    LeaderBehaviour leaderBehaviour;
    ShotToGoalBehaviour shotToGoalBehaviour;
    PassForwardBehaviour passForwardBehaviour;
    DefenseBehaviour defenseBehaviour;
    ClearingBehaviour clearingBehaviour;
    AttackBehaviour attackBehaviour;
    DefaultBehaviour defaultBehaviour;
    
    
    
    @Override
    public void configure() {

        map=new SoccerMap(this.abstract_robot);
        messagesIn = abstract_robot.getReceiveChannel();
        
        // Definition et ordonnancement des sous behaviours
        
        panicLockBehaviour=new UnlockBehaviour(map, true);
        
        leaderBehaviour=new LeaderBehaviour(map);
        
        unlockBehaviour=new UnlockBehaviour(map);
        shotToGoalBehaviour = new ShotToGoalBehaviour(map);
        passForwardBehaviour = new PassForwardBehaviour(map);
        
        leaderBehaviour.addNextBehaviour(unlockBehaviour);
        leaderBehaviour.addNextBehaviour(shotToGoalBehaviour);
        leaderBehaviour.addNextBehaviour(passForwardBehaviour);
        
        defenseBehaviour=new DefenseBehaviour(map);
        
        clearingBehaviour = new ClearingBehaviour(map);

        defenseBehaviour.addNextBehaviour(clearingBehaviour);
        
        attackBehaviour=new AttackBehaviour(map);
        
        defaultBehaviour=new DefaultBehaviour(map);
        
        
        
        // Ordonnancement des principaux comportements
        
        panicLockBehaviour.setNextBehavior(leaderBehaviour);
        leaderBehaviour.setNextBehavior(defenseBehaviour);
        defenseBehaviour.setNextBehavior(attackBehaviour);
        attackBehaviour.setNextBehavior(defaultBehaviour);
    
    }
    
    @Override
    public int takeStep() {
        map.init();
        
        int ret;

        // broadcast de la position
        sendAbsolutePos();
        // met a jour la map des positions ou declenche reception passe
        readMessages();

        start = panicLockBehaviour;
        while (!start.isTriggered()){
            start=start.getNextBehavior();
        }
        ret = start.action().doIt();
        
        
        map.updateCpt();
        
        return ret;
    }
    
    /**
     * Envoie un message de la position absolue 
     */
    public void sendAbsolutePos() {
        abstract_robot.broadcast(new TeammateMessage(TeammateMessage.POS, map.numPlayer, map.position));
    }
    
    /**
     * Lecture des messages
     */
    public void readMessages() {
        while (messagesIn.hasMoreElements())
        {
            TeammateMessage recvd = (TeammateMessage) messagesIn.nextElement();
            if (recvd.info==TeammateMessage.PASS) {
                map.cptPass++;
            }
            else if (recvd.info==TeammateMessage.POS) {
                map.recvdTeammatePos.put(recvd.numSender, recvd.absolutePosition);
            }
        }
    }
}

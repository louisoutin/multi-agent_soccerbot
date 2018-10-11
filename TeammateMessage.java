package lakradiOutinTeam;

import EDU.gatech.cc.is.communication.Message;
import EDU.gatech.cc.is.util.Vec2;

/**
 * Commnucation entre les robots, en vue des passes
 */
public class TeammateMessage extends Message {
    public final static int POS=0;
    public final static int PASS=1;
    
    protected int info;
    protected int numSender;
    protected Vec2 absolutePosition;

    /**
     * Constructeur logique.
     *
     * @param info
     * @param numSender
     */
    public TeammateMessage(int info, int numSender) {
        this.info=info;
        this.numSender=numSender;
        this.absolutePosition=null;
    }

    /**
     * Constructeur logique.
     *
     * @param info
     * @param numSender
     * @param absolutePosition
     */
    public TeammateMessage(int info, int numSender, Vec2 absolutePosition) {
        this.info=info;
        this.numSender=numSender;
        this.absolutePosition=absolutePosition;
    }
}
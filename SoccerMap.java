package lakradiOutinTeam;

import EDU.gatech.cc.is.abstractrobot.SocSmall;
import EDU.gatech.cc.is.util.Vec2;
import java.util.HashMap;
import java.util.Map;

/**
 * Carte de champs de potentiels
 */
public class SoccerMap {

    
    // les murs
    public final static double BOUND_X=1.37;
    public final static double BOUND_Y=0.7625;
    // rayon d'un but - rayon d'une balle
    public final static double GOAL_RADIUS=0.23;
    public final static int NB_DEFENSE=2;
    public final static int NB_ATTACK=2;
    public final static double MAX_DIST_PASS=0.6;
    public final static double MIN_DIST_PASS=0.25;
    // temps pour les unlocks
    public final static int LOCK=200;
    public final static int PANICLOCK=800;

    //robot
    protected SocSmall abstract_robot;

    protected boolean westTeam;
    protected int numPlayer;
    protected long curr_time;
    protected double steerHeading;
    protected Vec2 position;
    protected Vec2 ball;
    protected Vec2 ourGoal;
    protected Vec2 opponentGoal;
    protected Map<Integer,Vec2> recvdTeammatePos;

    protected boolean teammateNearestToBall;
    protected boolean teammateNearestToGoal;

    protected Vec2 prevAbsBallPosition;
    protected int longLock;
    protected int cptPass;
    
    /**
     * Constructeur logique.
     *
     * @param abstract_robot robot referent
     */
    public SoccerMap(SocSmall abstract_robot) {
        this.abstract_robot=abstract_robot;
        this.numPlayer=this.abstract_robot.getPlayerNumber(this.abstract_robot.getTime());
        this.recvdTeammatePos=new HashMap<>();
        this.longLock=0;
        this.cptPass=0;
    }
    
    /**
     * A appeller a chaque takeStep()
     */
    public void init() {
        curr_time=abstract_robot.getTime();
        ball=abstract_robot.getBall(curr_time);
        opponentGoal=abstract_robot.getOpponentsGoal(curr_time);
        ourGoal=abstract_robot.getOurGoal(curr_time);
        position=abstract_robot.getPosition(curr_time);
        steerHeading=abstract_robot.getSteerHeading(curr_time);
        westTeam=(opponentGoal.x>0);
        
        teammateNearestToBall=false;
        teammateNearestToGoal=false;
        
        for (Vec2 t:abstract_robot.getTeammates(curr_time)) {
            if (getDistanceToBall(t).r<ball.r) {
                teammateNearestToBall=true;
            }
            if (getDistanceToOurGoal(t).r<ourGoal.r) {
                teammateNearestToGoal=true;
            }
        }
    }
    
    /**
     * Met a jour les compteurs par rapport a curr_time a la fin de takeStep et l'ancienne position de la balle.
     */
    public void updateCpt() {
        if (this.prevAbsBallPosition==null) {
            longLock=0;
        }
        else {
            if (SoccerUtils.equalsPosition(this.prevAbsBallPosition, getAbsolutePosition(ball), 0.02)) {
                longLock++;
            }
            else {
                longLock=0;
            }
        }
        
        prevAbsBallPosition=getAbsolutePosition(ball);
    }

    /**
     * Est ce que la position de la balle ne bouge pas
     *
     * @return true si rien a bougé
     */
    public boolean isLock() {
        return longLock>LOCK;
    }

    /**
     * Est ce que la position de la balle ne bouge pas depuis trop longtemps
     *
     * @return true si rien a bougé
     */
    public boolean isPanicLock() {
        return longLock>PANICLOCK;
    }

    /**
     * Remet le compteur a 0
     */
    public void resetLongLock() {
        longLock=0;
    }

    /**
     * Position absolue par rapport au centre du terrain
     *
     * @param obj
     * @return
     */
    public Vec2 getAbsolutePosition(Vec2 obj) {
        Vec2 tmp=new Vec2(obj);
        tmp.add(position);
        return tmp;
    }
    
    /**
     * Distance entre le joueur et la balle
     *
     * @param player
     * @return Vec2 du joueur en parametre a la balle
     */
    public Vec2 getDistanceToBall(Vec2 player) {
        return SoccerUtils.getDistance(ball, player);
    }

    /**
     * Distance entre le joueur et notre but
     *
     * @param player
     * @return Vec2 du joueur en parametre a notre but
     */
    public Vec2 getDistanceToOurGoal(Vec2 player) {
        return SoccerUtils.getDistance(ourGoal, player);
    }

    /**
     * Distance entre le joueur et le but adverse
     *
     * @param player
     * @return Vec2 du joueur en parametre au but adverse
     */
    public Vec2 getDistanceToOpponentGoal(Vec2 player) {
        return SoccerUtils.getDistance(opponentGoal, player);
    }
    
    /**
     * Calcul de la position de l'impact dans le but adverse.
     * en fonction d'une orientation a partir de la position courante
     *
     * @param steer
     * @return point d'impact sur le but adverse, null si pas dans le but
     */
    public Vec2 getOpponentGoalImpact(double steer) {
        // tan(PI/2) et tan(-PI/2) non défini
        if (steer==Math.PI/2 || steer==-Math.PI/2) {
            return null;
        }
        
        // pour avoir un angle aigu
        double orientedSteer=SoccerUtils.ModuleAngle(westTeam?steer:Math.PI-steer);
        double adjLength=Math.abs(opponentGoal.x);
        double oppLength=adjLength*Math.tan(orientedSteer);
        
        Vec2 opponentGoalImpact = new Vec2(opponentGoal.x, oppLength);
        
        if (Math.abs(getAbsolutePosition(opponentGoalImpact).y) > SoccerMap.GOAL_RADIUS) {
            return null;
        }   
                
        return opponentGoalImpact;
    }

    /**
     * Determine si un objet est dans l'axe de l'orientation courante.
     *
     * @param target objet/player visé
     * @param epsilon tolerance de visée
     * @return true si la target est dans l'axe, false sinon
     */
    public boolean isBetweenAngle(Vec2 target, double epsilon) {
        return SoccerUtils.isBetweenAngle(steerHeading, target, epsilon);
    }
    
    /**
     * L'angle de base est le steerHeading.
     *
     * @param estimatedTarget objectif du tir, pour la distance...
     * @param epsilon tolerance de visée
     * @return true si la trajectoire est bouché
     */
    public boolean isOpponentsOnPath(Vec2 estimatedTarget, double epsilon) {
        for (Vec2 opponent:getOpponents()) {

            if (opponent.r<estimatedTarget.r
                && SoccerUtils.getDistance(opponent, estimatedTarget).r<estimatedTarget.r) {

                if (isBetweenAngle(opponent, epsilon)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Repulsion a se coller contre le mur en face (adverse).
     *
     * @return vecteur de repulsion
     */
    public Vec2 getOpponentBoundRepulsion() {
        Vec2 aR=new Vec2(opponentGoal.x, 0);
        aR.setr(-0.1/(aR.r*aR.r));

        return aR;
    }
    
    /**
     * Repulsion a se coller sur les bords d'en haut et d'en bas
     *
     * @return vecteur de repulsion
     */
    public Vec2 getSideBoundRepulsion() {
        Vec2 topAR=new Vec2(0, BOUND_Y-position.y);
        topAR.setr(-0.1/(topAR.r*topAR.r));
        Vec2 bottomAR=new Vec2(0, -BOUND_Y-position.y);
        topAR.setr(-0.1/(bottomAR.r*bottomAR.r));
        
        topAR.add(bottomAR);
        return topAR;
    }
    
    /**
     * Repulsion a se coller a un joueur
     *
     * @param player
     * @return Vec2 d'attraction/repulsion d'un joueur. En l'occurence, toujours repulsif
     */
    public Vec2 getPlayerRepulsion(Vec2 player) {
        Vec2 aR=new Vec2(player);
        aR.setr(-0.1/(player.r*player.r));

        return aR;
    }
    
    /**
     * Repulsion des joueurs de l'equipe
     *
     * @param mult multiplicateur d'attraction/repulsion
     * @return Vec2 global de repulsion des joueurs de l'equipe
     */
    public Vec2 getTeammatesRepulsion(double mult) {
        Vec2 teammatesAR=new Vec2(0,0);
        for (Vec2 vTM:abstract_robot.getTeammates(curr_time)) {
            teammatesAR.add(getPlayerRepulsion(vTM));
        }
        teammatesAR.setr(teammatesAR.r*mult);
        return teammatesAR;
    }
    
    
    /**
     * Repulsion des joueurs adverses
     *
     * @param mult multiplicateur d'attraction/repulsion
     * @return Vec2 global de repulsion des joueurs de l'equipe adverse
     */
    public Vec2 getOpponentsRepulsion(double mult) {
        Vec2 opponentsAR=new Vec2(0,0);
        for (Vec2 vOpp:abstract_robot.getOpponents(curr_time)) {
            opponentsAR.add(getPlayerRepulsion(vOpp));
        }
        opponentsAR.setr(opponentsAR.r*mult);
        return opponentsAR;
    }

    /**
     * Dipole orienté vers le but adverse.
     * le 'dos' est est orienté vers notre but
     *
     * @return Vec2 global du dipole
     */
    public Vec2 getBallDipoleAR() {
        return getBallDipoleAR(abstract_robot.getOurGoal(curr_time),
                         abstract_robot.getOpponentsGoal(curr_time));
    }
    
    /**
     * Dipole de la ball orienté vers le but ennemi.
     *
     * @return Vec2 global du dipole
     */
    public Vec2 getLeadBallDipoleAR() {
        return getBallDipoleAR(opponentGoal);
    }

    /**
     * Dipole de la balle orienté vers le target et tournant le dos a back
     *
     * @param back 'dos' du dipole
     * @param target 'cible' du dipole
     * @return Vec2 global du dipole
     */
    public Vec2 getBallDipoleAR(Vec2 back, Vec2 target) {
        // pole attractif
        Vec2 pA=new Vec2(ball);
        // sur l'axe theirGoal-ball
        pA.sub(target);
        // pole attractif 'au derriere' de la balle
        // (on le decalle d'une distance de joueur deriere la balle)
        pA.setr(SocSmall.RADIUS-0.02);
        // vecteur a partir du joueur
        pA.add(ball);
        // ponderation puissance attractivite de la balle fct de la distance
        pA.setr(3.0/ball.r);

        // pole repulsif
        Vec2 pR=new Vec2(ball);
        // sur l'axe ourGoal-ball
        pR.sub(back);
        // pole attractif 'au front' de la balle
        pR.setr(SocSmall.RADIUS);
        // vecteur a partir du joueur
        pR.add(ball);
        // ponderation puissance attractivite de la balle fct de la distance
        //pR.setr(-4.0);
        pR.setr(-2.0/ball.r);

        // ponderation puissance ball
        Vec2 aR=new Vec2(pA);
        aR.add(pR);

        return aR;
    }

    /**
     * Dipole orienté vers target.
     * le pole repulsif est devant la balle sur l'axe target
     *
     * @param target 'cible' du dipole
     * @return Vec2 global du dipole
     */
    public Vec2 getBallDipoleAR(Vec2 target) {
        Vec2 axe=new Vec2(ball);
        axe.sub(target);
        
        // pole attractif
        Vec2 pA=new Vec2(axe);
        pA.setr(SocSmall.RADIUS-0.02);
        pA.add(ball);
        pA.setr(3.0/ball.r);
        
        //pole repulsif
        Vec2 pR=new Vec2(axe);
        pR.setr(-1*SocSmall.RADIUS);
        pR.add(ball);
        pR.setr(-2.0/ball.r);
        
        Vec2 aR=new Vec2(pA);
        aR.add(pR);
        
        return aR;
    }
    
    /**
     * Dipole de la ball orienté dans l'axe du but ami.
     * le but ami etant dans le 'dos'
     *
     * @return Vec2 global du dipole
     */
    public Vec2 getOurgoalBallDipoleAR() {
        Vec2 axe=new Vec2(ball);
        axe.sub(abstract_robot.getOurGoal(curr_time));
        
        // pole attractif
        Vec2 pA=new Vec2(axe);
        pA.setr(-1*SocSmall.RADIUS-0.02);
        pA.add(ball);
        pA.setr(3.0/ball.r);
        
        //pole repulsif
        Vec2 pR=new Vec2(axe);
        pR.setr(SocSmall.RADIUS);
        pR.add(ball);
        pR.setr(-2.0/ball.r);
        
        Vec2 aR=new Vec2(pA);
        aR.add(pR);
        
        return aR;
    }

    // -------- ACCESSEURS -------- //

    /**
     * Retourne le tableau des equipiers indicié par le numPlayer
     *
     * @return null si self ou trop loin
     */
    public Vec2[] getTeammates() {
        return abstract_robot.getTeammates(curr_time);
    }

    /**
     * Retourne le tableau des adversaire indicié par le numPlayer
     *
     * @return liste de Vec2 des adversaires
     */

    public Vec2[] getOpponents() {
        return abstract_robot.getOpponents(curr_time);
    }
    public SocSmall getAbstract_robot() {
        return abstract_robot;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getBall() {
        return ball;
    }

    public Vec2 getOurGoal() {
        return ourGoal;
    }

    public Vec2 getOpponentGoal() {
        return opponentGoal;
    }

    public double getSteerHeading() {
        return steerHeading;
    }

    public Map<Integer, Vec2> getRecvdTeammatePos() {
        return recvdTeammatePos;
    }

    // true si un joueur de l'equipe est plus proche de la balle que ce robot
    public boolean isTeammateNearestToBall() {
        return teammateNearestToBall;
    }

    //true si un joueur de l'equipe est plus proche de notre but que ce robot
    public boolean isTeammateNearestToOurgoal() {
        return teammateNearestToGoal;
    }

    public boolean canKick() {
        return abstract_robot.canKick(curr_time);
    }

    public boolean isWestTeam() {
        return westTeam;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public int getLongLock() {
        return longLock;
    }

    public int getCptPass() {
        return cptPass;
    }
    
    public void setCptPass(int newCptPass) {
        cptPass=newCptPass;
    }
    
    public static double getGOAL_RADIUS() {
        return GOAL_RADIUS;
    }
    
    public static double getMAX_DIST_PASS() {
        return MAX_DIST_PASS;
    }
    // ---------------- //
    
}

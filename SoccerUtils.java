package lakradiOutinTeam;

import EDU.gatech.cc.is.util.Units;
import EDU.gatech.cc.is.util.Vec2;
import java.text.NumberFormat;

/**
 * Contient uniquement des méthodes statiques, outils permettant
 * tous les calculs vectorielle sans rapport avec la SoccerMap
 * et le SocSmall;
 */
public class SoccerUtils {

    /**
     * Verifie que deux objet sont de position 'egale' a un
     * epsilone près (estimation)
     *
     * @param obj1
     * @param obj2
     * @param epsilon
     * @return
     */
    public static boolean equalsPosition(Vec2 obj1, Vec2 obj2, double epsilon) {
        return  (obj1.x<obj2.x+epsilon && obj1.x>obj2.x-epsilon
                && obj1.y < obj2.y+epsilon && obj1.y > obj2.y-epsilon);
    }
    
    /**
     * Retourne la valeur d'un radian entre pi et -pi
     *
     * @param rad angle modulé entre -pi et pi
     * @return
     */
    public static double ModuleAngle(double rad) {
        while (rad > Math.PI)  rad -= Units.PI2;
        while (rad < -Math.PI) rad += Units.PI2;
        return rad;
    }
    
    /**
     * Determine si un objet est dans l'axe de l'orientation courante.
     *
     * @param steerHeading orientation du viseur
     * @param relativeTarget objet/player visé
     * @param epsilon tolerance de visée
     * @return true si la target est dans l'axe, false sinon
     */
    public static boolean isBetweenAngle(double steerHeading, Vec2 relativeTarget, double epsilon) {
        double theta=ModuleAngle(relativeTarget.t-steerHeading);
        return Math.abs(theta)<epsilon;
    }
    
    /**
     * Distance entre 2 objets en position relative
     *
     * @param o1
     * @param o2
     * @return Vec2
     */
    public static Vec2 getDistance(Vec2 o1, Vec2 o2) {
        Vec2 tmp=new Vec2(o1);
        tmp.sub(o2);
        return tmp;
    }
    
    /**
     * @param vec
     * @return 
     */
    public static String displayVec2(Vec2 vec) {
        NumberFormat format=NumberFormat.getInstance();
        format.setMinimumFractionDigits(2); //nb de chiffres apres la virgule
        
	return ("(" + format.format(vec.x) + ","
                + format.format(vec.y) + ") ("
                + format.format(vec.r) + ","
                + format.format(vec.t) + ")");
    }
    
    /**
     * @param d
     * @return
     */
    public static String displayDouble(double d) {
        NumberFormat format=NumberFormat.getInstance();
        format.setMinimumFractionDigits(2); //nb de chiffres apres la virgule
        
	return format.format(d);
    }
}

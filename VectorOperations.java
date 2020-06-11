import java.util.Vector;

public abstract class VectorOperations {

    public static Vector<Double> somaVecs(Vector<Double> x1, Vector<Double> x2){
        Vector<Double> result = new Vector<Double>();
        for(int i = 0; i < x1.size(); i++)
            result.add(x1.get(i) + x2.get(i));
        return result;
    }

    public static Vector<Double> multiVecEscalar(Vector<Double> x, double escalar){
        Vector<Double> result = new Vector<Double>();
        for(int i = 0; i < x.size(); i++)
            result.add(x.get(i)*escalar);
        return result;
    }
}
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

    //v^t*v = escalar
    public static double multVecTxVec(Vector<Double> v1t, Vector<Double>v2){
        double result = 0;
        for(int i = 0; i < v1t.size(); i++)
            result += v1t.get(i)*v2.get(i);
        return result;
    }

    //v*v^t=M
    public static Vector<Vector<Double>> multiVecxVecT(Vector<Double> v1, Vector<Double>v2t){
        Vector<Vector<Double>> result = new Vector<Vector<Double>>();
        for(int i = 0; i < v1.size(); i++){
            Vector<Double> aux = new Vector<Double>();
            for(int j = 0; j < v2t.size(); j++)
                aux.add(v1.get(i)*v2t.get(j));
            result.add(aux);
        }
        return result;
    }

    //M*v=v
    public static Vector<Double> multiMatrizxVec(Vector<Vector<Double>> m, Vector<Double> v){
        Vector<Double> result = new Vector<Double>();
        for(int linha = 0; linha < m.size(); linha++){
            double soma = 0;
            for(int col = 0; col < m.get(linha).size(); col++)
                soma += m.get(linha).get(col)*v.get(col);
            result.add(soma);
        }
        return result;
    }

    //v^t*M=v^t -> considerar o vetor resultante como um vetor transposto
    public static Vector<Double> multiVecTxMatriz(Vector<Double> v, Vector<Vector<Double>> m){
        Vector<Double> result = new Vector<Double>();
        for(int i = 0; i < m.get(0).size(); i++){
            double soma = 0;
            for(int j = 0; j < v.size(); j++)
                soma += v.get(j)*m.get(j).get(i);
            result.add(soma);
        }
        return result;
    }

    //M*k=M
    public static Vector<Vector<Double>> multiMatrizEscalar(Vector<Vector<Double>> m, double escalar){
        Vector<Vector<Double>> result = new Vector<Vector<Double>>();
        for(int i = 0; i < m.size(); i++){
            Vector<Double> aux = new Vector<Double>();
            for(int j=0; j < m.get(i).size(); j++){
                aux.add(m.get(i).get(j)*escalar);
            }
            result.add(aux);
        }
        return result;
    }

    public static Vector<Vector<Double>> somaMatrizes(Vector<Vector<Double>> m1, Vector<Vector<Double>> m2){
        Vector<Vector<Double>> result = new Vector<Vector<Double>>();
        for(int i = 0; i < m1.size(); i++){
            Vector<Double> aux = new Vector<Double>();
            for(int j = 0; j < m1.get(i).size(); j++){
                aux.add(m1.get(i).get(j)+m2.get(i).get(j));
            }
            result.add(aux);
        }
        return result;
    }

}
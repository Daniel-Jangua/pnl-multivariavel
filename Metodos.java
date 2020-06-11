import java.util.Vector;

public class Metodos {
    private String fx;
    private Vector<Double> x0;
    private int n;
    private double epsilon;
    private int it = 0;

    Metodos(String fx, int n, Vector<Double> x0, double epsilon) throws Exception{
        this.fx = fx;
        this.n = n;
        this.x0 = x0;
        this.epsilon = epsilon;
        //teste se esta tudo ok na função
        Interpretador.FxRn(fx, x0);
    }

    public int getIt(){
        return it;
    }

    private double dist(Vector<Double> x1, Vector<Double> x2){
        double d = 0;
        for(int i = 0; i < n; i++)
            d += Math.pow(x1.get(i)-x2.get(i),2);
        d = Math.sqrt(d);
        return d;
    }

    public Vector<Double> coordCiclicas() throws Exception{
        it = 0;
        int k = 0;
        Vector<Double> yj ;//= new Vector<Double>();
        Vector<Vector<Double>> dj = new Vector<Vector<Double>>();
        Vector<Double> xk = x0;
        Vector<Double> xk_ant;
        double lambda = 0;
        //montando os vetores de direção dj
        for(int j = 0; j < n; j++){
            Vector<Double> aux = new Vector<Double>();
            for(int i = 0; i < n; i++){
                if(i == j)
                    aux.add(1.0);
                else
                    aux.add(0.0);
            }
            dj.add(aux);
        }
        do{
            k++;
            //yj = (Vector<Double>)xk.clone();
            yj = new Vector<Double>(xk);
            for(int j = 0; j < n; j++){
                //construindo a f(yj + lambda*dj)
                Vector<String> x_aux = new Vector<String>();
                for(int l = 0; l < n; l++){
                    String str = "("+yj.get(l) + "+" + "x*" + dj.get(j).get(l)+")";
                    x_aux.add(str);
                }
                String ant = fx;
                String fy = null;
                for(int i = 1; i <= n; i++){
                    fy = ant.replace("x"+i, x_aux.get(i-1));
                    ant = fy;
                }
                //minimizando f(yj + lambda*dj)
                lambda = BuscaReta.newton(fy, epsilon/10);
                
                //yj = yj + lambda * dj.get(j);
                yj = VectorOperations.somaVecs(yj, VectorOperations.multiVecEscalar(dj.get(j), lambda));
                
            }
            //xk_ant = (Vector<Double>)xk.clone();
            xk_ant = new Vector<Double>(xk);
            xk = yj;
        }while(dist(xk, xk_ant) >= epsilon);
        it = k;
        return xk;
    }

    public Vector<Double> hookeJeeves(){
        return x0;
    } 

    public Vector<Double> gradDesc(){
        return x0;
    }

    public Vector<Double> newton(){
        return x0;
    }

    public Vector<Double> gradConj(){
        return x0;
    }

    public Vector<Double> fletcherReeves(){
        return x0;
    }

    public Vector<Double> davidonFletcherPowell(){
        return x0;
    }

}
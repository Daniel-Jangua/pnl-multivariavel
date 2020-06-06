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

    public Vector<Double> coordCiclicas(){
        return x0;
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
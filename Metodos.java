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

    private double norma_gradiente(Vector<Double> vet){
        double d = 0;
        for(int i = 0; i < n; i++)
            d += Math.pow(vet.get(i), 2);
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

    public Vector<Double> hookeJeeves() throws Exception{
        it = 0;
        int k = 0;
        Vector<Double> yj;
        Vector<Double> d;
        Vector<Double> xk = x0;
        Vector<Double> xk_ant;
        double lambda = 0;
        Vector<Vector<Double>> dj = new Vector<Vector<Double>>();
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
        yj = new Vector<Double>(xk);
        while(true){
            k++;  
            //Passo 1
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
                System.out.println("Lambda "+j+" : " + lambda);
                //yj = yj + lambda * dj.get(j);
                yj = VectorOperations.somaVecs(yj, VectorOperations.multiVecEscalar(dj.get(j), lambda));
            }
            xk_ant = new Vector<Double>(xk);
            xk = yj;

            if(dist(xk, xk_ant) < epsilon)
                break;
            
            //Passo 2
            //d = xk+1 - xk
            d = VectorOperations.somaVecs(xk, VectorOperations.multiVecEscalar(xk_ant,-1.0));
            //construindo a f(xk+1 + lambda*d)
            Vector<String> x_aux = new Vector<String>();
            for(int l = 0; l < n; l++){
                String str = "("+xk.get(l) + "+" + "x*" + d.get(l)+")";
                x_aux.add(str);
            }
            String ant = fx;
            String fy = null;
            for(int i = 1; i <= n; i++){
                fy = ant.replace("x"+i, x_aux.get(i-1));
                ant = fy;
            }
            //minimizando f(xk+1 + lambda*d)
            lambda = BuscaReta.newton(fy, epsilon/10);
            System.out.println("Lambda: " + lambda);
            //yj = yj + lambda * dj.get(j);
            yj = VectorOperations.somaVecs(xk, VectorOperations.multiVecEscalar(d, lambda));
        }
        it = k;
        return xk;
    } 

    public Vector<Double> gradDesc() throws Exception{
        it = 0;
        int k = 0;
        Vector<Double> yj;
        Vector<Double> d;
        Vector<Double> xk_ant;
        Vector<Double> vet_grad, dk;//= new Vector<Double>();
        Vector<Double> xk = x0;
        double lambda = 0, CP = 0;

        vet_grad = Gradiente(fx, xk);
        CP = norma_gradiente(vet_grad);

        yj = new Vector<Double>(xk);
            while(CP >= epsilon){
                k++;
                dk = VectorOperations.multiVecEscalar(vet_grad, -1.0);
                
                for(int j = 0; j < n; j++){
                    //construindo a f(yj + lambda*dj)
                    Vector<String> x_aux = new Vector<String>();
                    for(int l = 0; l < n; l++){
                    
                        String str = "("+yj.get(l) + "+" + "x*" + dk.get(l)+")";
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
                    System.out.println("Lambda "+j+" : " + lambda);
                    //yj = yj + lambda * dj.get(j);
                    yj = VectorOperations.somaVecs(yj, VectorOperations.multiVecEscalar(dk, lambda));
                }
                xk_ant = new Vector<Double>(xk);
                xk = yj;                
                vet_grad = Gradiente(fx, xk_ant);
                CP = norma_gradiente(vet_grad);
            }
        it = k;
        return xk;
    }

    public Vector<Double> newton(){
        try {
            System.out.println("valor da derivada "+ Gradiente(fx, x0));
            
        } catch (Exception e) {
            //TODO: handle exception
        }
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

    public Vector<Double> Gradiente(String f, Vector<Double> x) throws Exception{
        double res;
        Vector<Double> Grad = new Vector<Double>();
        for(int i=0; i<x.size();i++){
            res = derivadaParcial_Primeira(f, x, i);
            System.out.println("\n valor var Grad \n"  + res);
            Grad.add(res);
        }
        return Grad;
    }

    public Vector<Vector<Double>> Hessiana(String f, Vector<Double> x) throws Exception{
        double res;
        Vector<Double> vet_aux;
        Vector<Vector<Double>> Hess = new Vector<Vector<Double>>();
        for(int i=0; i<x.size();i++){
                vet_aux = new Vector<Double>();
            for(int j=0;j<x.size();j++){
                res = derivadaParcial_Segunda(f, x, i , j);
                System.out.println("\n valor var DP2 \n"  + res);
                vet_aux.add(res);
            }
            Hess.add(vet_aux);
        }
        return Hess;
    }

    public Double derivadaParcial_Primeira(String f, Vector<Double> x, int i ) throws Exception{
        double eps = 1e-8; 
        double h, xi=0, q=0, p=0, d=0;
        double xplus_h = 0, xminus_h = 0; 
        Vector<Double> xcopia = new Vector<Double>(x);

        h = 1000*eps;
        xi = xcopia.get(i);
        xcopia.set(i, xi+h);
        xplus_h = Interpretador.FxRn(f, xcopia );

        xcopia.set(i, xi-h);
        xminus_h = Interpretador.FxRn(f, xcopia);
        p = (xplus_h-xminus_h)/(2*h);

        for(int k=1; k < 10; k++){
            q = p;
           // System.out.println("\nk= " + k+"valor de q: \n" + q);
            h = h/2;
            xcopia.set(i, xi+h);
            xplus_h = Interpretador.FxRn(f, xcopia);
            xcopia.set(i, xi-h);
            xminus_h =  Interpretador.FxRn(f, xcopia);
            p = (xplus_h-xminus_h)/(2*h);
           // System.out.println("\n**valor de p: \n" + p);
            //System.out.println("\n***valor de p-q: \n"+(p-q));
            if (Math.abs(p-q)<eps){
                d = p;
                break;
            }    
        }
        d = p;
        return d;
    }

    public Double derivadaParcial_Segunda(String f, Vector<Double> x, int i, int j ) throws Exception{
        double eps = 1e-5; 
        double h, xi=0, xj=0, q=0, p=0, d=0;
        double fx1,fx2,fx3,fx4;
        Vector<Double> xcopia = new Vector<Double>(x);

        h=1000*eps;
        xi = xcopia.get(i);
        xj = xcopia.get(j);
     //   System.out.println("valor de xi \n"+ xi);
        //System.out.println("valor de xj \n"+ xj);
            if(i!=j){
            //    System.out.println("\nprimeiro IF fora do FOR\n");
                xcopia.set(i, xi+h); 
                xcopia.set(j, xj+h);
                fx1 = Interpretador.FxRn(f, xcopia);
                
                xcopia.set(j, xj - h);
                fx2= Interpretador.FxRn(f, xcopia);

                xcopia.set(i, xi - h);
                fx4= Interpretador.FxRn(f, xcopia);

                xcopia.set(j, xj + h);
                fx3= Interpretador.FxRn(f, xcopia);

                p = (fx1 - fx2 - fx3 + fx4) / (4*h*h); 
         //       System.out.println("\n**valor de P dentro IF e for do FOR \n" + p);   
            }
            else{
            //    System.out.println("\nprimeiro ELSE fora do FOR\n");
                xcopia.set(i, xi + (2*h));
                fx1 = Interpretador.FxRn(f, xcopia);
           //     System.out.println("fx1 " + fx1);

                xcopia.set(i, xi - (2*h));
                fx3 = Interpretador.FxRn(f, xcopia);
             //   System.out.println("fx3 " + fx3);

                xcopia.set(i, xi);
                fx2 = Interpretador.FxRn(f, xcopia);
               // System.out.println("fx2 " + fx2);

                p = (fx1 - (2*fx2) + fx3)/(4*h*h);
          //      System.out.println("\n**valor de P dentro ELSE e for do FOR \n" + p); 
            }
        
            for(int k=1;k<10;k++){
                q = p;
                h = h/2;

                if(i != j){
          //          System.out.println("\nprimeiro IF dentro do FOR\n");
                    xcopia.set(i, xi+h); 
                    xcopia.set(j, xj+h);
                    fx1 = Interpretador.FxRn(f, xcopia);
                    
                    xcopia.set(j, xj - h);
                    fx2= Interpretador.FxRn(f, xcopia);

                    xcopia.set(i, xi - h);
                    fx4= Interpretador.FxRn(f, xcopia);

                    xcopia.set(j, xj + h);
                    fx3= Interpretador.FxRn(f, xcopia);

                    p = (fx1 - fx2 - fx3 + fx4) / (4*h*h);
         //           System.out.println("\n**valor de P dentro IF e dentro do FOR \n" + p); 
                }
                else{
              //      System.out.println("\nprimeiro ELSE dentro do FOR\n");
                    xcopia.set(i, xi + (2*h));
                    fx1 = Interpretador.FxRn(f, xcopia);
    
                    xcopia.set(i, xi - (2*h));
                    fx3 = Interpretador.FxRn(f, xcopia);
    
                    xcopia.set(i, xi);
                    fx2 = Interpretador.FxRn(f, xcopia);
    
                    p = (fx1 - (2*fx2) + fx3)/(4*h*h);
           //         System.out.println("\n**valor de P dentro ELSE e dentro do FOR \n" + p);
                }
           //     System.out.println("k " + k+"valor de p-q \n" + Math.abs(p-q));
                if(Math.abs(p-q) < eps){
                    d = p;
                    break;
                }
            }
            d = p;
            return d;
    }

}
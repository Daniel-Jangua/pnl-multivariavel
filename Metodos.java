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
                    String str = "(("+yj.get(l) + ")+" + "x*(" + dj.get(j).get(l)+"))";
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
                    String str = "(("+yj.get(l) + ")+" + "x*(" + dj.get(j).get(l)+"))";
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
                String str = "(("+xk.get(l) + ")+" + "x*(" + d.get(l)+"))";
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
                    
                        String str = "(("+yj.get(l) + ")+" + "x*(" + dk.get(l)+"))";
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

    public Vector<Double> newton() throws Exception{
    	it = 0;
    	int k = 0;
    	Vector<Double> xk = new Vector<Double>(x0);
    	Vector<Double> xk_ant;
    	do {
    		k++;
    		Vector<Double> gk = Gradiente(fx, xk);
    		Vector<Vector<Double>> hes = Hessiana(fx, gk);
    		Vector<Double> w = new Vector<Double>();
    		w = Cholesky.resolveSistema(hes, VectorOperations.multiVecEscalar(gk, -1));
    		xk_ant = new Vector<Double>(xk);
    		xk = VectorOperations.somaVecs(xk_ant, w);
    	}while(norma_gradiente(xk) >= epsilon && (dist(xk, xk_ant) >= epsilon));
    	it = k;
        return xk;
    }
    
    public Vector<Double> gradConj() throws Exception{
        Vector<Double> gk, dk, xk = x0, xk_ant;
        Vector<Double> Hxk_dk, gk_mais_Negativo;
        Vector<Double> yj;
        Vector<Vector<Double>> hessian;
        double lambda, CP = 0, dkt_Hxk, gkt_dk, beta_numerador=0, beta_denominador=0, beta = 0;
        int its = 0;

        while(true){
            its++;
            //passo 1:
                gk = Gradiente(fx, xk);
                hessian = Hessiana(fx, gk);
                dk = VectorOperations.multiVecEscalar(gk, -1.0);
            //passo 2:
                for (int K_iteracao = 0; K_iteracao < n; K_iteracao++) {
                    //passo A)
                        gkt_dk = VectorOperations.multVecTxVec(gk, dk);             //v^t*v = escalar   //NUMERADOR
                        Hxk_dk = VectorOperations.multiMatrizxVec(hessian, dk);     //M*v=v             //DENOMINADOR
                        dkt_Hxk = VectorOperations.multVecTxVec(dk, Hxk_dk);        //v^t*v = escalar   //DENOMINADOR
                        lambda = -1.0*(gkt_dk / dkt_Hxk);
                        xk_ant = xk;
                        xk = VectorOperations.somaVecs(xk_ant, VectorOperations.multiVecEscalar(dk, lambda));
                    //passo B)
                        gk = Gradiente(fx, xk);
                    //passo C)
                        if(K_iteracao < n-1){
                            beta_numerador = VectorOperations.multVecTxVec(gk, Hxk_dk);   //v^t*v = escalar
                            beta_denominador = VectorOperations.multVecTxVec(dk, Hxk_dk);       //v^t*v = escalar
                            beta = beta_numerador / beta_denominador;
                            gk_mais_Negativo = VectorOperations.multiVecEscalar(gk, -1.0);
                            dk = (VectorOperations.somaVecs(gk_mais_Negativo, VectorOperations.multiVecEscalar(dk, beta)));
                            if(norma_gradiente(dk) < 1e-8)
                                break;
                        }
                        else
                            break;
                }
            gk = Gradiente(fx, xk);
            CP = norma_gradiente(gk);
             if(CP <= epsilon)
                break;
        }
    it = its;
    return xk;
}

    public Vector<Double> fletcherReeves() throws Exception{
        Vector<Double> gk;
        Vector<Double> gk_mais1 = null;
        Vector<Double> x = new Vector<Double>(x0);
        Vector<Double> dk;
        double lambda, beta;
        gk = Gradiente(fx, x);
        dk = VectorOperations.multiVecEscalar(gk, -1);
        int its = 0;
        //Passo 1
        while(norma_gradiente(gk) >= epsilon){
            its++;
            //Passo 2
            for(int k = 0; k < n; k++){
                //Construindo f(xk+lambda*dk)
                Vector<String> x_aux = new Vector<String>();
                for(int l = 0; l < n; l++){
                    String str = "(("+x.get(l) + ")+" + "x*(" + dk.get(l)+"))";
                    x_aux.add(str);
                }
                String ant = fx;
                String fy = null;
                for(int i = 1; i <= n; i++){
                    fy = ant.replace("x"+i, x_aux.get(i-1));
                    ant = fy;
                }
                //minimizando f(xk + lambda*dk)
                lambda = BuscaReta.newton(fy, epsilon/10);
                //xk+1 = xk + lambda*dk
                
                if(norma_gradiente(dk) > 1e-8)
                    x = VectorOperations.somaVecs(x, VectorOperations.multiVecEscalar(dk, lambda));

                gk_mais1 = Gradiente(fx, x);
               
                if(k < n-1){        
                    beta = VectorOperations.multVecTxVec(gk_mais1, gk_mais1)/VectorOperations.multVecTxVec(gk, gk);
                    dk = VectorOperations.somaVecs(VectorOperations.multiVecEscalar(gk_mais1, -1), VectorOperations.multiVecEscalar(dk, beta));
                }
            }
            //Passo 3
            gk = gk_mais1;
            dk = VectorOperations.multiVecEscalar(gk, -1);
        }
        it = its;
        return x;
    }

    public Vector<Double> davidonFletcherPowell() throws Exception{
        Vector<Vector<Double>> sk,alfa,beta;
        Vector<Double> gk,dk,gk_mais1,qk,pk;
        Vector<Double> x = new Vector<Double>(x0);
        double lambda;

        gk = Gradiente(fx, x);
        int its = 0;
        while(true){
            its++;
            sk = new Vector<Vector<Double>>();
            for(int i = 0; i < n; i++){
                Vector<Double> aux = new Vector<Double>();
                for(int j = 0; j < n; j++){
                    if(i == j)
                        aux.add(1.0);
                    else
                        aux.add(1.0);
                }
                sk.add(aux);
            }
            for(int k = 0; k < n; k++){
                if(norma_gradiente(gk) < epsilon){
                    it = its;
                    return x;
                }
                dk = VectorOperations.multiVecEscalar(VectorOperations.multiMatrizxVec(sk, gk), -1);
                //Construindo f(xk+lambda*dk)
                Vector<String> x_aux = new Vector<String>();
                for(int l = 0; l < n; l++){
                    String str = "(("+x.get(l) + ")+" + "x*(" + dk.get(l)+"))";
                    x_aux.add(str);
                }
                String ant = fx;
                String fy = null;
                for(int i = 1; i <= n; i++){
                    fy = ant.replace("x"+i, x_aux.get(i-1));
                    ant = fy;
                }
               
                //minimizando f(xk + lambda*dk)
                lambda = BuscaReta.newton(fy, epsilon/10);
                if(Double.isNaN(lambda)){
                    it = its;
                    return x;
                }
                //xk+1 = xk + lambda*dk
                x = VectorOperations.somaVecs(x, VectorOperations.multiVecEscalar(dk, lambda));
              
                
                if(k < n-1){
                    gk_mais1 = Gradiente(fx, x);
                    qk = VectorOperations.somaVecs(gk_mais1, VectorOperations.multiVecEscalar(gk, -1));
                    pk = VectorOperations.multiVecEscalar(dk, lambda);
                    alfa = VectorOperations.multiMatrizEscalar(VectorOperations.multiVecxVecT(pk, pk), 1/(VectorOperations.multVecTxVec(pk, qk)));
                    beta = VectorOperations.multiMatrizEscalar(VectorOperations.multiVecxVecT(VectorOperations.multiMatrizxVec(sk, qk), VectorOperations.multiVecTxMatriz(qk, sk)), 1/(VectorOperations.multVecTxVec(VectorOperations.multiVecTxMatriz(qk, sk), qk)));
                    sk = VectorOperations.somaMatrizes(VectorOperations.somaMatrizes(sk, alfa),VectorOperations.multiMatrizEscalar(beta, -1));
                    gk = gk_mais1;
                }else{
                    gk = Gradiente(fx, x);
                }
            }
            
        }
    }

    public Vector<Double> Gradiente(String f, Vector<Double> x) throws Exception{
        double res;
        Vector<Double> Grad = new Vector<Double>();
        for(int i=0; i<x.size();i++){
            res = derivadaParcial_Primeira(f, x, i);
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
            h = h/2;
            xcopia.set(i, xi+h);
            xplus_h = Interpretador.FxRn(f, xcopia);
            xcopia.set(i, xi-h);
            xminus_h =  Interpretador.FxRn(f, xcopia);
            p = (xplus_h-xminus_h)/(2*h);
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
            if(i!=j){
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
            }
            else{
                xcopia.set(i, xi + (2*h));
                fx1 = Interpretador.FxRn(f, xcopia);

                xcopia.set(i, xi - (2*h));
                fx3 = Interpretador.FxRn(f, xcopia);

                xcopia.set(i, xi);
                fx2 = Interpretador.FxRn(f, xcopia);

                p = (fx1 - (2*fx2) + fx3)/(4*h*h);
            }
        
            for(int k=1;k<10;k++){
                q = p;
                h = h/2;

                if(i != j){
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
                }
                else{
                    xcopia.set(i, xi + (2*h));
                    fx1 = Interpretador.FxRn(f, xcopia);
    
                    xcopia.set(i, xi - (2*h));
                    fx3 = Interpretador.FxRn(f, xcopia);
    
                    xcopia.set(i, xi);
                    fx2 = Interpretador.FxRn(f, xcopia);
    
                    p = (fx1 - (2*fx2) + fx3)/(4*h*h);
                }
                if(Math.abs(p-q) < eps){
                    d = p;
                    break;
                }
            }
            d = p;
            return d;
    }

}
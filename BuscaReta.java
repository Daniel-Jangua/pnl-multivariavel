public abstract class BuscaReta {

    private static double erro(double fx, double fx_ant) {
        double denom = Math.max(Math.abs(fx), 1);
        return Math.abs(fx - fx_ant) / denom;
    }
    
    private static double derivadaPrimeira(String f, double x ) throws Exception{
        double eps = 1e-8;
        double h = 1.0, fx = 0, fx_ant = Double.MAX_VALUE;
        double xplus_h = 0, xminus_h = 0; //xplus_h = f(x + h); xminus_h = f(x - h)

        double err;
        double err_ant = Double.MAX_VALUE;

        for(int i=0; i < 10; i++){
            xplus_h = Interpretador.FxR1(f, x + h);
            xminus_h = Interpretador.FxR1(f, x - h);
            fx = (xplus_h - xminus_h)/(2*h);
            err = erro(fx, fx_ant);
            if (err < eps) {
                break;
            }

            fx_ant = fx;

            if (err_ant < err) {
                // Erro crescente
                break;
            }
            err_ant = err;
            h = h/2;
        }
        return fx;
    }

    private static double derivadaSegunda(String f, double x ) throws Exception{
        double eps = 1e-8;
        double h = 1.0, fx = 0, fx_ant = Double.MAX_VALUE;
        double xp_hh = 0, xm_hh = 0, dois_fx = 0; //xp_hh = (x+2h); xm_hh = (x-2h); dois_fx = 2*f(x) 

        double err;
        double err_ant = Double.MAX_VALUE;

        for(int i=0; i < 10; i++){
            
            xp_hh = Interpretador.FxR1(f, x + (2*h));
            xm_hh = Interpretador.FxR1(f, x - (2*h));
            dois_fx = 2 * Interpretador.FxR1(f, x);

            fx = (xp_hh - dois_fx + xm_hh)/(4 * h * h);
            
            err = erro(fx, fx_ant);
            if (err < eps) {
                break;
            }

            fx_ant = fx;

            if (err_ant < err) {
                // Erro crescente
                break;
            }
            err_ant = err;
            h = h/2;
        }
      //  System.out.println("valor fora do LACO de D = " + d);
        return fx;
    }

    public static double newton(String func, double val_epsilon) throws Exception{
        double fx_linha = 0, fx_duaslinhas = 0, xk = 0, x_kmais1 = 0;
        double cp = Double.MAX_VALUE;
        //int k = 0;
        while(cp > val_epsilon){
            fx_linha = derivadaPrimeira(func, xk);
            fx_duaslinhas = derivadaSegunda(func, xk);
            x_kmais1 = xk - (fx_linha / fx_duaslinhas);
            //fxkmais1_linha = derivadaPrimeira(func, x_kmais1);
            cp = Math.abs(x_kmais1 - xk) / Math.max(Math.abs(x_kmais1), 1.0);
            xk = x_kmais1;
        }
        return x_kmais1;
    }

}
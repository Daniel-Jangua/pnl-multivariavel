import com.udojava.evalex.*;
import java.math.*;
import java.util.*;

public abstract class Interpretador{

    static double FxR1(String f, double x) throws Exception{
        Expression e;
        e = new Expression(f);
        e.with("x", new BigDecimal(x));
        return e.eval().doubleValue();
    }

    static double FxRn(String f, Vector<Double> x) throws Exception{
        Expression e;
        int i = 1;
        e = new Expression(f);
        Iterator it = x.iterator();
        while(it.hasNext()){
            e.with("x"+i, new BigDecimal((Double)it.next()));
            i++;
        }
        return e.eval().doubleValue();
    }
}
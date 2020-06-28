package cholesky;


import java.util.Vector;

public class Cholesky {
	public static double[][] chol(double[][] a){
		int m = a.length;
		double[][] l = new double[m][m]; //automatically initialzed to 0's
		for(int i = 0; i< m;i++){
			for(int k = 0; k < (i+1); k++){
				double sum = 0;
				for(int j = 0; j < k; j++){
					sum += l[i][j] * l[k][j];
				}
				l[i][k] = (i == k) ? Math.sqrt(a[i][i] - sum) :
					(1.0 / l[k][k] * (a[i][k] - sum));
			}
		}
		return l;
	}
	
	public static Vector<Vector<Double>> decompChol(Vector<Vector<Double>> v){
		Vector<Vector<Double>> result = new Vector<Vector<Double>>();
		Vector<Double> aux = new Vector<Double>();
		double[][] a = new double[v.size()][v.size()];
		for(int linha = 0; linha < v.size()  ; linha++ ) {//passando do vector pra matriz
			for(int col = 0; col < v.get(linha).size() ; col++) {
				a[linha][col] = v.get(linha).get(col);
			}
		}
		
		double l[][] = chol(a);

		for(int linha = 0; linha < v.size()  ; linha++ ) {
			aux = new Vector<Double>();
			for(int col = 0; col < v.get(linha).size() ; col++) {
				aux.add(l[linha][col]);
			}
			result.add(aux);
		}
		return result;
	}
	
	public static Vector<Vector<Double>> matrizTransposta(Vector<Vector<Double>> v){
		Vector<Vector<Double>> result = new Vector<Vector<Double>>();
		Vector<Double> aux = new Vector<Double>();
		for(int linha = 0; linha < v.size()  ; linha++ ) {
			aux = new Vector<Double>();
			for(int col = 0; col < v.get(linha).size() ; col++) {
				aux.add(v.get(col).get(linha));
			}
			result.add(aux);
		}
		return result;
	}
	
	//metodo para resolver G.y=b, recebe G(matriz decomposta) e b(termos independentes), retorna y
	public static Vector<Double> substituicao(Vector<Vector<Double>> v, Vector<Double> independente){
		Vector<Double> y = new Vector<Double>();
		int n = v.size();
		Double soma;
		y.add(independente.get(0)/v.get(0).get(0));
		for(int i = 1; i < n; i++) {
			soma = 0.0;
			for(int j = 0; j <= i-1; j++) {
				soma = soma+v.get(i).get(j) * y.get(j);
			}
			y.add((independente.get(i) - soma) / v.get(i).get(i));
		}
		return y;
	}
	
	//soluçao de G^t*x=y, recebe G^t(matrizTransposta(decompChol(v))) e y (independentes)
	public static Vector<Double> retroSubstituicao(Vector<Vector<Double>> v, Vector<Double> y){
		Vector<Double> x = new Vector<Double>();
		int n = v.size();
		Double soma;
		for(int i = 0; i < n; i++) {
			x.add(0.0);
		}
		//System.out.println(y.get(n-1));
		x.set(n-1, y.get(n-1) / v.get(n-1).get(n-1));
		for(int i = n-2; i >= 0; i--) {//certo
			soma = 0.0;
			for(int j = i+1; j < n; j++) {
				soma = soma + ((v.get(i).get(j)) * x.get(j));
			}
			x.set(i,(y.get(i) - soma) / v.get(i).get(i));
		}
		return x;
	}
	
	public static Vector<Double> resolveSistema(Vector<Vector<Double>> matriz, Vector<Double> independentes){
		return retroSubstituicao(matrizTransposta(decompChol(matriz)), substituicao(decompChol(matriz), independentes));
	}
}

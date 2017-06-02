import java.util.concurrent.ThreadLocalRandom;

public class Individuo {

	private int[] cadena;
	private double probabilidad;
	private int[] casillas;
	private int aptitud;
	
	public Individuo(int r){
		generacionAleatoria(r);
		probabilidad = 0;
		casillas = new int[2];
	}


	private void generacionAleatoria(int r) {
		this.cadena = new int[r];
		for(int i = 0; i < r; i++){
			cadena[i] = ThreadLocalRandom.current().nextInt(0, 1 + 1);
		}
		
	}
	
	public void setCadena(int[] cadena){
		this.cadena = cadena;
	}
	
	public int[] getCadena(){
		return cadena;
	}
	
	public int getAptitud(){
		int aptitud = 0;
		for(int i = 0; i < cadena.length; i++){
			aptitud += cadena[i];
		}
		this.aptitud = aptitud;
		return aptitud;
	}


	public void setProbabilidad(int sumaProbabilidades) {
		if(sumaProbabilidades != 0){
			this.probabilidad = ((double)getAptitud())/sumaProbabilidades;
		}else{
			this.probabilidad = 0;
		}	
		
	}
	
	public double getProbabilidad(){
		return probabilidad;
	}
	
	public void setCasillas(int  casillaInicial, int casillaFinal){
		casillas[0] = casillaInicial;
		casillas[1] = casillaFinal;
	}

	/**
	 * Metodo que devuelve true si la casilla pasada por argumento pertenece a las asignadas
	 * a este individuo.
	 * @param casilla
	 * @return
	 */
	public boolean hasCasilla(int casilla) {
		return (casillas[0] <= casilla) && (casillas[1] >= casilla);
	}


	
}

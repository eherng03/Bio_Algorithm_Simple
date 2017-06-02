import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
/**
 * 
 * @author Eva
 *Prueba: alfabeto del 1 al 9 y que encuentre la cadena con el mayor numero de 7.
 */
public class Main {

	private static int r = 7;
	private static int num_max_generaciones = 20;
	private static Individuo[] individuos;
	private static int num_casillas = 100;
	private static float probabilidad_crossover = (float) 0.70;
	private static double probabilidad_mutacion = 0.01;
	private static int mejorAptitud;
	
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int n = scan.nextInt();
		individuos = new Individuo[n];
		
		generacionAleatoria();
		
		int t = 0;
		while(t < num_max_generaciones){
			System.out.println("Generación " + t + ":");
			Individuo[] modificados = seleccion();
			modificados = crossOver(modificados);
			modificados = mutacion(modificados);
			
			//========================================================
			int mejor = 0;
			int media = 0;
			
			for(int i = 0; i < modificados.length; i++){
				if(modificados[i].getAptitud() >= modificados[mejor].getAptitud()){
					mejor = i;
				}
				media += modificados[i].getAptitud();
			}
			setProbabilidades(media);
			media = media/modificados.length;
			
			if(modificados[mejor].getAptitud() >= mejorAptitud ){
				individuos = modificados;
			}
			if(modificados[mejor].getAptitud() == individuos[0].getCadena().length){
				System.out.println("Hemos encontrado al mejor individuo:" + Arrays.toString(modificados[mejor].getCadena()) + ", con aptitud -> " + modificados[mejor].getAptitud());
				break;
			}
			t++;
		}
	}





	private static Individuo[] mutacion(Individuo[] modificados) {
		for(int i = 0; i < modificados.length; i++){
			int[] cadena = modificados[i].getCadena();
			for(int j = 0; j < cadena.length; j++){
				double rand = ThreadLocalRandom.current().nextDouble();
				if(rand <= probabilidad_mutacion){
					cadena[j] = (cadena[j] + 1) % 2;
				}
				//si no no muta
			}
			modificados[i].setCadena(cadena);
		}
		return modificados;
		
	}



	/**
	 * 
	 */
	private static Individuo[] seleccion() {
		int mejor = 0;
		int media = 0;
		
		for(int i = 0; i < individuos.length; i++){
			if(individuos[i].getAptitud() >= individuos[mejor].getAptitud()){
				mejor = i;
			}
			media += individuos[i].getAptitud();
		}
		setProbabilidades(media);
		media = media/individuos.length;
		System.out.println("El mejor individuo de la generacion es: " +  Arrays.toString(individuos[mejor].getCadena()) + ", con aptitud -> " + individuos[mejor].getAptitud());
		mejorAptitud = individuos[mejor].getAptitud();
		System.out.println("La media de aptitudes es: " + media);
		return setCasillas(mejor);
	}

	/**
	 * 
	 * @param mejor
	 */
	private static Individuo[] setCasillas(int mejor) {
		int[] casillas = new int[individuos.length];
		int sumaCasillas = 0;
		for(int i = 0; i < individuos.length; i++){
			casillas[i] = (int) (individuos[i].getProbabilidad() * 100);
			System.out.println("El numero de casillas del individuo " + (i+1) + " es " + casillas[i]);
			sumaCasillas += casillas[i];
		}
		
		int casillasVacias = num_casillas - sumaCasillas;
		
		System.out.println("El número de casillas es: " + sumaCasillas + ", le sumamos " + casillasVacias + " al mejor individuo.");
		
		if(casillasVacias != 0){
			casillas[mejor] += casillasVacias;
		}
		
		
		int i = 0;
		int individuo = 0;
		while(individuo < individuos.length){
			individuos[individuo].setCasillas(i, i + casillas[individuo] - 1);
			i += casillas[individuo];
			individuo++;
		}	
		
		return girarRuleta();
	}

	
	/**
	 * 
	 */
	private static Individuo[] girarRuleta() {
		Individuo[] seleccionados = new Individuo[individuos.length];
		for(int i = 0; i < individuos.length; i++){
			int casilla = ThreadLocalRandom.current().nextInt(0, num_casillas + 1);
			int individuo = buscarIndividuo(casilla);
			seleccionados[i] = individuos[individuo];
		}
		return seleccionados;
	}

	private static int buscarIndividuo(int casilla) {
		for(int i = 0; i < individuos.length; i++){
			if(individuos[i].hasCasilla(casilla)){
				return i;
			}
		}
		return 0;
	}



	/**
	 * 
	 * @param media
	 */
	private static void setProbabilidades(int media) {
		for(int i = 0; i < individuos.length; i++){
			individuos[i].setProbabilidad(media);
		}
		
	}

	/**
	 * 
	 */
	private static void generacionAleatoria() {
		for(int i = 0; i < individuos.length; i++){
			individuos[i] = new Individuo(r);
		}
		
	}
	
	/**
	 * @param modificados 
	 * @return 
	 * @return 
	 * 
	 */
	private static Individuo[] crossOver(Individuo[] modificados) {
		ArrayList<Individuo> cruzan = new ArrayList<>();
		ArrayList<Integer> posiciones = new ArrayList<>();
		for(int i = 0; i < individuos.length; i++){
			double aux = ThreadLocalRandom.current().nextDouble();
			if(aux <= probabilidad_crossover){
				cruzan.add(individuos[i]);
				posiciones.add(i);	//los que cruzan los pongo a null para volver a introducirlos despues del crossover
			}
		}
		
		if(cruzan.size() % 2 != 0){
			int aux = ThreadLocalRandom.current().nextInt(0, cruzan.size()-1);
			cruzan.remove(aux);
			posiciones.remove(aux);
		}
		int numParejas = cruzan.size() / 2;
		int contador = 0;
		
		for(int j = 0; j < numParejas; j++){
			int[][] cadenas = new int[2][cruzan.get(0).getCadena().length];
			int  corte = ThreadLocalRandom.current().nextInt(0, individuos[0].getCadena().length);
			int k;
			//Numeros hasta el corte
			for(k = 0; k < corte; k++){
				cadenas[0][k] = cruzan.get(contador).getCadena()[k];
				cadenas[1][k] = cruzan.get(contador+1).getCadena()[k];
			}
			//Numeros despues del corte
			while( k < cruzan.get(0).getCadena().length){
				cadenas[0][k] = cruzan.get(contador+1).getCadena()[k];
				cadenas[1][k] = cruzan.get(contador).getCadena()[k];
				k++;
			}
			cruzan.get(contador).setCadena(cadenas[0]);
			cruzan.get(contador+1).setCadena(cadenas[1]);
			contador += 2;
			if(contador+1 >= cruzan.size()){
				break;
			}
		}
		
		for(int i = 0; i < posiciones.size(); i++){
			modificados[posiciones.get(i)] = cruzan.get(i);
		}
		return modificados;
	}
}

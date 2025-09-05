package model;

import java.util.Random;
import java.util.Arrays;

public class GeneradorData {
    private long semilla;
    private Random random;

    public GeneradorData(){
        this.semilla = System.currentTimeMillis();
        this.random = new Random(semilla);
    }
    public GeneradorData(long semilla){
        setSemilla(semilla);
    }

    public void setSemilla(long semilla){
        this.semilla = semilla;
        this.random = new Random(semilla);
    }
    public long getSemilla(){
        return semilla;
    }
    public Candidato[] generarDatosAleatorios(int n, int m){
        Candidato[] candidatos = new Candidato[n];

        for (int i = 0; i <n; i++){
            candidatos[i] = new Candidato( i + 1);
            for (int j = 0; j <5; j++){
                candidatos[i].setAtributo(j, random.nextInt(m) + 1);
            }
        }
        return candidatos;
    }
    public Candidato[] generarDatosCasiOrdenados(int n, int m){
        Candidato[] candidatos = new Candidato[n];

        for (int i = 0; i <n; i++){
            candidatos[i] = new Candidato( i + 1);
            for (int j = 0; j <5; j++){
                int valorBase = (int) (((double)i/n)* m)+1;
                int variacion = (int) (m * 0.1); // 10
                int valor = Math.max(1, Math.min(m,
                    valorBase + random.nextInt(2 * variacion + 1)- variacion));
                candidatos[i].setAtributo(j, valor);
            }
        }
        int numPerturbaciones = Math.max(1, n / 20);
        for (int p = 0; p < numPerturbaciones; p++){
            int i = random.nextInt(n);
            int j = random.nextInt(n);
            if (i != j){
                intercambiarCandidatos(candidatos, i , j);

            }
        }
        return candidatos;

    }
    public Candidato[] generarDatosOrdenInverso(int n, int m) {
        Candidato[] candidatos = new Candidato[n];
        
        for (int i = 0; i < n; i++) {
            candidatos[i] = new Candidato(i + 1);
            for (int j = 0; j < 5; j++) {
                // Valores en orden descendente
                int valor = m - (int) (((double) i / (n - 1)) * (m - 1));
                candidatos[i].setAtributo(j, valor);
            }
        }
        
        return candidatos;
    }

    public void intercambiarCandidatos(Candidato[] candidatos, int i, int j){
        Candidato temp = candidatos[i];
        candidatos[i] = candidatos[j];
        candidatos[j] = temp;
    }
    
    public static Candidato[] copiarArreglo(Candidato[] original){
        Candidato[] copia = new Candidato[original.length];
        for (int i = 0; i < original.length; i++){
            copia[i] = new Candidato(original[i].getId(), original[i].getAtributos());
        }
        return copia;
    }
    public static boolean estaOrdenado(Candidato[] candidatos, int atributoIndex){
        for ( int i = 1; i < candidatos.length; i++){
            if (candidatos[i-1].getAtributo(atributoIndex) > 
                candidatos[i].getAtributo(atributoIndex)){
                return false;
            }
        }
        return true;

    }
    
}
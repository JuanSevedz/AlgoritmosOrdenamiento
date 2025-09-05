package util;

import model.Candidato;
import model.Metricas;

public class Validador {

    public boolean verificarOrdenCorrecto(Candidato[] arr, int atributoIndex) {
        if (arr == null || arr.length <= 1) {
            return true; // Un arreglo vacío o con un elemento siempre está ordenado
        }
        
        for (int i = 1; i < arr.length; i++) {
            int valorAnterior = arr[i - 1].getAtributo(atributoIndex);
            int valorActual = arr[i].getAtributo(atributoIndex);
            
            if (valorAnterior > valorActual) {
                System.err.printf("Error de ordenamiento en posiciones %d y %d: %d > %d%n", 
                                i - 1, i, valorAnterior, valorActual);
                return false;
            }
        }
        
        return true;
    }

    public boolean verificarConsistenciaMetricas(Metricas metricas, String algoritmo) {
        if (metricas == null) {
            System.err.println("Métricas nulas para algoritmo: " + algoritmo);
            return false;
        }
        
        // Verificaciones basicas
        if (metricas.getComparaciones() < 0) {
            System.err.println("Número de comparaciones negativo en " + algoritmo);
            return false;
        }
        
        if (metricas.getIntercambios() < 0) {
            System.err.println("Número de intercambios negativo en " + algoritmo);
            return false;
        }
        
        if (metricas.getTiempoEjecucion() < 0) {
            System.err.println("Tiempo de ejecución negativo en " + algoritmo);
            return false;
        }
        
        // Verificaciones especificas por algoritmo
        return verificarRangosEsperados(metricas, algoritmo);
    }
    
    private boolean verificarRangosEsperados(Metricas metricas, String algoritmo) {
        long comparaciones = metricas.getComparaciones();
        long intercambios = metricas.getIntercambios();
        
        switch (algoritmo.toLowerCase()) {
            case "burbuja":
            case "bubble":
                // Bubble sort: comparaciones e intercambios
                if (intercambios > comparaciones) {
                    System.err.println("Bubble sort: más intercambios que comparaciones");
                    return false;
                }
                break;
                
            case "insercion":
            case "insertion":
                // Insertion sort: intercambios
                if (intercambios > comparaciones) {
                    System.err.println("Insertion sort: más intercambios que comparaciones");
                    return false;
                }
                break;
                
            case "seleccion":
            case "selection":
                // Selection sort: número fijo de comparaciones, intercambios mínimos
                if (comparaciones == 0) {
                    System.err.println("Selection sort: debe hacer comparaciones");
                    return false;
                }
                break;
                
            case "merge":
                // Merge sort: no hace intercambios directos
                if (intercambios > 0) {
                    System.err.println("Merge sort no debería reportar intercambios");
                    return false;
                }
                break;
                
            case "quick":
                // Quick sort: debe hacer comparaciones y particiones
                if (comparaciones == 0) {
                    System.err.println("Quick sort: debe hacer comparaciones");
                    return false;
                }
                break;
                
            default:
                System.err.println("Algoritmo desconocido para validación: " + algoritmo);
                return false;
        }
        
        return true;
    }
    
    public boolean verificarPermutacion(Candidato[] original, Candidato[] ordenado, int atributoIndex) {
        if (original.length != ordenado.length) {
            return false;
        }
        
        // Crear mapas de frecuencia para cada valor del atributo
        java.util.Map<Integer, Integer> frecuenciaOriginal = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> frecuenciaOrdenado = new java.util.HashMap<>();
        
        for (Candidato c : original) {
            int valor = c.getAtributo(atributoIndex);
            frecuenciaOriginal.put(valor, frecuenciaOriginal.getOrDefault(valor, 0) + 1);
        }
        
        for (Candidato c : ordenado) {
            int valor = c.getAtributo(atributoIndex);
            frecuenciaOrdenado.put(valor, frecuenciaOrdenado.getOrDefault(valor, 0) + 1);
        }
        
        return frecuenciaOriginal.equals(frecuenciaOrdenado);
    }

    public boolean validacionCompleta(Candidato[] original, Candidato[] ordenado, 
                                    Metricas metricas, String algoritmo, int atributoIndex) {
        boolean ordenValido = verificarOrdenCorrecto(ordenado, atributoIndex);
        boolean metricasValidas = verificarConsistenciaMetricas(metricas, algoritmo);
        boolean permutacionValida = verificarPermutacion(original, ordenado, atributoIndex);
        
        if (!ordenValido) {
            System.err.println("FALLO: Arreglo no está correctamente ordenado");
        }
        if (!metricasValidas) {
            System.err.println("FALLO: Métricas inconsistentes");
        }
        if (!permutacionValida) {
            System.err.println("FALLO: No es una permutación válida del arreglo original");
        }
        
        return ordenValido && metricasValidas && permutacionValida;
    }
}
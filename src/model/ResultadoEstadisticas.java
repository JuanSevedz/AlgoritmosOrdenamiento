package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Almacena las estadísticas robustas calculadas a partir de múltiples repeticiones
 * 
 */
public class ResultadoEstadisticas {
    private double medianaComparaciones;
    private double ricComparaciones;
    private double medianaIntercambios;
    private double ricIntercambios;
    private double medianaTiempo;
    private double ricTiempo;
    
    public ResultadoEstadisticas(List<Metricas> metricas) {
        if (metricas == null || metricas.isEmpty()) {
            throw new IllegalArgumentException("La lista de métricas no puede ser nula o vacía");
        }
        
        calcularEstadisticas(metricas);
    }

    private void calcularEstadisticas(List<Metricas> metricas) {
        List<Long> comparaciones = new ArrayList<>();
        List<Long> intercambios = new ArrayList<>();
        List<Long> tiempos = new ArrayList<>();
        
        for (Metricas m : metricas) {
            comparaciones.add(m.getComparaciones());
            intercambios.add(m.getIntercambios());
            tiempos.add(m.getTiempoEjecucion());
        }
        
        this.medianaComparaciones = calcularMediana(comparaciones);
        this.ricComparaciones = calcularRangoIntercuartilico(comparaciones);
        
        this.medianaIntercambios = calcularMediana(intercambios);
        this.ricIntercambios = calcularRangoIntercuartilico(intercambios);
        
        this.medianaTiempo = calcularMediana(tiempos);
        this.ricTiempo = calcularRangoIntercuartilico(tiempos);
    }

    private double calcularMediana(List<Long> valores) {
        List<Long> sortedValues = new ArrayList<>(valores);
        Collections.sort(sortedValues);
        
        int n = sortedValues.size();
        if (n % 2 == 0) {
            return (sortedValues.get(n/2 - 1) + sortedValues.get(n/2)) / 2.0;
        } else {
            return sortedValues.get(n/2);
        }
    }

    private double calcularRangoIntercuartilico(List<Long> valores) {
        List<Long> sortedValues = new ArrayList<>(valores);
        Collections.sort(sortedValues);
        
        double q1 = calcularCuartil(sortedValues, 1);
        double q3 = calcularCuartil(sortedValues, 3);
        
        return q3 - q1;
    }
    
    private double calcularCuartil(List<Long> sortedValues, int quartile) {
        int n = sortedValues.size();
        double index;
        
        if (quartile == 1) {
            index = (n - 1) * 0.25;
        } else if (quartile == 3) {
            index = (n - 1) * 0.75;
        } else {
            throw new IllegalArgumentException("Solo se soportan cuartiles 1 y 3");
        }
        
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);
        
        if (lowerIndex == upperIndex) {
            return sortedValues.get(lowerIndex);
        } else {
            double weight = index - lowerIndex;
            return sortedValues.get(lowerIndex) * (1 - weight) + 
                   sortedValues.get(upperIndex) * weight;
        }
    }
    
    // Getters
    public double getMedianaComparaciones() {
        return medianaComparaciones;
    }
    
    public double getRicComparaciones() {
        return ricComparaciones;
    }
    
    public double getMedianaIntercambios() {
        return medianaIntercambios;
    }
    
    public double getRicIntercambios() {
        return ricIntercambios;
    }
    
    public double getMedianaTiempo() {
        return medianaTiempo;
    }
    
    public double getRicTiempo() {
        return ricTiempo;
    }

    public double getMedianaTiempoMs() {
        return medianaTiempo / 1_000_000.0;
    }

    public double getRicTiempoMs() {
        return ricTiempo / 1_000_000.0;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Estadísticas{" +
            "comp: med=%.0f, ric=%.0f; " +
            "int: med=%.0f, ric=%.0f; " +
            "tiempo: med=%.3fms, ric=%.3fms}",
            medianaComparaciones, ricComparaciones,
            medianaIntercambios, ricIntercambios,
            getMedianaTiempoMs(), getRicTiempoMs()
        );
    }

    public String toStringDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estadísticas Detalladas:\n");
        sb.append(String.format("  Comparaciones: mediana=%.0f, RIC=%.0f\n", 
                               medianaComparaciones, ricComparaciones));
        sb.append(String.format("  Intercambios:  mediana=%.0f, RIC=%.0f\n", 
                               medianaIntercambios, ricIntercambios));
        sb.append(String.format("  Tiempo:        mediana=%.3fms, RIC=%.3fms\n", 
                               getMedianaTiempoMs(), getRicTiempoMs()));
        return sb.toString();
    }
}
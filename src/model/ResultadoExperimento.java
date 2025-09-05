package model;

public class ResultadoExperimento {
    private String algoritmo;
    private int atributoIndex;
    private String distribucion;
    private int tamaño;
    private ResultadoEstadisticas estadisticas;
    
    public ResultadoExperimento(String algoritmo, int atributoIndex, String distribucion, 
                               int tamaño, ResultadoEstadisticas estadisticas) {
        this.algoritmo = algoritmo;
        this.atributoIndex = atributoIndex;
        this.distribucion = distribucion;
        this.tamaño = tamaño;
        this.estadisticas = estadisticas;
    }

    public String toCSV() {
        return String.format("%s,%d,%s,%d,%.0f,%.0f,%.0f,%.0f,%.6f,%.6f",
            algoritmo,
            atributoIndex,
            distribucion,
            tamaño,
            estadisticas.getMedianaComparaciones(),
            estadisticas.getRicComparaciones(),
            estadisticas.getMedianaIntercambios(),
            estadisticas.getRicIntercambios(),
            estadisticas.getMedianaTiempoMs(),
            estadisticas.getRicTiempoMs()
        );
    }
    
    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append(String.format("  \"algoritmo\": \"%s\",\n", algoritmo));
        json.append(String.format("  \"atributoIndex\": %d,\n", atributoIndex));
        json.append(String.format("  \"distribucion\": \"%s\",\n", distribucion));
        json.append(String.format("  \"tamaño\": %d,\n", tamaño));
        json.append("  \"estadisticas\": {\n");
        json.append(String.format("    \"comparaciones\": {\n"));
        json.append(String.format("      \"mediana\": %.0f,\n", estadisticas.getMedianaComparaciones()));
        json.append(String.format("      \"ric\": %.0f\n", estadisticas.getRicComparaciones()));
        json.append("    },\n");
        json.append(String.format("    \"intercambios\": {\n"));
        json.append(String.format("      \"mediana\": %.0f,\n", estadisticas.getMedianaIntercambios()));
        json.append(String.format("      \"ric\": %.0f\n", estadisticas.getRicIntercambios()));
        json.append("    },\n");
        json.append(String.format("    \"tiempo_ms\": {\n"));
        json.append(String.format("      \"mediana\": %.6f,\n", estadisticas.getMedianaTiempoMs()));
        json.append(String.format("      \"ric\": %.6f\n", estadisticas.getRicTiempoMs()));
        json.append("    }\n");
        json.append("  }\n");
        json.append("}");
        return json.toString();
    }

    public static String getCSVHeader() {
        return "algoritmo,atributoIndex,distribucion,tamaño," +
               "mediana_comparaciones,ric_comparaciones," +
               "mediana_intercambios,ric_intercambios," +
               "mediana_tiempo_ms,ric_tiempo_ms";
    }
    
    // Getters
    public String getAlgoritmo() {
        return algoritmo;
    }
    
    public int getAtributoIndex() {
        return atributoIndex;
    }
    
    public String getDistribucion() {
        return distribucion;
    }
    
    public int getTamaño() {
        return tamaño;
    }
    
    public ResultadoEstadisticas getEstadisticas() {
        return estadisticas;
    }
    
    public String getNombreAtributo() {
        String[] nombres = {
            "Distancia_Marchas",
            "Horas_Perdidas", 
            "Prebendas_Sindicales",
            "Sobornos_Politicos",
            "Actos_Corrupcion"
        };
        
        if (atributoIndex >= 0 && atributoIndex < nombres.length) {
            return nombres[atributoIndex];
        }
        return "Atributo_" + atributoIndex;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s - %s (n=%d):\n%s", 
                           algoritmo, getNombreAtributo(), distribucion, 
                           tamaño, estadisticas.toString());
    }
    
    public String toTable() {
        return String.format("%-12s %-18s %-15s %8d %12.0f %12.0f %10.3f", 
                           algoritmo, 
                           getNombreAtributo(), 
                           distribucion, 
                           tamaño,
                           estadisticas.getMedianaComparaciones(),
                           estadisticas.getMedianaIntercambios(),
                           estadisticas.getMedianaTiempoMs());
    }
    
    public static String getTableHeader() {
        return String.format("%-12s %-18s %-15s %8s %12s %12s %10s", 
                           "Algoritmo", "Atributo", "Distribución", "Tamaño",
                           "Med_Comp", "Med_Int", "Tiempo(ms)");
    }

    public static String getTableSeparator() {
        return "─".repeat(95);
    }
}
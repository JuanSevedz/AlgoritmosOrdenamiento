package util;

import model.ResultadoExperimento;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Exportador {

    public void exportarCSV(List<ResultadoExperimento> resultados, String filename) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Encabezado CSV
            writer.println("algoritmo,caracteristica,distribucion,tamaño," +
                          "mediana_comparaciones,ric_comparaciones," +
                          "mediana_intercambios,ric_intercambios," +
                          "mediana_tiempo_ms,ric_tiempo_ms");

            // Datos
            for (ResultadoExperimento resultado : resultados) {
                writer.println(resultado.toCSV());
            }

            System.out.println("Archivo CSV: " + filename);

        } catch (IOException e) {
            System.err.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    public void exportarJSON(List<ResultadoExperimento> resultados, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("{");
            writer.println("  \"resultados\": [");

            for (int i = 0; i < resultados.size(); i++) {
                writer.print("    " + resultados.get(i).toJSON());
                if (i < resultados.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }

            writer.println("  ]");
            writer.println("}");

            System.out.println("Archivo JSON generado: " + filename);

        } catch (IOException e) {
            System.err.println("Error al exportar JSON: " + e.getMessage());
        }
    }

    public String generarReporteCompleto(List<ResultadoExperimento> resultados) {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE COMPLETO DE BENCHMARK ===\n\n");

        String[] algoritmos = {"Burbuja", "Insercion", "Seleccion", "Merge", "Quick"};
        String[] distribuciones = {"Aleatoria", "CasiOrdenada", "OrdenInverso"};

        for (String algoritmo : algoritmos) {
            reporte.append("ALGORITMO: ").append(algoritmo).append("\n");
            reporte.append("=".repeat(50)).append("\n");

            for (String distribucion : distribuciones) {
                reporte.append("Distribución: ").append(distribucion).append("\n");
                reporte.append("-".repeat(30)).append("\n");

                for (int atributo = 0; atributo < 5; atributo++) {
                    ResultadoExperimento resultado = encontrarResultado(
                        resultados, algoritmo, distribucion, atributo
                    );

                    if (resultado != null) {
                        reporte.append(String.format("Atributo %d: %,d comp | %,d interc | %.2f ms%n",
                            atributo,
                            (long) resultado.getEstadisticas().getMedianaComparaciones(),
                            (long) resultado.getEstadisticas().getMedianaIntercambios(),
                            resultado.getEstadisticas().getMedianaTiempo()));
                    }
                }
                reporte.append("\n");
            }
            reporte.append("\n");
        }

        return reporte.toString();
    }

    private ResultadoExperimento encontrarResultado(List<ResultadoExperimento> resultados, 
                                                   String algoritmo, String distribucion, int atributo) {
        for (ResultadoExperimento resultado : resultados) {
            if (resultado.getAlgoritmo().equals(algoritmo) &&
                resultado.getDistribucion().equals(distribucion) &&
                resultado.getAtributoIndex() == atributo) {
                return resultado;
            }
        }
        return null;
    }
}
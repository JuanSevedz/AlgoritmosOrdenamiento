package model;

import java.util.ArrayList;
import java.util.List;

import util.Validador;
import util.ManejoMemoria;

public class Benchmark {
    private int kRepeticiones;
    private long semilla;
    private GeneradorData generadorData;
    private AlgoritmosOrdenamiento algoritmosOrdenamiento;
    private Validador validador;
    private ManejoMemoria manejoMemoria;

    public Benchmark(int kRepeticiones, long semilla) {
        this.kRepeticiones = kRepeticiones;
        this.semilla = semilla;
        this.generadorData = new GeneradorData(semilla);
        this.algoritmosOrdenamiento = new AlgoritmosOrdenamiento();
        this.validador = new Validador();
        this.manejoMemoria = new ManejoMemoria();
    }

    public List<ResultadoExperimento> ejecutarPruebasCompletas(int n, int m) {
        if (!manejoMemoria.esFactible(n, m)) {
            throw new RuntimeException("No hay memoria suficiente para ejecutar con n=" + n + " y m=" + m);
        }

        List<ResultadoExperimento> resultados = new ArrayList<>();
        String[] distribuciones = {"Aleatoria", "CasiOrdenada", "OrdenInverso"};
        String[] algoritmos = {"Burbuja", "Insercion", "Merge", "Seleccion", "Quick"};

        for (String distribucion : distribuciones) {
            for (int atributoIndex = 0; atributoIndex < 5; atributoIndex++) {
                for (String algoritmo : algoritmos) {
                    ResultadoExperimento resultado = ejecutarPruebaAlgoritmo(
                        algoritmo, atributoIndex, distribucion, n, m
                    );
                    resultados.add(resultado);
                }
            }
        }
        return resultados;
    }

    public ResultadoExperimento ejecutarPruebaAlgoritmo(String algoritmo, int atributoIndex, 
                                                      String distribucion, int n, int m) {
        if (!manejoMemoria.esFactible(n, m)) {
            throw new RuntimeException("No hay memoria suficiente para ejecutar con n=" + n + " y m=" + m);
        }

        List<Metricas> metricasRepeticiones = new ArrayList<>();

        for (int i = 0; i < kRepeticiones; i++) {
            Candidato[] datos = generarDatosPorDistribucion(n, m, distribucion);
            Candidato[] datosCopia = copiarDatos(datos);

            Metricas metricas = ejecutarAlgoritmo(algoritmo, datosCopia, atributoIndex);

            if (!validador.verificarOrdenCorrecto(datosCopia, atributoIndex)) {
                throw new RuntimeException("Error: Ordenamiento incorrecto en " + algoritmo);
            }

            metricasRepeticiones.add(metricas);
        }

        ResultadoEstadisticas estadisticas = new ResultadoEstadisticas(metricasRepeticiones);

        return new ResultadoExperimento(algoritmo, atributoIndex, distribucion, n, estadisticas);
    }

    private Candidato[] generarDatosPorDistribucion(int n, int m, String distribucion) {
        switch (distribucion) {
            case "Aleatoria": return generadorData.generarDatosAleatorios(n, m);
            case "CasiOrdenada": return generadorData.generarDatosCasiOrdenados(n, m);
            case "OrdenInverso": return generadorData.generarDatosOrdenInverso(n, m);
            default: throw new IllegalArgumentException("Distribución no válida: " + distribucion);
        }
    }

    private Metricas ejecutarAlgoritmo(String algoritmo, Candidato[] datos, int atributoIndex) {
        switch (algoritmo) {
            case "Burbuja": return algoritmosOrdenamiento.bubbleSort(datos, atributoIndex);
            case "Insercion": return algoritmosOrdenamiento.insertionSort(datos, atributoIndex);
            case "Seleccion": return algoritmosOrdenamiento.selectionSort(datos, atributoIndex);
            case "Merge": return algoritmosOrdenamiento.mergeSort(datos, atributoIndex);
            case "Quick": return algoritmosOrdenamiento.quickSort(datos, atributoIndex);
            default: throw new IllegalArgumentException("Algoritmo no válido: " + algoritmo);
        }
    }

    private Candidato[] copiarDatos(Candidato[] original) {
        Candidato[] copia = new Candidato[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }

    public void setSemilla(long semilla) {
        this.semilla = semilla;
        this.generadorData.setSemilla(semilla);
    }
}

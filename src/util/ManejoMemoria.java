package util;

/**
 * Utilidad para gestionar y verificar el uso de memoria durante los
 * experimentos,
 * especialmente importante para datasets grandes donde M > 10,000,000.
 */
public class ManejoMemoria {

    // Constantes para cálculos de memoria
    private static final int BYTES_PER_INT = 4;
    private static final int BYTES_PER_OBJECT_OVERHEAD = 16; // Overhead típico de objeto en JVM
    private static final int BYTES_PER_ARRAY_OVERHEAD = 24; // Overhead de array
    private static final double FACTOR_SEGURIDAD = 1.5; // Factor de seguridad para cálculos

    /**
     * Configura el heap de la JVM (solo informativo, no puede cambiar en runtime)
     * 
     * @param gigas tamaño deseado en gigabytes
     */
    public void configurarHeap(int gigas) {
        long heapMaxima = Runtime.getRuntime().maxMemory();
        long gigasDisponibles = heapMaxima / (1024 * 1024 * 1024);

        System.out.printf("Heap máximo configurado: %d GB\n", gigasDisponibles);
        System.out.printf("Heap solicitado: %d GB\n", gigas);

        if (gigasDisponibles < gigas) {
            System.err.printf("ADVERTENCIA: Heap disponible (%d GB) menor al solicitado (%d GB)\n",
                    gigasDisponibles, gigas);
            System.err.println("Para configurar más heap, usar: java -Xmx" + gigas + "g YourMainClass");
        }
    }

    public boolean esFactible(int n, int m) {
        long memoriaRequerida = calcularMemoriaRequerida(n);
        long memoriaDisponible = getMemoriaDisponible();

        System.out.printf("Memoria requerida: %.2f GB\n", memoriaRequerida / (1024.0 * 1024 * 1024));
        System.out.printf("Memoria disponible: %.2f GB\n", memoriaDisponible / (1024.0 * 1024 * 1024));

        boolean factible = memoriaRequerida < memoriaDisponible;

        if (!factible) {
            System.err.println("ERROR: Memoria insuficiente para el dataset solicitado");
            System.err.println("Sugerencias:");
            System.err.println("1. Reducir el valor de N");
            System.err.println("2. Aumentar heap: java -Xmx8g (o más) YourMainClass");
            System.err.println("3. Ejecutar en una máquina con más RAM");
        }

        return factible;
    }

    // Calcula la memoria aproximada requerida para un dataset

    private long calcularMemoriaRequerida(int n) {
        // Memoria para un Candidato
        long memoriaUnCandidato = BYTES_PER_INT + // id
                (5 * BYTES_PER_INT + BYTES_PER_ARRAY_OVERHEAD) + // atributos
                BYTES_PER_OBJECT_OVERHEAD; // overhead objeto
        // Memoria para el arreglo de candidatos
        long memoriaArreglo = (long) n * 8 + BYTES_PER_ARRAY_OVERHEAD; // referencias + overhead

        // Memoria total para los datos
        long memoriaData = ((long) n * memoriaUnCandidato) + memoriaArreglo;

        // Memoria adicional para operaciones (copias, algoritmos, etc.)
        // Los algoritmos pueden necesitar copias del arreglo
        long memoriaOperaciones = memoriaData * 3; // Hasta 3 copias simultáneas

        // Aplicar factor de seguridad
        long memoriaTotal = (long) ((memoriaData + memoriaOperaciones) * FACTOR_SEGURIDAD);

        return memoriaTotal;
    }

    // Obtiene la memoria disponible en bytes
    public long getMemoriaDisponible() {
        Runtime runtime = Runtime.getRuntime();
        long memoriaMaxima = runtime.maxMemory(); // Heap máximo
        long memoriaUsada = runtime.totalMemory() - runtime.freeMemory(); // Memoria actualmente en uso

        return memoriaMaxima - memoriaUsada;
    }

    // Imprime un reporte detallado del uso de memoria
    public void imprimirReporteMemoria() {
        Runtime runtime = Runtime.getRuntime();

        long maxMemoria = runtime.maxMemory();
        long totalMemoria = runtime.totalMemory();
        long memoriaLibre = runtime.freeMemory();
        long memoriaUsada = totalMemoria - memoriaLibre;
        long memoriaDisponible = maxMemoria - memoriaUsada;

        System.out.println("\n=== REPORTE DE MEMORIA ===");
        System.out.printf("Heap máximo:      %8.2f GB\n", maxMemoria / (1024.0 * 1024 * 1024));
        System.out.printf("Heap total:       %8.2f GB\n", totalMemoria / (1024.0 * 1024 * 1024));
        System.out.printf("Memoria usada:    %8.2f GB\n", memoriaUsada / (1024.0 * 1024 * 1024));
        System.out.printf("Memoria libre:    %8.2f GB\n", memoriaLibre / (1024.0 * 1024 * 1024));
        System.out.printf("Memoria disponible: %6.2f GB\n", memoriaDisponible / (1024.0 * 1024 * 1024));
        System.out.println("========================\n");
    }

    public int sugerirTamañoMaximo() {
        long memoriaDisponible = getMemoriaDisponible();

        // Usar el 70% de la memoria disponible para ser conservadores
        long memoriaUsable = (long) (memoriaDisponible * 0.7);

        // Memoria aproximada por candidato (incluyendo operaciones)
        long memoriaPorCandidato = 64 * 4; // 64 bytes base * 4

        int nMaximo = (int) (memoriaUsable / memoriaPorCandidato);
        nMaximo = (nMaximo / 1000) * 1000;

        return Math.max(1000, nMaximo); // Minimo 1000
    }

    public void limpiarMemoria() {
        long memoriaAntes = getMemoriaUsada();

        System.out.println("Ejecutando garbage collection...");
        System.gc();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long memoriaDespues = getMemoriaUsada();
        long memoriaLiberada = memoriaAntes - memoriaDespues;

        System.out.printf("Memoria liberada: %.2f MB\n", memoriaLiberada / (1024.0 * 1024));
    }

    private long getMemoriaUsada() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public boolean isMemoriaCritica() {
        long disponible = getMemoriaDisponible();
        long maxima = Runtime.getRuntime().maxMemory();

        double porcentajeDisponible = (double) disponible / maxima;

        return porcentajeDisponible < 0.1; // Menos del 10% disponible
    }
}
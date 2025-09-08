package terminal;

import model.Benchmark;
import model.ResultadoExperimento;
import model.GeneradorData;
import model.Candidato;
import util.Exportador;

import java.util.List;
import java.util.Scanner;

public class TerminalUI {
    private Configuracion config;
    private Benchmark benchmark;
    private Exportador exportador;
    private Scanner scanner;

    public TerminalUI() {
        this.config = new Configuracion();
        this.scanner = new Scanner(System.in);
        this.exportador = new Exportador();
    }

    public void iniciar() {
        System.out.println("=== ASO - SIN SIGLA: Benchmark de Algoritmos de Ordenamiento ===");
        
        while (true) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1: solicitarParametros(); break;
                case 2: ejecutarBenchmark(); break;
                case 3: exportarResultados(); break;
                case 4: mostrarInfoEntorno(); break;
                case 5: 
                anunciarGanador(); break;
            case 6:
                System.out.println("Hasta pronto!");
                return;
                default: 
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Configurar parámetros");
        System.out.println("2. Ejecutar benchmark");
        System.out.println("3. Exportar resultados");
        System.out.println("4. Mostrar información del entorno");
        System.out.println("5. Anunciar candidato ganador");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public void solicitarParametros() {
        System.out.println("\n=== CONFIGURACIÓN DE PARÁMETROS ===");
        
        System.out.print("Número de candidatos (n): ");
        config.setN(scanner.nextInt());
        
        System.out.print("Valor máximo (m): ");
        config.setM(scanner.nextInt());
        
        System.out.print("Número de repeticiones (k): ");
        config.setK(scanner.nextInt());
        
        System.out.print("Semilla (0 para aleatoria): ");
        long semilla = scanner.nextLong();
        config.setSemilla(semilla == 0 ? System.currentTimeMillis() : semilla);

        System.out.print("Tamaño de heap en GB (0 para por defecto): ");
        config.setGigasHeap(scanner.nextInt());
        
        System.out.println("✓ Parámetros configurados correctamente");
    }

    public void ejecutarBenchmark() {
        if (!config.validar()) {
            System.out.println("Error: Parámetros no válidos");
            return;
        }

        System.out.println("\n=== EJECUTANDO BENCHMARK ===");
        System.out.println("Configuración: " + config.toString());
        
        try {
            this.benchmark = new Benchmark(config.getK(), config.getSemilla());
            mostrarProgreso(0, "Iniciando benchmark...");

            if (config.getGigasHeap() > 0) {
                System.out.println("Configurando heap a " + config.getGigasHeap() + " GB");
            }
            
            List<ResultadoExperimento> resultados = benchmark.ejecutarPruebasCompletas(
                config.getN(), config.getM()
            );
            
            mostrarProgreso(100, "Benchmark completado!");
            mostrarResultados(resultados);
            
        } catch (RuntimeException e) {
            System.err.println("Error de memoria: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarResultados(List<ResultadoExperimento> resultados) {
        System.out.println("\n=== RESULTADOS ===");
        System.out.printf("%-12s %-15s %-12s %-8s %-15s %-15s %-12s%n",
                "Algoritmo", "Distribución", "Atributo", "Tamaño", 
                "Comp. Median", "Interc. Median", "Tiempo(ms)");
        
        for (ResultadoExperimento resultado : resultados) {
            System.out.printf("%-12s %-15s %-12d %-8d %-15.0f %-15.0f %-12.2f%n",
                    resultado.getAlgoritmo(),
                    resultado.getDistribucion(),
                    resultado.getAtributoIndex(),
                    resultado.getTamaño(),
                    resultado.getEstadisticas().getMedianaComparaciones(),
                    resultado.getEstadisticas().getMedianaIntercambios(),
                    resultado.getEstadisticas().getMedianaTiempo());
        }
    }

    public void exportarResultados() {
        if (benchmark == null) {
            System.out.println("Error: Primero debe ejecutar el benchmark");
            return;
        }

        System.out.println("\n=== EXPORTANDO RESULTADOS ===");
        
        try {
            List<ResultadoExperimento> resultados = benchmark.ejecutarPruebasCompletas(
                config.getN(), config.getM()
            );

            System.out.print("Seleccione formato de exportación (CSV/JSON): ");
            String formato = scanner.next();

            if (formato.equalsIgnoreCase("CSV")) {
                exportador.exportarCSV(resultados, "resultados_benchmark.csv");
                System.out.println("✓ Resultados exportados a resultados_benchmark.csv");
            } else if (formato.equalsIgnoreCase("JSON")) {
                exportador.exportarJSON(resultados, "resultados_benchmark.json");
                System.out.println("✓ Resultados exportados a resultados_benchmark.json");
            } else {
                System.out.println("Error: Formato no válido. Use CSV o JSON");
            }
            
        } catch (RuntimeException e) {
            System.err.println("Error de memoria: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante la exportación: " + e.getMessage());
        }
    }

    public void mostrarProgreso(int porcentaje, String mensaje) {
        System.out.printf("[%d%%] %s%n", porcentaje, mensaje);
    }

    private void mostrarInfoEntorno() {
        System.out.println("\n=== INFORMACIÓN DEL ENTORNO ===");
        System.out.println("JDK: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("Memoria máxima: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        System.out.println("Memoria libre: " + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB");
    }

    private int leerOpcion() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.next();
            return -1;
        }
    }


    private void anunciarGanador() {
        // Validar configuración actual
        if (config == null || !config.validar()) {
            System.out.println("Error: Parámetros no válidos. Configure los parámetros primero (opción 1).");
            return;
        }

        System.out.println("\n=== ANUNCIANDO GANADOR ===");
        // Generar datos según configuración actual (distribución uniforme por defecto)
        GeneradorData generador = new GeneradorData(config.getSemilla());
        Candidato[] candidatos = generador.generarDatosAleatorios(config.getN(), config.getM());

        if (candidatos == null || candidatos.length == 0) {
            System.out.println("No hay candidatos generados.");
            return;
        }

        // Encontrar el candidato con mayor suma de atributos (peor candidato)
        int sumaMaxima = Integer.MIN_VALUE;
        Candidato peor = null;
        for (Candidato c : candidatos) {
            int suma = 0;
            // asumiendo 5 atributos
            for (int i = 0; i < 5; i++) {
                suma += c.getAtributo(i);
            }
            if (suma > sumaMaxima) {
                sumaMaxima = suma;
                peor = c;
            }
        }

        if (peor != null) {
            System.out.println("\\n=== GANADOR (Peor candidato) ===");
            System.out.println("ID: " + peor.getId());
            System.out.println("Distancia en marchas: " + peor.getAtributo(0));
            System.out.println("Horas de clase perdidas: " + peor.getAtributo(1));
            System.out.println("Prebendas sindicales: " + peor.getAtributo(2));
            System.out.println("Número de políticos sobornados: " + peor.getAtributo(3));
            System.out.println("Valor total de corrupción: " + peor.getAtributo(4));
            System.out.println("Suma total de atributos (indicador): " + sumaMaxima);
        } else {
            System.out.println("No fue posible determinar un ganador.");
        }
    }
}

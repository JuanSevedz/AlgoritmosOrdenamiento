package model;

public class Metricas {
    private long comparaciones;
    private long intercambios;
    private long tiempoEjecucion;
    private long inicioTiempo;

    public Metricas() {
        this.comparaciones = 0;
        this.intercambios = 0;
        this.tiempoEjecucion = 0;
    }

    public void incrementarComparaciones() {
        this.comparaciones++;
    }
    public void incrementarIntercambios() {
        this.intercambios++;
    }
    public void iniciarTiempo() {
        this.inicioTiempo = System.nanoTime();
    }
    public void finalizarTiempo() {
        this.tiempoEjecucion = System.nanoTime() - this.inicioTiempo;
    }
    public long getComparaciones() {
        return comparaciones;
    }
    public long getIntercambios() {
        return intercambios;
    }
    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }
    public double getTiempoEjecucionMS() {
        return tiempoEjecucion / 1_000_000_000.0;
    }
    public double getTiempoEjecucionMicros(){
        return tiempoEjecucion / 1_000.0;
    }

    @Override
    public String toString() {
        return String.format("Metricas{comparaciones=%d, intercambios=%d, tiempoEjecucion=%d ns (%.6f ms, %.3f µs)}",
                comparaciones, intercambios, getTiempoEjecucionMS());
    }

    public Metricas copy(){
        Metricas copia = new Metricas();
        copia.comparaciones = this.comparaciones;
        copia.intercambios = this.intercambios;
        copia.tiempoEjecucion = this.tiempoEjecucion;
        return copia;
    }
}

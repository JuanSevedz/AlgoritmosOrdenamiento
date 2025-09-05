package terminal;

import java.util.Arrays;

public class Configuracion {
    private int n;
    private int m;
    private int k;
    private long semilla;
    private String[] distribuciones;
    private int[] atributosSeleccionados;
    private String formatoExportacion;
    private int gigasHeap;

    public Configuracion() {
        this.n = 1000;
        this.m = 100000;
        this.k = 5;
        this.semilla = System.currentTimeMillis();
        this.distribuciones = new String[]{"Aleatoria", "CasiOrdenada", "OrdenInverso"};
        this.atributosSeleccionados = new int[]{0, 1, 2, 3, 4};
        this.formatoExportacion = "CSV";
        this.gigasHeap = 0;
    }

    public boolean validar() {
        if (n <= 0) {
            System.err.println("Error: n debe ser mayor que 0");
            return false;
        }
        if (m <= 0 || m < n) {
            System.err.println("Error: m debe ser mayor que 0 y mayor o igual que n");
            return false;
        }
        if (k <= 0) {
            System.err.println("Error: k debe ser mayor que 0");
            return false;
        }
        if (distribuciones == null || distribuciones.length == 0) {
            System.err.println("Error: Debe seleccionar al menos una distribución");
            return false;
        }
        if (gigasHeap < 0) {
            System.err.println("Error: el tamaño de heap no puede ser negativo");
            return false;
        }
        return true;
    }

    public int getN() { return n; }
    public void setN(int n) { this.n = n; }

    public int getM() { return m; }
    public void setM(int m) { this.m = m; }

    public int getK() { return k; }
    public void setK(int k) { this.k = k; }

    public long getSemilla() { return semilla; }
    public void setSemilla(long semilla) { this.semilla = semilla; }

    public String[] getDistribuciones() { return distribuciones; }
    public void setDistribuciones(String[] distribuciones) { this.distribuciones = distribuciones; }

    public int[] getAtributosSeleccionados() { return atributosSeleccionados; }
    public void setAtributosSeleccionados(int[] atributosSeleccionados) {
        this.atributosSeleccionados = atributosSeleccionados;
    }

    public String getFormatoExportacion() { return formatoExportacion; }
    public void setFormatoExportacion(String formatoExportacion) {
        this.formatoExportacion = formatoExportacion.toUpperCase();
    }

    public int getGigasHeap() { return gigasHeap; }
    public void setGigasHeap(int gigasHeap) { this.gigasHeap = gigasHeap; }

    @Override
    public String toString() {
        return "Configuracion{" +
                "n=" + n +
                ", m=" + m +
                ", k=" + k +
                ", semilla=" + semilla +
                ", distribuciones=" + Arrays.toString(distribuciones) +
                ", atributosSeleccionados=" + Arrays.toString(atributosSeleccionados) +
                ", formatoExportacion='" + formatoExportacion + '\'' +
                ", gigasHeap=" + gigasHeap +
                '}';
    }
}

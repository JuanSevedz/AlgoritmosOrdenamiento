package model;

import util.Validador;

public class AlgoritmosOrdenamiento {
    private Validador validador;

    public AlgoritmosOrdenamiento() {
        this.validador = new Validador();
    }

    public Metricas bubbleSort(Candidato[] arr, int attrIndex) {
        Metricas metricas = new Metricas();
        metricas.iniciarTiempo();

        int n = arr.length;
        boolean intercambiado;

        for (int i = 0; i < n - 1; i++) {
            intercambiado = false;
            for (int j = 0; j < n - i - 1; j++) {
                metricas.incrementarComparaciones();
                if (arr[j].getAtributo(attrIndex) > arr[j + 1].getAtributo(attrIndex)) {
                    // Intercambiar elementos
                    intercambiar(arr, j, j + 1);
                    metricas.incrementarIntercambios();
                    intercambiado = true;
                }
            }

            if (!intercambiado)
                break;
        }

        metricas.finalizarTiempo();
        if (!validador.verificarOrdenCorrecto(arr, attrIndex)) {
            System.err.println("ERROR: Bubble Sort no ordenó correctamente");
        }
        return metricas;
    }

    public Metricas insertionSort(Candidato[] arr, int attrIndex) {
        Metricas metricas = new Metricas();
        metricas.iniciarTiempo();

        for (int i = 1; i < arr.length; i++) {
            Candidato key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                metricas.incrementarComparaciones();
                if (arr[j].getAtributo(attrIndex) > key.getAtributo(attrIndex)) {
                    arr[j + 1] = arr[j];
                    metricas.incrementarIntercambios();
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
        metricas.finalizarTiempo();
        if (!validador.verificarOrdenCorrecto(arr, attrIndex)) {
            System.err.println("ERROR: Insertion Sort no ordenó correctamente");
        }
        return metricas;
    }

    public Metricas selectionSort(Candidato[] arr, int attrIndex) {
        Metricas metricas = new Metricas();
        metricas.iniciarTiempo();

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                metricas.incrementarComparaciones();
                if (arr[j].getAtributo(attrIndex) < arr[minIndex].getAtributo(attrIndex)) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                intercambiar(arr, i, minIndex);
                metricas.incrementarIntercambios();
            }
        }
        metricas.finalizarTiempo();
        if (!validador.verificarOrdenCorrecto(arr, attrIndex)) {
            System.err.println("ERROR: Selection Sort no ordenó correctamente");
        }
        return metricas;
    }

    public Metricas mergeSort(Candidato[] arr, int attrIndex) {
        Metricas metricas = new Metricas();
        metricas.iniciarTiempo();

        mergeSortRecursivo(arr, 0, arr.length - 1, attrIndex, metricas);

        metricas.finalizarTiempo();

        if (!validador.verificarOrdenCorrecto(arr, attrIndex)) {
            System.err.println("ERROR: Merge Sort no ordenó correctamente");
        }

        return metricas;
    }

    private void mergeSortRecursivo(Candidato[] arr, int izq, int der, int attrIndex, Metricas metricas) {
        if (izq < der) {
            int medio = izq + (der - izq) / 2;

            mergeSortRecursivo(arr, izq, medio, attrIndex, metricas);
            mergeSortRecursivo(arr, medio + 1, der, attrIndex, metricas);

            merge(arr, izq, medio, der, attrIndex, metricas);
        }
    }

    private void merge(Candidato[] arr, int izq, int medio, int der, int attrIndex, Metricas metricas) {

        int n1 = medio - izq + 1;
        int n2 = der - medio;

        Candidato[] izqArr = new Candidato[n1];
        Candidato[] derArr = new Candidato[n2];

        System.arraycopy(arr, izq, izqArr, 0, n1);
        System.arraycopy(arr, medio + 1, derArr, 0, n2);

        int i = 0, j = 0, k = izq;

        while (i < n1 && j < n2) {
            metricas.incrementarComparaciones();
            if (izqArr[i].getAtributo(attrIndex) <= derArr[j].getAtributo(attrIndex)) {
                arr[k] = izqArr[i];
                i++;
            } else {
                arr[k] = derArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = izqArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = derArr[j];
            j++;
            k++;
        }
    }

    public Metricas quickSort(Candidato[] arr, int attrIndex) {
        Metricas metricas = new Metricas();
        metricas.iniciarTiempo();
        quickSortRecursivo(arr, 0, arr.length - 1, attrIndex, metricas);
        metricas.finalizarTiempo();
        if (!validador.verificarOrdenCorrecto(arr, attrIndex)) {
            System.err.println("ERROR: Quick Sort no ordenó correctamente");
        }
        return metricas;
    }

    private void quickSortRecursivo(Candidato[] arr, int bajo, int alto, int attrIndex, Metricas metricas) {
        if (bajo < alto) {
            int pi = particion(arr, bajo, alto, attrIndex, metricas);
            quickSortRecursivo(arr, bajo, pi - 1, attrIndex, metricas);
            quickSortRecursivo(arr, pi + 1, alto, attrIndex, metricas);
        }
    }

    private int particion(Candidato[] arr, int bajo, int alto, int attrIndex, Metricas metricas) {
        Candidato pivote = arr[alto];
        int i = bajo - 1;

        for (int j = bajo; j < alto; j++) {
            metricas.incrementarComparaciones();
            if (arr[j].getAtributo(attrIndex) <= pivote.getAtributo(attrIndex)) {
                i++;
                intercambiar(arr, i, j);
                metricas.incrementarIntercambios();
            }
        }
        intercambiar(arr, i + 1, alto);
        metricas.incrementarIntercambios();

        return i + 1;
    }

    private void intercambiar(Candidato[] arr, int i, int j) {
        Candidato temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static Candidato[] copiarArreglo(Candidato[] original) {
        Candidato[] copia = new Candidato[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = new Candidato(original[i].getId(), original[i].getAtributos());
        }
        return copia;
    }
}
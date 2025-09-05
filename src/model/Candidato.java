package model;

public class Candidato {
    private int id;
    private int[] atributos;

    public Candidato(int id) {
        this.id = id;
        this.atributos = new int[5];
    }

    public Candidato(int id, int[] atributos) {
        this.id = id;
        this.atributos = atributos.clone();
    }

    public int getAtributo(int index) {
        if (index < 0 || index >= atributos.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return atributos[index];
    }

    public void setAtributo(int index, int value) {
        if (index < 0 || index >= atributos.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        this.atributos[index] = value;
    }

    public int getId() {
        return id;
    }

    public int[] getAtributos() {
        return atributos.clone();
    }

    @Override
    public String toString() {
        return String.format("Candidato{id=%id, atributos=%s}", id, java.util.Arrays.toString(atributos));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Candidato candidato = (Candidato) obj;
        return id == candidato.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

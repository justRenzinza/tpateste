package app;

import java.util.ArrayList;

public class Disciplina {
    private int codigo;
    private String nome;
    private int cargaHoraria;
    private ArrayList<Disciplina> preRequisitos;

    public Disciplina(int codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<Disciplina>();
    }

    public int getCodigo() { return codigo; }

    public String getNome() { return nome; }

    public int getCargaHoraria() { return cargaHoraria; }

    public ArrayList<Disciplina> getPreRequisitos() { return preRequisitos; }

    public void addPreRequisito(Disciplina disciplina) { preRequisitos.add(disciplina); }

    @Override
    public String toString() {
        return "Código " + this.getCodigo() + ": " + this.getNome() + " - " + this.getCargaHoraria() + "h";
    }
}

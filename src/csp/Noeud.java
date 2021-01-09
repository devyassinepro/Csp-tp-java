package csp;


import java.util.ArrayList;

public class Noeud {

    private int idNoeud;
    private int valNoeud;
    public ArrayList<Integer> domaines;


    public Noeud() {
        this.domaines = new ArrayList<Integer>();
        this.idNoeud = 0;
        this.valNoeud = 0;
    }

    public Noeud(int idNoeud) {
        domaines = new ArrayList<Integer>();
        this.idNoeud = idNoeud;
        this.valNoeud = -1;
    }

    public Noeud(int idNoeud, ArrayList<Integer> domaines) {
        this.domaines = new ArrayList<Integer>();
        this.idNoeud = idNoeud;
        this.valNoeud = -1;
        this.setDomaines(domaines);
    }

    public void afficherDomaines(){
        for(int i = 0; i < domaines.size(); i++){
            System.out.print(domaines.get(i) + ", ");
        }
        System.out.println();
    }

    public int getIdNoeud() {
        return idNoeud;
    }


    public void setIdNoeud(int idNoeud) {
        this.idNoeud = idNoeud;
    }


    public int getValNoeud() {
        return valNoeud;
    }


    public void setValNoeud(int valNoeud) {
        this.valNoeud = valNoeud;
    }


    public ArrayList<Integer> getDomaine() {
        return domaines;
    }


    public void setDomaines(ArrayList<Integer> domaine) {
        this.domaines.addAll(domaine);
    }


    @Override
    public String toString() {
        return "Noeud [idNoeud=" + idNoeud + "]";
    }


}

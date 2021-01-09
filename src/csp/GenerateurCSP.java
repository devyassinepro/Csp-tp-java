package csp;


import java.util.ArrayList;
import java.util.Random;

public class GenerateurCSP {

    public ArrayList<Noeud> listeNoeuds;
    public ArrayList<Arc> listeArcs;
    public ArrayList<ArrayList<Arc>> listeAdjacenteEnEntree;
    public ArrayList<ArrayList<Arc>> listeAdjacenteEnSortie;
    public ArrayList<ArrayList<Integer>> listeDomaines;

    public GenerateurCSP () {
        listeNoeuds = new ArrayList<Noeud>();
        listeArcs = new ArrayList<Arc>();
        listeAdjacenteEnEntree = new ArrayList<ArrayList<Arc>>();
        listeAdjacenteEnSortie = new ArrayList<ArrayList<Arc>>();
        listeDomaines = new ArrayList<ArrayList<Integer>>();
    }

    public void genererCSP(int nbrNoeud, int tailleDomaine, double densite, double durete) {

        int nbrArc				  = 0;
        int nbrMaximumArc 		  = 0;
        int nbrContraintes 		  = 0;
        int nbrMaximumContraintes = 0;
        double densiteActuelle;
        double dureteActuelle;
        ArrayList<Integer> domaines = new ArrayList<Integer>();

        // Generation des noeuds et des domaines aleatoirement
        for(int i = 0; i < nbrNoeud; i++) {
            for(int j = 0; j < tailleDomaine; j++) {
                domaines.add(new Random().nextInt(100));
            }
            this.listeNoeuds.add(new Noeud(i, domaines));
            domaines.clear();
        }

        // Generation de l'ensemble des arcs
        for(int i = 0; i < this.listeNoeuds.size(); i++) {
            Noeud noeud = this.listeNoeuds.get(i);
            for(int j = 0; j < this.listeNoeuds.size(); j++) {
                if(noeud.getIdNoeud() != this.listeNoeuds.get(j).getIdNoeud()) {
                    Arc arc = new Arc (noeud, this.listeNoeuds.get(j));
                    this.listeArcs.add(arc);
                }
            }
        }

        // Generation des contraintes
        for(int i = 0; i < this.listeArcs.size(); i++) {
            this.listeArcs.get(i).ganererListeContr();
        }


        // Atteindre la densite souhaite
        nbrMaximumArc = this.listeArcs.size();
        nbrArc = nbrMaximumArc;
        densiteActuelle = nbrArc / nbrMaximumArc ;
        while (densiteActuelle > densite) {
            this.listeArcs.remove(new Random().nextInt(this.listeArcs.size()));
            nbrArc = this.listeArcs.size();
            densiteActuelle = nbrArc / nbrMaximumArc ;
        }

        // Atteindre la durete saisie aleatoirement en reduisant le nombre de contrainte
        nbrMaximumContraintes = this.listeArcs.get(0).getListeContraintes().size();
        for (int i = 0; i < this.listeArcs.size(); i++) {
            nbrContraintes = this.listeArcs.get(i).getListeContraintes().size();
            dureteActuelle = nbrContraintes / nbrMaximumContraintes;

            while (dureteActuelle > durete) {
                this.listeArcs.get(i).getListeContraintes().remove(new Random().nextInt(nbrContraintes));
                nbrContraintes = this.listeArcs.get(i).getListeContraintes().size();
                dureteActuelle = nbrContraintes / nbrMaximumContraintes;
            }
        }
        RemplirListes();
    }

    // Remplir les listes adjacentes en entree et en sortie
    public void RemplirListes() {

        for (int i = 0; i < this.listeNoeuds.size(); i++){
            ArrayList<Arc> arcEntree = new ArrayList<Arc>();
            ArrayList<Arc> arcSortie = new ArrayList<Arc>();

            for (int j = 0; j < listeArcs.size(); j++){
                if (this.listeArcs.get(j).getN1().getIdNoeud() == i)
                    arcSortie.add(this.listeArcs.get(j));
                else if (this.listeArcs.get(j).getN2().getIdNoeud() == i)
                    arcEntree.add(this.listeArcs.get(j));
            }
            this.listeAdjacenteEnEntree.add(i, arcEntree);
            this.listeAdjacenteEnSortie.add(i, arcSortie);
        }
    }

    // Afficher CSP
    public void AfficherCSP () {
        // Affichage de la liste des noeuds et leurs domaines
        System.out.println("\n Liste des noeuds générés avec leurs domaines --> \n");
        for (int i = 0; i < this.listeNoeuds.size(); i++) {
            System.out.print(this.listeNoeuds.get(i).toString() + " : ");
            this.listeNoeuds.get(i).afficherDomaines();
        }
        // Affichage des arcs
        System.out.println("\n Liste des arcs avec la densité souhaitée --> \n");
        for (int i = 0; i < this.listeArcs.size(); i++) {
            System.out.println(this.listeArcs.get(i).toString() + " , ");
        }

        // Affichage des contraintes
        System.out.println("\n Liste des contraintes avec la dureté souhaitée --> \n");
        for (int i = 0; i < this.listeArcs.size(); i++) {
            this.listeArcs.get(i).AfficherListeContr();
        }
    }

    // Verification du CSP
    public boolean VerifierCSP () {
        boolean verifier = true;
        RemplirListeDomaine();
        for (int i = 0; i < this.listeDomaines.size(); i++)
            if (this.listeDomaines.get(i).isEmpty()){
                if (!this.listeAdjacenteEnEntree.get(i).isEmpty() || !this.listeAdjacenteEnSortie.get(i).isEmpty())
                    return false;
                else {
                    this.listeNoeuds.get(i).setValNoeud(this.listeNoeuds.get(i).getDomaine().get(new Random().nextInt(this.listeNoeuds.get(i).getDomaine().size())));
                    this.listeDomaines.get(i).add(this.listeNoeuds.get(i).getValNoeud());
                }
            }
        FilterContraintes();
        return verifier;
    }

    // Filtrage des contraintes
    private void FilterContraintes() {
        int contrainte1, contrainte2, noeud;
        for (int i = 0; i < this.listeNoeuds.size(); i++) {
            for (int j = 0; j < this.listeAdjacenteEnSortie.get(i).size(); j++) {
                for (int k = 0 ; k < this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().size(); k++) {
                    contrainte1 = this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().get(k).getContrainte1();
                    noeud 	 = this.listeAdjacenteEnSortie.get(i).get(j).getN2().getIdNoeud();
                    contrainte2 = this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().get(k).getContrainte2();
                    if (!this.listeDomaines.get(i).contains(contrainte1) || !this.listeDomaines.get(noeud).contains(contrainte2)){
                        this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().remove(k);
                        k--;
                    }
                }
            }
        }
    }

    // Remplissage de la liste du domaine
    private void RemplirListeDomaine() {
        for (int i = 0; i < this.listeNoeuds.size(); i++){
            this.listeDomaines.add(i, FilterDomaines(i));
        }

    }

    // Filtrage du domaine
    private ArrayList<Integer> FilterDomaines(int idNoeud) {

        int val;
        ArrayList<Integer> domaines = new ArrayList<Integer>();

        // La liste des domaines correspondante au noeud passe en parametre
        domaines = this.listeNoeuds.get(idNoeud).getDomaine();

        // Pour chaque valeur du domaine du noeud, on verifie la consistance
        for (int i = 0; i < this.listeNoeuds.get(idNoeud).getDomaine().size(); i++){
            val = this.listeNoeuds.get(idNoeud).getDomaine().get(i);
            if(verifierConsistance(val, idNoeud) == false)
                // On retire du domaine les valeurs qui rendent le CSP inconsistant
                domaines.remove(domaines.indexOf(val));
        }

        // On retire de domaines les doublons
        for (int i = 0; i < domaines.size(); i++) {
            while (NbrRepetition(domaines.get(i), domaines) > 1){
                domaines.remove(domaines.indexOf(domaines.get(i)));
            }
        }
        return domaines;
    }

    // Retourne le nombre de repetitions de valeurs dans le domaine
    public int NbrRepetition(int valeur, ArrayList<Integer> domaines) {
        int i = 0;
        for(int j = 0; j < domaines.size(); j++){
            if(domaines.get(j) == valeur)
                i++;
        }
        return i;
    }

    private boolean verifierConsistance(int val, int idNoeud) {

        boolean verifier = false;

        if (this.listeAdjacenteEnSortie.get(idNoeud).isEmpty() && this.listeAdjacenteEnEntree.get(idNoeud).isEmpty())
            verifier = true;

        for (int i = 0; i < this.listeAdjacenteEnSortie.get(idNoeud).size(); i++){
            verifier = false;
            for (int j = 0; j < this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().size(); j++){
                if (val == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1()){
                    verifier = true;
                }
            }
            if(verifier == false){
                return verifier;
            }
        }

        for (int i = 0; i < this.listeAdjacenteEnEntree.get(idNoeud).size(); i++){
            verifier = false;
            for (int j = 0; j < this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().size(); j++){
                if(val == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2()){
                    verifier = true;
                }
            }
            if(verifier == false){
                return verifier;
            }
        }
        return verifier;
    }

    // Algorithme backjumping
    public long backjumping() {
        int i = 0, j = 0, valeur = 0;
        boolean verifier;
        long temps = System.nanoTime();
        long tempsExecution;
        boolean consistant;

        ArrayList<ArrayList<Integer>> domaines = new ArrayList<ArrayList<Integer>>();
        domaines = DomaineListe();

        ArrayList<Integer> coupables = new ArrayList<Integer>();
        for (int k = 0 ; k < this.listeNoeuds.size(); k++) {
            coupables.add(k, 0);
        }

        while (i >= 0 && i < this.listeNoeuds.size()) {
            verifier = false;
            while(!verifier && !domaines.get(i).isEmpty()){
                valeur = domaines.get(i).remove(0);
                consistant = true;
                j = 0;
                while (j > i && consistant) {
                    if (j > coupables.get(i)){
                        coupables.add(i, j);
                    }
                    if (VerifierassignationConsistance(valeur, this.listeNoeuds.get(i).getIdNoeud())){
                        j++;
                    } else {
                        consistant = false;
                    }
                }

                if (consistant) {
                    verifier = true;
                    this.listeNoeuds.get(i).setValNoeud(valeur);
                }
            }

            if (!verifier) {
                this.listeDomaines.get(i).clear();
                for (int z = 0; z < this.listeDomaines.get(i).size(); z++)
                    domaines.get(i).add(this.listeDomaines.get(i).get(z));
                i = coupables.get(i);
            } else {
                i++;
                coupables.add(i, 0);
            }
        }

        if (i < 0) {
            System.out.println("BackJumping : UNSAT");
            tempsExecution = System.nanoTime();
            return tempsExecution - temps;
        } else {
            tempsExecution = System.nanoTime();
            AfficherSolution();
            return tempsExecution - temps;
        }
    }


    // Code Algorithme backtracking
    public long backtracking () {

        int i = 0, valeur = 0;
        boolean verifier;
        long temps = System.nanoTime();
        long tempsExecution;

        ArrayList<ArrayList<Integer>> domaines = new ArrayList<ArrayList<Integer>>();
        domaines = DomaineListe();

        while (i >= 0 && i < this.listeNoeuds.size()) {
            verifier = false;
            while (!verifier && !domaines.get(i).isEmpty()){
                valeur = domaines.get(i).remove(0);
                if (VerifierassignationConsistance(valeur, this.listeNoeuds.get(i).getIdNoeud())){
                    verifier = true;
                    this.listeNoeuds.get(i).setValNoeud(valeur);
                }
            }
            if (!verifier) {
                domaines.get(i).clear();
                for(int j = 0; j < this.listeDomaines.get(i).size(); j++)
                    domaines.get(i).add(this.listeDomaines.get(i).get(j));
                i--;
            } else
                i++;
        }
        if (i < 0){
            System.out.println("BackTracking : UNSAT");
            tempsExecution = System.nanoTime();
            return tempsExecution - temps;
        } else {
            tempsExecution = System.nanoTime();
            AfficherSolution();
            return tempsExecution - temps;
        }
    }

    public ArrayList<ArrayList<Integer>> DomaineListe () {
        ArrayList<ArrayList<Integer>> domaines1 = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < this.listeDomaines.size(); i++){
            ArrayList<Integer> domaines2 = new ArrayList<Integer>();
            for(int j = 0; j < this.listeDomaines.get(i).size(); j++)
                domaines2.add(this.listeDomaines.get(i).get(j));
            domaines1.add(domaines2);
        }
        return domaines1;
    }

    private void AfficherSolution() {
        System.out.println("\n ---------------------------- Solution du CSP --------------------------- \n");
        System.out.println(" Noeuds avec leurs valeurs --> \n");
        for (int i = 0; i < this.listeNoeuds.size(); i++)
            System.out.println(" La valeur du noeud " + i + " : " + this.listeNoeuds.get(i).getValNoeud() + "\n");
    }

    private boolean VerifierassignationConsistance(int val, int idNoeud) {

        boolean verifier = false;
        for (int i = 0; i < this.listeAdjacenteEnSortie.get(idNoeud).size(); i++){
            verifier = false;
            if(this.listeAdjacenteEnSortie.get(idNoeud).get(i).getN2().getValNoeud() == -1)
                verifier = true;
            else {
                for(int j = 0; j < this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().size(); j++)
                    if(val == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1()
                            && this.listeAdjacenteEnSortie.get(idNoeud).get(i).getN2().getValNoeud() == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2())
                        verifier = true;
            }
            if (verifier == false)
                return verifier;
        }
        for(int i = 0; i < this.listeAdjacenteEnEntree.get(idNoeud).size(); i++){
            verifier = false;
            if(this.listeAdjacenteEnEntree.get(idNoeud).get(i).getN1().getValNoeud() == -1)
                verifier = true;
            else {
                for(int j = 0; j < this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().size(); j++)
                    if(val == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2()
                            && this.listeAdjacenteEnEntree.get(idNoeud).get(i).getN1().getValNoeud() == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1())
                        verifier = true;
            }
            if (verifier == false)
                return verifier;
        }
        return verifier;
    }

}

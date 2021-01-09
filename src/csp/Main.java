package csp;


import java.util.Scanner;

public class Main {

    static GenerateurCSP csp = new GenerateurCSP();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int taillDomaine= 0;
        int nbrNoeud= 0;
        double densite= 0;
        double duretee= 0;

        System.out.println(" Donner le nombre des noeuds en entier : ");
        Scanner sc = new Scanner(System.in);
        nbrNoeud = sc.nextInt();

        System.out.println(" Donner Votre taille du domaine en entier : ");
        taillDomaine = sc.nextInt();

        System.out.println("Entrer la densite entre 0 et 1 ! ");
        densite = sc.nextDouble();

        System.out.println("Durete entre 0 et 1 ! ");
        duretee = sc.nextDouble();
        sc.close();

        System.out.println(" Votre CSP est : ======> ");
        csp.genererCSP(nbrNoeud, taillDomaine, densite, duretee);
        csp.AfficherCSP();

        if(csp.VerifierCSP()){
            long dureeBT = csp.backtracking();
            double backtracking = (double) dureeBT / 1000000.0;

            long dureeBJ = csp.backjumping();
            double backjumping = (double) dureeBJ / 1000000.0;

            System.out.println("La duree execution backtracking est : " + backtracking + " ms");
            System.out.println("La duree execution backjumping est : " + backjumping + " ms");
        } else
            System.out.println("UNSAT");
    }


}

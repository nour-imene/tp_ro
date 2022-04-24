import java.util.Scanner;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class projet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		solveMe();
	}
	public static void solveMe() {
		
		//entr� le nombre de personne et machine
		Scanner sc=new Scanner(System.in);
		System.out.println("entrez le nombre de personne et de machine");
		int n=sc.nextInt();
		//remplissage de la matrice de productivit�
		double [][]p=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++) {
				System.out.println("donnez la productivit� de la personne " +j+" au poste " +i+ "\n");
				
				p[i][j]=sc.nextDouble();
				
			}
		}
		try {
			//D�finir un nouveau model
        	IloCplex cplex = new IloCplex();
        	IloNumVar[][] x = new IloNumVar[n][n];
        	for(int i=0;i<n;i++) {
        		for(int j =0;j<n;j++) {
        			x[i][j]= cplex.numVar(0,1);
        		}
        	}
        	//definir la fonction objectif
        	IloLinearNumExpr objective = cplex.linearNumExpr();
        	for (int i=0; i<n; i++) {
        		for (int j=0; j<n; j++) {
        			objective.addTerm(p[i][j],x[i][j]);
        		}
        	}
        	cplex.addMaximize(objective);
        	// les contraintes
        	IloLinearNumExpr[] per = new IloLinearNumExpr[n];
        	for (int j=0; j<n; j++) {
        		per[j] = cplex.linearNumExpr();
        		for (int i=0; i<n; i++) {
        			per[j].addTerm(1.0, x[i][j]);
        		
        		}
        	}
        	//contrainte une(pour chaque machine une seule personne)
        	for(int i=0;i<n;i++) {
        		cplex.addEq(per[i], 1);
        	}
        	//deuxi�me contrainte (chaque personne est affect� a une seule machine)
        	for (int i=0; i<n; i++) {
        		cplex.addEq(cplex.sum(x[i]),1);
        		
        	}
        	
        	//r�soudre le probl�me
        	if (cplex.solve()) {
        		System.out.println("resultat optimal est = "+cplex.getObjValue());
        	}
        	else {
        		System.out.println("problem not solved");
        	}
        	//affichage des valeurs des variables de d�cisions
        	System.out.println("Avec \n");
        	for (int i=0;i<n;i++) {
        		for (int j=0; j<n; j++) {
        			
        		 System.out.println( "X"+i+ ""+j+"= "+ cplex.getValue(x[i][j]));}
        	}
        		
        	cplex.end();
        	
		}
		//cas d'exeption
		catch(IloException exc){
			System.out.print("Exception lev�e " + exc);;
		}
	}
}

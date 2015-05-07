package algorithme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import parse.Data;

import composition.Composition;

import entites.Planche;
import execute.Execute;

public class Solution extends SolutionAbstract {
	int nbPlanche = Execute.NB_PATTERNS;
	private List<Planche> planches;
	private double prixTotal;
	private Data data;

	public Solution(Data data) {
		int nbImages = data.getNbImages();
		this.data = data;
		prixTotal = -1;
		Planche p = data.getPlanche();
		planches = new LinkedList<Planche>();

		// for (int j = 0; j < nbPlanche; j++) {
		// int[] compoPlanche = new int[nbImages];
		// listCompo.add(compoPlanche);
		// }

		int cpt = 0;
		int[] compo = new int[nbImages];
		for (int indice = 0; indice < nbImages; indice++) {
			if (cpt == 0) {
				compo[indice] = 1;
			}
			if (cpt == nbPlanche - 1) {
				cpt = 0;
			} else {
				cpt++;
			}
		}

		Composition c = new Composition(compo);
		Planche pl = new Planche(data.getPlanche().getPrix(), data.getPlanche()
				.getDimension(), 0, -1, c);
		planches.add(pl);

		for (int k = 1; k < nbPlanche; k++) {
			int[] compo2 = rotate(compo, k);
			Composition c2 = new Composition(compo2);
			Planche pl2 = new Planche(data.getPlanche().getPrix(), data
					.getPlanche().getDimension(), 0, -1, c2);
			planches.add(pl2);
		}
		for (Planche p6 : planches) {
			System.out.println(p6.toString());
		}

	}

	public Solution() {
		// TODO Auto-generated constructor stub
	}

	public int[] rotate(int[] arr, int order) {
		int[] base = arr.clone();
		if (arr == null || order < 0) {
			throw new IllegalArgumentException("Illegal argument!");
		}

		for (int i = 0; i < order; i++) {
			for (int j = arr.length - 1; j > 0; j--) {
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
		}
		int[] temp = arr.clone();
		arr = base;
		return temp;
	}

	public double getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(double prixTotal) {
		this.prixTotal = prixTotal;
	}

	/**
	 * Calcul de la fitness
	 * 
	 * @return
	 */
	public int calculCoutTotal() {
		int total = 0;
		total += data.getPlanche().getPrix() * this.planches.size();
		simplex();
		for (Planche p : planches) {
			if (p.getQuantite() < 0) {
				total += 1000;
			} else {
				total += p.getQuantite();
			}
		}
		this.prixTotal = total;
		return total;
	}

	public List<Integer> quantites() {
		List<Integer> quantites = new ArrayList<Integer>();
		for (Planche p : planches) {
			quantites.add(p.getQuantite());
		}
		return quantites;
	}

	/**
	 * Fonction qui met à jour le nombre de fois qu'il faut imprimer chaque
	 * planche pour avoir toute les images. Si l'image est sur différent
	 * pattern, on augmente le pattern qui comporte le plus de fois cette image.
	 */
	private void simplex() {
		int nbImages = data.getNbImages();
		int nbPlanche = planches.size();

		List<double[]> contraintes = new ArrayList<double[]>();
		// Initialise
		for (int i = 0; i < nbImages; i++) {
			double[] init = new double[nbPlanche];
			contraintes.add(init);
		}
		double[] fctObjective = new double[nbPlanche];
		for (int k = 0; k < fctObjective.length; k++) {
			fctObjective[k] = 1;
		}
		// Création des contraintes
		for (int j = 0; j < nbImages; j++) {
			int i = 0;
			for (Planche p : planches) {
				contraintes.get(j)[i] = p.getComposition().getCompoPlanche()[j];
				i++;
			}
		}

		LinearObjectiveFunction f = new LinearObjectiveFunction(fctObjective, 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		int indice = 0;
		for (double[] contrainte : contraintes) {
			constraints.add(new LinearConstraint(contrainte, Relationship.GEQ,
					data.getImage(indice).getQuantite()));
			indice++;
		}

		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = null;
		try {
			solution = solver.optimize(new MaxIter(Integer.MAX_VALUE), f,
					new LinearConstraintSet(constraints), GoalType.MINIMIZE,
					new NonNegativeConstraint(true));
		} catch (Exception e) {

		}
		int s = 0;
		if (solution != null) {
			for (double b : solution.getPoint()) {
				planches.get(s).setQuantite((int) Math.ceil(b));
				s++;
			}
		} else {
			for (Planche p : planches) {
				p.setQuantite(-1);
			}
		}

	}

	public List<Planche> getPlanches() {
		return planches;
	}

	public void setPlanches(List<Planche> planches) {
		this.planches = planches;
	}

	@Override
	public double deltaF(Solution solution) {
		return solution.fonctionObjectif() - this.fonctionObjectif();
	}

	@Override
	public double fonctionObjectif() {
		return calculCoutTotal();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("----------- Solution ---------------\n");
		for (Planche p : planches) {
			if (p != null)
				s.append(p.toString() + "\n");
		}
		s.append("prixTotal=" + prixTotal + "\n");
		return s.toString();
	}

	@Override
	public Solution clone() {
		Solution s = new Solution();
		List<Planche> listPlanches = new ArrayList<Planche>();
		for (Planche p : planches) {
			Planche p2 = p.clone();
			listPlanches.add(p2);
		}
		s.setPlanches(listPlanches);
		s.setPrixTotal(this.prixTotal);
		return s;

	}

}

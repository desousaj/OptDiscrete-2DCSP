package algorithme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import parse.Data;
import stats.Execute;
import composition.Composition;
import entites.Bin;
import entites.Planche;
import exception.MonException;
import graphique.FenetrePattern;

public class RecuitSimule {

	public static final int INIT_TEMP = 1000;
	public static double FACTEUR_DECROISSANCE = 0.9;
	public static int NB_ITERATIONS_PAR_PALIER = 1000;
	public static double TEMPERATURE_FINALE = 0.5;

	protected Data data;

	private AlgoPlacement algoPlacement;

	/** La solution courante du recuit simule. */
	protected Solution solutionCourante;
	/** La meilleure solution trouvee pour le moment. */
	protected Solution meilleureSolution;
	/** Le facteur de decroissance de la temperature du recuit. */
	protected double facteurDecroissance;

	public double getFacteurDecroissance() {
		return facteurDecroissance;
	}

	public void setFacteurDecroissance(double facteurDecroissance) {
		this.facteurDecroissance = facteurDecroissance;
	}

	public double getTemperatureFinale() {
		return temperatureFinale;
	}

	public void setTemperatureFinale(double temperatureFinale) {
		this.temperatureFinale = temperatureFinale;
	}

	public int getNbIterationsParPalier() {
		return nbIterationsParPalier;
	}

	public void setNbIterationsParPalier(int nbIterationsParPalier) {
		this.nbIterationsParPalier = nbIterationsParPalier;
	}

	/** La temperature du recuit. */
	protected double temperature;
	/** La temperature à atteindre pour arreter le recuit. */
	private double temperatureFinale = TEMPERATURE_FINALE;
	/** La valeur de la meilleure solution trouvee. */
	protected double meilleureValeur;
	/**
	 * Le taux d'acceptation de solutions couteuses acceptees par le recuit à
	 * la temperature initiale.
	 */
	// protected double probabiliteAcceptation;

	/** Le nombre d'iterations par palier de temperature */
	private int nbIterationsParPalier;
	/** Le nombre d'iterations courant pour le palier de temperature courant. */
	private int iterationCourante;

	public Solution calculSolution() {
		return null;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Construit un recuit simule.
	 * 
	 * @param facteurDecroissance
	 *            le facteur de decroissance de la temperature.
	 * @throws MonException
	 */
	public RecuitSimule(Data data, double facteurDecroissance,
			double tauxAcceptation) throws MonException {
		this.data = data;
		this.algoPlacement = new AlgoPlacement(data);
		this.facteurDecroissance = facteurDecroissance;
		// this.solutionCourante = new Solution(data);
		this.initSolutionInitial();
		this.nbIterationsParPalier = NB_ITERATIONS_PAR_PALIER;
		this.temperatureFinale = TEMPERATURE_FINALE;
		this.temperature = INIT_TEMP;
	}

	/**
	 * Construit un recuit simule.
	 * 
	 * @param facteurDecroissance
	 *            le facteur de decroissance de la temperature.
	 * @throws MonException
	 */
	public RecuitSimule(Data data, double tempFinale, double tempInitiale,
			int nbIteration, double factDecr) throws MonException {
		this.data = data;
		this.algoPlacement = new AlgoPlacement(data);
		this.facteurDecroissance = factDecr;
		this.initSolutionInitial();
		this.nbIterationsParPalier = nbIteration;
		this.temperatureFinale = tempFinale;
		this.temperature = tempInitiale;
	}

	/**
	 * Construit un recuit simule.
	 * 
	 * @param facteurDecroissance
	 *            le facteur de decroissance de la temperature.
	 * @throws MonException
	 */
	public RecuitSimule(Data data) throws MonException {
		this.data = data;
		this.algoPlacement = new AlgoPlacement(data);
		this.facteurDecroissance = FACTEUR_DECROISSANCE;
		// this.solutionCourante = new Solution(data);
		this.initSolutionInitial();
		this.nbIterationsParPalier = NB_ITERATIONS_PAR_PALIER;
		this.temperatureFinale = TEMPERATURE_FINALE;
		this.temperature = INIT_TEMP;
	}

	private void initSolutionInitial() throws MonException {
		int[][] temp_sol = new int[Execute.NB_PATTERNS][this.data.getNbImages()];
		int[] temp_compo;
		int numRandomPattern;
		Solution solution = new Solution(data);
		Random rand = new Random();

		System.out.println("Searching an initial solution... Wait for it...");

		// Tente de trouver une solution initiale au maximum 2Milliards de fois
		for (int n = 0; n < Integer.MAX_VALUE; n++) {
			// Prepare les valeurs d'une solution
			for (int i = 0; i < this.data.getNbImages(); i++) {
				numRandomPattern = rand.nextInt((Execute.NB_PATTERNS)); // donne
																		// un nb
																		// entre
																		// 0 et
																		// NB_PATTERNS-1
				for (int j = 0; j < Execute.NB_PATTERNS; j++) {
					temp_sol[j][i] = numRandomPattern == j ? 1 : 0;
				}
			}

			// Creer une solution avec les valeurs precedement trouve
			solution = new Solution(data);
			for (int i = 0; i < Execute.NB_PATTERNS; i++) {
				temp_compo = new int[this.data.getNbImages()];
				for (int j = 0; j < this.data.getNbImages(); j++) {
					temp_compo[j] = temp_sol[i][j];
				}
				solution.getPlanches().get(i)
						.setComposition(new Composition(temp_compo));
			}

			// Verifie la possibilit� de cette solution
			if (this.testPlacement(solution)) {
				System.out.println("Initial solution found");
				this.solutionCourante = solution;
				break;
			}
		}

		// Si le random n'a donne aucune possiblite reel
		if (this.solutionCourante == null) {
			System.out.println("Initial solution was not found... Abord");
			throw new MonException();
		}

		// Affichage solution de base - Test - TODO delete this
		// int i = 0;
		// for (Planche p : solution.getPlanches()) {
		// System.out.println("Planche " + i + "\n" + p.toString());
		// i++;
		// }

	}

	private boolean testPlacement(Solution solutionVoisine) {
		boolean test = true;
		List<Bin> bins = null;
		int j = 0;
		for (Planche p : solutionVoisine.getPlanches()) {
			bins = algoPlacement.placementBis(p.getComposition()
					.getCompoPlanche(), j);
			if (bins == null) {
				test = false;
				break;
			}
			j++;
		}
		return test;
	}

	private Map<Integer, List<Bin>> buildPlacement(Solution solutionVoisine) {
		Map<Integer, List<Bin>> maps = new HashMap<Integer, List<Bin>>();
		boolean test = true;
		List<Bin> bins = null;
		int j = 0;
		for (Planche p : solutionVoisine.getPlanches()) {
			bins = algoPlacement.placementBis(p.getComposition()
					.getCompoPlanche(), j);
			if (bins == null) {
				test = false;
				break;
			} else {
				maps.put(j, bins);
			}
			j++;
		}
		return maps;
	}

	/**
	 * Demarre le recuit simule. Implemente le coeur de l'algorithme du recuit
	 * simule commun à tous les problemes.
	 */
	public void lancer() {
		Solution solutionVoisine;
		double delta;
		AlgoPlacement algoPl = new AlgoPlacement(data);
		// Initilaisation du recuit
		// initialiserTemperature();
		meilleureValeur = solutionCourante.fonctionObjectif();
		meilleureSolution = solutionCourante.clone();

		// Les paliers de temperature
		while (testerCondition1()) {
			// Les iterations par palier
			while (testerCondition2()) {
				// On cherche une solution dans le voisinage
				solutionVoisine = voisin();

				delta = solutionCourante.deltaF(solutionVoisine);
				// afficherSolution(solutionVoisine, "Voisine");
				// afficherSolution(solutionCourante, "Courante");
				// System.out.println(meilleureSolution.getPrixTotal());

				// Si la solution du voisinage est meilleure que la
				// solution
				// courante
				if (delta <= 0) {
					// La solution voisine devient la solution
					// courante
					solutionCourante = solutionVoisine.clone();

					// Si la solution voisine est meilleure que la
					// meilleure
					// trouvee pour le moment
					// Elle devient la meilleure solution
					if (solutionCourante.fonctionObjectif() < meilleureValeur) {
						meilleureValeur = solutionCourante.fonctionObjectif();
						meilleureSolution = solutionCourante.clone();
					}
				}
				// Si la solution voisine n'ameliore pas la solution
				// courante
				// Elle peut être acceptee
				else {
					double p = Math.random();
					if (p <= Math.exp(-delta / temperature)) {
						solutionCourante = solutionVoisine.clone();
					}
				}
				// afficherSolution(solutionCourante,
				// "Solution courante");
			}
			decroitreTemperature();
		}
		// afficherSolution(meilleureSolution, "Meilleur solution");
		// afficherBins(algoPl.getListBins());
		// testPlacement(meilleureSolution);
		Map<Integer, List<Bin>> map = buildPlacement(meilleureSolution);
		FenetrePattern f = new FenetrePattern(data, map,
				meilleureSolution.quantites(),
				meilleureSolution.getPrixTotal(),
				meilleureSolution.getPlanches());
		// afficherBins(this.algoPlacement.getListBins());
		// afficherSolution(meilleureSolution, "Meilleur");
	}

	// }

	private Map<Integer, List<Bin>> reconstruireBin(List<Bin> listBins) {
		Map<Integer, List<Bin>> map = new HashMap<Integer, List<Bin>>();
		for (Bin b : listBins) {
			int indiceBin = b.getNumPatern();
			if (!map.containsKey(indiceBin)) {
				map.put(indiceBin, new ArrayList<Bin>());
			}
			map.get(indiceBin).add(b);
		}
		return map;
	}

	private void afficherBins(List<Bin> listBins) {
		Map<Integer, List<Bin>> m = reconstruireBin(listBins);
		StringBuilder s = new StringBuilder();
		for (int numPattern : m.keySet()) {
			s.append("==================== Bins n°" + numPattern
					+ "===============\n");
			for (Bin b : m.get(numPattern)) {
				s.append(b.toString() + "\n");
			}
		}
		System.out.println(s.toString());
	}

	private void afficherSolution(Solution solutionCourante2, String sol) {
		System.out.println("--------------------------" + sol
				+ "--------------------------");
		StringBuilder qte = new StringBuilder();
		qte.append("Quantite ==> ");
		for (Planche p : solutionCourante2.getPlanches()) {
			StringBuilder s = new StringBuilder();
			s.append("[");
			for (int i : p.getComposition().getCompoPlanche()) {
				s.append(i + ",");
			}
			s.append("]");
			System.out.println(s.toString());
			qte.append(p.getQuantite() + "; ");
		}
		System.out.println(qte.toString());
		System.out
				.println("Prix total ==> " + solutionCourante2.getPrixTotal());
	}

	/**
	 * Teste si le recuit est arrive la temperature finale demandee à la
	 * creation.
	 * 
	 * @return true si le recuit peut passer au palier de temp�rature suivant,
	 *         false sinon.
	 */
	protected boolean testerCondition1() {
		// System.out.println("Temperature : " + temperature);
		if (temperature > temperatureFinale) {
			return true;
		} else {
			return false;
		}
	}

	private boolean testSolTrouver() {
		for (Planche p : meilleureSolution.getPlanches()) {
			if (p.getQuantite() < 0) {
				System.out.println("Pas de solution trouv�e...On relance");
				return false;
			}
		}
		return true;
	}

	/**
	 * Teste si le recuit a encore des iterations à faire pour un palier de
	 * temperature.
	 * 
	 * @return true si le recuit doit continuer à ce palier de temperature,
	 *         false sinon.
	 */
	protected boolean testerCondition2() {
		iterationCourante++;
		if (iterationCourante < nbIterationsParPalier) {
			return true;
		} else {
			iterationCourante = 0;
			return false;
		}
	}

	/**
	 * Initialise la temperature du recuit. La temperature initiale doit
	 * accepter un certain nombre de solutions couteuses. Ce taux est fixe par
	 * l'utilisateur.
	 */
	// private void initialiserTemperature() {
	// Solution solutionVoisine;
	// AlgoPlacement algoPl = new AlgoPlacement(data);
	// double taux = 0;
	// temperature = 1000;
	//
	// // Tant que le taux d'acceptation n'est pas celui voulu
	// // On multiplie la temperature par 2
	// do {
	// int nbCouteuses = 0;
	// int nbCouteusesAcceptees = 0;
	//
	// // On fait 50 tirages de solutions voisines
	// for (int i = 0; i < 50; i++) {
	// solutionVoisine = voisin();
	// boolean test = testPlacement(solutionVoisine);
	// if (test) {
	// // Si on a une solution couteuse, on regarde si elle est
	// // acceptee
	// if (solutionCourante.deltaF(solutionVoisine) >= 0) {
	// this.solutionCourante = solutionVoisine.clone();
	// double p = Math.random();
	// nbCouteuses++;
	//
	// if (p <= Math.exp(-solutionCourante
	// .deltaF(solutionVoisine) / temperature)) {
	// nbCouteusesAcceptees++;
	// }
	// }
	// }
	// }
	//
	// taux = (double) nbCouteusesAcceptees / (double) nbCouteuses;
	// if (taux < probabiliteAcceptation) {
	// temperature *= 2;
	// }
	// } while (taux < probabiliteAcceptation);
	// }

	/**
	 * Decroit la temperature du recuit. La fonction utilisee est : f(t) = alpha
	 * x t avec alpha fixee par l'utilisateur.
	 */
	private void decroitreTemperature() {
		temperature *= facteurDecroissance;
	}

	/**
	 * Selectionne une solution dans le voisinage de la solution courante. Dans
	 * notre cas, une solution voisine est une solution dont on decalle le
	 * nombre d'image à l'image suivante et on ajoute 1 une des images.
	 * 
	 * @return une solution voisine de la solution courante.
	 */
	protected Solution voisin() {
		Solution voisin = null;
		boolean test = false;

		Random rand = new Random();
		int pattern;
		int image;
		int cpt;

		// Tant que le voisin n'est pas une solution realisable
		while (!test) {

			voisin = this.solutionCourante.clone();

			// 1ere transformation : ajout d'une image dans un pattern
			pattern = rand.nextInt(Execute.NB_PATTERNS);
			image = rand.nextInt(this.data.getNbImages());
			voisin.getPlanches().get(pattern).getComposition()
					.getCompoPlanche()[image] += 1;

			// 2eme transformation : suppresion d'une image dans un pattern
			pattern = rand.nextInt(Execute.NB_PATTERNS);
			image = rand.nextInt(this.data.getNbImages());

			cpt = 0;
			// Recupere le nombre de fois que l'image est presente sur
			// l'ensemble des patterns
			for (Planche p : voisin.getPlanches()) {
				cpt += p.getComposition().getCompoPlanche()[image];
			}
			// Seulement si ce nombre est superieur a 1
			if (cpt > 1) {
				// Si l'image n'est pas deja a 0 dans le pattern
				if (voisin.getPlanches().get(pattern).getComposition()
						.getCompoPlanche()[image] != 0) {
					voisin.getPlanches().get(pattern).getComposition()
							.getCompoPlanche()[image] -= 1;
				}
			}
			test = testPlacement(voisin);

			// Affichage solution de base - Test - TODO delete this
			// int i = 0;
			// System.out.println("----------------");
			// System.out.println(test);
			// for (Planche p : voisin.getPlanches()){
			// System.out.println("Planche " + i +
			// "\n" +p.toString());
			// i++;
			// }
		}
		return voisin;
	};

	public Solution getMeilleureSolution() {
		return meilleureSolution;
	}
}

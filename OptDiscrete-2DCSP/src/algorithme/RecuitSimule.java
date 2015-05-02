package algorithme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import parse.Data;

import composition.Composition;

import entites.Bin;
import entites.Planche;
import graphique.FenetrePattern;

public class RecuitSimule {

	private static final int INIT_TEMP = 1000;
	public static double FACTEUR_DECROISSANCE = 0.9;
	public static double TAUX_ACCEPTATION = 0.8;
	public static int NB_ITERATIONS_PAR_PALIER = 10000;
	public static double TEMPERATURE_FINALE = 0.5;

	protected Data data;

	/** La solution courante du recuit simulé. */
	protected Solution solutionCourante;
	/** La meilleure solution trouvée pour le moment. */
	protected Solution meilleureSolution;
	/** Le facteur de décroissance de la température du recuit. */
	protected double facteurDecroissance;
	/** La température du recuit. */
	protected double temperature;
	/** La température à atteindre pour arrêter le recuit. */
	private double temperatureFinale = TEMPERATURE_FINALE;
	/** La valeur de la meilleure solution trouvée. */
	protected double meilleureValeur;
	/**
	 * Le taux d'acceptation de solutions coûteuses acceptées par le recuit à la
	 * température initiale.
	 */
	protected double probabiliteAcceptation;

	/** Le nombre d'itérations par palier de température */
	private int nbIterationsParPalier;
	/** Le nombre d'itérations courant pour le palier de température courant. */
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
	 * Construit un recuit simulé.
	 * 
	 * @param facteurDecroissance
	 *            le facteur de décroissance de la température.
	 */
	public RecuitSimule(Data data, double facteurDecroissance,
			double tauxAcceptation) {
		this.data = data;
		this.facteurDecroissance = facteurDecroissance;
		this.probabiliteAcceptation = tauxAcceptation;
		this.solutionCourante = new Solution(data);
		this.nbIterationsParPalier = NB_ITERATIONS_PAR_PALIER;
		this.temperatureFinale = TEMPERATURE_FINALE;
		this.temperature = INIT_TEMP;

	}

	/**
	 * Construit un recuit simulé.
	 * 
	 * @param facteurDecroissance
	 *            le facteur de décroissance de la température.
	 */
	public RecuitSimule(Data data) {
		this.data = data;
		this.facteurDecroissance = FACTEUR_DECROISSANCE;
		this.probabiliteAcceptation = TAUX_ACCEPTATION;
		this.solutionCourante = new Solution(data);
		this.nbIterationsParPalier = NB_ITERATIONS_PAR_PALIER;
		this.temperatureFinale = TEMPERATURE_FINALE;
		this.temperature = INIT_TEMP;
	}

	private boolean testPlacement(AlgoPlacement algoPl, Solution solutionVoisine) {
		boolean test = true;
		List<Bin> bins = null;
		int j = 0;
		for (Planche p : solutionVoisine.getPlanches()) {
			bins = algoPl.placementBis(p.getComposition().getCompoPlanche(), j);
			if (bins == null) {
				test = false;
				break;
			}
			j++;
		}
		return test;
	}

	private Map<Integer, List<Bin>> buildPlacement(AlgoPlacement algoPl,
			Solution solutionVoisine) {
		Map<Integer, List<Bin>> maps = new HashMap<Integer, List<Bin>>();
		boolean test = true;
		List<Bin> bins = null;
		int j = 0;
		for (Planche p : solutionVoisine.getPlanches()) {
			bins = algoPl.placementBis(p.getComposition().getCompoPlanche(), j);
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
	 * Démarre le recuit simulé. Implémente le coeur de l'algorithme du recuit
	 * simulé commun à tous les problèmes.
	 */
	public void lancer() {
		Solution solutionVoisine;
		double delta;
		AlgoPlacement algoPl = new AlgoPlacement(data);
		boolean test = testPlacement(algoPl, solutionCourante);
		if (test) {
			// Initilaisation du recuit
			initialiserTemperature();
			meilleureValeur = solutionCourante.fonctionObjectif();
			meilleureSolution = solutionCourante.clone();
			test = testPlacement(algoPl, meilleureSolution);
			// while (!testSolTrouver()) {
			// Les paliers de température
			while (testerCondition1()) {
				// Les itérations par palier
				while (testerCondition2()) {
					// On cherche une solution dans le voisinage
					solutionVoisine = voisin();

					test = testPlacement(algoPl, solutionVoisine);

					if (test) {

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
							// trouvée pour le moment
							// Elle devient la meilleure solution
							if (solutionCourante.fonctionObjectif() < meilleureValeur) {
								meilleureValeur = solutionCourante
										.fonctionObjectif();
								meilleureSolution = solutionCourante.clone();
							}
						}
						// Si la solution voisine n'améliore pas la solution
						// courante
						// Elle peut être accepté
						else {
							double p = Math.random();
							if (p <= Math.exp(-delta / temperature)) {
								solutionCourante = solutionVoisine.clone();
							}
						}
					}
					// afficherSolution(solutionCourante,
					// "Solution courante");
				}
				decroitreTemperature();
			}
			// temperature = INIT_TEMP;
			// }
			afficherSolution(meilleureSolution, "Meilleur solution");
			// afficherBins(algoPl.getListBins());
			testPlacement(algoPl, meilleureSolution);
			Map<Integer, List<Bin>> map = buildPlacement(algoPl,
					meilleureSolution);
			FenetrePattern f = new FenetrePattern(data, map,
					meilleureSolution.quantites(),
					meilleureSolution.getPrixTotal(),
					meilleureSolution.getPlanches());
			afficherBins(algoPl.getListBins());
			afficherSolution(meilleureSolution, "Meilleur");
		}
	}

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
		qte.append("Quantité ==> ");
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
	 * Teste si le recuit est arrivé la température finale demandée à la
	 * création.
	 * 
	 * @return true si le recuit peut passer au palier de température suivant,
	 *         false sinon.
	 */
	protected boolean testerCondition1() {
		System.out.println("Température : " + temperature);
		if (temperature > temperatureFinale) {
			return true;
		} else {
			return false;
		}
	}

	private boolean testSolTrouver() {
		for (Planche p : meilleureSolution.getPlanches()) {
			if (p.getQuantite() < 0) {
				System.out.println("Pas de solution trouvée...On relance");
				return false;
			}
		}
		return true;
	}

	/**
	 * Teste si le recuit a encore des itérations à faire pour un palier de
	 * température.
	 * 
	 * @return true si le recuit doit continuer à ce palier de température,
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
	 * Initialise la température du recuit. La température initiale doit
	 * accepter un certain nombre de solutions coûteuses. Ce taux est fixé par
	 * l'utilisateur.
	 */
	private void initialiserTemperature() {
		Solution solutionVoisine;
		AlgoPlacement algoPl = new AlgoPlacement(data);
		double taux = 0;
		temperature = 1000;

		// Tant que le taux d'acceptation n'est pas celui voulu
		// On multiplie la température par 2
		do {
			int nbCouteuses = 0;
			int nbCouteusesAcceptees = 0;

			// On fait 50 tirages de solutions voisines
			for (int i = 0; i < 50; i++) {
				solutionVoisine = voisin();
				boolean test = testPlacement(algoPl, solutionVoisine);
				if (test) {
					// Si on a une solution coûteuse, on regarde si elle est
					// acceptée
					if (solutionCourante.deltaF(solutionVoisine) >= 0) {
						this.solutionCourante = solutionVoisine.clone();
						double p = Math.random();
						nbCouteuses++;

						if (p <= Math.exp(-solutionCourante
								.deltaF(solutionVoisine) / temperature)) {
							nbCouteusesAcceptees++;
						}
					}
				}
			}

			taux = (double) nbCouteusesAcceptees / (double) nbCouteuses;
			if (taux < probabiliteAcceptation) {
				temperature *= 2;
			}
		} while (taux < probabiliteAcceptation);
	}

	/**
	 * Décroit la température du recuit. La fonction utilisée est : f(t) = alpha
	 * x t avec alpha fixée par l'utilisateur.
	 */
	private void decroitreTemperature() {
		temperature *= facteurDecroissance;
	}

	/**
	 * Sélectionne une solution dans le voisinage de la solution courante. Dans
	 * notre cas, une solution voisine est une solution dont on décalle le
	 * nombre d'image à l'image suivante et on ajoute 1 une des images.
	 * 
	 * @return une solution voisine de la solution courante.
	 */
	protected Solution voisin() {
		Solution voisin = solutionCourante.clone();
		List<Planche> lastPlanches = solutionCourante.getPlanches();
		List<Planche> newsPlanches = new ArrayList<Planche>();
		for (Planche p : lastPlanches) {
			Random rand = new Random();
			int[] compoPlanche = null;
			compoPlanche = p.getComposition().getCompoPlanche();
			int nombreAleatoire = rand.nextInt(compoPlanche.length);
			int nombreAleatoire2 = rand.nextInt(compoPlanche.length);
			int[] newCompo = new int[compoPlanche.length];

			int premiereImageNb = compoPlanche[0];
			for (int i = 0; i < compoPlanche.length - 1; i++) {
				newCompo[i] = compoPlanche[i + 1];
			}
			newCompo[compoPlanche.length - 1] = premiereImageNb;
			if (newCompo[nombreAleatoire] < data.getImage(nombreAleatoire)
					.getQuantite()) {
				newCompo[nombreAleatoire] += 1;
			}
			if (newCompo[nombreAleatoire2] > 1) {
				newCompo[nombreAleatoire2] = 1;
			}
			Composition c = new Composition(newCompo);
			Planche pl = new Planche(p.getPrix(), p.getDimension(), p.getId(),
					p.getQuantite(), c);
			newsPlanches.add(pl);
		}
		voisin.setPlanches(newsPlanches);
		return voisin;
	};

	public Solution getMeilleureSolution() {
		return meilleureSolution;
	}
}

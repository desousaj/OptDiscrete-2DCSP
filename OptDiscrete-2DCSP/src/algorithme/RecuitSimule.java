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
import execute.Execute;
import graphique.FenetrePattern;

public class RecuitSimule {

	private static final int INIT_TEMP = 1000;
	public static double FACTEUR_DECROISSANCE = 0.9;
	public static double TAUX_ACCEPTATION = 0.8;
	public static int NB_ITERATIONS_PAR_PALIER = 10000;
	public static double TEMPERATURE_FINALE = 0.5;

	protected Data data;

	private AlgoPlacement algoPlacement;

	/** La solution courante du recuit simulé. */
	protected Solution solutionCourante;
	/** La meilleure solution trouvée pour le moment. */
	protected Solution meilleureSolution;
	/** Le facteur de décroissance de la température du recuit. */
	protected double facteurDecroissance;
	/** La température du recuit. */
	protected double temperature;
	/** La température à atteindre pour arreter le recuit. */
	private double temperatureFinale = TEMPERATURE_FINALE;
	/** La valeur de la meilleure solution trouvée. */
	protected double meilleureValeur;
	/**
	 * Le taux d'acceptation de solutions couteuses acceptées par le recuit à
	 * la température initiale.
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
		this.algoPlacement = new AlgoPlacement(data);
		this.facteurDecroissance = facteurDecroissance;
		this.probabiliteAcceptation = tauxAcceptation;
//		this.solutionCourante = new Solution(data);
		this.initSolutionInitial();
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
		this.algoPlacement = new AlgoPlacement(data);
		this.facteurDecroissance = FACTEUR_DECROISSANCE;
		this.probabiliteAcceptation = TAUX_ACCEPTATION;
//		this.solutionCourante = new Solution(data);
		this.initSolutionInitial();
		this.nbIterationsParPalier = NB_ITERATIONS_PAR_PALIER;
		this.temperatureFinale = TEMPERATURE_FINALE;
		this.temperature = INIT_TEMP;
	}
	
	public void initSolutionInitial(){
		int[][] temp_sol = new int[Execute.NB_PATTERNS][this.data.getNbImages()];
		int[] temp_compo;
		int numRandomPattern;
		Solution solution = new Solution(data);
		Random rand;
		
		// Tente de trouver une solution initiale au maximum 2Milliards de fois
		for (int n = 0; n < Integer.MAX_VALUE; n++){
			// Prepare les valeurs d'une solution
			for (int i = 0; i < this.data.getNbImages(); i++){
				rand = new Random();
				numRandomPattern = rand.nextInt((Execute.NB_PATTERNS)); // donne un nb entre 0 et NB_PATTERNS-1
				for (int j = 0; j < Execute.NB_PATTERNS; j++){
					temp_sol[j][i] = numRandomPattern == j ? 1 : 0;
				}
			}
			
			// Creer une solution avec les valeurs precedement trouve
			solution = new Solution(data);
			for (int i = 0; i < Execute.NB_PATTERNS; i++){
				temp_compo = new int[this.data.getNbImages()];
				for (int j = 0; j < this.data.getNbImages(); j++){
					temp_compo[j] = temp_sol[i][j];
				}
				solution.getPlanches().get(i).setComposition(new Composition(temp_compo));
			}
			
			// Verifie la possibilit� de cette solution
			if (this.testPlacement(solution)){
				System.out.println("solution inital trouve");
				this.solutionCourante = solution;
				break;
			}
		}
		
		// Si le random n'a donne aucune possiblite reel
		if (this.solutionCourante == null){
			System.out.println("solution initial realisable non trouve -> derniere solution random");
			this.solutionCourante = solution; //Met le dernier random dans la solution initial
		}
		
		//Affichage solution de base - Test - TODO delete this
//		int i = 0;
//		for (Planche p : solution.getPlanches()){
//			System.out.println("Planche " + i +
//					"\n" +p.toString());
//			i++;
//		}
		
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
	 * Démarre le recuit simulé. Implémente le coeur de l'algorithme du
	 * recuit simulé commun à tous les problèmes.
	 */
	public void lancer() {
		Solution solutionVoisine;
		double delta;
		AlgoPlacement algoPl = new AlgoPlacement(data);
		// Initilaisation du recuit
		// initialiserTemperature();
		meilleureValeur = solutionCourante.fonctionObjectif();
		meilleureSolution = solutionCourante.clone();
		// Les paliers de température
		while (testerCondition1()) {
			// Les itérations par palier
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
					// trouvée pour le moment
					// Elle devient la meilleure solution
					if (solutionCourante.fonctionObjectif() < meilleureValeur) {
						meilleureValeur = solutionCourante.fonctionObjectif();
						meilleureSolution = solutionCourante.clone();
					}
				}
				// Si la solution voisine n'améliore pas la solution
				// courante
				// Elle peut être acceptée
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
		afficherSolution(meilleureSolution, "Meilleur solution");
		// afficherBins(algoPl.getListBins());
		// testPlacement(meilleureSolution);
		Map<Integer, List<Bin>> map = buildPlacement(meilleureSolution);
		FenetrePattern f = new FenetrePattern(data, map,
				meilleureSolution.quantites(),
				meilleureSolution.getPrixTotal(),
				meilleureSolution.getPlanches());
		afficherBins(this.algoPlacement.getListBins());
		afficherSolution(meilleureSolution, "Meilleur");
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
	 * @return true si le recuit peut passer au palier de temp�rature suivant,
	 *         false sinon.
	 */
	protected boolean testerCondition1() {
		System.out.println("Temperature : " + temperature);
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
	 * accepter un certain nombre de solutions couteuses. Ce taux est fixé par
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
				boolean test = testPlacement(solutionVoisine);
				if (test) {
					// Si on a une solution couteuse, on regarde si elle est
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
	 * Décroit la température du recuit. La fonction utilisée est : f(t) =
	 * alpha x t avec alpha fixée par l'utilisateur.
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
		Solution voisin = null;
		boolean test = false;

		while (!test) {
			voisin = solutionCourante.clone();
			List<Planche> lastPlanches = solutionCourante.getPlanches();
			List<Planche> newsPlanches = new ArrayList<Planche>();
			int nbImages = lastPlanches.get(0).getComposition()
					.getCompoPlanche().length;

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
					newCompo[nombreAleatoire2] = 0;
				}
				Composition c = new Composition(newCompo);
				Planche pl = new Planche(p.getPrix(), p.getDimension(),
						p.getId(), p.getQuantite(), c);
				newsPlanches.add(pl);
			}
			// On regarde maintenant si toute les images sont plac�es au moins
			// une
			// fois sur n'importe quelle pattern
			int[] imageQuantity = new int[lastPlanches.get(0).getComposition()
					.getCompoPlanche().length];
			for (Planche p : newsPlanches) {
				for (int i = 0; i < nbImages; i++) {
					imageQuantity[i] = imageQuantity[i]
							+ p.getComposition().getCompoPlanche()[i];
				}
			}
			int cpt = 0;
			for (int i = 0; i < nbImages; i++) {
				if (imageQuantity[i] <= 0) {
					newsPlanches.get(cpt).getComposition().getCompoPlanche()[i] = 1;
				}
				cpt++;
				if (cpt % newsPlanches.size() == 0) {
					cpt = 0;
				}
			}

			voisin.setPlanches(newsPlanches);
			test = testPlacement(voisin);
		}
		return voisin;
	};

	public Solution getMeilleureSolution() {
		return meilleureSolution;
	}
}

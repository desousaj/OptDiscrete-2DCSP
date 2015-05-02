package algorithme;

/**
 * Classe contenant la solution d'un problème
 * 
 * 
 */
public abstract class SolutionAbstract {

	/**
	 * Retourne la différence entre deux solutions. Une valeur négative si le
	 * paramètre est meilleur que la solution courante.
	 * 
	 * @param solution
	 *            la solution à comparer à la solution courante.
	 * @return la différence entre la solution courante et la solution en
	 *         argument : solution - solution courante
	 */
	public abstract double deltaF(Solution solution);

	/**
	 * Retourne la valeur de la fonction objectif du problème.
	 * 
	 * @return retourne la valeur de la solution avec la fonction objectif du
	 *         problème.
	 */
	public abstract double fonctionObjectif();

}
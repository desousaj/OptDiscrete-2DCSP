package algorithme;

/**
 * Classe contenant la solution d'un probleme
 * 
 * 
 */
public abstract class SolutionAbstract{

	/**
	 * Retourne la difference entre deux solutions. Une valeur negative si le
	 * parametre est meilleur que la solution courante.
	 * 
	 * @param solution
	 *            la solution à comparer à la solution courante.
	 * @return la difference entre la solution courante et la solution en
	 *         argument : solution - solution courante
	 */
	public abstract double deltaF(Solution solution);

	/**
	 * Retourne la valeur de la fonction objectif du probleme.
	 * 
	 * @return retourne la valeur de la solution avec la fonction objectif du
	 *         probleme.
	 */
	public abstract double fonctionObjectif();

}
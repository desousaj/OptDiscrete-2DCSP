package entites;

import java.io.Serializable;

public class Coordonnees implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int abscisse;
	private int ordonnee;

	public Coordonnees(int abscisse, int ordonnee) {
		super();
		this.abscisse = abscisse;
		this.ordonnee = ordonnee;
	}

	public int getAbscisse() {
		return abscisse;
	}

	public void setAbscisse(int abscisse) {
		this.abscisse = abscisse;
	}

	public int getOrdonnee() {
		return ordonnee;
	}

	public void setOrdonnee(int ordonnee) {
		this.ordonnee = ordonnee;
	}

	@Override
	public String toString() {
		return "Coordonnées : x=" + abscisse + " et y=" + ordonnee + "\n";
	}

}

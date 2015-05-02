package entites;

import java.io.Serializable;

public class Dimension implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double largeur;
	private double hauteur;

	public Dimension(double largeur, double hauteur) {
		super();
		this.largeur = largeur;
		this.hauteur = hauteur;
	}

	public double getLargeur() {
		return largeur;
	}

	public void setLargeur(double largeur) {
		this.largeur = largeur;
	}

	public double getHauteur() {
		return hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public double calculeAire() {
		return hauteur * largeur;
	}

	@Override
	public String toString() {
		return "Dimension : largeur=" + largeur + " et hauteur=" + hauteur
				+ ", volume=" + calculeAire();
	}

}

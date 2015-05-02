package entites;

import java.awt.Color;

public class Image {
	private Dimension dimension;
	private Color couleur;
	private Coordonnees coordonnees;
	private int quantite;
	private int indice;
	private boolean isRotate;

	public Image(int indice, Dimension dimension, Color couleur,
			Coordonnees coordonnees) {
		super();
		this.setIndice(indice);
		this.dimension = dimension;
		this.couleur = couleur;
		this.coordonnees = coordonnees;
		this.isRotate = false;
	}

	public Image(int indice, Dimension dimension, Color couleur, int quantite) {
		super();
		this.indice = indice;
		this.dimension = dimension;
		this.couleur = couleur;
		this.quantite = quantite;
		this.coordonnees = null;
		this.isRotate = false;
	}

	public int calculMaxImages(Planche planche) {
		double airePlanche = planche.getDimension().calculeAire();
		double aireImg = dimension.calculeAire();
		return (int) (airePlanche / aireImg);
	}

	public Dimension getDimension() {
		if (isRotate) {
			return new Dimension(dimension.getHauteur(), dimension.getLargeur());
		} else {
			return dimension;
		}
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public Coordonnees getCoordonnees() {
		return coordonnees;
	}

	public void setCoordonnees(Coordonnees coordonnees) {
		this.coordonnees = coordonnees;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public double calculAire() {
		return dimension.calculeAire();
	}

	@Override
	public String toString() {
		String txt = "Image : \n ";
		if (dimension != null) {
			txt = txt.concat("\t" + dimension.toString() + "\n");
		}
		if (couleur != null) {
			txt = txt.concat("\t Couleur :" + couleur.toString() + "\n");
		}
		txt = txt.concat("\t Quantité :" + quantite + "\n");
		if (coordonnees != null) {
			txt = txt.concat("\t" + coordonnees.toString());
		}
		return txt;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public boolean isRotate() {
		return isRotate;
	}

	public void setRotate(boolean isRotate) {
		this.isRotate = isRotate;
	}

	@Override
	public Image clone() {
		return new Image(indice, dimension, couleur, coordonnees);
	}

}

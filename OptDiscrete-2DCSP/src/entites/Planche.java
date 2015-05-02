package entites;

import composition.Composition;

public class Planche {
	private Dimension dimension;
	private double prix;
	private Composition composition;
	// Nombre de fois qu'on imprime la planche
	private int quantite;
	private int id;

	public Planche() {

	}

	public Planche(Dimension dimension, double prix, int nbImages, int quantite) {
		super();
		this.dimension = dimension;
		this.prix = prix;
		this.composition = new Composition(nbImages);
		this.quantite = quantite;
	}

	public Planche(double prix, Dimension dimension, int id, int quantite,
			Composition composition) {
		this.composition = composition;
		this.dimension = dimension;
		this.id = id;
		this.prix = prix;
		this.quantite = quantite;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	protected int calculMaxImages(Image img) {
		double airePlanche = dimension.calculeAire();
		double aireImg = img.getDimension().calculeAire();
		return (int) (airePlanche / aireImg);
	}

	public double calculAire() {
		return dimension.calculeAire();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Planche : \n \t" + dimension.toString() + "\n \t Prix : "
				+ prix + "\n \t" + "\n \t Quantite : " + quantite + "\n \t");
		if (composition != null) {
			s.append("Composition : " + composition.toString());
		}
		return s.toString();
	}

	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	@Override
	public Planche clone() {
		Planche p = new Planche();
		p.setComposition(composition.clone());
		p.setDimension(dimension);
		p.setId(id);
		p.setPrix(prix);
		p.setQuantite(quantite);
		return p;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

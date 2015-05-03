package entites;

public class Bin {

	public static int NB_BIN = 0;
	// Bin est possible
	private boolean isOk;
	// Numéro du bin
	private int idBin;
	// Dimension de ce bin
	private Dimension dimension;
	// Numéro du pattern correspondant
	private int numPatern;
	// Coordonnées de ce bin sur son père. La coordonnée (0,0) correspond au bin
	// de base.
	private Coordonnees coordonnees;
	// 1er Bin après la découpe
	private int idBin1;
	// 2eme Bin après la découpe
	private int idBin2;

	private int idImage;

	private boolean imageIsRotate;
	private Dimension dimImage;

	public Dimension getDimImage() {
		return dimImage;
	}

	public void setDimImage(Dimension dimImage) {
		this.dimImage = dimImage;
	}

	public boolean isImageIsRotate() {
		return imageIsRotate;
	}

	public void setImageIsRotate(boolean imageIsRotate) {
		this.imageIsRotate = imageIsRotate;
	}

	public int getIdImage() {
		return idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	public Bin() {
		coordonnees = new Coordonnees(0, 0);
		isOk = true;
		idBin = NB_BIN;
		idImage = -1;
		NB_BIN++;
		imageIsRotate = false;
	}

	// @Override
	// public String toString() {
	// return "Bin [isOk=" + isOk + ", idBin=" + idBin + ", dimension="
	// + dimension + ", numPatern=" + numPatern + ", coordonnees="
	// + coordonnees + ", bin1=" + idBin1 + ", bin2=" + idBin2 + "]";
	// }

	@Override
	public String toString() {
		return "Bin [dimension=" + dimension + ", numPatern=" + numPatern
				+ ", coordonnees=" + coordonnees + ", IdImage=" + idImage + "]";
	}

	public int calculAire() {
		return (int) dimension.calculeAire();
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public int getIdBin() {
		return idBin;
	}

	public void setIdBin(int idBin) {
		this.idBin = idBin;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Coordonnees getCoordonnees() {
		return coordonnees;
	}

	public void setCoordonnees(Coordonnees coordonnees) {
		this.coordonnees = coordonnees;
	}

	public int getIdBin1() {
		return idBin1;
	}

	public void setIdBin1(int bin1) {
		this.idBin1 = bin1;
	}

	public int getIdBin2() {
		return idBin2;
	}

	public void setIdBin2(int bin2) {
		this.idBin2 = bin2;
	}

	public int getNumPatern() {
		return numPatern;
	}

	public void setNumPatern(int numPatern) {
		this.numPatern = numPatern;
	}

}

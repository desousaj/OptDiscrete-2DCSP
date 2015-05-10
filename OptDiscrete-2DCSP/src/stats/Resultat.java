package stats;

import java.io.Serializable;

public class Resultat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double prixTotal;
	private float time;
	private int nbPlanches;
	private double tempFinale;
	private double tempInitiale;
	private int nbIteration;
	private double factDecr;
	private String nameData;

	public Resultat(double d, float seconds, int nbPlanches, double tempFinale,
			double tempInitiale, int nbIteration, double factDecr,
			String nameData) {
		super();
		this.prixTotal = d;
		this.time = seconds;
		this.nbPlanches = nbPlanches;
		this.tempFinale = tempFinale;
		this.tempInitiale = tempInitiale;
		this.nbIteration = nbIteration;
		this.factDecr = factDecr;
		this.nameData = nameData;
	}

	public double getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(double prixTotal) {
		this.prixTotal = prixTotal;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public void setPrixTotal(int prixTotal) {
		this.prixTotal = prixTotal;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getNbPlanches() {
		return nbPlanches;
	}

	public void setNbPlanches(int nbPlanches) {
		this.nbPlanches = nbPlanches;
	}

	public double getTempFinale() {
		return tempFinale;
	}

	public void setTempFinale(double tempFinale) {
		this.tempFinale = tempFinale;
	}

	public double getTempInitiale() {
		return tempInitiale;
	}

	public void setTempInitiale(double tempInitiale) {
		this.tempInitiale = tempInitiale;
	}

	public int getNbIteration() {
		return nbIteration;
	}

	public void setNbIteration(int nbIteration) {
		this.nbIteration = nbIteration;
	}

	public double getFactDecr() {
		return factDecr;
	}

	public void setFactDecr(double factDecr) {
		this.factDecr = factDecr;
	}

	public String getNameData() {
		return nameData;
	}

	public void setNameData(String nameData) {
		this.nameData = nameData;
	}

	@Override
	public String toString() {
		return "Resultat [prixTotal=" + prixTotal + ", time=" + time
				+ ", nbPlanches=" + nbPlanches + ", tempFinale=" + tempFinale
				+ ", tempInitiale=" + tempInitiale + ", nbIteration="
				+ nbIteration + ", factDecr=" + factDecr + ", nameData="
				+ nameData + "]";
	}

}

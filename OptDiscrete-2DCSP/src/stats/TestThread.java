package stats;

import java.util.concurrent.Callable;

import parse.Data;
import parse.ParseData;
import algorithme.RecuitSimule;

public class TestThread implements Callable<Resultat> {

	private double tempFinale;
	private double tempInitiale;
	private int nbIteration;
	private double factDecr;
	private String nameData;

	public TestThread(double tempFinale, double tempInitiale, int nbIteration,
			double factDecr, String nameData) {
		super();
		this.tempFinale = tempFinale;
		this.tempInitiale = tempInitiale;
		this.nbIteration = nbIteration;
		this.factDecr = factDecr;
		this.nameData = nameData;
	}

	@Override
	public Resultat call() throws Exception {
		ParseData p = new ParseData();
		Data d = p.buildData(nameData);
		RecuitSimule r = new RecuitSimule(d, tempFinale, tempInitiale,
				nbIteration, factDecr);
		long tempsDebut = System.currentTimeMillis();
		r.lancer();
		long tempsFin = System.currentTimeMillis();
		float seconds = (tempsFin - tempsDebut) / 1000F;
		System.out.println("Opération effectuée en: " + Float.toString(seconds)
				+ " secondes.");
		return new Resultat(r.getMeilleureSolution().getPrixTotal(), seconds,
				Execute.NB_PATTERNS, tempFinale, tempInitiale, nbIteration,
				factDecr, nameData);
	}

}

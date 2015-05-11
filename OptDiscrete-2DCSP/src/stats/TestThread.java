package stats;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import parse.Data;
import parse.ParseData;
import algorithme.RecuitSimule;
import exception.MonException;

public class TestThread implements Callable<Resultat> {

	private double tempFinale;
	private double tempInitiale;
	private int nbIteration;
	private double factDecr;
	private String nameData;
	private String path;

	public TestThread(double tempFinale, double tempInitiale, int nbIteration,
			double factDecr, String nameData, String path) {
		super();
		this.tempFinale = tempFinale;
		this.tempInitiale = tempInitiale;
		this.nbIteration = nbIteration;
		this.factDecr = factDecr;
		this.nameData = nameData;
		this.path = path;
	}

	@SuppressWarnings("static-access")
	@Override
	public Resultat call() {
		ParseData p = new ParseData();
		Data d;
		try {
			d = p.buildData(path);
			if (d.testIsPossible()) {
				long tempsDebut = 0;
				RecuitSimule r = null;
				try {
					r = new RecuitSimule(d, tempFinale, tempInitiale,
							nbIteration, factDecr);
					tempsDebut = System.currentTimeMillis();
					r.lancer();
				} catch (MonException e) {
					System.out.println("Unable to find an initial solution...");
					// Boîte du message d'erreur
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(null,
							"Unable to find an initial solution... :(",
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
				long tempsFin = System.currentTimeMillis();
				float seconds = (tempsFin - tempsDebut) / 1000F;
				System.out.println("Opération effectuée en: "
						+ Float.toString(seconds) + " secondes.");
				return new Resultat(r.getMeilleureSolution().getPrixTotal(),
						seconds, Execute.NB_PATTERNS, tempFinale, tempInitiale,
						nbIteration, factDecr, nameData);
			} else {
				JOptionPane jop = new JOptionPane();
				jop.showMessageDialog(
						null,
						"La taille des images est supérieur aux patterns disponibles.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} catch (IOException e) {
			System.out
					.println("Unable to parse data. Veuillez selectionner un fichier valide...");
			// Boîte du message d'erreur
			JOptionPane jop = new JOptionPane();
			jop.showMessageDialog(
					null,
					"Unable to parse data. Veuillez selectionner un fichier valide...",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

}

package graphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import parse.Data;
import entites.Bin;
import entites.Planche;

public class FenetrePattern extends JFrame {
	private static final int HAUTEUR_FENETRE = 900;
	private Map<Integer, List<Bin>> bins;
	private Data data;
	private List<Integer> quantites;
	private int nbPatterns;
	private double prixTotal;
	private List<Planche> solutions;

	public FenetrePattern(Data data, Map<Integer, List<Bin>> bins,
			List<Integer> quantites, double prixTotal, List<Planche> solutions) {
		this.bins = bins;
		this.data = data;
		nbPatterns = bins.size();
		this.quantites = quantites;
		this.prixTotal = prixTotal;
		this.solutions = solutions;
		initFrame();
	}

	// public static void main(String[] args) {
	// Map<Integer, List<Bin>> map = new HashMap<Integer, List<Bin>>();
	// map.put(0, new ArrayList<Bin>());
	// map.put(1, new ArrayList<Bin>());
	// map.put(2, new ArrayList<Bin>());
	// List<Integer> quantites = new ArrayList<Integer>();
	// quantites.add(53);
	// quantites.add(42);
	// quantites.add(9);
	// new FenetrePattern(null, map, quantites);
	// }

	private void initFrame() {
		this.setTitle("Meilleure solution");
		int hauteurFenetre = HAUTEUR_FENETRE;
		int largeurFenetre;
		double largeurBin = data.getPlanche().getDimension().getLargeur();
		double hauteurBin = data.getPlanche().getDimension().getHauteur();
		int nbPattern = quantites.size();
		double ratio = hauteurFenetre / hauteurBin;
		largeurFenetre = (int) Math.floor(ratio * largeurBin * nbPattern);

		this.setSize(largeurFenetre, hauteurFenetre);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initPanneaux();
		this.setVisible(true);

	}

	public void initPanneaux() {
		boolean solutionNotFind = false;
		for (int i : quantites) {
			if (i < 0) {
				solutionNotFind = true;
				break;
			}
		}

		setLayout(new BorderLayout());
		JPanel prixTotalPanel = new JPanel();
		prixTotalPanel.setLayout(new GridLayout(1, 1));
		JLabel prixTotalLabel;
		if (solutionNotFind) {
			prixTotalLabel = new JLabel(
					"Nous n'avons pas pu trouver de solution, veuillez relancer le test !");
		} else {
			prixTotalLabel = new JLabel("Prix total : " + this.prixTotal);
		}
		prixTotalPanel.add(prixTotalLabel);
		add(prixTotalPanel, BorderLayout.NORTH);

		// Ajoute les patterns
		JPanel patterns = new JPanel();
		patterns.setLayout(new GridLayout(1, nbPatterns));
		for (int i : bins.keySet()) {
			patterns.add(new PanelPattern(bins.get(i), data));
			System.out.println(bins.get(i));
		}
		// patterns.setBackground(Color.BLUE);
		patterns.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		add(patterns, BorderLayout.CENTER);

		// Ajoute les libelles en bas
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(3, nbPatterns));
		labels.setBackground(Color.YELLOW);
		labels.setBorder(BorderFactory.createLineBorder(Color.RED));
		for (int i : bins.keySet()) {
			JLabel numPattern = new JLabel("Pattern n°" + i);
			labels.add(numPattern);
		}
		for (int i : bins.keySet()) {
			JLabel quantite;
			if (solutionNotFind) {
				quantite = new JLabel(
						"Quantite impossible à calculer car des images ne sont pas placee");
			} else {
				quantite = new JLabel("Quantite : " + quantites.get(i));
			}
			labels.add(quantite);
		}
		for (int i : bins.keySet()) {
			StringBuilder s = new StringBuilder();
			int[] compo = solutions.get(i).getComposition().getCompoPlanche();
			s.append("Solution = [ " + compo[0]);
			for (int ind = 1; ind < compo.length; ind++) {
				s.append(", " + compo[ind]);
			}
			s.append(" ]");
			JLabel solution = new JLabel(s.toString());
			labels.add(solution);
		}

		add(labels, BorderLayout.SOUTH);

	}

}

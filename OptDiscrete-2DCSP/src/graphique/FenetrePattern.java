package graphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import parse.Data;
import entites.Bin;
import entites.Planche;

public class FenetrePattern extends JFrame {

	public static final int FONT_SIZE = 15;
	public static final String FONT = "TimesRoman";

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

	private void initFrame() {
		this.setTitle("Meilleure solution");

		// Applique les proportions du pattern à la fenêtre en gardant une
		// hauteur de fenêtre maximale
		int hauteurFenetre = Toolkit.getDefaultToolkit().getScreenSize().height;
		int largeurMax = Toolkit.getDefaultToolkit().getScreenSize().width;
		int largeurFenetre;
		double largeurBin = data.getPlanche().getDimension().getLargeur();
		double hauteurBin = data.getPlanche().getDimension().getHauteur();
		double ratio = hauteurFenetre / hauteurBin;
		largeurFenetre = (int) Math.floor(ratio * largeurBin);
		while (largeurFenetre > largeurMax) {
			hauteurFenetre = hauteurFenetre - 10;
			ratio = hauteurFenetre / hauteurBin;
			largeurFenetre = (int) Math.floor(ratio * largeurBin);
		}

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
		JLabel prixTotalLabel;
		if (solutionNotFind) {
			prixTotalLabel = new JLabel(
					"Nous n'avons pas pu trouver de solution, veuillez relancer le test !");
		} else {
			prixTotalLabel = new JLabel(" Prix total : " + this.prixTotal + " ");
		}
		prixTotalPanel.setBackground(Color.WHITE);

		prixTotalLabel.setFont(new Font(FONT, Font.PLAIN, FONT_SIZE));

		prixTotalPanel.add(prixTotalLabel);
		add(prixTotalPanel, BorderLayout.NORTH);

		// Ajoute les patterns
		JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);
		// patterns.setLayout(new GridLayout(1, nbPatterns));
		for (int i : bins.keySet()) {
			JPanel onglet = new JPanel();
			onglet.setLayout(new BorderLayout());
			onglet.setPreferredSize(this.getPreferredSize());
			onglet.add(new PanelPattern(bins.get(i), data), BorderLayout.CENTER);
			onglets.addTab("Pattern " + i, onglet);

			// Ajoute les libelles en bas
			JPanel labels = new JPanel();
			labels.setLayout(new GridLayout(3, nbPatterns));
			labels.setBackground(Color.WHITE);
			JLabel numPattern = new JLabel(" Pattern n°" + i + " ");
			// Set the label's font size to the newly determined size.
			numPattern.setFont(new Font(FONT, Font.PLAIN, FONT_SIZE));
			labels.add(numPattern);
			JLabel quantite;
			if (solutionNotFind) {
				quantite = new JLabel(
						" Quantite impossible à calculer car des images ne sont pas placee ");
			} else {
				quantite = new JLabel(" Quantite : " + quantites.get(i) + " ");
			}
			quantite.setFont(new Font(FONT, Font.PLAIN, FONT_SIZE));

			labels.add(quantite);
			StringBuilder s = new StringBuilder();
			int[] compo = solutions.get(i).getComposition().getCompoPlanche();
			s.append(" Solution = [ " + compo[0]);
			for (int ind = 1; ind < compo.length; ind++) {
				s.append(", " + compo[ind]);
			}
			s.append(" ] ");
			JLabel solution = new JLabel(s.toString());
			solution.setFont(new Font(FONT, Font.PLAIN, FONT_SIZE));

			labels.add(solution);
			labels.setBorder(BorderFactory.createLineBorder(Color.RED));

			onglet.add(labels, BorderLayout.SOUTH);

		}

		onglets.setOpaque(true);
		add(onglets, BorderLayout.CENTER);

	}

}

package graphique;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parse.Data;
import parse.ParseData;
import algorithme.RecuitSimule;
import execute.Execute;

public class FenetreMenu extends JFrame implements ActionListener {

	private JButton ouvrirFichier = new JButton("SÃ©lectionner un fichier");
	private JTextField infosFichier = new JTextField("");
	private JComboBox<String> comboNbPatterns;
	private JLabel comboLabelPatterns = new JLabel("Nombre de Patterns");
	private JButton lancerSimu = new JButton("Lancer la simulation");

	public FenetreMenu() {
		this.setTitle("2D Cutting Stock Problem with Setup Cost");
		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panNbPatterns = new JPanel();
		String[] tabNbPatterns = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"10" };
		comboNbPatterns = new JComboBox<String>(tabNbPatterns);
		panNbPatterns.add(comboLabelPatterns);
		panNbPatterns.add(comboNbPatterns);
		this.add(panNbPatterns, BorderLayout.NORTH);

		ouvrirFichier.addActionListener(this);
		JPanel panelFichier = new JPanel();
		BorderLayout bord = new BorderLayout();
		panelFichier.setLayout(bord);
		panelFichier.add("South", infosFichier);
		panelFichier.add("Center", ouvrirFichier);
		this.add(panelFichier, BorderLayout.CENTER);

		lancerSimu.addActionListener(this);
		JPanel panelSimul = new JPanel();
		panelSimul.add(lancerSimu);
		this.add(panelSimul, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == ouvrirFichier) {
			JFileChooser chooser = new JFileChooser(".");
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				infosFichier.setText(chooser.getSelectedFile()
						.getAbsolutePath());
			}
		}
		if (source == lancerSimu) {
			Execute.NB_PATTERNS = comboNbPatterns.getSelectedIndex() + 1;
			ParseData parseData = new ParseData();
			Data d;
			try {
				d = parseData.buildData(infosFichier.getText());
				if (d.testIsPossible()) {
					RecuitSimule rs = new RecuitSimule(d);
					rs.lancer();
				} else {
					JOptionPane jop = new JOptionPane();
					jop.showMessageDialog(
							null,
							"La taille des images est supérieur aux patterns disponibles.",
							"Erreur", JOptionPane.ERROR_MESSAGE);
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

		}
	}

}

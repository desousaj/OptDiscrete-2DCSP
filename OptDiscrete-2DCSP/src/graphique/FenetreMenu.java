package graphique;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import stats.Execute;

public class FenetreMenu extends JFrame implements ActionListener {

	private JButton ouvrirFichier = new JButton("Sélectionner un fichier");
	private JTextField infosFichier = new JTextField("");
	private JComboBox<String> comboNbPatterns;
	private JComboBox<String> comboNbThreads;
	private JLabel comboLabelPatterns = new JLabel("Nombre de Patterns");
	private JButton lancerSimu = new JButton("Lancer la simulation");
	private JCheckBox multiThread;
	JLabel multiThreadLabel = new JLabel("Multi-Thread : ");

	public FenetreMenu() {
		this.setTitle("2D Cutting Stock Problem with Setup Cost");
		this.setSize(500, 200);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panNbPatterns = new JPanel();
		String[] tabNbPatterns = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"10" };
		String[] tabNbThread = { "2", "3", "4", "5" };
		comboNbPatterns = new JComboBox<String>(tabNbPatterns);
		comboNbThreads = new JComboBox<String>(tabNbThread);

		multiThread = new JCheckBox();
		panNbPatterns.add(comboLabelPatterns);
		panNbPatterns.add(comboNbPatterns);

		// Ajout des multi-thread
		JPanel multiThreadPanel = new JPanel();
		multiThreadPanel.setLayout(new GridLayout(1, 5));
		multiThreadPanel.add(multiThreadLabel);
		multiThreadPanel.add(multiThread);
		multiThreadPanel.add(comboNbThreads);
		panNbPatterns.add(multiThreadPanel);
		this.add(panNbPatterns, BorderLayout.NORTH);
		addActionListenerMultiThread();
		multiThread.setSelected(false);
		comboNbThreads.hide();

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

	public void addActionListenerMultiThread() {
		multiThread.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox cb = (JCheckBox) event.getSource();
				if (cb.isSelected()) {
					comboNbThreads.show();
				} else {
					comboNbThreads.hide();
				}
			}
		});
	}

	@SuppressWarnings("static-access")
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
			// Cas multi-thread : utilise les fonctions de stats
			if (multiThread.isSelected()) {
				Execute.NB_TEST_WITH_SAME_PARAMETERS = comboNbThreads
						.getSelectedIndex() + 2;
				System.out.println("---------Multi threads-----------");
			} else {
				// Cas 1 thread
				Execute.NB_TEST_WITH_SAME_PARAMETERS = 1;
				System.out.println("---------Single thread-----------");
			}
			Execute.genereData(infosFichier.getText());

		}
	}

}

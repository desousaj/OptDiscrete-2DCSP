package execute;

import graphique.FenetreMenu;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Execute {

	public static void main(String[] args) throws IOException {
		// Applique le thème windows
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | UnsupportedLookAndFeelException
				| IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		new FenetreMenu();
	}

}
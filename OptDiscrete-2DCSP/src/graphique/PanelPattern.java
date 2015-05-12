package graphique;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import parse.Data;
import entites.Bin;
import entites.Image;

public class PanelPattern extends JPanel {

	private List<Bin> bins;
	private Data data;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelPattern(List<Bin> bins, Data data) {
		this.bins = bins;
		this.data = data;
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setFont(new Font(FenetrePattern.FONT, Font.PLAIN,
				FenetrePattern.FONT_SIZE));
		double ratioLargeur = this.getWidth()
				/ data.getPlanche().getDimension().getLargeur();
		double ratioHauteur = this.getHeight()
				/ data.getPlanche().getDimension().getHauteur();
		for (Bin b : bins) {
			if (b.getIdImage() >= 0 && b.getCoordonnees() != null) {
				Image i = data.getImage(b.getIdImage());
				g.setColor(i.getCouleur());
				int x = (int) Math.floor(b.getCoordonnees().getAbscisse()
						* ratioLargeur);
				int y = (int) Math.floor(b.getCoordonnees().getOrdonnee()
						* ratioHauteur);
				int hauteur = (int) Math.floor(b.getDimImage().getHauteur()
						* ratioHauteur);
				int largeur = (int) Math.floor(b.getDimImage().getLargeur()
						* ratioLargeur);
				// if (b.isImageIsRotate()) {
				// hauteur = (int) Math.floor(i.getDimension().getLargeur()
				// * ratioLargeur);
				// largeur = (int) Math.floor(i.getDimension().getHauteur()
				// * ratioHauteur);
				// } else {
				// largeur = (int) Math.floor(i.getDimension().getLargeur()
				// * ratioLargeur);
				// hauteur = (int) Math.floor(i.getDimension().getHauteur()
				// * ratioHauteur);
				// }
				g.setColor(Color.RED);
				g.drawRect(x, y, largeur, hauteur);
				g.setColor(i.getCouleur());
				g.fillRect(x + 1, y + 1, largeur - 1, hauteur - 1);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(b.getIdImage()),
						(x + largeur / 2), (y + hauteur / 2));
			}
		}
	}
}
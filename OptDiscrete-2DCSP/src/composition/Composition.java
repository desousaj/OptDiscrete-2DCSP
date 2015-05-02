package composition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entites.Image;

/**
 * Représente la disposition des images présentes sur une planche.
 * 
 * @author Jérémy
 *
 */
public class Composition {
	// Nombre de fois que l'image (avec l'indice du tableau) sera placé sur la
	// planche
	private int[] compoPlanche;
	// Les images positionn�es sur la planche
	private List<Image> imagesPositionnees;

	public Composition(int nbImages) {
		super();
		initCompoPlanche(nbImages);
		imagesPositionnees = new ArrayList<Image>();
	}

	public Composition(int[] compoPlanche2) {
		this.compoPlanche = compoPlanche2;
		imagesPositionnees = new ArrayList<Image>();
	}

	/**
	 * Initialise la composition. Par d�fault, on place au moins une fois toute
	 * les images
	 * 
	 * @param nbImages
	 */
	private void initCompoPlanche(int nbImages) {
		compoPlanche = new int[nbImages];
		for (int i = 0; i < compoPlanche.length; i++) {
			compoPlanche[i] = 1;
		}
	}

	public int[] getCompoPlanche() {
		return compoPlanche;
	}

	public void setCompoPlanche(int[] compoPlanche) {
		this.compoPlanche = compoPlanche;
	}

	public List<Image> getImagesPositionnees() {
		return imagesPositionnees;
	}

	public void setImagesPositionnees(List<Image> imagesPositionnees) {
		this.imagesPositionnees = imagesPositionnees;
	}

	@Override
	public Composition clone() {
		Composition c = new Composition(compoPlanche.length);
		c.setCompoPlanche(compoPlanche);
		List<Image> images = new ArrayList<Image>();
		if (imagesPositionnees != null && !imagesPositionnees.isEmpty()) {
			for (Image i : imagesPositionnees) {
				images.add(i.clone());
			}
		}
		c.setImagesPositionnees(images);
		return c;
	}

	@Override
	public String toString() {
		return "Composition [compoPlanche=" + Arrays.toString(compoPlanche)
				+ ", imagesPositionnees=" + imagesPositionnees + "]";
	}

}

package parse;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import entites.Dimension;
import entites.Image;
import entites.Planche;

public class ParseData {
	private File dataFile;

	public File getData() {
		return dataFile;
	}

	public void setData(File data) {
		this.dataFile = data;
	}

	/**
	 * Construit l'objet Data à partir d'un fichier texte dont le chemin absolu
	 * est passé en paramètre. L'objet Data contient une liste d'images à
	 * placer trié de la plus grande à la plus petite gràce au tri fusion
	 * (trie le plus efficace nlog(n)).
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Data buildData(String path) throws IOException {
		Planche p = null;
		List<Image> listImages = null;
		Dimension dimPlanche = null;
		double prixPlanche = 0;
		List<String> lines = Files.readAllLines(Paths.get(path),
				StandardCharsets.UTF_8);
		String ligne = lines.get(0);
		lines.remove(0);
		Double largeurPlanche = buildDimension(ligne);
		ligne = lines.get(0);
		lines.remove(0);
		Double hauteurPlanche = buildDimension(ligne);
		ligne = lines.get(0);
		lines.remove(0);
		prixPlanche = buildDimension(ligne);

		dimPlanche = new Dimension(largeurPlanche, hauteurPlanche);

		Image image = null;
		Dimension dimImage = null;
		Double largeurImage;
		Double hauteurImage;
		int quantiteImages;
		listImages = new LinkedList<Image>();
		int i = 0;
		for (String line : lines) {
			String[] lineElements = line.split("\t");
			largeurImage = Double.parseDouble(lineElements[0]);
			hauteurImage = Double.parseDouble(lineElements[1]);
			quantiteImages = Integer.parseInt(lineElements[2]);
			dimImage = new Dimension(largeurImage, hauteurImage);
			image = new Image(i, dimImage, generateRandomColor(),
					quantiteImages);
			listImages.add(image);
			i++;
		}
		Image[] array = listImages.toArray(new Image[listImages.size()]);
		trieImages(array);
		int nbImages = array.length;
		p = new Planche(dimPlanche, prixPlanche, nbImages, -1);
		return new Data(p, listImages);
	}

	private Color generateRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}

	private void trieImages(Image[] images) {
		triFusion(images, 0, images.length - 1);
	}

	private void triFusion(Image[] tab, int debut, int fin) {
		int milieu;
		if (debut < fin) {
			milieu = (debut + fin) / 2;
			triFusion(tab, debut, milieu);
			triFusion(tab, milieu + 1, fin);
			fusionner(tab, debut, milieu, fin);
		}
	}

	public static void fusionner(Image tab[], int debut, int milieu, int fin) {
		Image[] old_tab = tab.clone();
		// tab.clone est tres gourmand en temps d'execution surtout dans un algo
		// recursif
		// il faudrait passer par un tableau temporaire pour stocker les
		// données
		// triées.
		// puis recopier la partie triée a la fin de la méthode.

		int i1 = debut; // indice dans la première moitié de old_tab
		int i2 = milieu + 1; // indice dans la deuxième moitié de old_tab
		int i = debut; // indice dans le tableau tab

		while (i1 <= milieu && i2 <= fin) {
			double aire1 = old_tab[i1].getDimension().calculeAire();
			double aire2 = old_tab[i2].getDimension().calculeAire();

			// quelle est la plus petite tête de liste?
			if (aire1 <= aire2) {
				tab[i] = old_tab[i1];
				i1++;
			} else {
				tab[i] = old_tab[i2];
				i2++;
			}
			i++;
		}
		if (i <= fin) {
			while (i1 <= milieu) // le reste de la première moitié
			{
				tab[i] = old_tab[i1];
				i1++;
				i++;
			}
			while (i2 <= fin) // le reste de la deuxième moitié
			{
				tab[i] = old_tab[i2];
				i2++;
				i++;
			}
		}
	}

	private double buildDimension(String l1) {
		String[] l1Tab = l1.split("=");
		String largeurPlanche = l1Tab[1];
		return Double.parseDouble(largeurPlanche);
	}

}

package algorithme;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import parse.Data;
import entites.Bin;
import entites.Coordonnees;
import entites.Dimension;
import entites.Image;
import execute.Execute;

public class AlgoPlacement {
	private static final int EN_TOURNANT = 1;
	private static final int SANS_TOURNER = 0;
	private static final int DEUX_SENS = 2;
	private static final int IMPOSSIBLE = -1;
	// Donnees de bases
	private Data data;
	// Liste des bins pour la solution
	private List<Bin> listBins = new ArrayList<Bin>();

	// Map des bin de placement avec la cl�� correspondant à l'indice du
	// premier
	// bin de chaque pattern.
	private Map<Integer, List<Bin>> mapBins;

	public List<Bin> getListBins() {
		return listBins;
	}

	public void setListBins(List<Bin> listBins) {
		this.listBins = listBins;
	}

	public AlgoPlacement(Data d) {
		this.data = d;
	}

	public List<Bin> initAlgo() {
		// Reinitialisation Id bin
		Bin.NB_BIN = 0;
		List<Bin> binBases = new LinkedList<Bin>();
		Bin bin = null;
		// mapBins = new HashMap<Integer, List<Bin>>();
		for (int i = 0; i < Execute.NB_PATTERNS; i++) {
			bin = new Bin();
			bin.setIdBin1(-1);
			bin.setIdBin2(-1);
			bin.setDimension(new Dimension(data.getPlanche().getDimension()
					.getLargeur(), data.getPlanche().getDimension()
					.getHauteur()));
			bin.setNumPatern(i);
			binBases.add(bin);
			// mapBins.put(bin.getIdBin(), value);
		}
		return binBases;
	}

	public List<Bin> initAlgoBis(int i) {
		// Reinitialisation Id bin
		Bin.NB_BIN = 0;
		List<Bin> binBases = new LinkedList<Bin>();
		Bin bin = null;
		// mapBins = new HashMap<Integer, List<Bin>>();
		bin = new Bin();
		bin.setIdBin1(-1);
		bin.setIdBin2(-1);
		bin.setDimension(new Dimension(data.getPlanche().getDimension()
				.getLargeur(), data.getPlanche().getDimension().getHauteur()));
		bin.setNumPatern(i);
		binBases.add(bin);
		// mapBins.put(bin.getIdBin(), value);

		return binBases;
	}

	public List<Bin> placementBis(int[] composTotal, int indicePattern) {
		boolean placementContinu = true;
		List<Bin> binBases = null;
		if (composTotal.length <= data.getListImages().size()) {
			// Liste des bins de base (1 bin par pattern)
			binBases = initAlgoBis(indicePattern);
			placementContinu = testSurfaceIsOk(composTotal, binBases);
			if (placementContinu) {
				int indiceImage;
				for (indiceImage = 0; indiceImage < composTotal.length; indiceImage++) {
					// Si on a pas d'image a placer, on passe à l'indice
					// suivant
					// sinon on la place et on construit les 2 bins "fils"
					if (composTotal[indiceImage] > 0) {
						for (int nb = composTotal[indiceImage]; nb > 0; nb--) {
							Image img = data.getImage(indiceImage);
							// On essaye de placer l'image sur le bin
							Bin bestBin = choixBin(binBases, img);
							// Test si le cas est impossible
							if (bestBin == null) {
								placementContinu = false;
								break;
							}
							bestBin.setIdImage(indiceImage);
							if (!placementContinu) {
								placementContinu = false;
								break;
							}
						}

					}
					if (!placementContinu) {
						placementContinu = false;
						break;
					}
				}
			}
		} else {
			System.err
					.println("Le nombre d'argument est trop grand par rapport au nombre d'images disponibles");
			placementContinu = false;
		}
		if (placementContinu) {
			return binBases;
		} else {
			return null;
		}

	}

	/**
	 * Fonction principale qui place les images passees en parametres sur les
	 * bins. true si c'est bon, false sin on ne peut pas placer l'image
	 * 
	 * @param composTotal
	 */
	public boolean placement(int[] composTotal) {
		boolean placementContinu = true;
		if (composTotal.length <= data.getListImages().size()) {
			// Liste des bins de base (1 bin par pattern)
			List<Bin> binBases = initAlgo();
			placementContinu = testSurfaceIsOk(composTotal, binBases);
			if (placementContinu) {
				int indiceImage;
				for (indiceImage = 0; indiceImage < composTotal.length; indiceImage++) {
					// Si on a pas d'image a placer, on passe à l'indice
					// suivant
					// sinon
					// on la place et on construit les 2 bins "fils"
					if (composTotal[indiceImage] > 0) {
						Image img = data.getImage(indiceImage);
						// On essaye de placer l'image sur le bin
						Bin bestBin = choixBin(binBases, img);
						// Test si le cas est impossible
						if (bestBin == null) {
							System.out.println("Solution impossible !!");
							placementContinu = false;
							break;
						}
						bestBin.setIdImage(indiceImage);

						if (!placementContinu) {
							placementContinu = false;
							break;
						}
					}
					if (!placementContinu) {
						placementContinu = false;
						break;
					}
				}
			}
			// if (placementContinu) {
			// for (Bin b : binBases) {
			// System.out.println(b.toString() + "\n");
			// }
			// }
			listBins = binBases;
		} else {
			System.err
					.println("Le nombre d'argument est trop grand par rapport au nombre d'images disponibles");
			placementContinu = false;
		}

		return placementContinu;
	}

	/**
	 * Fonction qui test si c'est possible de placer au moins une fois toutes
	 * les images sur les bins de base. Calcule l'aire total dispo des bins de
	 * base et calcul l'aire de chaque image à placer. Si l'air des images est
	 * superieur à celle des bins, alors il n'y a pas de solution.
	 * 
	 * @param composTotal
	 * @param binBases
	 * @return
	 */
	private boolean testSurfaceIsOk(int[] composTotal, List<Bin> binBases) {
		double aireBins = 0;
		double aireImages = 0;
		for (Bin b : binBases) {
			aireBins += b.calculAire();
		}

		for (int i = 0; i < composTotal.length; i++) {
			if (composTotal[i] > 0) {
				aireImages += composTotal[i] * data.getImage(i).calculAire();
			}
		}

		// if (!(aireImages <= aireBins)) {
		// System.out
		// .println("La surface des images est superieur à celle des planches.");
		// }
		return aireImages <= aireBins;

	}

	/**
	 * Associe l'image au 1er bin de base si c'est possible et decoupe ce bin
	 * pour construire ses 2 "fils". Si l'image ne pas être placee, alors il
	 * n'y a pas de solution possible et on retourne null.
	 * 
	 * @param binBases
	 * @param img
	 * @return
	 */
	private Bin choixBin(List<Bin> bins, Image img) {
		for (Bin bin : bins) {
			// S'il s'agit d'un bin "fils", on peut le d�couper
			if (bin.isOk()) {
				img.setRotate(false);
				int isPossible = isPossible(bin, img);
				switch (isPossible) {
				// On ne peut pas placer cette image sur le bin, on essaye sur
				// le bean suivant
				case IMPOSSIBLE:
					break;
				// Si on peut mettre l'image dans les 2 sens, on choisi de ne
				// pas la tourner
				case DEUX_SENS:
					couperBin(bins, bin, img);
					bin.setDimImage(img.getDimension());
					return bin;
				case SANS_TOURNER:
					couperBin(bins, bin, img);
					bin.setDimImage(img.getDimension());
					return bin;
				case EN_TOURNANT:
					img.setRotate(true);
					couperBin(bins, bin, img);
					bin.setImageIsRotate(true);
					bin.setDimImage(img.getDimension());
					return bin;
				}
			}
		}
		// Si on ne peut pas placer l'image sur les bins
		return null;
	}

	/**
	 * Fonction qui decoupe le bin place en parametre selon la vertical ou
	 * l'horizontal. Les deux bins "fils" sont crees et associes au bin
	 * passe en parametre. Le parametre image correpsont à l'image à placer
	 * sur le bin (en parametre). Si l'image peut être placee selon les 2
	 * decoupes, on privilegie la decoupe qui creer les 2 bins avec les
	 * aires les plus proches. La liste des bins est ensuite modifiee (on
	 * ajoute les bins fils et on met le pere à false).
	 * 
	 * @param bins
	 * 
	 * @param bin
	 * @param img
	 */
	private void couperBin(List<Bin> bins, Bin bin, Image img) {
		int hauteurImg = (int) img.getDimension().getHauteur();
		int largeurImg = (int) img.getDimension().getLargeur();

		int abcisseBin = bin.getCoordonnees().getAbscisse();
		int ordonneBin = bin.getCoordonnees().getOrdonnee();
		int largeurBin = (int) bin.getDimension().getLargeur();
		int hauteurBin = (int) bin.getDimension().getHauteur();
		// Test si l'image rentre sur le bin
		if (largeurBin >= img.getDimension().getLargeur()
				&& hauteurBin >= img.getDimension().getHauteur()) {
			// 1er cas : coupe selon toute la largeur
			Bin bin1 = null;
			Bin bin2 = null;
			bin1 = new Bin();
			bin1.setCoordonnees(new Coordonnees(abcisseBin, ordonneBin
					+ hauteurImg));
			bin1.setDimension(new Dimension(largeurBin, hauteurBin - hauteurImg));
			bin1.setNumPatern(bin.getNumPatern());

			bin2 = new Bin();
			bin2.setCoordonnees(new Coordonnees(abcisseBin + largeurImg,
					ordonneBin));
			bin2.setDimension(new Dimension(largeurBin - largeurImg, hauteurImg));
			bin2.setNumPatern(bin.getNumPatern());

			// 2eme cas : coupe selon toute la hauteur
			Bin bin3 = new Bin();
			bin3.setCoordonnees(new Coordonnees(abcisseBin, ordonneBin
					+ hauteurImg));
			bin3.setDimension(new Dimension(largeurImg, hauteurBin - hauteurImg));
			bin3.setNumPatern(bin.getNumPatern());

			Bin bin4 = new Bin();
			bin4.setCoordonnees(new Coordonnees(abcisseBin + largeurImg,
					ordonneBin));
			bin4.setDimension(new Dimension(largeurBin - largeurImg, hauteurBin));
			bin4.setNumPatern(bin.getNumPatern());

			// On garde les 2 bin qui ont l'aire la plus proche
			int deltaAire12 = Math.abs(bin1.calculAire() - bin2.calculAire());
			int deltaAire34 = Math.abs(bin2.calculAire() - bin3.calculAire());
			if (deltaAire12 < deltaAire34) {
				bin.setIdBin1(bin1.getIdBin());
				bin.setIdBin2(bin2.getIdBin());
				bin.setOk(false);
				bins.add(bin1);
				bins.add(bin2);
				// return B1 et B2
			} else {
				// return B3 et B4
				bin.setIdBin1(bin3.getIdBin());
				bin.setIdBin2(bin4.getIdBin());
				bin.setOk(false);
				bins.add(bin3);
				bins.add(bin4);
			}
		} else {
			System.out
					.println("Probleme : on essaye de placer un image sur un bin trop petit !");
		}
	}

	/**
	 * Retourne un entier qui indique si l'image peut être place sur le bin
	 * passe en parametre. Plusieurs cas sont possibles : -1 impossible de
	 * placer l'image, 0 l'image peut être placee telle qu'elle est, 1 l'image
	 * peut être placee en la tournant, 2 les 2 cas possibles (limage peut
	 * être placee dans les 2 sens)
	 * 
	 * @param bin
	 * @param img
	 * @return
	 */
	private int isPossible(Bin bin, Image img) {
		// Si l'aire de l'image est plus grande que celle du bin, on ne peut pas
		// placer l'image
		if (img.calculAire() > bin.calculAire()) {
			return IMPOSSIBLE;
		}
		// Si la hauteur de l'image est plus grande que la largeur et la hauteur
		// du bin, alors on ne peut pas la placer
		if ((img.getDimension().getHauteur() > bin.getDimension().getHauteur() && img
				.getDimension().getHauteur() > bin.getDimension().getLargeur())) {
			return IMPOSSIBLE;

		} else if ((img.getDimension().getLargeur() > bin.getDimension()
				.getHauteur() && img.getDimension().getLargeur() > bin
				.getDimension().getLargeur())) {
			// Si la largeur de l'image est plus grande que la largeur et la
			// hauteur du bin, alors on ne peut pas la placer
			return IMPOSSIBLE;
		} else {
			// Si l'image rentre dans le bin on test les 3 cas possibles
			if (img.getDimension().getHauteur() <= bin.getDimension()
					.getHauteur()
					&& img.getDimension().getLargeur() <= bin.getDimension()
							.getLargeur()) {
				if (img.getDimension().getHauteur() <= bin.getDimension()
						.getLargeur()
						&& img.getDimension().getLargeur() <= bin
								.getDimension().getHauteur()) {
					// L'image rentre dans les 2 sens
					return DEUX_SENS;
				} else {
					// L'image rentre sans la tourner
					return SANS_TOURNER;
				}
			} else {
				if (img.getDimension().getHauteur() <= bin.getDimension()
						.getLargeur()
						&& img.getDimension().getLargeur() <= bin
								.getDimension().getHauteur()) {
					// L'image rentre en la tournant
					return EN_TOURNANT;
				}
			}

		}
		return IMPOSSIBLE;
	}
}

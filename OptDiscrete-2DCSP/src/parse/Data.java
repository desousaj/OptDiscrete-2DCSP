package parse;

import java.util.LinkedList;
import java.util.List;

import stats.Execute;
import entites.Image;
import entites.Planche;

public class Data {
	private Planche planche;
	private List<Image> listImages;

	public Data() {
		super();
		this.planche = new Planche();
		this.listImages = new LinkedList<Image>();
	}

	public Data(Planche planche, List<Image> listImages) {
		super();
		this.planche = planche;
		this.listImages = listImages;
	}

	public int getNbImages() {
		return listImages.size();
	}

	public Planche getPlanche() {
		return planche;
	}

	public void setPlanche(Planche planche) {
		this.planche = planche;
	}

	public List<Image> getListImages() {
		return listImages;
	}

	public void setListImages(List<Image> listImages) {
		this.listImages = listImages;
	}

	public Image getImage(int indice) {
		for (Image img : listImages) {
			if (img.getIndice() == indice) {
				return img;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String txt = planche.toString() + "\n Images : \n";
		for (Image img : listImages) {
			txt = txt.concat(img.toString() + "\n");
		}
		return txt;
	}

	/**
	 * Test si au moins chaque image peut être placé sur les patterns
	 * 
	 * @return
	 */
	public boolean testIsPossible() {
		double airePatternsDispo = Execute.NB_PATTERNS
				* planche.getDimension().calculeAire();
		double aireImages = 0;
		for (Image i : listImages) {
			aireImages += i.calculAire();
		}
		return aireImages <= airePatternsDispo;
	}

}

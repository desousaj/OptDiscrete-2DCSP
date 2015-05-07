package execute;

import java.io.IOException;

import parse.Data;
import parse.ParseData;
import algorithme.RecuitSimule;

public class Execute {

	public static int NB_PATTERNS = 3;

	public static void main(String[] args) throws IOException {
		ParseData p = new ParseData();
		Data d = p.buildData("data_20Salpha.txt");
		RecuitSimule r = new RecuitSimule(d);
		r.lancer();
		// new FenetreMenu();
	}

}

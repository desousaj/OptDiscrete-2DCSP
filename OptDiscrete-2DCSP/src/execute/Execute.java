package execute;

import parse.Data;
import parse.ParseData;
import algorithme.RecuitSimule;

public class Execute {

	public final static int NB_PATTERNS = 3;

	public static void main(String[] args) {
		ParseData parseData = new ParseData();
		Data d = parseData.buildData("data_20Lalpha.txt");

		RecuitSimule rs = new RecuitSimule(d);
		rs.lancer();
	}

}

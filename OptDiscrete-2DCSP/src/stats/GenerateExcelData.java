package stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GenerateExcelData {
	private List<Resultat> resultats;
	private String path;
	private String nameFile;

	public GenerateExcelData(List<Resultat> resultats, String path,
			String nameFile) {
		super();
		this.resultats = resultats;
		this.path = path;
		this.nameFile = nameFile;
	}

	public void genereData() {
		// Creer le dossier
		File folder = new File(path + File.separator + "Statdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File file = new File(folder + File.separator + nameFile);
		HSSFWorkbook wb = null;
		if (file.exists()) {
			InputStream is;
			try {
				is = new FileInputStream(file.getAbsolutePath());
				wb = new HSSFWorkbook(is);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			wb = new HSSFWorkbook();
		}
		Resultat r1 = resultats.get(0);
		double tempInit = r1.getTempInitiale();
		double tempFinale = r1.getTempFinale();
		double facteurDecro = r1.getFactDecr();
		double nbIter = r1.getNbIteration();
		HSSFCell cell1v5 = null;
		HSSFCell cell0v5 = null;
		String nomFeuille = tempInit + "-" + tempFinale + "-" + facteurDecro
				+ "-" + nbIter;

		HSSFSheet sheet = wb.getSheet(nomFeuille);
		if (sheet == null) {
			sheet = wb.createSheet(nomFeuille);
			// Ligne 1
			HSSFRow row0 = sheet.createRow(0);
			HSSFCell cell0 = row0.createCell(0);
			cell0.setCellValue("Température init = ");
			HSSFCell cell0v = row0.createCell(1);
			cell0v.setCellValue(tempInit);
			HSSFCell cell0v4 = row0.createCell(3);
			cell0v4.setCellValue("Meilleur sol = ");
			cell0v5 = row0.createCell(4);

			// Ligne 2
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellValue("Température finale = ");
			HSSFCell cell1v = row1.createCell(1);
			cell1v.setCellValue(tempFinale);
			HSSFCell cell1v4 = row1.createCell(3);
			cell1v4.setCellValue("Temps moyenne = ");
			cell1v5 = row1.createCell(4);

			// Ligne 3
			HSSFRow row2 = sheet.createRow(2);
			HSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue("Facteur de décroissance = ");
			HSSFCell cell2v = row2.createCell(1);
			cell2v.setCellValue(facteurDecro);

			// Ligne 4
			HSSFRow row3 = sheet.createRow(3);
			HSSFCell cell3 = row3.createCell(0);
			cell3.setCellValue("Nombre d'itérations par palier = ");
			HSSFCell cell3v = row3.createCell(1);
			cell3v.setCellValue(nbIter);

			// Ligne 6
			HSSFRow row5 = sheet.createRow(5);
			HSSFCell cell5a = row5.createCell(0);
			cell5a.setCellValue("Nom");
			HSSFCell cell5b = row5.createCell(1);
			cell5b.setCellValue("Temps d'execution");
			HSSFCell cell5c = row5.createCell(2);
			cell5c.setCellValue("Nombre de patterns");
			HSSFCell cell5d = row5.createCell(3);
			cell5d.setCellValue("Prix total");
		} else {
			HSSFRow row0 = sheet.getRow(0);
			cell0v5 = row0.getCell(4);
			HSSFRow row1 = sheet.getRow(1);
			cell1v5 = row1.getCell(4);

		}

		int ligne = sheet.getLastRowNum();
		ligne++;
		for (Resultat r : resultats) {
			HSSFRow row = sheet.createRow(ligne);
			HSSFCell nom = row.createCell(0);
			nom.setCellValue(r.getNameData());
			HSSFCell temps = row.createCell(1);
			temps.setCellValue(r.getTime());
			HSSFCell patterns = row.createCell(2);
			patterns.setCellValue(r.getNbPlanches());
			HSSFCell prix = row.createCell(3);
			prix.setCellValue(r.getPrixTotal());
			ligne++;
		}

		// Calcul de la moyenne des temps et de la meilleur sol

		float total_temps = 0;
		int prix = Integer.MAX_VALUE;

		for (int i = 6; i < ligne; i++) {
			HSSFRow ligne1 = sheet.getRow(i);
			HSSFCell cellule_temps = ligne1.getCell(1);
			HSSFCell cellule_prix = ligne1.getCell(3);
			total_temps += cellule_temps.getNumericCellValue();
			prix = Math.min(prix, (int) cellule_prix.getNumericCellValue());
		}

		cell0v5.setCellValue(prix);
		cell1v5.setCellValue(total_temps / (ligne - 6));

		// Recalcul la largeur des colonnes
		for (int i = 0; i < 6; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fichier généré !");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public List<Resultat> getResultats() {
		return resultats;
	}

	public void setResultats(List<Resultat> resultats) {
		this.resultats = resultats;
	}

}

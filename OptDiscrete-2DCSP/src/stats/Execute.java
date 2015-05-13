package stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;

public class Execute {

	public static int NB_PATTERNS = 11;
	public static int NB_TEST_WITH_SAME_PARAMETERS = 3;
	public static String TEXTE_FILE_NAME = "data_30Valpha";
	public final static double TEMP_FINALE = 0.9;
	public final static double TEMP_INIT = 7000;
	public final static int NB_ITER = 1500;
	public final static double FACT_DECR = 0.3;
	public final static boolean SHOW_TEMP = true;

	public Execute() {

	}

	public static void main(String[] args) throws IOException {
		Execute e = new Execute();

		// 4 PATTERNS
		// e.genereData("data_20Salpha");
		// 5 PATTERNS
		// e.genereData("data_20Valpha");
		// e.genereData("data_20Lalpha");
		// 6 PATTERNS
		// e.genereData("data_30Salpha");
		// 10 PATTERNS
		// e.genereData("data_30Valpha");
		// e.genereData("data_30Lalpha");
		// 11 PATTERNS
		// e.genereData("data_40Salpha");

		// de 13 à 18 PATTERNS
		// for (int i = 18; i > 14; i--) {
		// Execute.NB_PATTERNS = i;
		// e.genereData("data_40Valpha");
		// e.genereData("data_40Lalpha");
		//
		// }

		// de 13 à 16 PATTERNS
		for (int i = 22; i > 18; i--) {
			Execute.NB_PATTERNS = i;
			// genereData("data_50Valpha");
			e.genereData("data_50Lalpha");

		}

	}

	public void genereData(String path) {
		// Traitement sur le nom du fichier pour vérifier s'il s'agit du path ou
		// du nom
		String nameFile = path;
		System.out.println("Nombre de Threads : "
				+ NB_TEST_WITH_SAME_PARAMETERS);
		if (path.contains("\\")) {
			nameFile = path.replace("\\", "\\\\");
			String[] fileNameTab = nameFile.split("\\\\");
			nameFile = fileNameTab[fileNameTab.length - 1];

		}
		if (nameFile.contains(".txt")) {
			nameFile = nameFile.replace(".txt", "");
		}
		if (!path.contains(".txt")) {
			path = path.concat(".txt");
		}

		String file = nameFile + ".txt";
		List<FutureTask<Resultat>> listTasks = new ArrayList<FutureTask<Resultat>>();
		List<Resultat> listResultats = new ArrayList<Resultat>();

		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < NB_TEST_WITH_SAME_PARAMETERS; i++) {
			TestThread callable1 = new TestThread(TEMP_FINALE, TEMP_INIT,
					NB_ITER, FACT_DECR, file, path);
			FutureTask<Resultat> futureTask1 = new FutureTask<Resultat>(
					callable1);
			listTasks.add(futureTask1);
			executor.execute(futureTask1);
		}

		boolean stop = false;
		Iterator<FutureTask<Resultat>> iterator = listTasks.iterator();
		while (!listTasks.isEmpty() && !stop) {
			for (int i = 0; i < listTasks.size(); i++) {
				FutureTask<Resultat> task = listTasks.get(i);
				if (task.isDone()) {
					System.out.println("Done");
					try {
						if (task.get() != null) {
							listResultats.add(task.get());
						} else {
							stop = true;
							executor.shutdownNow();
							JOptionPane
									.showMessageDialog(
											null,
											"Une erreur est survenue. Veuillez choisir un fichier texte valide, ainsi qu'un nombre de pattern suffisant...",
											"Erreur", JOptionPane.ERROR_MESSAGE);
							break;
						}
					} catch (InterruptedException | ExecutionException e) {
						System.out.println("Unable to complete task ...");
						break;
					}
					if (stop) {
						break;
					}
					listTasks.remove(i);

				}
			}

		}

		if (listResultats != null && !listResultats.isEmpty()) {
			String fileExcel = nameFile + ".xls";
			GenerateExcelData data = new GenerateExcelData(listResultats,
					System.getProperty("user.dir"), fileExcel);
			data.genereData();
		}
	}

}

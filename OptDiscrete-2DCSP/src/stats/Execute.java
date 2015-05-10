package stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Execute {

	public static int NB_PATTERNS = 4;
	public static int NB_TEST_WITH_SAME_PARAMETERS = 5;
	public static String TEXTE_FILE_NAME = "data_20Salpha";
	public final static double TEMP_FINALE = 0.5;
	public final static double TEMP_INIT = 1000;
	public final static int NB_ITER = 2500;
	public final static double FACT_DECR = 0.5;

	public static void main(String[] args) throws IOException {
		double fact_decr = FACT_DECR;
		// int nb = 1000;
		// while (nb < 5000) {
		String file = TEXTE_FILE_NAME + ".txt";
		List<FutureTask<Resultat>> listTasks = new ArrayList<FutureTask<Resultat>>();
		List<Resultat> listResultats = new ArrayList<Resultat>();

		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < NB_TEST_WITH_SAME_PARAMETERS; i++) {
			TestThread callable1 = new TestThread(TEMP_FINALE, TEMP_INIT,
					NB_ITER, fact_decr, file);

			FutureTask<Resultat> futureTask1 = new FutureTask<Resultat>(
					callable1);
			listTasks.add(futureTask1);
			executor.execute(futureTask1);
		}

		Iterator<FutureTask<Resultat>> iterator = listTasks.iterator();
		while (!listTasks.isEmpty()) {
			for (int i = 0; i < listTasks.size(); i++) {
				FutureTask<Resultat> task = listTasks.get(i);
				if (task.isDone()) {
					System.out.println("Done");
					try {
						if (task.get() != null)
							listResultats.add(task.get());
						listTasks.remove(i);
						break;
					} catch (InterruptedException | ExecutionException e) {
						System.out.println("Unable to add task !");
						e.printStackTrace();
					}

				}
			}

		}
		String fileExcel = TEXTE_FILE_NAME + ".xls";
		GenerateExcelData data = new GenerateExcelData(listResultats, "C:/",
				fileExcel);
		data.genereData();
		fact_decr += 0.1;
		// }
		// nb += 500;
	}

}

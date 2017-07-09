import java.io.File;
import java.io.IOException;

/*
 * @author Joseph Mammo	
 * @version 002
 */
public class OTU_to_ROC {

	public static void main(String[] args) {

		File att = new File("output/FeaturesSelected/");
		File roc = new File("output/ROC/");
		File completed = new File("output/DONE.temp");

		// create directories if they don't exist yet
		if (!att.exists()) {
			att.mkdirs();
		}
		if (!roc.exists()) {
			roc.mkdirs();
		}
		// clean up old files
		cleanUp(att, roc, completed);

		// Select Attributes
		SelectAttributes selAtts = new SelectAttributes();
		String[] options_Att = { "ip", "C:\\Users\\Joe\\Desktop\\Summer 2017\\BioSNTR\\datasets\\V1V3.csv", "-ra", "1",
				"-ca", "1", "-su", "0", "-ig", "0", "-ga", "1", "-att", "-1", "-op", "output/FeaturesSelected/" };
		try {
			selAtts.main(options_Att);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Generate ROC's
		GenerateROC genROC = new GenerateROC();
		String[] options_ROC = { "-nb", "1", "-rf", "0", "-rt", "0", "-ip", "output/FeaturesSelected", "-op",
				"output/ROC/" };
		try {
			genROC.main(options_ROC);
		} catch (Exception e) {
			e.printStackTrace();
		}

		completionIndicator();
		System.exit(1);
	}

	private static void cleanUp(File attDir, File ROCDir, File completed) {
		for (File file : attDir.listFiles())
			file.delete();
		for (File file : ROCDir.listFiles())
			file.delete();
		completed.delete();
	}

	private static void completionIndicator() {
		File completed = new File("output/DONE.temp");
		if (!completed.exists()) {
			try {
				completed.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

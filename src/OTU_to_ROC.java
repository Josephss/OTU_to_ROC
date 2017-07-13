import java.io.File;

/*
 * @author Joseph Mammo	
 * @version 004
 */
public class OTU_to_ROC {

	public static void main(String[] args) {

		File outputDir = new File(System.getProperty("user.dir") + "\\Output\\");

		// create directories if they don't exist yet
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		// clean up old files
		cleanUp(outputDir);

		// Select Attributes
		SelectAttributes selAtts = new SelectAttributes();
		String[] options_Att = { "ip", args[0], "-ra", "1", "-ca", "1", "-su", "1", "-ig", "1", "-ga", "1", "-att",
				"-1", "-op", outputDir.toString() + "/" };
		try {
			selAtts.main(options_Att);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(1);
	}

	private static void cleanUp(File attDir) {
		System.out.println("\n00. Cleaning up old outputs ...");
		for (File file : attDir.listFiles()) {
			file.delete();
		}
	}

}

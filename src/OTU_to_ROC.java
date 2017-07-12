import java.io.File;
//import java.io.IOException;

/*
 * @author Joseph Mammo	
 * @version 003
 */
public class OTU_to_ROC {

	public static void main(String[] args) {

		// File inputDir = new File(args[0]); // "C:\\Users\\Joe\\Desktop\\Summer
		// 2017\\BioSNTR\\datasets\\V1V3.csv"
		File outputDir = new File(System.getProperty("user.dir") + "\\Output\\");
		// File completedDir = new File("/output/DONE.temp");
		// System.out.println(outputDir.toString());
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

		// completionIndicator(completedDir);
		System.exit(1);
	}

	// TODO:Do pre-processing

	private static void cleanUp(File attDir) {
		System.out.println("\n00. Cleaning up old outputs ...");
		for (File file : attDir.listFiles()) {
			file.delete();
		}
		// completed.delete();
	}
	/*
	 * private static void completionIndicator(File completed) { if
	 * (!completed.exists()) { try { completed.createNewFile(); } catch (IOException
	 * e) { e.printStackTrace(); } } }
	 */
}

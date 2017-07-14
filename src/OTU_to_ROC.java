import java.io.File;

/*
 * @author Joseph Mammo	
 * @version 005
 */
public class OTU_to_ROC {

	public static void main(String[] args) {

		File outputDir = new File(System.getProperty("user.dir") + "\\Output_user\\");
		File outputDirMod = new File(System.getProperty("user.dir") + "\\Input\\");
		// create directories if they don't exist yet
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		if (!outputDirMod.exists()) {
			outputDirMod.mkdirs();
		}

		// clean up old files
		cleanUp(outputDir, outputDirMod);

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

	private static void cleanUp(File attDir, File pyDir) {
		System.out.println("\n00. Cleaning up old outputs ...");
		for (File file : attDir.listFiles()) {
			file.delete();
		}
		for (File file : pyDir.listFiles()) {
			file.delete();
		}
	}

}

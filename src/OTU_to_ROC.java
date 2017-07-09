
public class OTU_to_ROC {

	public static void main(String[] args) {
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
		String[] options_ROC = { "-nb", "1", "-rf", "1", "-rt", "0", "-p", "output/FeaturesSelected" };
		try {
			genROC.main(options_ROC);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

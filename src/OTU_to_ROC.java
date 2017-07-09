
public class OTU_to_ROC {

	public static void main(String[] args) {
		GenerateROC genROC = new GenerateROC();
		String[] options = {"-nb",  "1" ,"-rf", "0", "-rt", "0", "-p", "output/FeaturesSelected"};
		try {
			genROC.main(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

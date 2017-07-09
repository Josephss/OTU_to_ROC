import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.SymmetricalUncertAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

/*
 * @author Joseph Mammo	
 * @version 004
 */
public class SelectAttributes {

	/**
	 * Takes one argument: dataset in CSV or ARFF format (expects class to be last
	 * attribute).
	 * 
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// load data
		System.out.println("\n0. Loading data ...");
		DataSource source = new DataSource("C:\\Users\\Joe\\Desktop\\Summer 2017\\BioSNTR\\datasets\\V1V3.csv");
		Instances data = source.getDataSet();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		// process data
		System.out.println("\n1. Processing data ...");

		ReliefFAttributeEval(data);
		CorrelationAttributeEval(data);
		SymmetricalUncertAttributeEval(data);
		InfoGainAttributeEval(data);
		GainRatioAttributeEval(data);
	}

	protected static void ReliefFAttributeEval(Instances data) throws Exception {
		System.out.println("\n Selecting Features ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		ReliefFAttributeEval ra = new ReliefFAttributeEval();
		ra.setDoNotCheckCapabilities(false);
		ra.setNumNeighbours(10);
		ra.setSampleSize(-1);
		ra.setSeed(1);
		ra.setSigma(2);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(-1);
		rk.setThreshold(-1.7976931348623157E308);

		filter.setEvaluator(ra);
		filter.setSearch(rk);
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);

		// Select features
		AttributeSelection attsel = new AttributeSelection();
		attsel.setEvaluator(ra);
		attsel.setSearch(rk);

		attsel.setFolds(10);
		attsel.setSeed(1);

		attsel.SelectAttributes(data);
		int[] indices = attsel.selectedAttributes();
		System.out.println(attsel.toResultsString());

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "ReliefF"); // class, data, fileName

	}

	protected static void CorrelationAttributeEval(Instances data) throws Exception {
		System.out.println("\n Selecting Features ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		CorrelationAttributeEval ca = new CorrelationAttributeEval();
		ca.setDoNotCheckCapabilities(false);
		ca.setOutputDetailedInfo(false);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(-1);
		rk.setThreshold(-1.7976931348623157E308);

		filter.setEvaluator(ca);
		filter.setSearch(rk);
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);

		// Select features
		AttributeSelection attsel = new AttributeSelection();
		attsel.setEvaluator(ca);
		attsel.setSearch(rk);

		attsel.setFolds(10);
		attsel.setSeed(1);

		attsel.SelectAttributes(data);
		int[] indices = attsel.selectedAttributes();
		System.out.println(attsel.toResultsString());

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "Correlation"); // class, data, fileName

	}

	protected static void SymmetricalUncertAttributeEval(Instances data) throws Exception {
		System.out.println("\n Selecting Features ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		SymmetricalUncertAttributeEval su = new SymmetricalUncertAttributeEval();
		su.setDoNotCheckCapabilities(false);
		su.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(-1);
		rk.setThreshold(-1.7976931348623157E308);

		filter.setEvaluator(su);
		filter.setSearch(rk);
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);

		// Select features
		AttributeSelection attsel = new AttributeSelection();
		attsel.setEvaluator(su);
		attsel.setSearch(rk);

		attsel.setFolds(10);
		attsel.setSeed(1);

		attsel.SelectAttributes(data);
		int[] indices = attsel.selectedAttributes();
		System.out.println(attsel.toResultsString());

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "SymmetricalUncert"); // class, data, fileName

	}

	protected static void InfoGainAttributeEval(Instances data) throws Exception {
		System.out.println("\n Selecting Features ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		InfoGainAttributeEval ig = new InfoGainAttributeEval();
		ig.setBinarizeNumericAttributes(false);
		ig.setDoNotCheckCapabilities(false);
		ig.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(-1);
		rk.setThreshold(-1.7976931348623157E308);

		filter.setEvaluator(ig);
		filter.setSearch(rk);
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);

		// Select features
		AttributeSelection attsel = new AttributeSelection();
		attsel.setEvaluator(ig);
		attsel.setSearch(rk);

		attsel.setFolds(10);
		attsel.setSeed(1);

		attsel.SelectAttributes(data);
		int[] indices = attsel.selectedAttributes();
		System.out.println(attsel.toResultsString());

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "InfoGain"); // class, data, fileName

	}

	protected static void GainRatioAttributeEval(Instances data) throws Exception {
		System.out.println("\n Selecting Features ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		GainRatioAttributeEval gr = new GainRatioAttributeEval();
		gr.setDoNotCheckCapabilities(false);
		gr.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(-1);
		rk.setThreshold(-1.7976931348623157E308);

		filter.setEvaluator(gr);
		filter.setSearch(rk);
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);

		// Select features
		AttributeSelection attsel = new AttributeSelection();
		attsel.setEvaluator(gr);
		attsel.setSearch(rk);

		attsel.setFolds(10);
		attsel.setSeed(1);

		attsel.SelectAttributes(data);
		int[] indices = attsel.selectedAttributes();
		System.out.println(attsel.toResultsString());

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "GainRatio"); // class, data, fileName

	}

	public static void writeFile(Object[] classes, Instances data, String fileName) throws IOException {
		ArrayList<String> lines = new ArrayList<>();

		String[] tempData = asStrings(data.toArray());
		String[] tempClassStArr = asStrings(classes);

		lines.add(String.join(",", tempClassStArr));

		for (int i = 0; i < tempData.length; i++) {
			lines.add(tempData[i]);
		}

		Path file = Paths.get("output/FeaturesSelected/" + fileName + ".csv");
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	public static String[] asStrings(Object... objArray) {
		String[] strArray = new String[objArray.length];
		for (int i = 0; i < objArray.length; i++)
			strArray[i] = String.valueOf(objArray[i]);
		return strArray;
	}

}

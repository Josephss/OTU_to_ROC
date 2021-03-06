import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.nio.charset.StandardCharsets;

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
 * @version 005
 */
public class SelectAttributes {

	/**
	 * Takes in args usage: -ip input file path -ra 1 -ca 1 - su 1 -ig 1 -ga 1 -att
	 * -1 -op output file path -| (1 = true, 0 = false)
	 * 
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public void main(String[] args) throws Exception {

		boolean RA = false, CA = false, SU = false, IG = false, GA = false;

		if (args[3] == "1") {
			RA = true;
		}
		if (args[5] == "1") {
			CA = true;
		}
		if (args[7] == "1") {
			SU = true;
		}
		if (args[9] == "1") {
			IG = true;
		}
		if (args[11] == "1") {
			GA = true;
		}
		// load data
		System.out.println("\n0. Loading data ...");
		DataSource source = new DataSource(args[1]);
		Instances data = source.getDataSet();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		// process data
		System.out.println("\n1. Processing data ...");
		if (RA) {
			ReliefFAttributeEval(data, Integer.parseInt(args[13]), args[15]);
		}
		if (CA) {
			CorrelationAttributeEval(data, Integer.parseInt(args[13]), args[15]);
		}
		if (SU) {
			SymmetricalUncertAttributeEval(data, Integer.parseInt(args[13]), args[15]);
		}
		if (IG) {
			InfoGainAttributeEval(data, Integer.parseInt(args[13]), args[15]);
		}
		if (GA) {
			GainRatioAttributeEval(data, Integer.parseInt(args[13]), args[15]);
		}
		System.out.println("\n2. Finalizing output ...");
	}

	protected static void ReliefFAttributeEval(Instances data, int numOfAttributes, String outputPath)
			throws Exception {
		System.out.println("\n Selecting features using ReliefFAttributeEval attribute evaluator ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
		
		ReliefFAttributeEval ra = new ReliefFAttributeEval();
		ra.setDoNotCheckCapabilities(false);
		ra.setNumNeighbours(10);
		ra.setSampleSize(-1);
		ra.setSeed(1);
		ra.setSigma(2);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(numOfAttributes); // -1 to select all attributes
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
		System.out.println(" -- Done -- ");

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "ReliefF", outputPath); // user output
		writeFileMod(classes, newData, "ReliefF", outputPath); // ROC generator script output

	}

	protected static void CorrelationAttributeEval(Instances data, int numOfAttributes, String outputPath)
			throws Exception {
		System.out.println("\n Selecting features using CorrelationAttributeEval attribute evaluator ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		CorrelationAttributeEval ca = new CorrelationAttributeEval();
		ca.setDoNotCheckCapabilities(false);
		ca.setOutputDetailedInfo(false);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(numOfAttributes);
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
		// System.out.println(attsel.toResultsString());
		System.out.println(" -- Done -- ");

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "Correlation", outputPath); // user output
		writeFileMod(classes, newData, "Correlation", outputPath); // ROC generator script output

	}

	protected static void SymmetricalUncertAttributeEval(Instances data, int numOfAttributes, String outputPath)
			throws Exception {
		System.out.println("\n Selecting features using SymmetricalUncertAttributeEval attribute evaluator ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		SymmetricalUncertAttributeEval su = new SymmetricalUncertAttributeEval();
		su.setDoNotCheckCapabilities(false);
		su.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(numOfAttributes);
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
		// System.out.println(attsel.toResultsString());
		System.out.println(" -- Done -- ");

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "SymmetricalUncert", outputPath); // user output
		writeFileMod(classes, newData, "SymmetricalUncert", outputPath); // ROC generator script output

	}

	protected static void InfoGainAttributeEval(Instances data, int numOfAttributes, String outputPath)
			throws Exception {
		System.out.println("\n Selecting features using InfoGainAttributeEval attribute evaluator ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		InfoGainAttributeEval ig = new InfoGainAttributeEval();
		ig.setBinarizeNumericAttributes(false);
		ig.setDoNotCheckCapabilities(false);
		ig.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(numOfAttributes);
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
		// System.out.println(attsel.toResultsString());
		System.out.println(" -- Done -- ");

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "InfoGain", outputPath); // user output
		writeFileMod(classes, newData, "InfoGain", outputPath); // ROC generator script output
	}

	protected static void GainRatioAttributeEval(Instances data, int numOfAttributes, String outputPath)
			throws Exception {
		System.out.println("\n Selecting features using GainRatioAttributeEval attribute evaluator ...");
		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		GainRatioAttributeEval gr = new GainRatioAttributeEval();
		gr.setDoNotCheckCapabilities(false);
		gr.setMissingMerge(true);

		Ranker rk = new Ranker();
		rk.setGenerateRanking(true);
		rk.setNumToSelect(numOfAttributes);
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
		// System.out.println(attsel.toResultsString());
		System.out.println(" -- Done -- ");

		Object[] classes = new Object[indices.length];
		for (int i = 0; i < indices.length - 1; i++) {
			classes[i] = data.attribute(indices[i]).name();
		}
		classes[indices.length - 1] = "class"; // add class label so it is compatible to read back

		// Write out selected features
		writeFile(classes, newData, "GainRatio", outputPath); // user output
		writeFileMod(classes, newData, "GainRatio", outputPath); // ROC generator script output

	}

	/**
	 * 
	 * @param classes:
	 *            Takes in list of class names,
	 * @param data:
	 *            their corresponding attributes with values,
	 * @param fileName:
	 *            the name of the output file,
	 * @param outputPath:
	 *            the path of the output file and writes it out to a CSV file.
	 * @throws IOException
	 */
	public static void writeFile(Object[] classes, Instances data, String fileName, String outputPath)
			throws IOException {
		ArrayList<String> lines = new ArrayList<>();

		String[] tempData = asStrings(data.toArray());
		String[] tempClassStArr = asStrings(classes);

		lines.add(String.join(",", tempClassStArr)); // append attribute names

		for (int i = 0; i < tempData.length; i++) {
			lines.add(tempData[i]); // append attribute values
		}

		Path file = Paths.get(outputPath + fileName + ".csv");
		Files.write(file, lines, Charset.forName("UTF-8"));

	}

	/**
	 * 
	 * @param classes:
	 *            Takes in list of class names,
	 * @param data:
	 *            their corresponding attributes with values,
	 * @param fileName:
	 *            the name of the output file,
	 * @param outputPath:
	 *            the path of the output file and writes out a CSV file without the
	 *            attribute names and int representations of the classes.
	 * @throws IOException
	 */
	public static void writeFileMod(Object[] classes, Instances data, String fileName, String outputPath)
			throws IOException {

		File outputDir = new File(System.getProperty("user.dir") + "\\Input\\");
		ArrayList<String> lines = new ArrayList<>();

		String[] tempData = asStrings(data.toArray());

		for (int i = 0; i < tempData.length; i++) {
			lines.add(tempData[i]);
		}

		Path file = Paths.get(outputDir.toString() + "/" + fileName + ".csv");
		Files.write(file, lines, Charset.forName("UTF-8"));

		String[] tempClassStArr = asStrings(classes);
		File filee = new File(outputDir.toString() + "/" + fileName + ".csv");
		List<String> liness = Files.readAllLines(filee.toPath(), StandardCharsets.UTF_8);

		ArrayList<Object> cc = new ArrayList<>();
		for (String line : liness) {
			String[] array = line.split(",");
			cc.add(array[tempClassStArr.length - 1]);
		}

		ArrayList<String> lines2 = new ArrayList<>();
		ArrayList<String> fin;
		int temp = 0;
		for (String line : liness) {
			String[] tempArr1 = line.split(",");
			fin = new ArrayList<>();
			for (int i = 0; i < tempClassStArr.length - 2; i++) {
				fin.add(tempArr1[i]);
			}
			fin.add(Integer.toString(stringToInt(cc).get(temp)));

			StringBuilder finWithoutBrackets = new StringBuilder();
			for (String value : fin) {
				finWithoutBrackets.append(value);
				finWithoutBrackets.append(",");
			}
			lines2.add(finWithoutBrackets.toString().substring(0, finWithoutBrackets.length() - 1)); // remove the last comma and append
			temp++;
		}

		Files.write(file, lines2, Charset.forName("UTF-8"));

	}

	/**
	 * 
	 * @param input:
	 *            Takes in object array of class names
	 * @return outputInt: outputs incrementing int values corresponding to the class
	 *         inputs
	 */
	public static ArrayList<Integer> stringToInt(ArrayList<Object> input) {
		// convert to string array
		String[] tempData = asStrings(input.toArray());

		ArrayList<Integer> outputInt = new ArrayList<>();
		LinkedHashSet<String> lhs = new LinkedHashSet<>();
		HashMap<String, Integer> hs = new HashMap<>();

		// append unique class names, in order
		for (int i = 0; i < input.size(); i++) {
			lhs.add(tempData[i]);
		}
		// map each class name with incrementing int
		Object[] tempArr = lhs.toArray();
		for (int j = 0; j < lhs.size(); j++) {
			hs.put(tempArr[j].toString(), j);
		}
		// using mapping to assign the int values for each class
		for (int k = 0; k < input.size(); k++) {
			outputInt.add(hs.get(input.get(k)));
		}

		return outputInt;
	}

	public static String[] asStrings(Object... objArray) {
		String[] strArray = new String[objArray.length];
		for (int i = 0; i < objArray.length; i++) {
			strArray[i] = String.valueOf(objArray[i]);
		}
		return strArray;
	}

}

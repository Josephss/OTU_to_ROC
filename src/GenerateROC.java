import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.visualize.JComponentWriter;
import weka.gui.visualize.JPEGWriter;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import java.io.File;
import java.io.FileFilter;
import java.util.Random;

import javax.swing.JFrame;

/*
 * @author Joseph Mammo	
 * @version 003
 */
public class GenerateROC {

	/**
	 * Takes one argument: dataset in CSV or ARFF format (expects class to be last
	 * attribute).
	 * 
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public void main(String[] args) throws Exception {

		// args usage: -nb 1 -rf 1 -rt 1 -ip output/FeaturesSelected  -op ouput/ROC| (1 = true, 0 = false) | NB, RF, RT, Path 

		boolean NB = false, RF = false, RT = false;
		if (args[1] == "1") {
			NB = true;
		}
		if (args[3] == "1") {
			RF = true;
		}
		if (args[5] == "1") {
			RT = true;
		}
		// Get all filtered files
		String path = args[7];
		File[] files = new File(path).listFiles(new FileFilter() {
			public boolean accept(File path) {
				if (path.isFile()) {
					return true;
				}
				return false;
			}
		});

		// Analyze all files in the FeaturesSelected directory
		for (int j = 0; j < files.length; j++) {
			Instances data = DataSource.read(files[j].toString());
			data.setClassIndex(data.numAttributes() - 1);
			System.out.println("=========== Analyzing: " + data.relationName() + " =================");

			int n = data.numClasses();
			for (int i = 0; i < n; i++) {
				if (NB) {
					NaiveBayes(data, 10, i, data.relationName(), args[9]);
				}
				if (RF) {
					RandomForest(data, 10, i, data.relationName(), args[9]);
				}
				if (RT) {
					RandomTree(data, 10, i, data.relationName(), args[9]);
				}

			}
		}
		//System.exit(1);
	}

	public static void RandomForest(Instances data, int CrossValidation, int classIndex, String ClassifierName, String outputPath)
			throws Exception {
		System.out.println("Analyzing class: " + classIndex + " using RandomForest classifier ...");

		// evaluate classifier
		Classifier cl = new RandomForest();
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(cl, data, CrossValidation, new Random(1));

		// generate curve
		ThresholdCurve tc = new ThresholdCurve();
		Instances curve = tc.getCurve(eval.predictions(), classIndex);

		// plot curve
		ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
		tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		tvp.setName(curve.relationName());
		PlotData2D plotdata = new PlotData2D(curve);
		plotdata.setPlotName(curve.relationName());
		plotdata.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[curve.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		plotdata.setConnectPoints(cp);
		// add plot
		tvp.addPlot(plotdata);

		JFrame jf = new JFrame();
		jf.setSize(900, 700);
		jf.getContentPane().add(tvp);
		jf.pack();

		JComponentWriter jcw = new JPEGWriter(tvp.getPlotPanel(), new File(outputPath + ClassifierName + "_RF_Class_"
				+ classIndex + "_AUC_" + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + "_.jpg"));
		jcw.toOutput();

		System.out.println();
		System.out.println("(Area under PRC = " + Utils.doubleToString(ThresholdCurve.getPRCArea(curve), 4) + ")");
		System.out.println("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		System.out.println("Done!");
	}

	public static void NaiveBayes(Instances data, int CrossValidation, int classIndex, String ClassifierName, String outputPath)
			throws Exception {
		System.out.println("Analyzing class: " + classIndex + " using NaiveBayes classifier ...");

		// evaluate classifier
		Classifier cl = new NaiveBayes();
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(cl, data, CrossValidation, new Random(1));

		// generate curve
		ThresholdCurve tc = new ThresholdCurve();
		Instances curve = tc.getCurve(eval.predictions(), classIndex);

		// plot curve
		ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
		tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		tvp.setName(curve.relationName());
		PlotData2D plotdata = new PlotData2D(curve);
		plotdata.setPlotName(curve.relationName());
		plotdata.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[curve.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		plotdata.setConnectPoints(cp);
		// add plot
		tvp.addPlot(plotdata);

		JFrame jf = new JFrame();
		jf.setSize(900, 700);
		jf.getContentPane().add(tvp);
		jf.pack();

		JComponentWriter jcw = new JPEGWriter(tvp.getPlotPanel(), new File(outputPath + ClassifierName + "_NB_Class_"
				+ classIndex + "_AUC_" + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + "_.jpg"));
		jcw.toOutput();

		System.out.println();
		System.out.println("(Area under PRC = " + Utils.doubleToString(ThresholdCurve.getPRCArea(curve), 4) + ")");
		System.out.println("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		System.out.println("Done!");
	}

	public static void RandomTree(Instances data, int CrossValidation, int classIndex, String ClassifierName, String outputPath)
			throws Exception {
		System.out.println("Analyzing class: " + classIndex + " using RandomTree classifier ...");

		// evaluate classifier
		Classifier cl = new RandomTree();
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(cl, data, CrossValidation, new Random(1));

		// generate curve
		ThresholdCurve tc = new ThresholdCurve();
		Instances curve = tc.getCurve(eval.predictions(), classIndex);

		// plot curve
		ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
		tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		tvp.setName(curve.relationName());
		PlotData2D plotdata = new PlotData2D(curve);
		plotdata.setPlotName(curve.relationName());
		plotdata.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[curve.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		plotdata.setConnectPoints(cp);
		// add plot
		tvp.addPlot(plotdata);

		JFrame jf = new JFrame();
		jf.setSize(900, 700);
		jf.getContentPane().add(tvp);
		jf.pack();

		JComponentWriter jcw = new JPEGWriter(tvp.getPlotPanel(), new File(outputPath + ClassifierName + "_RT_Class_"
				+ classIndex + "_AUC_" + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + "_.jpg"));
		jcw.toOutput();

		System.out.println();
		System.out.println("(Area under PRC = " + Utils.doubleToString(ThresholdCurve.getPRCArea(curve), 4) + ")");
		System.out.println("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
		System.out.println("Done!");
	}
}

package cn.gucas.ml.recsys.attack.weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Debug.Random;
import cn.gucas.ml.recsys.attack.Constants;

public class Precision {
	public static int NUM_FOLDS = 10;

	public void process() {
		try {
			// J48 j48 = new J48();
			NaiveBayes nb = new NaiveBayes();

			String fileName = Constants.FEATURE_PREFIX + Constants.PATH_LEVEL
					+ Constants.FEATURE_AVERAGE_PREFIX + Constants.UNDER_LINE
					+ Constants.ATTACK_SIZE + Constants.UNDER_LINE
					+ Constants.FILLER_SIZE + Constants.WEKA_SUFFIX;
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			Instances data = new Instances(reader);
			reader.close();

			data.setClassIndex(data.numAttributes() - 1);

			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(nb, data, 10, new Random(1));
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toMatrixString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Precision precision = new Precision();
		precision.process();
	}
}
package cn.gucas.ml.recsys.attack.weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Instances;
import cn.gucas.ml.recsys.attack.Constants;

public class InfoGain {
	public static void process(int attackSize, int fillerSize, int level,
			int type) {
		try {
			// 1. load data
			String fileName = Constants.FEATURE_PREFIX
					+ Constants.LEVEL_PATH_ARRAY[level]
					+ Constants.FEATURE_PREFIX_ARRAY[type]
					+ Constants.UNDER_LINE + attackSize + Constants.UNDER_LINE
					+ fillerSize + Constants.WEKA_SUFFIX;

			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			Instances data = new Instances(reader);
			reader.close();

			data.setClassIndex(data.numAttributes() - 1);

			InfoGainAttributeEval eval = new InfoGainAttributeEval();
			eval.buildEvaluator(data);

			for (int i = 0; i < data.numAttributes() - 1; ++i) {
				if (i < data.numAttributes() - 2) {
					System.out.print(eval.evaluateAttribute(i)
							+ Constants.TABLE);
				} else {
					System.out.println(eval.evaluateAttribute(i));
				}
			}
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
}
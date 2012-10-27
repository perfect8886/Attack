package cn.gucas.ml.recsys.attack.weka;

import cn.gucas.ml.recsys.attack.Constants;
import cn.gucas.ml.recsys.attack.processor.Attacker;
import cn.gucas.ml.recsys.attack.processor.Detecter;

public class Generator {
	public void generateAttackFile() {
		for (int i = 0; i < Constants.ATTACK_SIZE_ARRAY.length; ++i) {
			Constants.ATTACK_SIZE = Constants.ATTACK_SIZE_ARRAY[i];
			for (int j = 0; j < Constants.FILLER_SIZE_ARRAY.length; ++j) {
				Constants.FILLER_SIZE = Constants.FILLER_SIZE_ARRAY[j];
				for (int type = 0; type < 3; ++type) {
					Constants.init();
					Attacker.attack(type);
				}
			}
		}
	}

	public void generateDetectFile() {
		for (int i = 0; i < Constants.ATTACK_SIZE_ARRAY.length; ++i) {
			Constants.ATTACK_SIZE = Constants.ATTACK_SIZE_ARRAY[i];
			for (int j = 0; j < Constants.FILLER_SIZE_ARRAY.length; ++j) {
				Constants.FILLER_SIZE = Constants.FILLER_SIZE_ARRAY[j];
				for (int type = 0; type < 3; ++type) {
					for (int level = 0; level < 3; ++level) {
						Constants.LEVEL = level;
						Constants.PATH_LEVEL = Constants.LEVEL_PATH_ARRAY[level];
						Detecter.detect(type);
					}
				}
			}
		}
	}

	public void generateInfoGain(int attackSize, int level, int type) {
		for (int j = 0; j < Constants.FILLER_SIZE_ARRAY.length; ++j) {
			int fillerSize = Constants.FILLER_SIZE_ARRAY[j];
			InfoGain.process(attackSize, fillerSize, level, type);
		}
	}

	public static void main(String[] args) {
		Generator generator = new Generator();
		// generator.generateAttackFile();
		// generator.generateDetectFile();
		generator.generateInfoGain(Constants.ATTACK_SIZE_ARRAY[3], 2, 0);
	}
}
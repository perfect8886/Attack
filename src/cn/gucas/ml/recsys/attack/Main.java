package cn.gucas.ml.recsys.attack;

import cn.gucas.ml.recsys.attack.processor.Attacker;
import cn.gucas.ml.recsys.attack.processor.Detecter;

public class Main {
	public static void main(String[] args) {
		String method = args[0];
		int type = Integer.parseInt(args[1]);
		int fillerSize = Integer.parseInt(args[2]);
		int level = Integer.parseInt(args[3]);

		Constants.FILLER_SIZE = fillerSize;
		System.out.println("filler size : " + Constants.FILLER_SIZE);

		Constants.LEVEL = level;
		if (level == 0) {
			Constants.PATH_LEVEL = "/origin";
		} else if (level == 1) {
			Constants.PATH_LEVEL = "/shift";
		} else if (level == 2) {
			Constants.PATH_LEVEL = "/interest";
		}

		if (method.equals("a")) {
			Attacker.attack(type);
		} else {
			Detecter.detect(type);
		}
	}
}
package cn.gucas.ml.recsys.attack.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.gucas.ml.recsys.attack.Constants;
import cn.gucas.ml.recsys.attack.Constants.Type;
import cn.gucas.ml.recsys.attack.profile.AttackProfile;
import cn.gucas.ml.recsys.attack.profile.Record;

public class Attacker {
	public static void attack(int type) {
		int attackSize = Constants.ATTACK_SIZE * Constants.USER_NUM / 100;
		System.out.println("ATTACK SIZE : " + attackSize);
		List<Record> list = Loader.load(Constants.ORIGIN_PATH);
		String fileName = "";

		Constants.loadOriginArray();
		Constants.loadItemAverage();

		if (type == Type.AVERAGE_ATTACK) {
			fileName = Constants.AVERAGE_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else if (type == Type.RANDOM_ATTACK) {
			Constants.loadMiuAndSigma();
			fileName = Constants.RANDOM_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else if (type == Type.BANDWAGON_ATTACK) {
			Constants.loadBestSeller();
			Constants.loadMiuAndSigma();
			fileName = Constants.BANDWAGON_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else {
			// do nothing
		}

		int count = 0;
		while (count < attackSize) {
			int uid = Constants.nextUid();
			int tiid = 0;
			while (true) {
				tiid = (int) (1 + Math.random() * Constants.ITEM_NUM);
				if (Constants.ITEM_AVERAGE[tiid - 1] < 3.5) {
					break;
				}
			}
			boolean push = true;
			AttackProfile profile = new AttackProfile(uid, tiid, push, type,
					Constants.SELECT_SIZE, Constants.FILLER_SIZE);
			profile.generate();
			HashMap<Integer, Integer> items = profile.getItems();
			// add the items to the list
			Iterator<Integer> keys = items.keySet().iterator();
			while (keys.hasNext()) {
				int iid = keys.next();
				Record record = new Record();
				record.setUid(uid);
				record.setAttack(1);
				record.setIid(iid);
				record.setRating(items.get(iid));

				list.add(record);
			}
			++count;
		}

		// generate dataset
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(new File(fileName))));
			Iterator<Record> iter = list.iterator();
			while (iter.hasNext()) {
				Record record = iter.next();
				StringBuilder sb = new StringBuilder();
				sb.append(record.getUid());
				sb.append(Constants.TABLE);
				sb.append(record.getIid());
				sb.append(Constants.TABLE);
				sb.append(record.getRating());
				sb.append(Constants.TABLE);
				sb.append(record.getAttack());

				pw.println(sb.toString());
			}

			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generatePushNukeList() {
		int[][] array = Loader.load(Constants.ORIGIN_PATH, Constants.USER_NUM,
				Constants.ITEM_NUM);

		double[] itemAverage = new double[Constants.ITEM_NUM];
		int[] itemCount = new int[Constants.ITEM_NUM];

		for (int j = 0; j < Constants.ITEM_NUM; ++j) {
			int count = 0;
			double sum = 0;
			for (int i = 0; i < Constants.USER_NUM; ++i) {
				if (array[i][j] > 0) {
					++count;
					sum += array[i][j];
				}
			}
			itemCount[j] = count;
			itemAverage[j] = sum / count;
		}

		int pushCount = 0;
		int nukeCount = 0;

		int maxAttackSize = Constants.MAX_ATTACK_SIZE * Constants.USER_NUM
				/ 100;
		int index = 0;
		while (pushCount < maxAttackSize || nukeCount < maxAttackSize) {
			index = (int) (Math.random() * Constants.ITEM_NUM);
			if (itemAverage[index] > 3.6 && itemCount[index] > 20) {

			}
		}
	}

	public static void main(String[] args) {
		Attacker.attack(Type.RANDOM_ATTACK);
	}
}

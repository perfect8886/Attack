package cn.gucas.ml.recsys.attack;

import cn.gucas.ml.recsys.attack.processor.Loader;
import cn.gucas.ml.recsys.attack.utils.Item;
import cn.gucas.ml.recsys.attack.utils.ItemHeap;

public class Constants {
	public static String[] LEVEL_PATH_ARRAY = { "/origin", "/shift",
			"/interest" };
	public static String[] FEATURE_PREFIX_ARRAY = { "/random", "/average",
			"/bandwagon" };
	public static int[] ATTACK_SIZE_ARRAY = { 1, 2, 5, 10 };
	public static int[] FILLER_SIZE_ARRAY = { 1, 3, 5, 10, 25, 40, 60 };

	public static int LEVEL = 1;
	public static int ATTACK_SIZE = 5;
	public static int FILLER_SIZE = 2;
	public static int SELECT_SIZE = 0;

	public static final String TABLE = "	";
	public static final String COMMA = ",";
	public static final String VERTICAL_LINE = "\\|";

	public static final int USER_NUM = 943;
	public static final int ITEM_NUM = 1682;

	public static final int ATTRIBUTE_NUM = 19;

	public static int LAST_USER_ID = USER_NUM + 1;

	public static void init() {
		LAST_USER_ID = USER_NUM + 1;
	}

	public static int nextUid() {
		return LAST_USER_ID++;
	}

	public static final String INTEREST_PATH = "./dataset/interest.data";

	public static final String ORIGIN_PATH = "./dataset/u.data";

	public static final String PATH_PREFIX = "./dataset/u";

	public static final String PATH_SUFFIX = ".data";

	public static final String PUSH_PATH = "./dataset/push.data";
	public static final String NUKE_PATH = "./dataset/nuke.data";

	public static final String RANDOM_PREFIX = "./dataset/attack/random";
	public static final String AVERAGE_PREFIX = "./dataset/attack/average";
	public static final String BANDWAGON_PREFIX = "./dataset/attack/bandwagon";
	public static final String UNDER_LINE = "_";

	public static final String FEATURE_PREFIX = "./dataset/feature";
	public static String PATH_LEVEL = "/shift";
	public static final String FEATURE_BANDWAGON_PREFIX = "/bandwagon";
	public static final String FEATURE_RANDOM_PREFIX = "/random";
	public static final String FEATURE_AVERAGE_PREFIX = "/average";
	public static final String WEKA_SUFFIX = ".arff";

	public static final String ITEM_PATH = "./dataset/u.item";

	public static final String KURTOSIS_PREFIX = "./dataset/detect/kurtosis";

	public final class Type {
		public static final int RANDOM_ATTACK = 0;
		public static final int AVERAGE_ATTACK = 1;
		public static final int BANDWAGON_ATTACK = 2;
		public static final int SEGMENT_ATTACK = 3;
		public static final int LOVE_HATE_ATTACK = 4;
	}

	public final class Rate {
		public static final int MAX = 5;
		public static final int MIN = 1;
	}

	public static int MAX_ATTACK_SIZE = 15;

	// data in memory
	public static int[][] ORIGIN_ARRAY;

	public static void loadOriginArray() {
		System.out.println("load original array...");
		ORIGIN_ARRAY = Loader.load(ORIGIN_PATH, USER_NUM, ITEM_NUM);
	}

	public static double MIU = 3.5;
	public static double SIGMA = 1.1;
	public static int COUNT = 0;

	public static void loadMiuAndSigma() {
		System.out.println("load miu and sigma...");
		// calculate the distribution of the original dataset
		for (int i = 0; i < USER_NUM; ++i) {
			for (int j = 0; j < ITEM_NUM; ++j) {
				if (ORIGIN_ARRAY[i][j] > 0) {
					MIU += ORIGIN_ARRAY[i][j];
					++COUNT;
				}
			}
		}
		MIU = MIU / COUNT;

		for (int i = 0; i < USER_NUM; ++i) {
			for (int j = 0; j < ITEM_NUM; ++j) {
				if (ORIGIN_ARRAY[i][j] > 0) {
					SIGMA += Math.pow(MIU - ORIGIN_ARRAY[i][j], 2);
				}
			}
		}

		SIGMA = Math.sqrt(SIGMA / COUNT);

		System.out.println("MIU : " + MIU);
		System.out.println("SIGMA : " + SIGMA);
	}

	public static double[] ITEM_AVERAGE = new double[ITEM_NUM];
	public static int[] ITEM_LENGTH = new int[ITEM_NUM];

	public static void loadItemAverage() {
		System.out.println("load item average rating...");
		// calculate the distribution of the original dataset
		for (int j = 0; j < ITEM_NUM; ++j) {
			double sum = 0.0;
			int count = 0;
			for (int i = 0; i < USER_NUM; ++i) {
				if (ORIGIN_ARRAY[i][j] > 0) {
					sum += ORIGIN_ARRAY[i][j];
					++count;
				}
			}
			if (count > 0) {
				ITEM_AVERAGE[j] = sum / count;
			} else {
				ITEM_AVERAGE[j] = MIU;
			}
			ITEM_LENGTH[j] = count;
		}
	}

	public static int BEST_SELLER_SIZE = 5;
	public static int[] BEST_SELLER = new int[BEST_SELLER_SIZE];

	public static void loadBestSeller() {
		System.out.println("load best seller...");
		loadOriginArray();
		loadItemAverage();
		Item[] items = new Item[ITEM_NUM];
		for (int i = 0; i < ITEM_NUM; ++i) {
			Item item = new Item();
			item.setId(i + 1);
			item.setRate(ITEM_LENGTH[i]);
			items[i] = item;
		}

		ItemHeap heap = new ItemHeap(items);

		BEST_SELLER = heap.loadBestSeller(BEST_SELLER_SIZE);
	}
}
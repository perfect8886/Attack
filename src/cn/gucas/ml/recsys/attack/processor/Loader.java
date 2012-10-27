package cn.gucas.ml.recsys.attack.processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.gucas.ml.recsys.attack.Constants;
import cn.gucas.ml.recsys.attack.profile.Record;

public class Loader {

	public static int[][] loadMovieAttribute() {
		// init
		int[][] array = new int[Constants.ITEM_NUM][Constants.ATTRIBUTE_NUM];
		for (int i = 0; i < Constants.ITEM_NUM; ++i) {
			for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
				array[i][j] = 0;
			}
		}

		// load
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(Constants.ITEM_PATH));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(Constants.VERTICAL_LINE);
				int iid = Integer.parseInt(splits[0]);
				for (int i = 5; i < 23; ++i) {
					int aid = i - 5;
					array[iid - 1][aid] = Integer.parseInt(splits[i]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}

	public static double[][] loadRecord(String path, int row, int column) {
		double[][] array = new double[row][column];
		// init
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < column; ++j) {
				array[i][j] = 0;
			}
		}

		// load
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(Constants.TABLE);
				int uid = Integer.parseInt(splits[0]);
				int iid = Integer.parseInt(splits[1]);
				int rate = Integer.parseInt(splits[2]);

				array[uid - 1][iid - 1] = rate - 2;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}

	public static int[][] loadAttack(String path, int row, int column) {
		int[][] array = new int[row][column];
		// init
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < column; ++j) {
				array[i][j] = 0;
			}
		}

		// load
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(Constants.TABLE);
				int uid = Integer.parseInt(splits[0]);
				int iid = Integer.parseInt(splits[1]);
				int rate = Integer.parseInt(splits[2]);
				int attack = Integer.parseInt(splits[3]);

				array[uid - 1][iid - 1] = rate;
				array[uid - 1][column - 1] = attack;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}

	public static List<Record> load(String path) {
		List<Record> list = new ArrayList<Record>();
		// load
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(Constants.TABLE);
				int uid = Integer.parseInt(splits[0]);
				int iid = Integer.parseInt(splits[1]);
				int rate = Integer.parseInt(splits[2]);
				long time = Long.parseLong(splits[3]);
				int attack = 0;

				Record record = new Record();
				record.setAttack(attack);
				record.setIid(iid);
				record.setRating(rate);
				record.setTimestamp(time);
				record.setUid(uid);
				list.add(record);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public static int[][] load(String path, int row, int column) {
		int[][] array = new int[row][column];
		// init
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < column; ++j) {
				array[i][j] = 0;
			}
		}

		// load
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(Constants.TABLE);
				int uid = Integer.parseInt(splits[0]);
				int iid = Integer.parseInt(splits[1]);
				int rate = Integer.parseInt(splits[2]);

				array[uid - 1][iid - 1] = rate;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return array;
	}
}
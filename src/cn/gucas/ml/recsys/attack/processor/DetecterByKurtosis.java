package cn.gucas.ml.recsys.attack.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import Jama.Matrix;
import cn.gucas.ml.recsys.attack.Constants;

public class DetecterByKurtosis {
	public static void detect() {
		int row = Constants.USER_NUM + Constants.ATTACK_SIZE
				* Constants.USER_NUM / 100;
		int column = Constants.ITEM_NUM;

		int[][] M = Loader.loadMovieAttribute();
		double[][] U = Loader.loadRecord(Constants.BANDWAGON_PREFIX
				+ Constants.ATTACK_SIZE + Constants.UNDER_LINE
				+ Constants.FILLER_SIZE + Constants.PATH_SUFFIX, row, column);

		// 1. calculate matrix T and normalize
		double[][] T = new double[row][Constants.ATTRIBUTE_NUM];

		for (int i = 0; i < row; ++i) {
			double sum = 0;
			for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
				for (int k = 0; k < Constants.ITEM_NUM; ++k) {
					T[i][j] += U[i][k] * M[k][j];
				}
				sum += Math.abs(T[i][j]);
			}

			// for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
			// T[i][j] /= sum;
			// }
		}

		// 0.1 calculate the average interest ratio
		double[] averageInterest = new double[Constants.ATTRIBUTE_NUM];
		for (int i = 0; i < Constants.ATTRIBUTE_NUM; ++i) {
			double average = 0;
			for (int j = 0; j < row; ++j) {
				average += T[j][i];
			}
			averageInterest[i] /= row;
		}
		//
		// // 0.2
		// for (int i = 0; i < row; ++i) {
		// for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
		// T[i][j] = T[i][j] - averageInterest[j];
		// }
		// }
		//
		// double[] averageUser = new double[row];
		// for (int i = 0; i < row; ++i) {
		// double average = 0;
		// for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
		// average += T[i][j];
		// }
		// averageUser[i] /= Constants.ATTRIBUTE_NUM;
		// }
		//
		// double[] K = new double[row];
		// for (int i = 0; i < row; ++i) {
		// double sum = 0;
		// for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
		// sum += Math.pow(T[i][j], 2);
		// }
		// K[i] = Math.sqrt(sum / Constants.ATTRIBUTE_NUM);
		// }

		// 1.0 print T
		// Matrix mT = new Matrix(T);
		// try {
		// PrintWriter writer = new PrintWriter(new OutputStreamWriter(
		// new FileOutputStream(Constants.INTEREST_PATH)), true);
		// mT.print(writer, 5, 3);
		// writer.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// 1.1 LSA
		// int k = 4;
		//
		// Matrix mT = new Matrix(T);
		// Matrix mS = mT.svd().getS();
		// Matrix mU = mT.svd().getU();
		// Matrix mV = mT.svd().getV();
		//
		// mS = mS.getMatrix(0, k, 0, k);
		// mU = mU.getMatrix(0, row - 1, 0, k);
		// mV = mV.getMatrix(0, k, 0, Constants.ATTRIBUTE_NUM - 1);
		//
		// mT = mU.times(mS).times(mV);
		//
		// T = mT.getArray();

		// 2.0 choose the most interested style
		// double[] K = new double[row];
		//
		// for (int i = 0; i < row; ++i) {
		// double temp = 0;
		// for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
		// if (T[i][j] > temp) {
		// temp = T[i][j];
		// }
		// }
		// K[i] = temp;
		// }

		// 2. calculte kurtosis of every user
		double[] K = new double[row];

		for (int i = 0; i < row; ++i) {
			double average = 1 / Constants.ATTRIBUTE_NUM;

			double u4 = 0;
			double u3 = 0;
			double u2 = 0;
			double u1 = 0;
			for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
				u4 += Math.pow(T[i][j] - averageInterest[j], 4);
				u3 += Math.pow(T[i][j] - averageInterest[j], 3);
				u2 += Math.pow(T[i][j] - averageInterest[j], 2);
				u1 += Math.abs(T[i][j] - averageInterest[j]);
			}
			u4 = u4 / Constants.ATTRIBUTE_NUM;
			u3 = u3 / Constants.ATTRIBUTE_NUM;
			u2 = u2 / Constants.ATTRIBUTE_NUM;
			u1 = u1 / Constants.ATTRIBUTE_NUM;
			// K[i] = u4 / Math.pow(u2, 2) - 3;
			// K[i] = u3 / Math.pow(u2, 3 / 2);
			K[i] = Math.sqrt(u2);
			// K[i] = u1;
		}

		// 3. output to txt
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(new File(Constants.KURTOSIS_PREFIX
							+ Constants.ATTACK_SIZE + Constants.UNDER_LINE
							+ Constants.FILLER_SIZE + Constants.PATH_SUFFIX))));
			for (int i = 0; i < row; ++i) {
				pw.println(K[i]);
			}

			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DetecterByKurtosis.detect();
	}
}
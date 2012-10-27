package cn.gucas.ml.recsys.attack.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import cn.gucas.ml.recsys.attack.Constants;
import cn.gucas.ml.recsys.attack.Constants.Type;

public class Detecter {
	public static void detect(int type) {
		String fileName = "";
		if (type == Type.AVERAGE_ATTACK) {
			fileName = Constants.AVERAGE_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else if (type == Type.RANDOM_ATTACK) {
			fileName = Constants.RANDOM_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else if (type == Type.BANDWAGON_ATTACK) {
			fileName = Constants.BANDWAGON_PREFIX + Constants.ATTACK_SIZE
					+ Constants.UNDER_LINE + Constants.FILLER_SIZE
					+ Constants.PATH_SUFFIX;
		} else {
			// do nothing
		}
		int row = Constants.USER_NUM + Constants.ATTACK_SIZE
				* Constants.USER_NUM / 100;
		int column = Constants.ITEM_NUM + 1;
		int[][] attackArray = Loader.loadAttack(fileName, row, column);

		double[][] featureArray = new double[row][11];

		// step 1: calculate the average rating/length of each item
		double[] itemAverage = new double[Constants.ITEM_NUM];
		int[] itemLength = new int[Constants.ITEM_NUM];
		for (int j = 0; j < Constants.ITEM_NUM; ++j) {
			double sum = 0.0;
			int count = 0;
			for (int i = 0; i < row; ++i) {
				if (attackArray[i][j] > 0) {
					sum += attackArray[i][j];
					++count;
				}
			}
			if (count > 0) {
				itemAverage[j] = sum / count;
			} else {
				itemAverage[j] = Constants.MIU;
			}
			itemLength[j] = count;
		}

		// step 2: calculate the length & average length of users
		int[] userLength = new int[row];
		double averageLength = 0.0;
		for (int i = 0; i < row; ++i) {
			int count = 0;
			for (int j = 0; j < Constants.ITEM_NUM; ++j) {
				if (attackArray[i][j] > 0) {
					++count;
				}
			}
			userLength[i] = count;
			averageLength += count;
		}
		averageLength /= row;

		double sumLength = 0.0;
		for (int i = 0; i < row; ++i) {
			sumLength += Math.pow((double) userLength[i] - averageLength, 2);
		}

		// step 3: calculate the similarity of each user
		double[][] userSim = new double[row][row];

		// step 3.1: calculate the average rate of each user
		double[] userAverageRate = new double[row];
		for (int i = 0; i < row; ++i) {
			double average = 0;
			int count = 0;
			for (int j = 0; j < Constants.ITEM_NUM; ++j) {
				if (attackArray[i][j] > 0) {
					average += attackArray[i][j];
					++count;
				}
			}
			if (count > 0) {
				average /= count;
			} else {
				average = 0;
			}
			userAverageRate[i] = average;
		}

		// step 3.2: calculate the pearson similarity of each user
		// for (int i = 0; i < row; ++i) {
		// for (int j = i + 1; j < row; ++j) {
		// double numerator = 0.0;
		// double denominator1 = 0.0;
		// double denominator2 = 0.0;
		// for (int k = 0; k < Constants.ITEM_NUM; ++k) {
		// numerator += (attackArray[i][k] - userAverageRate[i])
		// * (attackArray[j][k] - userAverageRate[j]);
		// denominator1 += Math.pow(attackArray[i][k]
		// - userAverageRate[i], 2);
		// denominator2 += Math.pow(attackArray[j][k]
		// - userAverageRate[j], 2);
		// }
		// userSim[i][j] = userSim[j][i] = numerator
		// / Math.sqrt(denominator1 * denominator2);
		// }
		// }
		// for (int i = 0; i < row; ++i) {
		// userSim[i][i] = 1;
		// }

		// step 4: calculate the interest of each user
		int[][] M = Loader.loadMovieAttribute();
		double[][] U = Loader.loadRecord(fileName, row, Constants.ITEM_NUM);
		double[][] T = new double[row][Constants.ATTRIBUTE_NUM];
		if (Constants.LEVEL == 2) {
			for (int i = 0; i < row; ++i) {
				double sum = 0;
				for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
					for (int k = 0; k < Constants.ITEM_NUM; ++k) {
						T[i][j] += U[i][k] * M[k][j];
					}
					sum += Math.abs(T[i][j]);
				}

				for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
					T[i][j] /= sum;
				}
			}
		}
		double[] averageInterest = new double[Constants.ATTRIBUTE_NUM];
		if (Constants.LEVEL == 2) {
			for (int i = 0; i < Constants.ATTRIBUTE_NUM; ++i) {
				double average = 0;
				for (int j = 0; j < row; ++j) {
					average += T[j][i];
				}
				averageInterest[i] = average / row;
			}
		}
		//
		// // step 4.1: LSA
		// int k = 0;
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
		// T = mT.getArray();

		// step 5: PCA
		// step 5.1: z-score
		// double[] userMiuSim = new double[row];
		// double[] userSigmaSim = new double[row];
		// for (int i = 0; i < row; ++i) {
		// for (int j = 0; j < row; ++j) {
		// userMiuSim[i] += userSim[i][j];
		// }
		// userMiuSim[i] /= row;
		// }
		//
		// for (int i = 0; i < row; ++i) {
		// for (int j = 0; j < row; ++j) {
		// userSigmaSim[i] += Math.pow(userMiuSim[i] - userSim[i][j], 2);
		// }
		// userSigmaSim[i] = Math.sqrt(userSigmaSim[i] / row);
		// }
		//
		// for (int i = 0; i < row; ++i) {
		// for (int j = 0; j < row; ++j) {
		// userSim[i][j] = (userSim[i][j] - userMiuSim[i])
		// / userSigmaSim[i];
		// }
		// }
		//
		// // step 5.2: Eigenvalue-Decoposition
		// Matrix D = new Matrix(userSim);
		// Matrix eU = D.svd().getU();

		// step 6: calculate RDMA/WDMA/WDA/LengthVar/DegSim/Kurtosis
		double[] RDMA = new double[row];
		double[] WDMA = new double[row];
		double[] WDA = new double[row];
		double[] LengthVar = new double[row];
		double[] DegSim = new double[row];
		double[] Kurtosis = new double[row];
		double[] PCA = new double[row];
		double[] HV = new double[row];
		double[] SV = new double[row];
		double[] MeanVar = new double[row];

		// Heap heap;
		for (int i = 0; i < row; ++i) {
			int count = 0;
			double rdma = 0.0;
			double wdma = 0.0;
			double hv1 = 0.0;
			double hv2 = 0.0;
			double sv1 = 0.0;
			double sv2 = 0.0;
			double mv = 0.0;
			int mvNum = 0;

			for (int j = 0; j < Constants.ITEM_NUM; ++j) {
				if (attackArray[i][j] > 0) {
					if (attackArray[i][j] != 5) {
						++mvNum;
						mv += Math.pow(attackArray[i][j] - userAverageRate[i],
								2);
					}
					if (Constants.LEVEL == 0) {
						rdma += Math.abs((double) attackArray[i][j]
								- itemAverage[j])
								/ itemLength[j];
						wdma += Math.abs((double) attackArray[i][j]
								- itemAverage[j])
								/ (itemLength[j] * itemLength[j]);
					} else {
						rdma += Math.abs((double) attackArray[i][j]
								- itemAverage[j] - userAverageRate[i]
								+ Constants.MIU)
								/ itemLength[j];
						wdma += Math.abs((double) attackArray[i][j]
								- itemAverage[j]
								- (userAverageRate[i] - Constants.MIU))
								/ (itemLength[j] * itemLength[j]);
					}

					hv1 += Math.pow((double) attackArray[i][j] - itemAverage[j]
							- userAverageRate[i] + Constants.MIU, 2);
					hv2 += Math.pow((double) attackArray[i][j]
							- userAverageRate[i], 2);

					sv1 += Math.pow((double) attackArray[i][j] - itemAverage[j]
							- userAverageRate[i] + Constants.MIU, 2);
					sv2 += Math.pow(
							(double) attackArray[i][j] - itemAverage[j], 2);
					++count;
				}
			}
			RDMA[i] = rdma / count;
			WDMA[i] = wdma / count;
			WDA[i] = rdma;

			HV[i] = hv1 / hv2;
			SV[i] = sv1 / sv2;

			MeanVar[i] = mv / mvNum;

			LengthVar[i] = Math.abs((double) userLength[i] - averageLength)
					/ sumLength;

			// DegSim
			// int K = 25;
			// heap = new Heap(userSim[i]);
			// DegSim[i] = heap.getDegSim(K);

			// Kurtosis
			if (Constants.LEVEL == 2) {
				double u1 = 0;
				double u2 = 0;
				for (int j = 0; j < Constants.ATTRIBUTE_NUM; ++j) {
					u1 += Math.pow(T[i][j] - averageInterest[j], 2);
					u2 += Math.pow(T[i][j] - 1 / Constants.ATTRIBUTE_NUM, 2);
				}
				// Kurtosis[i] = u4 / Math.pow(u2, 2) - 3;
				// Kurtosis[i] = u3 / Math.pow(u2, 3 / 2);
				// Kurtosis[i] = Math.sqrt(u2);
				// Kurtosis[i] = mU.get(i, 0);
				Kurtosis[i] = u1 / u2;
			}
			// PCA
			// PCA[i] = Math.pow(eU.get(i, 0), 2) + Math.pow(eU.get(i, 1), 2);

			featureArray[i][0] = RDMA[i];
			featureArray[i][1] = WDMA[i];
			featureArray[i][2] = WDA[i];
			featureArray[i][3] = LengthVar[i];
			featureArray[i][4] = DegSim[i];
			featureArray[i][5] = Kurtosis[i];
			featureArray[i][6] = PCA[i];
			featureArray[i][7] = HV[i];
			featureArray[i][8] = SV[i];
			featureArray[i][9] = MeanVar[i];
			featureArray[i][10] = attackArray[i][Constants.ITEM_NUM];
		}

		// generate feature dataset
		try {
			File file = null;
			if (type == Type.BANDWAGON_ATTACK) {
				file = new File(Constants.FEATURE_PREFIX + Constants.PATH_LEVEL
						+ Constants.FEATURE_BANDWAGON_PREFIX
						+ Constants.UNDER_LINE + Constants.ATTACK_SIZE
						+ Constants.UNDER_LINE + Constants.FILLER_SIZE
						+ Constants.WEKA_SUFFIX);
			} else if (type == Type.AVERAGE_ATTACK) {
				file = new File(Constants.FEATURE_PREFIX + Constants.PATH_LEVEL
						+ Constants.FEATURE_AVERAGE_PREFIX
						+ Constants.UNDER_LINE + Constants.ATTACK_SIZE
						+ Constants.UNDER_LINE + Constants.FILLER_SIZE
						+ Constants.WEKA_SUFFIX);
			} else if (type == Type.RANDOM_ATTACK) {
				file = new File(Constants.FEATURE_PREFIX + Constants.PATH_LEVEL
						+ Constants.FEATURE_RANDOM_PREFIX
						+ Constants.UNDER_LINE + Constants.ATTACK_SIZE
						+ Constants.UNDER_LINE + Constants.FILLER_SIZE
						+ Constants.WEKA_SUFFIX);
			}
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file)));

			pw.println("@relation movie");
			pw.println();
			pw.println("@attribute RDMA real");
			pw.println("@attribute WDMA real");
			pw.println("@attribute WDA real");
			pw.println("@attribute LengthVar real");
			// pw.println("@attribute DegSim real");
			pw.println("@attribute Kurtosis real");
			// pw.println("@attribute PCA real");
			pw.println("@attribute HV real");
			pw.println("@attribute SV real");
			pw.println("@attribute MeanVar real");
			pw.println("@attribute attack {1.0, 0.0}");
			pw.println();
			pw.println("@data");
			DecimalFormat df = new DecimalFormat("#0.0#########");
			for (int i = 0; i < row; ++i) {
				StringBuilder sb = new StringBuilder();
				sb.append(df.format(featureArray[i][0]));
				sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][1]));
				sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][2]));
				sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][3]));
				sb.append(Constants.COMMA);
				// sb.append(df.format(featureArray[i][4]));
				// sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][5]));
				sb.append(Constants.COMMA);
				// sb.append(df.format(featureArray[i][6]));
				// sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][7]));
				sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][8]));
				sb.append(Constants.COMMA);
				sb.append(df.format(featureArray[i][9]));
				sb.append(Constants.COMMA);
				sb.append(String.valueOf(featureArray[i][10]));
				pw.println(sb.toString());
			}

			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double beta(int len) {
		double beta = 0.0;
		beta = 0.5 * (1 - Math.pow(Math.E, 0.5 * (20 - len)))
				/ (1 + Math.pow(Math.E, 0.5 * (20 - len)));

		return beta;
	}

	public static double alpha(int len) {
		double alpha = 0.0;
		alpha = 1 / (1 + Math.pow(Math.E, -0.5 * len));
		return alpha;
	}

	public static void main(String[] args) {
		Detecter.detect(Type.RANDOM_ATTACK);
	}
}
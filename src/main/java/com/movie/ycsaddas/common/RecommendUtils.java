package com.movie.ycsaddas.common;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import org.ujmp.core.SparseMatrix;
import org.ujmp.core.calculation.Calculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static org.ujmp.core.util.MathUtil.round;

/**
 * @author walker
 * @date 2018/06/04
 */
public class RecommendUtils {

	public static Matrix readFileAsRatingsMatrix(String filepath) {
		/**
		 * @Method_name: readFileAsRatingsMatrix
		 * @Description: 读取评分数据，构建评分矩阵
		 * @Date: 2017/9/30
		 * @Time: 14:26
		 * @param: [filepath]文件路径
		 * @return: org.ujmp.core.Matrix
		 **/
		Set<Double> userSet = new HashSet();
		Set<Double> itemSet = new HashSet();
		try {
			Scanner in = new Scanner(new File(filepath));
			while (in.hasNext()) {
				String str = in.nextLine();
				Double user = Double.parseDouble(str.split("\t")[0]) - 1;
				Double item = Double.parseDouble(str.split("\t")[1]) - 1;
				userSet.add(user);
				itemSet.add(item);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Integer userCounts = userSet.size();
		Integer itemCounts = itemSet.size();
		Matrix ratingsMatrix = SparseMatrix.Factory.zeros(userCounts, itemCounts);

		try {
			Scanner in = new Scanner(new File(filepath));
			while (in.hasNext()) {
				String str = in.nextLine();
				Long user = Long.parseLong(str.split("\t")[0]);
				Long item = Long.parseLong(str.split("\t")[1]);
				Double rating = Double.parseDouble(str.split("\t")[2]);
				ratingsMatrix.setAsDouble(rating, user - 1, item - 1);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ratingsMatrix;

	}


	public static Matrix readFileAsItemsFeatureMatrix(String filepath) {
		/**
		 * @Method_name: readFileAsItemsFeatureMatrix
		 * @Description: 读取商品的特征文件，构建特征举证
		 * @Date: 2017/9/30
		 * @Time: 14:28
		 * @param: [filepath] 商品特征路径
		 * @return: org.ujmp.core.Matrix
		 **/
		ArrayList<String> itemsFeatures = new ArrayList(Arrays.asList("unknown", "Action", "Adventure", "Animation", "Childrens", "Comedy", "Crime", "Documentary",
				"Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery",
				"Romance", "Sci-Fi", "Thriller", "War", "Western"));
		Integer featuresCount = itemsFeatures.size();
		Integer itemsCount = 0;
		try {
			Scanner in = new Scanner(new File(filepath));
			while (in.hasNext()) {
				String str = in.nextLine();
				itemsCount++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Matrix itemsFeatureMatrix = SparseMatrix.Factory.zeros(itemsCount, featuresCount);
		try {
			Scanner in = new Scanner(new FileInputStream(filepath));
			Integer startIndx = 5;
			Integer count = 0;
			while (in.hasNext()) {
				count++;
				String str = in.nextLine();
				String[] content = str.split("\\|");
				Long itemId = Long.parseLong(content[0]) - 1;
				for (int i = startIndx; i < content.length; i++) {
					Integer feature = Integer.parseInt(content[i]);
					itemsFeatureMatrix.setAsInt(feature, itemId - 1, i - startIndx);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return itemsFeatureMatrix;
	}

	/**
	 * @Description: 未评分的使用替代评分策略，选项包括使用items的平均分和用户的平均分
	 * @param: [R, inp] R:输入的矩阵 inp:选择插值方法
	 * @return: org.ujmp.core.Matrix
	 **/
	public static Matrix impulation(Matrix R, String inp) {
		Matrix R_Tmp = R.clone();
		Long rowCount = R_Tmp.getRowCount();
		Long columnCount = R_Tmp.getColumnCount();
		if (inp.equals("userAverage")) {
			for (int i = 0; i < rowCount; i++) {
				Matrix Ri = R_Tmp.selectRows(Calculation.Ret.NEW, i);
				Double replaceValue = Ri.getValueSum();
				Double nonZeroCounts = 0.0;
				for (int j = 0; j < columnCount; j++) {
					if (R_Tmp.getAsDouble(i, j) > 0)
						nonZeroCounts++;
				}
				replaceValue = replaceValue / nonZeroCounts;
				for (int j = 0; j < columnCount; j++) {
					if (R_Tmp.getAsDouble(i, j) == 0)
						R_Tmp.setAsDouble(replaceValue, i, j);
				}
			}
			return R_Tmp;
		} else {
			for (int j = 0; j < columnCount; j++) {
				Matrix Rj = R_Tmp.selectColumns(Calculation.Ret.NEW, j);
				Double replaceValue = Rj.getValueSum();
				Double nonZeroCounts = 0.0;
				for (int i = 0; i < rowCount; i++) {
					if (R_Tmp.getAsDouble(i, j) > 0)
						nonZeroCounts++;
				}
				replaceValue = replaceValue / nonZeroCounts;
				for (int i = 0; i < rowCount; i++) {
					if (R_Tmp.getAsDouble(i, j) == 0)
						R_Tmp.setAsDouble(replaceValue, i, j);
				}

			}
			return R_Tmp;
		}
	}

	/**
	 * @Description: 获取两行数据的相关性系数
	 * @param: [x, y, type] x:矩阵x y:矩阵y type:选择相关性系数类型
	 * @return: java.lang.Double
	 **/
	public static Double calcSimilarity(Matrix x, Matrix y, String type) {
		if (type.equals("consine"))
			return x.cosineSimilarityTo(y, true);
		Long columnCount = x.getColumnCount();
		Double xMean = x.getMeanValue();
		Double yMean = y.getMeanValue();
		Double sim = 0.0;
		Double xDen = 0.0;
		Double yDen = 0.0;
		for (int j = 0; j < columnCount; j++) {
			sim += (x.getAsDouble(0, j) - xMean) * (y.getAsDouble(0, j) - yMean);
			xDen += Math.pow(x.getAsDouble(0, j) - xMean, 2);
			yDen += Math.pow((y.getAsDouble(0, j) - yMean), 2);

		}
		sim = sim / (Math.sqrt(xDen) * Math.sqrt(yDen));
		return sim;
	}

	/**
	 * @Description: 获取用户相似度矩阵
	 * @param: [ratingsMatrix, type] ratingsMatrix:评分矩阵 type:选择相似性矩阵的类型
	 * @return: org.ujmp.core.Matrix
	 **/
	public static Matrix calcUserSimilarityMatrix(Matrix ratingsMatrix, String type) {
		Long rowCounts = ratingsMatrix.getRowCount();
		Matrix userSimilarityMatrix = DenseMatrix.Factory.zeros(rowCounts, rowCounts);
		for (int i = 0; i < rowCounts; i++) {
			userSimilarityMatrix.setAsDouble(1.0, i, i);
			for (int j = i + 1; j < rowCounts; j++) {
				Double similarity = calcSimilarity(ratingsMatrix.selectRows(Calculation.Ret.NEW, i), ratingsMatrix.selectRows(Calculation.Ret.NEW, j), type);
				if (similarity.isNaN())
					similarity = 0.0;
				userSimilarityMatrix.setAsDouble(similarity, i, j);
				userSimilarityMatrix.setAsDouble(similarity, j, i);
			}
		}
		return userSimilarityMatrix;
	}

	/**
	 * @Description: 获取商品相似度矩阵
	 * @param: [ratingsMatrix, type]
	 * @return: org.ujmp.core.Matrix
	 **/
	public static Matrix calcItemsSimilarityMatrix(Matrix ratingsMatrix, String type) {
		return calcUserSimilarityMatrix(ratingsMatrix.transpose(), type);
	}

	/**
	 * @Description: 均方误差计算方法, 如果需要修改可以
	 * @param: [ratingsMatrix, predictionsMatrix]
	 * @return: java.lang.Double
	 **/
	public static Double getMSE(Matrix ratingsMatrix, Matrix predictionsMatrix) {
		Double cost = 0.;
		Long counts = 0L;
		Long userCounts = ratingsMatrix.getRowCount();
		Long itemCounts = ratingsMatrix.getColumnCount();
		for (int i = 0; i < userCounts; i++) {
			for (int j = 0; j < itemCounts; j++) {
				if (ratingsMatrix.getAsDouble(i, j) > 0) {
					counts += 1;
					cost += Math.pow(round(ratingsMatrix.getAsDouble(i, j), 2) - round(predictionsMatrix.getAsDouble(i, j), 2), 2);
				}

			}
		}
		return cost / counts;
	}

	/**
	 * @Description: 取矩阵的集
	 * @param: [matrix, row, col]
	 * @return: org.ujmp.core.Matrix
	 **/
	public static Matrix getSubMatrix(Matrix matrix, Long row, Long col) {
		Matrix subMatrix = Matrix.Factory.zeros(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++)
				subMatrix.setAsDouble(matrix.getAsDouble(i, j), i, j);
		}
		return subMatrix;
	}

	/**
	 * @Description: 过滤非法评分的方法，如果有其他要求修改本方法
	 * @param: [matrix]
	 * @return: org.ujmp.core.Matrix
	 **/
	public static Matrix modifyIllegalValue(Matrix matrix) {
		Matrix ratingsMatrix = matrix;
		Long row = matrix.getRowCount();
		Long col = matrix.getColumnCount();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (ratingsMatrix.getAsDouble(i, j) < 0)
					ratingsMatrix.setAsDouble(0, i, j);
			}
		}
		return ratingsMatrix;
	}

	public static Double getAllMeanValue(Matrix ratingsMatrix) {
		Double sum = 0.;
		Integer counts = 0;
		Long rows = ratingsMatrix.getRowCount();
		Long columns = ratingsMatrix.getColumnCount();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (ratingsMatrix.getAsDouble(i, j) > 0) {
					sum += ratingsMatrix.getAsDouble(i, j);
					counts += 1;
				}
			}
		}
		return round(sum / counts, 3);
	}

	public static HashMap<Integer, Double> getColumnDiffMap(Matrix ratingsMatrix, Integer lambda1) {
		HashMap<Integer, Double> columnDiffMap = new HashMap<Integer, Double>();
		Long rows = ratingsMatrix.getRowCount();
		Long columns = ratingsMatrix.getColumnCount();
		Double allMeanValue = getAllMeanValue(ratingsMatrix);
		for (int j = 0; j < columns; j++) {
			Integer counts = lambda1;
			Double sum = 0.;
			for (int i = 0; i < rows; i++) {
				if (ratingsMatrix.getAsDouble(i, j) > 0) {
					counts += 1;
					sum += ratingsMatrix.getAsDouble(i, j) - allMeanValue;
				}
			}
			columnDiffMap.put(j, round(sum / counts, 3));
		}
		return columnDiffMap;
	}

	public static HashMap<Integer, Double> getRowDiffMap(Matrix ratingsMatrix, Integer lambda2, HashMap<Integer, Double> columnDiffMap) {
		HashMap<Integer, Double> rowDiffMap = new HashMap<Integer, Double>();
		Long rows = ratingsMatrix.getRowCount();
		Long columns = ratingsMatrix.getColumnCount();
		Double allMeanValue = getAllMeanValue(ratingsMatrix);
		for (int i = 0; i < rows; i++) {
			Integer counts = lambda2;
			Double sum = 0.;
			for (int j = 0; j < columns; j++) {
				if (ratingsMatrix.getAsDouble(i, j) > 0) {
					counts += 1;
					sum += ratingsMatrix.getAsDouble(i, j) - allMeanValue - columnDiffMap.get(j);
					counts += 1;
				}
			}
			rowDiffMap.put(i, round(sum / counts, 3));
		}
		return rowDiffMap;
	}

	public static Matrix roundMatrix(Matrix matrix, Integer decimal) {
		Matrix returnMatrix = Matrix.Factory.zeros(matrix.getSize());
		for (int i = 0; i < returnMatrix.getRowCount(); i++) {
			for (int j = 0; j < returnMatrix.getColumnCount(); j++) {
				returnMatrix.setAsDouble(round(matrix.getAsDouble(i, j), decimal), i, j);
			}
		}
		return returnMatrix;
	}
}

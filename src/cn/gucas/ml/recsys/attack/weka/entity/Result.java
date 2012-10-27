package cn.gucas.ml.recsys.attack.weka.entity;

import cn.gucas.ml.recsys.attack.Constants;

public class Result {
	private double precision;
	private double recall;
	private double FMeasure;
	private double ROC;

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getFMeasure() {
		return FMeasure;
	}

	public void setFMeasure(double measure) {
		FMeasure = measure;
	}

	public double getROC() {
		return ROC;
	}

	public void setROC(double roc) {
		ROC = roc;
	}

	public String toString() {
		return precision + Constants.TABLE + recall + Constants.TABLE
				+ FMeasure + Constants.TABLE + ROC;
	}
}

package cn.gucas.ml.recsys.attack.utils;

import java.util.Random;

public class NormGenerator {
	private double miu;
	private double sigma;

	public NormGenerator(double miu, double sigma) {
		this.miu = miu;
		this.sigma = sigma;
	}

	public double getMiu() {
		return miu;
	}

	public void setMiu(double miu) {
		this.miu = miu;
	}

	public double getSigma() {
		return sigma;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public int nextGaussian() {
		Random r = new Random();
		double result = miu + sigma * r.nextGaussian();
		if (result > 5) {
			result = 5;
		} else if (result < 1) {
			result = 1;
		}
		return (int) Math.round(result);
	}

	public static void main(String[] args) {
		NormGenerator norm = new NormGenerator(3.6, 1.1);
		while (true) {
			System.out.println(norm.nextGaussian());
		}
	}
}

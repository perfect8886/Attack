package cn.gucas.ml.recsys.attack.utils;

public class Heap {
	double[] A;
	int heapSize = 0;

	public Heap(double[] A) {
		this.A = A;
	}

	public final int parent(int i) {
		return (i + 1) >> 1 - 1;
	}

	public final int left(int i) {
		return i << 1 + 1;
	}

	public final int right(int i) {
		return i << 1 + 2;
	}

	public final void exchange(int i, int j) {
		double temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	public void maxHeapify(int i) {
		int l = left(i);
		int r = right(i);
		int largest = 0;
		if (l <= heapSize && A[l] > A[i]) {
			largest = l;
		} else {
			largest = i;
		}
		if (r <= heapSize && A[r] > A[largest]) {
			largest = r;
		}
		if (largest != i) {
			exchange(largest, i);
			maxHeapify(largest);
		}
	}

	public void buildMaxHeap() {
		heapSize = A.length;
		for (int i = A.length / 2 - 1; i >= 0; --i) {
			maxHeapify(i);
		}
	}

	public double getDegSim(int k) {
		buildMaxHeap();
		double DegSim = 0.0;
		for (int i = 0; i < k; ++i) {
			int tail = heapSize - 1;
			exchange(0, tail);
			--heapSize;
			maxHeapify(0);
			DegSim += A[tail];
		}
		return DegSim / k;
	}
}

package cn.gucas.ml.recsys.attack.utils;

public class ItemHeap {
	Item[] A;
	int heapSize = 0;

	public ItemHeap(Item[] A) {
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
		Item temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	public void maxHeapify(int i) {
		int l = left(i);
		int r = right(i);
		int largest = 0;
		if (l <= heapSize && A[l].getRate() > A[i].getRate()) {
			largest = l;
		} else {
			largest = i;
		}
		if (r <= heapSize && A[r].getRate() > A[largest].getRate()) {
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

	public int[] loadBestSeller(int k) {
		int[] bestSellers = new int[k];
		buildMaxHeap();
		for (int i = 0; i < k; ++i) {
			int tail = heapSize - 1;
			exchange(0, tail);
			--heapSize;
			maxHeapify(0);
			bestSellers[i] = A[tail].getId();
		}
		return bestSellers;
	}
}

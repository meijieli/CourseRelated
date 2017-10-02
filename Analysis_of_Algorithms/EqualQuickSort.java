package homework2;

import java.util.Arrays;
import java.util.Random;

public class EqualQuickSort {
	
	public static void main(String[] args) {
		int[] a = new int[]{3, 4, 1, 9, 2, 3, 3, 4, 1, 0};
		quickSort(a, 0, a.length - 1);
		System.out.println(Arrays.toString(a));
	}
	
	public static void quickSort(int[] a, int start, int end){
		if (start >= end)
			return;
		int[] boundary = new int[2];
		boundary = partition(a, start, end);
		quickSort(a, start, boundary[0]);
		quickSort(a, boundary[1] + 1, end);
		
	}
	
	public static int[] partition(int[] a, int start, int end){
		Random random = new Random();
		int pivotIndex = random.nextInt(end - start + 1);		// randomly choose pivot
		int pivot = a[start + pivotIndex];
		swap(a, start + pivotIndex, end);						// put pivot at the last position
		int l = start, h = end, e = end - 1;					
		// l is the right boundary of elements smaller than pivot
		// e is the left boundary of elements equal to pivot
		// h is the left boundary of elements larger than pivot
		while (e >= l){
			if (a[e] < pivot)
				swap(a, l++, e);
			else if (a[e] > pivot)
				swap(a, e--, h--);
			else
				e--;
		}
		return new int[]{e, h};
	}
	
	public static void swap(int[] a, int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}

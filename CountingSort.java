package class7;

import java.util.Arrays;

public class CountingSort {
	
	public static void main(String[] args) {
		int[] a = new int[]{1, 3, 2, 3, 2, 3, 1, 2, 3, 5, 3, 2};
		int k = 5;
		System.out.println(Arrays.toString(countingSort(a, k)));
	}
	
	/** The element in array will be within [0, k] */
	public static int[] countingSort(int[] array, int k){
		int[] count = new int[k + 1];
		for (int i = 0; i < array.length; i++)
			count[array[i]]++;
		int[] result = new int[array.length];
		int index = 0;
		for (int i = 0; i < k + 1; i++)
			for (int j = 0; j < count[i]; j++)
				result[index++] = i;
		return result;
	}
}

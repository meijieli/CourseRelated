package class8;

import java.util.Arrays;

public class RadixSort {
	
	public static void main(String[] args) {
		int[] a = new int[]{132, 342, 323, 423, 634, 472, 856, 537, 934};
		System.out.println(Arrays.toString(radixSort(a)));
	}
	
	public static int[] radixSort(int[] array){
		// Counting sort on each digit
		// From the least significant digit to most significant digit
		for (int i = 0; i < 3; i++)
			array = countingSort(array, i);		
		return array;
	}
	
	public static int[] countingSort(int[] array, int digit){
		int[] count = new int[10];	// 0-9
		for (int i = 0; i < array.length; i++){
			int number = array[i];
			for (int j = 0; j < digit; j++)		// get different digit in different loops
				number = number / 10;
			count[number % 10]++;
		}
		for (int i = 1; i < 10; i++)	// accumulate counting
			count[i] = count[i] + count[i - 1];
		int[] result = new int[array.length];
		for (int i = array.length - 1; i >= 0; i--){
			int number = array[i];
			for (int j = 0; j < digit; j++)
				number = number / 10;
			result[count[number % 10] - 1] = array[i]; // be careful, index begins from 0
			count[number % 10]--;
		}
		return result;
	}
}

package class2;

import java.util.Arrays;

/** Merge Sort --- O(nlgn) */
// An example of the divide and conquer method
public class MergeSort {

	public void sort(int[] array){
		if (array.length < 2) // base case
			return;
		int mid = array.length;
		int[] left = Arrays.copyOfRange(array, 0, mid); // left half, mid exclusive
		int[] right = Arrays.copyOfRange(array, mid, array.length - 1); // right half, mid inclusive
		sort(left); // recurse on left half
		sort(right);
		merge(left, right, array);
	}
	
	/** A utility method that merges two half arrays back to the original array in increasing order */
	public void merge(int[] left, int[] right, int[] array){
		int i = 0, j = 0;
		while(i + j < array.length){
			if (j == right.length || ((left[i] < right[j]) && (i < left.length)))
				array[i + j] = left[i++];
			else
				array[i + j] = left[j++];
		}
	}
}

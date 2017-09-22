package class6;

import java.util.Random;

/** Find the kth smallest element of a unsorted array */
public class FindKth {
	
	public static void main(String[] args) {
		int[] array = new int[]{1, 3, 12, -2, 34, 2, -5};
		System.out.println(findKth(array, 0, array.length - 1, 4));
	}
	/*
	 * 1st thought:
	 * 1) Sort the input array
	 * 2) Return the kth element of the sorted array
	 * Time Complexity: O(nlogn)
	 */
	
	/*
	 * 2nd thought:
	 * Use divide and conquer pattern
	 * 1) choose(randomly) a pivot from the unsorted array
	 * 2) divide the array into two parts, <= pivot and > pivot
	 * 3) if left.length > i - 1, recurse on left part
	 * 	  else if left.length == i - 1, return pivot
	 * 	  else recurse on right part
	 * Time Complexity: O(n), worst case: O(n ^ 2)
	 */
	public static int findKth(int[] array, int start, int end, int k){
		if (start >= end)
			return array[start];
		Random random = new Random();
		int q = random.nextInt(end - start + 1);
		int pivot = array[start + q];				// don't write to the case when start = 0, end = length - 1
		swap(array, start + q, end);
		int left = start, right = end - 1;
		while(left <= right){								// same operations as quick sort
			while(left <= right && array[left] <= pivot)
				left++;
			while(left <= right && array[right] > pivot)
				right--;
			if (left <= right){
				swap(array, left, right);
				left++;
				right--;
			}
		}
		swap(array, left, end);
		if ((left - start) == k - 1)				// be careful when start is not 0!!
			return array[left];
		else if ((left - start) > k - 1)
			return findKth(array, start, left - 1, k);
		else
			return findKth(array, left + 1, end, k - (left - start) - 1);	// left-start! relative relationship
	}
	
	public static void swap(int[] array, int i, int j){
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}

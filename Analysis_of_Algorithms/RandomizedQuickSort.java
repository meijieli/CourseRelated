package class5;

import java.util.Arrays;
import java.util.Random;

public class RandomizedQuickSort {
	
	public static void main(String[] args) {
		int[] a = new int[]{23, 4, 12, 33, -4, 0};
		quickSort(a, 0, a.length - 1);
		System.out.println(Arrays.toString(a));
	}
	
	public static void quickSort(int[] nums, int a, int b){
		if (a >= b)
			return;									// has been trivially sorted
		int left = a, right = b - 1;				// two pointers
		Random random = new Random();
		int index = a + random.nextInt(b - a);		// the index of the pivot
		swap(nums, index, b);						// set the last element to be pivot
		
		int pivot = nums[b];
		while(left <= right){
			while (left <= right && (nums[left] < pivot))	// left <= right must firstly be judged
				left++;
			while (left <= right && (nums[right] > pivot))
				right--;
			if (left <= right){		// don't write as nums[left] <= nums[right]!!
				swap(nums, left, right);
				left++; right--;
			}
		}
		swap(nums, left, b);					// the pivot is stored at left
												// since we swap left and b, we need to 
												// let nums[left] > pivot
												// that's why we use <=, not <
		quickSort(nums, a, left - 1);			
		quickSort(nums, left + 1, b);
	}
	
	private static void swap(int nums[], int i, int j){
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}

package homework2;

import java.util.Arrays;
import java.util.Random;

public class KthClosestElements {
	
	public static void main(String[] args) {
		int[] a = new int[]{7, 2, 5, 15, 6, 12, 10, 18, 0, -4};
		int k = 4;
		findKClosest(a, k);
	}
	
	public static void findKClosest(int[] array, int k){
		int[] absArray = new int[array.length];		// length = array.length - 1
		for (int i = 0; i < array.length; i++)
			absArray[i] = Math.abs(array[i] - array[0]);	// get the absolute difference value
		System.out.println(Arrays.toString(absArray));
		int kthIndex = findKth(array, absArray, 1, absArray.length - 1, k);
		System.out.println(kthIndex);
		int[] resultIndex = new int[k];
		int[] result = new int[k];
		int j = 0;
		for (int i = 1; i < absArray.length; i++){
			if (absArray[i] < absArray[kthIndex])
				resultIndex[j++] = i;
		}
		for (int i = 0; i < j; i++)
			result[i] = array[resultIndex[i]];
		while(j < k){
			result[j++] = array[kthIndex];
		}
		System.out.println(Arrays.toString(result));
	}
	
	/** Use quickselect to get the kth smallest element in the array */
	// Be careful with absolute and relative index numbers
	public static int findKth(int[] array, int[] absArray, int start, int end, int k){
		if (start >= end)
			return start;			// don't forget base case
		Random random = new Random();
		int pivotIndex = random.nextInt(end - start + 1);
		int pivot = absArray[start + pivotIndex];
		
		// In-place partition
		swap(array, end, start + pivotIndex);		// put pivot at last position
		swap(absArray, end, start + pivotIndex);
		int left = start, right = end - 1;	// not include pivot
		while(left <= right){
			if (left <= right && absArray[left] <= pivot)
				left++;
			if (left <= right && absArray[right] > pivot)
				right--;
			if (left <= right){
				swap(array, left, right);
				swap(absArray, left, right);				
				left++;
				right--;
			}
		}
		swap(array, left, end); // put pivot into correct position
		swap(absArray, left, end); // put pivot into correct position
		if ((left - start) == k - 1)
			return left;
		else if ((left - start) > k - 1)
			return findKth(array, absArray, start, left - 1, k);
		else
			return findKth(array, absArray, left + 1, end, k - (left - start) - 1);
	}
	
	/** Utility method that swaps two values in the array */
	public static void swap(int[] nums, int i, int j){
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}

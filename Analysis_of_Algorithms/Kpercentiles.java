package homework2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Kpercentiles {
	public static void main(String[] args) {
		int[] a = new int[]{1,2,3,4,5,6,7,8,9,10};
		int k = 4;
		List<Integer> result = new ArrayList<>();
		getPercentiles(a, result, 0, a.length - 1, k);
		Collections.sort(result);
		System.out.println(result);
	}
	
	public static void getPercentiles(int[] array, List<Integer> result, 
			int start, int end, int k){
		if (k == 1)						// not looking for a percentile
			return;
		int n = end - start + 1;		// the length of the current array
		int mid = k / 2;				// the percentile in the middle
		int index = (int)Math.ceil(mid * n / (double)k);	// be careful with int/double
		
		int position = quickSelect(array, start, end, index);	// find the percentile
		int percentile = array[position];						// add result
		result.add(percentile);
		int left = Partition(array, percentile, position, start, end);	// partition
		
		getPercentiles(array, result, start, left, (int)Math.floor(k / 2.0));	// recursive calls
		getPercentiles(array, result, left + 1, end, (int)Math.ceil(k / 2.0));	// recursive calls
	}
	
	public static int Partition(int[] array, int pivot, int position, int start, int end){
		swap(array, position, end);
		int left = start, right = end - 1;
		while(left <= right){
			while (left <= right && array[left] <= pivot)
				left++;
			while (left <= right && array[left] > pivot)
				right--;
			if (left <= right){
				swap(array, left, right);
				left++;
				right--;
			}
		}
		swap(array, left, end);
		return left;
	}
	
	public static int quickSelect(int[] array, int start, int end, int toFind){
		if (start >= end)
			return start;
		Random random = new Random();
		int q = random.nextInt(end - start + 1);
		int pivot = array[start + q];
		int left = Partition(array, pivot, start + q, start, end);
		if ((left - start) == toFind - 1)
			return left;
		else if ((left - start) > toFind - 1)
			return quickSelect(array, start, left - 1, toFind);
		else
			return quickSelect(array, left + 1, end, toFind - (left - start) - 1);
	}
	
	public static void swap(int[] nums, int i, int j){
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
}

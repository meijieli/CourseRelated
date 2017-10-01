package class3;

/** Find the sub array with maximum sum --- O(nlgn) */
// An example of divide and conquer method
public class MaximumSubarray {
	
	// test
	public static void main(String[] args) {
		int[] a = new int[]{3, 1, -5, 7, 8, -6, 2, 4};
		MaximumSubarray ms = new MaximumSubarray();
		int[] result = ms.findCrossMaximum(a, 0, a.length - 1);
		for (int i = 0; i < result.length; i++)
			System.out.print(result[i] + "\t");
	}
	/*
	 * The method returns an array contains the start index, 
	 * end index, and the maximum sum
	 */
	public int[] findMaximum(int[] array, int start, int end){
		if (start == end)
			return new int[]{start, end ,array[start]}; 
		else{
			int mid = (start + end) / 2;
			int[] left = new int[3];
			left = findMaximum(array, start, mid);
			int[] right = new int[3];
			right = findMaximum(array, mid + 1, end);
			int[] cross = new int[3];
			cross = findCrossMaximum(array, start, end);
			if (left[2] >= right[2] && left[2] >= cross[2])
				return left;
			else if (right[2] >= left[2] && right[2] >= cross[2])
				return right;
			else
				return cross;
		}
		
	}
	
	/* Utility method that finds the maximum subarray with elements
	 * crossing the mid element --- O(n)
	 */
	public int[] findCrossMaximum(int[] array, int start, int end){
		int mid = (start + end) / 2;
		int leftSum = Integer.MIN_VALUE, leftIndex = mid, sum = 0;
		for (int j = mid; j >= start; j--){
			sum += array[j];
			if (sum > leftSum){
				leftSum = sum;
				leftIndex = j;
			}
		}
		sum = 0;
		int rightSum = Integer.MIN_VALUE, rightIndex = mid;
		for (int j = mid + 1; j <= end; j++){
			sum += array[j];
			if (sum > rightSum){
				rightSum = sum;
				rightIndex = j;
			}
		}
		return new int[]{leftIndex, rightIndex, leftSum + rightSum};
	}
}

package class4;

import java.util.LinkedList;
import java.util.Queue;

public class InPlaceQuickSort {
	
	/*
	 * Quick sort chooses a pivot from the array, and compare
	 * each element in array to the pivot, thus divide the original array
	 * into three parts, the first part contains elements that are smaller 
	 * than pivot, the second part contains elements that equal to pivot,
	 * the last part contains elements that are larger than pivot.
	 *
	 * Although quick sort can only realize an expected O(nlgn) complexity,
	 * it can have great performance in most cases.
	 * Another advantage of quick sort is that its in-place algorithm is 
	 * easy to implement.
	 */
	
	/** Not in-place algorithm */
	public void quickSort(Queue<Integer> data){ // Use queue for example
		Queue<Integer> left = new LinkedList<>();
		Queue<Integer> right = new LinkedList<>();
		Queue<Integer> equal = new LinkedList<>();
		int pivot = data.peek();
		for (int i = 0; i < data.size(); i++){
			if (pivot > data.peek())
				left.add(data.poll());
			else if (pivot < data.peek())
				right.add(data.poll());
			else
				equal.add(data.poll());
		}
		quickSort(left);
		quickSort(right);
		while(left.size() != 0)
			data.add(left.poll());
		while(equal.size() != 0)
			data.add(equal.poll());
		while(right.size() != 0)
			data.add(right.poll());
	}
	
	/** In-place algorithm */
	public void quickSort(int[] array, int left, int right){
		if (left >= right)
			return;
		int pivot = array[array.length - 1]; // choose pivot as the last element
		while(left <= right){
			while (left <= right && array[left] < pivot)
				left++;
			while(left <= right && array[right] > pivot)
				right--;
			if (array[left] >= array[right]){ // if left == right == pivot, still going, 
											  // till left reaches a value larger than pivot (exchange positions with pivot)
				array[left] = array[left] ^ array[right];
				array[right] = array[left] ^ array[right];
				array[left] = array[left] ^ array[right];
				left++; right--;
			}	
		}
		array[left] = array[right + 1] ^ array[left];
		array[right + 1] = array[right + 1] ^ array[left];
		array[left] = array[right + 1] ^ array[left];
		quickSort(array, 0, left - 1);
		quickSort(array, left + 1, right + 1);
	}
}

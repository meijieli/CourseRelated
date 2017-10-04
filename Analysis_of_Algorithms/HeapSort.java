package class9;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class HeapSort {
	
	public static void main(String[] args) {
		int[] a = new int[]{3, 5, 1, 2, 5, 6, 7, 8, 9, 2, 3, 4};
		heapSortInc(a);
		System.out.println(Arrays.toString(a));
		heapSortDec(a);
		System.out.println(Arrays.toString(a));
	}
	
	/** Increasing order */
	public static void heapSortInc(int[] nums){
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		for (int i = 0; i < nums.length; i++)	// build heap
			pq.add(nums[i]);
		for (int i = 0; i < nums.length; i++)	// extract min from heap
			nums[i] = pq.poll();
	}
	
	public static void heapSortDec(int[] nums){
		PriorityQueue<Integer> pq = new PriorityQueue<>(nums.length, new Comparator<Integer>(){
			
			@Override
			public int compare(Integer a1, Integer a2){
				if (a1.intValue() > a2.intValue())
					return - 1;
				else if (a1.intValue() == a2.intValue())
					return 0;
				else
					return 1;
			}
		});
		for (int i = 0; i < nums.length; i++)
			pq.add(nums[i]);
		for (int i = 0; i < nums.length; i++)	// extract max from heap
			nums[i] = pq.poll();
	}
}

package class12;

import java.util.Arrays;

/** Calculate Fibonacci sequence */
public class Fibonacci {
	
	public static void main(String[] args) {
		for (int i = 1; i <= 5; i++)
			System.out.print(getSequence1(i) + " ");
		System.out.println();
		getSequence2(5);
	}
	
	/*
	 * Use top-down method, recursive call
	 * exponential complexity
	 */
	public static int getSequence1(int n){
		if (n == 1)
			return 1;
		if (n == 2)
			return 1;
		return getSequence1(n - 1) + getSequence1(n - 2);
	}
	
	/*
	 * Use bottom-up method, iterative call
	 * dynamic programming
	 * linear complexity
	 */
	public static void getSequence2(int n){
		int[] sequence = new int[n];
		sequence[0] = 1;
		sequence[1] = 1;
		for (int i = 2; i < n; i++)
			sequence[i] = sequence[i - 1] + sequence[i - 2];
		System.out.println(Arrays.toString(sequence));
	}
}

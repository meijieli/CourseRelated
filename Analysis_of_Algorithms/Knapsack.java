package dynamicProgramming;

/* n items, with given integer weights wi, values vi, i = 1,...,n
 * Knapsack has weight capacity W. Choose a subset of items that fits in 
 * the knapsack and has maximum value.  */
public class Knapsack {
	
	public static void main(String[] args) {
		int[] weights = new int[]{4, 6, 2, 5, 7};
		int[] values = new int[]{25, 30, 10, 27, 35};
		int capacity = 13;
		System.out.println(KNAP(weights, values, capacity, weights.length));
		System.out.println(dpKNAP(weights, values, capacity));
	}
	
	// Top-down, Recursive method, O(2 ^ n)
	public static int KNAP(int[] weights, int[] values, int capacity, int i){
		if (i == 0 || capacity == 0)	// have used all items or there is no capacity
			return 0;
		if (weights[i - 1] > capacity)	// can't take this item
			return KNAP(weights, values, capacity, i - 1);
		// take n-th item or not, choose the better one
		return Math.max(KNAP(weights, values, capacity - weights[i - 1], i - 1) + values[i - 1], 
				KNAP(weights, values, capacity, i - 1));
	}
	
	// Dynamic programming, bottom up, O(nW)
	public static int dpKNAP(int[] weights, int[] values, int capacity){
		// make a dp table
		int[][] dp = new int[weights.length + 1][capacity + 1];		// add dummy row and column
		// mark the route to the predecessor
		int[][] s = new int[weights.length + 1][capacity + 1];
		for (int i = 1; i <= weights.length; i++){			// for each item
			for (int j = 1; j <= capacity; j++){			// construct its dp array  				
				if (weights[i - 1] <= j && dp[i - 1][j - weights[i - 1]] + values[i - 1] > dp[i - 1][j]){
					dp[i][j] = dp[i - 1][j - weights[i - 1]] + values[i - 1];
					s[i][j] = 1;	// this item is chosen
				}
				else
					dp[i][j] = dp[i - 1][j];
			}
		}
		reconstruct(s, weights, capacity);
		return dp[weights.length][capacity];
	}
	
	// find out the items that are selected
	public static void reconstruct(int[][] s, int[] weights, int capacity){
		int n = weights.length;
		int b = capacity;	// must start from the bottom-right element of s
		// from the end point, back trace to the start point
		for (int i = n; i >= 1; i--){
			if (s[i][b] == 1){
				System.out.println("Pick: " + i);
				b -= weights[i - 1];
			}
		}
	}
}

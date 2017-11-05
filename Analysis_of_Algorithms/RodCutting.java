package DynamicProgramming;

/* Given a rod of length n, prices pi, i = 1,...,n for rods of length i,
 * Find the optimal way to cut the rod of length n into smaller rods that
 * maximizes the total revenue*/
public class CutRod {
	
	public static void main(String[] args) {
		int[] prices = new int[]{2, 5, 7, 9, 11};
		System.out.println(bestCut(prices, 5));
		System.out.println(dpBestCut(prices));
	}
	
	// Top-down, recursive method, O(2 ^ n)
	public static int bestCut(int[] price, int n){
		if (n == 0)
			return 0;
		if (n == 1)
			return price[0];
		int profit = Integer.MIN_VALUE;
		// The first cut can cut the rod to length 1,...,n
		for (int i = 1; i <= n; i++){
			profit = Math.max(profit, price[i - 1] + bestCut(price, n - i));
		}
		return profit;
	}
	
	
	// Bottom-up, dynamic programming, iterative method, O(n ^ 2)
	public static int dpBestCut(int[] price){
		int n = price.length;
		// an array that stores the maximum profit for each length
		int[] opt = new int[n + 1];
		// an array to keep record of the best move at each length
		int[] s = new int[n];
		
		// Increase the length of rod we consider from 1 to n
		for (int i = 1; i <= n; i++){
			int profit = Integer.MIN_VALUE;	// the initial profit 
			for (int j = 1; j <= i; j++){	// find the maximum profit from all combinations
				if (profit < price[j - 1] + opt[i - j]){
					profit = price[j - 1] + opt[i - j];
					s[i - 1] = j;
				}
			}
			opt[i] = profit; 
		}
		reconstruct(s);
		return opt[n];
	}
	
	// a method that reconstructs the ways of optimal cutting
	public static void reconstruct(int[] s){
		int curLen = s.length;
		while(curLen > 0){
			System.out.println("Cut rod: " + s[curLen - 1]);
			curLen -= s[curLen - 1];	// find the optimal cut at current length
		}
	}
}

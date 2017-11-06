package dynamicProgramming;

/** Longest Common Subsequence problem */
public class LCS {
	
	public static void main(String[] args) {
		String x = "ABCBDAB";
		String y = "BDCABA";
		System.out.println(getLCS(x, y));
	}
	
	// Brute force way: find all subsequences of x and 
	// compare them with y to decide whether there is a match O(m * 2^n)
	
	// Dynamic programming, create a two dimensional table
	public static int getLCS(String x, String y){
		int n = x.length();
		int m = y.length();
		int[][] dp = new int[n + 1][m + 1];	// the dp matrix, i = 0 and j = 0 are initialized to 0
		for (int i = 0; i < n; i++){
			char xcur = x.charAt(i);
			for (int j = 0; j < m; j++){
				char ycur = y.charAt(j);
				if (xcur == ycur)	// a match, diagonal increase
					dp[i + 1][j + 1] = dp[i][j] + 1;
				else	// mismatch, choose the larger one from previous two results
					dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
			}
		}
		reconstruct(dp, x, y);
		return dp[n][m];
	}
	
	// Find the route back to the original point
	public static void reconstruct(int[][] dp, String x, String y){
		int n = x.length();
		int m = y.length();
		StringBuilder sb = new StringBuilder();
		int i = n, j = m;
		while (i > 0 && j > 0){
			if (x.charAt(i - 1) == y.charAt(j - 1)){	// compare the two characters at current indexes
				sb.append(x.charAt(i - 1));
				i--;
				j--;
			}
			else if (dp[i - 1][j] >= dp[i][j - 1])	// gets to current position from above
				i--;
			else
				j--;
		}
		System.out.println(sb.reverse().toString());
	}
}

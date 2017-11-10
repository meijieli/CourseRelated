package dynamicProgramming;

/** Find the longest palindrome sequence of a string */
public class LPS {

	public static void main(String[] args) {
		String test = "asdoabbsdas";
		System.out.println(getSequence1(test));
		System.out.println(getSequence2(test));
		System.out.println(getSequence3(test));
	}

	// Method1: Recurrence, Top-down, solve same problem multiple times
	public static int getSequence1(String str){
		return helper(str, 0, str.length() - 1);
	}
	
	public static int helper(String str, int start, int end){
		// two base cases
		if (start == end)
			return 1;
		else if ((end - start) == 1 && str.charAt(start) == str.charAt(end))
			return 2;
		// if two characters match, recurse on the middle part
		else if (str.charAt(start) == str.charAt(end)){
			return helper(str, start + 1, end - 1) + 2;	// plus 2
		}else
			return Math.max(helper(str, start + 1, end), helper(str, start, end - 1));
	}
	
	
	
	// Method2: Dynamic Programming, O(n ^ 2)
	// For each character in the string, 
	public static int getSequence2(String str) {
		int n = str.length();
		int[][] dp = new int[n][n];
		// all single characters are palindrome with length 1 --- initialize the diagonal
		for (int i = 0; i < n; i++)
			dp[i][i] = 1;
		// The upper-right part of the dynamic programming table is unused
		// because we will need the result of shorter sub string in calculation
		// we calculate the dynamic programming table from small length to large length
		for (int len = 2; len <= n; len++){	// len == 1 has been dealt with
			for (int i = 0; i < n - len + 1; i++){	// the beginning character of this length
				int j = i + len - 1;	// the ending character of this length
				if (str.charAt(i) == str.charAt(j))
					if (len == 2)
						dp[i][j] = 2;
					else
						dp[i][j] = dp[i + 1][j - 1] + 2;	// middle string plus 2, can't deal with len == 2
				else
					dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);	// better one
			}
		}
		return dp[0][n - 1];
	}

	
	
	// Method3: call LCS, O(n ^ 2)
	public static String getSequence3(String str) {
		// get the reversed original string
		String reversed = new StringBuilder(str).reverse().toString();
		// return the longest common sequence of str and reversed
		return LCS(str, reversed);
	}
	
	public static String LCS(String str, String reversed) {
		// Dynamic Programming
		int n = str.length();
		int[][] dp = new int[n + 1][n + 1]; // added dummy row and column
		// at each position, if str.charAt(i) == reversed.charAt(j), then
		// dp[i][j] = dp[i - 1][j - 1] + 1
		// otherwise, dp[i][j] = max{dp[i - 1][j], dp[i][j - 1]} (be careful
		// with indexes)
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (str.charAt(i - 1) == reversed.charAt(j - 1))
					dp[i][j] = dp[i - 1][j - 1] + 1;
				else
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
			}
		}
		return reconstruct(dp, str, reversed);
	}

	public static String reconstruct(int[][] dp, String str, String reversed) {
		StringBuilder sb = new StringBuilder();
		int n = str.length();
		int i = n, j = n;
		while (i > 0 && j > 0) {
			if (str.charAt(i - 1) == reversed.charAt(j - 1)) {
				sb.append(str.charAt(i - 1));
				i--; // diagonal
				j--;
			} else {
				if (dp[i - 1][j] >= dp[i][j - 1])
					i--;
				else
					j--;
			}
		}
		return sb.reverse().toString();
	}
}

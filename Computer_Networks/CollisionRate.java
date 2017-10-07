package trail;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CollisionRate {
	/**
	 * Calculate the collision probability under certain number of users and allowed number of users
	 * @param numOfUserAllowed
	 * 		The maximum number of user allowed at same time
	 * @param active
	 * 		The active probability of each user
	 * @param n
	 * 		Total number of users
	 * 
	 * @author Shenzhi Zhang
	 * @Date  10/06/2017
	 */
	
	public static void main(String[] args) {
		double active = 1;
		int numOfUserAllowed = 12;		// question d = 12
		for (int n = numOfUserAllowed; n < 30; n++){	// question d
		//for (int n = 40; n <= 40; n++){					// question f
			BigDecimal[] probability = new BigDecimal[numOfUserAllowed + 1];	// store previous value 
			probability[0] = Combination.Cnk(n, 0, active);						// first value
			for (int i = 1; i < probability.length; i++){						// calculate the sum
				probability[i] = Combination.Cnk(n, i, active).add(probability[i - 1]);
			}
			System.out.println("Number of user: " + n + "  No Collision probability: " + 
					probability[probability.length - 1].toString());			// print the sum when there are n users
		}
	}
}

/** Class for calculating combination value */
class Combination{
	
	public static BigDecimal Cnk(int n, int k, double p){
		double distribution = Math.pow(1 - p, n - k) * Math.pow(p, k);
		if (k == 0)
			return new BigDecimal(String.valueOf(distribution));
		BigInteger numerator = new BigInteger("1");
		BigInteger denominator = new BigInteger("1");
		for (int i = 2; i <= n; i++)		// n!
			numerator = numerator.multiply(new BigInteger(String.valueOf(i)));
		for (int i = 2; i <= n - k; i++)	// (n - k)!
			denominator = denominator.multiply(new BigInteger(String.valueOf(i)));
		for (int i = 2; i <= k; i++)		// k!
			denominator = denominator.multiply(new BigInteger(String.valueOf(i)));
		BigDecimal result = new BigDecimal(numerator);
		result = result.multiply(new BigDecimal(String.valueOf(distribution)));	// * p^i * (1-p)^(n-i)
		return result.divide(new BigDecimal(denominator));	// final term
	}
}

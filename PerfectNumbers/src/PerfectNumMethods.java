import java.math.BigInteger;

public class PerfectNumMethods {
	/**
	 * Uses properties of perfect numbers, mersenne primes, and experiments results to
	 * efficently determine whether an integer is a perfect number
	 * links:https://en.wikipedia.org/wiki/List_of_perfect_numbers
	 * https://en.wikipedia.org/wiki/Mersenne_prime#:~:text=In%20mathematics%2C%20a%20Mersenne%20prime,in%20the%20early%2017th%20century.
	 * https://en.wikipedia.org/wiki/Lucas%E2%80%93Lehmer_primality_test#Time_complexity
	 * runs in O((log(n)^2)*log log n * log log log (n)) for a good implementation
	 * @param n the integer you want to test
	 * @return whether the input is a perfect number or not
	 */
	public static boolean isPerfect(int n) {
		if(n%2==1) {
			return false;
		}
		int mersenne = n;
		int p = 0;
		int factor = 1;
		boolean divisible = true;
		while(divisible) {
			if(mersenne%2==0) {
				p+=1;
				mersenne/=2;
				factor*=2;
			}
			else {
				divisible = false;
			}
		}
		if(factor*2-1 != mersenne) {
			return false;
		}
		if(ll(mersenne,p) || mersenne == 3) {
			System.out.println("Perfect Number factored by 2^p-1 * Mersenne Prime (2^p-1): " + (factor) +"X" + mersenne);
			return true;
		}
		return false;
	}
	/**
	 * Uses the lucah-lehmer primality test to very efficently determine if a m
	 * is a mersenne prime. Runs in O(p^2 log p log log p)
	 * @param m the number of the form 2^p -1 you want to test for primality
	 * @param p the power of 2 of the number you are testing is based on 2^p -1
	 * @return whether it is a mersenne prime or not
	 */
	public static boolean ll(int m, int p) {
		int s = 4;
		p+=1;
		//System.out.println(Math.log(m+1)/Math.log(2));
		//System.out.println(p);
		for(int i = 0; i < p-2; i++) {
			s = ((s*s)-2)%m;
		}
		if(s==0) {
			return true;
		}
		return false;
	}
	/**
	 * Method that implements the above perfect number test with BigInteger class so that we can
	 * test much larger numbers. Limited to numbers with less than 1500 digits.
	 * @param n The BigInteger number you want to test, less then 10^1500
	 * @return whether it is a perfect number or not.
	 */
	public static boolean isPerfect(BigInteger n) {
		BigInteger two = new BigInteger("2");
		if(n.remainder(two).equals(BigInteger.ONE)) {
			/* It is known there is no odd perfect number below 10^1500
			* https://en.wikipedia.org/wiki/List_of_perfect_numbers
			* Given even Lucas-Lehmer has troubles with 1500 digit numbers we will just assume
			 your computer can't handle the computation :/.
			 */
			System.out.println("Either odd below 10^1500 or to large to compute in a reasonable amount of time");
			return false;
		}
		BigInteger mersenne = n;
		BigInteger p = BigInteger.ZERO;
		BigInteger factor = BigInteger.ONE;
		boolean divisible = true;
		while(divisible) {
			if(mersenne.remainder(two).equals(BigInteger.ZERO)) {
				mersenne = mersenne.divide(two);
				p = p.add(BigInteger.ONE);
				factor = factor.multiply(two);
			}
			else {
				divisible = false;
			}
		}
		if(!factor.multiply(two).subtract(BigInteger.ONE).equals(mersenne)){
			return false;
		}
		if(ll(mersenne,p.add(BigInteger.ONE))) {
			System.out.println("Perfect Number factored by 2^p-1 * Mersenne Prime (2^p-1):");
			System.out.println(factor);
			System.out.println("X");
			System.out.println(mersenne);
			return true;
		}
		return false;
		
	}
	/**
	 * Lucah-Lehmer algorithm implemented with Big Integer for larger compute values.
	 * Returns whether a number of the form 2^p - 1 is a prime number
	 * @param m The number of the form 2^p - 1 you want to primality test
	 * @param p The power of 2 of the number being tested is.
	 * @return
	 */
	public static boolean ll(BigInteger m, BigInteger p) {
		BigInteger s = new BigInteger("4");
		BigInteger tempP = new BigInteger(p.toString()).subtract(new BigInteger("2"));
		//System.out.println(m);
		//System.out.println(p);
		//System.out.println(tempP);
		while(!tempP.equals(BigInteger.ZERO)) {
			s = ((s.multiply(s)).subtract(new BigInteger("2"))).mod(m);
			tempP= tempP.subtract(BigInteger.ONE);
		}
		if(s.equals(BigInteger.ZERO)) {
			return true;
		}
		return false;
	}
	
}

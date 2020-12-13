import java.math.BigInteger;

public class PerfectNumDriver {
	/**
	 * The main driver method for the program.
	 * I used to be obsessed with mersenne primes and GIMP so I just had to do this problem haha.
	 * Literally used to run my crappy desktop for days on end dreaming of finding the next largest prime :)!
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(PerfectNumMethods.isPerfect(6));
		//System.out.println(PerfectNumMethods.isPerfect(7));
		//System.out.println(PerfectNumMethods.isPerfect(496));
		//System.out.println(PerfectNumMethods.isPerfect(33550336));
		//Inefficiently find perfect numbers 1-1000
		System.out.println("Perfect numbers 1-1000");
		for(int i = 1; i < 1001; i++) {
			PerfectNumMethods.isPerfect(i);
		}
		//The other perfect numbers before we hit the integer limit!:
		System.out.println("Perfect numbers < " + Integer.MAX_VALUE);
		PerfectNumMethods.isPerfect(8128);
		PerfectNumMethods.isPerfect(33550336);
		
		//BigInteger implementations:
		System.out.println("Big Integer:");
		PerfectNumMethods.isPerfect(new BigInteger("8128"));
		PerfectNumMethods.isPerfect(new BigInteger("33550336"));
		PerfectNumMethods.isPerfect(new BigInteger("8589869056"));
		PerfectNumMethods.isPerfect(new BigInteger("137438691328"));
		PerfectNumMethods.isPerfect(new BigInteger("2305843008139952128"));
		BigInteger factor = new BigInteger("1152921504606846976");
		BigInteger mersenne = new BigInteger("2305843009213693951");
		PerfectNumMethods.isPerfect(factor.multiply(mersenne));
	}
}

package utils;

import java.util.Random;

public class Utils {
    
    /**
     * Method to generate a random number between @link min and @link max
     * @param min Minimum number
     * @param max Maximum number
     * @return a integer random number between @link min and @link max
     */
    public static int generateRandomNumber(int min, int max){
	Random r = new Random();	
	return r.nextInt(max-min) + min;
    }

}

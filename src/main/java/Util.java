/**
 * Created by k22jain on 16/10/16.
 */
public class Util {

    public static String byteArrayToString(byte[] byteArray)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    public static byte[] stringToByteArray(String inputString,int arraySize)
    {
        int optimalStringLength = 2*arraySize;
        byte[] input = new byte[arraySize];
        if (inputString.length() < optimalStringLength) {
            int no_of_zeroes = optimalStringLength- inputString.length();
            String zeroes = "";
            for (int iter = 1; iter <= no_of_zeroes; iter++) {
                zeroes = zeroes + "0";
            }
            inputString= inputString + zeroes;
        }
        for (int i = 0; i < optimalStringLength; i += 2) {
            input[i / 2] = (byte) ((Character.digit(inputString.charAt(i), 16) << 4)
                    + Character.digit(inputString.charAt(i + 1), 16));
        }
        return input;
    }
}

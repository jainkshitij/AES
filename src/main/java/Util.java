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

    public static byte[] arrayConvertor (byte[][] twoDimensionArray)
    {

        byte[] oneDimesionArray =new byte[twoDimensionArray.length*twoDimensionArray[0].length];
        for(int i=0;i<twoDimensionArray.length;i++)
        {
            for(int j=0;j<twoDimensionArray[i].length;j++)
            {
                oneDimesionArray[i*twoDimensionArray.length+j]=twoDimensionArray[i][j];
            }
        }
        return oneDimesionArray;
    }

    public static byte[] columnMajorArrayConvertor (byte[][] twoDimensionArray)
    {

        byte[] oneDimesionArray =new byte[twoDimensionArray.length*twoDimensionArray[0].length];
        for(int i=0;i<twoDimensionArray.length;i++)
        {
            for(int j=0;j<twoDimensionArray[i].length;j++)
            {
                oneDimesionArray[i*twoDimensionArray.length+j]=twoDimensionArray[j][i];
            }
        }
        return oneDimesionArray;
    }

    public static byte byteMultiply(byte a, byte b) {
        byte p = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p = (byte) (p ^ a);
            }
            b = (byte) (b >> 1);
            byte carry = (byte) (a & 0x80);
            a = (byte) (a << 1);
            if (carry == (byte) (0x80)) {
                a = (byte) (a ^ (byte) 0x1b);
            }
        }
        return p;
    }
}

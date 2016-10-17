import static org.junit.Assert.*;

/**
 * Created by k22jain on 16/10/16.
 */
public class AesTest {
    private final static String key="603deb1015ca71be2b73aef0857d77811f352c073b6108d72d9810a30914dff4";
    @org.junit.Test
    public void encrypt() throws Exception {
        Aes aes=new Aes();
        String encryptedString=Util.byteArrayToString(aes.encrypt(Util.stringToByteArray(key,32),Util.stringToByteArray("6bc1bee22e409f96e93d7e117393172a",16)));
        assertEquals("String doesn't match","F3EED1BDB5D2A03C064B5A7E3DB181F8",encryptedString);

    }

    @org.junit.Test
    public void decrypt() throws Exception {
        Aes aes=new Aes();
        String decryptedString=Util.byteArrayToString(aes.decrypt(Util.stringToByteArray(key,32),Util.stringToByteArray("23304B7A39F9F3FF067D8D8F9E24ECC7",16)));
        assertEquals("String doesn't match","F69F2445DF4F9B17AD2B417BE66C3710",decryptedString);

    }

}
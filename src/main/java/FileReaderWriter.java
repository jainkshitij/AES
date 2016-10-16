import java.io.*;


public class FileReaderWriter {



    public static void main(String[] args) throws IOException {


        FileInputStream fstream = new FileInputStream("res/textfile.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        fstream = new FileInputStream("res/key.txt");
        in = new DataInputStream(fstream);
        BufferedReader keybr = new BufferedReader(new InputStreamReader(in));
        String strLine;

        strLine = keybr.readLine();
        byte[] key=Util.stringToByteArray(strLine,32);
        Aes aes=new Aes();

        File enc = new File("encryption.txt");
        File dec = new File("decryption.txt");
        FileWriter writer = new FileWriter(enc);
        FileWriter writer1 = new FileWriter(dec);

        while ((strLine = br.readLine()) != null) {
            byte[] input=Util.stringToByteArray(strLine,16);
            byte[] encryptedMessage=aes.encrypt(key,input);
            writer.append(Util.byteArrayToString(encryptedMessage));
            writer.append("\r\n");

            byte[] decryptedMessage=aes.decrypt(key,encryptedMessage);
            writer1.append(Util.byteArrayToString(decryptedMessage));
            writer1.append("\r\n");
        }

        in.close();

        writer.flush();
        writer.close();
        writer1.flush();
        writer1.close();

    }


}

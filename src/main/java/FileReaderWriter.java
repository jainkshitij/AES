import java.io.*;


public class FileReaderWriter {



    public static void main(String[] args) throws IOException {


        FileInputStream fstream = new FileInputStream("res/textfile.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        fstream = new FileInputStream("res/key.txt");
        in = new DataInputStream(fstream);
        BufferedReader keyBufferedReader = new BufferedReader(new InputStreamReader(in));
        String strLine;

        strLine = keyBufferedReader.readLine();
        byte[] key=Util.stringToByteArray(strLine,32);
        Aes aes=new Aes();

        File encryptionFile = new File("encryption.txt");
        File decryptionFile = new File("decryption.txt");
        FileWriter encryptionWriter = new FileWriter(encryptionFile);
        FileWriter decryptionWriter = new FileWriter(decryptionFile);

        while ((strLine = br.readLine()) != null) {
            byte[] input=Util.stringToByteArray(strLine,16);
            byte[] encryptedMessage=aes.encrypt(key,input);
            encryptionWriter.append(Util.byteArrayToString(encryptedMessage));
            encryptionWriter.append("\r\n");

            byte[] decryptedMessage=aes.decrypt(key,encryptedMessage);
            decryptionWriter.append(Util.byteArrayToString(decryptedMessage));
            decryptionWriter.append("\r\n");
        }

        in.close();

        encryptionWriter.flush();
        encryptionWriter.close();
        decryptionWriter.flush();
        decryptionWriter.close();

    }


}

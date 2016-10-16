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
        byte[][] state = new byte[4][4];
        byte[] input = new byte[16];
        byte[] inp = new byte[32];
        byte[] inter;
        strLine = keybr.readLine();
        inp=Util.stringToByteArray(strLine,32);
        Aes aes=new Aes();
        File enc = new File("encryption.txt");
        File dec = new File("decryption.txt");
        FileWriter writer = new FileWriter(enc);
        FileWriter writer1 = new FileWriter(dec);

        while ((strLine = br.readLine()) != null) {
            input=Util.stringToByteArray(strLine,16);
            inter=aes.encrypt(inp,input);
            writer.append(Util.byteArrayToString(inter));
            writer.append("\r\n");

            inter=aes.decrypt(inp,inter);
            writer1.append(Util.byteArrayToString(inter));
            writer1.append("\r\n");
        }

        in.close();

        writer.flush();
        writer.close();
        writer1.flush();
        writer1.close();

        //String encryptedString=Util.byteArrayToString(aes.encrypt(inp,Util.stringToByteArray("6bc1bee22e409f96e93d7e117393172a",16)));
        //System.out.println(encryptedString);

    }


}

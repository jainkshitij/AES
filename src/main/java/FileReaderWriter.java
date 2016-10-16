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

        for (int i = 0; i < 64; i += 2) {
            inp[i / 2] = (byte) ((Character.digit(strLine.charAt(i), 16) << 4)
                    + Character.digit(strLine.charAt(i + 1), 16));
        }
        Aes aes=new Aes();
        File enc = new File("encryption.txt");
        File dec = new File("decryption.txt");
        FileWriter writer = new FileWriter(enc);
        FileWriter writer1 = new FileWriter(dec);
        FileOutputStream stream = new FileOutputStream("encryption.txt");

        while ((strLine = br.readLine()) != null) {

            if (strLine.length() < 32) {
                int no_of_zeroes = 32 - strLine.length();
                String zeroes = "";
                for (int iter = 1; iter <= no_of_zeroes; iter++) {
                    zeroes = zeroes + "0";
                }
                strLine = strLine + zeroes;
                System.out.println("zeroes padded for the following line:" + zeroes.length());
                System.out.printf("\n\n\n");
            }


            for (int i = 0; i < 32; i += 2) {
                input[i / 2] = (byte) ((Character.digit(strLine.charAt(i), 16) << 4)
                        + Character.digit(strLine.charAt(i + 1), 16));
            }
            for (int i = 0; i < 16; i++) {
                state[i % 4][i / 4] = input[i];

            }

            inter=aes.encrypt(inp,input);
            stream.write(inter);
            StringBuilder sb = new StringBuilder();
            for (byte b : inter) {
                sb.append(String.format("%02X", b));
            }

            writer.append(sb.toString());
            writer.append("\r\n");

            inter=aes.decrypt(inp,inter);
            sb = new StringBuilder();
            for (byte b : inter) {
                sb.append(String.format("%02X", b));
            }
            writer1.append(sb.toString());
            writer1.append("\r\n");
        }

        in.close();

        stream.close();
        writer.flush();
        writer.close();
        writer1.flush();
        writer1.close();

    }


}

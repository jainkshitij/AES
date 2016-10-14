/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author kshitij
 */
public class Aes {
    private static int Rcon[] = { 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
    };
    private static int[] sbox = { 0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F,
            0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76, 0xCA, 0x82,
            0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C,
            0xA4, 0x72, 0xC0, 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC,
            0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15, 0x04, 0xC7, 0x23,
            0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27,
            0xB2, 0x75, 0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52,
            0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84, 0x53, 0xD1, 0x00, 0xED,
            0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58,
            0xCF, 0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9,
            0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8, 0x51, 0xA3, 0x40, 0x8F, 0x92,
            0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
            0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E,
            0x3D, 0x64, 0x5D, 0x19, 0x73, 0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A,
            0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB, 0xE0,
            0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62,
            0x91, 0x95, 0xE4, 0x79, 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E,
            0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08, 0xBA, 0x78,
            0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B,
            0xBD, 0x8B, 0x8A, 0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E,
            0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E, 0xE1, 0xF8, 0x98,
            0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55,
            0x28, 0xDF, 0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41,
            0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16 };
    private static int[] inv_sbox = { 0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5,
            0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB, 0x7C, 0xE3,
            0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4,
            0xDE, 0xE9, 0xCB, 0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D,
            0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E, 0x08, 0x2E, 0xA1,
            0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B,
            0xD1, 0x25, 0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4,
            0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92, 0x6C, 0x70, 0x48, 0x50,
            0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D,
            0x84, 0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4,
            0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06, 0xD0, 0x2C, 0x1E, 0x8F, 0xCA,
            0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
            0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF,
            0xCE, 0xF0, 0xB4, 0xE6, 0x73, 0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD,
            0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E, 0x47,
            0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E,
            0xAA, 0x18, 0xBE, 0x1B, 0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79,
            0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4, 0x1F, 0xDD,
            0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27,
            0x80, 0xEC, 0x5F, 0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D,
            0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF, 0xA0, 0xE0, 0x3B,
            0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53,
            0x99, 0x61, 0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1,
            0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D };

    /**
     * @param args the command line arguments
     */
    static int nb=4;
    static int nk=8;
    static int nr=14;
    static byte[][] genkey;
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

        // Open the file that is the first
        // command line parameter
        FileInputStream fstream = new FileInputStream("res/textfile.txt");
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        fstream=new FileInputStream("res/key.txt");
        in=new DataInputStream(fstream);
        BufferedReader keybr=new BufferedReader(new InputStreamReader(in));
        String strLine;
        Aes obj = new Aes();
        int i,j;
        byte[][] state = new byte[4][4];
        byte[][] key = new byte[4][8];
        byte [] input = new byte[16];
        byte [] inp = new byte[32];
        byte [] inter;
        strLine=keybr.readLine();

        for ( i = 0; i <64; i += 2) {
            inp[i / 2] = (byte) ((Character.digit(strLine.charAt(i), 16) << 4)
                    + Character.digit(strLine.charAt(i+1), 16));
        }
        for(i=0;i<32;i++)
            key [i%4][i/4]=inp[i];
  /* for(i=0;i<4;i++)
      {
          for(j=0;j<8;j++)
          {

              System.out.printf("%02X ", key[i][j]);
          }
          System.out.println();
      } */
        genkey=generateSubkeys(inp);
        // System.out.println(genkey.length);
  /*    for(i=0;i<genkey.length;i++)
      {
          for(j=0;j<4;j++)
          {
              System.out.printf("%02X ", genkey[i][j]);
          }
      } */
        File enc = new File("encryption.txt");
        File dec = new File("decryption.txt");
        FileWriter writer = new FileWriter(enc);
        FileWriter writer1 = new FileWriter(dec);
        FileOutputStream stream = new FileOutputStream("encryption.txt");

        while ((strLine = br.readLine()) != null)   {

            if (strLine.length()<32){
                int no_of_zeroes= 32-strLine.length();String zeroes="";
                for (int iter=1;iter<=no_of_zeroes;iter++){
                    zeroes=zeroes+"0";
                }
                strLine=strLine+zeroes;
                System.out.println("zeroes padded for the following line:"+zeroes.length());System.out.printf("\n\n\n");
            }


            for ( i = 0; i <32; i += 2) {
                input[i / 2] = (byte) ((Character.digit(strLine.charAt(i), 16) << 4)
                        + Character.digit(strLine.charAt(i+1), 16));
            }
            for(i=0;i<16;i++)
            {
                state[i%4][i/4]=input[i];

            }

   /*for(i=0;i<4;i++)
      {
          for(j=0;j<4;j++)
          {

              System.out.printf("%02X ", state[i][j]);
          }
          System.out.println();
      } */
            inter=AddRoundKey(input,0);
            System.out.println("Encryption Round 0");
            Display(inter);
            for(i=1;i<=13;i++)
            {
                inter=ByteSub(inter);
                //Display(inter);
                inter=ShiftRow(inter);
                //Display(inter);
                inter=MixColumn(inter);
                // Display(inter);
                inter=AddRoundKey(inter,i);
                System.out.println("Encryption Round "+i);
                Display(inter);

            }

            inter=ByteSub(inter);
            inter=ShiftRow(inter);
            inter=AddRoundKey(inter,14);
            System.out.println("Encryption Round "+i);
            Display(inter);

            stream.write(inter);
            StringBuilder sb = new StringBuilder();
            for (byte b : inter) {
                sb.append(String.format("%02X", b));
            }
            // System.out.println(sb.toString());
            writer.append(sb.toString());
            writer.append("\r\n");

            System.out.println("Decryption");
            //////////////////Decryption//////////////////////////////
            inter=AddRoundKey(inter,i);

            inter=InverseByteSub(inter);
            inter=InverseShiftRow(inter);
            System.out.println("Decryption Round "+0);
            Display(inter);

            for(i=13;i>0;i--)
            {
                inter=AddRoundKey(inter,i);
                //Display(inter);
                inter=InverseMixColumn(inter);
                inter=InverseByteSub(inter);
                inter=InverseShiftRow(inter);
                System.out.println("Decryption Round "+(14-i));
                Display(inter);
            }
            inter=AddRoundKey(inter,i);
            System.out.println("Decryption Round "+(14-i));
            Display(inter);
            sb = new StringBuilder();
            for (byte b : inter) {
                sb.append(String.format("%02X", b));
            }
            // System.out.println(sb.toString());
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
    private static byte[][] generateSubkeys(byte[] key) {
        byte[][] expkey = new byte[nb * (nr + 1)][4];
        int i;
        for(i = 0; i < nk; i++)
        {
            expkey[i][0] = key[i * 4];
            expkey[i][1] = key[i * 4 + 1];
            expkey[i][2] = key[i * 4 + 2];
            expkey[i][3] = key[i * 4 + 3];
        }
        for(i=nk;i<nb*(nr+1);i++)
        {
            byte[] temp = new byte[4];
            for(int k = 0;k<4;k++)
            {
                temp[k] = expkey[i-1][k];
            }
            if(i%nk==0)

            {

                temp = SubByte(rotateByte(temp));
                temp[0] = (byte) (temp[0] ^ (Rcon[i /nk]));
            }
            else if(i%nk==4)
            {
                temp = SubByte(temp);
            }
            expkey[i][0]=(byte)(expkey[i-nk][0]^temp[0]);
            expkey[i][1]=(byte)(expkey[i-nk][1]^temp[1]);
            expkey[i][2]=(byte)(expkey[i-nk][2]^temp[2]);
            expkey[i][3]=(byte)(expkey[i-nk][3]^temp[3]);




        }
        return expkey;
    }

    private static byte[] SubByte(byte[] temp) {
        for(int i=0;i<temp.length;i++)
        {
            //System.out.printf("%02X ",temp[i]);
            temp[i] = (byte) (sbox[temp[i] & 0x000000ff]);


        }
        return temp;
    }

    private static byte[] rotateByte(byte[] temp) {
        byte rot[]=new byte[4];
        rot[0]=temp[1];
        rot[1]=temp[2];
        rot[2]=temp[3];
        rot[3]=temp[0];
        //  System.out.printf("%02X ", rot[0]);
        return rot;

    }

    private static byte[] AddRoundKey(byte[] input,int round) {
        int i,j,c=0;
        j=0;
        // System.out.println("Round "+round);

        for(i=4*round;i<4*round+4;i++)
        {
            for(j=0;j<4;j++)
            {
                input[c]=(byte)(input[c]^genkey[i][j]);
                c++;
            }
        }
        return input;
    }

    private static byte[] ByteSub(byte[] inter) {
        int i;
        for(i=0;i<inter.length;i++)
        {
            inter[i]=(byte)(sbox[inter[i] & 0x000000ff]);
        }
        return inter;
    }

    private static byte[] ShiftRow(byte[] inter) {
        int i,j,c=0;
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for(i=0;i<16;i++)
        {
            state[i%4][i/4]=inter[i];

        }
        for(i=0;i<4;i++)
        {
            if(i==0)
            {
                for(j=0;j<4;j++)
                {
                    tempstate[i][j]=state[i][j];
                }
            }
            if(i==1)
            {
                tempstate[i][0]=state[i][1];
                tempstate[i][1]=state[i][2];
                tempstate[i][2]=state[i][3];
                tempstate[i][3]=state[i][0];
            }
            if(i==2)
            {
                tempstate[i][0]=state[i][2];
                tempstate[i][1]=state[i][3];
                tempstate[i][2]=state[i][0];
                tempstate[i][3]=state[i][1];
            }
            if(i==3)
            {
                tempstate[i][0]=state[i][3];
                tempstate[i][1]=state[i][0];
                tempstate[i][2]=state[i][1];
                tempstate[i][3]=state[i][2];
            }
        }
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                // System.out.printf("%02X ", tempstate[i][j]);
                inter[c++]=tempstate[j][i];

            }
            //System.out.println();
        }
        return inter;
    }

    private static byte[] MixColumn(byte[] inter) {
        byte b2=(byte)(0x02);
        byte b3=(byte)(0x03);
        byte a,b,c,d,e;
        int i,j;
        byte [] temp = new byte[4];
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for(i=0;i<16;i++)
        {
            state[i%4][i/4]=inter[i];

        }
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                temp[j]=state[j][i];
            }
            a=mul(temp[0],(byte)0x02);
            b=mul(temp[1],(byte)0x03);
            c=mul(temp[2],(byte)0x01);
            d=mul(temp[3],(byte)0x01);
            e=(byte)(a^b^c^d);
            tempstate[i][0]=e;
            a=mul(temp[0],(byte)0x01);
            b=mul(temp[1],(byte)0x02);
            c=mul(temp[2],(byte)0x03);
            d=mul(temp[3],(byte)0x01);
            e=(byte)(a^b^c^d);
            tempstate[i][1]=e;
            a=mul(temp[0],(byte)0x01);
            b=mul(temp[1],(byte)0x01);
            c=mul(temp[2],(byte)0x02);
            d=mul(temp[3],(byte)0x03);
            e=(byte)(a^b^c^d);
            tempstate[i][2]=e;
            a=mul(temp[0],(byte)0x03);
            b=mul(temp[1],(byte)0x01);
            c=mul(temp[2],(byte)0x01);
            d=mul(temp[3],(byte)0x02);
            e=(byte)(a^b^c^d);
            tempstate[i][3]=e;



        }
        c=0;
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                // System.out.printf("%02X ", tempstate[i][j]);
                inter[c++]=tempstate[i][j];
            }
            // System.out.println();
        }

        return inter;
    }
    private static byte mul(byte a,byte b)
    {
        byte p=0;
        int i;
        for(i=0;i<8;i++)
        {
            if((b&1)!=0)
            {
                p=(byte)(p^a);
                //  System.out.printf("%02X ",p);
            }
            b=(byte)(b>>1);
            byte carry= (byte)(a&0x80);
            a=(byte)(a<<1);
            if(carry==(byte)(0x80))
            {
                a=(byte)(a^(byte)0x1b);
            }
  /* System.out.printf("%02X ",carry);
 System.out.printf("%02X ",p);
  System.out.printf("%02X ",a);
   System.out.printf("%02X ",b);
   System.out.println(); */
        }
        return p;
    }

    private static byte[] InverseByteSub(byte[] inter) {
        for(int i=0;i<inter.length;i++)
        {
            //System.out.printf("%02X ",temp[i]);
            inter[i] = (byte) (inv_sbox[inter[i] & 0x000000ff]);


        }
        return inter;
    }
    private static void Display(byte[] inter)
    {
        for(int j=0;j<inter.length;j++)
            System.out.printf("%02X", inter[j]);
        System.out.println();
    }

    private static byte[] InverseShiftRow(byte[] inter) {
        int i,j,c=0;
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for(i=0;i<16;i++)
        {
            state[i%4][i/4]=inter[i];

        }
        for(i=0;i<4;i++)
        {
            if(i==0)
            {
                for(j=0;j<4;j++)
                {
                    tempstate[i][j]=state[i][j];
                }
            }
            if(i==1)
            {
                tempstate[i][0]=state[i][3];
                tempstate[i][1]=state[i][0];
                tempstate[i][2]=state[i][1];
                tempstate[i][3]=state[i][2];
            }
            if(i==2)
            {
                tempstate[i][0]=state[i][2];
                tempstate[i][1]=state[i][3];
                tempstate[i][2]=state[i][0];
                tempstate[i][3]=state[i][1];
            }
            if(i==3)
            {
                tempstate[i][0]=state[i][1];
                tempstate[i][1]=state[i][2];
                tempstate[i][2]=state[i][3];
                tempstate[i][3]=state[i][0];
            }
        }
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                // System.out.printf("%02X ", tempstate[i][j]);
                inter[c++]=tempstate[j][i];

            }
            //System.out.println();
        }
        return inter;
    }

    private static byte[] InverseMixColumn(byte[] inter) {
        byte b14=(byte)(0x14);
        byte b13=(byte)(0x13);
        byte b11=(byte)(0x11);
        byte b9=(byte)(0x09);
        byte a,b,c,d,e;
        int i,j;
        byte [] temp = new byte[4];
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for(i=0;i<16;i++)
        {
            state[i%4][i/4]=inter[i];

        }
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                temp[j]=state[j][i];
            }
            a=mul(temp[0],(byte)0x0e);
            b=mul(temp[1],(byte)0x0b);
            c=mul(temp[2],(byte)0x0d);
            d=mul(temp[3],(byte)0x09);
            e=(byte)(a^b^c^d);
            tempstate[i][0]=e;
            a=mul(temp[0],(byte)0x09);
            b=mul(temp[1],(byte)0x0e);
            c=mul(temp[2],(byte)0x0b);
            d=mul(temp[3],(byte)0x0d);
            e=(byte)(a^b^c^d);
            tempstate[i][1]=e;
            a=mul(temp[0],(byte)0x0d);
            b=mul(temp[1],(byte)0x09);
            c=mul(temp[2],(byte)0x0e);
            d=mul(temp[3],(byte)0x0b);
            e=(byte)(a^b^c^d);
            tempstate[i][2]=e;
            a=mul(temp[0],(byte)0x0b);
            b=mul(temp[1],(byte)0x0d);
            c=mul(temp[2],(byte)0x09);
            d=mul(temp[3],(byte)0x0e);
            e=(byte)(a^b^c^d);
            tempstate[i][3]=e;



        }
        c=0;
        for(i=0;i<4;i++)
        {
            for(j=0;j<4;j++)
            {
                // System.out.printf("%02X ", tempstate[i][j]);
                inter[c++]=tempstate[i][j];
            }
            // System.out.println();
        }

        return inter;
    }
}

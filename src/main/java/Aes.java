/**
 * Created by k22jain on 15/10/16.
 */
public class Aes {

    final static int nb = 4;
    final static int nk = 8;
    final static int nr = 14;
    private final static byte two = (byte) 0x02;
    private final static byte three = (byte) 0x03;
    private final static byte nine = (byte) 0x09;
    private final static byte eleven = (byte) 0x0b;
    private final static byte thirteen = (byte) 0x0d;
    private final static byte fourteen = (byte) 0x0e;
    static byte[][] genkey;

    private static byte[][] generateSubkeys(byte[] key) {
        byte[][] expandedKey= new byte[nb * (nr + 1)][4];

        for (int i = 0; i < nk; i++) {
            for(int j=0;j<4;j++) {
                expandedKey[i][j]=key[i*4+j];
            }
        }

        for (int i = nk; i < nb * (nr + 1); i++) {
            byte[] temp = new byte[4];
            System.arraycopy(expandedKey[i - 1], 0, temp, 0, 4);
            if (i % nk == 0) {
                temp = SubByte(rotateByte(temp));
                temp[0] = (byte) (temp[0] ^ (Constants.Rcon[i / nk]));
            } else if (i % nk == 4) {
                temp = SubByte(temp);
            }
            for(int j=0;j<4;j++) {
                expandedKey[i][j]=(byte) (expandedKey[i - nk][j] ^ temp[j]);
            }
        }
        return expandedKey;
    }

    private static byte[] SubByte(byte[] temp) {
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (byte) (Constants.sbox[temp[i] & 0x000000ff]);
        }
        return temp;
    }

    private static byte[] rotateByte(byte[] temp) {
        byte rot[] = new byte[4];
        for(int i=0;i<rot.length;i++) {
            rot[i]=temp[(i+1)%4];
        }
        return rot;
    }

    private static byte[] AddRoundKey(byte[] input, int round) {
        int c = 0;
        for (int i = 4 * round; i < 4 * round + 4; i++) {
            for (int j = 0; j < 4; j++) {
                input[c] = (byte) (input[c++] ^ genkey[i][j]);
            }
        }
        return input;
    }

    private static byte[] ByteSub(byte[] inter) {
        int i;
        for (i = 0; i < inter.length; i++) {
            inter[i] = (byte) (Constants.sbox[inter[i] & 0x000000ff]);
        }
        return inter;
    }

    private static byte[] ShiftRow(byte[] inter) {
        int i, j, c = 0;
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for (i = 0; i < 16; i++) {
            state[i % 4][i / 4] = inter[i];

        }
        for (i = 0; i < 4; i++) {
            for(j=0;j<4;j++)
            {
                tempstate[i][j]=state[i][(j+i)%4];
            }
        }
        return Util.columnMajorArrayConvertor(tempstate);
    }

    private static byte[] MixColumn(byte[] inter) {
        int i, j;
        byte[] temp = new byte[4];
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for (i = 0; i < 16; i++) {
            state[i % 4][i / 4] = inter[i];

        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                temp[j] = state[j][i];
            }
            tempstate[i][0] = (byte) (Util.byteMultiply(temp[0], two) ^ Util.byteMultiply(temp[1], three) ^ temp[2] ^ temp[3]);
            tempstate[i][1] = (byte) (temp[0] ^ Util.byteMultiply(temp[1], two) ^ Util.byteMultiply(temp[2], three) ^ temp[3]);
            tempstate[i][2] = (byte) (temp[0] ^ temp[1] ^ Util.byteMultiply(temp[2], two) ^ Util.byteMultiply(temp[3], three));
            tempstate[i][3] = (byte) (Util.byteMultiply(temp[0], three) ^ temp[1] ^ temp[2] ^ Util.byteMultiply(temp[3], two));
        }
        return Util.arrayConvertor(tempstate);
    }

    private static byte[] InverseByteSub(byte[] inter) {
        for (int i = 0; i < inter.length; i++) {
            inter[i] = (byte) (Constants.inv_sbox[inter[i] & 0x000000ff]);
        }
        return inter;
    }

    private static byte[] InverseShiftRow(byte[] inter) {
        int i, j, c = 0;
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for (i = 0; i < 16; i++) {
            state[i % 4][i / 4] = inter[i];

        }
        for (i = 0; i < 4; i++) {
            for(j=0;j<4;j++)
            {
                tempstate[i][j]=state[i][(j+4-i)%4];
            }
        }
        return Util.columnMajorArrayConvertor(tempstate);
    }

    private static byte[] InverseMixColumn(byte[] inter) {

        int i, j;
        byte[] temp = new byte[4];
        byte[][] state = new byte[4][4];
        byte[][] tempstate = new byte[4][4];
        for (i = 0; i < 16; i++) {
            state[i % 4][i / 4] = inter[i];

        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                temp[j] = state[j][i];
            }
            tempstate[i][0] = (byte) (Util.byteMultiply(temp[0],fourteen) ^ Util.byteMultiply(temp[1],eleven) ^ Util.byteMultiply(temp[2],thirteen) ^ Util.byteMultiply(temp[3],nine));
            tempstate[i][1] = (byte) (Util.byteMultiply(temp[0],nine) ^ Util.byteMultiply(temp[1],fourteen) ^ Util.byteMultiply(temp[2],eleven) ^ Util.byteMultiply(temp[3],thirteen));
            tempstate[i][2] = (byte) (Util.byteMultiply(temp[0],thirteen) ^ Util.byteMultiply(temp[1],nine) ^ Util.byteMultiply(temp[2],fourteen) ^ Util.byteMultiply(temp[3],eleven));
            tempstate[i][3] = (byte) (Util.byteMultiply(temp[0],eleven) ^ Util.byteMultiply(temp[1],thirteen) ^ Util.byteMultiply(temp[2],nine) ^ Util.byteMultiply(temp[3],fourteen));
        }
        return Util.arrayConvertor(tempstate);
    }

    public byte[] encrypt(byte[] key, byte[] input) {
        for (int i = 0; i < 32; i++) {
            genkey = generateSubkeys(key);
        }
        byte[] inter = AddRoundKey(input, 0);
        for (int i = 1; i <= 13; i++) {
            inter = ByteSub(inter);
            inter = ShiftRow(inter);
            inter = MixColumn(inter);
            inter = AddRoundKey(inter, i);
        }
        inter = ByteSub(inter);
        inter = ShiftRow(inter);
        inter = AddRoundKey(inter, 14);
        return inter;
    }

    public byte[] decrypt(byte[] key, byte[] input) {
        for (int i = 0; i < 32; i++) {
            genkey = generateSubkeys(key);
        }
        byte[] inter = AddRoundKey(input, 14);
        inter = InverseByteSub(inter);
        inter = InverseShiftRow(inter);
        for (int i = 13; i > 0; i--) {
            inter = AddRoundKey(inter, i);
            inter = InverseMixColumn(inter);
            inter = InverseByteSub(inter);
            inter = InverseShiftRow(inter);
        }
        inter = AddRoundKey(inter, 0);
        return inter;
    }

}

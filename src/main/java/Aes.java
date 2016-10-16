/**
 * Created by k22jain on 15/10/16.
 */
public class Aes {

    final static int nb = 4;
    final static int nk = 8;
    final static int nr = 14;
    private final static byte two = (byte) 0x02;
    private final static byte three = (byte) 0x03;
    static byte[][] genkey;

    private static byte[][] generateSubkeys(byte[] key) {
        byte[][] expkey = new byte[nb * (nr + 1)][4];
        int i;
        for (i = 0; i < nk; i++) {
            expkey[i][0] = key[i * 4];
            expkey[i][1] = key[i * 4 + 1];
            expkey[i][2] = key[i * 4 + 2];
            expkey[i][3] = key[i * 4 + 3];
        }
        for (i = nk; i < nb * (nr + 1); i++) {
            byte[] temp = new byte[4];
            System.arraycopy(expkey[i - 1], 0, temp, 0, 4);
            if (i % nk == 0) {
                temp = SubByte(rotateByte(temp));
                temp[0] = (byte) (temp[0] ^ (Constants.Rcon[i / nk]));
            } else if (i % nk == 4) {
                temp = SubByte(temp);
            }
            expkey[i][0] = (byte) (expkey[i - nk][0] ^ temp[0]);
            expkey[i][1] = (byte) (expkey[i - nk][1] ^ temp[1]);
            expkey[i][2] = (byte) (expkey[i - nk][2] ^ temp[2]);
            expkey[i][3] = (byte) (expkey[i - nk][3] ^ temp[3]);


        }
        return expkey;
    }

    private static byte[] SubByte(byte[] temp) {
        for (int i = 0; i < temp.length; i++) {

            temp[i] = (byte) (Constants.sbox[temp[i] & 0x000000ff]);


        }
        return temp;
    }

    private static byte[] rotateByte(byte[] temp) {
        byte rot[] = new byte[4];
        rot[0] = temp[1];
        rot[1] = temp[2];
        rot[2] = temp[3];
        rot[3] = temp[0];

        return rot;

    }

    private static byte[] AddRoundKey(byte[] input, int round) {
        int i, j, c = 0;
        for (i = 4 * round; i < 4 * round + 4; i++) {
            for (j = 0; j < 4; j++) {
                input[c] = (byte) (input[c] ^ genkey[i][j]);
                c++;
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
            if (i == 0) {
                for (j = 0; j < 4; j++) {
                    tempstate[i][j] = state[i][j];
                }
            }
            if (i == 1) {
                tempstate[i][0] = state[i][1];
                tempstate[i][1] = state[i][2];
                tempstate[i][2] = state[i][3];
                tempstate[i][3] = state[i][0];
            }
            if (i == 2) {
                tempstate[i][0] = state[i][2];
                tempstate[i][1] = state[i][3];
                tempstate[i][2] = state[i][0];
                tempstate[i][3] = state[i][1];
            }
            if (i == 3) {
                tempstate[i][0] = state[i][3];
                tempstate[i][1] = state[i][0];
                tempstate[i][2] = state[i][1];
                tempstate[i][3] = state[i][2];
            }
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                inter[c++] = tempstate[j][i];
            }

        }
        return inter;
    }

/*    private static byte[] MixColumn(byte[] inter) {
        byte a, b, c, d, e;
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
            a = mul(temp[0], (byte) 0x02);
            b = mul(temp[1], (byte) 0x03);
            c = mul(temp[2], (byte) 0x01);
            d = mul(temp[3], (byte) 0x01);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][0] = e;
            a = mul(temp[0], (byte) 0x01);
            b = mul(temp[1], (byte) 0x02);
            c = mul(temp[2], (byte) 0x03);
            d = mul(temp[3], (byte) 0x01);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][1] = e;
            a = mul(temp[0], (byte) 0x01);
            b = mul(temp[1], (byte) 0x01);
            c = mul(temp[2], (byte) 0x02);
            d = mul(temp[3], (byte) 0x03);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][2] = e;
            a = mul(temp[0], (byte) 0x03);
            b = mul(temp[1], (byte) 0x01);
            c = mul(temp[2], (byte) 0x01);
            d = mul(temp[3], (byte) 0x02);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][3] = e;


        }
        c = 0;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                inter[c++] = tempstate[i][j];
            }
        }

        return inter;
    }*/

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
            tempstate[i][0] = (byte)(mul(temp[0],two)^mul(temp[1],three)^temp[2]^temp[3]);
            tempstate[i][1] = (byte)(temp[0]^mul(temp[1],two)^mul(temp[2],three)^temp[3]);
            tempstate[i][2] = (byte)(temp[0]^temp[1]^mul(temp[2],two)^mul(temp[3],three));
            tempstate[i][3] = (byte)(mul(temp[0],three)^temp[1]^temp[2]^mul(temp[3],two));
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                inter[4*i+j] = tempstate[i][j];
            }
        }

        return inter;
    }

    private static byte mul(byte a, byte b) {
        byte p = 0;
        int i;
        for (i = 0; i < 8; i++) {
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
            if (i == 0) {
                for (j = 0; j < 4; j++) {
                    tempstate[i][j] = state[i][j];
                }
            }
            if (i == 1) {
                tempstate[i][0] = state[i][3];
                tempstate[i][1] = state[i][0];
                tempstate[i][2] = state[i][1];
                tempstate[i][3] = state[i][2];
            }
            if (i == 2) {
                tempstate[i][0] = state[i][2];
                tempstate[i][1] = state[i][3];
                tempstate[i][2] = state[i][0];
                tempstate[i][3] = state[i][1];
            }
            if (i == 3) {
                tempstate[i][0] = state[i][1];
                tempstate[i][1] = state[i][2];
                tempstate[i][2] = state[i][3];
                tempstate[i][3] = state[i][0];
            }
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {

                inter[c++] = tempstate[j][i];

            }

        }
        return inter;
    }

    private static byte[] InverseMixColumn(byte[] inter) {

        byte a, b, c, d, e;
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
            a = mul(temp[0], (byte) 0x0e);
            b = mul(temp[1], (byte) 0x0b);
            c = mul(temp[2], (byte) 0x0d);
            d = mul(temp[3], (byte) 0x09);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][0] = e;
            a = mul(temp[0], (byte) 0x09);
            b = mul(temp[1], (byte) 0x0e);
            c = mul(temp[2], (byte) 0x0b);
            d = mul(temp[3], (byte) 0x0d);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][1] = e;
            a = mul(temp[0], (byte) 0x0d);
            b = mul(temp[1], (byte) 0x09);
            c = mul(temp[2], (byte) 0x0e);
            d = mul(temp[3], (byte) 0x0b);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][2] = e;
            a = mul(temp[0], (byte) 0x0b);
            b = mul(temp[1], (byte) 0x0d);
            c = mul(temp[2], (byte) 0x09);
            d = mul(temp[3], (byte) 0x0e);
            e = (byte) (a ^ b ^ c ^ d);
            tempstate[i][3] = e;


        }
        c = 0;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {

                inter[c++] = tempstate[i][j];
            }
        }
        return inter;
    }

    public byte[] encrypt (byte[] key, byte[] input)
    {
        for (int i = 0; i < 32; i++) {
            genkey = generateSubkeys(key);
        }
        byte[] inter= AddRoundKey(input, 0);
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

    public byte[] decrypt (byte[] key, byte[] input) {
        for (int i = 0; i < 32; i++) {
            genkey = generateSubkeys(key);
        }
        byte[] inter= AddRoundKey(input, 14);
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

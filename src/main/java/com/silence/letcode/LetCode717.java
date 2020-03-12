package com.silence.letcode;

/**
 * @author 李晓冰
 * @date 2019年11月17日
 */
public class LetCode717 {
    public boolean isOneBitCharacter(int[] bits) {
        return isOneBitCharacterIterate(bits);
    }

    private boolean isOneBitCharacter(int[] bits, int index) {
        if (bits.length - index == 1) {
            return true;
        }
        if (bits.length - index == 2) {
            if (bits[index]==0)
                return true;
            else
                return false;
        }

        int curBit = bits[index];
        if (curBit == 0) {
            return isOneBitCharacter(bits, index + 1);
        } else {
            return isOneBitCharacter(bits,index+2);
        }
    }

    private boolean isOneBitCharacterIterate(int[] bits){
        if (bits.length==1)
            return true;

        for (int i = 0; i < bits.length;) {
            if (i == bits.length-2 && bits[i] == 1)
                return false;

            if (bits[i] == 1) {
                i += 2;
            } else {
                i+=1;
            }
        }
        return true;
    }

    private boolean isOneBitCharacterFinal(int[] bits) {
        int last = -1;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]==1)
                i++;
            else
                last = i;
        }
        return last == bits.length-1;
    }

    public static void main(String[] args) {
        LetCode717 letCode717 = new LetCode717();
        int[] bits = new int[]{1,1,1,1,0,0};
        System.out.println(letCode717.isOneBitCharacter(bits));
    }
}

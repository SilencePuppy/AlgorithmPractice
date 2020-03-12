package com.silence.wz;

import com.silence.illustration.TimSortLow;

import java.util.Comparator;
import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年02月18日
 */
public class Test {
    public static void main(String[] args) {
        byte a=127;
        System.out.println(a<<1);



        Comparator c =Comparator.comparing(Integer::intValue);

        Random random = new Random(System.currentTimeMillis());
        int len = 500;
        int i = 0;
        Integer[] arr = new Integer[len];
        while (i < len) {
            arr[i++] = random.nextInt(100);
        }
        printArr(arr);
        TimSortLow.sort(arr, 0, arr.length,c);
        printArr(arr);

    }

    static void printArr(Integer[] arr){
        int i=0;
        for (Integer integer : arr) {
            System.out.print(integer);
            System.out.print(" ");
            if (++i % 30 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("-----------------------------------------------");
    }

}
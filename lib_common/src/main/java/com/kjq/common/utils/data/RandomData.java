package com.kjq.common.utils.data;

import java.util.Random;

/**
 * <p>随机数据</p>
 *
 * @author 康建群 948182974---->>>2018/8/7 18:28
 * @version 1.0.0
 */
public class RandomData {

    public static int getRandom(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }

    public static int getRan(int min,int max){
        Random random = new Random();
        return random.nextInt( max )%( max - min + 1 ) + min;
    }

}

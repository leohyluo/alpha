package com.alpha.commons.util.sim;

import com.alpha.commons.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by iEBM on 2017-05-12.
 */
public class Similarity {

    public static  Map<Character, int[]>  initSimilarity(String string1, String string2) {
         Map<Character, int[]> vectorMap = new HashMap<Character, int[]>();
        int[] tempArray = null;
        for (Character character1 : string1.toCharArray()) {
            if (vectorMap.containsKey(character1)) {
                vectorMap.get(character1)[0]++;
            } else {
                tempArray = new int[2];
                tempArray[0] = 1;
                tempArray[1] = 0;
                vectorMap.put(character1, tempArray);
            }
        }
        for (Character character2 : string2.toCharArray()) {
            if (vectorMap.containsKey(character2)) {
                vectorMap.get(character2)[1]++;
            } else {
                tempArray = new int[2];
                tempArray[0] = 0;
                tempArray[1] = 1;
                vectorMap.put(character2, tempArray);
            }
        }
        return vectorMap;
    }

    // 求余弦相似度
    public static double sim(String string1, String string2) {
        if(StringUtils.isEmpty(string1)||StringUtils.isEmpty(string2))
            return 0d;
        Map<Character, int[]> vectorMap = initSimilarity(string1, string2);
        double result = 0;
        result = pointMulti(vectorMap) / sqrtMulti(vectorMap);
        return result;
    }

     public static double sqrtMulti(Map<Character, int[]> paramMap) {
        double result = 0;
        result = squares(paramMap);
        result = Math.sqrt(result);
        return result;
    }

    // 求平方和
    private static double squares(Map<Character, int[]> paramMap) {
        double result1 = 0;
        double result2 = 0;
        Set<Character> keySet = paramMap.keySet();
        for (Character character : keySet) {
            int temp[] = paramMap.get(character);
            result1 += (temp[0] * temp[0]);
            result2 += (temp[1] * temp[1]);
        }
        return result1 * result2;
    }

    // 点乘法
    private static double pointMulti(Map<Character, int[]> paramMap) {
        double result = 0;
        Set<Character> keySet = paramMap.keySet();
        for (Character character : keySet) {
            int temp[] = paramMap.get(character);
            result += (temp[0] * temp[1]);
        }
        return result;
    }

/*
    public static void main(String[] args) {
        String s1 = "乏力";
        String s2 = "心力衰竭";
        System.out.println(sim(s1,s2));
        String s3 = "乏力";
        String s4 = "疲乏无力";
        System.out.println(sim(s3,s4));


    }*/
}

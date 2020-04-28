package org.noear.water.utils;

import java.util.Random;

public class RandomUtils {
    public static String code(int size) {
        char codeTemplate[] = {
                'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x',
                'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R',
                'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z',
                '0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9'
        };
        int temp_size = codeTemplate.length;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append(codeTemplate[random.nextInt(temp_size) % temp_size]);
        }

        return sb.toString();
    }
}

package org.gatein.lwwcm.misc.test;

import org.junit.Test;

public class RegexTest {

    @Test
    public void variablesTest() {

        String test1 = "/tmp/${system.variable}/tmp2/${system.variable2}/${system.variable3}/${system.variable4}";
        // test1 = "nothingtochange";

        int first = 0;
        while (first > -1) {
            first = test1.indexOf("${", 0);
            int next = test1.indexOf("}", first);
            if (next > -1) {
                String variable = test1.substring(first, next+1);
                System.out.println(variable);
                test1 = test1.replace(variable, "FOUND");
                System.out.println(test1);
            }
        }
        System.out.println(test1);
    }

}

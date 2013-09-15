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

    @Test
    public void extractProperties() {
        String template = "<wcm-list   id = \"value1\" class=\"value2\"   >";
        int start = template.indexOf(" ");
        int i = start;
        int j = 0;
        while (j != -1) {
            // Look property name
            j = template.indexOf("=", i);
            if (j != -1) {
                String name = template.substring(i, j).trim();
                i = template.indexOf("\"", j);
                j = template.indexOf("\"", i+1);
                String value = template.substring(i+1, j);
                i = j+1;
                System.out.println(name + "=" + value);
            }
        }
    }

    @Test
    public void extractImages() {
        String html = "<img src=\"uno\"> <img src=\"tres\"> <img src=\"dos\">";
        int index = 4;
        int found = 0;
        int i = 0;
        int j = 0;
        String output = "<img>";
        while (found != -1) {
            j = html.indexOf("<img", i);
            if (j != -1) {
                if (found == index) {
                    i = html.indexOf(">", j);
                    output = html.substring(j, i+1);
                    found = -1;
                } else {
                    i = j+1;
                    found++;
                }
            } else {
                found = -1;
            }
        }
        System.out.println(output);
    }

    @Test
    public void cleanImgStyle() {
        String html = "<img alt=\"Mime mania\" class=\"test-class\" src=\"/lwwcm/rs/u/185\" style=\"height:480px; width:640px\">";
        String regexp = "style=\"[0-9a-zA-Z :;,-]*\"";

        System.out.println(html.replace(regexp, ""));
    }

    @Test
    public void replaceCssClass() {
        String html = "<img alt=\"Mime mania\" src=\"/lwwcm/rs/u/185\" style=\"height:480px; width:640px\">";
        String regexp = "class=\"[0-9a-zA-Z_-]*\"";
        System.out.println(html.replace(regexp, ""));
    }

}

package org.gatein.lwwcm.misc.test;

import junit.framework.Assert;
import org.gatein.lwwcm.portlet.content.render.WcmTags;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    public void parseVariablesTest() {
        String test1 = "/tmp/${system.variable}/tmp2/${system.variable2}/${system.variable3}/${system.variable4}";
        int first = 0;
        while (first > -1) {
            first = test1.indexOf("${", 0);
            int next = test1.indexOf("}", first);
            if (next > -1) {
                String variable = test1.substring(first, next+1);
                test1 = test1.replace(variable, "FOUND");
            }
        }
        Assert.assertEquals("/tmp/FOUND/tmp2/FOUND/FOUND/FOUND", test1);
    }

    @Test
    public void extractPropertiesTest() {
        String template = "<wcm-list   id = \"value1\" class=\"value2\"   >";
        int start = template.indexOf(" ");
        int i = start;
        int j = 0;
        int count = 0;
        while (j != -1) {
            // Look property name
            j = template.indexOf("=", i);
            if (j != -1) {
                String name = template.substring(i, j).trim();
                i = template.indexOf("\"", j);
                j = template.indexOf("\"", i+1);
                String value = template.substring(i+1, j);
                i = j+1;
                if (count == 0) {
                    Assert.assertEquals("id", name);
                    Assert.assertEquals("value1", value);
                }
                if (count == 1) {
                    Assert.assertEquals("class", name);
                    Assert.assertEquals("value2", value);
                }
                count++;
            }
        }
    }

    @Test
    public void addClassImage() {
        String img1 = "<img>";
        String img2 = "<img src=\"Test\">";
        String img3 = "<img src='Test'>";
        String img4 = "<img src=\"Test\" class=\"something\">";
        String img5 = "<img src=\"Test\" class='something'>";

        System.out.println(img5.replaceAll("class=[\"''](.*)[\"']", "class=\"$1 mas\""));
        System.out.println(img5.matches(".*src=(\"|').*(\"|').*"));
    }

    @Test
    public void extractImg() {
        WcmTags tags = new WcmTags();
        String img1 = "<img>";
        String img5 = "<img src=\"Test\" class='something'>";

        Assert.assertEquals("<img>", tags.extractImg(img1, 0, true));
        Assert.assertEquals("<img src=\"Test\">", tags.extractImg(img5, 0, true));


    }

}

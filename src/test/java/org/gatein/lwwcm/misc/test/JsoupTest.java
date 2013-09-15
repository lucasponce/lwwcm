package org.gatein.lwwcm.misc.test;

import org.gatein.lwwcm.portlet.content.render.RenderActions;
import org.gatein.lwwcm.portlet.content.render.WcmTags;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void helloWorld() throws Exception {
        String html = "<div><p>" +
                                "<div wcm-type='list'>This is a template for wcm-content list</div>" +
                                "<div wcm-type='single'>This is a template for wcm-content single</div>" +
                      "</p></div>";
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        Elements elements = body.getElementsByAttribute("wcm-type");
        for (Element e : elements) {
            System.out.println("WCM tag:");
            if (!"".equals(e.attr("wcm-type")))
                System.out.print("wcm-type: " + e.attr("wcm-type"));
            if (!"".equals(e.attr("wcm-content")))
                System.out.print(" wcm-content: " + e.attr("wcm-content"));
            System.out.println(" template: " + e.text());
        }
    }

    @Test
    public void wcmTagListTest() throws Exception {
        String html = "<div><p>" +
                "<wcm-list>" +
                    "<h1><wcm-title /></h1>" +
                    "<h4><wcm-excerpt /></h4>" +
                    "<p><wcm-author /> Date: <wcm-date /></p>" +
                    "<p><wcm-content /></p>" +
                "</wcm-list>" +
                "<wcm-single>This is a template for wcm-content single</wcm-single>" +
                "</p></div>";
        WcmTags r = new WcmTags();
        String output = r.extractTag("wcm-list", html);
        String inside = r.insideTag("wcm-list", output);
        String processed = html.replace(output, "PROCESSED");
        System.out.println(output);
        System.out.println(inside);
        System.out.println(processed);

        output = r.extractTag("wcm-single", processed);
        processed = processed.replace(output, "PROCESSED2");
        System.out.println(output);
        System.out.println(inside);
        System.out.println(processed);

    }
}

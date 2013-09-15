package org.gatein.lwwcm.portlet.content.render;

import org.gatein.lwwcm.domain.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Wcm custom tags processing
 */
public class WcmTags {

    /*
        <wcm-list> / <wcm-param-list> tag processing
     */
    public String tagWcmList(String tagWcmList, String initialTemplate, List<Post> listPosts) {
        String processedTemplate = "";
        String tag = extractTag(tagWcmList, initialTemplate);
        if (tag != null && listPosts != null) {
            String inside = insideTag(tagWcmList, tag);
            Map<String, String> properties = propertiesTag(tag);
            int from = 0;
            int to = listPosts.size();
            if (properties.containsKey("from")) {
                String value = properties.get("from");
                if (value.equals("first")) {
                    from = 0;
                } else if (value.equals("last")) {
                    from = listPosts.size();
                } else {
                    try {
                        from = new Integer(value).intValue();
                        if (from < 0) from = 0;
                    } catch (Exception e) {
                        // Default value if exception happens
                    }
                }
            }
            if (properties.containsKey("to")) {
                String value = properties.get("to");
                if (value.equals("first")) {
                    to = 0;
                } else if (value.equals("last")) {
                    to = listPosts.size();
                } else {
                    try {
                        to = new Integer(value).intValue();
                        if (to > listPosts.size()) to = listPosts.size();
                    } catch (Exception e) {
                        // Default value if exception happens
                    }
                }
            }
            int size = to - from;
            String outputList = "<";
            if (size == 1) {
                outputList += "div";
            } else {
                outputList += "ul";
            }
            if (properties.containsKey("id")) {
                outputList += " id=\"" + properties.get("id") + "\"";
            }
            if (properties.containsKey("class")) {
                outputList += " class=\"" + properties.get("class") + "\"";
            }

            outputList += " >";
            if (listPosts != null) {
                for (int i = from; i < to; i++) {
                    Post p = listPosts.get(i);
                    if (size > 1) outputList += "<li>";
                    outputList += combine(inside, p, i);
                    if (size > 1) outputList += "</li>";
                }
            }
            outputList += "</";
            if (size == 1) {
                outputList += "div";
            } else {
                outputList += "ul";
            }
            outputList += ">";
            processedTemplate = initialTemplate.replace(tag, outputList);
        } else if (tag != null && listPosts == null) {
            processedTemplate = initialTemplate.replace(tag, "<div>No content found</div>");
        }
        return processedTemplate;
    }

    /*
        <wcm-single> / <wcm-param-single> tag processing
     */
    public String tagWcmSingle(String tagWcmSingle, String initialTemplate, Post post) {
        String processedTemplate = null;
        String tag = extractTag(tagWcmSingle, initialTemplate);
        if (tag != null) {
            String inside = insideTag(tagWcmSingle, tag);
            String outputSingle = "<div>";
            outputSingle += combine(inside, post, 0);
            outputSingle += "</div>";
        }
        return processedTemplate;
    }

    /*
        Combine in-line tags with Post object
     */
    public String combine(String template, Post post, int iteration) {
        if (post == null) return "";
        boolean foundTag = false;
        String output = template;
        while (!foundTag) {
            if (hasTag("wcm-link", output)) {
                output = tagWcmLink(output, post);
            } else if (hasTag("wcm-img", output)) {
                output = tagWcmImg(output, post);
            } else if (hasTag("wcm-title", output)) {
                output = tagWcmTitle(output, post);
            } else if (hasTag("wcm-excerpt", output)) {
                output = tagWcmExcerpt(output, post);
            } else if (hasTag("wcm-iter", output)) {
                output = tagWcmIter(output, iteration);
            } else {
                foundTag = true;
            }
        }
        return output;
    }

    /*
        <wcm-link> tag processing
     */
    public String tagWcmLink(String template, Post post) {
        String tag = extractTag("wcm-link", template);
        String inside = insideTag("wcm-link", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "<a";
        if (properties.containsKey("href")) {
            output += " href=\"" + properties.get("href") + "\"";
        } else {
            output += " href=\"#Post" + post.getId() + "\"";
        }
        if (properties.containsKey("class")) {
            output += " class=\"" + properties.get("class") + "\"";
        }
        output += ">";
        output += inside;
        output += "</a>";
        return template.replace(tag, output);
    }

    /*
        <wcm-img> tag processing
     */
    public String tagWcmImg(String template, Post post) {
        String tag = extractTag("wcm-img", template);
        String inside = insideTag("wcm-img", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        int index = 0;
        if (properties.containsKey("index")) {
            try {
                index = new Integer(properties.get("index")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
        }
        output = extractImg(post.getContent(), index);
        // Check style
        if (properties.containsKey("class")) {
            String cssClass = properties.get("class");
            // Reset class in img extracted
            output = output.replaceAll("class=\"[0-9a-zA-Z_-]*\"", "");
            output = "<img class=\"" + cssClass + "\" " + output.substring(4);
        }
        // Cleaning hard code style
        output = output.replaceAll("style=\"[0-9a-zA-Z :;,-]*\"", "");
        return template.replace(tag, output);
    }

    /*
        <wcm-title> tag processing
     */
    public String tagWcmTitle(String template, Post post) {
        String tag = extractTag("wcm-title", template);
        String inside = insideTag("wcm-title", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("max-length")) {
            int max = 100;
            try {
                max = new Integer(properties.get("max-length")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            output = post.getTitle().substring(0, max);
        } else {
            output = post.getTitle();
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-excerpt> tag processing
     */
    public String tagWcmExcerpt(String template, Post post) {
        String tag = extractTag("wcm-excerpt", template);
        String inside = insideTag("wcm-excerpt", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("max-length")) {
            int max = 100;
            try {
                max = new Integer(properties.get("max-length")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            output = post.getExcerpt().substring(0, max);
        } else {
            output = post.getExcerpt();
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-iter> tag processing
     */
    public String tagWcmIter(String template, int iteration) {
        String tag = extractTag("wcm-iter", template);
        String inside = insideTag("wcm-iter", template);
        Map<String, String> properties = propertiesTag(tag);
        int i = -1;
        if (properties.containsKey("i")) {
            try {
                i = new Integer(properties.get("i")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            if (iteration == i) {
                return template.replace(tag, inside);
            }
        } else if (properties.containsKey("par")) {
            if ("true".equals(properties.get("par")) && (i%2 == 0)) {
                return template.replace(tag, inside);
            }
        }
        return template.replace(tag, "");
    }

    /*
        Aux functions to manipulate tags
     */
    public boolean hasTag(String tag, String template) {
        if (template == null || template.equals("")) return false;
        return (template.indexOf("<" + tag) > -1);
    }

    public String extractTag(String tag, String template) {
        String output = "";
        int i = template.indexOf("<" + tag);
        if (i != -1) {
            int j = template.indexOf(">", i);
            // Check if we are in <tag /> or <tag></tag>
            if (j > 0 && template.charAt(j-1) == '/') {
                output = template.substring(i, j + 1);
            } else {
                j = template.indexOf("</" + tag + ">", i);
                if (j>-1) {
                    output = template.substring(i, j + ("</" + tag + ">").length());
                }
            }
        }
        return output;
    }

    public String extractImg(String html, int index) {
        if (html == null) return "<img>";
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
        return output;
    }

    public Map<String, String> propertiesTag(String template) {
        Map<String, String> output = new HashMap<String, String>();
        if (template != null) {
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
                    output.put(name, value);
                }
            }
        }
        return output;
    }

    public String insideTag(String tag, String template) {
        String output = "";
        int i = template.indexOf("<" + tag);
        i = template.indexOf(">", i);
        int j = template.indexOf("</" + tag + ">", i);
        if (i>-1 && j>-1) {
            output = template.substring(i+1, j);
        }
        return output;
    }

}

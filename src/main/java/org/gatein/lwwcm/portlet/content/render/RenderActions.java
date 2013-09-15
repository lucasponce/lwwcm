package org.gatein.lwwcm.portlet.content.render;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Template;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/*
    Render Actions for ContentPortlet
 */
public class RenderActions {
    private static final Logger log = Logger.getLogger(RenderActions.class.getName());

    @Inject
    private WcmService wcm;

    /*
        Main render method
     */
    public String renderTemplate(RenderRequest request, RenderResponse response, UserWcm userWcm) throws PortletException, IOException {

        String contentTemplateId = request.getPreferences().getValue("contentTemplateId", null);

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        if (listContentAttached == null) {
            listContentAttached = request.getPreferences().getValue("listContentAttached", null);
            request.getPortletSession().setAttribute("listContentAttached", listContentAttached);
        }

        Template template = null;
        List<Object> contentAttached = null;
        String processedTemplate = null;
        String profile = "read";

        template = getTemplate(contentTemplateId, userWcm);
        contentAttached = getContentAttached(listContentAttached, userWcm);
        processedTemplate = processTemplate(template, contentAttached, userWcm);
        request.setAttribute("processedTemplate", processedTemplate);

        return profile;
    }

    /*
        Get Template attached with ContentPortlet
     */
    private Template getTemplate(String contentTemplateId, UserWcm userWcm) {
        Template template = null;
        if (contentTemplateId != null) {
            try {
                template = wcm.findTemplate(new Long(contentTemplateId), userWcm);
            } catch(WcmException e) {
                log.warning("Error query template id " + contentTemplateId);
                e.printStackTrace();
            }
        }
        return template;
    }

    /*
        Get list of content attached with ContentPortlet
     */
    private List<Object> getContentAttached(String listContentAttached, UserWcm userWcm) {
        List<Object> contentAttached = null;
        if (listContentAttached != null && !listContentAttached.equals("")) {
            try {
                String[] listIds = listContentAttached.split(",");
                contentAttached = new ArrayList<Object>();
                listIds = listContentAttached.split(",");
                for (String contentId : listIds) {
                    String type = contentId.split("_")[1];
                    String id = contentId.split("_")[0];
                    if (type.equals("C")) {
                        Category c = wcm.findCategory(new Long(id), userWcm);
                        contentAttached.add(c);
                    } else {
                        Post p = wcm.findPost(new Long(id), userWcm);
                        contentAttached.add(p);
                    }
                }
            } catch (WcmException e) {
                log.warning("Error query categories/posts list");
                e.printStackTrace();
            }
        }
        return contentAttached;
    }

    /*
        Main process method
     */
    private String processTemplate(Template template, List<Object> contentAttached, UserWcm userWcm) {
        String processedTemplate = null;
        if (template != null) {
            processedTemplate = template.getContent();
            boolean foundTag = false;
            int indexList = 0;
            int indexPost = 0;
            while (!foundTag) {
                if (hasTag("wcm-list", processedTemplate)) {
                    // Get Posts from category attached
                    List<Post> listPosts = getPostsFromCategory(contentAttached, indexList, userWcm);
                    processedTemplate = tagWcmList(processedTemplate, listPosts);
                    indexList++;
                } else if (hasTag("wcm-single", processedTemplate)) {
                    // Get Post attached
                    Post post = getPost(contentAttached, indexPost, userWcm);
                    processedTemplate = tagWcmSingle(processedTemplate, post);
                    indexPost++;
                } else {
                    foundTag = true;
                }
            }
        }
        return processedTemplate;
    }

    /*
        List<Object> contentAttached can store Category or Post objects.
        Category objects are linked with <wcm-list> tag in Template.
        Post objects are linked with <wcm-single> tag with Template.
        Template can have several <wcm-list> or <wcm-single> tags.
        indexCategory variable indicates order of the Category to retrieve.
     */
    private List<Post> getPostsFromCategory(List<Object> contentAttached, int indexCategory, UserWcm userWcm) {
        Category c = null;
        List<Post> listPosts = null;
        int localIndex = 0;
        for (Object o : contentAttached) {
            if (o instanceof Category) {
                if (localIndex == indexCategory) {
                    c = (Category)o;
                    break;
                }
                localIndex++;
            }
        }
        if (c != null) {
            try {
                listPosts = wcm.findPosts(c.getId(), Wcm.POSTS.PUBLISHED, userWcm);
            } catch (WcmException e) {
                log.warning("Error query posts list");
                e.printStackTrace();
            }
        }
        return listPosts;
    }

    /*
        List<Object> contentAttached can store Category or Post objects.
        Category objects are linked with <wcm-list> tag in Template.
        Post objects are linked with <wcm-single> tag with Template.
        Template can have several <wcm-list> or <wcm-single> tags.
        indexCategory variable indicates order of the Post to retrieve.
     */
    private Post getPost(List<Object> contentAttached, int indexPost, UserWcm userWcm) {
        Post post = null;
        int localIndex = 0;
        for (Object o : contentAttached) {
            if (o instanceof Post) {
                if (localIndex == indexPost) {
                    post = (Post)o;
                    if (!post.getPostStatus().equals(Wcm.POSTS.PUBLISHED)) post = null;
                }
                localIndex++;
            }
        }
        return post;
    }

    /*
        <wcm-list> tag processing
     */
    private String tagWcmList(String initialTemplate, List<Post> listPosts) {
        String processedTemplate = "";
        String tag = extractTag("wcm-list", initialTemplate);
        if (tag != null && listPosts != null) {
            String inside = insideTag("wcm-list", tag);
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
            processedTemplate = initialTemplate.replace(tag, "<div>No content attached to wcm-list</div>");
        }
        return processedTemplate;
    }

    /*
        <wcm-single> tag processing
     */
    private String tagWcmSingle(String initialTemplate, Post post) {
        String processedTemplate = null;
        String tag = extractTag("wcm-single", initialTemplate);
        if (tag != null) {
            String inside = insideTag("wcm-single", tag);
            String outputSingle = "<div>";
            outputSingle += combine(inside, post, 0);
            outputSingle += "</div>";
        }
        return processedTemplate;
    }

    /*
        Combine in-line tags with Post object
     */
    private String combine(String template, Post post, int iteration) {
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
    private String tagWcmLink(String template, Post post) {
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
    private String tagWcmImg(String template, Post post) {
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
    private String tagWcmTitle(String template, Post post) {
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
    private String tagWcmExcerpt(String template, Post post) {
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
    private String tagWcmIter(String template, int iteration) {
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
    private boolean hasTag(String tag, String template) {
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

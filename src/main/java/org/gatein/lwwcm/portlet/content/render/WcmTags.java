package org.gatein.lwwcm.portlet.content.render;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.portlet.util.ParseDates;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/*
    Wcm custom tags processing
 */
public class WcmTags {
    private static final Logger log = Logger.getLogger(WcmTags.class.getName());

    private Map<String, String> urlParams;

    /*
        <wcm-list> / <wcm-param-list> tag processing
     */
    public String tagWcmList(String tagWcmList, String initialTemplate, List<Post> listPosts, Map<String, String> params) {
        String processedTemplate = "";
        String tag = extractTag(tagWcmList, initialTemplate);
        this.urlParams = params;

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
                    outputList += combine(inside, p, i, false);
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
        <wcm-file-list> tag processing
     */
    public String tagWcmFileList(String tagWcmFileList, String initialTemplate, List<Upload> listUploads, Map<String, String> params) {
        String processedTemplate = "";
        String tag = extractTag(tagWcmFileList, initialTemplate);
        this.urlParams = params;

        if (tag != null && listUploads != null) {
            String inside = insideTag(tagWcmFileList, tag);
            Map<String, String> properties = propertiesTag(tag);
            int from = 0;
            int to = listUploads.size();
            if (properties.containsKey("from")) {
                String value = properties.get("from");
                if (value.equals("first")) {
                    from = 0;
                } else if (value.equals("last")) {
                    from = listUploads.size();
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
                    to = listUploads.size();
                } else {
                    try {
                        to = new Integer(value).intValue();
                        if (to > listUploads.size()) to = listUploads.size();
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
            if (listUploads != null) {
                for (int i = from; i < to; i++) {
                    Upload u = listUploads.get(i);
                    if (size > 1) outputList += "<li>";
                    outputList += combineUpload(inside, u, i);
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
        } else if (tag != null && listUploads == null) {
            processedTemplate = initialTemplate.replace(tag, "<div>No content found</div>");
        }
        return processedTemplate;
    }

    /*
        <wcm-single> / <wcm-param-single> tag processing
     */
    public String tagWcmSingle(String tagWcmSingle, String initialTemplate, Post post, Map<String, String> params, boolean canWrite) {
        String processedTemplate = null;
        String tag = extractTag(tagWcmSingle, initialTemplate);
        this.urlParams = params;

        if (tag != null) {
            String inside = insideTag(tagWcmSingle, tag);
            String outputSingle = combine(inside, post, 0, canWrite);
            /*
            Map<String, String> properties = propertiesTag(tag);
            String wcmClass = "";
            if (properties.containsKey("class")) {
                wcmClass += " class=\"" + properties.get("class") + "\"";
            }
            if (canWrite) {
                outputSingle = "<div " + wcmClass + " contenteditable=\"true\">" + outputSingle + "</div>";
            }
            */
            processedTemplate = initialTemplate.replace(tag, outputSingle);
        } else {
            processedTemplate = initialTemplate.replace(tag, "<div \" + wcmClass + \">No content found</div>");
        }
        return processedTemplate;
    }

    /*
        Combine in-line tags with Post object
     */
    public String combine(String template, Post post, int iteration, boolean canWrite) {
        if (post == null) return "";
        boolean foundTag = false;
        String output = template;
        while (!foundTag) {
            if (hasTag("wcm-link", output)) {
                output = tagWcmLink(output, post);
            } else if (hasTag("wcm-img", output)) {
                output = tagWcmImg(output, post, canWrite);
            } else if (hasTag("wcm-title", output)) {
                output = tagWcmTitle(output, post, canWrite);
            } else if (hasTag("wcm-excerpt", output)) {
                output = tagWcmExcerpt(output, post, canWrite);
            } else if (hasTag("wcm-iter", output)) {
                output = tagWcmIter(output, iteration);
            } else if (hasTag("wcm-created", output)) {
                output = tagWcmCreated(output, post);
            } else if (hasTag("wcm-author", output)) {
                output = tagWcmAuthor(output, post);
            } else if (hasTag("wcm-content", output)) {
                output = tagWcmContent(output, post, canWrite);
            } else {
                foundTag = true;
            }
        }
        return output;
    }

    /*
        Combine in-line tags with Upload object
     */
    public String combineUpload(String template, Upload upload, int iteration) {
        if (upload == null) return "";
        boolean foundTag = false;
        String output = template;
        while (!foundTag) {
            if (hasTag("wcm-link", output)) {
                output = tagWcmLink(output, upload);
            } else if (hasTag("wcm-filename", output)) {
                output = tagWcmFileName(output, upload);
            } else if (hasTag("wcm-iter", output)) {
                output = tagWcmIter(output, iteration);
            } else if (hasTag("wcm-created", output)) {
                output = tagWcmCreated(output, upload);
            } else if (hasTag("wcm-author", output)) {
                output = tagWcmAuthor(output, upload);
            } else if (hasTag("wcm-mimetype", output)) {
                output = tagWcmMimeType(output, upload);
            } else if (hasTag("wcm-description", output)) {
                output = tagWcmDescription(output, upload);
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

        String postUrl = Wcm.SUFFIX.POST + "/" + Wcm.SUFFIX.ID + "/" + post.getId();

        // By default wcm-link add a post id to href
        if (properties.containsKey("index") && properties.get("index").equals("disable")) {
            postUrl="";
        }
        String output = "<a";
        if (properties.containsKey("href")) {
            output += " href=\"" + properties.get("href") + (!"".equals(postUrl)?"/" + postUrl:"") + "\"";
        } else {
            String page = urlParams.get("page");
            if (urlParams.containsKey("post") || urlParams.containsKey("category")) {
                page = "../../../" + page;
            }
            output += " href=\"" + page + (!"".equals(postUrl)?"/" + postUrl:"") + "\"";
        }
        if (properties.containsKey("class")) {
            output += " class=\"" + properties.get("class") + "\"";
        }
        output += ">";
        output += inside;
        output += "</a>";
        return template.replace(tag, output);
    }

    public String tagWcmLink(String template, Upload upload) {
        String tag = extractTag("wcm-link", template);
        String inside = insideTag("wcm-link", template);
        Map<String, String> properties = propertiesTag(tag);

        String uploadUrl = "/lwwcm/rs/u" + upload.getId();

        String output = "<a";
        if (properties.containsKey("target")) {
            output += " target=\"" + properties.get("target") + "\"";
        }
        if (properties.containsKey("class")) {
            output += " class=\"" + properties.get("class") + "\"";
        }
        output += "href=\"" + uploadUrl + "\" ";
        output += ">";
        output += inside;
        output += "</a>";
        return template.replace(tag, output);
    }


    /*
        <wcm-img> tag processing
     */
    public String tagWcmImg(String template, Post post, boolean canWrite) {
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
        // output = output.replaceAll("style=\"[0-9a-zA-Z :;,-]*\"", "");
        // Editing tags
        if (canWrite) {
            output = "<p contenteditable=\"true\" class=\"lwwcm-content-edit\" data-post-id=\"" + post.getId() + "\" data-post-attr=\"image\">" + output + "</p>";
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-title> tag processing
     */
    public String tagWcmTitle(String template, Post post, boolean canWrite) {
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
        // Editing tags
        if (canWrite) {
            output = "<p contenteditable=\"true\" class=\"lwwcm-content-edit\" data-post-id=\"" + post.getId() + "\" data-post-attr=\"title\">" + output + "</p>";
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-excerpt> tag processing
     */
    public String tagWcmExcerpt(String template, Post post, boolean canWrite) {
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
        // Editing tags
        if (canWrite) {
            output = "<p contenteditable=\"true\" data-post-id=\"" + post.getId() + "\" data-post-attr=\"excerpt\">" + output + "</p>";
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
        <wcm-created> tag processing
     */
    public String tagWcmCreated(String template, Object object) {
        String tag = extractTag("wcm-created", template);
        String inside = insideTag("wcm-created", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("format")) {
            try {
                SimpleDateFormat custom = new SimpleDateFormat(properties.get("format"));
                if (object instanceof Post) {
                    output = custom.format(((Post)object).getCreated().getTime());
                } else if (object instanceof Upload) {
                    output = custom.format(((Upload)object).getCreated().getTime());
                }

            } catch (Exception e) {
                log.warning("Error parsing date with format " + properties.get("format"));
            }
            output = "";
        } else {
            if (object instanceof Post) {
                output = ParseDates.parse(((Post)object).getCreated());
            } else if (object instanceof Upload) {
                output = ParseDates.parse(((Upload)object).getCreated());
            }
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-author> tag processing
     */
    public String tagWcmAuthor(String template, Object object) {
        String tag = extractTag("wcm-author", template);
        String inside = insideTag("wcm-author", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (object instanceof Post) {
            output = ((Post)object).getAuthor();
        } else if (object instanceof Upload) {
            output = ((Upload)object).getUser();
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-content> tag processing
        skipImages is a list of indexes of images in the content.
        We can combine <wcm-content skipimages="0"> if we want to use <wcm-img index="0"> and I don't want to repeat the same image
     */
    public String tagWcmContent(String template, Post post, boolean canWrite) {
        String tag = extractTag("wcm-content", template);
        String inside = insideTag("wcm-content", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("skipimages")) {
            try {
                output = post.getContent();
                String[] skipImages = properties.get("skipimages").split(",");
                for (int i=0; i<skipImages.length; i++) {
                    int iImage = new Integer(skipImages[i]).intValue();
                    String image = extractImg(output, iImage);
                    String imageNotVisible = notVisibleImg(image);
                    output = output.replace(image, imageNotVisible);
                }
            } catch (Exception e) {
                log.warning("Error parsing content with skipImages " + properties.get("skipImages"));
            }
        } else {
            output = post.getContent();
        }
        // Editing tags
        if (canWrite) {
            output = "<div contenteditable=\"true\" data-post-id=\"" + post.getId() + "\" data-post-attr=\"content\">" + output + "</div>";
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-filename> tag processing
     */
    public String tagWcmFileName(String template, Upload upload) {
        String tag = extractTag("wcm-filename", template);
        String inside = insideTag("wcm-filename", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("max-length")) {
            int max = 100;
            try {
                max = new Integer(properties.get("max-length")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            output = upload.getFileName().substring(0, max);
        } else {
            output = upload.getFileName();
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-mimetype> tag processing
     */
    public String tagWcmMimeType(String template, Upload upload) {
        String tag = extractTag("wcm-mimetype", template);
        String inside = insideTag("wcm-mimetype", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("max-length")) {
            int max = 100;
            try {
                max = new Integer(properties.get("max-length")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            output = upload.getMimeType().substring(0, max);
        } else {
            output = upload.getMimeType();
        }
        return template.replace(tag, output);
    }

    /*
        <wcm-description> tag processing
     */
    public String tagWcmDescription(String template, Upload upload) {
        String tag = extractTag("wcm-description", template);
        String inside = insideTag("wcm-description", template);
        Map<String, String> properties = propertiesTag(tag);
        String output = "";
        if (properties.containsKey("max-length")) {
            int max = 100;
            try {
                max = new Integer(properties.get("max-length")).intValue();
            } catch (Exception e) {
                // Default value if exception happens
            }
            output = upload.getDescription().substring(0, max);
        } else {
            output = upload.getDescription();
        }
        return template.replace(tag, output);
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

    // Rules to replace content in inline editor
    // Covers specific cases as image extraction or cleaning whitespaces or newlines
    public String replace(String target, String oldData, String newData) {
        if (target == null || oldData == null || newData == null) return null;

        if (oldData.indexOf("class=\"lwwcm-skip\"") != -1) {
            oldData = oldData.replaceAll("class=\"lwwcm-skip\"", "");
        } else if (newData.indexOf("lwwcm-skip") != -1) {
            oldData = oldData.replaceAll("lwwcm-skip", "");
        }

        if (newData.indexOf("class=\"lwwcm-skip\"") != -1) {
            newData = newData.replaceAll("class=\"lwwcm-skip\"", "");
        } else if (newData.indexOf("lwwcm-skip") != -1) {
            newData = newData.replaceAll("lwwcm-skip", "");
        }

        target = target.replace(oldData, newData);

        return target;
    }

    public String replace(String newData) {
        if (newData == null) return null;

        if (newData.indexOf("class=\"lwwcm-skip\"") != -1) {
            newData = newData.replaceAll("class=\"lwwcm-skip\"", "");
        } else if (newData.indexOf("lwwcm-skip") != -1) {
            newData = newData.replaceAll("lwwcm-skip", "");
        }
        return newData;
    }

    public String notVisibleImg(String img) {
        if (img.indexOf("class") > -1) {
            img = img.replaceAll("class=[\"''](.*)[\"']", "class=\"$1 lwwcm-skip\"");
        } else {
            img = "<img class=\"lwwcm-skip\" " + img.trim().substring(4);
        }
        return img;
    }

}

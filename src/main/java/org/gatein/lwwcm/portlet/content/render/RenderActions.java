package org.gatein.lwwcm.portlet.content.render;

import org.gatein.api.PortalRequest;
import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.*;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.*;
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

    @Inject
    private WcmTags tags;

    private Map<String, String> urlParams;

    /*
        Main render method
     */
    public String renderTemplate(RenderRequest request, RenderResponse response, UserWcm userWcm) throws PortletException, IOException {

        this.urlParams = parseUrl();

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        if (listContentAttached == null) {
            listContentAttached = request.getPreferences().getValue("listContentAttached", null);
            request.getPortletSession().setAttribute("listContentAttached", listContentAttached);
        }

        String mainTemplateId = request.getPreferences().getValue("mainTemplateId", null);
        String postTemplateId = request.getPreferences().getValue("postTemplateId", null);
        String categoryTemplateId = request.getPreferences().getValue("categoryTemplateId", null);

        Template template = null;
        List<Object> contentAttached = null;
        String processedTemplate = null;
        String profile = "read";
        Post postParameter = null;
        Category catParameter = null;

        // Validation if post id or category id is valid, if not we forward to the main template of the portlet
        if (urlParams.containsKey("post") && postTemplateId != null && !"".equals(postTemplateId) && !"-1".equals(postTemplateId)) {
            postParameter = getPostParameter(urlParams, userWcm);
        } else if (urlParams.containsKey("category") && categoryTemplateId != null && !"".equals(categoryTemplateId) && !"-1".equals(categoryTemplateId))  {
            catParameter = getCategoryParameter(urlParams, userWcm);
        }

        if (postParameter != null) {
            // Can write postParameter
            boolean canWrite = false;
            if (!"anonymous".equals(userWcm.toString()) && userWcm.canWrite(postParameter)) {
                canWrite = true;
                profile = "editor";
                // Categories for editor
                List<Category> categories = null;
                try {
                    categories = wcm.findCategories(userWcm);
                } catch (WcmException e) {
                    log.warning("Error accessing categories.");
                    e.printStackTrace();
                }
                request.setAttribute("categories", categories);
            }
            // Template
            template = getTemplate(postTemplateId, userWcm);
            // Content used: content attached in portlet configuration + content defined in parameter
            contentAttached = getContentAttached(listContentAttached, userWcm);
            // Processing template with content
            processedTemplate = processTemplate(canWrite, template, postParameter, null, contentAttached, userWcm);
        } else if (catParameter != null) {
            // Template
            template = getTemplate(categoryTemplateId, userWcm);
            // Content used: content attached in portlet configuration + content defined in parameter
            contentAttached = getContentAttached(listContentAttached, userWcm);

            // Processing template with content
            processedTemplate = processTemplate(false, template, null, catParameter, contentAttached, userWcm);
        } else {
            // Processing main template
            if (mainTemplateId != null && !"".equals(mainTemplateId) && !"-1".equals(mainTemplateId)) {
                // Template
                template = getTemplate(mainTemplateId, userWcm);
                // Content used: content attached in portlet configuration + content defined in parameter
                contentAttached = getContentAttached(listContentAttached, userWcm);
                // Processing template with content
                processedTemplate = processTemplate(false, template, null, null, contentAttached, userWcm);
            }
        }

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

    private Post getPostParameter(Map<String, String> params, UserWcm userWcm) {
        Post postParameter = null;
        try {
            postParameter = wcm.findPost(new Long(params.get("id")), userWcm);
            if (postParameter != null && postParameter.getPostStatus() != null && !postParameter.getPostStatus().equals(Wcm.POSTS.PUBLISHED)) {
                // Only show published posts
                postParameter = null;
            }
        } catch(WcmException e) {
            log.warning("Error query post");
            e.printStackTrace();
        } catch(Exception e) {
            // Error parsing id
            log.warning("Error parsing id: " + params.get("id"));
        }
        return postParameter;
    }

    private Category getCategoryParameter(Map<String, String> params, UserWcm userWcm) {
        Category catParameter = null;
        try {
            catParameter = wcm.findCategory(new Long(params.get("id")), userWcm);
        } catch(WcmException e) {
            log.warning("Error query post");
            e.printStackTrace();
        } catch(Exception e) {
            // Error parsing id
            log.warning("Error parsing id: " + params.get("id"));
        }
        return catParameter;
    }

    /*
        Main process method
     */
    private String processTemplate(boolean canWrite, Template template, Post postParameter, Category catParameter, List<Object> contentAttached, UserWcm userWcm) {
        String processedTemplate = null;
        if (template != null) {
            processedTemplate = template.getContent();
            boolean foundTag = false;
            int indexList = 0;
            int indexPost = 0;
            while (!foundTag) {
                if (tags.hasTag("wcm-list", processedTemplate)) {
                    // Check explicit order for content in the template
                    String tag = tags.extractTag("wcm-list", processedTemplate);
                    Map<String, String> properties = tags.propertiesTag(tag);
                    int customIndex = -1;
                    if (properties.containsKey("index")) {
                        try {
                            customIndex = new Integer(properties.get("index")).intValue();
                        } catch (Exception e) {
                            // Default customIndex and error
                        }
                    }
                    // Get Posts attached
                    List<Post> listPosts = null;
                    if (customIndex != -1) {
                        listPosts = getPostsFromCategory(contentAttached, customIndex, userWcm);
                    } else {
                        // Default order
                        listPosts = getPostsFromCategory(contentAttached, indexList, userWcm);
                    }
                    processedTemplate = tags.tagWcmList("wcm-list", processedTemplate, listPosts, this.urlParams);
                    indexList++;
                } else if (tags.hasTag("wcm-single", processedTemplate)) {
                    // Check explicit order for content in the template
                    String tag = tags.extractTag("wcm-single", processedTemplate);
                    Map<String, String> properties = tags.propertiesTag(tag);
                    int customIndex = -1;
                    if (properties.containsKey("index")) {
                        try {
                            customIndex = new Integer(properties.get("index")).intValue();
                        } catch (Exception e) {
                            // Default customIndex and error
                        }
                    }
                    // Get Post attached
                    Post post = null;
                    if (customIndex != -1) {
                        post = getPost(contentAttached, customIndex, userWcm);
                    } else {
                        // Default order
                        post = getPost(contentAttached, indexPost, userWcm);
                    }
                    processedTemplate = tags.tagWcmSingle("wcm-single", processedTemplate, post, this.urlParams, false);
                    indexPost++;
                } else if (tags.hasTag("wcm-param-single", processedTemplate)) {
                    processedTemplate = tags.tagWcmSingle("wcm-param-single", processedTemplate, postParameter, this.urlParams, canWrite);
                } else if (tags.hasTag("wcm-param-list", processedTemplate)) {
                    List<Post> listPosts = getPostsFromCategory(catParameter, userWcm);
                    processedTemplate = tags.tagWcmList("wcm-param-list", processedTemplate, listPosts, this.urlParams);
                } else if (tags.hasTag("wcm-file-list", processedTemplate)) {
                    // Check explicit order for content in the template
                    String tag = tags.extractTag("wcm-file-list", processedTemplate);
                    Map<String, String> properties = tags.propertiesTag(tag);
                    int customIndex = -1;
                    if (properties.containsKey("index")) {
                        try {
                            customIndex = new Integer(properties.get("index")).intValue();
                        } catch (Exception e) {
                            // Default customIndex and error
                        }
                    }
                    // Get Posts attached
                    List<Upload> listUploads = null;
                    if (customIndex != -1) {
                        listUploads = getUploadsFromCategory(contentAttached, customIndex, userWcm);
                    } else {
                        // Default order
                        listUploads = getUploadsFromCategory(contentAttached, indexList, userWcm);
                    }
                    processedTemplate = tags.tagWcmFileList("wcm-file-list", processedTemplate, listUploads, this.urlParams);
                    indexList++;
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

    private List<Post> getPostsFromCategory(Category c, UserWcm userWcm)  {
        List<Post> listPosts = null;
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

    private List<Upload> getUploadsFromCategory(List<Object> contentAttached, int indexCategory, UserWcm userWcm) {
        Category c = null;
        List<Upload> listUploads = null;
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
                listUploads = wcm.findUploads(c.getId(), userWcm);
            } catch (WcmException e) {
                log.warning("Error query posts list");
                e.printStackTrace();
            }
        }
        return listUploads;
    }


    /*
        Reserved words for page names:
        Wcm.SUFFIX.*
     */
    private Map<String, String> parseUrl() {

        HashMap<String, String> out = new HashMap<String, String>();

        PortalRequest pRequest = PortalRequest.getInstance();
        String path = pRequest.getNodePath().toString();
        if (path != null) {
            int i = -1;
            int j = -1;
            if ((i = path.indexOf("/" + Wcm.SUFFIX.POST)) != -1) {
                String page = path.substring(0, i);
                out.put("page", page.substring(page.lastIndexOf('/') + 1));
                String params = path.substring(i + 1);
                if ((j = params.indexOf("/" + Wcm.SUFFIX.ID)) != -1) {
                    String id = params.substring(j + Wcm.SUFFIX.ID.length() + 2);
                    out.put("id", id);
                    out.put("post", "true");
                } else if ((j = params.indexOf("/" + Wcm.SUFFIX.NAME)) != -1) {
                    String name = params.substring(j + Wcm.SUFFIX.NAME.length() + 2);
                    out.put("name", name);
                    out.put("post", "true");
                } else {
                    // post suffix but without id or name
                }
            } else if ((i = path.indexOf("/" + Wcm.SUFFIX.CATEGORY)) != -1) {
                String page = path.substring(0, i);
                out.put("page", page.substring(page.lastIndexOf('/') + 1));
                String params = path.substring(i + 1);
                if ((j = params.indexOf("/" + Wcm.SUFFIX.ID)) != -1) {
                    String id = params.substring(j + Wcm.SUFFIX.ID.length() + 2);
                    out.put("id", id);
                    out.put("category", "true");
                } else if ((j = params.indexOf("/" + Wcm.SUFFIX.NAME)) != -1) {
                    String name = params.substring(j + Wcm.SUFFIX.NAME.length() + 2);
                    out.put("name", name);
                    out.put("category", "true");
                } else {
                    // post suffix but without id or name
                }
            } else {
                String page = path;
                out.put("page", page.substring(page.lastIndexOf('/') + 1));
            }
        }

        return out;
    }

    // For Content Editor events
    public String eventShowPostUploads(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String namespace = request.getParameter("namespace");
        String filterCategoryId = request.getParameter("filterCategoryId");
        String filterName = request.getParameter("filterName");
        try {
            List<Upload> uploads = null;
            if (filterCategoryId != null && !filterCategoryId.equals("") && !filterCategoryId.equals("-1")) {
                uploads = wcm.findUploads(new Long(filterCategoryId), userWcm);
            } else if (filterName != null && !filterName.equals("")) {
                uploads = wcm.findUploads(filterName, userWcm);
            } else {
                uploads = wcm.findUploads(userWcm);
            }
            request.setAttribute("uploads", uploads);
            request.setAttribute("namespace", namespace);
        } catch(WcmException e) {
            log.warning("Error accesing uploads.");
            e.printStackTrace();
        } catch(Exception e) {
            log.warning("Error parsing filterCategoryId: " + filterCategoryId);
            e.printStackTrace();
        }
        return "/jsp/content/render/contentUploads.jsp";
    }

    public void eventUpdateContent(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String postId = request.getParameter("postId");
        String contentType = request.getParameter("contentType");
        String original = request.getParameter("original");
        String newData = request.getParameter("newData");
        try {
            if (postId != null && !"".equals(postId) && contentType != null && original != null && newData != null) {
                Post post = wcm.findPost(new Long(postId), userWcm);
                if (post != null) {
                    if (contentType.equals("title")) {
                        if ("".equals(post.getTitle())) {
                            post.setTitle(newData);
                        } else {
                            String newTitle = "";
                            if ("".equals(original)) {
                                newTitle = post.getTitle() + newData;
                            } else {
                                newTitle = post.getTitle().replace(original, newData);
                            }
                            post.setTitle(newTitle);
                        }
                        wcm.update(post, userWcm);
                    } else if (contentType.equals("excerpt")) {
                        if ("".equals(post.getExcerpt())) {
                            post.setExcerpt(newData);
                        } else {
                            String newExcerpt = "";
                            if ("".equals(original)) {
                                newExcerpt = post.getExcerpt() + newData;
                            } else {
                                newExcerpt = post.getExcerpt().replace(original, newData);
                            }
                            post.setExcerpt(newExcerpt);
                        }
                        wcm.update(post, userWcm);
                    } else if (contentType.equals("image")) {
                        if ("".equals(post.getContent())) {
                            post.setContent(newData);
                        } else {
                            String newContent = "";
                            if ("".equals(original)) {
                                newContent = post.getContent() + newData;
                            } else {
                                newContent = tags.replace(post.getContent(), original, newData);
                            }
                            post.setContent(newContent);
                        }
                        wcm.update(post, userWcm);
                    } else if (contentType.equals("content")) {
                        if ("".equals(post.getContent())) {
                            post.setContent(newData);
                        } else {
                            String newContent = "";
                            if ("".equals(original)) {
                                newContent = post.getContent() + newData;
                            } else {
                                newContent = tags.replace(newData);
                            }
                            post.setContent(newContent);
                            wcm.update(post, userWcm);
                        }
                    } else {
                        // Nothing
                    }
                }
            }
        } catch(WcmException e) {
            log.warning("Error updating post.");
            e.printStackTrace();
        } catch(Exception e) {
            log.warning("Error updating post");
            e.printStackTrace();
        }
    }
}

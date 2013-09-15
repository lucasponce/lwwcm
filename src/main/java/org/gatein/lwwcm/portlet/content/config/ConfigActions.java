package org.gatein.lwwcm.portlet.content.config;

import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Template;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Actions for Config area of ContentPortlet
 */
public class ConfigActions {
    private static final Logger log = Logger.getLogger(ConfigActions.class.getName());

    @Inject
    private WcmService wcm;

    public String viewConfig(RenderRequest request, RenderResponse response, UserWcm userWcm) throws PortletException, IOException {
        String mainTemplateId = request.getParameter("mainTemplateId");
        if (mainTemplateId == null) {
            mainTemplateId = request.getPreferences().getValue("mainTemplateId", null);
            mainTemplateId = (mainTemplateId == null ? "-1" : mainTemplateId);
        }
        request.setAttribute("mainTemplateId", mainTemplateId);

        String postTemplateId = request.getParameter("postTemplateId");
        if (postTemplateId == null) {
            postTemplateId = request.getPreferences().getValue("postTemplateId", null);
            postTemplateId = (postTemplateId == null ? "-1" : postTemplateId);
        }
        request.setAttribute("postTemplateId", postTemplateId);

        String categoryTemplateId = request.getParameter("categoryTemplateId");
        if (categoryTemplateId == null) {
            categoryTemplateId = request.getPreferences().getValue("categoryTemplateId", null);
            categoryTemplateId = (categoryTemplateId == null ? "-1" : categoryTemplateId);
        }
        request.setAttribute("categoryTemplateId", categoryTemplateId);

        // Get templates list
        try {
            List<Template> templates = wcm.findTemplates(userWcm);
            request.setAttribute("templates", templates);
        } catch (WcmException e) {
            log.warning("Error query template list");
            e.printStackTrace();
        }

        try {
            String contentType = request.getParameter("contentType");
            if (contentType != null && contentType.equals("P")) {
                List<Post> chooseContent = wcm.findPosts(userWcm);
                request.setAttribute("chooseContent", chooseContent);
            } else {
                contentType= "C";
                List<Category> chooseContent = wcm.findCategories(userWcm);
                request.setAttribute("chooseContent", chooseContent);
            }
            request.setAttribute("contentType", contentType);
        } catch (WcmException e) {
            log.warning("Error query categories/posts list");
            e.printStackTrace();
        }

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        if (listContentAttached == null) {
            listContentAttached = request.getPreferences().getValue("listContentAttached", null);
            request.getPortletSession().setAttribute("listContentAttached", listContentAttached);
        }
        if (listContentAttached != null && !listContentAttached.equals("")) {
            try {
                String[] listIds = listContentAttached.split(",");
                List<Object> output = new ArrayList<Object>();
                listIds = listContentAttached.split(",");
                for (String contentId : listIds) {
                    String type = contentId.split("_")[1];
                    String id = contentId.split("_")[0];
                    if (type.equals("C")) {
                        Category c = wcm.findCategory(new Long(id), userWcm);
                        output.add(c);
                    } else {
                        Post p = wcm.findPost(new Long(id), userWcm);
                        output.add(p);
                    }
                }
                request.setAttribute("listAttachedContent", output);
            } catch (WcmException e) {
                log.warning("Error query categories/posts list");
                e.printStackTrace();
            }
        }

        return "/jsp/content/config/config.jsp";
    }

    public void actionSaveConfig(ActionRequest request, ActionResponse response, UserWcm userWcm) throws PortletException, IOException {
        String mainTemplateId = request.getParameter("mainTemplateId");
        String postTemplateId = request.getParameter("postTemplateId");
        String categoryTemplateId = request.getParameter("categoryTemplateId");

        if (mainTemplateId != null && !mainTemplateId.equals("")) {
            request.getPreferences().setValue("mainTemplateId", mainTemplateId);
        }
        if (postTemplateId != null && !postTemplateId.equals("")) {
            request.getPreferences().setValue("postTemplateId", postTemplateId);
        }
        if (categoryTemplateId != null && !categoryTemplateId.equals("")) {
            request.getPreferences().setValue("categoryTemplateId", categoryTemplateId);
        }

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        if (listContentAttached != null) {
            request.getPreferences().setValue("listContentAttached", listContentAttached);
        }
        request.getPreferences().store();

        response.setRenderParameter("mainTemplateId", mainTemplateId);
        response.setRenderParameter("postTemplateId", postTemplateId);
        response.setRenderParameter("categoryTemplateId", categoryTemplateId);
    }

    public String eventNewContentAttached(ResourceRequest request, ResourceResponse response, UserWcm userWcm) throws PortletException, IOException {
        String newTypeContent = request.getParameter("newTypeContent");
        String newContentId = request.getParameter("newContentId");

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        listContentAttached = (listContentAttached == null ? "" : listContentAttached);
        String[] listIds = listContentAttached.split(",");

        if (listContentAttached.length() == 0) {
            listContentAttached = newContentId + "_" + newTypeContent;
        } else {
            listContentAttached += "," + newContentId + "_" + newTypeContent;
        }
        request.getPortletSession().setAttribute("listContentAttached", listContentAttached);

        try {
            List<Object> output = new ArrayList<Object>();
            listIds = listContentAttached.split(",");
            for (String contentId : listIds) {
                String type = contentId.split("_")[1];
                String id = contentId.split("_")[0];
                if (type.equals("C")) {
                    Category c = wcm.findCategory(new Long(id), userWcm);
                    output.add(c);
                } else {
                    Post p = wcm.findPost(new Long(id), userWcm);
                    output.add(p);
                }
            }
            // This attribute stores objects for rendering in the config view
            request.setAttribute("listAttachedContent", output);
        } catch (WcmException e) {
            log.warning("Error query categories/posts list");
            e.printStackTrace();
        }
        return "/jsp/content/config/configContentAttached.jsp";
    }

    public String eventDeleteContentAttached(ResourceRequest request, ResourceResponse response, UserWcm userWcm) throws PortletException, IOException {
        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        String deleteContentId = request.getParameter("deleteContentId");
        if (listContentAttached != null) {
            String[] listIds = listContentAttached.split(",");
            String updateListContentAttached = "";
            boolean found = false;
            for (String contentId : listIds) {
                if (contentId.equals(deleteContentId)) {
                    found = true;
                } else {
                    updateListContentAttached += contentId + ",";
                }
            }
            if (updateListContentAttached.length() > 0)
                updateListContentAttached = updateListContentAttached.substring(0, updateListContentAttached.length() - 1);
            request.getPortletSession().setAttribute("listContentAttached", updateListContentAttached);
        }
        return null;
    }

    public String eventChangeChooseContent(ResourceRequest request, ResourceResponse response, UserWcm userWcm) throws PortletException, IOException {
        try {
            String newTypeContent = request.getParameter("newTypeContent");
            if (newTypeContent != null && newTypeContent.equals("P")) {
                List<Post> chooseContent = wcm.findPosts(userWcm);
                request.setAttribute("chooseContent", chooseContent);
                request.setAttribute("newTypeContent", newTypeContent);
            } else {
                newTypeContent= "C";
                List<Category> chooseContent = wcm.findCategories(userWcm);
                request.setAttribute("chooseContent", chooseContent);
                request.setAttribute("newTypeContent", newTypeContent);
            }
            request.setAttribute("newTypeContent", newTypeContent);
            return "/jsp/content/config/configContentList.jsp";
        } catch (WcmException e) {
            log.warning("Error query categories/posts list");
            e.printStackTrace();
        }
        return null;
    }

}

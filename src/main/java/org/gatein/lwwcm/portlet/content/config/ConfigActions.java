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
        String contentTemplateId = request.getParameter("contentTemplateId");
        if (contentTemplateId == null) {
            contentTemplateId = request.getPreferences().getValue("contentTemplateId", null);
            contentTemplateId = (contentTemplateId == null ? "-1" : contentTemplateId);
        }
        request.setAttribute("contentTemplateId", contentTemplateId);

        String listenId = request.getParameter("listenId");
        if (listenId == null) {
            listenId = request.getPreferences().getValue("listenId", null);
            listenId = (listenId == null ? "" : listenId);
        }
        request.setAttribute("listenId", listenId);

        String exportId = request.getParameter("exportId");
        if (exportId == null) {
            exportId = request.getPreferences().getValue("exportId", null);
            exportId = (exportId == null ? "" : exportId);
        }
        request.setAttribute("exportId", exportId);


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

        return "/jsp/content/config.jsp";
    }

    public void actionSaveConfig(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String saveTemplateId = request.getParameter("saveTemplateId");
        String saveListenId = request.getParameter("saveListenId");
        String saveExportId = request.getParameter("saveExportId");
        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");

        response.setRenderParameter("contentTemplateId", saveTemplateId);
        response.setRenderParameter("listenId", saveListenId);
        response.setRenderParameter("exportId", saveExportId);

        request.getPreferences().setValue("contentTemplateId", saveTemplateId);
        request.getPreferences().setValue("listenId", saveListenId);
        request.getPreferences().setValue("exportId", saveExportId);
        request.getPreferences().setValue("listContentAttached", listContentAttached);
        request.getPreferences().store();
    }

    public String eventNewContentAttached(ResourceRequest request, ResourceResponse response, UserWcm userWcm) throws PortletException, IOException {
        String newTypeContent = request.getParameter("newTypeContent");
        String newContentId = request.getParameter("newContentId");

        String listContentAttached = (String)request.getPortletSession().getAttribute("listContentAttached");
        listContentAttached = (listContentAttached == null ? "" : listContentAttached);
        String[] listIds = listContentAttached.split(",");
        boolean found = false;
        for (String contentId : listIds) {
            if (contentId.split("_")[0].equals(newContentId)) {
                found = true;
            }
        }
        if (!found) {
            if (listContentAttached.length() == 0) {
                listContentAttached = newContentId + "_" + newTypeContent;
            } else {
                listContentAttached += "," + newContentId + "_" + newTypeContent;
            }
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
            request.setAttribute("listAttachedContent", output);
        } catch (WcmException e) {
            log.warning("Error query categories/posts list");
            e.printStackTrace();
        }
        return "/jsp/content/configContentAttached.jsp";
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
            return "/jsp/content/configContentList.jsp";
        } catch (WcmException e) {
            log.warning("Error query categories/posts list");
            e.printStackTrace();
        }
        return null;
    }

}

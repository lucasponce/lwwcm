/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gatein.lwwcm.portlet;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.PortalService;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * GateIn WCM Editor Portlet.
 *
 * @author <a href="mailto:lponce@redhat.com">Lucas Ponce</a>
 */
public class EditorPortlet extends GenericPortlet {
    private static final Logger log = Logger.getLogger(EditorPortlet.class.getName());

    @Inject
    private PortalService portal;

    @Inject
    private WcmService wcm;

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        final String view = request.getParameter("view");
        final String prefix = "/jsp/";
        String url = null;

        // Check user
        if (request.getUserPrincipal() == null || request.getUserPrincipal().getName() == null) {
            url = prefix + "notaccess.jsp";
            request.setAttribute("userWcm", "anonymous");
        } else {
            try {
                String user = request.getUserPrincipal().getName();
                UserWcm userWcm = portal.getPortalUser(user);
                if (!userWcm.getGroups().contains(Wcm.GROUPS.EDITOR)) {
                    url = prefix + "notaccess.jsp";
                    request.setAttribute("userWcm", userWcm);
                }
            } catch (WcmException e) {
                log.warning("Cannot access Portal User interface.");
                e.printStackTrace();
            }
        }

        // UserWcm is validated, checking controller variable on MVC pattern
        if (url == null) {
            if (view == null) {
                url = prefix + "posts.jsp";
            }
            if (view != null && view.equals("posts"))  {
                url = prefix + "posts.jsp";
            }
            if (view != null && view.equals("newpost")) {
                url = prefix + "post.jsp";
            }
            if (view != null && view.equals("categories"))  {
                url = prefix + "categories.jsp";
            }
            if (view != null && view.equals("newcategory")) {
                url = prefix + "category.jsp";
            }
            if (view != null && view.equals("uploads"))  {
                url = prefix + "uploads.jsp";
            }
            if (view != null && view.equals("newupload")) {
                url = prefix + "upload.jsp";
            }
            if (view != null && view.equals("templates"))  {
                url = prefix + "templates.jsp";
            }
            if (view != null && view.equals("newtemplate")) {
                url = prefix + "template.jsp";
            }
            if (view != null && view.equals("editcategory")) {
                url = prefix + "categoryEdit.jsp";
            }
        }

        PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
        prd.include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        // Get user
        UserWcm userWcm = null;
        try {
            String user = (request.getUserPrincipal() != null?request.getUserPrincipal().getName():null);
            userWcm = portal.getPortalUser(user);
        } catch (WcmException e) {
            log.warning("Cannot access Portal User interface.");
            e.printStackTrace();
        }

        // Get parameters
        String action = request.getParameter("action");
        String view = request.getParameter("view");

        // Actions
        if (action != null) {

            // New category action
            if (action.equals("newCategory")) {
                // Get parameters
                String newCategoryName = request.getParameter("newCategoryName");
                String newCategoryType = request.getParameter("newCategoryType");
                String newCategoryParent = request.getParameter("newCategoryParent");

                Category newCategory = new Category(newCategoryName);
                if (newCategoryType.equals("Category")) {
                    newCategory.setType(Wcm.CATEGORIES.CATEGORY);
                }
                if (newCategoryType.equals("Tag")) {
                    newCategory.setType(Wcm.CATEGORIES.TAG);
                }
                if (newCategoryType.equals("Folder")) {
                    newCategory.setType(Wcm.CATEGORIES.FOLDER);
                    if (!newCategoryParent.equals("-1")) {
                        long parentId = -1;
                        try {
                            parentId = new Long(newCategoryParent).longValue();
                        } catch (Exception ignored) { }
                        if (parentId>-1) {
                            try {
                                Category parentCategory = wcm.findCategory(parentId, userWcm);
                                newCategory.setParent(parentCategory);
                            } catch (WcmException e) {
                                log.warning("Error getting parentCategory.");
                                e.printStackTrace();
                                response.setRenderParameter("errorWcm", "Error getting parentCategory " + e.toString());
                            }
                        }
                    }
                }
                try {
                    wcm.create(newCategory, userWcm);
                    view = "categories";
                } catch (Exception e) {
                    log.warning("Error creating new category");
                    e.printStackTrace();
                    response.setRenderParameter("errorWcm", "Error creating new category " + e.toString());
                }
            }

            // Delete category action
            if (action.equals("deleteCategory")) {
                String catId = request.getParameter("deletedCategoryId");
                try {
                    wcm.deleteCategory(new Long(catId), userWcm);
                    view = "categories";
                } catch (Exception e) {
                    log.warning("Error deleting category");
                    e.printStackTrace();
                    response.setRenderParameter("errorWcm", "Error deleting category " + e.toString());
                }
            }

            // Edit category action
            if (action.equals("editCategory")) {
                String catId = request.getParameter("editCategoryId");
                String newCategoryName = request.getParameter("newCategoryName");
                String newCategoryType = request.getParameter("newCategoryType");
                String newCategoryParent = request.getParameter("newCategoryParent");

                Category updateCategory = null;
                try {
                    updateCategory = wcm.findCategory(new Long(catId), userWcm);
                    updateCategory.setName(newCategoryName);
                    if (newCategoryType.equals("Category")) {
                        updateCategory.setType(Wcm.CATEGORIES.CATEGORY);
                        updateCategory.setParent(null);
                    }
                    if (newCategoryType.equals("Tag")) {
                        updateCategory.setType(Wcm.CATEGORIES.TAG);
                        updateCategory.setParent(null);
                    }
                    if (newCategoryType.equals("Folder")) {
                        updateCategory.setType(Wcm.CATEGORIES.FOLDER);
                        if (!newCategoryParent.equals("-1")) {
                            long parentId = -1;
                            try {
                                parentId = new Long(newCategoryParent).longValue();
                            } catch (Exception ignored) { }
                            if (parentId>-1 && parentId != new Long(catId)) {
                                try {
                                    Category parentCategory = wcm.findCategory(parentId, userWcm);
                                    updateCategory.setParent(parentCategory);
                                } catch (WcmException e) {
                                    log.warning("Error getting parentCategory.");
                                    e.printStackTrace();
                                    response.setRenderParameter("errorWcm", "Error getting parentCategory " + e.toString());
                                }
                            }
                        } else {
                            updateCategory.setParent(null);
                        }
                    }
                    wcm.update(updateCategory, userWcm);
                    view = "categories";
                } catch (Exception e) {
                    log.warning("Error updating category");
                    e.printStackTrace();
                    response.setRenderParameter("errorWcm", "Error updating category " + e.toString());
                }
            }

            if (action.equals("posts")) {
                String event = request.getParameter("event");
                String selected = request.getParameter("selected");
                String page = request.getParameter("page");
                log.info("Action postsActions with event: " + event + " selected: " + selected + " page: " + page);
            }

        }

        // Views
        if (view != null) {
            // Query list of categories
            if (view.equals("categories")) {
                List<Category> categories = null;
                try {
                    categories = wcm.findRootCategories(userWcm);
                    // "list" variable will be global and it will be used to store all list from action phase to render phase
                    // this way it can survive several invocation of third party portlets
                    request.getPortletSession().setAttribute("list", categories);
                } catch (WcmException cE) {
                    log.warning("Error accessing categories.");
                    cE.printStackTrace();
                    response.setRenderParameter("errorWcm", "Error accessing categories: " + cE.toString());
                }
            }

            // New category
            if (view.equals("newcategory")) {
                List<Category> categories = null;
                try {
                    categories = wcm.findCategories(Wcm.CATEGORIES.FOLDER, userWcm);
                } catch (WcmException e) {
                    log.warning("Error accessing categories.");
                    e.printStackTrace();
                }
                request.getPortletSession().setAttribute("categories", categories);
            }

            // Query edit category
            if (view.equals("editcategory")) {
                String editId = request.getParameter("editid");
                try {
                    Category category = wcm.findCategory(new Long(editId), userWcm);
                    List<Category> categories = wcm.findCategories(Wcm.CATEGORIES.FOLDER, userWcm);
                    request.getPortletSession().setAttribute("edit", category);
                    request.getPortletSession().setAttribute("categories", categories);
                } catch (WcmException cE) {
                    log.warning("Error accessing categories.");
                    cE.printStackTrace();
                    response.setRenderParameter("errorWcm", "Error accessing categories: " + cE.toString());
                }
            }

            response.setRenderParameter("view", view);
        }
    }

    /*
        It will be used to handle ajax calls
     */
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {

        // Get user
        UserWcm userWcm = null;
        try {
            String user = (request.getUserPrincipal() != null?request.getUserPrincipal().getName():null);
            userWcm = portal.getPortalUser(user);
        } catch (WcmException e) {
            log.warning("Cannot access Portal User interface.");
            e.printStackTrace();
        }

        String event = request.getParameter("event");

        // Show children categories
        if (event != null && event.equals("showCategoriesChildren")) {
            String parentid = request.getParameter("parentid");
            String namespace = request.getParameter("namespace");
            List<Category> categories = null;
            try {
                categories = wcm.findChildren(new Long(parentid));
            } catch (WcmException e) {
                log.warning("Error accessing categories.");
                e.printStackTrace();
            }
            request.setAttribute("categories", categories);
            request.setAttribute("namespace", namespace);
            request.setAttribute("parentid", parentid);
            String url = "/jsp/categoriesChildren.jsp";
            PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
            prd.include(request, response);
        }
    }
}

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
package org.gatein.lwwcm.portlet.editor;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.editor.views.CategoriesActions;
import org.gatein.lwwcm.portlet.editor.views.PostsActions;
import org.gatein.lwwcm.portlet.editor.views.TemplatesActions;
import org.gatein.lwwcm.portlet.editor.views.UploadsActions;
import org.gatein.lwwcm.services.PortalService;

import javax.inject.Inject;
import javax.portlet.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * GateIn WCM Editor Portlet.
 * Implements a specific MVC pattern for portlets.
 *
 * @author <a href="mailto:lponce@redhat.com">Lucas Ponce</a>
 */
public class EditorPortlet extends GenericPortlet {
    private static final Logger log = Logger.getLogger(EditorPortlet.class.getName());

    @Inject
    private PortalService portal;

    @Inject
    private UploadsActions uploads;

    @Inject
    private CategoriesActions categories;

    @Inject
    private TemplatesActions templates;

    @Inject
    private PostsActions posts;

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        final String view = request.getParameter("view");
        String url = null;

        UserWcm userWcm = null;
        // Check user
        if (request.getUserPrincipal() == null || request.getUserPrincipal().getName() == null) {
            url = "/jsp/notaccess.jsp";
            request.setAttribute("userWcm", "anonymous");
        } else {
            try {
                String user = request.getUserPrincipal().getName();
                userWcm = portal.getPortalUser(user);
                if (!userWcm.getGroups().contains(Wcm.GROUPS.EDITOR)) {
                    url = "/jsp/notaccess.jsp";
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
                // First access shows default post list
                if (request.getPortletSession().getAttribute("list") == null) {
                    posts.viewInitPosts(request, response, userWcm);
                }
                url = "/jsp/posts/posts.jsp";
            } else {
                if (view.equals(Wcm.VIEWS.POSTS))  {
                    url = "/jsp/posts/posts.jsp";
                } else if (view.equals(Wcm.VIEWS.NEW_POST)) {
                    url = "/jsp/posts/post.jsp";
                } else if (view.equals(Wcm.VIEWS.CATEGORIES))  {
                    url = "/jsp/categories/categories.jsp";
                } else if (view.equals(Wcm.VIEWS.NEW_CATEGORY)) {
                    url = "/jsp/categories/category.jsp";
                } else if (view.equals(Wcm.VIEWS.UPLOADS))  {
                    url = "/jsp/uploads/uploads.jsp";
                } else if (view.equals(Wcm.VIEWS.NEW_UPLOAD)) {
                    url = "/jsp/uploads/upload.jsp";
                } else if (view.equals(Wcm.VIEWS.TEMPLATES))  {
                    url = "/jsp/templates/templates.jsp";
                } else if (view.equals(Wcm.VIEWS.NEW_TEMPLATE)) {
                    url = "/jsp/templates/template.jsp";
                } else if (view.equals(Wcm.VIEWS.EDIT_CATEGORY)) {
                    url = "/jsp/categories/categoryEdit.jsp";
                } else if (view.equals(Wcm.VIEWS.EDIT_UPLOAD)) {
                    url = "/jsp/uploads/uploadEdit.jsp";
                } else if (view.equals(Wcm.VIEWS.EDIT_TEMPLATE)) {
                    url = "/jsp/templates/templateEdit.jsp";
                } else if (view.equals(Wcm.VIEWS.EDIT_POST)) {
                    url = "/jsp/posts/postEdit.jsp";
                } else {
                   // View parameter wrong. Default view.
                    url = "/jsp/posts/posts.jsp";
                }
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
            if (action.equals(Wcm.ACTIONS.NEW_CATEGORY)) {
                // New category action
                view = categories.actionNewCategory(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_CATEGORY)) {
                // Delete category action
                view = categories.actionDeleteCategory(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.EDIT_CATEGORY)) {
                // Edit category action
                view = categories.actionEditCategory(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.NEW_UPLOAD)) {
                // New upload action
                view = uploads.actionNewUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.RIGHT_UPLOADS)) {
                // Right page actions uploads
                view = uploads.actionRightUploads(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.LEFT_UPLOADS)) {
                // Left page actions uploads
                view = uploads.actionLeftUploads(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.EDIT_UPLOAD)) {
                // Edit upload action
                view = uploads.actionEditUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_CATEGORY_UPLOAD)) {
                // Add category upload action
                view = uploads.actionAddCategoryUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.FILTER_CATEGORY_UPLOADS)) {
                // Filter category uploads action
                view = uploads.actionFilterCategoryUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_UPLOAD)) {
                // Delete upload action
                view = uploads.actionDeleteUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_SELECTED_UPLOAD)) {
                // Delete selected upload action
                view = uploads.actionDeleteSelectedUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_SELECTED_CATEGORY_UPLOAD))  {
                // Add selected category upload action
                view = uploads.actionAddSelectedCategoryUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.REMOVE_CATEGORY_UPLOAD))  {
                // Remove category upload action
                view = uploads.actionRemoveCategoryUpload(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.NEW_TEMPLATE)) {
                // New template action
                view = templates.actionNewTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.RIGHT_TEMPLATES)) {
                // Right page actions templates
                view = templates.actionRightTemplates(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.LEFT_TEMPLATES)) {
                // Left page actions templates
                view = templates.actionLeftTemplates(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.EDIT_TEMPLATE)) {
                // Edit template action
                view = templates.actionEditTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_CATEGORY_TEMPLATE)) {
                // Add category template action
                view = templates.actionAddCategoryTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.FILTER_CATEGORY_TEMPLATES)) {
                // Filter category templates action
                view = templates.actionFilterCategoryTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_TEMPLATE)) {
                // Delete template action
                view = templates.actionDeleteTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_SELECTED_TEMPLATE)) {
                // Delete selected
                view = templates.actionDeleteSelectedTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_SELECTED_CATEGORY_TEMPLATE)) {
                // Add selected category template action
                view = templates.actionAddSelectedCategoryTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.REMOVE_CATEGORY_TEMPLATE)) {
                // Remove category template action
                view = templates.actionRemoveCategoryTemplate(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.NEW_POST)) {
                // New post action
                view = posts.actionNewPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.RIGHT_POSTS)) {
                // Right page actions posts
                view = posts.actionRightPosts(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.LEFT_POSTS)) {
                // Left page actions posts
                view = posts.actionLeftPosts(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.EDIT_POST)) {
                // Edit post action
                view = posts.actionEditPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_CATEGORY_POST)) {
                // Add category post action
                view = posts.actionAddCategoryPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.FILTER_CATEGORY_POSTS)) {
                // Filter category posts action
                view = posts.actionFilterCategoryPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_POST)) {
                // Delete post action
                view = posts.actionDeletePost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.DELETE_SELECTED_POST)) {
                // Delete selected post action
                view = posts.actionDeleteSelectedPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.ADD_SELECTED_CATEGORY_POST))  {
                // Add selected category post action
                view = posts.actionAddSelectedCategoryPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.REMOVE_CATEGORY_POST))  {
                // Remove category post action
                view = posts.actionRemoveCategoryPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.PUBLISH_POST))  {
                // Publish post action
                view = posts.actionPublishPost(request, response, userWcm);
            } else if (action.equals(Wcm.ACTIONS.PUBLISH_POSTS))  {
                // Publish post action
                view = posts.actionPublishPosts(request, response, userWcm);
            } else {
                // View parameter doesn't modified by actions
            }
        }

        // Views
        if (view != null) {
            if (view.equals(Wcm.VIEWS.CATEGORIES)) {
                // Query list of categories
                categories.viewCategories(request, response, userWcm);
            } else if (view.equals(Wcm.VIEWS.NEW_CATEGORY)) {
                // New category
                categories.viewNewCategory(request, response, userWcm);
            } else if (view.equals(Wcm.VIEWS.EDIT_CATEGORY)) {
                // Query edit category
                categories.viewEditCategory(request, response, userWcm);
            } else if (view.equals(Wcm.VIEWS.UPLOADS)) {
                // Query list of uploads
                uploads.viewUploads(request, response, userWcm);
            } else if (view.equals(Wcm.VIEWS.EDIT_UPLOAD)) {
                // Query edit upload
                uploads.viewEditUpload(request, response, userWcm);
            } if (view.equals(Wcm.VIEWS.TEMPLATES)) {
               // Query list of templates
                templates.viewTemplates(request, response, userWcm);
            } if (view.equals(Wcm.VIEWS.EDIT_TEMPLATE)) {
               // Query edit template
                templates.viewEditTemplate(request, response, userWcm);
            } if (view.equals(Wcm.VIEWS.POSTS)) {
                // Query list of posts
                posts.viewPosts(request, response, userWcm);
            } if (view.equals(Wcm.VIEWS.EDIT_POST)) {
                // Query edit post
                posts.viewEditPost(request, response, userWcm);
            } else {
                // No new action attached to view
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
        if (event != null) {
            String url = null;
            if (event.equals(Wcm.EVENTS.SHOW_CATEGORIES_CHILDREN)) {
                // Show children categories
                url = categories.eventShowCategoriesChildren(request, response, userWcm);
            } else if (event.equals(Wcm.EVENTS.DOWNLOAD_UPLOAD)) {
                // Show download attached of Upload object
                uploads.eventDownloadUpload(request, response, userWcm);
            } else {
                // No default view.
            }
            if (url != null) {
                PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
                prd.include(request, response);
            }
        }
    }
}

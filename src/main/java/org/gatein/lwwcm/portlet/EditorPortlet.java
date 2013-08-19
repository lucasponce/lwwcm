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
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.PortalService;

import javax.inject.Inject;
import javax.portlet.*;
import java.io.IOException;
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

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        final String view = request.getParameter("view");
        final String prefix = "/jsp/";
        String url = null;

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
                log.severe("Cannot access Portal User interface.");
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
        }

        PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
        prd.include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String view = request.getParameter("view");
        if (view != null) {
            response.setRenderParameter("view", view);
        }

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("postsActions")) {
                String event = request.getParameter("event");
                String selected = request.getParameter("selected");
                String page = request.getParameter("page");
                log.info("Action postsActions with event: " + event + " selected: " + selected + " page: " + page);
            }
        }
    }

    /*
        It will be used to handle ajax calls
     */
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        String user = request.getUserPrincipal().getName();
        String event = request.getParameter("event");
        if (event != null && event.equals("showCategories")) {
            log.info("Showing categories to choose for user " + user);
            String url = "/jsp/categoriesDialog.jsp";
            PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
            prd.include(request, response);
        }
    }
}

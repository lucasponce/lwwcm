package org.gatein.lwwcm.portlet.content;

import org.gatein.api.PortalRequest;
import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Template;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.content.config.ConfigActions;
import org.gatein.lwwcm.portlet.content.render.RenderActions;
import org.gatein.lwwcm.services.PortalService;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * GateIn WCM Content Portlet.
 * Show content rendered using templates
 *
 * @author <a href="mailto:lponce@redhat.com">Lucas Ponce</a>
 */
public class ContentPortlet extends GenericPortlet {
    private static final Logger log = Logger.getLogger(ContentPortlet.class.getName());

    @Inject
    private PortalService portal;

    @Inject
    private ConfigActions config;

    @Inject
    private RenderActions render;

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        UserWcm userWcm = null;
        try {
            String user = (request.getUserPrincipal() != null?request.getUserPrincipal().getName():null);
            userWcm = portal.getPortalUser(user);
            if (userWcm == null) {
                userWcm = new UserWcm("anonymous");
            }
        } catch (WcmException e) {
            log.warning("Cannot access Portal User interface.");
            e.printStackTrace();
        }
        request.setAttribute("userWcm", userWcm);

        String profile = render.renderTemplate(request, response, userWcm);

        String url = "/jsp/content/render/content.jsp";

        if (profile != null && profile.equals("editor")) {
            url = "/jsp/content/render/contentEdit.jsp";
        }

        PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
        prd.include(request, response);
    }

    @Override
    protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        UserWcm userWcm = null;
        try {
            String user = (request.getUserPrincipal() != null?request.getUserPrincipal().getName():null);
            userWcm = portal.getPortalUser(user);
        } catch (WcmException e) {
            log.warning("Cannot access Portal User interface.");
            e.printStackTrace();
        }

        String url = config.viewConfig(request, response, userWcm);

        PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
        prd.include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        UserWcm userWcm = null;
        try {
            String user = (request.getUserPrincipal() != null?request.getUserPrincipal().getName():null);
            userWcm = portal.getPortalUser(user);
        } catch (WcmException e) {
            log.warning("Cannot access Portal User interface.");
            e.printStackTrace();
        }

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals(Wcm.CONFIG.ACTIONS.SAVE_CONFIGURATION)) {
                config.actionSaveConfig(request, response, userWcm);
            } else {
                // No default action
            }
        }
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
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
            if (event.equals(Wcm.CONFIG.EVENTS.CHANGE_CHOOSE_CONTENT)) {
                url = config.eventChangeChooseContent(request, response, userWcm);
            } else if (event.equals(Wcm.CONFIG.EVENTS.NEW_CONTENT_ATTACHED)) {
                url = config.eventNewContentAttached(request, response, userWcm);
            } else if (event.equals(Wcm.CONFIG.EVENTS.DELETE_CONTENT_ATTACHED)) {
                url = config.eventDeleteContentAttached(request, response, userWcm);
            } if (event.equals(Wcm.EVENTS.SHOW_POST_UPLOADS)) {
                // Show list uploads
                url = render.eventShowPostUploads(request, response, userWcm);
            } if (event.equals(Wcm.EVENTS.UPDATE_CONTENT_POST)) {
                // Updating content
                render.eventUpdateContent(request, response, userWcm);
            } else {
                // No default event
            }
            if (url != null) {
                PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher(url);
                prd.include(request, response);
            }
        }
    }
}

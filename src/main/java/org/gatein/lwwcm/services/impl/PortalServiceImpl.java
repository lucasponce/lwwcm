package org.gatein.lwwcm.services.impl;

import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.PortalService;

import javax.enterprise.context.RequestScoped;
import java.util.Collection;
import java.util.logging.Logger;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Group;

@RequestScoped
public class PortalServiceImpl implements PortalService {

    private static final Logger log = Logger.getLogger(PortalServiceImpl.class.getName());

    @Override
    public UserWcm getPortalUser(String user) throws WcmException {
        if (user == null) return null;
        OrganizationService os = (OrganizationService)PortalContainer
                .getInstance()
                .getComponentInstanceOfType(OrganizationService.class);
        if (os == null) {
            throw new WcmException("Cannot retrieve OrganizationService from PortalContainer.");
        }
        UserWcm userWcm = new UserWcm(user);
        try {
            Collection groups = os.getGroupHandler().findGroupsOfUser(user);
            for (Object o : groups) {
                Group g = (Group)o;
                userWcm.add(g.getId());
            }
        } catch (Exception e) {
            throw new WcmException(e);
        }
        return userWcm;
    }
}

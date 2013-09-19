package org.gatein.lwwcm.services.impl;

import org.exoplatform.services.organization.Membership;
import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.PortalService;

import javax.enterprise.context.RequestScoped;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
                // We only return wcm groups
                if (g.getId().startsWith(Wcm.GROUPS.WCM)) {
                    userWcm.add(g.getId());
                    if (g.getId().equals(Wcm.GROUPS.WCM)) {
                        try {
                            Membership m = os.getMembershipHandler().findMembershipByUserGroupAndType(user, g.getId(), Wcm.GROUPS.MANAGER);
                            if (m != null) userWcm.setManager(true);
                        } catch (Exception e) {
                            log.warning("Error querying user");
                            e.printStackTrace();
                        }
                    }
                }
            }
            // If user has not valid group we asign a default group
            if (userWcm.getWriteGroups().isEmpty()) {
                userWcm.add(Wcm.GROUPS.LOST);
                log.warning("User: " + userWcm + " has not a valid writable group. Asigning to default group: " + Wcm.GROUPS.LOST);
            }
        } catch (Exception e) {
            throw new WcmException(e);
        }
        return userWcm;
    }

    @Override
    public Set<String> getWcmGroups() throws WcmException {
        Set<String> groups = new HashSet<String>();
        try {
            OrganizationService os = (OrganizationService)PortalContainer
                    .getInstance()
                    .getComponentInstanceOfType(OrganizationService.class);

            Group wcmGroup = os.getGroupHandler().findGroupById(Wcm.GROUPS.WCM);
            Collection children = os.getGroupHandler().findGroups(wcmGroup);
            Iterator iChildren = children.iterator();
            while (iChildren.hasNext()) {
                Group g = (Group)iChildren.next();
                groups.add(g.getId());
            }
        } catch (Exception e) {
            throw new WcmException(e);
        }
        return groups;
    }
}

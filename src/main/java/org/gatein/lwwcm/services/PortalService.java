package org.gatein.lwwcm.services;

import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.UserWcm;

import java.util.Set;

public interface PortalService {

    UserWcm getPortalUser(String user) throws WcmException;
    Set<String> getWcmGroups() throws WcmException;
}

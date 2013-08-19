package org.gatein.lwwcm.services;

import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.UserWcm;

public interface PortalService {

    UserWcm getPortalUser(String user) throws WcmException;
}

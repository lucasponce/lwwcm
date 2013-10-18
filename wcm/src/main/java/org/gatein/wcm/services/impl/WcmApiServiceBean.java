/*
 * JBoss, a division of Red Hat
 * Copyright 2010, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.wcm.services.impl;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.gatein.wcm.domain.UserWcm;
import org.gatein.wcm.services.WcmService;
import org.gatein.wcm.api.domain.Post;
import org.gatein.wcm.api.services.WcmApiService;

@Stateless
public class WcmApiServiceBean implements WcmApiService {

    private static final Logger log = Logger.getLogger(WcmApiServiceBean.class.getName());

    @Inject
    WcmService wcm;

    @Override
    public Post testPost() {
        UserWcm userWcm = new UserWcm("anonymous");
        Post p = null;
        return p;
    }
}

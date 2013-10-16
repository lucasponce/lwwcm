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

package org.gatein.lwwcm.portlet.editor.views;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.WcmLockException;
import org.gatein.lwwcm.domain.Acl;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Lock;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Relationship;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.util.ViewMetadata;
import org.gatein.lwwcm.services.PortalService;
import org.gatein.lwwcm.services.WcmService;

/**
 * Actions for Manager area of EditorPortlet
 *
 * @see org.gatein.lwwcm.Wcm.VIEWS
 * @see org.gatein.lwwcm.Wcm.ACTIONS
 *
 * @author <a href="mailto:lponce@redhat.com">Lucas Ponce</a>
 */
public class ManagerActions {
    private static final Logger log = Logger.getLogger(ManagerActions.class.getName());

    @Inject
    private WcmService wcm;

    @Inject
    private PortalService portal;

    public String eventShowLocks(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String namespace = request.getParameter("namespace");
        try {
            // List of locks
            List<Lock> locks = wcm.findLocks(userWcm);
            Map<Long, Object> locksObjects = wcm.findLocksObjects(locks, userWcm);
            request.setAttribute("locks", locks);
            request.setAttribute("locksObjects", locksObjects);
            request.setAttribute("namespace", namespace);
        } catch (Exception e) {
            log.warning("Error locks");
            e.printStackTrace();
        }
        return "/jsp/manager/managerLocks.jsp";
    }

    public String eventRemoveLock(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String namespace = request.getParameter("namespace");
        String lockid = request.getParameter("lockid");
        String locktype = request.getParameter("locktype");
        try {
            // Remove lock
            wcm.removeLock(new Long(lockid), new Character(locktype.charAt(0)), userWcm);
            // List of locks
            List<Lock> locks = wcm.findLocks(userWcm);
            Map<Long, Object> locksObjects = wcm.findLocksObjects(locks, userWcm);
            request.setAttribute("locks", locks);
            request.setAttribute("locksObjects", locksObjects);
            request.setAttribute("namespace", namespace);
        } catch (Exception e) {
            log.warning("Error locks");
            e.printStackTrace();
        }
        return "/jsp/manager/managerLocks.jsp";
    }
}

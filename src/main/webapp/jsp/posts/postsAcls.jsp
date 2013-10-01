<%
    /*
     * JBoss, a division of Red Hat
     * Copyright 2012, Red Hat Middleware, LLC, and individual
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
%>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="static org.gatein.lwwcm.Wcm.*" %>
<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ page import="org.gatein.lwwcm.domain.Acl" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@include file="../urls.jsp"%>
<portlet:resourceURL var="removeAclPostEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.REMOVE_ACL_POST %>" />
</portlet:resourceURL>
<%
    String n = (String)request.getAttribute("namespace");
%>
ACLs:
<ul>
<%
    Post post = (Post)request.getAttribute("post");
    if (post != null) {
        Set<Acl> acls = post.getAcls();
        for (Acl acl : acls) {
%>
    <li id="${n}acl<%= acl.getId() %>">
        <span class="glyphicon glyphicon-pencil margin-right"></span> <%= ViewMetadata.aclType(acl.getPermission()) %>
        <span class="glyphicon glyphicon-user margin-right margin-left-cat"></span> <%= acl.getPrincipal() %>
        <a href="#" onclick="deleteAcl('<%= n %>', '${removeAclPostEvent}', '<%= acl.getId() %>', '<%= post.getId() %>')" title="Delete ACL"><span class="glyphicon glyphicon-remove lwwcm-acl-remove"></span></a>
    </li>
<%
        }
    }
%>
</ul>
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
<%@ page import="org.gatein.lwwcm.Wcm" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects />
<portlet:resourceURL var="addCommentPost">
    <portlet:param name="event" value="<%= Wcm.EVENTS.ADD_COMMENT_POST %>" />
</portlet:resourceURL>
<%
    Long editid = (Long)request.getAttribute("editid");
%>
<portlet:actionURL var="editorView">
    <portlet:param name="action" value="<%= Wcm.CONTENT.ACTIONS.INLINE_EDITOR%>" />
    <portlet:param name="activeEditor" value="true" />
    <portlet:param name="editid" value="<%= editid.toString() %>" />
</portlet:actionURL>
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/lwwcm-content.css") %>" />
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/bootstrap-glyphicons.css") %>" />
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/content/render/content.js") %>"></script>
<% String n = renderResponse.getNamespace(); %>
<input id="<%=n%>-addCommentPost" type="hidden" value="<%= addCommentPost %>" />
<%
    String processedTemplate =  (String)renderRequest.getAttribute("processedTemplate");
    if (processedTemplate != null) {
%>
<div class="lwwcm-write">
    <%
        boolean locked = (renderRequest.getPortletSession().getAttribute("lockMsg") != null);
        String icon = "pencil";
        if (locked) {
            renderRequest.getPortletSession().setAttribute("lockMsg", null);
            icon = "lock";
        }
    %>
    <div class="lwwcm-inline-editor"><a href="<%= editorView %>"><span class="glyphicon glyphicon-<%= icon %>"></span></a></div>
    <%= processedTemplate %>
</div>
<%
    }
%>

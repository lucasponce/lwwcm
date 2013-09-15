<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="static org.gatein.lwwcm.Wcm.*" %>
<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@include file="../urls.jsp"%>

<%
    String n = (String)request.getAttribute("namespace");
%>
<ul>
<%
    List<Upload> uploads = (List<Upload>)request.getAttribute("uploads");
    if (uploads != null) {
        for (Upload u : uploads) {
%>
    <portlet:resourceURL var="downloadUploadEvent">
        <portlet:param name="event" value="<%= Wcm.EVENTS.DOWNLOAD_UPLOAD %>" />
        <portlet:param name="uploadid" value="<%= u.getId().toString() %>" />
    </portlet:resourceURL>
    <li data-id="<%= u.getId() %>" data-mimetype="<%= (u.getMimeType() != null ? u.getMimeType():"") %>" data-filename="<%= u.getFileName() %>">
        <%= u.getFileName() %>
        <span class="lwwcm-blue"><%=(u.getDescription()!=null && !u.getDescription().equals("")?"(" + u.getDescription() + ")":"")%></span>
        <a href="#" id="${n}upload<%= u.getId() %>" <% if (u.getMimeType() != null && u.getMimeType().startsWith("image")) { %>onmouseover="showPreview('<%= n %>', this.id, '${downloadUploadEvent}');" onmouseout="hidePreview('<%= n %>', this.id, '${downloadUploadEvent}');" <% } %>><span class="lwwcm-upload-mimetype"><%= u.getMimeType() %></span></a> <% if (u.getMimeType() != null && u.getMimeType().startsWith("image")) { %><span class="lwwcm-upload-mimetype glyphicon glyphicon-eye-open margin-top"></span><% } %>
    </li>
<%
        }
    }
%>
</ul>
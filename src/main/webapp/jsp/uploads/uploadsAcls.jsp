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
<portlet:resourceURL var="removeAclUploadEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.REMOVE_ACL_UPLOAD %>" />
</portlet:resourceURL>
<%
    String n = (String)request.getAttribute("namespace");
%>
ACLs:
<ul>
<%
    Upload upload = (Upload)request.getAttribute("upload");
    if (upload != null) {
        Set<Acl> acls = upload.getAcls();
        for (Acl acl : acls) {
%>
    <li id="${n}acl<%= acl.getId() %>">
        <span class="glyphicon glyphicon-pencil margin-right"></span> <%= ViewMetadata.aclType(acl.getPermission()) %>
        <span class="glyphicon glyphicon-user margin-right margin-left-cat"></span> <%= acl.getPrincipal() %>
        <a href="#" onclick="deleteAcl('<%= n %>', '${removeAclUploadEvent}', '<%= acl.getId() %>', '<%= upload.getId() %>')" title="Delete ACL"><span class="glyphicon glyphicon-remove lwwcm-acl-remove"></span></a>
    </li>
<%
        }
    }
%>
</ul>
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
<portlet:resourceURL var="removeAclCategoryEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.REMOVE_ACL_CATEGORY %>" />
</portlet:resourceURL>
<%
    String n = (String)request.getAttribute("namespace");
%>
ACLs:
<ul>
<%
    Category category = (Category)request.getAttribute("category");
    if (category != null) {
        Set<Acl> acls = category.getAcls();
        for (Acl acl : acls) {
%>
    <li id="${n}acl<%= acl.getId() %>">
        <span class="glyphicon glyphicon-pencil margin-right"></span> <%= ViewMetadata.aclType(acl.getPermission()) %>
        <span class="glyphicon glyphicon-user margin-right margin-left-cat"></span> <%= acl.getPrincipal() %>
        <a href="#" onclick="deleteAcl('<%= n %>', '${removeAclCategoryEvent}', '<%= acl.getId() %>', '<%= category.getId() %>')" title="Delete ACL"><span class="glyphicon glyphicon-remove lwwcm-acl-remove"></span></a>
    </li>
<%
        }
    }
%>
</ul>
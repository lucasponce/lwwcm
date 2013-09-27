<%@ page import="org.gatein.lwwcm.Wcm" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects />
<portlet:resourceURL var="addCommentPost">
    <portlet:param name="event" value="<%= Wcm.EVENTS.ADD_COMMENT_POST %>" />
</portlet:resourceURL>
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/lwwcm-content.css") %>" />
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/content/render/content.js") %>"></script>
<% String n = renderResponse.getNamespace(); %>
<input id="<%=n%>-addCommentPost" type="hidden" value="<%= addCommentPost %>" />
<%
    String processedTemplate =  (String)renderRequest.getAttribute("processedTemplate");
    if (processedTemplate != null) {
%>
<%= processedTemplate %>
<%
    }
%>

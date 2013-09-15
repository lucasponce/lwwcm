<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects />
<div>
    User: <%= renderRequest.getAttribute("userWcm") %>
    Main content: <%= portletPreferences.getValue("contentTemplateId", "NO DEFINED") %>
    Content: <%= portletPreferences.getValue("listContentAttached", "NO DEFINED") %>
</div>
<%
    String processedTemplate =  (String)renderRequest.getAttribute("processedTemplate");
    if (processedTemplate != null) {
%>
<div><%= processedTemplate %></div>
<%
} else {
%>
<div>No template defined in this content</div>
<%
    }
%>
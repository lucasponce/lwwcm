<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects />
<%
    String processedTemplate =  (String)renderRequest.getAttribute("processedTemplate");
    if (processedTemplate != null) {
%>
<%= processedTemplate %>
<%
    }
%>

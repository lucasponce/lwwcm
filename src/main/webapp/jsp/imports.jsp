<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="org.gatein.lwwcm.Wcm" %>
<portlet:defineObjects />
<%
    /*
        Loading CSS resources
     */
%>
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/lwwcm.css") %>" />
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/bootstrap-glyphicons.css") %>" />

<%
    /*
        JS events for imports.jsp page
     */
%>
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/imports.js") %>"></script>
<%
    /*
        Global variable for namespace in all jsp objects
     */
%>
<c:set var="n"><portlet:namespace/></c:set>

<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page trimDirectiveWhitespaces="true"%>

<%
    /*
        Global variable for resource bundle
     */
%>
<c:set var="rsc" value="${portletConfig.getResourceBundle(renderRequest.locale)}" />
<%
    /*
        UserWcm of request
     */
    Object o = renderRequest.getAttribute("userWcm");
    UserWcm userWcm = null;
    boolean isManager = false;
    if (o instanceof UserWcm) {
        userWcm = (UserWcm)o;
        isManager = userWcm.isManager();
    }
%>
<%
    /*
        Global layer for modal window containing warning messages
     */
%>
<div id="${n}msg-dialog" class="lwwcm-modal-window lwwcm-dialog">
    <div class="lwwcm-dialog-title">Modal window</div>
    <a href="#" id="${n}close-msg-dialog" class="lwwcm-dialog-close"><span> </span></a>
    <div id="${n}msg-dialog-body" class="lwwcm-dialog-body"></div>
</div>
<div id="${n}msg-mask" class="lwwcm-modal-mask"></div>

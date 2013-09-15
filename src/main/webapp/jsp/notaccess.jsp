<%@ page import="org.gatein.lwwcm.domain.UserWcm" %>
<%@include file="imports.jsp"%>
<div class="lwwcm-notaccess">
<%
    if (renderRequest.getAttribute("userWcm").equals("anonymous")) {
%>
        <div class="lwwcm-notaccess inner">
            <h2><span class="glyphicon glyphicon-warning-sign lwwcm-grey icon-big-size"></span> Anonymous user can not access into GateIn LightWeight WCM</h2>
            <p>Please, login into GateIn.</p>
        </div>
<%
    } else {
        UserWcm userWcm = (UserWcm)renderRequest.getAttribute("userWcm");
%>
        <div class="lwwcm-notaccess inner">
            <h2><span class="glyphicon glyphicon-warning-sign lwwcm-grey icon-big-size"></span> User <span class="lwwcm-red"><%= userWcm.getUsername() %></span> has not rights to access GateIn LightWeight WCM</h2>
            <p>Please, contact with WCM administrator.</p>
        </div>
<%
    }
%>
</div>
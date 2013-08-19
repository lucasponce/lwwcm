<%@ page import="org.gatein.lwwcm.domain.UserWcm" %>
<%@include file="imports.jsp"%>
<%
    if (renderRequest.getAttribute("userWcm").equals("anonymous")) {
%>
<div class="lwwcm-header lwwcm-black center">
    <h2>User anonymous can not access to GateIn LW WCM application. Please, login into GateIn.</h2>
</div>
<%
    } else {
        UserWcm userWcm = (UserWcm)renderRequest.getAttribute("userWcm");
%>
<div class="lwwcm-header lwwcm-black center">
    <h2>User <span class="lwwcm-red"><%= userWcm.getUsername() %></span> has not rights to acess GateIn LW WCM. Please contact with WCM administrator.</h2>
</div>
<%
    }
%>
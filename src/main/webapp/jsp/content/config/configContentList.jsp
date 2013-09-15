<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects />
<%
    String newTypeContent = (String)resourceRequest.getAttribute("newTypeContent");
    if (newTypeContent != null && newTypeContent.equals("C")) {
        List<Category> list = (List<Category>)resourceRequest.getAttribute("chooseContent");
        for (Category c : list) {
%>
<option value="<%= c.getId() %>"><%= c.getName() %></option>
<%
        }
    }
    if (newTypeContent != null && newTypeContent.equals("P")) {
        List<Post> list = (List<Post>)resourceRequest.getAttribute("chooseContent");
        for (Post p : list) {
%>
<option value="<%= p.getId() %>"><%= p.getTitle() %></option>
<%
        }
    }
%>
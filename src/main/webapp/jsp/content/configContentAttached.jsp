<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<portlet:defineObjects />
<c:set var="n"><portlet:namespace/></c:set>
<ul>
    <%
        List<Object> listAttachedContent = (List<Object>)resourceRequest.getAttribute("listAttachedContent");
        if (listAttachedContent != null) {
            for (Object o : listAttachedContent) {
                if (o instanceof Category) {
                    Category c = (Category)o;
        %>
        <li id="${n}attached<%= c.getId()%>_C"><span class="glyphicon glyphicon-tags margin-right"></span> <%= c.getName() %> <a href="#" onclick="deleteAttachment${n}('<%= c.getId()%>_C');"><span class="glyphicon glyphicon-remove middle"></span></a></li>
        <%
                } else {
                    Post p = (Post)o;
        %>
        <li id="${n}attached<%= p.getId()%>_P"><span class="glyphicon glyphicon-file margin-right"></span> <%= p.getTitle() %> <a href="#" onclick="deleteAttachment${n}('<%= p.getId()%>_P');"><span class="glyphicon glyphicon-remove middle"></span></a></li>
        <%
                }
            }
        }
    %>
</ul>
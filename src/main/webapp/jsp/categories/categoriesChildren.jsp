<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="static org.gatein.lwwcm.Wcm.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@include file="../urls.jsp"%>

<%
    String namespace = (String)request.getAttribute("namespace");
    String parentid = (String)request.getAttribute("parentid");
    String showchildrenhref = (String)request.getAttribute("showchildrenhref");
%>
<ul id="<%= namespace %>listChildrenId<%= parentid %>" class="lwwcm-categories">
<%
    List<Category> list = (List<Category>)request.getAttribute("categories");
    if (list != null) {
        for (Category c : list) {
            String typeIcon = "bookmark";
            String type = "Category";
            String color = "green";
            if (c.getType() == CATEGORIES.FOLDER) {
                typeIcon = "folder-open";
                type = "Folder";
                color = "blue";
            }
            if (c.getType() == CATEGORIES.TAG) {
                typeIcon = "tag";
                type = "Tag";
                color = "red";
            }
%>
    <li>
        <div id="<%= namespace %>categoryId<%= c.getId()%>">
            <div class="lwwcm-category-title"><span class="glyphicon glyphicon-<%= typeIcon %> margin-right lwwcm-<%= color%>"></span> <%= c.getName() %> <span class="lwwcm-category-type">(<%= type %>)</span></div>
            <div class="lwwcm-category-actions"><a href="<%= editCategoryView %>&editid=<%= c.getId() %>">Edit</a> | <a href="javascript:deleteCategory('<%= namespace %>', <%= c.getId() %>)">Delete</a>
                <%
                    if (c.getNumChildren() > 0) {
                %>
                | <a href="javascript:showChildrenCategories('<%= namespace %>', '<%= showCategoriesChildrenEvent %>', <%= c.getId() %>);" id="<%= namespace %>linkCatId<%= c.getId() %>">Show Children(<%= c.getNumChildren() %>)</a>
                <%
                    }
                %>
                | <a href="${filterCategoryPostsAction}&filterCategoryId=<%= c.getId()%>" title="Show Posts"><span class="glyphicon glyphicon-file margin-right"></span></a>
                | <a href="${filterCategoryUploadsAction}&filterCategoryId=<%= c.getId()%>" title="Show Uploads"><span class="glyphicon glyphicon-picture margin-right"></span></a>
                | <a href="${filterCategoryTemplatesAction}&filterCategoryId=<%= c.getId()%>" title="Show Templates"><span class="glyphicon glyphicon-th margin-right"></span></a>
            </div>
        </div>
    </li>
<%
        }
    }
%>
</ul>
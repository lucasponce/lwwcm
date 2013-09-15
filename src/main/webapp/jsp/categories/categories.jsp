<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="static org.gatein.lwwcm.Wcm.*" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/categories/categories.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>

    <form id="${n}deleteCategoryForm" method="post" action="${deleteCategoryAction}">
        <input type="hidden" id="${n}deleteCategoryId" name="deletedCategoryId" />
    </form>

    <ul class="lwwcm-categories">
        <%
            List<Category> list = (List<Category>)portletSession.getAttribute("list");
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
            <div id="${n}categoryId<%= c.getId()%>">
                <div class="lwwcm-category-title"><span class="glyphicon glyphicon-<%= typeIcon %> margin-right lwwcm-<%= color%> lwwcm-category-icon"></span> <%= c.getName() %>&nbsp;<span class="lwwcm-category-type">(<%= type %>)</span></div>
                <div class="lwwcm-category-actions"><a href="<%= editCategoryView %>&editid=<%= c.getId() %>">Edit</a> | <a href="javascript:deleteCategory('${n}', <%= c.getId() %>)">Delete</a>
                    <%
                        if (c.getNumChildren() > 0) {
                    %>
                    | <a href="javascript:showChildrenCategories('${n}', '${showCategoriesChildrenEvent}', <%= c.getId() %>);" id="${n}linkCatId<%= c.getId() %>">Show Children(<%= c.getNumChildren() %>)</a>
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
</div>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.Wcm" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/categories/category.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>

    <%
        Category c = (Category) portletSession.getAttribute("edit");
        if (c != null) {
    %>
    <form id="${n}editCategoryForm" method="post" action="${editCategoryAction}">
        <input type="hidden" id="${n}editCategoryId" name="editCategoryId" value="<%= c.getId() %>"/>
        <div class="lwwcm-newcategory">
                <label for="${n}newCategoryName">Category name: </label>
                <div class="lwwcm-newcategory-name"><input id="${n}newCategoryName" name="newCategoryName" class="lwwcm-input" value="<%= c.getName() %>"/></div>
                <label for="${n}newCategoryType">Category type: </label>
                <div class="lwwcm-newcategory-type"><select id="${n}newCategoryType" name="newCategoryType" class="lwwcm-input">
                    <option value="Category" <% if (c.getType() == Wcm.CATEGORIES.CATEGORY) { %> selected <% } %>>Category</option>
                    <option value="Folder" <% if (c.getType() == Wcm.CATEGORIES.FOLDER) { %> selected <% } %>>Folder</option>
                    <option value="Tag" <% if (c.getType() == Wcm.CATEGORIES.TAG) { %> selected <% } %>>Tag</option>
                </select></div>
                <a href="javascript:saveUpdateCategory('${n}');" class="button" title="Save Category">Save Category</a>
        </div>
        <%  String cssClass = "lwwcm-newcategory-parent";
            if (c.getType() == Wcm.CATEGORIES.FOLDER) {
                cssClass = "lwwcm-editcategory-parent";
            }
        %>
        <div class="<%= cssClass %>" id="${n}editCategoryParentContainer">
            <label for="${n}newCategoryParent">Category parent: </label>
            <div class="lwwcm-newcategory-type">
                <select id="${n}newCategoryParent" name="newCategoryParent" class="lwwcm-input">
                    <option value="-1">Root (no parent)</option>
                    <%
                        List<Category> list = (List<Category>)portletSession.getAttribute("categories");
                        if (list != null) {
                            for (Category cat : list) {
                                if (!cat.getId().equals(c.getId())) {
                    %>
                    <option value="<%= cat.getId() %>" <% if (c.getParent() !=null && c.getParent().getId().equals(cat.getId())) { %> selected <% } %>><%= ViewMetadata.categoryTitle(cat) %></option>
                    <%          }
                            }
                        }
                    %>
                </select>
            </div>
        </div>
    </form>
    <%
        }
    %>
</div>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/categories/category.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <form id="${n}newCategoryForm" method="post" action="${newCategoryAction}">
        <div class="lwwcm-newcategory">
                <label for="${n}newCategoryName">Category name: </label>
                <div class="lwwcm-newcategory-name"><input id="${n}newCategoryName" name="newCategoryName" class="lwwcm-input" /></div>
                <label for="${n}newCategoryType">Category type: </label>
                <div class="lwwcm-newcategory-type"><select id="${n}newCategoryType" name="newCategoryType" class="lwwcm-input" onchange="showParentCategory('${n}');">
                    <option value="Category">Category</option>
                    <option value="Folder">Folder</option>
                    <option value="Tag">Tag</option>
                </select></div>
                <a href="javascript:saveNewCategory('${n}');" class="button" title="Save Category">Save Category</a>
        </div>
        <div class="lwwcm-newcategory-parent" id="${n}editCategoryParentContainer">
            <label for="${n}newCategoryParent">Category parent: </label>
            <div class="lwwcm-newcategory-type">
                <select id="${n}newCategoryParent" name="newCategoryParent" class="lwwcm-input">
                    <option value="-1">Root (no parent)</option>
                    <%
                        List<Category> list = (List<Category>)portletSession.getAttribute("categories");
                        if (list != null) {
                            for (Category cat : list) {
                    %>
                    <option value="<%= cat.getId() %>"><%= cat.getName() %> [<%= cat.getId() %>]</option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
        </div>
    </form>
</div>
<%@include file="imports.jsp"%>
<%@include file="urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/category.js") %>"></script>
<div class="container">
    <%@include file="header.jsp"%>
    <%@include file="actions.jsp"%>
    <div class="lwwcm-newcategory">
        <label for="${n}editCategoryName">Category name: </label>
        <div class="lwwcm-newcategory-name"><input id="${n}editCategoryName" class="lwwcm-input" /></div>
        <label for="${n}editCategoryType">Category type: </label>
        <div class="lwwcm-newcategory-type"><select id="${n}editCategoryType" class="lwwcm-input">
            <option value="Category">Category</option>
            <option value="Folder">Folder</option>
            <option value="Tag">Tag</option>
        </select></div>
        <a href="#" class="button" title="Save Category">Save Category</a>
        <div class="lwwcm-newcategory-parent">
            <label for="${n}editCategoryParent">Category parent: </label>
            <div class="lwwcm-newcategory-type">
                <select id="${n}editCategoryParent" class="lwwcm-input">
                    <option value="Category 1">Category 1</option>
                </select>
            </div>
        </div>
    </div>
</div>
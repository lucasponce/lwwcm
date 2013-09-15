<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ page import="org.gatein.lwwcm.Wcm" %>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/uploads/uploadsActions.js") %>"></script>
<div class="lwwcm-posts-actions">
    <div class="lwwcm-checkbox left">
        <input type="checkbox" value="0" id="${n}selectAll" name="selectAll" onchange="selectAllUploads('${n}')" />
        <label for="${n}selectAll" title="Select All"></label>
    </div>

    <form id="${n}deleteSelectedUploadForm" method="post" action="${deleteSelectedUploadAction}">
        <input type="hidden" id="${n}deleteSelectedListId" name="deleteSelectedListId" />
    </form>
    <div class="left margin-left">
        <a href="javascript:deleteSelectedUploads('${n}');" class="button" title="Delete selected"><span class="glyphicon glyphicon-trash"></span></a>
    </div>

    <form id="${n}addSelectedCategoryUploadForm" method="post" action="${addSelectedCategoryUploadAction}">
        <input type="hidden" id="${n}addSelectedCategoryUploadListId" name="addSelectedCategoryUploadListId" />
        <input type="hidden" id="${n}addCategoryUploadId" name="addCategoryUploadId" />
    </form>
    <div class="margin-center">
        <a href="#" onclick="showSelectedCategories('${n}', this.id)" class="button" title="Assign Category" id="${n}assign-category"><span class="glyphicon glyphicon-tag"></span></a>
    </div>

    <%  ViewMetadata metadata = (ViewMetadata)portletSession.getAttribute("metadata");
        List<Category> listCategories = (List<Category>)portletSession.getAttribute("categories");
    %>
    <form id="${n}filterCategoryUploadsForm" method="post" action="${filterCategoryUploadsAction}">
        <input type="hidden" id="${n}filterCategoryId" name="filterCategoryId" />
    </form>
    <form id="${n}filterNameUploadsForm" method="post" action="${filterNameUploadsAction}">
        <input type="hidden" id="${n}filterName" name="filterName"  />
    </form>
    <div class="lwwcm-select margin-left">
        <select id="${n}selectFilterCategory" class="lwwcm-input" onchange="showFilterCategories('${n}')">
            <option value="-1">All categories</option>
            <%
                if (listCategories != null) {
                    for (Category c : listCategories) {
            %>
            <option value="<%= c.getId()%>" <% if (metadata != null && metadata.isFilterCategory() && metadata.getCategoryId().equals(c.getId())) { %> selected <% } %> > <%= ViewMetadata.categoryTitle(c) %></option>
            <%
                    }
                }
            %>
        </select>
    </div>
    <div class="lwwcm-post-filtername margin-left">
        <%
            String filterName = "Filter By Name";
            if (metadata != null && metadata.isFilterName()) filterName = metadata.getName();
        %>
        <input id="${n}inputFilterName" class="lwwcm-input" value="<%= filterName %>" onfocus="if (this.value == 'Filter By Name') this.value=''" onblur="if (this.value == '') this.value='Filter By Name'" onkeypress="showFilterNameUploads(event, '${n}')" />
    </div>
    <%
        if (metadata != null && ((metadata.getToIndex() + 1) < metadata.getTotalIndex())) {
    %>
    <a href="${rightUploadsAction}" class="button right" title="Older entries"><span class="glyphicon glyphicon-chevron-right"></span></a>
    <%
        }
        if (metadata != null && ((metadata.getFromIndex() +1) >= Wcm.VIEWS.MAX_PER_PAGE)) {
    %>
    <a href="${leftUploadsAction}" class="button right" title="Newer entries"><span class="glyphicon glyphicon-chevron-left"></span></a>
    <%
        }
        if (metadata != null) {
    %>
    <div class="lwwcm-pagination right"><%= metadata.getFromIndex()+1 %>-<%= metadata.getToIndex()+1 %> of <%= metadata.getTotalIndex() %></div>
    <%
        }
    %>
</div>
<form id="${n}addCategoryUploadForm" method="post" action="${addCategoryUploadAction}">
    <input type="hidden" id="${n}categoryId" name="categoryId" />
    <input type="hidden" id="${n}uploadId" name="uploadId" />
</form>

<div id="${n}uploads-categories" class="lwwcm-popup-categories lwwcm-dialog">
    <div class="lwwcm-dialog-title">Select category to Add</div>
    <a href="#" id="${n}close-posts-categories" class="lwwcm-dialog-close"><span> </span></a>
    <div class="lwwcm-dialog-body">
        <div class="lwwcm-select left">
            <select id="${n}selectAddCategory" class="lwwcm-input">
                <%
                    if (listCategories != null) {
                        for (Category c : listCategories) {
                %>
                <option value="<%= c.getId()%>"><%= ViewMetadata.categoryTitle(c) %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <a href="#" class="button right" title="Assign Category" id="${n}assign-single-category"><span class="glyphicon glyphicon-tag"></span></a>
        <div class="clear"></div>
    </div>
</div>

<div id="${n}uploads-preview" class="lwwcm-upload-preview">
    <img id="${n}uploads-preview-content" class="lwwcm-upload-image" src="" />
</div>
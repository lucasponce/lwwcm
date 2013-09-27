<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ page import="java.util.Set" %>
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/posts/postsActions.js") %>"></script>
<div class="lwwcm-posts-actions">
    <div class="lwwcm-checkbox left">
        <input type="checkbox" value="0" id="${n}selectAll" name="selectAll" onchange="selectAllPosts('${n}')" />
        <label for="${n}selectAll" title="Select All"></label>
    </div>

    <form id="${n}deleteSelectedPostForm" method="post" action="${deleteSelectedPostAction}">
        <input type="hidden" id="${n}deleteSelectedListId" name="deleteSelectedListId" />
    </form>
    <div class="left margin-left">
        <a href="javascript:deleteSelectedPosts('${n}');" class="button" title="Delete selected"><span class="glyphicon glyphicon-trash"></span></a>
    </div>

    <form id="${n}addSelectedCategoryPostForm" method="post" action="${addSelectedCategoryPostAction}">
        <input type="hidden" id="${n}addSelectedCategoryPostListId" name="addSelectedCategoryPostListId" />
        <input type="hidden" id="${n}addCategoryPostId" name="addCategoryPostId" />
    </form>
    <form id="${n}publishPostsForm" method="post" action="${publishPostsAction}">
        <input type="hidden" id="${n}publishPostsId" name="publishPostsId" />
        <input type="hidden" id="${n}publishStates" name="publishStates" />
    </form>
    <div class="margin-center">
        <a href="#" onclick="showSelectedCategoriesPosts('${n}', this.id)" class="button" title="Assign Category" id="${n}assign-category"><span class="glyphicon glyphicon-tag"></span></a>
        <a href="javascript:publishPosts('${n}', '<%= Wcm.POSTS.PUBLISHED %>');" class="button" title="Publish"><span class="glyphicon glyphicon-thumbs-up"></span></a>
        <a href="javascript:publishPosts('${n}', '<%= Wcm.POSTS.DRAFT %>');" class="button" title="Draft"><span class="glyphicon glyphicon-thumbs-down"></span></a>
    </div>

    <%  ViewMetadata metadata = (ViewMetadata)portletSession.getAttribute("metadata");
        List<Category> listCategories = (List<Category>)renderRequest.getAttribute("categories");
    %>
    <form id="${n}filterCategoryPostsForm" method="post" action="${filterCategoryPostsAction}">
        <input type="hidden" id="${n}filterCategoryId" name="filterCategoryId" />
    </form>
    <form id="${n}filterNamePostsForm" method="post" action="${filterNamePostsAction}">
        <input type="hidden" id="${n}filterName" name="filterName"  />
    </form>
    <div class="lwwcm-select margin-left">
        <select id="${n}selectFilterCategory" class="lwwcm-input" onchange="showFilterCategoriesPosts('${n}')">
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
        <input id="${n}inputFilterName" class="lwwcm-input" value="<%= filterName %>" onfocus="if (this.value == 'Filter By Name') this.value=''" onblur="if (this.value == '') this.value='Filter By Name'" onkeypress="showFilterNamePosts(event, '${n}')" />
    </div>
    <%
        if (metadata != null && ((metadata.getToIndex() + 1) < metadata.getTotalIndex())) {
    %>
    <a href="${rightPostsAction}" class="button right" title="Older entries"><span class="glyphicon glyphicon-chevron-right"></span></a>
    <%
        }
        if (metadata != null && ((metadata.getFromIndex() +1) >= Wcm.VIEWS.MAX_PER_PAGE)) {
    %>
    <a href="${leftPostsAction}" class="button right" title="Newer entries"><span class="glyphicon glyphicon-chevron-left"></span></a>
    <%
        }
        if (metadata != null) {
    %>
    <div class="lwwcm-pagination right"><%= metadata.getFromIndex()+1 %>-<%= metadata.getToIndex()+1 %> of <%= metadata.getTotalIndex() %></div>
    <%
        }
    %>
</div>
<form id="${n}addCategoryPostForm" method="post" action="${addCategoryPostAction}">
    <input type="hidden" id="${n}categoryId" name="categoryId" />
    <input type="hidden" id="${n}postId" name="postId" />
</form>

<div id="${n}posts-categories" class="lwwcm-popup-categories lwwcm-dialog">
    <div class="lwwcm-dialog-title">Select category to Add</div>
    <a href="#" id="${n}close-posts-categories" class="lwwcm-dialog-close"><span> </span></a>
    <div class="lwwcm-dialog-body">
        <div class="lwwcm-select left">
            <select id="${n}selectAddCategory" class="lwwcm-input">
                <%
                    if (listCategories != null) {
                        for (Category c : listCategories) {
                            if (userWcm.canWrite(c)) {
                %>
                <option value="<%= c.getId()%>"><%= ViewMetadata.categoryTitle(c) %> </option>
                <%
                            }
                        }
                    }
                %>
            </select>
        </div>
        <a href="#" class="button right" title="Assign Category" id="${n}assign-single-category"><span class="glyphicon glyphicon-tag"></span></a>
        <div class="clear"></div>
    </div>
</div>


<input type="hidden" id="${n}aclPostId" name="aclPostId" />
<div id="${n}posts-acls" class="lwwcm-popup-categories lwwcm-dialog">
    <div id="${n}posts-acls-title" class="lwwcm-dialog-title">ACLs</div>
    <a href="#" id="${n}close-posts-acls" class="lwwcm-dialog-close"><span> </span></a>
    <div class="lwwcm-dialog-body">
        <div class="lwwcm-acl-new">
            New ACL:

            <div class="lwwcm-acl-type"><select id="${n}aclType" name="aclType" class="lwwcm-input lwwcm-acl-type-input">
                <option value="<%= Wcm.ACL.WRITE %>">WRITE</option>
                <option value="<%= Wcm.ACL.NONE%>">NONE</option>
            </select></div>

            <div class="lwwcm-acl-group"><select id="${n}aclWcmGroup" name="aclWcmGroup" class="lwwcm-input lwwcm-acl-group-input">
                <option value="<%= Wcm.GROUPS.ALL%>">ALL</option>
                <%
                    Set<String> wcmGroups = (Set<String>)request.getAttribute("wcmGroups");
                    if (wcmGroups != null) {
                        for (String groupId : wcmGroups) {
                %>
                <option value="<%= groupId %>"><%= groupId %></option>
                <%
                        }
                    }
                %>
            </select></div>
            <a href="javascript:;" onclick="addAcl('${n}', '${addAclPostEvent}')" id="${n}addAclButton" class="button" title="Add ACL"><span class="glyphicon glyphicon-plus"></span></a>
        </div>

        <div class="lwwcm-acl-attached" id="${n}posts-acls-attached">
        </div>

    </div>
</div>

<div id="${n}posts-comments" class="lwwcm-popup-categories lwwcm-dialog">
    <div id="${n}posts-comments-title" class="lwwcm-dialog-title">Comments</div>
    <a href="#" id="${n}close-posts-comments" class="lwwcm-dialog-close"><span> </span></a>
    <div class="lwwcm-dialog-body" id="${n}post-comments-list">

    </div>
</div>

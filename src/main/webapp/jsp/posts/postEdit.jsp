<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ page import="javax.portlet.ResourceURL" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/posts/post.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>
    <%
        List<Category> listCategories = (List<Category>)portletSession.getAttribute("categories");
    %>
    <div id="${n}uploads-preview" class="lwwcm-upload-preview">
        <img id="${n}uploads-preview-content" class="lwwcm-upload-image" src="" />
    </div>
    <div id="${n}post-select-upload" class="lwwcm-popup-categories lwwcm-dialog">
        <div class="lwwcm-dialog-title">Select upload to Add</div>
        <a href="#" id="${n}close-post-select-upload" class="lwwcm-dialog-close"><span> </span></a>
        <div class="lwwcm-dialog-body">
            <div class="lwwcm-select left">
                <select id="${n}selectFilterCategory" class="lwwcm-input">
                    <option value="-1">All categories</option>
                    <%
                        if (listCategories != null) {
                            for (Category c : listCategories) {
                    %>
                    <option value="<%= c.getId()%>"><%= ViewMetadata.categoryTitle(c) %> </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="lwwcm-post-filtername right">
                <%
                    String filterName = "Filter By Name";
                %>
                <input id="${n}inputFilterName" class="lwwcm-input" value="<%= filterName %>" onfocus="if (this.value == 'Filter By Name') this.value=''" onblur="if (this.value == '') this.value='Filter By Name'" />
            </div>
            <div class="clear"></div>
            <div class="lwwcm-post-uploads" id="${n}listUploads">
            </div>
        </div>
    </div>

    <%
        Post p = (Post) portletSession.getAttribute("edit");
        if (p != null)
    %>
    <form id="${n}editPostForm" method="post" action="${editPostAction}">
    <div class="lwwcm-newpost-title"><input id="${n}postTitle" name="postTitle" class="lwwcm-input" value="<%= p.getTitle() %>" onfocus="if (this.value == 'Post Title') this.value=''" onblur="if (this.value == '') this.value='Post Title'"/></div>
    <div class="lwwcm-newpost-title"><textarea id="${n}postExcerpt" name="postExcerpt" class="lwwcm-input" onfocus="if (this.value == 'Summary / Excerpt') this.value=''" onblur="if (this.value == '') this.value='Summary / Excerpt'"><%= p.getExcerpt() %></textarea></div>
    <div class="lwwcm-newtemplate">
        Locale: <div class="lwwcm-newtemplate-locale"><input id="${n}postLocale" name="postLocale" class="lwwcm-input" value="<%= p.getLocale() %>"/></div>
        Comments: <div class="lwwcm-newtemplate-type">
                    <select id="${n}postCommentsStatus" name="postCommentsStatus" class="lwwcm-input">
                        <option value="<%= Wcm.COMMENTS.ANONYMOUS%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.ANONYMOUS)) { %> selected <% } %>>Anonymous</option>
                        <option value="<%= Wcm.COMMENTS.LOGGED%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.LOGGED)) { %> selected <% } %>>Logged</option>
                        <option value="<%= Wcm.COMMENTS.NO_COMMENTS%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.NO_COMMENTS)) { %> selected <% } %>>No Comments</option>
                    </select>
                  </div>
        Status:  <div class="lwwcm-newtemplate-type">
                    <select id="${n}postStatus" name="postStatus" class="lwwcm-input">
                        <option value="<%= Wcm.POSTS.DRAFT %>" <% if (p.getPostStatus().equals(Wcm.POSTS.DRAFT)) { %> selected <% } %>>Draft</option>
                        <option value="<%= Wcm.POSTS.PUBLISHED %>" <% if (p.getPostStatus().equals(Wcm.POSTS.PUBLISHED)) { %> selected <% } %>>Published</option>
                    </select>
                 </div>
        <a href="javascript:saveUpdatePost('${n}');" class="button" title="Save Post">Save Post</a>
    </div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <input id="${n}urlShowPostUploadsEvent" type="hidden" value="${showPostUploadsEvent}" />
    <script>
        CKEDITOR.on( 'instanceCreated', function( event ) {
            var editor = event.editor;
            editor.portalnamespace='${n}';
        });
    </script>
    <textarea class="ckeditor" id="${n}postContent" name="postContent"><%= p.getContent() %></textarea>
    </form>
</div>
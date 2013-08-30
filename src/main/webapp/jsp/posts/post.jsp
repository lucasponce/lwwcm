<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/posts/post.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <form id="${n}newPostForm" method="post" action="${newPostAction}">
    <div class="lwwcm-newpost-title"><input id="${n}postTitle" name="postTitle" class="lwwcm-input" value="Post Title" onfocus="if (this.value == 'Post Title') this.value=''" onblur="if (this.value == '') this.value='Post Title'"/></div>
    <div class="lwwcm-newpost-title"><textarea id="${n}postExcerpt" name="postExcerpt" class="lwwcm-input" onfocus="if (this.value == 'Summary / Excerpt') this.value=''" onblur="if (this.value == '') this.value='Summary / Excerpt'">Summary / Excerpt</textarea></div>
    <div class="lwwcm-newtemplate">
        Locale: <div class="lwwcm-newtemplate-locale"><input id="${n}postLocale" name="postLocale" class="lwwcm-input" value="<%= renderRequest.getLocale().getLanguage() %>"/></div>
        Comments: <div class="lwwcm-newtemplate-type">
                    <select id="${n}postCommentsStatus" name="postCommentsStatus" class="lwwcm-input">
                        <option value="<%= Wcm.COMMENTS.ANONYMOUS%>">Anonymous</option>
                        <option value="<%= Wcm.COMMENTS.LOGGED%>">Logged</option>
                        <option value="<%= Wcm.COMMENTS.NO_COMMENTS%>">No Comments</option>
                    </select>
                  </div>
        Status:  <div class="lwwcm-newtemplate-type">
                    <select id="${n}postStatus" name="postStatus" class="lwwcm-input">
                        <option value="<%= Wcm.POSTS.DRAFT %>">Draft</option>
                        <option value="<%= Wcm.POSTS.PUBLISHED %>">Published</option>
                    </select>
                 </div>
        <a href="javascript:saveNewPost('${n}');" class="button" title="Save Post">Save Post</a>
    </div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <textarea class="ckeditor" id="${n}postContent" name="postContent"></textarea>
    </form>
</div>
<%@include file="imports.jsp"%>
<%@include file="urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/post.js") %>"></script>
<div class="container">
    <%@include file="header.jsp"%>
    <%@include file="actions.jsp"%>
    <div class="lwwcm-newpost-title"><input id="${n}editTitle" class="lwwcm-input" value="Post Title" onfocus="if (this.value == 'Post Title') this.value=''" onblur="if (this.value == '') this.value='Post Title'"/></div>
    <div class="lwwcm-newpost-title"><textarea id="${n}editSummary" class="lwwcm-input" onfocus="if (this.value == 'Summary / Excerpt') this.value=''" onblur="if (this.value == '') this.value='Summary / Excerpt'">Summary / Excerpt</textarea></div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <textarea class="ckeditor" id="${n}editPost">First post. Testing inline editor.</textarea>
</div>
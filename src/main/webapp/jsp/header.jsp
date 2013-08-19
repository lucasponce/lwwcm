<c:set var="view" value="${renderRequest.getParameter('view')}" />
<c:if test="${view == null}">
    <c:set var="view" value="posts" />
</c:if>
<div class="lwwcm-header">
    <ul>
        <li <c:if test="${view == 'posts' || view == 'newpost'}">class="selected"</c:if>><a href="${posts}">Posts</a></li>
        <li <c:if test="${view == 'categories' || view == 'newcategory'}">class="selected"</c:if>><a href="${categories}">Categories</a></li>
        <li <c:if test="${view == 'uploads' || view == 'newupload'}">class="selected"</c:if>><a href="${uploads}">Uploads</a></li>
        <li <c:if test="${view == 'templates' || view == 'newtemplate'}">class="selected"</c:if>><a href="${templates}">Templates</a></li>
    </ul>
</div>
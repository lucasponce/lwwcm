<div class="lwwcm-actions">
    <ul>
        <li><a href="${newPostView}"><span class="glyphicon glyphicon-file margin-right"></span> New post</a></li>
        <li><a href="${newCategoryView}"><span class="glyphicon glyphicon-tags margin-right"></span> New category</a></li>
        <li><a href="${newUploadView}"><span class="glyphicon glyphicon-picture margin-right"></span> New upload</a></li>
        <%
            if (isManager) {
        %>
        <li><a href="${newTemplateView}"><span class="glyphicon glyphicon-th margin-right"></span> New template</a></li>
        <%
            }
        %>
    </ul>
</div>
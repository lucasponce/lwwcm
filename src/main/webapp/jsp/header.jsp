<%
        String view = renderRequest.getParameter("view");
        if (view == null) view = Wcm.VIEWS.POSTS;
%>
<div class="lwwcm-header">
    <ul>
        <li <% if (view.equals(Wcm.VIEWS.POSTS) || view.equals(Wcm.VIEWS.NEW_POST)) { %>class="selected" <% } %>><a href="${postsView}">Posts</a></li>
        <li <% if (view.equals(Wcm.VIEWS.CATEGORIES) || view.equals(Wcm.VIEWS.NEW_CATEGORY) || view.equals(Wcm.VIEWS.EDIT_CATEGORY)) { %>class="selected" <% } %>><a href="${categoriesView}">Categories</a></li>
        <li <% if (view.equals(Wcm.VIEWS.UPLOADS) || view.equals(Wcm.VIEWS.NEW_UPLOAD) || view.equals(Wcm.VIEWS.EDIT_UPLOAD)) { %>class="selected" <% } %>><a href="${uploadsView}">Uploads</a></li>
        <li <% if (view.equals(Wcm.VIEWS.TEMPLATES) || view.equals(Wcm.VIEWS.NEW_TEMPLATE) || view.equals(Wcm.VIEWS.EDIT_TEMPLATE)) { %>class="selected" <% } %>><a href="${templatesView}">Templates</a></li>
    </ul>
</div>
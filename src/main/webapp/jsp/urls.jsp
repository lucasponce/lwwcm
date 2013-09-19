<%@ page import="org.gatein.lwwcm.Wcm" %>
<% // View URLs %>
<portlet:actionURL var="postsView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="categoriesView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.CATEGORIES %>" />
</portlet:actionURL>
<portlet:actionURL var="uploadsView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.UPLOADS %>" />
</portlet:actionURL>
<portlet:actionURL var="templatesView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.TEMPLATES %>" />
</portlet:actionURL>
<portlet:actionURL var="newPostView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.NEW_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="newCategoryView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.NEW_CATEGORY %>" />
</portlet:actionURL>
<portlet:actionURL var="newUploadView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.NEW_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="newTemplateView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.NEW_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="editCategoryView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.EDIT_CATEGORY %>" />
</portlet:actionURL>
<portlet:actionURL var="editUploadView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.EDIT_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="editTemplateView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.EDIT_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="editPostView">
    <portlet:param name="view" value="<%= Wcm.VIEWS.EDIT_POST %>" />
</portlet:actionURL>
<% // Actions URLs %>
<portlet:actionURL var="postsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="newPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.NEW_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="editPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.EDIT_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="deletePostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="publishPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.PUBLISH_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="publishPostsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.PUBLISH_POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="removeCategoryPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.REMOVE_CATEGORY_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteSelectedPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_SELECTED_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="addSelectedCategoryPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_SELECTED_CATEGORY_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="filterCategoryPostsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_CATEGORY_POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="filterNamePostsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_NAME_POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="rightPostsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.RIGHT_POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="leftPostsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.LEFT_POSTS %>" />
</portlet:actionURL>
<portlet:actionURL var="addCategoryPostAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_CATEGORY_POST %>" />
</portlet:actionURL>
<portlet:actionURL var="newCategoryAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.NEW_CATEGORY %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteCategoryAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_CATEGORY %>" />
</portlet:actionURL>
<portlet:actionURL var="editCategoryAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.EDIT_CATEGORY %>" />
</portlet:actionURL>
<portlet:actionURL var="newUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.NEW_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="rightUploadsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.RIGHT_UPLOADS %>" />
</portlet:actionURL>
<portlet:actionURL var="leftUploadsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.LEFT_UPLOADS %>" />
</portlet:actionURL>
<portlet:actionURL var="editUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.EDIT_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="addCategoryUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_CATEGORY_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="filterCategoryUploadsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_CATEGORY_UPLOADS %>" />
</portlet:actionURL>
<portlet:actionURL var="filterNameUploadsAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_NAME_UPLOADS %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteSelectedUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_SELECTED_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="addSelectedCategoryUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_SELECTED_CATEGORY_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="removeCategoryUploadAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.REMOVE_CATEGORY_UPLOAD %>" />
</portlet:actionURL>
<portlet:actionURL var="newTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.NEW_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteSelectedTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_SELECTED_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="addSelectedCategoryTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_SELECTED_CATEGORY_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="filterCategoryTemplatesAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_CATEGORY_TEMPLATES %>" />
</portlet:actionURL>
<portlet:actionURL var="filterNameTemplatesAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.FILTER_NAME_TEMPLATES %>" />
</portlet:actionURL>
<portlet:actionURL var="rightTemplatesAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.RIGHT_TEMPLATES %>" />
</portlet:actionURL>
<portlet:actionURL var="leftTemplatesAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.LEFT_TEMPLATES %>" />
</portlet:actionURL>
<portlet:actionURL var="addCategoryTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.ADD_CATEGORY_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="removeCategoryTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.REMOVE_CATEGORY_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="editTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.EDIT_TEMPLATE %>" />
</portlet:actionURL>
<portlet:actionURL var="deleteTemplateAction">
    <portlet:param name="action" value="<%= Wcm.ACTIONS.DELETE_TEMPLATE %>" />
</portlet:actionURL>
<% // Event URL %>
<portlet:resourceURL var="showCategoriesChildrenEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_CATEGORIES_CHILDREN %>" />
</portlet:resourceURL>
<portlet:resourceURL var="showPostUploadsEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_POST_UPLOADS %>" />
</portlet:resourceURL>
<portlet:resourceURL var="showPostAclsEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_POST_ACLS %>" />
</portlet:resourceURL>
<portlet:resourceURL var="addAclPostEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.ADD_ACL_POST %>" />
</portlet:resourceURL>
<portlet:resourceURL var="showCategoryAclsEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_CATEGORY_ACLS %>" />
</portlet:resourceURL>
<portlet:resourceURL var="addAclCategoryEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.ADD_ACL_CATEGORY %>" />
</portlet:resourceURL>
<portlet:resourceURL var="showUploadAclsEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_UPLOAD_ACLS %>" />
</portlet:resourceURL>
<portlet:resourceURL var="addAclUploadEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.ADD_ACL_UPLOAD %>" />
</portlet:resourceURL>




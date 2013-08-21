<% // View URLs %>
<portlet:actionURL var="posts">
    <portlet:param name="view" value="posts" />
</portlet:actionURL>
<portlet:actionURL var="categories">
    <portlet:param name="view" value="categories" />
</portlet:actionURL>
<portlet:actionURL var="uploads">
    <portlet:param name="view" value="uploads" />
</portlet:actionURL>
<portlet:actionURL var="templates">
    <portlet:param name="view" value="templates" />
</portlet:actionURL>
<portlet:actionURL var="newpost">
    <portlet:param name="view" value="newpost" />
</portlet:actionURL>
<portlet:actionURL var="newcategory">
    <portlet:param name="view" value="newcategory" />
</portlet:actionURL>
<portlet:actionURL var="editcategory">
    <portlet:param name="view" value="editcategory" />
</portlet:actionURL>
<portlet:actionURL var="newupload">
    <portlet:param name="view" value="newupload" />
</portlet:actionURL>
<portlet:actionURL var="newtemplate">
    <portlet:param name="view" value="newtemplate" />
</portlet:actionURL>
<% // Actions URLs %>
<portlet:actionURL var="postsAction">
    <portlet:param name="action" value="posts" />
</portlet:actionURL>
<portlet:actionURL var="newCategoryAction">
    <portlet:param name="action" value="newCategory" />
</portlet:actionURL>
<portlet:actionURL var="deleteCategoryAction">
    <portlet:param name="action" value="deleteCategory" />
</portlet:actionURL>
<portlet:actionURL var="editCategoryAction">
    <portlet:param name="action" value="editCategory" />
</portlet:actionURL>
<% // Event URL %>
<portlet:resourceURL var="showCategoriesChildrenEvent">
    <portlet:param name="event" value="showCategoriesChildren" />
</portlet:resourceURL>

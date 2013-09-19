<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.domain.UserWcm" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@include file="../../imports.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/content/render/content.js") %>"></script>
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
<%
    List<Category> listCategories = (List<Category>)request.getAttribute("categories");
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
<script>
    CKEDITOR.on( 'instanceCreated', function( event ) {
        var editor = event.editor;
        editor.portalnamespace='${n}';
    });
</script>
<portlet:resourceURL var="showPostUploadsEvent">
    <portlet:param name="event" value="<%= Wcm.EVENTS.SHOW_POST_UPLOADS %>" />
</portlet:resourceURL>
<input id="${n}urlShowPostUploadsEvent" type="hidden" value="${showPostUploadsEvent}" />
<%
    String processedTemplate =  (String)renderRequest.getAttribute("processedTemplate");
    if (processedTemplate != null) {
%>
<%= processedTemplate %>
<%
    }
%>
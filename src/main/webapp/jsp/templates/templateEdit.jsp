<%@ page import="org.gatein.lwwcm.domain.Template" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/templates/template.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>
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

    <%
        Template t = (Template) request.getAttribute("edit");
        if (t != null) {
    %>
    <form id="${n}editTemplateForm" method="post" action="${editTemplateAction}">
    <input type="hidden" id="${n}templateEditId" name="templateEditId" value="<%= t.getId() %>" />
    <div class="lwwcm-newpost-title"><input id="${n}templateName" name="templateName" class="lwwcm-input" value="<%= t.getName() %>" onfocus="if (this.value == 'Template Name') this.value=''" onblur="if (this.value == '') this.value='Template Name'"/></div>
    <div class="lwwcm-newtemplate">
        Locale: <div class="lwwcm-newtemplate-locale"><input id="${n}templateLocale" name="templateLocale" class="lwwcm-input" value="<%= t.getLocale() %>"/></div>
        Template type: <div class="lwwcm-newtemplate-type">
                            <select id="${n}templateType" name="templateType" class="lwwcm-input">
                                <option value="S" <% if (t.getType().equals(Wcm.TEMPLATES.SINGLE)) {%> selected <% } %>>Single Content</option>
                                <option value="L" <% if (t.getType().equals(Wcm.TEMPLATES.LIST)) {%> selected <% } %>>List Content</option>
                            </select>
                        </div>
        <a href="javascript:saveUpdateTemplate('${n}');" class="button" title="Save Template">Save Template</a>
    </div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <input id="${n}urlShowPostUploadsEvent" type="hidden" value="${showPostUploadsEvent}" />
    <script>
        CKEDITOR.on( 'instanceCreated', function( event ) {
            var editor = event.editor;
            editor.portalnamespace='${n}';
        });
    </script>
    <textarea class="ckeditor" id="${n}templateContent" name="templateContent"><%= t.getContent() %></textarea>
    </form>
    <%
        }
    %>
</div>
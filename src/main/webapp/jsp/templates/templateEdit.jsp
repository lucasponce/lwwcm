<%@ page import="org.gatein.lwwcm.domain.Template" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/template.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <%
        Template t = (Template) portletSession.getAttribute("edit");
        if (t != null) {
    %>
    <form id="${n}editTemplateForm" method="post" action="${editTemplateAction}">
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
    <textarea class="ckeditor" id="${n}templateContent" name="templateContent"><%= t.getContent() %></textarea>
    </form>
    <%
        }
    %>
</div>
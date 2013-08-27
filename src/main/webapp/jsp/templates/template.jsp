<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/template.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <form id="${n}newTemplateForm" method="post" action="${newTemplateAction}">
    <div class="lwwcm-newpost-title"><input id="${n}templateName" name="templateName" class="lwwcm-input" value="Template Name" onfocus="if (this.value == 'Template Name') this.value=''" onblur="if (this.value == '') this.value='Template Name'"/></div>
    <div class="lwwcm-newtemplate">
        Locale: <div class="lwwcm-newtemplate-locale"><input id="${n}templateLocale" name="templateLocale" class="lwwcm-input" value="<%= renderRequest.getLocale().getLanguage() %>"/></div>
        Template type: <div class="lwwcm-newtemplate-type">
                            <select id="${n}templateType" name="templateType" class="lwwcm-input">
                                <option value="S">Single Content</option>
                                <option value="L">List Content</option>
                            </select>
                        </div>
        <a href="javascript:saveNewTemplate('${n}');" class="button" title="Save Template">Save Template</a>
    </div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <textarea class="ckeditor" id="${n}templateContent" name="templateContent"></textarea>
    </form>
</div>
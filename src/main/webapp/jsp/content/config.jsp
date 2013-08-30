<%@ page import="org.gatein.lwwcm.Wcm" %>
<%@ page import="org.gatein.lwwcm.domain.Template" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects />
<c:set var="n"><portlet:namespace/></c:set>
<portlet:actionURL var="saveConfigurationAction">
    <portlet:param name="action" value="<%= Wcm.CONFIG.ACTIONS.SAVE_CONFIGURATION %>" />
</portlet:actionURL>
<portlet:resourceURL var="changeContentEvent" />
<script type="text/javascript">
    function changeType${n}() {
        require(["SHARED/jquery"], function($) {
            var typeContentId = "#${n}typeContent";
            var contentId = "#${n}contentId";
            var newTypeContent = $(typeContentId).val();
            $.ajax({
                type: "POST",
                url: '<%= changeContentEvent %>&event=<%= Wcm.CONFIG.EVENTS.CHANGE_CHOOSE_CONTENT %>&newTypeContent=' + newTypeContent,
                cache: false,
                dataType: "text",
                success: function(data)
                {
                    $(contentId).html(data);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown)
                {
                    alert("Ajax call KO !!");
                }
            });
        });
    }
    function addNewAttachment${n}() {
        require(["SHARED/jquery"], function($) {
            var typeContentId = "#${n}typeContent";
            var contentId = "#${n}contentId";
            var attachedId = "#${n}contentAttached";
            $.ajax({
                type: "POST",
                url: '<%= changeContentEvent %>&event=<%= Wcm.CONFIG.EVENTS.NEW_CONTENT_ATTACHED %>&newTypeContent=' + $(typeContentId).val() + '&newContentId=' +  $(contentId).val(),
                cache: false,
                dataType: "text",
                success: function(data)
                {
                    $(attachedId).html(data);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown)
                {
                    alert("Ajax call KO !!");
                }
            });
        });
    }
    function deleteAttachment${n}(contentId) {
        require(["SHARED/jquery"], function($) {
            var attachedId = "#${n}attached" + contentId;
            $.ajax({
                type: "POST",
                url: '<%= changeContentEvent %>&event=<%= Wcm.CONFIG.EVENTS.DELETE_CONTENT_ATTACHED %>&deleteContentId=' + contentId,
                cache: false,
                dataType: "text",
                success: function(data)
                {
                    $(attachedId).remove();
                },
                error: function(XMLHttpRequest, textStatus, errorThrown)
                {
                    alert("Ajax call KO !!");
                }
            });
        });
    }
    function saveConfig${n}() {
        require(["SHARED/jquery"], function($) {
            var formId = "#${n}saveConfiguration";
            var saveTemplateId = "#${n}saveTemplateId";
            var saveListenId = "#${n}saveListenId";
            var saveExportId = "#${n}saveExportId";
            var contentTemplateId = "#${n}contentTemplateId";
            var listenId = "#${n}listenId";
            var exportId = "#${n}exportId";
            $(saveTemplateId).val( $(contentTemplateId).val() );
            $(saveListenId).val( $(listenId).val() );
            $(saveExportId).val( $(exportId).val() );
            $(formId).submit();
        });
    }
</script>
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/lwwcm.css") %>" />
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/bootstrap-glyphicons.css") %>" />
<form id="${n}changeChooseContent" name="changeChooseContent" method="post" action="${changeContentAction}" class="lwwcm-config-form">
    <input type="hidden" id="${n}contentType" name="contentType" />
</form>
<form id="${n}newContentAttached" name="newContentAttached" method="post" action="${newContentAttachedAction}">
    <input type="hidden" id="${n}newTypeContent" name="newTypeContent" />
    <input type="hidden" id="${n}newContentId" name="newContentId" />
</form>
<form id="${n}saveConfiguration" method="post" action="${saveConfigurationAction}" class="lwwcm-config-form">
    <input type="hidden" id="${n}saveTemplateId" name="saveTemplateId" />
    <input type="hidden" id="${n}saveListenId" name="saveListenId" />
    <input type="hidden" id="${n}saveExportId" name="saveExportId" />
</form>
<div class="lwwcm-config">
    <span class="glyphicon glyphicon-th margin-right"></span> Template:
        <%
            List<Template> templates = (List<Template>)renderRequest.getAttribute("templates");
            String contentTemplateId = (String)renderRequest.getAttribute("contentTemplateId");
        %>
        <div class="lwwcm-config-template"><select id="${n}contentTemplateId" name="contentTemplateId" class="lwwcm-input lwwcm-config-template-input">
            <option value="-1" <% if (contentTemplateId != null && contentTemplateId.equals("-1")) { %>selected <% } %>>No Template</option>
            <%
                if (templates != null) {
                    for (Template t : templates) {
            %>
            <option value="<%= t.getId()%>" <% if (contentTemplateId != null && contentTemplateId.equals(t.getId().toString())) { %>selected <% } %>><%= t.getName() %> (<%= t.getLocale() %>) [<%= t.getId()%>]</option>
            <%
                    }
                }
            %>
        </select></div>
</div>
<div class="lwwcm-config">
    <span class="glyphicon glyphicon-file margin-right"></span> Content attached:
    <div class="lwwcm-attached" id="${n}contentAttached">
        <ul>
            <%
                List<Object> listAttachedContent = (List<Object>)renderRequest.getAttribute("listAttachedContent");
                if (listAttachedContent != null) {
                    for (Object o : listAttachedContent) {
                        if (o instanceof Category) {
                            Category c = (Category)o;
            %>
            <li id="${n}attached<%= c.getId()%>_C"><span class="glyphicon glyphicon-tags margin-right"></span> <%= c.getName() %> <a href="#" onclick="deleteAttachment${n}('<%= c.getId()%>_C');"><span class="glyphicon glyphicon-remove middle"></span></a></li>
            <%
            } else {
                Post p = (Post)o;
            %>
            <li id="${n}attached<%= p.getId()%>_P"><span class="glyphicon glyphicon-file margin-right"></span> <%= p.getTitle() %> <a href="#" onclick="deleteAttachment${n}('<%= p.getId()%>_P');"><span class="glyphicon glyphicon-remove middle"></span></a></li>
            <%
                        }
                    }
                }
            %>
        </ul>
    </div>

    <div class="lwwcm-config-new">
        New:
        <%
            String contentType = (String)request.getAttribute("contentType");
        %>
        <div class="lwwcm-config-type"><select id="${n}typeContent" name="typeContent" class="lwwcm-input lwwcm-config-type-input" onchange="changeType${n}();">
            <option value="C" <% if (contentType != null && contentType.equals("C")) { %> selected <% } %>>Category</option>
            <option value="P" <% if (contentType != null && contentType.equals("P")) { %> selected <% } %>>Post</option>
        </select></div>
        <div class="lwwcm-config-ids"><select id="${n}contentId" name="contentId" class="lwwcm-input lwwcm-config-ids-input">
            <%
                if (contentType.equals("C")) {
                    List<Category> categories = (List<Category>)request.getAttribute("chooseContent");
                    if (categories != null) {
                        for (Category c : categories) {
            %>
            <option value="<%= c.getId() %>"><%= c.getName() %></option>
            <%
                        }
                    }
                } else {
                    List<Post> posts = (List<Post>)request.getAttribute("chooseContent");
                    if (posts != null) {
                        for (Post p : posts) {
            %>
            <option value="<%= p.getId() %>"><%= p.getTitle() %></option>
            <%
                        }
                    }
                }
            %>
        </select></div>
        <a href="#" onclick="addNewAttachment${n}();" class="button" title="Attach Content"><span class="glyphicon glyphicon-plus"></span></a>
    </div>

</div>
<div class="lwwcm-config-new">
    <span class="glyphicon glyphicon-eye-open margin-right"></span> Listen id:
    <%
        String listenId = (String)renderRequest.getAttribute("listenId");
        listenId = (listenId == null ? "" : listenId);

        String exportId = (String)renderRequest.getAttribute("exportId");
        exportId = (exportId == null ? "" : exportId);
    %>
    <div class="lwwcm-config-type lwwcm-config-margin"><input id="${n}listenId" value="<%= listenId %>" name="listenId" type="text" class="lwwcm-input lwwcm-config-type-input" /></div>
    <span class="glyphicon glyphicon-share-alt margin-right"></span> Export id:
    <div class="lwwcm-config-type lwwcm-config-margin"><input id="${n}exportId" value="<%= exportId %>" name="exportId" type="text" class="lwwcm-input lwwcm-config-type-input" /></div>
    <a href="#" onclick="saveConfig${n}();" class="button" title="Save Configuration">Save Configuration</a>
</div>


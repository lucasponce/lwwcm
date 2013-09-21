<%@ page import="org.gatein.lwwcm.Wcm" %>
<%@ page import="org.gatein.lwwcm.domain.Template" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
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
                    saveConfig${n}();
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
                    saveConfig${n}();
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
            var mainTemplateId = "#${n}mainTemplateId";
            var mainTemplateIdSave = "#${n}mainTemplateIdSave";
            var postTemplateId = "#${n}postTemplateId";
            var postTemplateIdSave = "#${n}postTemplateIdSave";
            var categoryTemplateId = "#${n}categoryTemplateId";
            var categoryTemplateIdSave = "#${n}categoryTemplateIdSave";

            $(mainTemplateIdSave).val( $(mainTemplateId).val() );
            $(postTemplateIdSave).val( $(postTemplateId).val() );
            $(categoryTemplateIdSave).val( $(categoryTemplateId).val() );
            $(formId).submit();
        });
    }
</script>
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/lwwcm.css") %>" />
<link rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/bootstrap-glyphicons.css") %>" />
<form id="${n}changeChooseContent" name="changeChooseContent" method="post" action="${changeContentAction}" class="lwwcm-config-form">
    <input type="hidden" id="${n}contentType" name="contentType" />
</form>
<form id="${n}newContentAttached" name="newContentAttached" method="post" action="${newContentAttachedAction}" class="lwwcm-config-form">
    <input type="hidden" id="${n}newTypeContent" name="newTypeContent" />
    <input type="hidden" id="${n}newContentId" name="newContentId" />
</form>
<form id="${n}saveConfiguration" method="post" action="${saveConfigurationAction}" class="lwwcm-config-form">
    <input type="hidden" id="${n}mainTemplateIdSave" name="mainTemplateId" />
    <input type="hidden" id="${n}postTemplateIdSave" name="postTemplateId" />
    <input type="hidden" id="${n}categoryTemplateIdSave" name="categoryTemplateId" />
</form>
<div class="lwwcm-config">
    <div class="lwwcm-config-inner">
        <div class="lwwwcm-column-left">
            <span class="glyphicon glyphicon-th margin-right"></span> Main Template:
        </div>
        <%
            List<Template> templates = (List<Template>)renderRequest.getAttribute("templates");
            String mainTemplateId = (String)renderRequest.getAttribute("mainTemplateId");
        %>
        <div class="lwwcm-config-template lwwwcm-column-right"><select id="${n}mainTemplateId" class="lwwcm-input lwwcm-config-template-input" onchange="saveConfig${n}()">
            <option value="-1" <% if (mainTemplateId != null && mainTemplateId.equals("-1")) { %>selected <% } %>>No Template</option>
            <%
                if (templates != null) {
                    for (Template t : templates) {
            %>
            <option value="<%= t.getId()%>" <% if (mainTemplateId != null && mainTemplateId.equals(t.getId().toString())) { %>selected <% } %>><%= t.getName() %> (<%= t.getLocale() %>) [<%= t.getId()%>]</option>
            <%
                    }
                }
            %>
        </select></div>
    </div>
    <div class="lwwcm-config-inner">
        <div class="lwwwcm-column-left">
            <span class="glyphicon glyphicon-th margin-right"></span> Post Template:
        </div>
        <%
            templates = (List<Template>)renderRequest.getAttribute("templates");
            String postTemplateId = (String)renderRequest.getAttribute("postTemplateId");
        %>
        <div class="lwwcm-config-template lwwwcm-column-right"><select id="${n}postTemplateId" name="postTemplateId" class="lwwcm-input lwwcm-config-template-input" onchange="saveConfig${n}()">
            <option value="-1" <% if (postTemplateId != null && postTemplateId.equals("-1")) { %>selected <% } %>>No Template</option>
            <%
                if (templates != null) {
                    for (Template t : templates) {
            %>
            <option value="<%= t.getId()%>" <% if (postTemplateId != null && postTemplateId.equals(t.getId().toString())) { %>selected <% } %>><%= t.getName() %> (<%= t.getLocale() %>) [<%= t.getId()%>]</option>
            <%
                    }
                }
            %>
        </select></div>
    </div>
    <div class="lwwcm-config-inner">
        <div class="lwwwcm-column-left">
            <span class="glyphicon glyphicon-th margin-right"></span> Category Template:
        </div>
        <%
            templates = (List<Template>)renderRequest.getAttribute("templates");
            String categoryTemplateId = (String)renderRequest.getAttribute("categoryTemplateId");
        %>
        <div class="lwwcm-config-template lwwwcm-column-right"><select id="${n}categoryTemplateId" name="categoryTemplateId" class="lwwcm-input lwwcm-config-template-input" onchange="saveConfig${n}()">
            <option value="-1" <% if (categoryTemplateId != null && categoryTemplateId.equals("-1")) { %>selected <% } %>>No Template</option>
            <%
                if (templates != null) {
                    for (Template t : templates) {
            %>
            <option value="<%= t.getId()%>" <% if (categoryTemplateId != null && categoryTemplateId.equals(t.getId().toString())) { %>selected <% } %>><%= t.getName() %> (<%= t.getLocale() %>) [<%= t.getId()%>]</option>
            <%
                    }
                }
            %>
        </select></div>
    </div>
</div>
<div class="lwwcm-config">
    <span class="glyphicon glyphicon-file margin-right"></span> Content attached:

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
            <option value="<%= c.getId() %>"><%= ViewMetadata.categoryTitle(c) %></option>
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
</div>


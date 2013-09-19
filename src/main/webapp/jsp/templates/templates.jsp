<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ParseDates" %>
<%@ page import="org.gatein.lwwcm.domain.Template" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>
    <%@include file="templatesActions.jsp"%>

    <form id="${n}deleteTemplateForm" method="post" action="${deleteTemplateAction}">
        <input type="hidden" id="${n}deleteTemplateId" name="deleteTemplateId" />
    </form>
    <input type="hidden" id="${n}listTemplateId" name="listTemplateId" />
    <table class="lwwcm-posts" id="${n}templates">
        <%
            List<Template> listTemplates = (List<Template>)renderRequest.getAttribute("list");
            if (listTemplates != null) {
                for (Template t : listTemplates) {
        %>
        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="<%= t.getId() %>" id="${n}selectRow<%= t.getId() %>" name="" onchange="selectTemplate('${n}', '<%= t.getId() %>')" />
                    <label for="${n}selectRow<%= t.getId() %>"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-th margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="${editTemplateView}&editid=<%= t.getId() %>"><%= t.getName() %></a></div>
                    <div class="lwwcm-post-categories"><%
                        for (Category cU : t.getCategories()) {
                    %>[<a href="javascript:showFilterCategoriesById('${n}', '<%= cU.getId() %>');"><%= cU.getName() %></a> <a href="${removeCategoryTemplateAction}&catid=<%= cU.getId() %>&templateid=<%= t.getId() %>" class="lwwcm-delete-category"><span class="glyphicon glyphicon-remove middle"></span></a>] <%
                        }
                    %>
                    </div>
                    <div class="lwwcm-post-actions"><a href="${editTemplateView}&editid=<%= t.getId() %>">Edit</a> | <a href="javascript:deleteTemplate('${n}', <%= t.getId() %>)">Delete</a> | <a href="javascript:;" onclick="javascript:showSingleCategoriesTemplate('${n}', this.id, '<%= t.getId() %>');" id="${n}addCategory<%= t.getId() %>" >Category</a></div>
                </div>
            </td>
            <td class="row-author"><%= t.getUser() %></td>
            <td class="row-timestamp"><%= ParseDates.parse(t.getModified()) %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

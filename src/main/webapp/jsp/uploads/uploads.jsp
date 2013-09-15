<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ParseDates" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>
    <%@include file="uploadsActions.jsp"%>

    <form id="${n}deleteUploadForm" method="post" action="${deleteUploadAction}">
        <input type="hidden" id="${n}deleteUploadId" name="deleteUploadId" />
    </form>
    <input type="hidden" id="${n}listUploadId" name="listUploadId" />
    <table class="lwwcm-posts" id="${n}uploads">
        <%
            List<Upload> listUploads = (List<Upload>)portletSession.getAttribute("list");
            if (listUploads != null) {
                for (Upload u : listUploads) {
         %>
        <portlet:resourceURL var="downloadUploadEvent">
            <portlet:param name="event" value="<%= Wcm.EVENTS.DOWNLOAD_UPLOAD %>" />
            <portlet:param name="uploadid" value="<%= u.getId().toString() %>" />
        </portlet:resourceURL>
        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="<%= u.getId() %>" id="${n}selectRow<%= u.getId() %>" name="" onchange="selectUpload('${n}', '<%= u.getId() %>')" />
                    <label for="${n}selectRow<%= u.getId() %>"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-picture margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="${downloadUploadEvent}" target="_blank"><%= u.getFileName() %>&nbsp;<span class="lwwcm-blue"><%= (u.getDescription().equals("")?"":"(" + u.getDescription() + ")") %></span></a> <a href="${downloadUploadEvent}" target="_blank" id="${n}preview<%= u.getId() %>" <% if (u.getMimeType() != null && u.getMimeType().startsWith("image")) { %>onmouseover="showPreview('${n}', this.id, '${downloadUploadEvent}');" onmouseout="hidePreview('${n}', this.id, '${downloadUploadEvent}');" <% } %>><span class="lwwcm-upload-mimetype"><%= u.getMimeType() %></span></a> <% if (u.getMimeType() != null && u.getMimeType().startsWith("image")) { %><span class="lwwcm-upload-mimetype glyphicon glyphicon-eye-open margin-top"></span><% } %></div>
                    <div class="lwwcm-post-categories"><%
                            for (Category cU : u.getCategories()) {
                        %>[<a href="javascript:showFilterCategoriesById('${n}', '<%= cU.getId() %>');"><%= cU.getName() %></a> <a href="${removeCategoryUploadAction}&catid=<%= cU.getId() %>&uploadid=<%= u.getId() %>" class="lwwcm-delete-category"><span class="glyphicon glyphicon-remove middle"></span></a>] <%
                            }
                        %>
                    </div>
                    <div class="lwwcm-post-actions"><a href="${editUploadView}&editid=<%= u.getId() %>">Edit</a> | <a href="javascript:deleteUpload('${n}', <%= u.getId() %>)">Delete</a> | <a href="javascript:;" onclick="javascript:showSingleCategories('${n}', this.id, '<%= u.getId() %>');" id="${n}addCategory<%= u.getId() %>" >Category</a></div>
                </div>
            </td>
            <td class="row-author"><%= u.getUser() %></td>
            <td class="row-timestamp"><%= ParseDates.parse(u.getModified()) %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>
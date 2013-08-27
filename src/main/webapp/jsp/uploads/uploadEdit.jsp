<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/upload.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <%
        Upload u = (Upload) portletSession.getAttribute("edit");
        if (u != null) {
    %>
    <portlet:resourceURL var="downloadUploadEvent">
        <portlet:param name="event" value="<%= Wcm.EVENTS.DOWNLOAD_UPLOAD %>" />
        <portlet:param name="uploadid" value="<%= u.getId().toString() %>" />
    </portlet:resourceURL>

    <form id="${n}editUploadForm" method="post" enctype="multipart/form-data" action="${editUploadAction}">
        <input type="hidden" id="${n}editUploadId" name="editUploadId" value="<%= u.getId() %>"/>
        <div class="lwwcm-newupload">
            <label>Filename: </label>
            <div class="lwwcm-editupload-name"><%= u.getFileName() %></div>
            <a href="${downloadUploadEvent}" target="_blank" class="button" title="Download">&nbsp;&nbsp;Download&nbsp;&nbsp;</a>
        </div>
        <div class="lwwcm-newupload">
            <a href="javascript:showUploadFile('${n}');" class="button" title="Upload file">Upload file</a>
            <input type="file" id="${n}uploadFile" name="uploadFile" class="lwwcm-newupload-file" />
            <div class="lwwcm-newupload-name" id="${n}uploadFileName"></div>
            <a href="javascript:saveUpdateUpload('${n}');" class="button" title="Save Upload">Save Upload</a>
        </div>
        <div class="lwwcm-newupload">
            <label for="${n}uploadFileDescription">Description: </label>
            <div class="lwwcm-newupload-description"><input id="${n}uploadFileDescription" name="uploadFileDescription" class="lwwcm-input"  value="<%= u.getDescription() %>"/></div>
        </div>
    </form>
    <%
        }
    %>
</div>
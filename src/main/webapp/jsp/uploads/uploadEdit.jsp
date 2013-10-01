<%
    /*
     * JBoss, a division of Red Hat
     * Copyright 2012, Red Hat Middleware, LLC, and individual
     * contributors as indicated by the @authors tag. See the
     * copyright.txt in the distribution for a full listing of
     * individual contributors.
     *
     * This is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as
     * published by the Free Software Foundation; either version 2.1 of
     * the License, or (at your option) any later version.
     *
     * This software is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
     * Lesser General Public License for more details.
     *
     * You should have received a copy of the GNU Lesser General Public
     * License along with this software; if not, write to the Free
     * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
     * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
     */
%>
<%@ page import="org.gatein.lwwcm.domain.Upload" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/uploads/upload.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>

    <%
        Upload u = (Upload) renderRequest.getAttribute("edit");
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
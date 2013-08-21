<%@include file="imports.jsp"%>
<%@include file="urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/upload.js") %>"></script>
<div class="container">
    <%@include file="header.jsp"%>
    <%@include file="actions.jsp"%>

    <div class="lwwcm-newupload">
        <a href="javascript:showUploadFile('${n}');" class="button" title="Upload file">Upload file</a>
        <input type="file" id="${n}uploadFile" class="lwwcm-newupload-file" />
        <div class="lwwcm-newupload-name" id="${n}uploadFileName"></div>
        <a href="#" class="button" title="Save Upload">Save Upload</a>
    </div>
</div>
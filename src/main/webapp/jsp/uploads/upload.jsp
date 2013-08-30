<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/uploads/upload.js") %>"></script>
<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>

    <form id="${n}newUploadForm" method="post" enctype="multipart/form-data" action="${newUploadAction}">
        <div class="lwwcm-newupload">
            <a href="javascript:showUploadFile('${n}');" class="button" title="Upload file">Upload file</a>
            <input type="file" id="${n}uploadFile" name="uploadFile" class="lwwcm-newupload-file" />
            <div class="lwwcm-newupload-name" id="${n}uploadFileName"></div>
            <a href="javascript:saveNewUpload('${n}');" class="button" title="Save Upload">Save Upload</a>
        </div>
        <div class="lwwcm-newupload">
            <label for="${n}uploadFileDescription">Description: </label>
            <div class="lwwcm-newupload-description"><input id="${n}uploadFileDescription" name="uploadFileDescription" class="lwwcm-input" /></div>
        </div>
    </form>
</div>
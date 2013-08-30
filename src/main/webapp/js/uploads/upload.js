/* Upload functions
 ================== */

function showUploadFile(namespace) {
    var uploadId = "#" + namespace + "uploadFile";
    var uploadNameId = "#" + namespace + "uploadFileName";
    if ($._data($(uploadId)[0], "events") === undefined) {
        $(uploadId).change(function () {
           $(uploadNameId).text( $(uploadId).val() );
        });
    }
    $(uploadId).click();
}

function saveNewUpload(namespace) {
    var formId = "#" + namespace + "newUploadForm";
    var uploadId = "#" + namespace + "uploadFile";

    if ($(uploadId).val() == '') {
        showMsg(namespace, 'Upload file cannot be empty');
    } else {
        $(formId).submit();
    }
}

function saveUpdateUpload(namespace) {
    var formId = "#" + namespace + "editUploadForm";
    var uploadId = "#" + namespace + "uploadFile";

    $(formId).submit();
}


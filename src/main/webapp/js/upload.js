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


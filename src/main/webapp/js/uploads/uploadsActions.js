/* Uploads Actions functions
 =========================== */

function showSingleCategories(namespace, link, uploadId) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "uploads-categories";
        var linkid = "#" + link; // namespace included
        $(id).css('top',  $(linkid).offset().top - $(document).scrollTop());
        $(id).css('left', $(linkid).offset().left);
        $(id).fadeIn(100);

        var buttonid = "#" + namespace + "assign-single-category";
        var selectCatId = "#" + namespace + "selectAddCategory";

        var formId  = "#" + namespace + "addCategoryUploadForm";
        var formCatId = "#" + namespace + "categoryId";
        var formUploadId = "#" + namespace + "uploadId";

        $(buttonid).click(function(e) {
            e.preventDefault();
            $(formUploadId).val(uploadId);
            $(formCatId).val( $(selectCatId).val() );
            $(formId).submit();
        });

        var closeid = "#" + namespace + "close-posts-categories";
        $(closeid).click(function (e) {
            e.preventDefault();
            $(id).hide();
        });
    });
}

function showFilterCategories(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryUploadsForm";
        var filterId = "#" + namespace + "filterCategoryId";
        var selectId = "#" + namespace + "selectFilterCategory";

        $(filterId).val( $(selectId).val() );
        $(formId).submit();
    });
}

function showFilterCategoriesById(namespace, id) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryUploadsForm";
        var filterId = "#" + namespace + "filterCategoryId";

        $(filterId).val( id );
        $(formId).submit();
    });
}


function deleteUpload(namespace, uploadid) {
    require(["SHARED/jquery"], function($) {
        if (confirm("Confirm delete Upload ID [" + uploadid + "] ?")) {
            $("#" + namespace + "deleteUploadId").val(uploadid);
            $("#" + namespace + "deleteUploadForm").submit();
        }
    });
}

function selectUpload(namespace, uploadid) {
    require(["SHARED/jquery"], function($) {
        var listUploadId = "#" + namespace + "listUploadId";
        var currentList = $(listUploadId).val();

        // Check if uploadid exists
        var found = false;
        var array = currentList.split(",");
        var output = "";
        for (var i = 0; i<array.length; i++) {
            if (array[i].localeCompare(uploadid) == 0) {
                found = true;
            } else {
                output = output + array[i];
                if (output.length > 0) {
                    output = output + ",";
                }
            }
        }
        if (!found) {
            output = output + uploadid;
        } else {
            if (output.length > 0) {
                output = output.substr(0, output.length -1);
            }
        }

        $(listUploadId).val(output);
    });
}

function selectAllUploads(namespace) {
    require(["SHARED/jquery"], function($) {
        var selectAllId = "#" + namespace + "selectAll";
        var uploadsId = "#" + namespace + "uploads";
        var listUploadId = "#" + namespace + "listUploadId";
        var output = "";

        if ($(selectAllId).is(":checked")) {
            $(uploadsId).find("input").each(function() {
                $(this).prop("checked", true);
                output = output + $(this).val() + ",";
            });
        } else {
            $(uploadsId).find("input").each(function() {
                $(this).prop("checked", false)
            });
        }

        if (output.length > 0) {
            output = output.substr(0, output.length - 1);
        }
        $(listUploadId).val(output);
    });
}

function deleteSelectedUploads(namespace) {
    require(["SHARED/jquery"], function($) {
        var listUploadId = "#" + namespace + "listUploadId"
        var selectedId = "#" + namespace + "deleteSelectedListId";
        var deleteSelectedFormId = "#" + namespace + "deleteSelectedUploadForm";
        if ($(listUploadId).val().length > 0) {
            if (confirm("Confirm delete Upload Selected ID [" + $(listUploadId).val() + "] ?")) {
                $(selectedId).val( $(listUploadId).val() );
                $(deleteSelectedFormId).submit();
            }
        }
    });
}

function showSelectedCategories(namespace, link) {
    require(["SHARED/jquery"], function($) {
        var listUploadId = "#" + namespace + "listUploadId";
        if ($(listUploadId).val().length > 0) {
            // Show popup
            var id = "#" + namespace + "uploads-categories";
            var linkid = "#" + link; // namespace included
            $(id).css('top',  $(linkid).offset().top);
            $(id).css('left', $(linkid).offset().left);
            $(id).fadeIn(100);

            var buttonid = "#" + namespace + "assign-single-category";
            var selectCatId = "#" + namespace + "selectAddCategory";

            var formId  = "#" + namespace + "addSelectedCategoryUploadForm";
            var formUploadId = "#" + namespace + "addSelectedCategoryUploadListId";
            var formCatId = "#" + namespace + "addCategoryUploadId";

            $(buttonid).click(function(e) {
                e.preventDefault();
                $(formUploadId).val($(listUploadId).val());
                $(formCatId).val( $(selectCatId).val() );
                $(formId).submit();
            });

            var closeid = "#" + namespace + "close-posts-categories";
            $(closeid).click(function (e) {
                e.preventDefault();
                $(id).hide();
            });
        }
    });
}

function showFilterNameUploads(e, namespace) {
    require(["SHARED/jquery"], function($) {
        if (typeof e == 'undefined' && window.event) { e = window.event; }
        if (e.keyCode == 13) {
            var formId = "#" + namespace + "filterNameUploadsForm";
            var filterId = "#" + namespace + "filterName";
            var inputId = "#" + namespace + "inputFilterName";

            $(filterId).val( $(inputId).val() );
            $(formId).submit();
        }
    });
}

function showPreview(namespace, link, uploadId) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "uploads-preview";
        var linkid = "#" + link; // namespace included
        var img = "#" + namespace + "uploads-preview-content";
        // Top adjustement
        var top =  $(linkid).offset().top;
        var windowTop = $(window).height();
        var scrollTop = $(document).scrollTop();
        if ((top + 200) < (windowTop + scrollTop)) {
            // Correct
            top = top - scrollTop;
        } else {
            var diff = (top + 200) - (windowTop + scrollTop);
            var gap = 20;
            top = top - diff - gap - scrollTop;
        }
        $(id).css('top',  top);
        $(id).css('left', $(linkid).offset().left + 100);
        $(img).attr('src', uploadId);
        $(id).fadeIn(100);

    });
}

function hidePreview(namespace, link, uploadId) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "uploads-preview";
        $(id).fadeOut(100);
    });
}
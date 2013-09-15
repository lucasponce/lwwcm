/* Templates Actions functions
 ============================= */

function showSingleCategoriesTemplate(namespace, link, templateId) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "templates-categories";
        var linkid = "#" + link; // namespace included
        $(id).css('top',  $(linkid).offset().top - $(document).scrollTop());
        $(id).css('left', $(linkid).offset().left);
        $(id).fadeIn(100);

        var buttonid = "#" + namespace + "assign-single-category";
        var selectCatId = "#" + namespace + "selectAddCategory";

        var formId  = "#" + namespace + "addCategoryTemplateForm";
        var formCatId = "#" + namespace + "categoryId";
        var formTemplateId = "#" + namespace + "templateId";

        $(buttonid).click(function(e) {
            e.preventDefault();
            $(formTemplateId).val(templateId);
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

function showFilterCategoriesTemplates(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryTemplatesForm";
        var filterId = "#" + namespace + "filterCategoryId";
        var selectId = "#" + namespace + "selectFilterCategory";

        $(filterId).val( $(selectId).val() );
        $(formId).submit();
    });
}

function showFilterCategoriesById(namespace, id) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryTemplatesForm";
        var filterId = "#" + namespace + "filterCategoryId";

        $(filterId).val( id );
        $(formId).submit();
    });
}


function deleteTemplate(namespace, templateid) {
    require(["SHARED/jquery"], function($) {
        if (confirm("Confirm delete Template ID [" + templateid + "] ?")) {
            $("#" + namespace + "deleteTemplateId").val(templateid);
            $("#" + namespace + "deleteTemplateForm").submit();
        }
    });
}

function selectTemplate(namespace, templateid) {
    require(["SHARED/jquery"], function($) {
        var listTemplateId = "#" + namespace + "listTemplateId";
        var currentList = $(listTemplateId).val();

        // Check if uploadid exists
        var found = false;
        var array = currentList.split(",");
        var output = "";
        for (var i = 0; i<array.length; i++) {
            if (array[i].localeCompare(templateid) == 0) {
                found = true;
            } else {
                output = output + array[i];
                if (output.length > 0) {
                    output = output + ",";
                }
            }
        }
        if (!found) {
            output = output + templateid;
        } else {
            if (output.length > 0) {
                output = output.substr(0, output.length -1);
            }
        }
        $(listTemplateId).val(output);
    });
}

function selectAllTemplates(namespace) {
    require(["SHARED/jquery"], function($) {
        var selectAllId = "#" + namespace + "selectAll";
        var templatesId = "#" + namespace + "templates";
        var listTemplateId = "#" + namespace + "listTemplateId";
        var output = "";

        if ($(selectAllId).is(":checked")) {
            $(templatesId).find("input").each(function() {
                $(this).prop("checked", true);
                output = output + $(this).val() + ",";
            });
        } else {
            $(templatesId).find("input").each(function() {
                $(this).prop("checked", false)
            });
        }

        if (output.length > 0) {
            output = output.substr(0, output.length - 1);
        }
        $(listTemplateId).val(output);
    });
}

function deleteSelectedTemplates(namespace) {
    require(["SHARED/jquery"], function($) {
        var listTemplateId = "#" + namespace + "listTemplateId"
        var selectedId = "#" + namespace + "deleteSelectedListId";
        var deleteSelectedFormId = "#" + namespace + "deleteSelectedTemplateForm";
        if ($(listTemplateId).val().length > 0) {
            if (confirm("Confirm delete Template Selected ID [" + $(listTemplateId).val() + "] ?")) {
                $(selectedId).val( $(listTemplateId).val() );
                $(deleteSelectedFormId).submit();
            }
        }
    });
}

function showSelectedCategoriesTemplates(namespace, link) {
    require(["SHARED/jquery"], function($) {
        var listTemplateId = "#" + namespace + "listTemplateId";
        if ($(listTemplateId).val().length > 0) {
            // Show popup
            var id = "#" + namespace + "templates-categories";
            var linkid = "#" + link; // namespace included
            $(id).css('top',  $(linkid).offset().top);
            $(id).css('left', $(linkid).offset().left);
            $(id).fadeIn(100);

            var buttonid = "#" + namespace + "assign-single-category";
            var selectCatId = "#" + namespace + "selectAddCategory";

            var formId  = "#" + namespace + "addSelectedCategoryTemplateForm";
            var formTemplateId = "#" + namespace + "addSelectedCategoryTemplateListId";
            var formCatId = "#" + namespace + "addCategoryTemplateId";

            $(buttonid).click(function(e) {
                e.preventDefault();
                $(formTemplateId).val($(listTemplateId).val());
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

function showFilterNameTemplates(e, namespace) {
    require(["SHARED/jquery"], function($) {
        if (typeof e == 'undefined' && window.event) { e = window.event; }
        if (e.keyCode == 13) {
            var formId = "#" + namespace + "filterNameTemplatesForm";
            var filterId = "#" + namespace + "filterName";
            var inputId = "#" + namespace + "inputFilterName";

            $(filterId).val( $(inputId).val() );
            $(formId).submit();
        }
    });
}
/* Template functions
===================== */

function saveNewTemplate(namespace) {
    var formId = "#" + namespace + "newTemplateForm";
    var nameId = "#" + namespace + "templateName";
    // null validation
    if ($(nameId).val() == '' || $(nameId).val() == 'Template Name') {
        showMsg(namespace, 'Template name cannot be empty');
    } else {
        $(formId).submit();
    }
}

function saveUpdateTemplate(namespace) {
    var formId = "#" + namespace + "editTemplateForm";
    $(formId).submit();
}

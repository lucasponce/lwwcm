/* Category functions
===================== */

function showParentCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var parentId = "#" + namespace + "editCategoryParentContainer";
        var selectId = "#" + namespace + "newCategoryType";

        if ($(selectId).val() == 'Folder') {
            $(parentId).css('display', 'block');
        } else {
            $(parentId).css('display', 'none');
        }
    });
}

function saveNewCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "newCategoryForm";
        var nameId = "#" + namespace + "newCategoryName";
        // null validation
        if ($(nameId).val() == '') {
            showMsg(namespace, 'Category name cannot be empty');
        } else {
            $(formId).submit();
        }
    });
}

function saveUpdateCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "editCategoryForm";
        var nameId = "#" + namespace + "newCategoryName";
        // null validation
        if ($(nameId).val() == '') {
            showMsg(namespace, 'Category name cannot be empty');
        } else {
            $(formId).submit();
        }
    });
}
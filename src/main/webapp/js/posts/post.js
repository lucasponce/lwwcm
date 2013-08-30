/* Post functions
================= */
function saveNewPost(namespace) {
    var formId = "#" + namespace + "newPostForm";
    var titleId = "#" + namespace + "postTitle";
    var excerptId = "#" + namespace + "postExcerpt";
    // null validation
    if ($(titleId).val() == '' || $(titleId).val() == 'Post Title') {
        showMsg(namespace, 'Post title cannot be empty');
    } else {
        if ( $(excerptId).val() == 'Summary / Excerpt') {
            $(excerptId).val('');
        }
        $(formId).submit();
    }
}

function saveUpdatePost(namespace) {
    var formId = "#" + namespace + "editPostForm";
    $(formId).submit();
}

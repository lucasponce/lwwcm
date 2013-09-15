/* Posts Actions functions
========================== */

function selectPost(namespace, postid) {
    require(["SHARED/jquery"], function($) {
        var listPostId = "#" + namespace + "listPostId";
        var currentList = $(listPostId).val();

        // Check if post exists
        var found = false;
        var array = currentList.split(",");
        var output = "";
        for (var i = 0; i<array.length; i++) {
            if (array[i].localeCompare(postid) == 0) {
                found = true;
            } else {
                output = output + array[i];
                if (output.length > 0) {
                    output = output + ",";
                }
            }
        }
        if (!found) {
            output = output + postid;
        } else {
            if (output.length > 0) {
                output = output.substr(0, output.length -1);
            }
        }
        $(listPostId).val(output);
    });
}

function showFilterCategoriesById(namespace, id) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryPostsForm";
        var filterId = "#" + namespace + "filterCategoryId";

        $(filterId).val( id );
        $(formId).submit();
    });
}

function deletePost(namespace, postid) {
    require(["SHARED/jquery"], function($) {
        if (confirm("Confirm delete Post ID [" + postid + "] ?")) {
            $("#" + namespace + "deletePostId").val(postid);
            $("#" + namespace + "deletePostForm").submit();
        }
    });
}

function showSingleCategoriesPost(namespace, link, postId) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "posts-categories";
        var linkid = "#" + link; // namespace included
        $(id).css('top',  $(linkid).offset().top - $(document).scrollTop());
        $(id).css('left', $(linkid).offset().left);
        $(id).fadeIn(100);

        var buttonid = "#" + namespace + "assign-single-category";
        var selectCatId = "#" + namespace + "selectAddCategory";

        var formId  = "#" + namespace + "addCategoryPostForm";
        var formCatId = "#" + namespace + "categoryId";
        var formPostId = "#" + namespace + "postId";

        $(buttonid).click(function(e) {
            e.preventDefault();
            $(formPostId).val(postId);
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

function selectAllPosts(namespace) {
    require(["SHARED/jquery"], function($) {
        var selectAllId = "#" + namespace + "selectAll";
        var postsId = "#" + namespace + "posts";
        var listPostId = "#" + namespace + "listPostId";
        var output = "";

        if ($(selectAllId).is(":checked")) {
            $(postsId).find("input").each(function() {
                $(this).prop("checked", true);
                output = output + $(this).val() + ",";
            });
        } else {
            $(postsId).find("input").each(function() {
                $(this).prop("checked", false)
            });
        }

        if (output.length > 0) {
            output = output.substr(0, output.length - 1);
        }
        $(listPostId).val(output);
    });
}

function deleteSelectedPosts(namespace) {
    require(["SHARED/jquery"], function($) {
        var listPostId = "#" + namespace + "listPostId"
        var selectedId = "#" + namespace + "deleteSelectedListId";
        var deleteSelectedFormId = "#" + namespace + "deleteSelectedPostForm";
        if ($(listPostId).val().length > 0) {
            if (confirm("Confirm delete Post Selected ID [" + $(listPostId).val() + "] ?")) {
                $(selectedId).val( $(listPostId).val() );
                $(deleteSelectedFormId).submit();
            }
        }
    });
}

function showSelectedCategoriesPosts(namespace, link) {
    require(["SHARED/jquery"], function($) {
        var listPostId = "#" + namespace + "listPostId";
        if ($(listPostId).val().length > 0) {
            // Show popup
            var id = "#" + namespace + "posts-categories";
            var linkid = "#" + link; // namespace included
            $(id).css('top',  $(linkid).offset().top);
            $(id).css('left', $(linkid).offset().left);
            $(id).fadeIn(100);

            var buttonid = "#" + namespace + "assign-single-category";
            var selectCatId = "#" + namespace + "selectAddCategory";

            var formId  = "#" + namespace + "addSelectedCategoryPostForm";
            var formPostId = "#" + namespace + "addSelectedCategoryPostListId";
            var formCatId = "#" + namespace + "addCategoryPostId";

            $(buttonid).click(function(e) {
                e.preventDefault();
                $(formPostId).val($(listPostId).val());
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

function showFilterCategoriesPosts(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "filterCategoryPostsForm";
        var filterId = "#" + namespace + "filterCategoryId";
        var selectId = "#" + namespace + "selectFilterCategory";

        $(filterId).val( $(selectId).val() );
        $(formId).submit();
    });
}

function publishPost(namespace, postid, publishstate) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "publishPostForm";
        var postId = "#" + namespace + "publishPostId";
        var stateId = "#" + namespace + "publishState";
        $(postId).val(postid);
        $(stateId).val(publishstate);
        $(formId).submit();
    });
}

function publishPosts(namespace, publishstate) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "publishPostsForm";
        var postId = "#" + namespace + "publishPostsId";
        var stateId = "#" + namespace + "publishStates";
        var listPostId = "#" + namespace + "listPostId";

        if ($(listPostId).val().length > 0) {
            $(postId).val($(listPostId).val());
            $(stateId).val(publishstate);
            $(formId).submit();
        }
    });
}

function showFilterNamePosts(e, namespace) {
    require(["SHARED/jquery"], function($) {
        if (typeof e == 'undefined' && window.event) { e = window.event; }
        if (e.keyCode == 13) {
            var formId = "#" + namespace + "filterNamePostsForm";
            var filterId = "#" + namespace + "filterName";
            var inputId = "#" + namespace + "inputFilterName";

            $(filterId).val( $(inputId).val() );
            $(formId).submit();
        }
    });
}


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

function showSingleAclPost(namespace, link, href, postId, hrefClose) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "posts-acls";
        var linkid = "#" + link; // namespace included
        var aclId = "#" + namespace + "posts-acls-attached";
        var aclPostId = "#" + namespace + "aclPostId";
        $(id).css('top',  $(linkid).offset().top - $(document).scrollTop());
        $(id).css('left', $(linkid).offset().left);
        $(id).fadeIn(100);

        $(aclPostId).val( postId );

        $.ajax({
            type: "POST",
            url: href + "&postid=" + postId + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            success: function(data)
            {
                // Write ACL data
                $(aclId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing showSingleAclPost()");
            }
        });

        var closeid = "#" + namespace + "close-posts-acls";
        $(closeid).click(function (e) {
            e.preventDefault();
            $(id).hide();
            window.location.assign(hrefClose);
        });
    });
}

function addAcl(namespace, href) {
    var aclTypeId = "#" + namespace + "aclType";
    var aclWcmGroupId = "#" + namespace + "aclWcmGroup";
    var aclPostId = "#" + namespace + "aclPostId";
    var aclId = "#" + namespace + "posts-acls-attached";

    require(["SHARED/jquery"], function($) {

        $.ajax({
            type: "POST",
            url: href + "&aclpostid=" + $(aclPostId).val() + "&namespace=" + namespace + "&acltype=" + $(aclTypeId).val() + "&aclwcmgroup=" + $(aclWcmGroupId).val(),
            cache: false,
            dataType: "text",
            success: function(data)
            {
                // Write ACL data
                $(aclId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing addAcl()");
            }
        });

    });
}

function deleteAcl(namespace, href, aclAclId, aclPostId) {
    var aclId = "#" + namespace + "posts-acls-attached";

    require(["SHARED/jquery"], function($) {

        $.ajax({
            type: "POST",
            url: href + "&aclid=" + aclAclId + "&namespace=" + namespace + "&aclpostid=" + aclPostId,
            cache: false,
            dataType: "text",
            success: function(data)
            {
                // Write ACL data
                $(aclId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing deleteAcl()");
            }
        });

    });
}

function showCommentsPost(namespace, link, href, postId, hrefClose) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "posts-comments";
        var linkid = "#" + link; // namespace included
        var commentsId = "#" + namespace + "post-comments-list";

        // Open comments in the center
        var w = (($(window).width() - 980)/2);
        if (w < 0) w = 100;
        else w = w + 100;
        $(id).css('top',  200);    // Fixed at the beginning of
        $(id).css('left', w);
        $(id).fadeIn(100);

        $.ajax({
            type: "POST",
            url: href + "&postid=" + postId + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            success: function(data)
            {
                // Write Comment data
                $(commentsId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing showCommentsPost()");
            }
        });

        var closeid = "#" + namespace + "close-posts-comments";
        $(closeid).click(function (e) {
            e.preventDefault();
            $(id).hide();
            window.location.assign(hrefClose);
        });
    });
}

function addComment(namespace, href, postId) {
    require(["SHARED/jquery"], function($) {
        var newCommentId = "#" + namespace + "newComment";
        var commentsId = "#" + namespace + "post-comments-list";

        $.ajax({
            type: "POST",
            url: href + "&postid=" + postId + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            data: { comment: $(newCommentId).val() },
            success: function(data)
            {
                // Write Comment data
                $(commentsId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing addComment()");
            }
        });

    });
}

function changeStatusComment(namespace, href, postId, commentId, status) {
    require(["SHARED/jquery"], function($) {
        var commentsId = "#" + namespace + "post-comments-list";
        $.ajax({
            type: "POST",
            url: href + "&postid=" + postId + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            data: { commentId: commentId, status: status },
            success: function(data)
            {
                // Write Comment data
                $(commentsId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing changeStatusComment()");
            }
        });
    });
}

function changeCommentsPost(namespace, href, postId) {
    require(["SHARED/jquery"], function($) {
        var postCommentsStatusId = "#" + namespace + "postCommentsStatus";
        var commentsId = "#" + namespace + "post-comments-list";
        $.ajax({
            type: "POST",
            url: href + "&postid=" + postId + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            data: { postCommentsStatus: $(postCommentsStatusId).val() },
            success: function(data)
            {
                // Write Comment data
                $(commentsId).html(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing changeCommentsPost()");
            }
        });
    });
}



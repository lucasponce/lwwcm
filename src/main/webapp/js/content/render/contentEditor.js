/* Content Editor functions
=========================== */

function showSelectUploadsPost(namespace, editor) {
    require(["SHARED/jquery"], function($) {
        // Show popup
        var id = "#" + namespace + "post-select-upload";
        // var linkid = "#cke_" + namespace + "templateContent > div.cke_inner"; // cke_ prefix is added automatically for CKEditor
        var uploadsid = "#" + namespace + "listUploads";
        var href= $("#" + namespace + "urlShowPostUploadsEvent").val();;
        var selecfilterid = "#" + namespace + "selectFilterCategory";
        var inputfilterid = "#" + namespace + "inputFilterName";

        $(inputfilterid).val('Filter By Name');
        $(selecfilterid)[0].selectedIndex = 0;

        // Events
        $(selecfilterid).change(function () {
            filterCategoryUploadsPost(namespace, editor);
        });
        $(inputfilterid).keypress(function(event) {
            if (event.which == 13) {
                filterNameUploadsPost(namespace, editor);
            }
        });

        $.ajax({
            type: "POST",
            url: href + "&namespace=" + namespace,
            cache: false,
            dataType: "text",
            success: function(data)
            {
                $(uploadsid).unbind();
                $(uploadsid + " *").unbind();
                // Write uploads list
                $(uploadsid).html(data);
                $(uploadsid).one("click", "li", function(event) {
                    if ($(this).data("mimetype").indexOf("image") == 0) {
                        editor.insertHtml( "<img src='/lwwcm/rs/u/" + $(this).data("id") + "' > " );
                    } else {
                        editor.insertHtml("<a href='/lwwcm/rs/u/" + $(this).data("id") + "' target='_blank'>" + $(this).data("filename") + "</a>");
                    }
                    $(id).hide();
                    $(selecfilterid).unbind();
                    $(inputfilterid).unbind();
                    $(uploadsid).unbind();
                    $(uploadsid + " *").unbind();
                });
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing showSelectUploadsPost()");
            }
        });

        // floating toolsbar editor is handle via .cke_focus
        var ckeFocus = $(".cke_focus").first().offset();
        $(id).css('top',  ckeFocus.top);
        $(id).css('left', ckeFocus.left);
        $(id).css('z-index', 19999);
        $(id).fadeIn(100);

        var closeid = "#" + namespace + "close-post-select-upload";
        $(closeid).click(function (e) {
            e.preventDefault();
            $(id).hide();
            $(uploadsid).unbind();
            $(uploadsid + " *").unbind();
            $(selecfilterid).unbind();
            $(inputfilterid).unbind('keypress');
        });
    });
}

function filterCategoryUploadsPost(namespace, editor) {
    require(["SHARED/jquery"], function($) {
        var id = "#" + namespace + "post-select-upload";
        var filterselectid = "#" + namespace + "selectFilterCategory";
        var filterinputid = "#" + namespace + "inputFilterName";
        var href= $("#" + namespace + "urlShowPostUploadsEvent").val();
        var uploadsid = "#" + namespace + "listUploads";

        $(filterinputid).val('Filter By Name');

        console.log("filterCategoryUploadsPost() value: " + $(filterselectid).val());
        $.ajax({
            type: "POST",
            url: href + "&namespace=" + namespace + "&filterCategoryId=" + $(filterselectid).val(),
            cache: false,
            dataType: "text",
            success: function(data)
            {
                $(uploadsid).unbind();
                $(uploadsid + " *").unbind();
                // Write uploads list
                $(uploadsid).html(data);
                $(uploadsid).one("click", "li", function(event) {
                    if ($(this).data("mimetype").indexOf("image") == 0) {
                        editor.insertHtml( "<img src='/lwwcm/rs/u/" + $(this).data("id") + "' > " );
                    } else {
                        editor.insertHtml("<a href='/lwwcm/rs/u/" + $(this).data("id") + "' target='_blank'>" + $(this).data("filename") + "</a>");
                    }
                    $(id).hide();
                    $(filterselectid).unbind();
                    $(filterinputid).unbind();
                    $(uploadsid).unbind();
                    $(uploadsid + " *").unbind();
                });
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing showSelectUploadsPost()");
            }
        });

    });
}

function filterNameUploadsPost(namespace, editor) {
    require(["SHARED/jquery"], function($) {
        var id = "#" + namespace + "post-select-upload";
        var filterinputid = "#" + namespace + "inputFilterName";
        var filterselectid = "#" + namespace + "selectFilterCategory";
        var href= $("#" + namespace + "urlShowPostUploadsEvent").val();
        var uploadsid = "#" + namespace + "listUploads";

        $(filterselectid)[0].selectedIndex = 0;

        console.log("filterNameUploadsPost() value: " + $(filterinputid).val());
        $.ajax({
            type: "POST",
            url: href + "&namespace=" + namespace + "&filterName=" + $(filterinputid).val(),
            cache: false,
            dataType: "text",
            success: function(data)
            {
                $(uploadsid).unbind();
                $(uploadsid + " *").unbind();
                // Write uploads list
                $(uploadsid).html(data);
                $(uploadsid).one("click", "li", function(event) {
                    if ($(this).data("mimetype").indexOf("image") == 0) {
                        editor.insertHtml( "<img src='/lwwcm/rs/u/" + $(this).data("id") + "' > " );
                    } else {
                        editor.insertHtml("<a href='/lwwcm/rs/u/" + $(this).data("id") + "' target='_blank'>" + $(this).data("filename") + "</a>");
                    }
                    $(id).hide();
                    $(filterselectid).unbind();
                    $(filterinputid).unbind();
                    $(uploadsid).unbind();
                    $(uploadsid + " *").unbind();
                });
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing filterNameUploadsPost()");
            }
        });

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
        // Hide popup
        var id = "#" + namespace + "uploads-preview";
        $(id).fadeOut(100);
    });
}

function preSave(namespace, editor) {
    require(["SHARED/jquery"], function($) {
        var originalContentId = "#" + namespace + "originalContent";
        var selected = $(".cke_focus");
        var postId = "#" + namespace + "postId";
        var contentTypeId = "#" + namespace + "contentType";
        $(postId).val(selected.attr("data-post-id"));
        $(contentTypeId).val(selected.attr("data-post-attr"));
        $(originalContentId).val(editor.getData());
    });
}

function postSave(namespace, editor) {
    require(["SHARED/jquery"], function($) {
        var originalContentId = "#" + namespace + "originalContent";
        var newData = editor.getData();
        if ($(originalContentId).val() != newData) {
            var hrefId = "#" + namespace + "urlUpdateContentEvent";
            var postId = "#" + namespace + "postId";
            var contentTypeId = "#" + namespace + "contentType";

            $.ajax({
                type: "POST",
                url: $(hrefId).val() + "&postId=" + $(postId).val() + "&contentType=" + $(contentTypeId).val(),
                cache: false,
                data: {
                    'original' : $(originalContentId).val(),
                    'newData' : editor.getData()
                },
                dataType: "text",
                success: function(data)
                {

                },
                error: function(XMLHttpRequest, textStatus, errorThrown)
                {
                    alert("Problem saving post in postSave()");
                }
            });
        }

    });
}
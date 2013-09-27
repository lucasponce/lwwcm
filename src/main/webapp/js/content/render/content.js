function lwwcmAddComment(namespace, postid) {
    require(["SHARED/jquery"], function($) {
        var href = $("#" + namespace + "-addCommentPost").val();
        var content = "";
        if ($("#" + namespace + "-content").length > 0) {
            content = $("#" + namespace + "-content").val();
        }
        var author = "";
        if ($("#" + namespace + "-author").length > 0) {
            author = $("#" + namespace + "-author").val();
        }
        var email = "";
        if ($("#" + namespace + "-email").length > 0) {
            email = $("#" + namespace + "-email").val();
        }
        var url = "";
        if ($("#" + namespace + "-url").length > 0) {
            url = $("#" + namespace + "-url").val();
        }
        $.ajax({
            type: "POST",
            url: href + "&postid=" + postid,
            cache: false,
            dataType: "text",
            data: {
                content: content,
                author: author,
                email: email,
                url: url
            },
            success: function(data)
            {
                location.reload();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
                alert("Problem accessing lwwcmAddComment()");
            }
        });
    });
}
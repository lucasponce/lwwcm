/* Posts Actions functions
========================== */

function deleteSelected(namespace) {
    $("#" + namespace + "event").val("deleteSelected");
    $("#" + namespace + "selected").val("test");
    $("#" + namespace + "postsActionsForm").submit();
}

function showCategories(namespace, href) {
    $.ajax({
        type: "POST",
        url: href,
        cache: false,
        dataType: "text",
        success: function(data)
        {
            // Write data into the float dialog
            // alert("showCategories() OK !");
            var idtarget = "#" + namespace + "posts-categories-list";
            $(idtarget).html(data);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
            alert("showCategories() KO !!");
        }
    });

    // Show popup
    var id = "#" + namespace + "posts-categories";
    var linkid = "#" + namespace + "assign-category";
    $(id).css('top',  $(linkid).offset().top);
    $(id).css('left', $(linkid).offset().left);
    $(id).fadeIn(100);

    var closeid = "#" + namespace + "close-posts-categories";
    $(closeid).click(function (e) {
        e.preventDefault();
        $(id).hide();
    });

}


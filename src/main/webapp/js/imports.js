/* Imports functions
==================== */

function showMsg(namespace, msg) {
    var id = "#" + namespace + "msg-dialog";
    var maskid = "#" + namespace + "msg-mask";
    var closeid = "#" + namespace + "close-msg-dialog";
    var msgId = "#" + namespace + "msg-dialog-body";

    // Setting msg
    $(msgId).html(msg);
    // Setting mask
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();
    $(maskid).css({'top':0, 'left': 0, 'width': maskWidth, 'height': maskHeight});
    $(maskid).fadeTo("fast", 0.3);
    // Setting dialog
    var winH = $(window).height();
    var winW = $(window).width();
    // $(id).css('top',  winH/2-$(id).height()/2);
    $(id).css('left', winW/2-$(id).width()/2);
    $(id).show();
    // Setting close button
    $(closeid).click(function (e) {
        e.preventDefault();
        $(maskid).hide();
        $(id).hide();
    });
}



function navigation(listid, navigationid) {
    // To use GateIn's jquery library
    require(["SHARED/jquery"], function($) {
        var list = "#" + listid;
        var nav = "#" + navigationid;
        var size = $(list + " li").size();
        $(nav).append('<ol>').addClass('mockup-nav-list');
        for (var i=0; i<size; i++) {
            $(nav + " ol").append('<li>');
            if (i==0) {
                $(nav + " li").addClass('mockup-active');
            }
        }
        $(nav).delegate('li', 'click', function () {
            var index = $(this).index();
            for (var i=0; i<size; i++) {
                var li = $(list + " li").eq(i);
                var linav = $(nav + " li").eq(i);
                if (index == i) {
                    li.css({'display':'block'});
                    linav.addClass('mockup-active');
                } else {
                    li.css({'display':'none'});
                    linav.removeClass('mockup-active');
                }
            }
        });
    });
}
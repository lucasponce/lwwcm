<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>
    <%@include file="postsActions.jsp"%>

    <table class="lwwcm-posts" id="${n}_posts">
        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="1" id="selectRow1" name="" />
                    <label for="selectRow1"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-file margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="#">Test Post with a long name</a></div>
                    <div class="lwwcm-post-categories">[<a href="#">Category1</a>, <a href="#">Category2</a>, <a href="#">Category3</a>]</div>
                    <div class="lwwcm-post-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Category</a> | <a href="#">Comments(0)</a></div>
                </div>
            </td>
            <td class="row-author">Test Author</td>
            <td class="row-status"><span class="glyphicon glyphicon-thumbs-down lwwcm-red middle"> Draft</span> </td>
            <td class="row-timestamp">00:00:00</td>
        </tr>

        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="2" id="selectRow2" name="" />
                    <label for="selectRow2"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-file margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="#">Test Post with a long name</a></div>
                    <div class="lwwcm-post-categories">[<a href="#">Category1</a>, <a href="#">Category2</a>, <a href="#">Category3</a>]</div>
                    <div class="lwwcm-post-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Category</a> | <a href="#">Comments(0)</a></div>
                </div>
            </td>
            <td class="row-author">Test author</td>
            <td class="row-status"><span class="glyphicon glyphicon-thumbs-up lwwcm-green middle"> Published</span> </td>
            <td class="row-timestamp">00:00:00</td>
        </tr>
    </table>

</div>

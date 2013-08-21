<%@include file="imports.jsp"%>
<%@include file="urls.jsp"%>

<div class="container">
    <%@include file="header.jsp"%>
    <%@include file="actions.jsp"%>
    <%@include file="templatesActions.jsp"%>

    <table class="lwwcm-posts" id="${n}_templates">
        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="1" id="selectRow1" name="" />
                    <label for="selectRow1"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-th margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="#">Template Single Content (es)</a></div>
                    <div class="lwwcm-post-categories">[<a href="#">Category1</a>, <a href="#">Category2</a>, <a href="#">Category3</a>]</div>
                    <div class="lwwcm-post-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Category</a></div>
                </div>
            </td>
            <td class="row-author">Test Author</td>
            <td class="row-timestamp">00:00:00</td>
        </tr>

        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="1" id="selectRow2" name="" />
                    <label for="selectRow2"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-th margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="#">Template List Content (en)</a></div>
                    <div class="lwwcm-post-categories">[<a href="#">Category1</a>, <a href="#">Category2</a>, <a href="#">Category3</a>]</div>
                    <div class="lwwcm-post-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Category</a></div>
                </div>
            </td>
            <td class="row-author">Test Author</td>
            <td class="row-timestamp">00:00:00</td>
        </tr>
    </table>
</div>
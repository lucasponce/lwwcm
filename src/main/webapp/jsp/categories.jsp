<%@include file="imports.jsp"%>
<%@include file="urls.jsp"%>

<div class="container">
    <%@include file="header.jsp"%>
    <%@include file="actions.jsp"%>
    <ul class="lwwcm-categories">
        <li>
            <div>
                <div class="lwwcm-category-title"><span class="glyphicon glyphicon-bookmark margin-right lwwcm-green"></span> Category 1 <span class="lwwcm-category-type">(Category)</span></div>
                <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Show Children(0)</a></div>
            </div>
        </li>
        <li>
            <div>
                <div class="lwwcm-category-title"><span class="glyphicon glyphicon-folder-open margin-right lwwcm-blue"></span> Category 2 <span class="lwwcm-category-type">(Folder)</span></div>
                <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Hide Children(0)</a></div>
            </div>
            <ul class="lwwcm-categories">
                <li>
                    <div>
                        <div class="lwwcm-category-title"><span class="glyphicon glyphicon-bookmark margin-right lwwcm-green"></span> Category 1 <span class="lwwcm-category-type">(Category)</span></div>
                        <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Show Children(0)</a></div>
                    </div>
                </li>
                <li>
                    <div>
                        <div class="lwwcm-category-title"><span class="glyphicon glyphicon-folder-open margin-right lwwcm-blue"></span> Category 2 <span class="lwwcm-category-type">(Folder)</span></div>
                        <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Show Children(0)</a></div>
                    </div>
                </li>
                <li>
                    <div>
                        <div class="lwwcm-category-title"><span class="glyphicon glyphicon-tag margin-right lwwcm-red"></span> Category 3 <span class="lwwcm-category-type">(Tag)</span></div>
                        <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Show Children(0)</a></div>
                    </div>
                </li>
            </ul>
        </li>
        <li>
            <div>
                <div class="lwwcm-category-title"><span class="glyphicon glyphicon-tag margin-right lwwcm-red"></span> Category 3 <span class="lwwcm-category-type">(Tag)</span></div>
                <div class="lwwcm-category-actions"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Show Children(0)</a></div>
            </div>
        </li>
    </ul>
</div>
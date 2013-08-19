<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/postsActions.js") %>"></script>
<div class="lwwcm-posts-actions">
    <div class="lwwcm-checkbox left">
        <input type="checkbox" value="1" id="selectAll" name="" />
        <label for="selectAll" title="Select All"></label>
    </div>

    <div class="left margin-left">
        <a href="javascript:deleteSelected('${n}');" class="button" title="Delete selected"><span class="glyphicon glyphicon-trash"></span></a>
    </div>

    <div class="margin-center">
        <a href="javascript:showCategories('${n}', '${showCategories}')" class="button" title="Assign Category" id="${n}assign-category"><span class="glyphicon glyphicon-tag"></span></a>
        <a href="javascript:showMsg('${n}')" class="button" title="Publish"><span class="glyphicon glyphicon-thumbs-up"></span></a>
        <a href="#" class="button" title="Draft"><span class="glyphicon glyphicon-thumbs-down"></span></a>
    </div>

    <div class="lwwcm-select margin-left">
        <select id="selectCategory" class="lwwcm-input">
            <option value="Category 1">Category 1</option>
            <option value="Category 2">Category 2</option>
            <option value="Category 3">Category 3</option>
        </select>
    </div>

    <a href="#" class="button right" title="Older entries"><span class="glyphicon glyphicon-chevron-right"></span></a>
    <a href="#" class="button right" title="Newer entries"><span class="glyphicon glyphicon-chevron-left"></span></a>
    <div class="lwwcm-pagination right">1-2 of 2</div>
</div>
<form id="${n}postsActionsForm" method="post" action="${postsActions}">
    <input type="hidden" id="${n}event" name="event" />
    <input type="hidden" id="${n}selected" name="selected" />
    <input type="hidden" id="${n}page" name="page" />
</form>
<div id="${n}posts-categories" class="lwwcm-posts-categories lwwcm-dialog">
    <div class="lwwcm-dialog-title">Select categories to Add</div>
    <a href="#" id="${n}close-posts-categories" class="lwwcm-dialog-close"><span> </span></a>
    <div id="${n}posts-categories-list" class="lwwcm-dialog-body">

    </div>
</div>
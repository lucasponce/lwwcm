<%
    /*
     * JBoss, a division of Red Hat
     * Copyright 2012, Red Hat Middleware, LLC, and individual
     * contributors as indicated by the @authors tag. See the
     * copyright.txt in the distribution for a full listing of
     * individual contributors.
     *
     * This is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as
     * published by the Free Software Foundation; either version 2.1 of
     * the License, or (at your option) any later version.
     *
     * This software is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
     * Lesser General Public License for more details.
     *
     * You should have received a copy of the GNU Lesser General Public
     * License along with this software; if not, write to the Free
     * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
     * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
     */
%>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/posts/post.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>
    <%
        List<Category> listCategories = (List<Category>)request.getAttribute("categories");
    %>
    <div id="${n}uploads-preview" class="lwwcm-upload-preview">
        <img id="${n}uploads-preview-content" class="lwwcm-upload-image" src="" />
    </div>
    <div id="${n}post-select-upload" class="lwwcm-popup-categories lwwcm-dialog">
        <div class="lwwcm-dialog-title">Select upload to Add</div>
        <a href="#" id="${n}close-post-select-upload" class="lwwcm-dialog-close"><span> </span></a>
        <div class="lwwcm-dialog-body">
            <div class="lwwcm-select left">
                <select id="${n}selectFilterCategory" class="lwwcm-input">
                    <option value="-1">All categories</option>
                    <%
                        if (listCategories != null) {
                            for (Category c : listCategories) {
                    %>
                    <option value="<%= c.getId()%>"><%= ViewMetadata.categoryTitle(c) %> </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="lwwcm-post-filtername right">
                <%
                    String filterName = "Filter By Name";
                %>
                <input id="${n}inputFilterName" class="lwwcm-input" value="<%= filterName %>" onfocus="if (this.value == 'Filter By Name') this.value=''" onblur="if (this.value == '') this.value='Filter By Name'" />
            </div>
            <div class="clear"></div>
            <div class="lwwcm-post-uploads" id="${n}listUploads">
            </div>
        </div>
    </div>

    <form id="${n}newPostForm" method="post" action="${newPostAction}">
    <div class="lwwcm-newpost-title"><input id="${n}postTitle" name="postTitle" class="lwwcm-input" value="Post Title" onfocus="if (this.value == 'Post Title') this.value=''" onblur="if (this.value == '') this.value='Post Title'"/></div>
    <div class="lwwcm-newpost-title"><textarea id="${n}postExcerpt" name="postExcerpt" class="lwwcm-input" onfocus="if (this.value == 'Summary / Excerpt') this.value=''" onblur="if (this.value == '') this.value='Summary / Excerpt'">Summary / Excerpt</textarea></div>
    <div class="lwwcm-newtemplate">
        <span class="glyphicon glyphicon-globe margin-right margin-top"></span>
        Locale: <div class="lwwcm-newtemplate-locale"><input id="${n}postLocale" name="postLocale" class="lwwcm-input" value="<%= renderRequest.getLocale().getLanguage() %>"/></div>
        <span class="glyphicon glyphicon-comment margin-right margin-top"></span>
        Comments: <div class="lwwcm-newpost-comments">
                    <select id="${n}postCommentsStatus" name="postCommentsStatus" class="lwwcm-input">
                        <option value="<%= Wcm.COMMENTS.ANONYMOUS%>">Anonymous</option>
                        <option value="<%= Wcm.COMMENTS.LOGGED%>">Logged</option>
                        <option value="<%= Wcm.COMMENTS.NO_COMMENTS%>">No Comments</option>
                    </select>
                  </div>
        <span class="glyphicon glyphicon-share margin-right margin-top"></span>
        Status:  <div class="lwwcm-newpost-status">
                    <select id="${n}postStatus" name="postStatus" class="lwwcm-input">
                        <option value="<%= Wcm.POSTS.DRAFT %>">Draft</option>
                        <option value="<%= Wcm.POSTS.PUBLISHED %>">Published</option>
                    </select>
                 </div>
        <a href="javascript:saveNewPost('${n}');" class="button" title="Save Post">Save Post</a>
    </div>
    <script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/ckeditor/ckeditor.js") %>"></script>
    <input id="${n}urlShowPostUploadsEvent" type="hidden" value="${showPostUploadsEvent}" />
    <script>
        CKEDITOR.on( 'instanceCreated', function( event ) {
            var editor = event.editor;
            editor.portalnamespace='${n}';
            editor.on( 'configLoaded', function() {
                editor.config.removePlugins = 'stylescombo';
            });
        });
    </script>
    <textarea class="ckeditor" id="${n}postContent" name="postContent"></textarea>
    </form>
</div>
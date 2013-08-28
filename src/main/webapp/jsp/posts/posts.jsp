<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ParseDates" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<div class="container">
    <%@include file="../header.jsp"%>
    <%@include file="../actions.jsp"%>
    <%@include file="postsActions.jsp"%>

    <form id="${n}deletePostForm" method="post" action="${deletePostAction}">
        <input type="hidden" id="${n}deletePostId" name="deletePostId" />
    </form>
    <input type="hidden" id="${n}listPostId" name="listPostId" />
    <form id="${n}publishPostForm" method="post" action="${publishPostAction}">
        <input type="hidden" id="${n}publishPostId" name="publishPostId" />
        <input type="hidden" id="${n}publishState" name="publishState" />
    </form>
    <table class="lwwcm-posts" id="${n}posts">
        <%
            List<Post> listPosts = (List<Post>)portletSession.getAttribute("list");
            if (listPosts != null) {
                for (Post p : listPosts) {
        %>
        <tr class="row-post">
            <td class="row-checkbox">
                <div class="lwwcm-checkbox margin-left">
                    <input type="checkbox" value="<%= p.getId() %>" id="${n}selectRow<%= p.getId() %>" name="" onchange="selectPost('${n}', '<%= p.getId() %>')" />
                    <label for="${n}selectRow<%= p.getId() %>"></label>
                </div>
            </td>
            <td class="row-type"><span class="glyphicon glyphicon-file margin-right"></span></td>
            <td>
                <div>
                    <div class="lwwcm-post-title"><a href="${editPostView}&editid=<%= p.getId() %>"><%= p.getTitle() %> [<%= p.getId() %>]</a></div>
                    <div class="lwwcm-post-categories"><%
                        for (Category cU : p.getCategories()) {
                    %>[<a href="javascript:showFilterCategoriesById('${n}', '<%= cU.getId() %>');"><%= cU.getName() %></a> <a href="${removeCategoryPostAction}&catid=<%= cU.getId() %>&postid=<%= p.getId() %>" class="lwwcm-delete-category"><span class="glyphicon glyphicon-remove middle"></span></a>] <%
                        }
                    %>
                    </div>
                    <div class="lwwcm-post-actions"><a href="${editPostView}&editid=<%= p.getId() %>">Edit</a> | <a href="javascript:deletePost('${n}', <%= p.getId() %>)">Delete</a> | <a href="#" onclick="javascript:showSingleCategoriesPost('${n}', this.id, '<%= p.getId() %>');" id="${n}addCategory<%= p.getId() %>">Category</a> | <a href="#">Comments(0)</a></div>
                </div>
            </td>
            <td class="row-author"><%= p.getAuthor() %></td>
            <%
                String statusColor = "red";
                String status = "Draft";
                String statusThumb = "down";
                String nextStatus = Wcm.POSTS.PUBLISHED.toString();
                if (p.getPostStatus().equals(Wcm.POSTS.PUBLISHED)) {
                    statusColor = "green";
                    status = "Published";
                    statusThumb = "up";
                    nextStatus = Wcm.POSTS.DRAFT.toString();
                }
            %>
            <td class="row-status"><a href="javascript:publishPost('${n}', '<%= p.getId() %>', '<%= nextStatus %>');"><span class="glyphicon glyphicon-thumbs-<%= statusThumb %> lwwcm-<%=statusColor %> middle"> <%= status %></span></a> </td>
            <td class="row-timestamp"><%= ParseDates.parse(p.getCreated()) %></td>
        </tr>
        <%
                }
            }
        %>
    </table>

</div>

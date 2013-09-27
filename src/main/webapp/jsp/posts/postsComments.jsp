<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@ page import="org.gatein.lwwcm.domain.Post" %>
<%@ page import="org.gatein.lwwcm.domain.Comment" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ParseDates" %>
<%@ page import="org.gatein.lwwcm.domain.UserWcm" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@include file="../urls.jsp"%>
<%
    String n = (String)request.getAttribute("namespace");
    Post p = (Post)request.getAttribute("post");
    UserWcm userWcm = (UserWcm)request.getAttribute("userWcm");

    boolean canWrite = userWcm.canWrite(p);

    if (p != null) {
%>
<div class="lwwcm-comments" >
    <ul>
    <%
        if (p.getComments() != null) {
            for (Comment c : p.getComments()) {
    %>
        <li>
            <%
                String statusClass = "";
                if (c.getStatus().equals(Wcm.COMMENT.DELETED)) {
                    statusClass = "lwwcm-red";
                } else if (c.getStatus().equals(Wcm.COMMENT.REJECTED)) {
                    statusClass = "lwwcm-orange";
                }
            %>
            <div class="lwwcm-comment-a"><span class="glyphicon glyphicon-comment <%= statusClass %>"></span></div>
            <div class="lwwcm-comment-b <%= statusClass %>"><%= c.getContent() %></div>
            <div class="lwwcm-comment-c">
                <div class="lwwcm-comment-date"><%= ParseDates.parse(c.getCreated()) %></div>
                <div class="lwwcm-comment-user"><span class="glyphicon glyphicon-user"></span> <%= c.getAuthor() %></div>
                <%
                    if (canWrite) {
                %>
                <div class="lwwcm-comment-actions">
                    <%
                        if (c.getStatus().equals(Wcm.COMMENT.PUBLIC)) {
                    %>
                    <span class="glyphicon glyphicon-eye-close margin-top"></span> <a href="javascript:;" onclick="changeStatusComment('<%= n %>', '<%= changeStatusCommentPostEvent%>', '<%= p.getId() %>', '<%= c.getId()%>', '<%= Wcm.COMMENT.REJECTED %>');">Reject</a>
                    <%
                        }
                    %>
                    <%
                        if (c.getStatus().equals(Wcm.COMMENT.REJECTED) || c.getStatus().equals(Wcm.COMMENT.DELETED)) {
                    %>
                    <span class="glyphicon glyphicon-eye-open margin-top"></span> <a href="javascript:;" onclick="changeStatusComment('<%= n %>', '<%= changeStatusCommentPostEvent%>', '<%= p.getId() %>', '<%= c.getId()%>', '<%= Wcm.COMMENT.PUBLIC %>');">Public</a>
                    <%
                        }
                    %>
                    <%
                        if (!c.getStatus().equals(Wcm.COMMENT.DELETED)) {
                    %>
                    <span class="glyphicon glyphicon-remove margin-top"></span> <a href="javascript:;" onclick="changeStatusComment('<%= n %>', '<%= changeStatusCommentPostEvent%>', '<%= p.getId() %>', '<%= c.getId()%>', '<%= Wcm.COMMENT.DELETED %>');">Delete</a>
                    <%
                        }
                    %>
                </div>
                <%
                    }
                %>
            </div>
            <div class="clear"></div>
        </li>
    <%
            }
        }
    %>
    </ul>
</div>
<%
        boolean canComment = false;
        if (p.getCommentsStatus().equals(Wcm.COMMENTS.ANONYMOUS)) canComment = true;
        if (p.getCommentsStatus().equals(Wcm.COMMENTS.LOGGED) && !userWcm.getUsername().equals("anonymous")) canComment = true;
%>
<div class="lwwcm-newcomment">
    <% if (canComment) { %>
    <div class="lwwcm-comment-a">
        <span class="glyphicon glyphicon-comment"></span>
    </div>
    <div class="lwwcm-comment-b">
        <textarea id="<%= n %>newComment" name="newComment" class="lwwcm-input lwwcm-newcomment-comment" onfocus="if (this.value == 'New Comment') this.value=''" onblur="if (this.value == '') this.value='New Comment'">New Comment</textarea>

    </div>
    <% } %>
    <div class="lwwcm-comment-c">
        <% if (!p.getCommentsStatus().equals(Wcm.COMMENTS.NO_COMMENTS)) { %>
        <a href="javascript:;" onclick="addComment('<%= n %>', '<%= addCommentPostEvent%>', '<%= p.getId() %>');" id="<%= n %>addCommentButton" class="button" title="Add Comment"><span class="glyphicon glyphicon-comment"></span></a>
        <% } %>
        <% if (canWrite) { %>
        <div class="lwwcm-newtemplate-type lwwcm-comment-type">
            <select id="<%= n %>postCommentsStatus" name="postCommentsStatus" class="lwwcm-input lwwcm-comment-select" onchange="changeCommentsPost('<%= n %>', '<%= changeCommentsPostEvent%>', '<%= p.getId() %>');">
                <option value="<%= Wcm.COMMENTS.ANONYMOUS%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.ANONYMOUS)) { %>selected <% } %> >Anonymous</option>
                <option value="<%= Wcm.COMMENTS.LOGGED%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.LOGGED)) { %>selected <% } %>>Logged</option>
                <option value="<%= Wcm.COMMENTS.NO_COMMENTS%>" <% if (p.getCommentsStatus().equals(Wcm.COMMENTS.NO_COMMENTS)) { %>selected <% } %>>No Comments</option>
            </select>
        </div>
        <% } %>
    </div>
    <div class="clear"></div>
</div>
<%
    }
%>
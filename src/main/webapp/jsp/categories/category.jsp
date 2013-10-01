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
<%@ page import="java.util.List" %>
<%@ page import="org.gatein.lwwcm.domain.Category" %>
<%@ page import="org.gatein.lwwcm.portlet.util.ViewMetadata" %>
<%@include file="../imports.jsp"%>
<%@include file="../urls.jsp"%>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/categories/category.js") %>"></script>
<div class="container">
    <%@include file="../menu.jsp"%>
    <%@include file="../submenu.jsp"%>

    <form id="${n}newCategoryForm" method="post" action="${newCategoryAction}">
        <div class="lwwcm-newcategory">
                <label for="${n}newCategoryName">Category name: </label>
                <div class="lwwcm-newcategory-name"><input id="${n}newCategoryName" name="newCategoryName" class="lwwcm-input" /></div>
                <label for="${n}newCategoryType">Category type: </label>
                <div class="lwwcm-newcategory-type"><select id="${n}newCategoryType" name="newCategoryType" class="lwwcm-input">
                    <option value="Category">Category</option>
                    <option value="Folder">Folder</option>
                    <option value="Tag">Tag</option>
                </select></div>
                <a href="javascript:saveNewCategory('${n}');" class="button" title="Save Category">Save Category</a>
        </div>
        <div class="lwwcm-newcategory-parent" id="${n}editCategoryParentContainer">
            <label for="${n}newCategoryParent">Category parent: </label>
            <div class="lwwcm-newcategory-type">
                <select id="${n}newCategoryParent" name="newCategoryParent" class="lwwcm-input">
                    <option value="-1">Root (no parent)</option>
                    <%
                        List<Category> list = (List<Category>)renderRequest.getAttribute("categories");
                        if (list != null) {
                            for (Category cat : list) {
                    %>
                    <option value="<%= cat.getId() %>"><%= ViewMetadata.categoryTitle(cat) %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
        </div>
    </form>
</div>
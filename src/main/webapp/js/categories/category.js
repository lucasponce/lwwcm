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

/* Category functions
===================== */

function showParentCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var parentId = "#" + namespace + "editCategoryParentContainer";
        var selectId = "#" + namespace + "newCategoryType";

        if ($(selectId).val() == 'Folder') {
            $(parentId).css('display', 'block');
        } else {
            $(parentId).css('display', 'none');
        }
    });
}

function saveNewCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "newCategoryForm";
        var nameId = "#" + namespace + "newCategoryName";
        // null validation
        if ($(nameId).val() == '') {
            showMsg(namespace, 'Category name cannot be empty', 'Categories');
        } else {
            $(formId).submit();
        }
    });
}

function saveUpdateCategory(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "editCategoryForm";
        var nameId = "#" + namespace + "newCategoryName";
        // null validation
        if ($(nameId).val() == '') {
            showMsg(namespace, 'Category name cannot be empty', 'Categories');
        } else {
            $(formId).submit();
        }
    });
}
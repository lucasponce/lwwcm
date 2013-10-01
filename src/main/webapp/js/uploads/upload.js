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

/* Upload functions
 ================== */

function showUploadFile(namespace) {
    require(["SHARED/jquery"], function($) {
        var uploadId = "#" + namespace + "uploadFile";
        var uploadNameId = "#" + namespace + "uploadFileName";
        if ($._data($(uploadId)[0], "events") === undefined) {
            $(uploadId).change(function () {
               $(uploadNameId).text( $(uploadId).val() );
            });
        }
        $(uploadId).click();
    });
}

function saveNewUpload(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "newUploadForm";
        var uploadId = "#" + namespace + "uploadFile";

        if ($(uploadId).val() == '') {
            showMsg(namespace, 'Upload file cannot be empty', 'Uploads');
        } else {
            $(formId).submit();
        }
    });
}

function saveUpdateUpload(namespace) {
    require(["SHARED/jquery"], function($) {
        var formId = "#" + namespace + "editUploadForm";
        var uploadId = "#" + namespace + "uploadFile";

        $(formId).submit();
    });
}


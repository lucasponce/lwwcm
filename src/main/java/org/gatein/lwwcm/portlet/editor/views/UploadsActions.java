package org.gatein.lwwcm.portlet.editor.views;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.util.ViewMetadata;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

/*
    Actions for Uploads area of EditorPortlet
 */
public class UploadsActions {
    private static final Logger log = Logger.getLogger(UploadsActions.class.getName());

    @Inject
    private WcmService wcm;

    public String actionNewUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        FileItemFactory factory = new DiskFileItemFactory(Wcm.UPLOADS.MAX_FILE_SIZE, new File(Wcm.UPLOADS.TEMP_DIR));
        PortletFileUpload upload = new PortletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            FileItem file = null;
            String description = "";
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    file = item;
                } else {
                    description = item.getString();
                }
            }
            Upload newUpload = new Upload();
            newUpload.setFileName(file.getName());
            newUpload.setMimeType(file.getContentType());
            newUpload.setDescription(description);
            wcm.create(newUpload, file.getInputStream(), userWcm);
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error uploading file");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error uploading file " + e.toString());
        }
        return null;
    }

    public String actionRightUploads(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        viewMetadata.rightPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.UPLOADS;
    }

    public String actionLeftUploads(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        viewMetadata.leftPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.UPLOADS;
    }

    public String actionEditUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        FileItemFactory factory = new DiskFileItemFactory(Wcm.UPLOADS.MAX_FILE_SIZE, new File(Wcm.UPLOADS.TEMP_DIR));
        PortletFileUpload upload = new PortletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            FileItem file = null;
            String description = null;
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    file = item;
                } else {
                    if (item.getFieldName().equals("uploadFileDescription")) {
                        description = item.getString();
                    }
                }
            }
            Upload updateUpload = (Upload)request.getPortletSession().getAttribute("edit");
            if (file != null && file.getSize() > 0 && !file.getName().equals("")) {
                updateUpload.setFileName(file.getName());
                updateUpload.setMimeType(file.getContentType());
                updateUpload.setDescription(description);
                wcm.update(updateUpload, file.getInputStream(), userWcm);
            } else {
                if (description != null) {
                    updateUpload.setDescription(description);
                    wcm.update(updateUpload, userWcm);
                }
            }
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error uploading file");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error uploading file " + e.toString());
        }
        return null;
    }

    public String actionAddCategoryUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String categoryId = request.getParameter("categoryId");
        String uploadId = request.getParameter("uploadId");
        try {
            Category cat = wcm.findCategory(new Long(categoryId), userWcm);
            Upload upload = wcm.findUpload(new Long(uploadId), userWcm);
            wcm.add(upload, cat, userWcm);
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error adding category to upload.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to upload " + e.toString());
        }
        return null;
    }

    public String actionFilterCategoryUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String filterCategoryId = request.getParameter("filterCategoryId");
        Long filterId = new Long(filterCategoryId);
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (!viewMetadata.isFilterCategory()) {
            viewMetadata.setCategoryId(filterId);
            viewMetadata.setFromIndex(0); // First search, reset pagination
            viewMetadata.setToIndex(Wcm.VIEWS.MAX_PER_PAGE - 1);
            viewMetadata.setFilterCategory(true);
        } else {
            if (!viewMetadata.getCategoryId().equals(filterId)) {
                if (filterId == -1) {
                    viewMetadata.setFilterCategory(false);
                } else {
                    viewMetadata.setCategoryId(filterId);
                }
                viewMetadata.setFromIndex(0); // First search, reset pagination
                viewMetadata.setToIndex(Wcm.VIEWS.MAX_PER_PAGE - 1);
            }
        }
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.UPLOADS;
    }

    public String actionDeleteUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deleteUploadId = request.getParameter("deleteUploadId");
        Long uploadId = new Long(deleteUploadId);
        try {
            wcm.deleteUpload(uploadId, userWcm);
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error deleting upload.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting upload " + e.toString());
        }
        return null;
    }

    public String actionDeleteSelectedUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deleteSelectedListId = request.getParameter("deleteSelectedListId");
        try {
            String[] uploadIds = deleteSelectedListId.split(",");
            for (String uploadId : uploadIds) {
                wcm.deleteUpload(new Long(uploadId), userWcm);
            }
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error deleting upload.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting upload " + e.toString());
        }
        return null;
    }

    public String actionAddSelectedCategoryUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String addSelectedCategoryUploadListId = request.getParameter("addSelectedCategoryUploadListId");
        String addCategoryUploadId = request.getParameter("addCategoryUploadId");
        try {
            Category cat = wcm.findCategory(new Long(addCategoryUploadId), userWcm);
            String[] uploadIds = addSelectedCategoryUploadListId.split(",");
            for (String uploadId : uploadIds) {
                Upload upload = wcm.findUpload(new Long(uploadId), userWcm);
                wcm.add(upload, cat, userWcm);
            }
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error adding category to upload.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to upload " + e.toString());
        }
        return null;
    }

    public String actionRemoveCategoryUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String catId = request.getParameter("catid");
        String uploadId = request.getParameter("uploadid");
        try {
            wcm.removeUploadCategory(new Long(uploadId), new Long(catId), userWcm);
            return Wcm.VIEWS.UPLOADS;
        } catch(Exception e) {
            log.warning("Error adding category to upload.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to upload " + e.toString());
        }
        return null;
    }

    public void viewUploads(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.getPortletSession().setAttribute("categories", categories);

            // New default view
            if (viewMetadata == null || viewMetadata.getViewType() != ViewMetadata.ViewType.UPLOADS) {
                List<Upload> allUploads = wcm.findUploads(userWcm);
                if (allUploads != null) {
                    viewMetadata = new ViewMetadata();
                    viewMetadata.setViewType(ViewMetadata.ViewType.UPLOADS);
                    viewMetadata.setTotalIndex(allUploads.size());
                    viewMetadata.resetPagination();
                    if (viewMetadata.getTotalIndex() > 0) {
                        List<Upload> viewList = allUploads.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                        request.getPortletSession().setAttribute("list", viewList);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    } else {
                        request.getPortletSession().setAttribute("list", null);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    }
                }
            } else {
                // Filter per category
                if (viewMetadata.isFilterCategory()) {
                    List<Upload> filterUploads = wcm.findUploads(viewMetadata.getCategoryId(), userWcm);
                    if (filterUploads != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.UPLOADS);
                        viewMetadata.setTotalIndex(filterUploads.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Upload> viewList = filterUploads.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.getPortletSession().setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.getPortletSession().setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                } else {
                    List<Upload> allUploads = wcm.findUploads(userWcm);
                    if (allUploads != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.UPLOADS);
                        viewMetadata.setTotalIndex(allUploads.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Upload> viewList = allUploads.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.getPortletSession().setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.getPortletSession().setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                }
            }
        } catch(WcmException e) {
            log.warning("Error accessing uploads.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing upoads: " + e.toString());
        }
    }

    public void viewEditUpload(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String editId = request.getParameter("editid");
        try {
            Upload upload = wcm.findUpload(new Long(editId), userWcm);
            request.getPortletSession().setAttribute("edit", upload);
        } catch (WcmException e) {
            log.warning("Error accessing uploads.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing uploads: " + e.toString());
        }
    }

    public void eventDownloadUpload(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String uploadid = request.getParameter("uploadid");
        try {
            Upload upload = wcm.findUpload(new Long(uploadid), userWcm);
            if (upload != null) {
                download(upload, response);
            }
        } catch (WcmException e) {
            log.warning("Error accessing upload.");
            e.printStackTrace();
        } catch (Exception e) {
            log.warning("Error downloading file.");
            e.printStackTrace();
        }
    }

    public void download(Upload upload, ResourceResponse response) throws Exception {
        if (response == null || upload == null) return;

        String dirPath;
        String fullPath;

        if (System.getProperty(Wcm.UPLOADS.FOLDER) == null) {
            dirPath = Wcm.UPLOADS.DEFAULT;
        } else {
            dirPath = System.getProperty(Wcm.UPLOADS.FOLDER);
        }
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdir()) {
            throw new WcmException("Cannot read dir: " + Wcm.UPLOADS.FOLDER);
        }
        fullPath = dirPath + File.separator + upload.getStoredName();

        response.setContentType(upload.getMimeType());
        response.setProperty("Content-Disposition","inline; filename=\"" + upload.getFileName() + "\"");

        BufferedInputStream input = null;
        OutputStream output = response.getPortletOutputStream();

        input = new BufferedInputStream(new FileInputStream(fullPath));

        byte[] buffer = new byte[Wcm.UPLOADS.LENGTH_BUFFER];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            output.write(buffer, 0, length);
        }
        input.close();
        output.flush();
        output.close();
    }

}

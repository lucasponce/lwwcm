package org.gatein.lwwcm.portlet.views;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.util.List;
import java.util.logging.Logger;

/*
    Actions for Categories area of EditorPortlet
 */
public class CategoriesActions {
    private static final Logger log = Logger.getLogger(CategoriesActions.class.getName());

    @Inject
    private WcmService wcm;

    public String actionNewCategory(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        // Get parameters
        String newCategoryName = request.getParameter("newCategoryName");
        String newCategoryType = request.getParameter("newCategoryType");
        String newCategoryParent = request.getParameter("newCategoryParent");

        Category newCategory = new Category(newCategoryName);
        if (newCategoryType.equals("Category")) {
            newCategory.setType(Wcm.CATEGORIES.CATEGORY);
        }
        if (newCategoryType.equals("Tag")) {
            newCategory.setType(Wcm.CATEGORIES.TAG);
        }
        if (newCategoryType.equals("Folder")) {
            newCategory.setType(Wcm.CATEGORIES.FOLDER);
            if (!newCategoryParent.equals("-1")) {
                long parentId = -1;
                try {
                    parentId = new Long(newCategoryParent).longValue();
                } catch (Exception ignored) { }
                if (parentId>-1) {
                    try {
                        Category parentCategory = wcm.findCategory(parentId, userWcm);
                        newCategory.setParent(parentCategory);
                    } catch (WcmException e) {
                        log.warning("Error getting parentCategory.");
                        e.printStackTrace();
                        response.setRenderParameter("errorWcm", "Error getting parentCategory " + e.toString());
                    }
                }
            }
        }
        try {
            wcm.create(newCategory, userWcm);
            return Wcm.VIEWS.CATEGORIES;
        } catch (Exception e) {
            log.warning("Error creating new category");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error creating new category " + e.toString());
        }
        return null;
    }

    public String actionDeleteCategory(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String catId = request.getParameter("deletedCategoryId");
        try {
            wcm.deleteCategory(new Long(catId), userWcm);
            return Wcm.VIEWS.CATEGORIES;
        } catch (Exception e) {
            log.warning("Error deleting category");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting category " + e.toString());
        }
        return null;
    }

    public String actionEditCategory(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String catId = request.getParameter("editCategoryId");
        String newCategoryName = request.getParameter("newCategoryName");
        String newCategoryType = request.getParameter("newCategoryType");
        String newCategoryParent = request.getParameter("newCategoryParent");

        Category updateCategory = null;
        try {
            updateCategory = wcm.findCategory(new Long(catId), userWcm);
            updateCategory.setName(newCategoryName);
            if (newCategoryType.equals("Category")) {
                updateCategory.setType(Wcm.CATEGORIES.CATEGORY);
                updateCategory.setParent(null);
            }
            if (newCategoryType.equals("Tag")) {
                updateCategory.setType(Wcm.CATEGORIES.TAG);
                updateCategory.setParent(null);
            }
            if (newCategoryType.equals("Folder")) {
                updateCategory.setType(Wcm.CATEGORIES.FOLDER);
                if (!newCategoryParent.equals("-1")) {
                    long parentId = -1;
                    try {
                        parentId = new Long(newCategoryParent).longValue();
                    } catch (Exception ignored) { }
                    if (parentId>-1 && parentId != new Long(catId)) {
                        try {
                            Category parentCategory = wcm.findCategory(parentId, userWcm);
                            updateCategory.setParent(parentCategory);
                        } catch (WcmException e) {
                            log.warning("Error getting parentCategory.");
                            e.printStackTrace();
                            response.setRenderParameter("errorWcm", "Error getting parentCategory " + e.toString());
                        }
                    }
                } else {
                    updateCategory.setParent(null);
                }
            }
            wcm.update(updateCategory, userWcm);
            return Wcm.VIEWS.CATEGORIES;
        } catch (Exception e) {
            log.warning("Error updating category");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error updating category " + e.toString());
        }
        return null;
    }

    public void viewCategories(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        List<Category> categories = null;
        try {
            categories = wcm.findRootCategories(userWcm);
            // "list" variable will be global and it will be used to store all list from action phase to render phase
            // this way it can survive several invocation of third party portlets
            request.getPortletSession().setAttribute("list", categories);
        } catch (WcmException e) {
            log.warning("Error accessing categories.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing categories: " + e.toString());
        }
    }

    public void viewNewCategory(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        List<Category> categories = null;
        try {
            categories = wcm.findCategories(Wcm.CATEGORIES.FOLDER, userWcm);
        } catch (WcmException e) {
            log.warning("Error accessing categories.");
            e.printStackTrace();
        }
        request.getPortletSession().setAttribute("categories", categories);
    }

    public void viewEditCategory(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String editId = request.getParameter("editid");
        try {
            Category category = wcm.findCategory(new Long(editId), userWcm);
            List<Category> categories = wcm.findCategories(Wcm.CATEGORIES.FOLDER, userWcm);
            request.getPortletSession().setAttribute("edit", category);
            request.getPortletSession().setAttribute("categories", categories);
        } catch (WcmException e) {
            log.warning("Error accessing categories.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing categories: " + e.toString());
        }
    }

    public String eventShowCategoriesChildren(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String parentid = request.getParameter("parentid");
        String namespace = request.getParameter("namespace");
        List<Category> categories = null;
        try {
            categories = wcm.findChildren(new Long(parentid));
        } catch (WcmException e) {
            log.warning("Error accessing categories.");
            e.printStackTrace();
        }
        request.setAttribute("categories", categories);
        request.setAttribute("namespace", namespace);
        request.setAttribute("parentid", parentid);
        return "/jsp/categories/categoriesChildren.jsp";
    }

}

package org.gatein.lwwcm.portlet.editor.views;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Template;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.util.ViewMetadata;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.util.List;
import java.util.logging.Logger;

/*
    Actions for Templates area of EditorPortlet
 */
public class TemplatesActions {
    private static final Logger log = Logger.getLogger(TemplatesActions.class.getName());

    @Inject
    private WcmService wcm;

    public String actionNewTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String templateName = request.getParameter("templateName");
        String templateLocale = request.getParameter("templateLocale");
        String templateContent = request.getParameter("templateContent");
        String templateType = request.getParameter("templateType");
        try {
            Template newTemplate = new Template();
            newTemplate.setUser(userWcm.getUsername());
            newTemplate.setName(templateName);
            newTemplate.setLocale(templateLocale);
            newTemplate.setContent(templateContent);
            newTemplate.setType(templateType.charAt(0));
            wcm.create(newTemplate, userWcm);
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error saving template");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error saving template " + e.toString());
        }
        return null;
    }

    public String actionRightTemplates(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) viewMetadata.rightPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.TEMPLATES;
    }

    public String actionLeftTemplates(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) viewMetadata.leftPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.TEMPLATES;
    }

    public String actionEditTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String templateName = request.getParameter("templateName");
        String templateLocale = request.getParameter("templateLocale");
        String templateContent = request.getParameter("templateContent");
        String templateType = request.getParameter("templateType");
        try {
            Template updateTemplate = (Template)request.getPortletSession().getAttribute("edit");
            updateTemplate.setName(templateName);
            updateTemplate.setLocale(templateLocale);
            updateTemplate.setContent(templateContent);
            updateTemplate.setType(templateType.charAt(0));
            wcm.update(updateTemplate, userWcm);
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error uploading template");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error uploading template " + e.toString());
        }
        return null;
    }

    public String actionAddCategoryTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String categoryId = request.getParameter("categoryId");
        String templateId = request.getParameter("templateId");
        try {
            Category cat = wcm.findCategory(new Long(categoryId), userWcm);
            Template template = wcm.findTemplate(new Long(templateId), userWcm);
            wcm.add(template, cat, userWcm);
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error adding category to template.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to template " + e.toString());
        }
        return null;
    }

    public String actionFilterCategoryTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String filterCategoryId = request.getParameter("filterCategoryId");
        Long filterId = new Long(filterCategoryId);
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) {
            // Reset viewMetadata if it comes from a different view
            if (viewMetadata.getViewType() != ViewMetadata.ViewType.TEMPLATES) {
                viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                viewMetadata.setFilterCategory(false);
            }
            if (!viewMetadata.isFilterCategory()) {
                viewMetadata.setCategoryId(filterId);
                viewMetadata.setFromIndex(0); // First search, reset pagination
                viewMetadata.setToIndex(Wcm.VIEWS.MAX_PER_PAGE - 1);
                viewMetadata.setFilterCategory(true);
                viewMetadata.setFilterName(false);
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
        }
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.TEMPLATES;
    }

    public String actionFilterNameTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String filterName = request.getParameter("filterName");
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) {
            // Reset viewMetadata if it comes from a different view
            if (viewMetadata.getViewType() != ViewMetadata.ViewType.TEMPLATES) {
                viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                viewMetadata.setFilterName(false);
            }
            if (!viewMetadata.isFilterName()) {
                viewMetadata.setName(filterName);
                viewMetadata.setFromIndex(0); // First search, reset pagination
                viewMetadata.setToIndex(Wcm.VIEWS.MAX_PER_PAGE - 1);
                viewMetadata.setFilterName(true);
                viewMetadata.setFilterCategory(false);
            } else {
                if (!viewMetadata.getName().equals(filterName)) {
                    if (filterName.equals("")) {
                        viewMetadata.setFilterName(false);
                    } else {
                        viewMetadata.setName(filterName);
                    }
                    viewMetadata.setFromIndex(0); // First search, reset pagination
                    viewMetadata.setToIndex(Wcm.VIEWS.MAX_PER_PAGE - 1);
                }
            }
        }
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.TEMPLATES;
    }

    public String actionDeleteTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deleteTemplateId = request.getParameter("deleteTemplateId");
        Long templateId = new Long(deleteTemplateId);
        try {
            wcm.deleteTemplate(templateId, userWcm);
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error deleting template.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting template " + e.toString());
        }
        return null;
    }

    public String actionDeleteSelectedTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deleteSelectedListId = request.getParameter("deleteSelectedListId");
        try {
            String[] templatesIds = deleteSelectedListId.split(",");
            for (String templateId : templatesIds) {
                wcm.deleteTemplate(new Long(templateId), userWcm);
            }
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error deleting template.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting template " + e.toString());
        }
        return null;
    }

    public String actionAddSelectedCategoryTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String addSelectedCategoryTemplateListId = request.getParameter("addSelectedCategoryTemplateListId");
        String addCategoryTemplateId = request.getParameter("addCategoryTemplateId");
        try {
            Category cat = wcm.findCategory(new Long(addCategoryTemplateId), userWcm);
            String[] templatesIds = addSelectedCategoryTemplateListId.split(",");
            for (String templateId : templatesIds) {
                Template template = wcm.findTemplate(new Long(templateId), userWcm);
                wcm.add(template, cat, userWcm);
            }
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error adding category to template.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to template " + e.toString());
        }
        return null;
    }

    public String actionRemoveCategoryTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String catId = request.getParameter("catid");
        String templateId = request.getParameter("templateid");
        try {
            wcm.removeTemplateCategory(new Long(templateId), new Long(catId), userWcm);
            return Wcm.VIEWS.TEMPLATES;
        } catch(Exception e) {
            log.warning("Error adding category to template.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to template " + e.toString());
        }
        return null;
    }

    public void viewTemplates(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.getPortletSession().setAttribute("categories", categories);

            // New default view
            if (viewMetadata == null || viewMetadata.getViewType() != ViewMetadata.ViewType.TEMPLATES) {
                List<Template> allTemplates = wcm.findTemplates(userWcm);
                if (allTemplates != null) {
                    viewMetadata = new ViewMetadata();
                    viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                    viewMetadata.setTotalIndex(allTemplates.size());
                    viewMetadata.resetPagination();
                    if (viewMetadata.getTotalIndex() > 0) {
                        List<Template> viewList = allTemplates.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
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
                    List<Template> filterTemplates = wcm.findTemplates(viewMetadata.getCategoryId(), userWcm);
                    if (filterTemplates != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                        viewMetadata.setTotalIndex(filterTemplates.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Template> viewList = filterTemplates.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.getPortletSession().setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.getPortletSession().setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                } else if (viewMetadata.isFilterName()) {
                    List<Template> filterTemplates = wcm.findTemplates(viewMetadata.getName(), userWcm);
                    if (filterTemplates != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                        viewMetadata.setTotalIndex(filterTemplates.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Template> viewList = filterTemplates.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.getPortletSession().setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.getPortletSession().setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                } else {
                    List<Template> allTemplates = wcm.findTemplates(userWcm);
                    if (allTemplates != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.TEMPLATES);
                        viewMetadata.setTotalIndex(allTemplates.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Template> viewList = allTemplates.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
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
            log.warning("Error accessing templates.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing templates: " + e.toString());
        }
    }

    public void viewEditTemplate(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String editId = request.getParameter("editid");
        try {
            Template template = wcm.findTemplate(new Long(editId), userWcm);
            request.getPortletSession().setAttribute("edit", template);
        } catch (WcmException e) {
            log.warning("Error accessing templates.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing templates: " + e.toString());
        }
    }
}

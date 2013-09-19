package org.gatein.lwwcm.portlet.editor.views;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.*;
import org.gatein.lwwcm.portlet.util.ViewMetadata;
import org.gatein.lwwcm.services.PortalService;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.*;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/*
    Actions for Posts area of EditorPortlet
 */
public class PostsActions {
    private static final Logger log = Logger.getLogger(PostsActions.class.getName());

    @Inject
    private WcmService wcm;

    @Inject
    private PortalService portal;

    public String actionNewPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String postTitle = request.getParameter("postTitle");
        String postExcerpt = request.getParameter("postExcerpt");
        String postLocale = request.getParameter("postLocale");
        String postStatus = request.getParameter("postStatus");
        String postCommentsStatus = request.getParameter("postCommentsStatus");
        String postContent = request.getParameter("postContent");
        try {
            Post newPost = new Post();
            newPost.setTitle(postTitle);
            newPost.setExcerpt(postExcerpt);
            newPost.setLocale(postLocale);
            newPost.setPostStatus(postStatus.charAt(0));
            newPost.setCommentsStatus(postCommentsStatus.charAt(0));
            newPost.setContent(postContent);
            newPost.setAuthor(userWcm.getUsername());
            wcm.create(newPost, userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error saving post");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error saving post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionRightPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) viewMetadata.rightPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.POSTS;
    }

    public String actionLeftPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) viewMetadata.leftPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.POSTS;
    }

    public String actionEditPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String postEditId = request.getParameter("postEditId");
        String postTitle = request.getParameter("postTitle");
        String postExcerpt = request.getParameter("postExcerpt");
        String postLocale = request.getParameter("postLocale");
        String postStatus = request.getParameter("postStatus");
        String postCommentsStatus = request.getParameter("postCommentsStatus");
        String postContent = request.getParameter("postContent");
        try {
            Post updatePost = wcm.findPost(new Long(postEditId), userWcm);
            updatePost.setTitle(postTitle);
            updatePost.setExcerpt(postExcerpt);
            updatePost.setLocale(postLocale);
            updatePost.setPostStatus(postStatus.charAt(0));
            updatePost.setCommentsStatus(postCommentsStatus.charAt(0));
            updatePost.setContent(postContent);
            updatePost.setAuthor(userWcm.getUsername());
            wcm.update(updatePost, userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error uploading post");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error uploading post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionAddCategoryPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String categoryId = request.getParameter("categoryId");
        String postId = request.getParameter("postId");
        try {
            Category cat = wcm.findCategory(new Long(categoryId), userWcm);
            Post post = wcm.findPost(new Long(postId), userWcm);
            wcm.add(post, cat, userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error adding category to post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionFilterCategoryPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String filterCategoryId = request.getParameter("filterCategoryId");
        Long filterId = new Long(filterCategoryId);
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) {
            // Reset viewMetadata if it comes from a different view
            if (viewMetadata.getViewType() != ViewMetadata.ViewType.POSTS) {
                viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
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
        return Wcm.VIEWS.POSTS;
    }

    public String actionFilterNamePost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String filterName = request.getParameter("filterName");
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        if (viewMetadata != null) {
            // Reset viewMetadata if it comes from a different view
            if (viewMetadata.getViewType() != ViewMetadata.ViewType.POSTS) {
                viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
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
        return Wcm.VIEWS.POSTS;
    }

    public String actionDeletePost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deletePostId = request.getParameter("deletePostId");
        Long postId = new Long(deletePostId);
        try {
            wcm.deletePost(postId, userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error deleting post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionDeleteSelectedPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String deleteSelectedListId = request.getParameter("deleteSelectedListId");
        try {
            String[] postsIds = deleteSelectedListId.split(",");
            for (String postId : postsIds) {
                wcm.deletePost(new Long(postId), userWcm);
            }
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error deleting post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error deleting post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionAddSelectedCategoryPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String addSelectedCategoryPostListId = request.getParameter("addSelectedCategoryPostListId");
        String addCategoryPostId = request.getParameter("addCategoryPostId");
        try {
            Category cat = wcm.findCategory(new Long(addCategoryPostId), userWcm);
            String[] postsIds = addSelectedCategoryPostListId.split(",");
            for (String postId : postsIds) {
                Post post = wcm.findPost(new Long(postId), userWcm);
                wcm.add(post, cat, userWcm);
            }
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error adding category to post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionRemoveCategoryPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String catId = request.getParameter("catid");
        String postId = request.getParameter("postid");
        try {
            wcm.removePostCategory(new Long(postId), new Long(catId), userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error adding category to post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error adding category to post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionPublishPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String publishPostId = request.getParameter("publishPostId");
        String publishState = request.getParameter("publishState");
        try {
            Post post = wcm.findPost(new Long(publishPostId), userWcm);
            post.setPostStatus(publishState.charAt(0));
            wcm.update(post, userWcm);
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error publishing post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error publishing post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public String actionPublishPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String publishStates = request.getParameter("publishStates");
        String publishPostsId = request.getParameter("publishPostsId");
        try {
            String[] postsIds = publishPostsId.split(",");
            for (String postId : postsIds) {
                Post post = wcm.findPost(new Long(postId), userWcm);
                post.setPostStatus(publishStates.charAt(0));
                wcm.update(post, userWcm);
            }
            return Wcm.VIEWS.POSTS;
        } catch(Exception e) {
            log.warning("Error publishing post.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error publishing post " + e.toString());
        }
        return Wcm.VIEWS.POSTS;
    }

    public void viewInitPosts(RenderRequest request, RenderResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.setAttribute("categories", categories);

            // Wcm groups
            Set<String> wcmGroups = portal.getWcmGroups();
            request.setAttribute("wcmGroups", wcmGroups);

            // New default view
            if (viewMetadata == null || viewMetadata.getViewType() != ViewMetadata.ViewType.POSTS) {
                List<Post> allPosts = wcm.findPosts(userWcm);
                if (allPosts != null) {
                    viewMetadata = new ViewMetadata();
                    viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                    viewMetadata.setTotalIndex(allPosts.size());
                    viewMetadata.resetPagination();
                    if (viewMetadata.getTotalIndex() > 0) {
                        List<Post> viewList = allPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                        request.setAttribute("list", viewList);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    } else {
                        request.setAttribute("list", null);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    }
                }
            }
        } catch(WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
        }
    }

    public void viewPosts(RenderRequest request, RenderResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.setAttribute("categories", categories);

            // Wcm groups
            Set<String> wcmGroups = portal.getWcmGroups();
            request.setAttribute("wcmGroups", wcmGroups);

            // New default view
            if (viewMetadata == null || viewMetadata.getViewType() != ViewMetadata.ViewType.POSTS) {
                List<Post> allPosts = wcm.findPosts(userWcm);
                if (allPosts != null) {
                    viewMetadata = new ViewMetadata();
                    viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                    viewMetadata.setTotalIndex(allPosts.size());
                    viewMetadata.resetPagination();
                    if (viewMetadata.getTotalIndex() > 0) {
                        List<Post> viewList = allPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                        request.setAttribute("list", viewList);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    } else {
                        request.setAttribute("list", null);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    }
                }
            } else {
                // Filter per category
                if (viewMetadata.isFilterCategory()) {
                    List<Post> filterPosts = wcm.findPosts(viewMetadata.getCategoryId(), userWcm);
                    if (filterPosts != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                        viewMetadata.setTotalIndex(filterPosts.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Post> viewList = filterPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                } else if (viewMetadata.isFilterName()) {
                    List<Post> filterPosts = wcm.findPosts(viewMetadata.getName(), userWcm);
                    if (filterPosts != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                        viewMetadata.setTotalIndex(filterPosts.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Post> viewList = filterPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                } else {
                    List<Post> allPosts = wcm.findPosts(userWcm);
                    if (allPosts != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                        viewMetadata.setTotalIndex(allPosts.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Post> viewList = allPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.setAttribute("list", null);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        }
                    }
                }
            }
        } catch(WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
            request.setAttribute("errorWcm", "Error accessing posts: " + e.toString());
        }
    }

    public void viewEditPost(RenderRequest request, RenderResponse response, UserWcm userWcm) {
        String editId = request.getParameter("editid");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.setAttribute("categories", categories);

            Post post = wcm.findPost(new Long(editId), userWcm);
            request.setAttribute("edit", post);
        } catch (WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
            request.setAttribute("errorWcm", "Error accessing posts: " + e.toString());
        }
    }

    public void viewNewPost(RenderRequest request, RenderResponse response, UserWcm userWcm) {
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.setAttribute("categories", categories);
        } catch (WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
            request.setAttribute("errorWcm", "Error accessing posts: " + e.toString());
        }
    }

    public String eventShowPostUploads(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String namespace = request.getParameter("namespace");
        String filterCategoryId = request.getParameter("filterCategoryId");
        String filterName = request.getParameter("filterName");
        try {
            List<Upload> uploads = null;
            if (filterCategoryId != null && !filterCategoryId.equals("") && !filterCategoryId.equals("-1")) {
                uploads = wcm.findUploads(new Long(filterCategoryId), userWcm);
            } else if (filterName != null && !filterName.equals("")) {
                uploads = wcm.findUploads(filterName, userWcm);
            } else {
                uploads = wcm.findUploads(userWcm);
            }
            request.setAttribute("uploads", uploads);
            request.setAttribute("namespace", namespace);
        } catch(WcmException e) {
            log.warning("Error accesing uploads.");
            e.printStackTrace();
        } catch(Exception e) {
            log.warning("Error parsing filterCategoryId: " + filterCategoryId);
            e.printStackTrace();
        }
        return "/jsp/posts/postUploads.jsp";
    }

    public String eventShowPostAcls(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String namespace = request.getParameter("namespace");
        String postId = request.getParameter("postid");
        try {
            Post post = null;
            if (postId != null && !"".equals(postId) && namespace != null && !"".equals(namespace)) {
                post = wcm.findPost(new Long(postId), userWcm);
                if (!userWcm.canWrite(post)) post = null;
            }
            request.setAttribute("post", post);
            request.setAttribute("namespace", namespace);
        } catch(WcmException e) {
            log.warning("Error accesing post acls.");
            e.printStackTrace();
        } catch (Exception e) {
            log.warning("Error parsing postId: " + postId);
            e.printStackTrace();
        }
        return "/jsp/posts/postsAcls.jsp";
    }

    public String eventAddAclPost(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String aclPostId = request.getParameter("aclpostid");
        String aclType = request.getParameter("acltype");
        String aclWcmGroup = request.getParameter("aclwcmgroup");
        String namespace = request.getParameter("namespace");
        try {
            Post post = wcm.findPost(new Long(aclPostId), userWcm);
            if (post != null && aclType != null && aclWcmGroup != null && userWcm.canWrite(post)) {
                Acl newAcl = new Acl();
                if (aclType.equals(Wcm.ACL.WRITE.toString())) {
                    newAcl.setPermission(Wcm.ACL.WRITE);
                } else {
                    newAcl.setPermission(Wcm.ACL.NONE);
                }
                newAcl.setPrincipal(aclWcmGroup);

                // Check if exists
                boolean found = false;
                for (Acl acl : post.getAcls()) {
                    if (acl.getPermission().equals(newAcl.getPermission()) &&
                            acl.getPrincipal().equals(newAcl.getPrincipal())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    post.getAcls().add(newAcl);
                    newAcl.setPost(post);
                    wcm.update(post, userWcm);
                }
            } else {
                post = null;
            }
            request.setAttribute("post", post);
            request.setAttribute("namespace", namespace);
        } catch (Exception e) {
            log.warning("Error adding Acl to Post");
            e.printStackTrace();
        }
        return "/jsp/posts/postsAcls.jsp";
    }

    public String eventRemoveAclPost(ResourceRequest request, ResourceResponse response, UserWcm userWcm) {
        String aclPostId = request.getParameter("aclpostid");
        String aclId = request.getParameter("aclid");
        String namespace = request.getParameter("namespace");
        try {
            Post post = wcm.findPost(new Long(aclPostId), userWcm);
            if (post != null && aclId != null && userWcm.canWrite(post)) {
                // Check if exists
                Acl found = null;
                for (Acl acl : post.getAcls()) {
                    if (acl.getId().toString().equals(aclId)) {
                        found = acl;
                        break;
                    }
                }
                // Rules to remove:
                // - at least 1 WRITE ACL should exists
                if ((found.getPermission() == Wcm.ACL.NONE && post.getAcls().size() > 1) ||
                        (found.getPermission() == Wcm.ACL.WRITE && countAcl(post.getAcls(), Wcm.ACL.WRITE) > 1)) {
                    wcm.remove(found, userWcm);
                    post.getAcls().remove(found);
                    post = wcm.findPost(new Long(aclPostId), userWcm);
                    if (!userWcm.canWrite(post)) post = null;
                }
            } else {
                post = null;
            }
            request.setAttribute("post", post);
            request.setAttribute("namespace", namespace);

        } catch (Exception e) {
            log.warning("Error removing Acl to Post");
            e.printStackTrace();
        }
        return "/jsp/posts/postsAcls.jsp";
    }

    private int countAcl(Set<Acl> acl, Character type) {
        if (acl == null) return -1;
        int count = 0;
        for (Acl a : acl) if (a.getPermission() == type) count++;
        return count;
    }

}

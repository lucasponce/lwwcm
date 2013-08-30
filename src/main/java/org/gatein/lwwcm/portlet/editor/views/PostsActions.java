package org.gatein.lwwcm.portlet.editor.views;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.portlet.util.ViewMetadata;
import org.gatein.lwwcm.services.WcmService;

import javax.inject.Inject;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;
import java.util.logging.Logger;

/*
    Actions for Posts area of EditorPortlet
 */
public class PostsActions {
    private static final Logger log = Logger.getLogger(PostsActions.class.getName());

    @Inject
    private WcmService wcm;

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
        return null;
    }

    public String actionRightPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        viewMetadata.rightPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.POSTS;
    }

    public String actionLeftPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        viewMetadata.leftPage();
        request.getPortletSession().setAttribute("metadata", viewMetadata);
        return Wcm.VIEWS.POSTS;
    }

    public String actionEditPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String postTitle = request.getParameter("postTitle");
        String postExcerpt = request.getParameter("postExcerpt");
        String postLocale = request.getParameter("postLocale");
        String postStatus = request.getParameter("postStatus");
        String postCommentsStatus = request.getParameter("postCommentsStatus");
        String postContent = request.getParameter("postContent");
        try {
            Post updatePost = (Post)request.getPortletSession().getAttribute("edit");
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
        return null;
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
        return null;
    }

    public String actionFilterCategoryPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
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
        return null;
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
        return null;
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
        return null;
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
        return null;
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
        return null;
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
        return null;
    }

    public void viewInitPosts(RenderRequest request, RenderResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.getPortletSession().setAttribute("categories", categories);
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
                        request.getPortletSession().setAttribute("list", viewList);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    } else {
                        request.getPortletSession().setAttribute("list", null);
                        request.getPortletSession().setAttribute("metadata", viewMetadata);
                    }
                }
            }
        } catch(WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
        }
    }

    public void viewPosts(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        // Check view metadata
        ViewMetadata viewMetadata = (ViewMetadata)request.getPortletSession().getAttribute("metadata");
        try {
            // Categories
            List<Category> categories = wcm.findCategories(userWcm);
            request.getPortletSession().setAttribute("categories", categories);

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
                    List<Post> filterPosts = wcm.findPosts(viewMetadata.getCategoryId(), userWcm);
                    if (filterPosts != null) {
                        viewMetadata.setViewType(ViewMetadata.ViewType.POSTS);
                        viewMetadata.setTotalIndex(filterPosts.size());
                        viewMetadata.checkPagination();
                        if (viewMetadata.getTotalIndex() > 0) {
                            List<Post> viewList = filterPosts.subList(viewMetadata.getFromIndex(), viewMetadata.getToIndex()+1);
                            request.getPortletSession().setAttribute("list", viewList);
                            request.getPortletSession().setAttribute("metadata", viewMetadata);
                        } else {
                            request.getPortletSession().setAttribute("list", null);
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
            log.warning("Error accessing posts.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing posts: " + e.toString());
        }
    }

    public void viewEditPost(ActionRequest request, ActionResponse response, UserWcm userWcm) {
        String editId = request.getParameter("editid");
        try {
            Post post = wcm.findPost(new Long(editId), userWcm);
            request.getPortletSession().setAttribute("edit", post);
        } catch (WcmException e) {
            log.warning("Error accessing posts.");
            e.printStackTrace();
            response.setRenderParameter("errorWcm", "Error accessing posts: " + e.toString());
        }
    }
}

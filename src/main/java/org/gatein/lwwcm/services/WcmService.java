package org.gatein.lwwcm.services;

import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;

import org.gatein.lwwcm.WcmAuthorizationException;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.*;

@Local
public interface WcmService {
	
	/*
	 * Categories API.
	 */
	void create(Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
    void deleteCategory(Long id, UserWcm user) throws WcmAuthorizationException, WcmException;
    Category findCategory(Long id, UserWcm user) throws WcmException;
    List<Category> findCategories(UserWcm user) throws WcmException;
	List<Category> findCategories(Character type, UserWcm user) throws WcmException;
    List<Category> findRootCategories(UserWcm user) throws WcmException;
    List<Category> findChildren(Category cat) throws WcmException;
    List<Category> findChildren(Long id) throws WcmException;


	/*
	 * Post API.
	 */
	void create(Post post, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Post post, UserWcm user) throws WcmAuthorizationException, WcmException;
    void deletePost(Long id, UserWcm user) throws WcmAuthorizationException, WcmException;
	void add(Post post, Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
    void removePostCategory(Long postId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException;
	void add(Post post, Comment comment) throws WcmAuthorizationException, WcmException;
	void remove(Post post, Comment comment, UserWcm user) throws WcmAuthorizationException, WcmException;
    Post findPost(Long id, UserWcm user) throws WcmException;
    List<Post> findPosts(Long categoryId, UserWcm user) throws WcmException;
    List<Post> findPosts(UserWcm user) throws WcmException;

	
	/*
	 * Upload API.
	 */
	void create(Upload upload, InputStream is, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Upload upload, InputStream is, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Upload upload, UserWcm user) throws WcmAuthorizationException, WcmException;
	void delete(Upload upload, UserWcm user) throws WcmAuthorizationException, WcmException;
    void deleteUpload(Long id, UserWcm user) throws WcmAuthorizationException, WcmException;
    void add(Upload upload, Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
    void removeUploadCategory(Long uploadId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException;
    Upload findUpload(Long id, UserWcm user) throws WcmException;
    List<Upload> findUploads(Long categoryId, UserWcm user) throws WcmException;
    List<Upload> findUploads(UserWcm user) throws WcmException;


    /*
     * Template API
     */
    void create(Template temp, UserWcm user) throws WcmAuthorizationException, WcmException;
    void update(Template template, UserWcm user) throws WcmAuthorizationException, WcmException;
    void deleteTemplate(Long id, UserWcm user) throws WcmAuthorizationException, WcmException;
    void add(Template template, Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
    void removeTemplateCategory(Long templateId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException;
    Template findTemplate(Long id, UserWcm user) throws WcmException;
    List<Template> findTemplates(Long categoryId, UserWcm user) throws WcmException;
    List<Template> findTemplates(UserWcm user) throws WcmException;


}

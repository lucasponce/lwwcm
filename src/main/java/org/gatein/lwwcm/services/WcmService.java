package org.gatein.lwwcm.services;

import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;

import org.gatein.lwwcm.WcmAuthorizationException;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Comment;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;

@Local
public interface WcmService {
	
	/*
	 * Categories API.
	 */
	void create(Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
	void delete(Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
	List<Category> findCategories(String name, UserWcm user) throws WcmException;
	List<Category> findCategories(Character type, UserWcm user) throws WcmException;
	List<Category> findChildren(Category cat) throws WcmException;
	
	/*
	 * Post API.
	 */
	void create(Post post, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Post post, UserWcm user) throws WcmAuthorizationException, WcmException;
	void delete(Post post, UserWcm user) throws WcmAuthorizationException, WcmException;	
	void add(Post post, Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;
	void remove(Post post, Category cat, UserWcm user) throws WcmAuthorizationException, WcmException;	
	void add(Post post, Comment comment) throws WcmAuthorizationException, WcmException;
	void remove(Post post, Comment comment, UserWcm user) throws WcmAuthorizationException, WcmException;
	List<Post> findPosts(String name, UserWcm user) throws WcmException;
	
	/*
	 * Upload API.
	 */
	void create(Upload upload, InputStream is, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Upload upload, InputStream is, UserWcm user) throws WcmAuthorizationException, WcmException;
	void update(Upload upload, UserWcm user) throws WcmAuthorizationException, WcmException;
	void delete(Upload upload, UserWcm user) throws WcmAuthorizationException, WcmException;
	List<Upload> findUpload(String fileName, UserWcm user) throws WcmException;
	
	/*
	 * Relationship API.
	 */
	
}

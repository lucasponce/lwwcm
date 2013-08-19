package org.gatein.lwwcm.services.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmAuthorizationException;
import org.gatein.lwwcm.WcmException;
import org.gatein.lwwcm.domain.Acl;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Comment;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.PostHistory;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UploadHistory;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;

@Stateless
public class WcmServiceImpl implements WcmService {

    @PersistenceContext
    EntityManager em;		
	
	private boolean canWrite(Set<Acl> acls, UserWcm user) {
		if (acls == null) return true;
		if (user == null) return false;
		for (String group : user.getGroups()) {
			for (Acl acl : acls) {
				if ((acl.getPrincipal().equals(group) 
					|| acl.getPrincipal().equals(Wcm.GROUPS.ALL)) 
					&& acl.getPermission().equals(Wcm.ACL.WRITE)
					) {
					return true;
				}
			}
		}		
		return false;
	}

	private boolean canRead(Set<Acl> acls, UserWcm user) {
		if (acls == null) return true;
		if (user == null) return false;
		for (String group : user.getGroups()) {
			for (Acl acl : acls) {
				if ((acl.getPrincipal().equals(group) 
					|| acl.getPrincipal().equals(Wcm.GROUPS.ALL)) 
					&& acl.getPermission().equals(Wcm.ACL.NONE)
					) {
					return false;
				}
			}
		}
		return true;
	}
		
	@Override
	public void create(Category cat, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (cat == null || user == null) return;
		if (cat.getParent() == null) {
			Acl read = new Acl(Wcm.GROUPS.ALL, Wcm.ACL.READ);
			read.setCategory(cat);
			cat.add(read);
			for (String group : user.getGroups()) {
				Acl write = new Acl(group, Wcm.ACL.WRITE);
				write.setCategory(cat);
				cat.add(write);
			}			
		} else {
			Set<Acl> parentAcls = cat.getParent().getAcls();
			if (canWrite(parentAcls, user)) {
				for (Acl parentAcl : parentAcls) {
					Acl childAcl = new Acl(parentAcl.getPrincipal(), parentAcl.getPermission());
					childAcl.setCategory(cat);
					cat.add(childAcl);
				}
			} else {
				throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat.getParent());
			}
		}
		try {
			em.persist(cat);			
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}

	@Override
	public void update(Category cat, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (cat == null  || user == null) return;
		if (!canWrite(cat.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
		}
		try {
			em.merge(cat);
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}

	@Override
	public void delete(Category cat, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (cat == null || user == null) return;
		if (!canWrite(cat.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
		}		
		try {
			Category attached = em.find(Category.class, cat.getId());
			deleteOnCascade(attached);
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}	
	
	private void deleteOnCascade(Category cat) throws Exception {
		List<Category> children = findChildren(cat);
		if (children != null && children.size() > 0) {
			for (Category child : children) {
				deleteOnCascade(child);
			}
		}
		em.remove(cat);
	}
	
	@Override
	public List<Category> findCategories(String name, UserWcm user)
			throws WcmException {
		if (user == null) return null;
		try {
			List<Category> result = em.createNamedQuery("listCategoriesName", Category.class)
					.setParameter("name", name)
					.getResultList();
			return aclFilter(result, user);
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}
	
	private List aclFilter(List col, UserWcm user) {
		if (col == null || user == null) return null;
		List filtered = new ArrayList();		
		for (Object o : col) {
			Set<Acl> acls = null;
			if (o instanceof Category)
				acls = ((Category)o).getAcls();
			if (o instanceof Post)
				acls = ((Post)o).getAcls();
			if (o instanceof Upload)
				acls = ((Upload)o).getAcls();
			if (canRead(acls, user))
				filtered.add(o);
		}
		return filtered;
	}

	@Override
	public List<Category> findChildren(Category cat) throws WcmException {
		if (cat == null || cat.getId() == null) return null;
		try {
			List<Category> result = em.createNamedQuery("listCategoriesChildren", Category.class)
					.setParameter("id", cat.getId())
					.getResultList();
			return result;
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}

	@Override
	public List<Category> findCategories(Character type, UserWcm user)
			throws WcmException {
		if (user == null) return null;
		try {
			List<Category> result = em.createNamedQuery("listCategoriesType", Category.class)
					.setParameter("type", type)
					.getResultList();
			return aclFilter(result, user);
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}

	@Override
	public void create(Post post, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null || user == null) return;
		post.setAuthor(user.getUsername());		
		Acl read = new Acl(Wcm.GROUPS.ALL, Wcm.ACL.READ);
		read.setPost(post);
		post.add(read);
		for (String group : user.getGroups()) {
			Acl write = new Acl(group, Wcm.ACL.WRITE);
			write.setPost(post);
			post.add(write);
		}					
		try {
			em.persist(post);			
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}

	@Override
	public void add(Post post, Category cat, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null || cat == null || user == null) return;
		if (!canWrite(cat.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
		}
		cat.add(post);
		post.add(cat);
		try {			
			em.merge(post);
			em.merge(cat);
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}
	
	@Override
	public void update(Post post, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null  || user == null) return;
		if (!canWrite(post.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
		}
		try {						
			PostHistory postVersion = createVersion(post);
			em.persist(postVersion);
			
			post.setModified(Calendar.getInstance());
			post.setVersion(post.getVersion().longValue() + 1);
			em.merge(post);
		} catch (Exception e) {
			throw new WcmException(e);
		}			
	}

	@Override
	public void delete(Post post, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null || user == null || post.getId() == null) return;
		if (!canWrite(post.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
		}		
		try {
			post = em.getReference(Post.class, post.getId());
			for (Category cat : post.getCategories()) {
				cat.remove(post);
				post.remove(cat);
				em.merge(cat);
			}
			
			PostHistory postVersion = createVersion(post);
			postVersion.setDeleted(Calendar.getInstance());
			em.persist(postVersion);
			
			em.remove(post);
		} catch (Exception e) {
			throw new WcmException(e);
		}	
	}

	@Override
	public List<Post> findPosts(String name, UserWcm user) throws WcmException {
		if (name == null || user == null) return null;
		try {
			List<Post> result = em.createNamedQuery("listPostsName", Post.class)
					.setParameter("name", name)
					.getResultList();			
			return aclFilter(result, user);
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}	

	private PostHistory createVersion(Post post) {
		if (post == null || post.getId() == null || post.getVersion() == null) return null;		
		PostHistory copy = new PostHistory();
		copy.setAuthor(post.getAuthor());
		copy.setContent(post.getContent());
		copy.setCreated(post.getCreated());
		copy.setExcerpt(post.getExcerpt());
		copy.setGroup(post.getGroup());
		copy.setId(post.getId());
		copy.setLocale(post.getLocale());
		copy.setModified(post.getModified());
		copy.setName(post.getName());
		copy.setPostStatus(post.getPostStatus());
		copy.setTitle(post.getTitle());
		copy.setVersion(post.getVersion());
		return copy;		
	}

	@Override
	public void add(Post post, Comment comment) throws WcmAuthorizationException, WcmException {
		if (post == null || comment == null || post.getId() == null) return;
		if (post.getCommentsStatus().equals(Wcm.COMMENTS.NO_COMMENTS)) {
			throw new WcmAuthorizationException("Post: " + post + " has not COMMENTS enabled ");
		}
		comment.setPost(post);
		try {
			comment.setPost(post);
			em.persist(comment);
			em.merge(post);
		} catch (Exception e) {
			throw new WcmException(e);
		}		
	}

	@Override
	public void remove(Post post, Comment comment, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null || comment == null || post.getId() == null || comment.getId() == null) return;
		if (!post.getComments().contains(comment)) return;
		if (!canWrite(post.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
		}				
		comment.setPost(null);
		post.getComments().remove(comment);		
		try {
			comment = em.find(Comment.class, comment.getId());
			em.remove(comment);
			em.merge(post);
		} catch (Exception e) {
			throw new WcmException(e);
		}		
	}

	@Override
	public void remove(Post post, Category cat, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (post == null || cat == null || post.getId() == null || cat.getId() == null) return;
		if (!post.getCategories().contains(cat)) return;
		if (!canWrite(post.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
		}
		post.remove(cat);
		cat.remove(post);
		try {
			em.merge(cat);
			em.merge(post);
		} catch (Exception e) {
			throw new WcmException(e);
		}		
	}

	@Override
	public void create(Upload upload, InputStream is, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (upload == null || user == null) return;
		upload.setUser(user.getUsername());		
		Acl read = new Acl(Wcm.GROUPS.ALL, Wcm.ACL.READ);
		read.setUpload(upload);
		upload.add(read);
		for (String group : user.getGroups()) {
			Acl write = new Acl(group, Wcm.ACL.WRITE);
			write.setUpload(upload);
			upload.add(write);
		}					
		try {
			String storedName = UUID.randomUUID().toString();
			copyFile(is, storedName);
			upload.setStoredName(storedName);
			upload.setUser(user.getUsername());
			em.persist(upload);			
		} catch (Exception e) {
			throw new WcmException(e);
		}	
		
	}
	
	private void copyFile(InputStream is, String storedFile) throws Exception {
		if (is == null || storedFile == null) return;
		
		String dirPath;
		String fullPath;
		
		if (System.getProperty(Wcm.UPLOADS.FOLDER) == null) {
			dirPath = Wcm.UPLOADS.DEFAULT;
		} else {
			dirPath = System.getProperty(Wcm.UPLOADS.FOLDER);
		}
		File dir = new File(dirPath);
		if (!dir.exists() && !dir.mkdir()) {
			throw new WcmException("Cannot create dir: " + Wcm.UPLOADS.FOLDER);
		}		
		fullPath = dirPath + File.pathSeparator + storedFile;
		
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		
		input = new BufferedInputStream(is);
		output = new BufferedOutputStream(new FileOutputStream(fullPath));
		byte[] buffer = new byte[Wcm.UPLOADS.LENGTH_BUFFER];
	    for (int length = 0; (length = input.read(buffer)) > 0;) {
	        output.write(buffer, 0, length);
	    }
	    input.close();
	    output.flush();
	    output.close();
	}

	@Override
	public void update(Upload upload, InputStream is, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (upload == null || is == null || user == null || upload.getId() == null) return;
		if (!canWrite(upload.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Upload " + upload);
		}	
		try {
			UploadHistory version = createVersion(upload);
			em.persist(version);
			
			upload = em.find(Upload.class, upload.getId());			
			String storedName = UUID.randomUUID().toString();
			copyFile(is, storedName);
			upload.setStoredName(storedName);
			upload.setVersion(upload.getVersion().longValue() + 1);
			upload.setUser(user.getUsername());
			em.merge(upload);				
		} catch (Exception e) {
			throw new WcmException(e);
		}		
	}
	
	private UploadHistory createVersion(Upload upload) {
		if (upload == null) return null;
		UploadHistory version = new UploadHistory();
		version.setCreated(upload.getCreated());
		version.setDescription(upload.getDescription());
		version.setFileName(upload.getFileName());
		version.setId(upload.getVersion());
		version.setMimeType(upload.getMimeType());
		version.setModified(upload.getModified());
		version.setStoredName(upload.getStoredName());
		version.setUser(upload.getUser());
		version.setVersion(upload.getVersion());
		return version;
	}

	@Override
	public void update(Upload upload, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (upload == null || user == null || upload.getId() == null) return;
		if (!canWrite(upload.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Upload " + upload);
		}	
		try {
			UploadHistory version = createVersion(upload);
			em.persist(version);
			
			upload = em.find(Upload.class, upload.getId());			
			upload.setVersion(upload.getVersion().longValue() + 1);
			upload.setUser(user.getUsername());
			em.merge(upload);				
		} catch (Exception e) {
			throw new WcmException(e);
		}	
	}

	@Override
	public void delete(Upload upload, UserWcm user)
			throws WcmAuthorizationException, WcmException {
		if (upload == null || user == null || upload.getId() == null) return;
		if (!canWrite(upload.getAcls(), user)) {
			throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Upload " + upload);
		}			
		try {
			UploadHistory version = createVersion(upload);
			version.setDeleted(Calendar.getInstance());
			em.persist(version);			
			
			upload = em.find(Upload.class, upload.getId());
			em.remove(upload);			
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}

	@Override
	public List<Upload> findUpload(String fileName, UserWcm user)
			throws WcmException {
		if (fileName == null || user == null) return null;
		try {
			List<Upload> result = em.createNamedQuery("listUploadsFileName", Upload.class)
					.setParameter("fileName", fileName)
					.getResultList();			
			return aclFilter(result, user);
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}	
	
}
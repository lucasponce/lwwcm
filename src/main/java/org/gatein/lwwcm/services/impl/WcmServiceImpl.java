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
import org.gatein.lwwcm.domain.*;
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
    public void deleteCategory(Long id, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (id == null || user == null) return;
        Category cat = em.find(Category.class, id);
        if (!canWrite(cat.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
        }
        try {
            deleteOnCascade(cat);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Category> findCategories(UserWcm user) throws WcmException {
        if (user == null) return null;
        try {
            List<Category> result = em.createNamedQuery("listAllCategories", Category.class)
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
            for (Category c : result) {
                List<Category> children = findChildren(c);
                if (children != null) {
                    c.setNumChildren(children.size());
                }
            }
			return result;
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}

    @Override
    public List<Category> findChildren(Long id) throws WcmException {
        if (id == null) return null;
        try {
            List<Category> result = em.createNamedQuery("listCategoriesChildren", Category.class)
                    .setParameter("id", id)
                    .getResultList();
            for (Category c : result) {
                List<Category> children = findChildren(c);
                if (children != null) {
                    c.setNumChildren(children.size());
                }
            }
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
    public List<Category> findRootCategories(UserWcm user) throws WcmException {
        if (user == null) return null;
        try {
            List<Category> result = em.createNamedQuery("listRootCategories", Category.class)
                    .getResultList();
            for (Category c : result) {
                List<Category> children = findChildren(c);
                if (children != null) {
                    c.setNumChildren(children.size());
                }
            }
            return aclFilter(result, user);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public Category findCategory(Long id, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (id == null) return null;
        try {
            Category cat = em.find(Category.class, id);
            if (cat.getAcls() != null) {
                if (canRead(cat.getAcls(), user)) return cat;
                else return null;
            }
            return cat;
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
		// cat.add(post); // Category -> Posts is lazy, findPosts(Category) is used for that instead to browse into the relationship
		post.add(cat);
		try {
			post = em.merge(post);
			cat = em.merge(cat);
            if (!cat.getPosts().contains(post))
                cat.getPosts().add(post);
            em.flush();
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
    public void deletePost(Long id, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (id == null || user == null) return;
        Post post = em.getReference(Post.class, id);
        if (!canWrite(post.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
        }
        try {
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
    public Post findPost(Long id, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (id == null) return null;
        try {
            Post p = em.find(Post.class, id);
            if (p.getAcls() != null) {
                if (canRead(p.getAcls(), user)) return p;
                else return null;
            }
            return p;
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Post> findPosts(UserWcm user) throws WcmException {
        if (user == null) return null;
        try {
            List<Post> result = em.createNamedQuery("listAllPosts", Post.class)
                    .getResultList();
            return aclFilter(result, user);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Post> findPosts(Long categoryId, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (user == null) return null;
        try {
            Category cat = em.find(Category.class, categoryId);
            if (cat == null) return null;
            List<Post> result = new ArrayList<Post>(cat.getPosts());
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
		copy.setId(post.getId());
		copy.setLocale(post.getLocale());
		copy.setModified(post.getModified());
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
    public void removePostCategory(Long postId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (postId == null || catId == null || user == null) return;
        Post post = em.find(Post.class, postId);
        if (!canWrite(post.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Post " + post);
        }
        try {
            Category cat = em.find(Category.class, catId);
            cat.getPosts().remove(post);
            post.getCategories().remove(cat);
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
		fullPath = dirPath + File.separator + storedFile;
		
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
            upload.setFileName(version.getFileName());
            upload.setDescription(version.getDescription());
			upload.setStoredName(storedName);
			upload.setVersion(upload.getVersion().longValue() + 1);
			upload.setUser(user.getUsername());
            upload.setModified(Calendar.getInstance());
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
		version.setId(upload.getId());
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
            upload.setDescription(version.getDescription());
            upload.setModified(Calendar.getInstance());
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
            upload = em.getReference(Upload.class, upload.getId());

			UploadHistory version = createVersion(upload);
			version.setDeleted(Calendar.getInstance());
			em.persist(version);
            for (Category c : upload.getCategories()) {
                c.getUploads().remove(upload);
            }
			em.remove(upload);
		} catch (Exception e) {
			throw new WcmException(e);
		}
	}

    @Override
    public void deleteUpload(Long id, UserWcm user) throws WcmAuthorizationException, WcmException {
        Upload upload = findUpload(id, user);
        delete(upload, user);
    }

    @Override
    public List<Upload> findUploads(UserWcm user) throws WcmException {
        if (user == null) return null;
        try {
            List<Upload> result = em.createNamedQuery("listAllUploads", Upload.class)
                    .getResultList();
            return aclFilter(result, user);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void add(Upload upload, Category cat, UserWcm user)
            throws WcmAuthorizationException, WcmException {
        if (upload == null || cat == null || user == null) return;
        if (!canWrite(cat.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
        }
        upload.add(cat);
        try {
            upload = em.merge(upload);
            cat = em.merge(cat);
            if (!cat.getUploads().contains(upload))
                cat.getUploads().add(upload);
            em.flush();
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void removeUploadCategory(Long uploadId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (uploadId == null || catId == null || user == null) return;
        Upload upload = em.find(Upload.class, uploadId);
        if (!canWrite(upload.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Upload " + upload);
        }
        try {
            Category cat = em.find(Category.class, catId);
            cat.getUploads().remove(upload);
            upload.getCategories().remove(cat);
            em.merge(cat);
            em.merge(upload);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public Upload findUpload(Long id, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (id == null) return null;
        try {
            Upload u = em.find(Upload.class, id);
            if (u.getAcls() != null) {
                if (canRead(u.getAcls(), user)) return u;
                else return null;
            }
            return u;
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Upload> findUploads(Long categoryId, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (user == null) return null;
        try {
            Category cat = em.find(Category.class, categoryId);
            if (cat == null) return null;
            List<Upload> result = new ArrayList<Upload>(cat.getUploads());
            return aclFilter(result, user);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void create(Template temp, UserWcm user)
            throws WcmAuthorizationException, WcmException {
        if (temp == null || user == null) return;
        try {
            em.persist(temp);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Template> findTemplates(Long categoryId, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (user == null) return null;
        try {
            Category cat = em.find(Category.class, categoryId);
            if (cat == null) return null;
            List<Template> result = new ArrayList<Template>(cat.getTemplates());
            // return aclFilter(result, user);
            return result; // In this version we don't have ACL on Template entities
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public List<Template> findTemplates(UserWcm user) throws WcmException {
        if (user == null) return null;
        try {
            List<Template> result = em.createNamedQuery("listAllTemplates", Template.class)
                    .getResultList();
            // return aclFilter(result, user);
            return result; // In this version we don't have ACL on Template entities
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    public Template findTemplate(Long id, UserWcm user) throws WcmException {
        if (user == null) return null;
        if (id == null) return null;
        try {
            Template t = em.find(Template.class, id);
            // In this version we don't have ACL on Template entities
            return t;
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void add(Template template, Category cat, UserWcm user)
            throws WcmAuthorizationException, WcmException {
        if (template == null || cat == null || user == null) return;
        if (!canWrite(cat.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
        }
        try {
            template.getCategories().add(cat);
            template = em.merge(template);
            cat = em.merge(cat);
            if (!cat.getTemplates().contains(template))
                cat.getTemplates().add(template);
            em.flush();
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void deleteTemplate(Long id, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (id == null || user == null) return;
        // In this version we don't have ACL on Template entities
        try {
            Template template = em.getReference(Template.class, id);
            // Template object has not versioning functionality
            for (Category c : template.getCategories()) {
                c.getTemplates().remove(template);
            }
            em.remove(template);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void removeTemplateCategory(Long templateId, Long catId, UserWcm user) throws WcmAuthorizationException, WcmException {
        if (templateId == null || catId == null || user == null) return;
        Template template = em.find(Template.class, templateId);
        Category cat = em.find(Category.class, catId);
        if (!canWrite(cat.getAcls(), user)) {
            throw new WcmAuthorizationException("User: " + user + " has not WRITE rights on Category " + cat);
        }
        try {
            cat.getTemplates().remove(template);
            template.getCategories().remove(cat);
            em.merge(cat);
            em.merge(template);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

    @Override
    public void update(Template template, UserWcm user)
            throws WcmAuthorizationException, WcmException {
        if (template == null || user == null || template.getId() == null) return;
        try {
            em.merge(template);
        } catch (Exception e) {
            throw new WcmException(e);
        }
    }

}
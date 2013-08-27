package org.gatein.lwwcm.services.test;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmAuthorizationException;
import org.gatein.lwwcm.domain.Acl;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Comment;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;
import org.gatein.lwwcm.services.impl.WcmServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WcmServicesTest {

	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "lwwcm-core-tests.war")
            .addPackage(WcmService.class.getPackage())
            .addPackage(Acl.class.getPackage())
            .addPackage(Wcm.class.getPackage())
            .addPackage(WcmServiceImpl.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }	
	
	@EJB
	WcmService w;
	
	@Before
	public void prepareTest() {		
		
	}
	
	@Test
	public void createCategoryBasicTest() throws Exception {
				
		UserWcm user = new UserWcm("usertest");
		user.add("sports");
				
		Category parent = new Category("parent", Wcm.CATEGORIES.FOLDER);
		w.create(parent, user);
		
		Category child = new Category("child", Wcm.CATEGORIES.FOLDER);
		child.setParent(parent);		
		w.create(child, user);
		
		List<Category> out = w.findCategories("parent", user);
		assertEquals(1, out.size());
		assertEquals("parent", out.get(0).getName());
		
		List<Category> children = w.findChildren(out.get(0));
		assertEquals(1, children.size());		
		
		Category newCategory = new Category("sibling", Wcm.CATEGORIES.FOLDER);
		w.create(newCategory, user);
		
		newCategory.setName("sibling-modified");
		newCategory.setParent(parent);
		w.update(newCategory, user);
		
		w.delete(parent, user);
	}
	
	@Test
	public void deleteCategoriesOnCascade() throws Exception {
		
		UserWcm user = new UserWcm("usertest");
		user.add("cascade");
		
		Category c1 = new Category("c1");	
		w.create(c1, user);

		Category c2 = new Category("c2");
		c2.setParent(c1);
		w.create(c2, user);
		
		Category c3 = new Category("c3");
		c3.setParent(c2);
		w.create(c3, user);

		Category c4 = new Category("c4");
		c4.setParent(c3);
		w.create(c4, user);

		Category c5 = new Category("c5");
		c5.setParent(c4);
		w.create(c5, user);
		
		w.delete(c1, user);
		
		assertEquals(0, w.findCategories("c1", user).size());
	}
	
	@Test
	public void validateAclWithCategories() throws Exception {

		final String GROUP1 = "group1";
		final String GROUP2 = "group2";
		final String GROUP3 = "group3";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);

		UserWcm usergroup2 = new UserWcm("usergroup2");
		usergroup2.add(GROUP2);

		UserWcm usergroup3 = new UserWcm("usergroup3");
		usergroup3.add(GROUP3);
				
		Category c1 = new Category("c1");
		w.create(c1, usergroup1);
		
		Category c2 = new Category("c2");
		c2.setParent(c1);
		w.create(c2, usergroup1);
		
		Category c3 = new Category("c3");
		c3.setParent(c2);
		try {
			w.create(c3, usergroup2);
			throw new Exception("GROUP2 should not create c3");
		} catch (WcmAuthorizationException expected) { }
		
		Acl acl = new Acl(GROUP2, Wcm.ACL.WRITE);
		
		c3.add(acl);
		w.create(c3, usergroup1);
		
		Category c4 = new Category("c4");
		c4.setParent(c3);
		w.create(c4, usergroup2);
		
		Category c5 = new Category("c5");
		c5.setParent(c4);
		try {
			w.create(c5, usergroup3);
			throw new Exception("GROUP3 should not create c5");
		} catch (WcmAuthorizationException expected) { }
		
		w.delete(c1, usergroup1);
	}
	
	@Test
	public void validateAclWithCategoriesAndQueries() throws Exception {
		
		final String GROUP1 = "group1";
		final String GROUP2 = "group2";
		final String GROUP3 = "group3";

		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);

		UserWcm usergroup2 = new UserWcm("usergroup2");
		usergroup2.add(GROUP2);

		UserWcm usergroup3 = new UserWcm("usergroup3");
		usergroup3.add(GROUP3);		
		
		Category c1 = new Category("c1", Wcm.CATEGORIES.CATEGORY);
		w.create(c1, usergroup1);
		Category c2 = new Category("c2", Wcm.CATEGORIES.FOLDER);
		w.create(c2, usergroup1);
		Category c3 = new Category("c3", Wcm.CATEGORIES.TAG);
		w.create(c3, usergroup1);
		
		Category c4 = new Category("c4", Wcm.CATEGORIES.CATEGORY);
		w.create(c4, usergroup2);
		Category c5 = new Category("c5", Wcm.CATEGORIES.FOLDER);
		w.create(c5, usergroup2);
		Category c6 = new Category("c6", Wcm.CATEGORIES.TAG);
		w.create(c6, usergroup2);	
		
		Category c7 = new Category("c7", Wcm.CATEGORIES.CATEGORY);
		w.create(c7, usergroup3);
		Category c8 = new Category("c8", Wcm.CATEGORIES.FOLDER);
		w.create(c8, usergroup3);
		Category c9 = new Category("c9", Wcm.CATEGORIES.TAG);
		w.create(c9, usergroup3);	
		
		assertEquals(9, w.findCategories("%", usergroup1).size());
		assertEquals(9, w.findCategories("%", usergroup2).size());
		assertEquals(9, w.findCategories("%", usergroup3).size());
		
		Acl acl = new Acl(GROUP2, Wcm.ACL.NONE);
		c1.add(acl);
		w.update(c1, usergroup1);
		assertEquals(8, w.findCategories("%", usergroup2).size());
		
		assertEquals(3, w.findCategories(Wcm.CATEGORIES.CATEGORY, usergroup1).size());
		assertEquals(3, w.findCategories(Wcm.CATEGORIES.FOLDER, usergroup1).size());
		assertEquals(3, w.findCategories(Wcm.CATEGORIES.TAG, usergroup1).size());
		
		w.delete(c1, usergroup1);
		w.delete(c2, usergroup1);
		w.delete(c3, usergroup1);
		
		w.delete(c4, usergroup2);
		w.delete(c5, usergroup2);
		w.delete(c6, usergroup2);

		w.delete(c7, usergroup3);
		w.delete(c8, usergroup3);
		w.delete(c9, usergroup3);
				
	}
	
	@Test
	public void createBasicPost() throws Exception {
		
		final String GROUP1 = "group1";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);
		
		Post post = new Post();
		
		post.setName("Basic Post");
		post.setTitle("This is a Basic Post in LWWCM");
		post.setExcerpt("LWWCM is a small plugin for GateIn Portal to offer a new lightweight WCM system.");
		post.setContent("There are a lot of valid options in the WCM field. "
				+ "LWWCM is a small module that offers a set of org.gatein.lwwcm.portlet to edit and show content."
				+ "if offers: localization, templates, acl and more features.");
		
		w.create(post, usergroup1);
		
		List<Post> result = w.findPosts("%Basic%", usergroup1);
		assert result != null;
		
		w.delete(post, usergroup1);
	} 
	
	@Test
	public void createPostWithCategory() throws Exception {
		
		final String GROUP1 = "group1";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);		
		
		Post post = new Post("Category Post");
		
		post.setTitle("This is a Category Post in LWWCM");
		post.setExcerpt("LWWCM is a small plugin for GateIn Portal to offer a new lightweight WCM system.");
		post.setContent("There are a lot of valid options in the WCM field. "
				+ "LWWCM is a small module that offers a set of org.gatein.lwwcm.portlet to edit and show content."
				+ "if offers: localization, templates, acl and more features.");				
		w.create(post, usergroup1);
		
		Category cat = new Category("Sports", Wcm.CATEGORIES.CATEGORY);
		w.create(cat, usergroup1);
		
		w.add(post, cat, usergroup1);
		
		// assertEquals(1, cat.getPosts().size());
		assertEquals(1, post.getCategories().size());
		
		w.remove(post, cat, usergroup1);
		
		List<Post> out = w.findPosts("Category Post", usergroup1);
		assertEquals(1, out.size());
		assertEquals(0, out.get(0).getCategories().size());
		
		w.delete(post, usergroup1);		
		w.delete(cat, usergroup1);
	}
	
	@Test
	public void createPostWithComments() throws Exception {

		final String GROUP1 = "group1";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);		
		
		Post post = new Post("Post with Comments");
		
		post.setTitle("This is a Post with Comments in LWWCM");
		post.setExcerpt("LWWCM is a small plugin for GateIn Portal to offer a new lightweight WCM system.");
		post.setContent("There are a lot of valid options in the WCM field. "
				+ "LWWCM is a small module that offers a set of org.gatein.lwwcm.portlet to edit and show content."
				+ "if offers: localization, templates, acl and more features.");				
		w.create(post, usergroup1);
		
		Comment com1 = new Comment();		
		com1.setAuthor("Author1");
		com1.setAuthorEmail("auth1@noexists.test");
		com1.setAuthorUrl("http://auth1.noexist.test");
		com1.setContent("This is a comment from Author1");
		com1.setStatus(Wcm.COMMENT.PUBLIC);
		w.add(post, com1);
		
		Comment com2 = new Comment();		
		com2.setAuthor("Author2");
		com2.setAuthorEmail("auth2@noexists.test");
		com2.setAuthorUrl("http://auth2.noexist.test");
		com2.setContent("This is a comment from Author2");
		com2.setStatus(Wcm.COMMENT.PUBLIC);
		w.add(post, com2);
		
		List<Post> out = w.findPosts("Post with Comments", usergroup1);
		assert out != null;
		assertEquals(1, out.size());
		assertEquals(2, out.get(0).getComments().size());
		
		w.delete(post, usergroup1);		
	}
	
	@Test
	public void deleteCommentFromPost() throws Exception {

		final String GROUP1 = "group1";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);		
		
		Post post = new Post("Deleting Post with Comments");
		
		post.setTitle("This is a Post with Comments in LWWCM");
		post.setExcerpt("LWWCM is a small plugin for GateIn Portal to offer a new lightweight WCM system.");
		post.setContent("There are a lot of valid options in the WCM field. "
				+ "LWWCM is a small module that offers a set of org.gatein.lwwcm.portlet to edit and show content."
				+ "if offers: localization, templates, acl and more features.");				
		w.create(post, usergroup1);
		
		Comment com1 = new Comment();		
		com1.setAuthor("Author1");
		com1.setAuthorEmail("auth1@noexists.test");
		com1.setAuthorUrl("http://auth1.noexist.test");
		com1.setContent("This is a comment from Author1");
		com1.setStatus(Wcm.COMMENT.PUBLIC);
		w.add(post, com1);
		
		Comment com2 = new Comment();		
		com2.setAuthor("Author2");
		com2.setAuthorEmail("auth2@noexists.test");
		com2.setAuthorUrl("http://auth2.noexist.test");
		com2.setContent("This is a comment from Author2");
		com2.setStatus(Wcm.COMMENT.PUBLIC);
		w.add(post, com2);
		
		List<Post> out = w.findPosts("Deleting Post with Comments", usergroup1);
		assert out != null;
		assertEquals(1, out.size());
		assertEquals(2, out.get(0).getComments().size());

		w.remove(out.get(0), out.get(0).getComments().iterator().next(), usergroup1);
				
		out = w.findPosts("Deleting Post with Comments", usergroup1);
		assert out != null;
		assertEquals(1, out.size());
		assertEquals(1, out.get(0).getComments().size());		
		
		w.delete(post, usergroup1);
	}	

	@Test
	public void createUpload() throws Exception {

		final String GROUP1 = "group1";
		
		UserWcm usergroup1 = new UserWcm("usergroup1");
		usergroup1.add(GROUP1);		
		
		String fileName = "persistence.xml";
		
		Upload upload = new Upload();
		upload.setDescription("XML file");
		upload.setFileName(fileName);
		upload.setMimeType("application/xml");
		upload.setUser(usergroup1.getUsername());

		InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml");		
		w.create(upload, is, usergroup1);
		
		List<Upload> out = w.findUploads(fileName, usergroup1);
		assertEquals(1, out.size());

        w.delete(upload, usergroup1);
	}

    @Test
    public void createUploadWithCategories() throws Exception {
        final String GROUP1 = "group1";

        UserWcm usergroup1 = new UserWcm("usergroup1");
        usergroup1.add(GROUP1);

        String fileName = "persistence.xml";

        Upload upload = new Upload();
        upload.setDescription("XML file");
        upload.setFileName(fileName);
        upload.setMimeType("application/xml");
        upload.setUser(usergroup1.getUsername());

        InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml");
        w.create(upload, is, usergroup1);

        List<Upload> out = w.findUploads(fileName, usergroup1);
        assertEquals(1, out.size());

        Category cat = new Category("Sports", Wcm.CATEGORIES.CATEGORY);
        w.create(cat, usergroup1);

        w.add(upload, cat, usergroup1);

        // assertEquals(1, cat.getUploads().size());   Lazy colletion findUploads(Category) instead
        assertEquals(1, upload.getCategories().size());

        w.remove(upload, cat, usergroup1);

        out = w.findUploads(fileName, usergroup1);
        assertEquals(1, out.size());
        assertEquals(0, out.get(0).getCategories().size());

        w.delete(upload, usergroup1);
        w.delete(cat, usergroup1);
    }

    @Test
    public void createUploadWithCategoriesAttached() throws Exception {
        final String GROUP1 = "group1";

        UserWcm usergroup1 = new UserWcm("usergroup1");
        usergroup1.add(GROUP1);

        String fileName = "persistence.xml";

        Upload upload = new Upload();
        upload.setDescription("XML file");
        upload.setFileName(fileName);
        upload.setMimeType("application/xml");
        upload.setUser(usergroup1.getUsername());

        InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml");
        w.create(upload, is, usergroup1);

        List<Upload> out = w.findUploads(fileName, usergroup1);
        assertEquals(1, out.size());

        Category cat = new Category("Sports", Wcm.CATEGORIES.CATEGORY);
        w.create(cat, usergroup1);
        // Check if relation is consistent
        cat = w.findCategory(cat.getId(), usergroup1);

        w.add(upload, cat, usergroup1);

        // assertEquals(1, cat.getUploads().size());  Lazy colletion -> findUploads(Category) instead
        assertEquals(1, upload.getCategories().size());

        w.remove(upload, cat, usergroup1);

        out = w.findUploads(fileName, usergroup1);
        assertEquals(1, out.size());
        assertEquals(0, out.get(0).getCategories().size());

        w.delete(upload, usergroup1);
        w.delete(cat, usergroup1);
    }

	@After
	public void finishTest() {
		
	}
	
}

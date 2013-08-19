package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gatein.lwwcm.Wcm;

/**
 * Post represents a content publication in the wcm system.
 */
@Entity
@Table(name = "lwwcm_posts")
@Cacheable
@NamedQueries({
		@NamedQuery(name = "listPostsName", query = "from Post p where p.name like :name")})
public class Post implements Serializable {	

	private Long id;
	private Long version;
	private String author;
	private Calendar created;
	private String content;
	private String title;
	private String excerpt;
	private Character postStatus;
	private String name;
	private Calendar modified;
	private String group;
	private String locale;
	private Character commentsStatus;
	private Set<Comment> comments = new HashSet<Comment>();
	private Set<Category> categories = new HashSet<Category>();
	private Set<Acl> acls = new HashSet<Acl>();
	
	public Post() {
		this.version = 1l;
		this.created = Calendar.getInstance();
		this.postStatus = Wcm.POSTS.DRAFT;
		this.locale = Locale.getDefault().getLanguage();
		this.commentsStatus = Wcm.COMMENTS.ANONYMOUS;
	}
	
	public Post(String name) {
		this();			
		this.name = name;
	}
	
	public Post(String name, String locale) {
		this(name);
		this.locale = locale;
	}
	
	@Id @GeneratedValue
	@Column(name = "post_id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "post_version")	
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}	
	
	@Column(name = "post_author")
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Column(name = "post_created")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}
	
	@Lob
	@Column(name = "post_content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "post_title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "post_excerpt")
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	
	@Column(name = "post_status")
	public Character getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(Character postStatus) {
		this.postStatus = postStatus;
	}
	
	@Column(name = "post_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "post_modified")
	@Temporal(TemporalType.TIMESTAMP)	
	public Calendar getModified() {
		return modified;
	}
	public void setModified(Calendar modified) {
		this.modified = modified;
	}
	
	@Column(name = "post_group")
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}

	@Column(name = "post_locale")
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@Column(name = "post_comment_status")	
	public Character getCommentsStatus() {
		return commentsStatus;
	}

	public void setCommentsStatus(Character commentsStatus) {
		this.commentsStatus = commentsStatus;
	}

	@OneToMany(mappedBy = "post", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	public Set<Comment> getComments() {
		return comments;
	}
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	public void add(Comment c) {
		if (c == null) return;
		c.setPost(this);
		this.comments.add(c);
	}
	
	@ManyToMany(mappedBy = "posts", fetch = FetchType.EAGER)
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public void add(Category cat) {
		if (cat == null) return;
		if (categories.contains(cat)) return;
		categories.add(cat);
	}
	public void remove(Category cat) {
		if (cat == null) return;
		categories.remove(cat);		
	}
	
	@OneToMany(mappedBy = "post", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	public Set<Acl> getAcls() {
		return acls;
	}
	public void setAcls(Set<Acl> acls) {
		this.acls = acls;
	}
	public void add(Acl acl) {
		if (acl == null) return;
		acl.setPost(this);
		this.acls.add(acl);
	}
	public void remove(Acl acl) {
		if (acl == null) return;
		this.acls.remove(acl);
	}
	
	@Override
	public String toString() {
		return "Post [id=" + id + ", author=" + author + ", created=" + created
				+ ", postStatus=" + postStatus + ", name=" + name
				+ ", modified=" + modified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((excerpt == null) ? 0 : excerpt.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result
				+ ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((postStatus == null) ? 0 : postStatus.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (excerpt == null) {
			if (other.excerpt != null)
				return false;
		} else if (!excerpt.equals(other.excerpt))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (postStatus == null) {
			if (other.postStatus != null)
				return false;
		} else if (!postStatus.equals(other.postStatus))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}


}
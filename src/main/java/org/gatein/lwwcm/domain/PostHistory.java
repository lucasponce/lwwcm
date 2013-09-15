package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

/**
 * PostHistory will store older posts.
 * Older post will implement a versioning utility inside wcm.
 */
@Entity
@Table(name = "lwwcm_posts_history")
@IdClass(PostHistoryPK.class)
public class PostHistory implements Serializable {

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
	private Calendar deleted;
	private String group;
	private String locale;

	@Id
	@Column(name = "post_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Id
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

    @Lob
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
	
	@Column(name = "post_deleted")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDeleted() {
		return deleted;
	}
	public void setDeleted(Calendar deleted) {
		this.deleted = deleted;
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
	
	@Override
	public String toString() {
		return "PostHistory [id=" + id + ", version=" + version + ", author="
				+ author + ", created=" + created + ", content=" + content
				+ ", title=" + title + ", excerpt=" + excerpt + ", postStatus="
				+ postStatus + ", name=" + name + ", modified=" + modified
				+ ", group=" + group + ", locale=" + locale + "]";
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
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		PostHistory other = (PostHistory) obj;
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
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}	
	
}
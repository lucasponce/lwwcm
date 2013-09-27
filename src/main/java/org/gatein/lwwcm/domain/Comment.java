package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

/**
 * Comments are attached to posts.
 */
@Entity
@Table(name = "lwwcm_comments")
@Cacheable
public class Comment implements Serializable {

	private Long id;
	private Post post;
	private String author;
	private String authorEmail;
	private String authorUrl;
	private Calendar created;
	private String content;
	private Character status;
	
	public Comment() {
		this.created = Calendar.getInstance();
	}
	
	@Id @GeneratedValue
	@Column(name = "comment_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "comment_post_id")
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	
	@Column(name = "comment_author")
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Column(name = "comment_author_email")
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
	@Column(name = "comment_author_url")
	public String getAuthorUrl() {
		return authorUrl;
	}
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}
	
    @Column(name = "comment_created")
	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}
	
	@Column(name = "comment_content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "comment_status")
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", author=" + author
				+ ", authorEmail=" + authorEmail + ", authorUrl=" + authorUrl
				+ ", created=" + created + ", content=" + content + ", status="
				+ status + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((authorEmail == null) ? 0 : authorEmail.hashCode());
		result = prime * result
				+ ((authorUrl == null) ? 0 : authorUrl.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Comment other = (Comment) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (authorEmail == null) {
			if (other.authorEmail != null)
				return false;
		} else if (!authorEmail.equals(other.authorEmail))
			return false;
		if (authorUrl == null) {
			if (other.authorUrl != null)
				return false;
		} else if (!authorUrl.equals(other.authorUrl))
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
}
package org.gatein.lwwcm.domain;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Acl defines rights for content manipulation.
 * Acl can be linked to a Post or a Category.
 * Acl is also linked with users or groups.
 * Users and groups are read from GateIn Portal Organization service.
 */
@Entity
@Table(name = "lwwcm_security_acl")
@Cacheable
@NamedQueries({ 
	@NamedQuery(name = "listAclCategoryPrincipal", query = "from Acl a where a.principal = :principal and a.category is not null and a.category.id = :categoryId"),
	@NamedQuery(name = "deleteAclCategory", query = "delete from Acl a where a.category is not null and a.category.id = :id")
})
public class Acl implements Serializable {

	private Long id;
	/**
	 * principal can be an username or a groupname
	 */
	private String principal;
	private Character permission;
	private Category category;
	private Post post;
	private Upload upload;
	
	public Acl() { }
	
	public Acl(String principal, Character permission) {
		this.principal = principal;
		this.permission = permission;
	}
	
	@Id @GeneratedValue
	@Column(name = "acl_id")		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "acl_principal")
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	@Column(name = "acl_type")
	public Character getPermission() {
		return permission;
	}
	public void setPermission(Character permission) {
		this.permission = permission;
	}
	
	@ManyToOne
	@JoinColumn(name = "acl_category_id")	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {		
		this.category = category;
	}	
	
	@ManyToOne
	@JoinColumn(name = "acl_post_id")		
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}	
	
	@ManyToOne
	@JoinColumn(name = "acl_upload_id")
	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	@Override
	public String toString() {
		return "Acl [id=" + id + ", principal=" + principal + ", permission="
				+ permission + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result
				+ ((principal == null) ? 0 : principal.hashCode());
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
		Acl other = (Acl) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		return true;
	}
	
}
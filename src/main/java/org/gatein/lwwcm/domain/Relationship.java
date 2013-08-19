package org.gatein.lwwcm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * It represents a relationship between content.
 * Implements where two content are related by a non-trivial relation.
 * For example, translations between posts or documents.
 */
@Entity
@Table(name = "lwwcm_relationships")
public class Relationship implements Serializable {
	
	private Long id;
	private Post origin;
	private String key;
	private Post alias;

	@Id
	@Column(name = "relationship_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "relationship_origin_id")		
	public Post getOrigin() {
		return origin;
	}
	public void setOrigin(Post origin) {
		this.origin = origin;
	}
	
	@Column(name = "relationship_key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@ManyToOne
	@JoinColumn(name = "relationship_alias_id")			
	public Post getAlias() {
		return alias;
	}
	public void setAlias(Post alias) {
		this.alias = alias;
	}
	
	@Override
	public String toString() {
		return "Relationship [id=" + id + ", origin=" + origin + ", key=" + key
				+ ", alias=" + alias + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
		Relationship other = (Relationship) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}
	
}
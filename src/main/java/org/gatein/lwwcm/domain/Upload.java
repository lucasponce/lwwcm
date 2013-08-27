package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Upload represents files uploaded into wcm as images or resources.
 * These resources will be accessible throw special wcm links.
 * Files binary are stored directly into file system, this class represents metadata of the file.
 */
@Entity
@Table(name = "lwwcm_uploads")
@Cacheable
@NamedQueries({
	@NamedQuery(name = "listUploadsFileName", query = "from Upload u where u.fileName like :fileName"),
    @NamedQuery(name = "listAllUploads", query = "from Upload u order by u.id")
})
public class Upload implements Serializable {
	
	private Long id;
	private Long version;
	private String fileName;
	private String storedName;
	private String mimeType;
	private Calendar created;
	private Calendar modified;
	private String user; 
	private String description;
    private Set<Category> categories = new HashSet<Category>();
	private Set<Acl> acls = new HashSet<Acl>();

	public Upload() {
		this.created = Calendar.getInstance();
		this.version = 1l;
	}
	
	@Id @GeneratedValue
	@Column(name = "upload_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "upload_version")	
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Column(name = "upload_filename")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "upload_storedname")
	public String getStoredName() {
		return storedName;
	}
	public void setStoredName(String storedName) {
		this.storedName = storedName;
	}
	
	@Column(name = "upload_mimetype")
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	@Column(name = "upload_created")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}

	@Column(name = "upload_modified")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getModified() {
		return modified;
	}
	public void setModified(Calendar modified) {
		this.modified = modified;
	}

	@Column(name = "upload_user")
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	@Column(name = "upload_description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @ManyToMany(mappedBy = "uploads", cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
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

	@OneToMany(mappedBy = "upload", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	public Set<Acl> getAcls() {
		return acls;
	}
	public void setAcls(Set<Acl> acls) {
		this.acls = acls;
	}
	public void add(Acl acl) {
		if (acl == null) return;
		acl.setUpload(this);
		this.acls.add(acl);
	}
	public void remove(Acl acl) {
		if (acl == null) return;
		this.acls.remove(acl);
	}	
	
	@Override
	public String toString() {
		return "Upload [id=" + id + ", fileName=" + fileName + ", storedName="
				+ storedName + ", mimeType=" + mimeType + ", created="
				+ created + ", user=" + user + ", description=" + description
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result
				+ ((storedName == null) ? 0 : storedName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Upload other = (Upload) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		if (storedName == null) {
			if (other.storedName != null)
				return false;
		} else if (!storedName.equals(other.storedName))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
			
}
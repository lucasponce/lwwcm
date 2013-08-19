package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UploadHistory will store versioning of uploaded files.  
 */
@Entity
@Table(name = "lwwcm_uploads_history")
@IdClass(UploadHistoryPK.class)
public class UploadHistory implements Serializable {

	private Long id;
	private Long version;
	private String fileName;
	private String storedName;
	private String mimeType;
	private Calendar created;
	private Calendar modified;
	private Calendar deleted;
	private String user;
	private String description;	
	
	@Id
	@Column(name = "upload_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Id
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
	
	@Column(name = "upload_deleted")
	public Calendar getDeleted() {
		return deleted;
	}
	public void setDeleted(Calendar deleted) {
		this.deleted = deleted;
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
	
	@Override
	public String toString() {
		return "UploadHistory [id=" + id + ", version=" + version
				+ ", fileName=" + fileName + ", storedName=" + storedName
				+ ", mimeType=" + mimeType + ", created=" + created + ", user="
				+ user + ", description=" + description + "]";
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
		UploadHistory other = (UploadHistory) obj;
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
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}	
	
}
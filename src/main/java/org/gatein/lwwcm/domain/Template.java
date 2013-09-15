package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Template is a representation of the html fragment that wcm will use to render content.
 * A WCM org.gatein.lwwcm.portlet will combine a template with posts to render a response.
 */
@Entity
@Table(name = "lwwcm_templates")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "listTemplatesName", query = "from Template t where upper(t.name) like :name order by t.modified desc"),
        @NamedQuery(name = "listAllTemplates", query = "from Template t order by t.modified desc")
})
public class Template implements Serializable {

	private Long id;
	private Character type;
	private String name;
	private String content;
	private String locale;
    private Calendar created;
    private Calendar modified;
    private String user;
    private Set<Category> categories = new HashSet<Category>();

    public Template() {
        this.created = Calendar.getInstance();
        this.modified = (Calendar)this.created.clone();
    }

    @Id @GeneratedValue
	@Column(name = "template_id")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "template_type")
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
	
	@Column(name = "template_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Lob
	@Column(name = "template_content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "template_locale")
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}

    @Column(name = "template_created")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getCreated() {
        return created;
    }
    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Column(name = "template_modified")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getModified() {
        return modified;
    }
    public void setModified(Calendar modified) {
        this.modified = modified;
    }

    @Column(name = "template_user")
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    @ManyToMany(mappedBy = "templates", cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
	public String toString() {
		return "Template [id=" + id + ", type=" + type + ", name=" + name
				+ ", content=" + content + ", locale=" + locale + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Template other = (Template) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
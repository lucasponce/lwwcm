package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Categories group content. 
 * Category have a tree structure with Category's parent reference.
 * Categories can be used to grant rights for creation or publishing in an ACL style.
 */
@Entity
@Table(name = "lwwcm_categories")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "listAllCategories", query = "from Category c order by c.id"),
		@NamedQuery(name = "listCategoriesName", query = "from Category c where c.name like :name"),
		@NamedQuery(name = "listCategoriesType", query = "from Category c where c.type = :type"),
		@NamedQuery(name = "listCategoriesNameType", query = "from Category c where c.name like :name and c.type = :type"),
		@NamedQuery(name = "listCategoriesChildren", query = "from Category c where c.parent is not null and c.parent.id = :id"),
        @NamedQuery(name = "listRootCategories", query = "from Category c where c.parent is null order by c.id")
})
public class Category implements Serializable {

	private Long id;
	private String name;
	private Character type;
	private Set<Post> posts = new HashSet<Post>();
    private Set<Upload> uploads = new HashSet<Upload>();
    private Set<Template> templates = new HashSet<Template>();
	private Category parent;
	private Set<Acl> acls = new HashSet<Acl>();
    private int numChildren;
	
	public Category() { }
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category(String name, Character type) {
		this.name = name;
		this.type = type;
	}
	
	@Id @GeneratedValue
	@Column(name = "category_id")		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "category_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "category_type")
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lwwcm_categories_posts", 
			joinColumns = { @JoinColumn(name = "category_id", referencedColumnName = "category_id") },
			inverseJoinColumns = { @JoinColumn(name = "post_id", referencedColumnName = "post_id") })
	public Set<Post> getPosts() {
		return posts;
	}
	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	public void add(Post post) {
		if (post == null) return;
		if (posts.contains(post)) return;
		posts.add(post);
	}
	public void remove(Post post) {
		if (post == null) return;
		this.posts.remove(post);		
	}

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lwwcm_categories_uploads",
            joinColumns = { @JoinColumn(name = "category_id", referencedColumnName = "category_id") },
            inverseJoinColumns = { @JoinColumn(name = "upload_id", referencedColumnName = "upload_id") })
    public Set<Upload> getUploads() {
        return uploads;
    }
    public void setUploads(Set<Upload> uploads) {
        this.uploads = uploads;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lwwcm_categories_templates",
            joinColumns = { @JoinColumn(name = "category_id", referencedColumnName = "category_id") },
            inverseJoinColumns = { @JoinColumn(name = "template_id", referencedColumnName = "template_id") })
    public Set<Template> getTemplates() {
        return templates;
    }
    public void setTemplates(Set<Template> templates) {
        this.templates = templates;
    }

	@ManyToOne
	@JoinColumn(name = "category_parent_id")
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy = "category", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	public Set<Acl> getAcls() {
		return acls;
	}
	public void setAcls(Set<Acl> acls) {
		this.acls = acls;
	}
	public void add(Acl acl) {
		if (acl == null) return;
		acl.setCategory(this);
		this.acls.add(acl);
	}
	public void remove(Acl acl) {
		if (acl == null) return;
		this.acls.remove(acl);
	}

    @Transient
    public int getNumChildren() {
        return numChildren;
    }
    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    @PreRemove
	private void preRemove() {
		
	}
	
	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
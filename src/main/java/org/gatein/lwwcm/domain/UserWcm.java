package org.gatein.lwwcm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User representacion.
 * Users are managed in GateIn Portal.
 * Authorization is managed in GateIn Portal using groups under an specific path.
 * For example:
 * 	/wcm/group1
 *  /wcm/group2
 *  /wcm/group3
 *  
 *  All groups under /wcm/* path will be used to authorization inside lwwcm system.
 *  It's responsability to user to popule only these groups.
 *  
 * This class is not persisted due we don't need to maintain two models of users, it's used as a helper for some functions.
 */
public class UserWcm implements Serializable {

	private String username;
	private Set<String> groups = new HashSet<String>();
	
	public UserWcm() { }
	
	public UserWcm(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void add(String group) {
		if (group == null) return;
		if (groups.contains(group)) return;
		groups.add(group);
	}
	
	public void remove(String group) {
		if (group == null) return;
		groups.remove(group);
	}

	@Override
	public String toString() {
		return "UserWcm [username=" + username + ", groups=" + groups + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		UserWcm other = (UserWcm) obj;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}

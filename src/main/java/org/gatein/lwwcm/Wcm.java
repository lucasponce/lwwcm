package org.gatein.lwwcm;


/**
 * Global interface with wcm constants.
 */
public interface Wcm {
	
	interface GROUPS {
		static final String ADMINISTRATORS = "/platform/administrators";
        static final String EDITOR = "/wcm/editor";
		static final String ALL = "*";
	}
	
	interface ACL {	
		static final Character WRITE = 'W';
		static final Character READ = 'R';
		static final Character NONE = 'N';
		static final Character PUBLISHER = 'P';
	}
	
	interface CATEGORIES {
		static final Character CATEGORY = 'C';
		static final Character TAG = 'T';
		static final Character FOLDER = 'F';
	}
	
	interface POSTS {
		static final Character PUBLIC = 'P';
		static final Character PRIVATE = 'V';
		static final Character DRAFT = 'D';
	}
	
	interface COMMENTS {
		static final Character ANONYMOUS = 'A';
		static final Character LOGGED = 'L';
		static final Character NO_COMMENTS = 'N';
	}
	
	interface COMMENT {
		static final Character PUBLIC = 'P';
		static final Character REJECTED = 'R';
		static final Character DELETED = 'D';
	}
	
	interface UPLOADS {
		static final String FOLDER = "gatein.lwwcm.uploads.folder";
		static final String DEFAULT = "/tmp/lwwcm";
		static final int LENGTH_BUFFER = 16384;
	}
	
}
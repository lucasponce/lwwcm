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
		static final Character PUBLISHED = 'P';
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
        static final String TEMP_DIR = "/tmp";
		static final int LENGTH_BUFFER = 16384;
        static final int MAX_FILE_SIZE = 5242880;   // 5 Mb as default max size for file upload
	}

    interface TEMPLATES {
        static final Character SINGLE = 'S';
        static final Character LIST = 'L';
    }

    interface VIEWS {
        static int MAX_PER_PAGE = 3;
        static final String POSTS = "posts";
        static final String CATEGORIES = "categories";
        static final String UPLOADS = "uploads";
        static final String TEMPLATES = "templates";
        static final String NEW_POST = "newpost";
        static final String NEW_CATEGORY = "newcategory";
        static final String NEW_UPLOAD = "newupload";
        static final String NEW_TEMPLATE = "newtemplate";
        static final String EDIT_CATEGORY = "editcategory";
        static final String EDIT_UPLOAD = "editupload";
        static final String EDIT_TEMPLATE = "edittemplate";
        static final String EDIT_POST = "editpost";
    }

    interface ACTIONS {
        static final String POSTS = "posts";
        static final String NEW_CATEGORY = "newcategory";
        static final String DELETE_CATEGORY = "deletecategory";
        static final String EDIT_CATEGORY = "editcategory";
        static final String NEW_UPLOAD = "newupload";
        static final String RIGHT_UPLOADS = "rightuploads";
        static final String LEFT_UPLOADS = "leftuploads";
        static final String EDIT_UPLOAD = "editupload";
        static final String ADD_CATEGORY_UPLOAD = "addcategoryupload";
        static final String FILTER_CATEGORY_UPLOADS = "filtercategoryuploads";
        static final String DELETE_UPLOAD = "deleteupload";
        static final String DELETE_SELECTED_UPLOAD = "deleteselectedupload";
        static final String ADD_SELECTED_CATEGORY_UPLOAD = "addselectedcategoryupload";
        static final String REMOVE_CATEGORY_UPLOAD = "removecategoryupload";
        static final String NEW_TEMPLATE = "newtemplate";
        static final String DELETE_SELECTED_TEMPLATE = "deleteselectedtemplate";
        static final String ADD_SELECTED_CATEGORY_TEMPLATE = "addselectedcategorytemplate";
        static final String FILTER_CATEGORY_TEMPLATES = "filtercategorytemplates";
        static final String RIGHT_TEMPLATES = "righttemplates";
        static final String LEFT_TEMPLATES = "lefttemplates";
        static final String ADD_CATEGORY_TEMPLATE = "addcategorytemplate";
        static final String REMOVE_CATEGORY_TEMPLATE = "removecategorytemplate";
        static final String EDIT_TEMPLATE = "edittemplate";
        static final String DELETE_TEMPLATE = "deletetemplate";
        static final String NEW_POST = "newpost";
        static final String RIGHT_POSTS = "rightposts";
        static final String LEFT_POSTS = "leftposts";
        static final String EDIT_POST = "editpost";
        static final String ADD_CATEGORY_POST = "addcategorypost";
        static final String FILTER_CATEGORY_POSTS = "filtercategoryposts";
        static final String DELETE_POST = "deletepost";
        static final String DELETE_SELECTED_POST = "deleteselectedpost";
        static final String ADD_SELECTED_CATEGORY_POST = "addselectedcategorypost";
        static final String REMOVE_CATEGORY_POST = "removecategorypost";
        static final String PUBLISH_POST = "publishpost";
        static final String PUBLISH_POSTS = "draftpost";
    }

    interface EVENTS {
        static final String SHOW_CATEGORIES_CHILDREN = "showcategorieschildren";
        static final String DOWNLOAD_UPLOAD = "downloadupload";
    }


}
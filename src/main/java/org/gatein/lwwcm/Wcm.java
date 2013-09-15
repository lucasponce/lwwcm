package org.gatein.lwwcm;


/**
 * Global interface with wcm constants.
 */
public interface Wcm {

    static final String CONFIGURATION_FILE = "lwwcm.properties";
	
	interface GROUPS {
		static final String ADMINISTRATORS = "/platform/administrators";
        static final String EDITOR = (System.getProperty("lwwcm.groups.editor") == null ? "/wcm/editor" : System.getProperty("lwwcm.groups.editor"));
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
		static final String FOLDER = "lwwcm.uploads.folder";
		static final String DEFAULT = "jboss.server.data.dir";
        static final String TMP_DIR = "java.io.tmpdir";
		static final int LENGTH_BUFFER = (System.getProperty("lwwcm.uploads.length_buffer") == null ? 5242880 : new Integer(System.getProperty("lwwcm.uploads.length_buffer")));
        static final int MAX_FILE_SIZE = (System.getProperty("lwwcm.uploads.max_file_size") == null ? 5242880 : new Integer(System.getProperty("lwwcm.uploads.max_file_size"))); // 5 Mb as default max size for file upload
	}

    interface TEMPLATES {
        static final Character SINGLE = 'S';
        static final Character LIST = 'L';
    }

    interface VIEWS {
        static final int MAX_PER_PAGE = (System.getProperty("lwwcm.views.max_per_page") == null ? 3 : new Integer(System.getProperty("lwwcm.views.max_per_page")));
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
        static final String FILTER_NAME_UPLOADS = "filternameuploads";
        static final String DELETE_UPLOAD = "deleteupload";
        static final String DELETE_SELECTED_UPLOAD = "deleteselectedupload";
        static final String ADD_SELECTED_CATEGORY_UPLOAD = "addselectedcategoryupload";
        static final String REMOVE_CATEGORY_UPLOAD = "removecategoryupload";
        static final String NEW_TEMPLATE = "newtemplate";
        static final String DELETE_SELECTED_TEMPLATE = "deleteselectedtemplate";
        static final String ADD_SELECTED_CATEGORY_TEMPLATE = "addselectedcategorytemplate";
        static final String FILTER_CATEGORY_TEMPLATES = "filtercategorytemplates";
        static final String FILTER_NAME_TEMPLATES = "filternametemplates";
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
        static final String FILTER_NAME_POSTS = "filternameposts";
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
        static final String SHOW_POST_UPLOADS = "showpostuploads";
    }

    interface CONFIG {
        interface ACTIONS {
            static final String SAVE_CONFIGURATION = "changetemplate";
        }

        interface VIEWS {
        }

        interface EVENTS {
            static final String CHANGE_CHOOSE_CONTENT = "changechoosecontent";
            static final String NEW_CONTENT_ATTACHED = "newcontentattached";
            static final String DELETE_CONTENT_ATTACHED = "deletecontentattached";
        }
    }

    interface SUFFIX {
        static final String POST = "post";
        static final String CATEGORY = "cat";
        static final String ID = "id";
        static final String NAME = "name";
    }
}
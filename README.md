LightWeight WCM for GateIn (LW WCM)
==================================

This project is a lightweight Web Content Management (WCM) extension for GateIn Portal.

Requeriments
------------

- JDK 1.7
- Maven 3
- GateIn Portal 3.6.0 for JBoss AS 7, or
- JBoss Portal 6.1.0 Beta

Installation
------------

[0] Check you have a correct installation of GateIn Portal for JBoss AS 7 / JBoss Portal.

[1] Update fileuploads dependency

    - In GateIn Portal open $GATEIN/modules/org/apache/commons/fileupload/main/module.xml
    - In JBoss Portal open $JPP/jboss-jpp-6.1/modules/system/add-ons/gatein/org/apache/commons/fileupload/main/module.xml
    - Add org.apache.commons.io dependency:

        <dependencies>
            [...]
            <module name="org.apache.commons.io" />
            [...]
        </dependencies>

[2] Install lwwcmDS datasource

    - For demo porpuses we are using h2 database embedded in GateIn Portal / JBoss Portal, but LW WCM is compatible with any database with JPA / Hibernate SQL dialect.
    - In GateIn Portal / JBoss Portal open $GATEIN/standalone/configuration/standalone.xml
    - Add a new datasource:

        [...]
        <datasource jndi-name="java:jboss/datasources/lwwcmDS" pool-name="lwwcmDS" enabled="true" use-java-context="true">
            <connection-url>jdbc:h2:file:${jboss.server.data.dir}/lwwcm/db/lwwcm_database;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE</connection-url>
            <driver>h2</driver>
            <security>
                <user-name>sa</user-name>
                <password>sa</password>
            </security>
        </datasource>
        [...]

[3] Install lwwcm.properties

    - Browse in lwwcm project and locate examples/lwwcm.properties file
    - Copy $PROJECT/examples/lwwcm.properties file to $GATEIN/standalone/configuration

[4] build and deploy lwwcm.war artifact

    - cd $PROJECT
    - mvn clean package
    - copy target/lwwcm.war to $GATEIN/standalone/deployments

While LW WCM should build without any extra maven repository configuration it may happen that the build complains about missing artifacts.

Check "Maven dependencies issues" for GateIn Portal in the following link https://github.com/gatein/gatein-portal

Configuration
-------------

[1] Start GateIn Portal / JBoss Portal

    - cd $GATEIN
    - bin/standalone.sh
    - Open a browser and access to http://localhost:8080/portal or configured URL where GateIn Portal / JBoss Portal is running

[2] Configuration of LW WCM groups

    - Access GateIn Portal / JBoss Portal as root user
    - Access to Group > Organization > Users and groups management
    - Create a new group from root parent called "wcm"
    - Create a new group under "/wcm" called "editor"
    - Check that you have a "/wcm" group and a "/wcm/editor" group.
    - "/wcm" represents all users that can access to LW WCM Editor application.
    - "/wcm/editor" represents a group with write rights.

[3] Configuration of LW WCM applications

    - Access GateIn Portal / JBoss Portal as root user
    - Access to Group > Administration > Application Registry
    - Add a new category called "WCM", set permission to all members ("*") of "/wcm" group.
    - Add "LightWeight WCM Content" application to "WCM" category.
    - Add "LightWeight WCM Editor" application to "WCM" category.

[4] Configuration of "wcm" group navigation

    - Access GateIn Portal / JBoss Portal as root user
    - Access to Group > Manage Groups
    - Add Navigation for "/wcm" group
    - Edit "/wcm" Navigation:
        - Add Node
    - In "Page Node Setting" tab:
        - Add new Node called "wcm" for "/wcm" Navigation with label "LW WCM Editor"
    - In "Page Selector" tab:
        - Create new Page with Page Id "wcm" and Title "LW WCM Editor"
    - Save dialog.
    - Edit Node "LW WCM Editor"'s page.
    - Editing the page:
        - Add a "LightWeight WCM Editor" into the empty layout of the page.
        - Check that "LightWeight WCM Editor" portlet has access permission to "/wcm" group with "*" membership.
    - Save and close.
    - Check that under Group menu there is a new menu called "wcm's pages" with LW WCM Editor node.
    - Access and check that you see a empty WCM editor with no data.

Getting Started
---------------

A quick way to start learning LW WCM is to import a site.

You can download a prepackaged site under this link:

https://dl.dropboxusercontent.com/u/22093229/pdf/data.zip

Once downloaded you can import into your GateIn Portal / JBoss Portal with the following steps:

[1] Stop GateIn Portal / JBoss Portal

[2] Make a backup of your $GATEIN/standalone/data folder

[3] Unzip downloaded data.zip into $GATEIN/standalone to replace previous data folder.

[4] Run GateIn Portal / JBoss Portal

[5] Start browsing into TicketMonster's Magazine site !!

![alt tag](https://raw.github.com/lucasponce/lwwcm/master/lwwcm-preview.png)

Documentation
-------------

Project is in a early stage and we haven't written a proper documentation yet.
This is on our priorities, meanwhile you can always browse into the source code.

[Intro to GateIn lightweight WCM](http://www.slideshare.net/ponceballesteros/gatein-lightweight-web-content-management)

Feedback
--------

Please, feel free to give feedback for future modifications.

Thanks !







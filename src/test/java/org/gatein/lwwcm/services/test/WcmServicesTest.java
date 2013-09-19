package org.gatein.lwwcm.services.test;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;

import org.gatein.lwwcm.Wcm;
import org.gatein.lwwcm.WcmAuthorizationException;
import org.gatein.lwwcm.domain.Acl;
import org.gatein.lwwcm.domain.Category;
import org.gatein.lwwcm.domain.Comment;
import org.gatein.lwwcm.domain.Post;
import org.gatein.lwwcm.domain.Upload;
import org.gatein.lwwcm.domain.UserWcm;
import org.gatein.lwwcm.services.WcmService;
import org.gatein.lwwcm.services.impl.WcmServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WcmServicesTest {

	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "lwwcm-tests.war")
            .addPackage(WcmService.class.getPackage())
            .addPackage(Acl.class.getPackage())
            .addPackage(Wcm.class.getPackage())
            .addPackage(WcmServiceImpl.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }	
	
	@EJB
	WcmService w;
	
	@Before
	public void prepareTest() {		
		
	}

	@After
	public void finishTest() {
		
	}
	
}

package org.gatein.lwwcm.misc.test;

import junit.framework.Assert;
import org.gatein.lwwcm.services.impl.WcmServiceImpl;
import org.junit.Test;

public class ExtractPath {

    @Test
    public void extractPath() {

        WcmServiceImpl wsi = new WcmServiceImpl();

        String test1 = "/one/two/three";
        Assert.assertEquals("three", wsi.child(test1));
        Assert.assertEquals("/one/two", wsi.parent(test1));

        String test2 = "/one/two/";
        Assert.assertEquals("", wsi.child(test2));
        Assert.assertEquals("/one/two", wsi.parent(test2));

        String test3 = "pepe";
        Assert.assertEquals("pepe", wsi.child(test3));
        Assert.assertEquals("", wsi.parent(test3));

        String test4 = "/";
        Assert.assertEquals("", wsi.child(test4));
        Assert.assertEquals("", wsi.parent(test4));
    }
}

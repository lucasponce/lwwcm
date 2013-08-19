package org.gatein.lwwcm.misc.test;

import java.util.Locale;

import org.junit.Test;

public class MiscellaneousTest {

	@Test
	public void getDefaultLocale() {
		System.out.println(Locale.getDefault().getLanguage());
	}
	
	@Test
	public void getTempDirectory() {
		System.out.println(System.getProperty("java.io.tempdir"));
	}
	
	@Test
	public void getResourceAsStream() {
		System.out.println( getClass().getClassLoader().getResource("test-persistence.xml") );
	}
}

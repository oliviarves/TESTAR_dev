package nl.ou.testar.a11y.wcag;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.ou.testar.a11y.wcag.Principle;

public class PrincipleTest {

	@Test
	public void testGetNr() {
		Principle p = new Principle(1, "Foo");
		assertEquals("1", p.getNr());
	}

}
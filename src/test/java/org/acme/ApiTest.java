package org.acme;


import static api.tabular.Properties.*;
import static api.tabular.Tables.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

import org.junit.Test;

import api.tabular.Column;
import api.tabular.Properties;
import api.tabular.Property;
import api.tabular.Row;
import api.tabular.Table;

public class ApiTest {

	
	
	@Test
	public void modelSupportsEquals() {
		
		Property p = prop("n").description("d").internal(true).value("v");
		Property p2 = prop("n").description("d").internal(true).value("v");
		
		assertEquals(p,p2);
		
		Property p3 = prop("n3").value("v3");
		
		Properties ps = props().add(p).add(p2).add(p3);
		Properties ps2 = props().add(p).add(p2).add(p3);
		
		assertEquals(2,ps.size());
		assertFalse(ps.empty());
		assertTrue(ps.contains(p));
		assertTrue(ps.contains(p.name()));
		assertEquals(p,ps.lookup(p.name()));
		
		assertEquals(ps,ps2);
		
		Column col = col("c").type(Integer.class).kind("k").add(p).add(p3);
		Column col2 = col("c").type(Integer.class).kind("k").add(p).add(p3);
		
		assertEquals(col, col2);

		Column col3 = col("c3");
		
		Row r = row().col(col).is("v1").col(col3).is("v2").end();
		Row r2 = row().col(col).is("v1").col(col3).is("v2").end();
		
		assertEquals(r, r2);
	}
	
	@Test
	public void materialtables() {
		
		Table t = table().cols("c1","c2")
								    .row( "1", "2")
								    .row( "3", "3")
						 .end();
		

		assertEquals(asList(col("c1"),col("c2")), t.columns());
		
		assertSame(t, t.materialise());
		
		Table t2 = table().cols("c1","c2")
						  .row( "1", "2")
						   .row( "3", "3")
						   .end();
		
		
		assertEquals(t, t2);
		
		assertFalse(t.stream().collect(toList()).isEmpty());
		assertFalse(t.stream().collect(toList()).isEmpty());

	}
	
	@Test
	public void streamedtables() {
		
		
		Table material = table().cols("c1","c2")
							    .row( "1", "2")
							    .row( "3", "3")
					     .end();
		
		Table  t = table().cols(material.columns())
						  .rows(material.stream().collect(toList()).iterator());
		
		assertEquals(asList(col("c1"),col("c2")), t.columns());
		
		
		assertFalse(t.stream().collect(toList()).isEmpty());
		assertTrue(t.stream().collect(toList()).isEmpty());

	}
}

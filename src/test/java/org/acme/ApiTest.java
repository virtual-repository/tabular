package org.acme;


import static api.tabular.Tables.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import api.tabular.Column;
import api.tabular.Properties;
import api.tabular.Property;
import api.tabular.Row;
import api.tabular.Table;

public class ApiTest {

	//test with DSL compromises test modularity and convenience.
	
	@Test
	public void property() {

		Property p = prop("n","v");
		
		assertEquals(p,prop("n","v"));
		
		assertTrue(p.is(String.class));
		assertEquals("v",p.as(Object.class));
		assertEquals("v",p.value());
		assertEquals("n",p.name());

		p.value("new");
		assertEquals("new",p.value());
	}
	
	@Test
	public void properties() {

		Property p1 = prop("n1","v1");
		Property p2 = prop("n2","v2");
		
		Properties ps = props(p1,p2);
		
		assertEquals(ps,props(p1,p2));
		
		assertTrue(ps.has(p1,p2));
		assertTrue(ps.has(p1.name(),p2.name()));
		assertTrue(ps.has(props(p1,p2)));
		
		Property p3 = prop("n3","v3");

		assertFalse(ps.has(p3));
		
		try {
			ps.prop("bad");
			fail();
		}
		catch(IllegalStateException matternot) {}
		
		assertEquals(prop("bad","default"),ps.propOr("bad", "default"));
		
		Property p4 = prop("n4","v4");
		
		ps.add(p3,p4);
		
		assertEquals(ps,props(p1,p2,p3,p4));
		
		ps.remove(p1,p3);
		
		assertEquals(ps,props(p4,p2));
		
		ps.remove(props(p2,p4));
		
		assertTrue(ps.empty());


	}
	
	@Test
	public void columns() {
		
		Column c = col("c");
	
		assertEquals("c", c.name());
		assertEquals(c, col("c"));
		
		c.properties().add("tag");
		c.properties().add(prop("some").value("val"),prop("name","val"));
		
	}
	
	@Test
	public void rows() {
		
		Row r = row().col("c1","v1").col("c2","v2").end();
		
		assertEquals("v1",r.get("c1"));
		assertNull(r.get("c3"));
		assertEquals("default",r.getOr("c3","default"));
		assertTrue(r.has("c1","c2"));
		assertTrue(r.has(col("c2"),col("c1")));
		
		assertEquals(r, row().col(col("c2"),"v2").col(col("c1"),"v1").end());

		assertEquals(row().col("c1","changed").col("c2","v2").col("c3","v3").end(), 
					 r.merge(row().col("c3","v3").col("c1","changed").end()));
				
		assertEquals("newvalue",r.set("c2","newvalue").get("c2"));
		
		assertTrue(r.remove("c2","c3").size()==1);

	}
	
	@Test
	public void tables() {
		
		//string-based, from succinct to clear
		Table t = table($("c1","c2"),$("1","2"),$("3","4"));
		Table t2 = table().with($("c1","c2"),$("1","2"),$("3","4"));
		Table t3 = table().cols("c1","c2").rows($("1","2"),$("3","4"));
		Table t4 = table().cols("c1","c2").row("1","2").row("3","4").end();
		
		assertEquals(t,t2);
		assertEquals(t2,t3);
		assertEquals(t3,t4);
		
		//materialised, can be iterated over multiple times
		assertFalse(t.stream().collect(toList()).isEmpty());
		assertFalse(t.stream().collect(toList()).isEmpty());
		assertSame(t, t.materialise());
		
		//streamed
		List<Row> rows = asList(row().col("c1").is("1").end(), row().col("c2").is("2").end());
				
		Table t5 = table().cols("c1","c2").rows(rows.iterator());
		
		assertFalse(t5.stream().collect(toList()).isEmpty());
		assertTrue(t5.stream().collect(toList()).isEmpty());
		
	}
}

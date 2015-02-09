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

	
	
	@Test
	public void base_apis() {
		
		//dsl
		Property p = prop("n","v");
		
		assertEquals(p,prop("n","v"));
		
		//////////
	
		Property p2 = prop("n2").value("v2");
		
		Properties ps = props(p,p2);
	
		assertEquals(ps,props().add(p,p2));
		
		assertEquals(2,ps.size());
		assertFalse(ps.empty());
		
		assertTrue(ps.has(p));
		assertTrue(ps.has(p.name()));
		assertTrue(ps.has(ps));
		
		assertEquals(p,ps.prop("n"));
		
		///////////
		
		Column col = col("c").add(p,p2);
		
		assertEquals(col, col("c").add(p,p2));

		Column col2 = col("c2");
		
		Row r = row().col(col,"v1").col(col2,"v2").end();
		Row r2 = row().col(col,"v1").col(col2,"v2").end();
		
		assertEquals(r, r2);
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

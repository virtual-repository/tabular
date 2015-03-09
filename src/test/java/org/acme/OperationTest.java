package org.acme;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static smallgears.api.tabular.dsl.Tables.*;
import static smallgears.api.tabular.operations.TableOperations.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;

public class OperationTest {

	@Test
	public void simple_join() {
		
		Table t1 = table($("c1","c2"), $("v1","v2"),$("v3","v4"));
		
		Table t2 = table($("c2","c3"), $("v2","w2"),$("v4","w4"));
		
		join(t1).with(t2).basedOn(match("c2"));
		
		assertEquals(table($("c1","c2","c3"), $("v1","v2","w2"),$("v3","v4","w4")),t1);
		
	}
	
	@Test
	public void simple_grouping() {
		
		Row r1 = row($("c1","c2"),$("v1","v2"));
		Row r2 = row($("c1","c2"),$("v1","v3"));
		Row r3 = row($("c1","c2"),$("v2","v4"));
		
		Table t = table().cols($("c1","c2")).rows(r1,r2,r3);
		
		Map<String,List<Row>> group = group(t).by("c1");
		
		assertEquals(asList(r1,r2), group.get("v1"));
		assertEquals(asList(r3), group.get("v2"));
		
	}
	
	@Test
	public void copy() {
		
		Table t1 = table($("c1","c2"), $("v1","v2"),$("v3","v4"));
		
		Table t2 = t1.with(Row::new).materialise();
		
		assertEquals(t1,t2);
		assertEquals(t2,t1.copy().materialise());
		
	}
	
}

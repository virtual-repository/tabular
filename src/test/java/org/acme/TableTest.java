package org.acme;

import static org.junit.Assert.*;
import static smallgears.api.tabular.dsl.Tables.*;

import org.junit.Test;

import smallgears.api.tabular.Table;

public class TableTest {

	@Test
	public void drop_column() {
		
		Table t = table($("c1","c2"), $("v1","v2"),$("v3","v4"));
		
		assertTrue(t.materialised());
		
		t = t.with($->$.remove("c1"));
		
		assertFalse(t.materialised());
		
		t = t.materialise();
		
		assertTrue(t.materialised());
		
		assertTrue(t.stream().allMatch($->!$.has("c1")));
		
	}
	
	@Test
	public void extract_column() {
		
		Table t = table($("c1","c2"), $("v1","v2"),$("v3","v4"));
		
		t = t.with($->$.extract("c1"));
		
		assertTrue(t.stream().allMatch($->!$.has("c2")));
		
	}
}

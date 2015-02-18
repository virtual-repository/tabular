package org.acme;

import static api.tabular.TableOperations.*;
import static api.tabular.Tables.*;
import static org.junit.Assert.*;

import org.junit.Test;

import api.tabular.Table;

public class JoinTest {

	@Test
	public void simple_join() {
		
		Table t1 = table($("c1","c2"), $("v1","v2"),$("v3","v4"));
		
		Table t2 = table($("c2","c3"), $("v2","w2"),$("v4","w4"));
		
		join(t1).with(t2).basedOn(match("c2"));
		
		assertEquals(t1,table($("c1","c2","c3"), $("v1","v2","w2"),$("v3","v4","w4")));
		
	}
	
}

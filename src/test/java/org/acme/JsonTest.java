package org.acme;

import static org.junit.Assert.*;
import static smallgears.api.tabular.dsl.Tables.*;
import static smallgears.api.tabular.json.Mapper.*;

import org.junit.Test;

import smallgears.api.tabular.Column;
import smallgears.api.tabular.Csv;

public class JsonTest {
	
	@Test
	public void params_round_trip() {
		
		Column col = col("d1");
		
		col.properties().add(prop("n","v"));

		Csv csv = csv().delimiter(';')
				   .hasHeader(true)
				   .encoding("some")
				   .quote('q')
				   .rows(100);

		csv.columns().add(col);
		
		assertEquals(csv,csvOf(jsonOf(csv)));
	}

}

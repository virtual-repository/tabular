package org.acme;

import static api.tabular.Tables.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import api.tabular.Column;
import api.tabular.Csv;
import api.tabular.Table;

public class CsvTests {

	//can we correctly and flexibly parse csv into tables and serialise tables back to csv?
	
	
	
	//   parsing    //////////////////////////////////////////////////////////////////////////
	
	//-- assume tables can be correctly created in memory.
	//-- create csv stream fixtures in memory with test facilities
	//-- parse them and compare the tables with expected in memory tables
	
	//note: streamed tables need to be materialised before we can compare them, 
	//as they have different underlying impls until we do that.
	
	
	
	
	@Test
	public void header_compensates_for_missing_columns() {
		
		//our test facilities are directed by csv directives too, just a convenience
		InputStream stream = some( csv().with("c1","c2"),
				$("1","2"),
				$("3","4"));
		
		///////////////////////////////////////
		
		//its the default, but let lets make it explicit.
		Csv csv = csv().hasHeader(true);
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		Table expected = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		assertEquals(expected, parsed);
		
		assertEquals(csv.columns(), parsed.columns());
	}
	
	@Test
	public void columns_compensate_for_missing_header() {
		
		InputStream stream = some(csv(),
				$("1","2"),
				$("3","4"));
		
		////////////////////////////////////////////////
		
		Csv csv = csv().with("c1","c2").hasHeader(false);
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		Table expected = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		assertEquals(expected, parsed);
		
	}
	
	@Test
	public void columns_override_existing_header() {
		
		InputStream stream = some(csv().with("c1","c2"),
				$("1","2"),
				$("3","4"));
		
		//////////////////////////////////////////////////////////
		
		//tell there's header to override
		Csv csv = csv().with("d1","d2").hasHeader(true);
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		Table expected = table().with($("d1","d2"), $("1","2"),$("3","4"));
		
		assertEquals(expected, parsed);
		
	}
	
	
	@Test
	public void missing_columns_are_synthesised_if_header_is_missing() {
		
		InputStream stream = some(csv(),
				$("1","2"),
				$("3","4"));
		
		//////////////////////////////////////////////////////////////
		
		Csv csv = csv().hasHeader(false);
		
		Table parsed = table().from(csv).in(stream);
		
		parsed = parsed.materialise();

		List<Column> synthetic = parsed.columns();
		
		assertEquals(2, synthetic.size());
		
		Table expected = table().cols(synthetic).rows($("1","2"),$("3","4"));
		
		assertEquals(expected, parsed);
		
		assertEquals(csv.columns(), parsed.columns());
		
	}
	
	@Test
	public void can_subset_the_data_vertically_with_columns() {
		
		InputStream stream = some(csv().with("c1","c2"),
				$("1","2"),
				$("3","4"));
		
		/////////////////////////////////////////////////////////////
		
		Table parsed = table().from(csv().with("c1")).in(stream).materialise();
		
		Table expected = table().cols("c1").rows($("1"),$("3"));
		
		assertEquals(expected,parsed);
		
	}
	
	@Test
	public void header_also_can_subsets_the_data_vertically() {
		
		InputStream stream = some(csv().with("c1"),
				$("1","2"),
				$("3","4"));
		
		//////////////////////////////////////////////////////////////
		
		Table parsed = table().from(csv()).in(stream).materialise();
		
		Table expected = table().cols("c1").rows($("1"),$("3"));
		
		assertEquals(expected, parsed);
		
	}
	
	
	@Test
	public void can_also_subset_data_horizontally() {
		
		InputStream stream = some(csv().with("c1","c2"),
				$("1","2"),
				$("3","4"));
		
		
		////////////////////////////////////////////////////////////
		
		Csv csv = csv().rows(1);
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		Table expected = table().with($("c1","c2"),$("1","2"));
		
		assertEquals(expected, parsed);
		
	}
	
	
	@Test
	public void offers_parsing_options() {
		
		InputStream stream = some(csv().with("c1 1","c2 2").delimiter('\t').quote('\''),
				$("1 1","'2 2'"),
				$("3 3","'4 4'"));
		
		////////////////////////////////////////////////////////////
		
		Csv csv = csv().delimiter('\t').quote('\'');
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		Table expected = table().with($("c1 1","c2 2"),$("1 1","2 2"),$("3 3","4 4"));
		
		assertEquals(expected, parsed);
		
	}
	
	
	
	//   serialisation    //////////////////////////////////////////////////////////////////////////
	
	//-- assume tables can be correctly created in memory or parsed from CSV.
	//-- create table in memory and serialise it to csv, also in memory
	//-- parse stream and compare resulting table with original one
	
	//note: streamed tables need to be materialised before we can compare them, 
	//as they have different underlying impls until we do that.
	
	@Test
	public void table_compensates_for_missing_columns() {
		
		Table created = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		Csv csv = csv().hasHeader(true); //just to emphasise default
		
		InputStream stream = csv.serialise(created);
		
		assertEquals(created.columns(),csv.columns());
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		assertEquals(created,parsed);
		
	}
	
	
	@Test
	public void directives_superside_table() {
		
		Table created = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		Csv csv = csv().hasHeader(true).with("c1"); //just to emphasise default
		
		InputStream stream = csv.serialise(created);
		
		Table parsed = table().from(csv()).in(stream).materialise();
		
		Table expected = table().with($("c1"), $("1"),$("3"));
		
		assertEquals(expected,parsed);
	}
	
	@Test
	public void csv_can_be_headerless() {
		
		Table created = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		Csv csv = csv().hasHeader(false);
		
		InputStream stream = csv.serialise(created);
		
		assertEquals(created.columns(),csv.columns());

		//if this is headerless, then reparsing it with the starting columsn ought to bring us back
		Csv csv2 = csv().hasHeader(false).with("c1","c2");
		
		Table parsed = table().from(csv2).in(stream).materialise();
		
		assertEquals(created,parsed);
		
	}


	@Test
	public void offers_serialisation_options() {
		
		Table created = table().with($("c1","c2"), $("1","2"),$("3","4"));
		
		Csv csv = csv().hasHeader(true).delimiter(';').quote('\''); 
		
		InputStream stream = csv.serialise(created);
		
		Table parsed = table().from(csv).in(stream).materialise();
		
		assertEquals(created,parsed);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	////   helpers    /////////////////////////////////////////////////////////////////
	
	
	InputStream some(Csv csv, String[] ... rows) {
		
		StringBuilder $ = new StringBuilder();
		
		if (csv.hasHeader()) {
			
			for (Column col : csv.columns())
				$.append(col.name()).append(csv.delimiter());

			if ($.length()>0)
				$.replace($.length()-1, $.length(), "\n");

		}
		
		for (String[] row : rows) {
			
			for (String val : row)
				$.append(val).append(csv.delimiter());
			
			$.replace($.length()-1, $.length(), "\n");
		}
				
		//System.err.println("csv:\n"+$);
		
		return new ByteArrayInputStream($.toString().getBytes());
	}
}

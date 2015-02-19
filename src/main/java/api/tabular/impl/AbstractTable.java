package api.tabular.impl;

import static api.tabular.Properties.*;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import api.tabular.Column;
import api.tabular.Properties;
import api.tabular.Row;
import api.tabular.Table;
import api.tabular.utils.Streamable;


@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class AbstractTable implements Table, Streamable<Row> {

	@NonNull
	protected final List<Column> columns;
	
	
	private final Properties properties = props();
	
	
	public final SimpleTable materialise() {
		
		return materialised() ? (SimpleTable) this :new SimpleTable(this);
	}
	
	@Override
	public final boolean materialised() {
		
		return this instanceof SimpleTable;
	}
	
	@Override
	public String toString() {
		
		return "Table [columns="+ (columns != null ? columns : null) + ", properties="+ properties + "]";
	}
	
	

}
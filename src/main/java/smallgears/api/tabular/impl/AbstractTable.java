package smallgears.api.tabular.impl;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import smallgears.api.properties.Properties;
import smallgears.api.tabular.Column;
import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;
import smallgears.api.tabular.utils.Streamable;


@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class AbstractTable implements Table, Streamable<Row> {

	@NonNull
	protected final List<Column> columns;
	
	
	private final Properties properties = Properties.props();
	
	
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
package api.tabular;

import static api.tabular.Properties.*;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import api.tabular.utils.Streamable;


@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class AbstractTable implements Table, Streamable<Row> {

	@NonNull
	protected final List<Column> columns;
	
	private final Properties properties = props();
	
	public Table materialise() {
		
		return new MaterialTable(this);
	}
	
	@Override
	public String toString() {
		final int maxLen = 100;
		return "Table [columns="
				+ (columns != null ? columns.subList(0, Math.min(columns.size(), maxLen)) : null) + ", properties="
				+ properties + "]";
	}

}
package moe.protasis.yukicommons.api.data;

import javax.sql.DataSource;

public interface IDatabaseProvider {
    /**
     * Acquires the <code>DataSource</code> object.
     * 
     * @return The data source.
     */
    DataSource GetDataSource();
}

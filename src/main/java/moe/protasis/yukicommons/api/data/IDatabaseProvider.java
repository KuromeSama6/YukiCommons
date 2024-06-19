package moe.protasis.yukicommons.api.data;

import javax.sql.DataSource;

public interface IDatabaseProvider {
    DataSource GetDataSource();
}

package com.jd.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return String.valueOf(DataSourceTypeManager.getDataSourceType());
    }
}

package com.jd.datasource;

import org.springframework.core.NamedThreadLocal;

public class DataSourceTypeManager {

   private static final ThreadLocal<DataSourceType> dataSourceHolder = new NamedThreadLocal("dataSourceType") {
       @Override
       protected DataSourceType initialValue() {
           return DataSourceType.MASTER;
       }
   };

   public static DataSourceType getDataSourceType() {
       return dataSourceHolder.get();
   }

   public static void setDataSourceType(DataSourceType dataSourceType) {
       dataSourceHolder.set(dataSourceType);
   }

   public static void remove() {
       dataSourceHolder.remove();
   }
}

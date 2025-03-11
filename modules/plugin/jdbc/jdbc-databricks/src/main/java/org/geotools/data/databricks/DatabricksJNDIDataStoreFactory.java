/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.databricks;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for Databricks SQL
 *
 * @author Stuart Lynn
 */
public class DatabricksJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public DatabricksJNDIDataStoreFactory() {
        super(new DatabricksDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);

        // Add Databricks parameters
        parameters.put(DatabricksDataStoreFactory.DBTYPE.key, DatabricksDataStoreFactory.DBTYPE);
//        parameters.put(DatabricksDataStoreFactory.CATALOG.key, DatabricksDataStoreFactory.CATALOG);
//        parameters.put(DatabricksDataStoreFactory.SCHEMA.key, DatabricksDataStoreFactory.SCHEMA);
    }

    @Override
    public String getDisplayName() {
        return "Databricks SQL (JNDI)";
    }

    @Override
    public String getDescription() {
        return "Connection to Databricks SQL Warehouse with Spatial Extensions via JNDI";
    }
}

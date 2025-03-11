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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the DatabricksDataStoreFactory
 *
 * @author Stuart Lynn
 */
public class DatabricksDataStoreFactoryTest {

    private DatabricksDataStoreFactory factory;
    private Map<String, Object> params;

    @Before
    public void setUp() throws Exception {
        factory = new DatabricksDataStoreFactory();
        params = new HashMap<>();
        params.put(DatabricksDataStoreFactory.DBTYPE.key, "databricks");
        params.put(DatabricksDataStoreFactory.HOST.key, "dbc-xyz-abc.cloud.databricks.com");
        params.put(DatabricksDataStoreFactory.HTTP_PATH.key, "/sql/1.0/warehouses/xyz");
        params.put(DatabricksDataStoreFactory.TOKEN.key, "dapi_xyz");
    }

    @Test
    public void testFactoryCreation() {
        assertNotNull(factory);
        assertEquals("Databricks SQL", factory.getDisplayName());
        assertTrue(factory.getDescription().contains("Databricks SQL Warehouse"));
    }

    @Test
    public void testCanProcessParameters() {
        assertTrue(factory.canProcess(params));
    }

    @Test
    public void testGetJDBCUrl() throws Exception {
        String url = factory.getJDBCUrl(params);
        assertNotNull(url);
        assertTrue(url.startsWith("jdbc:databricks://"));
        assertTrue(url.contains("httpPath="));
    }

    @Test
    public void testCreateSQLDialect() throws Exception {
        assertNotNull(factory.createSQLDialect(null));
    }
}

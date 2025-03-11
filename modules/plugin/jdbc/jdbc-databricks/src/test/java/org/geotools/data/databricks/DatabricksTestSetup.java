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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * Test setup for Databricks SQL
 *
 * @author Stuart Lynn
 */
public class DatabricksTestSetup extends JDBCTestSetup {

    @Override
    protected String typeName(String raw) {
        return raw.toLowerCase();
    }
    
    @Override
    public void setFixture(Properties fixture) {
        super.setFixture(fixture);
        
        try {
            // Ensure the URL property is set based on other properties
            if (fixture.getProperty("url") == null) {
                Map<String, String> params = (Map)fixture;
                String url = new DatabricksDataStoreFactory().getJDBCUrl(params);
                fixture.setProperty("url", url);
            }
            
            // Ensure driver is set
            if (fixture.getProperty("driver") == null) {
                fixture.setProperty("driver", DatabricksDataStoreFactory.DRIVER_CLASS_NAME);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting Databricks connection properties", e);
        }
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new DatabricksDataStoreFactory();
    }

    private String getProperty(String key, String defaultValue) {
        // Try system property first, then environment variable
        String value = System.getProperty(key);
        if (value == null || value.isEmpty()) {
            value = System.getenv(key.replace('.', '_').toUpperCase());
        }
        return value != null ? value : defaultValue;
    }

    @Override
    protected void setUpData() throws Exception {
        // Create test tables with spatial data
        runSafe("DROP TABLE IF EXISTS ft1");
        run("CREATE TABLE ft1 (id INT, geometry BINARY, intProperty INT, "
                + "doubleProperty DOUBLE, stringProperty STRING, date TIMESTAMP)");

        // Insert sample points
        run("INSERT INTO ft1 VALUES (0, "
                + "ST_AsBinary(ST_GeomFromText('POINT(0 0)', 4326)), 0, 0.0, 'zero', TIMESTAMP'2015-01-01 00:00:00')");
        run("INSERT INTO ft1 VALUES (1, "
                + "ST_AsBinary(ST_GeomFromText('POINT(1 1)', 4326)), 1, 1.1, 'one', TIMESTAMP'2015-01-01 01:01:01')");
        run("INSERT INTO ft1 VALUES (2, "
                + "ST_AsBinary(ST_GeomFromText('POINT(2 2)', 4326)), 2, 2.2, 'two', TIMESTAMP'2015-01-01 02:02:02')");

        // Create test geometry table
        runSafe("DROP TABLE IF EXISTS ft2");
        run("CREATE TABLE ft2 (id INT, "
                + "geom_point BINARY, geom_line BINARY, geom_polygon BINARY, geom_multipoint BINARY, "
                + "geom_multiline BINARY, geom_multipolygon BINARY)");

        // Insert geometry values
        run(
                "INSERT INTO ft2 VALUES (0, " + "ST_AsBinary(ST_GeomFromText('POINT(0 0)', 4326)), "
                        + "ST_AsBinary(ST_GeomFromText('LINESTRING(0 0, 1 1)', 4326)), "
                        + "ST_AsBinary(ST_GeomFromText('POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))', 4326)), "
                        + "ST_AsBinary(ST_GeomFromText('MULTIPOINT((0 0), (1 1))', 4326)), "
                        + "ST_AsBinary(ST_GeomFromText('MULTILINESTRING((0 0, 1 1), (2 2, 3 3))', 4326)), "
                        + "ST_AsBinary(ST_GeomFromText('MULTIPOLYGON(((0 0, 1 0, 1 1, 0 1, 0 0)), ((2 2, 3 2, 3 3, 2 3, 2 2)))', 4326)))");
    }

    @Override
    protected Properties createExampleFixture() {
        // Create a fixture with connection details
        Properties fixture = new Properties();
        fixture.put("dbtype", "databricks");
        fixture.put("driver", "com.databricks.client.jdbc.Driver");
        fixture.put("host", "dbc-xyz-abc.cloud.databricks.com");
        fixture.put("httpPath", "/sql/1.0/warehouses/xyz");
        fixture.put("token", "dapi_xyz");
        fixture.put("Catalog", "hive_metastore");
        fixture.put("Schema", "default");
        fixture.put("ssl", "true");
        return fixture;
    }

    @Override
    protected Properties createOfflineFixture() {
        return createExampleFixture();
    }
}

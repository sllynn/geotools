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

import org.geotools.jdbc.JDBCAggregateTestSetup;

/**
 * Test setup for Databricks SQL aggregate functions
 *
 * @author Stuart Lynn
 */
public class DatabricksAggregateFunctionTestSetup extends JDBCAggregateTestSetup {

    public DatabricksAggregateFunctionTestSetup() {
        super(new DatabricksTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        run("CREATE TABLE aggregate_test (id INT, geom BINARY, intProperty INT)");
        run("INSERT INTO aggregate_test VALUES (0, " + "ST_AsBinary(ST_GeomFromText('POINT(0 0)', 4326)), 0)");
        run("INSERT INTO aggregate_test VALUES (1, " + "ST_AsBinary(ST_GeomFromText('POINT(1 1)', 4326)), 1)");
        run("INSERT INTO aggregate_test VALUES (2, " + "ST_AsBinary(ST_GeomFromText('POINT(2 2)', 4326)), 2)");
        run("INSERT INTO aggregate_test VALUES (3, " + "ST_AsBinary(ST_GeomFromText('POINT(3 3)', 4326)), 3)");
        run("INSERT INTO aggregate_test VALUES (4, " + "ST_AsBinary(ST_GeomFromText('POINT(4 4)', 4326)), 4)");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS aggregate_test");
    }
}

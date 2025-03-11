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

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

/**
 * API test setup for Databricks SQL
 *
 * @author Stuart Lynn
 */
public class DatabricksDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    public DatabricksDataStoreAPITestSetup() {
        super(new DatabricksTestSetup());
    }

    @Override
    protected void createRoadTable() throws Exception {
        run("CREATE TABLE road (fid INT, id INT, geom BINARY, name STRING)");
        run("INSERT INTO road VALUES (0, 0, "
                + "ST_AsBinary(ST_GeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)', 4326)), 'r1')");
        run("INSERT INTO road VALUES (1, 1, "
                + "ST_AsBinary(ST_GeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)', 4326)), 'r2')");
        run("INSERT INTO road VALUES (2, 2, "
                + "ST_AsBinary(ST_GeomFromText('LINESTRING(3 2, 4 2, 5 3)', 4326)), 'r3')");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run("CREATE TABLE river (fid INT, id INT, geom BINARY, river STRING, flow DOUBLE)");
        run("INSERT INTO river VALUES (0, 0, "
                + "ST_AsBinary(ST_GeomFromText('LINESTRING(5 5, 7 4)', 4326)), 'rv1', 4.5)");
        run("INSERT INTO river VALUES (1, 1, "
                + "ST_AsBinary(ST_GeomFromText('LINESTRING(4 6, 6 4, 7 2)', 4326)), 'rv2', 3.0)");
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE lake (fid INT, id INT, geom BINARY, name STRING)");
        run("INSERT INTO lake VALUES (0, 0, "
                + "ST_AsBinary(ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 4326)), 'muddy')");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS road");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS river");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS lake");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS building");
    }
}

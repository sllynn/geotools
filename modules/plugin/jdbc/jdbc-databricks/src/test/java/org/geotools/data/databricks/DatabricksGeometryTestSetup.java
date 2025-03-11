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

import org.geotools.jdbc.JDBCGeometryTestSetup;

/**
 * Geometry test setup for Databricks SQL
 *
 * @author Stuart Lynn
 */
public class DatabricksGeometryTestSetup extends JDBCGeometryTestSetup {

    public DatabricksGeometryTestSetup() {
        super(new DatabricksTestSetup());
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();

        // Create any additional geometry tables needed for testing

        // Sample table with dimension test
        createPointTable();
        createLineStringTable();
        createPolygonTable();
        createMultiPointTable();
        createMultiLineStringTable();
        createMultiPolygonTable();
    }

    @Override
    public void tearDown() throws Exception {
        // Clean up all test tables
        dropPointTable();
        dropLineStringTable();
        dropPolygonTable();
        dropMultiPointTable();
        dropMultiLineStringTable();
        dropMultiPolygonTable();
    }

    protected void createPointTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS point_test (id INT, geom BINARY, name STRING)");
        run("INSERT INTO point_test VALUES (1, ST_AsBinary(ST_GeomFromText('POINT(10 10)', 4326)), 'p1')");
        run("INSERT INTO point_test VALUES (2, ST_AsBinary(ST_GeomFromText('POINT(20 20)', 4326)), 'p2')");
    }

    protected void createLineStringTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS line_test (id INT, geom BINARY, name STRING)");
        run(
                "INSERT INTO line_test VALUES (1, ST_AsBinary(ST_GeomFromText('LINESTRING(10 10, 20 20, 30 40)', 4326)), 'l1')");
        run(
                "INSERT INTO line_test VALUES (2, ST_AsBinary(ST_GeomFromText('LINESTRING(40 40, 30 30, 20 20, 10 10)', 4326)), 'l2')");
    }

    protected void createPolygonTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS poly_test (id INT, geom BINARY, name STRING)");
        run("INSERT INTO poly_test VALUES (1, ST_AsBinary(ST_GeomFromText("
                + "'POLYGON((10 10, 10 20, 20 20, 20 15, 10 10))', 4326)), 'p1')");
        run("INSERT INTO poly_test VALUES (2, ST_AsBinary(ST_GeomFromText("
                + "'POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))', 4326)), 'p2')");
    }

    protected void createMultiPointTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS mpoint_test (id INT, geom BINARY, name STRING)");
        run("INSERT INTO mpoint_test VALUES (1, ST_AsBinary(ST_GeomFromText("
                + "'MULTIPOINT((10 10), (20 20))', 4326)), 'mp1')");
        run("INSERT INTO mpoint_test VALUES (2, ST_AsBinary(ST_GeomFromText("
                + "'MULTIPOINT((15 15), (30 30))', 4326)), 'mp2')");
    }

    protected void createMultiLineStringTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS mline_test (id INT, geom BINARY, name STRING)");
        run("INSERT INTO mline_test VALUES (1, ST_AsBinary(ST_GeomFromText("
                + "'MULTILINESTRING((10 10, 20 20), (30 30, 40 40))', 4326)), 'ml1')");
        run("INSERT INTO mline_test VALUES (2, ST_AsBinary(ST_GeomFromText("
                + "'MULTILINESTRING((10 10, 10 20), (20 20, 20 10))', 4326)), 'ml2')");
    }

    protected void createMultiPolygonTable() throws Exception {
        run("CREATE TABLE IF NOT EXISTS mpoly_test (id INT, geom BINARY, name STRING)");
        run("INSERT INTO mpoly_test VALUES (1, ST_AsBinary(ST_GeomFromText("
                + "'MULTIPOLYGON(((10 10, 10 20, 20 20, 20 10, 10 10)),"
                + "((60 60, 70 70, 80 60, 60 60)))', 4326)), 'mp1')");
        run("INSERT INTO mpoly_test VALUES (2, ST_AsBinary(ST_GeomFromText("
                + "'MULTIPOLYGON(((30 30, 30 40, 40 40, 40 30, 30 30)),"
                + "((80 80, 90 90, 100 80, 80 80)))', 4326)), 'mp2')");
    }

    protected void dropPointTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS point_test");
    }

    protected void dropLineStringTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS line_test");
    }

    protected void dropPolygonTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS poly_test");
    }

    protected void dropMultiPointTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS mpoint_test");
    }

    protected void dropMultiLineStringTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS mline_test");
    }

    protected void dropMultiPolygonTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS mpoly_test");
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DROP TABLE IF EXISTS " + tableName);
    }
}

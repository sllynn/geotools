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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Dialect for Databricks SQL with Spatial Functions
 *
 * @author Stuart Lynn
 */
public class DatabricksDialect extends BasicSQLDialect {

    public DatabricksDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    @Override
    public String getNameEscape() {
        return "`";
    }
    
    @Override
    public String getGeometryTypeName(Integer type) {
        return "BINARY";
    }
    
    // Note: Removed non-functional getColumnTypeName method that doesn't override anything in parent class

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        StringBuffer sql = new StringBuffer();

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("MyFeature");
        builder.add(columnName, Point.class);
        GeometryDescriptor geometryDescriptor = builder.buildFeatureType().getGeometryDescriptor();
        sql.append("SELECT ST_SRID(");
        encodeGeometryColumn(geometryDescriptor, null, 0, null, sql);
        sql.append(") ");
        sql.append("FROM ");

        if (schemaName != null) {
            encodeTableName(schemaName, sql);
            sql.append(".");
        }

        encodeTableName(tableName, sql);
        sql.append(" WHERE ");
        encodeColumnName(null, columnName, sql);
        sql.append(" IS NOT NULL LIMIT 1");

        dataStore.getLogger().fine(sql.toString());
        Statement st = cx.createStatement();

        try {
            ResultSet rs = st.executeQuery(sql.toString());

            try {
                if (rs.next()) {
                    return Integer.valueOf(rs.getInt(1));
                } else {
                    // Could not find SRID
                    return null;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);
        // Ensure proper mapping from SQL types to Java classes
        mappings.put(Types.BINARY, Geometry.class);
        mappings.put(Types.VARBINARY, Geometry.class);
        mappings.put(Types.VARCHAR, String.class);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
        mappings.put(Geometry.class, Types.BINARY);
        mappings.put(String.class, Types.VARCHAR);
    }
    
    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        // Add mappings from Databricks type names to Java classes
        mappings.put("STRING", String.class);
        mappings.put("VARCHAR", String.class);
        mappings.put("CHAR", String.class);
        mappings.put("BINARY", Geometry.class);
        mappings.put("INT", Integer.class);
        mappings.put("BIGINT", Long.class);
        mappings.put("DOUBLE", Double.class);
        mappings.put("FLOAT", Float.class);
        mappings.put("BOOLEAN", Boolean.class);
        mappings.put("TIMESTAMP", java.sql.Timestamp.class);
        mappings.put("DATE", java.sql.Date.class);
        // Explicitly prevent ARRAY from being used for string values
        mappings.put("ARRAY", byte[].class);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append("ST_Envelope(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append(")");
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        try {
            byte[] wkb = rs.getBytes(column);
            if (wkb == null) {
                return new Envelope();
            }

            Geometry envelope = new WKBReader().read(wkb);
            return envelope.getEnvelopeInternal();
        } catch (ParseException e) {
            throw new IOException("Error decoding geometry envelope", e);
        }
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        sql.append("ST_GeomFromWKB(");
        encodeColumnName(null, gatt.getLocalName(), sql);
        sql.append(")");
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {
        if (value == null) {
            sql.append("NULL");
        } else {
            sql.append("ST_GeomFromText('");
            sql.append(new WKTWriter().write(value));
            sql.append("', ");
            sql.append(srid);
            sql.append(")");
        }
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        byte[] bytes = rs.getBytes(column);

        if (bytes == null) {
            return null;
        }

        try {
            return new WKBReader(factory).read(bytes);
        } catch (ParseException e) {
            throw new IOException("Error parsing WKB", e);
        }
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(null, column, sql);
        sql.append(" BIGINT NOT NULL");
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if (limit >= 0 && limit < Integer.MAX_VALUE) {
            sql.append(" LIMIT ").append(limit);
            if (offset > 0) {
                sql.append(" OFFSET ").append(offset);
            }
        } else if (offset > 0) {
            sql.append(" LIMIT ").append(Integer.MAX_VALUE);
            sql.append(" OFFSET ").append(offset);
        }
    }
    
    @Override
    public Integer getSQLType(AttributeDescriptor ad) {
        Class<?> binding = ad.getType().getBinding();
        
        // Use explicit String binding for text attributes to avoid ARRAY type
        if (String.class.equals(binding)) {
            return Types.VARCHAR;
        }
        
        // For all other types, use the default mapping
        return super.getSQLType(ad);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        // Register binary types for geometry storage
        overrides.put(Types.BINARY, "BINARY");
        overrides.put(Types.VARBINARY, "BINARY");
        // Explicitly set VARCHAR to use STRING instead of ARRAY
        overrides.put(Types.VARCHAR, "STRING");
        overrides.put(Types.CHAR, "STRING");
        overrides.put(Types.LONGVARCHAR, "STRING");
    }

    /** Encode a filter function that spatially operates on the given geometry column. */
    public String functionEncode(String function, String[] arguments) {
        if (function.equalsIgnoreCase("intersects")) {
            return "ST_Intersects(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("contains")) {
            return "ST_Contains(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("within")) {
            return "ST_Within(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("touches")) {
            return "ST_Touches(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("disjoint")) {
            return "ST_Disjoint(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("equals")) {
            return "ST_Equals(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("covers")) {
            return "ST_Covers(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("distance")) {
            return "ST_Distance(" + arguments[0] + ", " + arguments[1] + ")";
        }
        if (function.equalsIgnoreCase("buffer")) {
            return "ST_AsText(ST_Buffer(" + arguments[0] + ", " + arguments[1] + "))";
        }
        if (function.equalsIgnoreCase("centroid")) {
            return "ST_AsText(ST_Centroid(" + arguments[0] + "))";
        }
        if (function.equalsIgnoreCase("difference")) {
            return "ST_AsText(ST_Difference(" + arguments[0] + ", " + arguments[1] + "))";
        }
        if (function.equalsIgnoreCase("intersection")) {
            return "ST_AsText(ST_Intersection(" + arguments[0] + ", " + arguments[1] + "))";
        }

        // Use default encoding
        return null;
    }

//    @Override
//    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {
//        // No post-table creation steps needed
//    }

//    @Override
//    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
//        // Log table name for debugging
//        dataStore.getLogger().fine("Checking table: " + (schemaName != null ? schemaName + "." : "") + tableName);
//
//        // Skip Databricks system tables and metadata tables
//        if (tableName.startsWith("system.") ||
//            tableName.startsWith("information_schema.") ||
//            "COLUMNS".equals(tableName) ||
//            "TABLES".equals(tableName)) {
//            return false;
//        }
//
//        // Additional debugging
//        DatabaseMetaData metadata = cx.getMetaData();
//        dataStore.getLogger().fine("Database product: " + metadata.getDatabaseProductName());
//        dataStore.getLogger().fine("Database version: " + metadata.getDatabaseProductVersion());
//
//        try {
//            // Try to query the table to verify it exists and has the expected structure
//            Statement stmt = cx.createStatement();
//            try {
//                // Use a query that will be very fast but still verify the table exists
//                String sql = "SELECT 1 FROM ";
//                if (schemaName != null && !schemaName.isEmpty()) {
//                    sql += schemaName + ".";
//                }
//                sql += tableName + " WHERE 1=0";
//
//                dataStore.getLogger().fine("Testing table with query: " + sql);
//                stmt.executeQuery(sql).close();
//                dataStore.getLogger().fine("Table " + tableName + " verified to exist");
//                return true;
//            } catch (SQLException e) {
//                // If the query fails, this is likely not a valid table
//                dataStore.getLogger().fine("Excluding table " + tableName + " due to error: " + e.getMessage());
//                return false;
//            } finally {
//                stmt.close();
//            }
//        } catch (SQLException e) {
//            dataStore.getLogger().warning("Error checking table " + tableName + ": " + e.getMessage());
//            // On error, better to include the table than exclude it
//            return true;
//        }
//    }
    
//    /**
//     * Add additional logging to help debug table loading issues
//     */
//    @Override
//    public String[] getDesiredTablesType() {
//        String[] types = super.getDesiredTablesType();
//        dataStore.getLogger().fine("Desired table types: " + Arrays.toString(types));
//        return types;
//    }
}

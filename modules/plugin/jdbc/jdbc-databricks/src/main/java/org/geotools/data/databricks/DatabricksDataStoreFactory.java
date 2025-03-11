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
import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.api.data.Parameter;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for Databricks SQL
 *
 * @author Stuart Lynn
 */
public class DatabricksDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, "databricks", Collections.singletonMap(Parameter.LEVEL, "program"));

    /** parameter for Databricks workspace hostname */
    public static final Param HOST =
            new Param("host", String.class, "Host", true, "dbc-xyz123-abc.cloud.databricks.com");

    /** parameter for Databricks HTTP path */
    public static final Param HTTP_PATH =
            new Param("httpPath", String.class, "HTTP Path", true, "/sql/1.0/warehouses/abc123def456");

    /** parameter for Databricks personal access token */
    public static final Param TOKEN =
            new Param("token", String.class, "Personal Access Token", true, "dapi123abc456def789");

    /** parameter for Databricks catalog name */
    public static final Param CATALOG = new Param("Catalog", String.class, "Catalog Name", false, "hive_metastore");

    /** parameter for Databricks schema/database name */
    public static final Param SCHEMA = new Param("Schema", String.class, "Schema/Database Name", false, "default");

    /** parameter for Databricks schema/database name */
    public static final Param SOCKET_TIMEOUT = new Param("SocketTimeout", String.class, "Socket Timeout (in seconds)", false, "10");

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new DatabricksDialectPrepared(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return "databricks";
    }

    /** driver class name */
    public static final String DRIVER_CLASS_NAME = "com.databricks.client.jdbc.Driver";
    
    @Override
    protected String getDriverClassName() {
        return DRIVER_CLASS_NAME;
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);

        // Remove some unused JDBCDataStore params
        parameters.remove(USER.key);
        parameters.remove(PASSWD.key);
        parameters.remove(JDBCDataStoreFactory.SCHEMA.key); // Remove JDBC schema
        parameters.remove(NAMESPACE.key);
        parameters.remove(MAXCONN.key);
        parameters.remove(MINCONN.key);
        parameters.remove(FETCHSIZE.key);
        parameters.remove(MAXWAIT.key);
        parameters.remove(VALIDATECONN.key);
        parameters.remove(PK_METADATA_TABLE.key);

        // Add Databricks-specific parameters
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(HOST.key, HOST);
        parameters.put(HTTP_PATH.key, HTTP_PATH);
        parameters.put(TOKEN.key, TOKEN);
        parameters.put(CATALOG.key, CATALOG);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(SOCKET_TIMEOUT.key, SOCKET_TIMEOUT);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String httpPath = (String) HTTP_PATH.lookUp(params);
        String token = (String) TOKEN.lookUp(params);
        String catalog = (String) CATALOG.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
        String socketTimeout = (String) SOCKET_TIMEOUT.lookUp(params);

        // Build Databricks JDBC URL in format:
        // jdbc:databricks://host;httpPath=path;transportMode=http;SSL=1;AuthMech=3;PWD=token;catalog=name;schema=name
        StringBuilder url = new StringBuilder("jdbc:databricks://");
        url.append(host);
        url.append(";httpPath=").append(httpPath);

        // Auth mechanism is 3 for Personal Access Token
        url.append(";AuthMech=3");

        // Set token as password
        url.append(";PWD=").append(token);

        if (catalog != null && !catalog.isEmpty()) {
            url.append(";ConnCatalog=").append(catalog);
        }

        if (schema != null && !schema.isEmpty()) {
            url.append(";ConnSchema=").append(schema);
        }

        if (schema != null && !schema.isEmpty()) {
            url.append(";SocketTimeout=").append(socketTimeout);
        }

        // Set transport mode to HTTP
        url.append(";transportMode=http");

        return url.toString();
    }

    @Override
    public String getDisplayName() {
        return "Databricks SQL";
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        // Get the schema from our parameters
        String schema = (String) SCHEMA.lookUp(params);
        String catalog = (String) CATALOG.lookUp(params);

        // Set the database catalog in the data store
        if (catalog != null && !catalog.isEmpty()) {
            dataStore.setDatabaseCatalog(catalog);
        }
        // Set the database schema in the data store
        if (schema != null && !schema.isEmpty()) {
            dataStore.setDatabaseSchema(schema);
        }

        return dataStore;
    }

    @Override
    public String getDescription() {
        return "Connection to Databricks SQL Warehouse with Spatial Extensions";
    }
}

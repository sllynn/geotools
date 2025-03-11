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
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;

/**
 * Databricks SQL filter to SQL converter
 *
 * @author Stuart Lynn
 */
public class DatabricksFilterToSQL extends PreparedFilterToSQL {

    public DatabricksFilterToSQL(PreparedStatementSQLDialect dialect) {
        super(dialect);
    }

    // The parent class doesn't have a constructor that takes both dialect and featureType,
    // so we should use the other constructor pattern from other dialects
    public DatabricksFilterToSQL(PreparedStatementSQLDialect dialect, SimpleFeatureType featureType) {
        super(dialect);
        this.featureType = featureType;
    }

    @Override
    public Object visit(Literal expression, Object context) {
        // Handle geometry literals
        if (expression.getValue() instanceof Geometry) {
            try {
                Geometry g = (Geometry) expression.getValue();

                // Databricks doesn't support LinearRing, so convert to LineString
                if (g instanceof LinearRing) {
                    g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
                }

                out.write("ST_GeomFromText('");
                out.write(g.toText());
                out.write("', ");
                out.write(String.valueOf(currentSRID));
                out.write(")");
            } catch (IOException e) {
                throw new RuntimeException("Error writing geometry literal", e);
            }
            return context;
        } else {
            // For all other literals, use the default behavior
            return super.visit(expression, context);
        }
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleBBOX(filter, left, right, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleDistanceOperator(filter, left, right, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleDistanceOperator(filter, left, right, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        try {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();

            handleStandardSpatialOperator(filter, left, right, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    private void handleDistanceOperator(DistanceBufferOperator filter, Expression e1, Expression e2, boolean swapped)
            throws IOException {
        out.write("ST_Distance(");
        e1.accept(this, null);
        out.write(", ");
        e2.accept(this, null);
        out.write(")");

        if (filter instanceof DWithin) {
            out.write(" <= ");
        } else if (filter instanceof Beyond) {
            out.write(" > ");
        } else {
            throw new RuntimeException(
                    "Unknown distance operator: " + filter.getClass().getName());
        }

        // Get the distance value and output it
        double distance = getDistanceInMeters(filter);
        out.write(String.valueOf(distance));
    }

    private void handleBBOX(BBOX filter, Expression e1, Expression e2, boolean swapped) throws IOException {
        out.write("ST_Intersects(");

        if (swapped) {
            e2.accept(this, null);
            out.write(", ");
            e1.accept(this, null);
        } else {
            e1.accept(this, null);
            out.write(", ");

            // Get bounds from the BBOX filter
            org.geotools.api.geometry.BoundingBox bounds = filter.getBounds();

            // Create a polygon from the BBOX
            out.write("ST_GeomFromText('POLYGON((");
            out.write(Double.toString(bounds.getMinX()));
            out.write(" ");
            out.write(Double.toString(bounds.getMinY()));
            out.write(",");
            out.write(Double.toString(bounds.getMinX()));
            out.write(" ");
            out.write(Double.toString(bounds.getMaxY()));
            out.write(",");
            out.write(Double.toString(bounds.getMaxX()));
            out.write(" ");
            out.write(Double.toString(bounds.getMaxY()));
            out.write(",");
            out.write(Double.toString(bounds.getMaxX()));
            out.write(" ");
            out.write(Double.toString(bounds.getMinY()));
            out.write(",");
            out.write(Double.toString(bounds.getMinX()));
            out.write(" ");
            out.write(Double.toString(bounds.getMinY()));
            out.write("))', ");
            out.write(String.valueOf(currentSRID));
            out.write(")");
        }

        out.write(")");
    }

    private void handleStandardSpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped, Object extraData)
            throws IOException {

        String operator;

        if (filter instanceof Contains) {
            operator = swapped ? "ST_Within" : "ST_Contains";
        } else if (filter instanceof Crosses) {
            operator = "ST_Crosses";
        } else if (filter instanceof Disjoint) {
            operator = "ST_Disjoint";
        } else if (filter instanceof Equals) {
            operator = "ST_Equals";
        } else if (filter instanceof Intersects) {
            operator = "ST_Intersects";
        } else if (filter instanceof Overlaps) {
            // Databricks may not directly support ST_Overlaps
            // Use alternative implementation:
            out.write("(NOT ST_Equals(");
            e1.accept(this, extraData);
            out.write(", ");
            e2.accept(this, extraData);
            out.write(") AND NOT ST_Disjoint(");
            e1.accept(this, extraData);
            out.write(", ");
            e2.accept(this, extraData);
            out.write("))");
            return;
        } else if (filter instanceof Touches) {
            operator = "ST_Touches";
        } else if (filter instanceof Within) {
            operator = swapped ? "ST_Contains" : "ST_Within";
        } else {
            throw new RuntimeException(
                    "Unsupported spatial operator: " + filter.getClass().getName());
        }

        out.write(operator);
        out.write("(");

        if (swapped
                && !filter.getClass().equals(Contains.class)
                && !filter.getClass().equals(Within.class)) {
            e2.accept(this, extraData);
            out.write(", ");
            e1.accept(this, extraData);
        } else {
            e1.accept(this, extraData);
            out.write(", ");
            e2.accept(this, extraData);
        }

        out.write(")");
    }

    /** Gets the distance value in meters from a distance buffer operator */
    private double getDistanceInMeters(DistanceBufferOperator operator) {
        // For simplicity, we're assuming the distance is already in meters
        // In a real implementation, you might need to convert from the specified units
        return operator.getDistance();
    }
}

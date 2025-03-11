# Getting Started with Spatial SQL

Updated:  Mar 4, 2025 | [Kent Marten](mailto:kent.marten@databricks.com)[Michael Johns](mailto:mjohns@databricks.com)

Thank you for participating in the Spatial SQL Private Preview with Databricks. The following guide will show you how to enable the preview and provide examples for types of queries you can now execute. *There is also a corresponding, and fairly comprehensive, notebook titled DBR 14.2 Getting Started: Spatial SQL Preview \[v2\] that you should also receive as part of onboarding to the Preview.*

# Highlights

The following are as-of DBR 16.2:

* Now at 80+ ST\_ expressions  
* Performance issue for ST\_Transform is fixed  
* [Photonization](https://www.databricks.com/product/photon) of nearly all ST\_ expressions, with goal of 100% by Public Preview  
* Nearly all ST\_ expressions are now supported in PySpark, with goal of 100% by Public Preview  
* [Spark Connect](https://www.databricks.com/blog/2022/07/07/introducing-spark-connect-the-power-of-apache-spark-everywhere.html) for all ST\_ Scala expressions; also for Scala [H3 APIs (GA)](https://docs.databricks.com/en/sql/language-manual/sql-ref-h3-geospatial-functions.html)  

Planned for Public Preview:

* GEOMETRY / GEOGRAPHY spatial type storage being implemented for Public Preview in coordination with community efforts around parquet, to include [Delta Lake](https://docs.databricks.com/en/delta/index.html)  
* Performance optimizations for spatial joins 

# Configuration

## Serverless Compute

The preview is  supported in [Serverless Compute](https://docs.databricks.com/en/compute/serverless/index.html#what-types-of-serverless-compute-are-available-on-databricks) for [notebooks](https://docs.databricks.com/en/compute/serverless/notebooks.html), [sql warehouses](https://docs.databricks.com/en/compute/sql-warehouse/index.html#what-is-serverless-sql), and [jobs](https://docs.databricks.com/en/jobs/run-serverless-jobs.html); it is enabled for customers by the Databricks team on a per workspace basis. After you sign the ToS, please reach out through your account team with any `workspace_id` to enable (can be multiple).

## Classic Compute

The preview is supported in Classic Compute for [all-purpose](https://docs.databricks.com/en/compute/use-compute.html) and [jobs](https://docs.databricks.com/en/jobs/compute.html) starting with DBR 14.2; customers can self-serve to enable or disable with the following which can be specified at the [cluster](https://docs.databricks.com/en/compute/configure.html#spark-configuration) or [session](https://docs.databricks.com/en/sql/language-manual/sql-ref-syntax-aux-conf-mgmt-set.html#databricks-runtime-examples) configs. *Note: you cannot set these configs on Serverless Compute.*

### Enable

#### SQL

| set spark.databricks.geo.st.enabled=true; |
| :---- |

#### Scala, Python

| spark.conf.set("spark.databricks.geo.st.enabled", "true") |
| :---- |

### Disable

#### SQL

| set spark.databricks.geo.st.enabled=false; |
| :---- |

#### Scala, Python

| spark.conf.set("spark.databricks.geo.st.enabled","false") |
| :---- |

## \[DLT\] Pipelines

The preview is supported in Pipelines in both Preview and Current channels. Customer need to self-serve enable the preview in this context by adding the pipeline configuration.  
![][image1]

# Imports

## SQL

Assuming the preview is enabled in the environment, spatial sql expressions are always available without any imports.

## Python

Import databricks functions available for use in pyspark dataframes, e.g. namespaced as dbf.

| from pyspark.databricks.sql import functions as dbf |
| :---- |

## Scala

Import databricks functions available for use in spark dataframes.

| import com.databricks.sql.functions.\_ |
| :---- |

## Additional

Since SQL is always available, you can make use of the following to execute sql expressions in dataframes of any supported language:

Dataframe function `selectExpr` \[[Python](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/api/pyspark.sql.DataFrame.selectExpr.html) | [R](https://spark.apache.org/docs/3.5.0/api/R/reference/selectExpr.html) | [Scala](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/api/pyspark.sql.functions.expr.html#pyspark.sql.functions.expr)\]

| df.selectExpr("\<sql-1\>"\[,"\<sql-2\>"..\]) |
| :---- |

Spark function `expr` is available  \[[Python](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/api/pyspark.sql.functions.expr.html#pyspark.sql.functions.expr) | [R](https://spark.apache.org/docs/3.5.0/api/R/reference/column_nonaggregate_functions.html) | [Scala](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/api/pyspark.sql.functions.expr.html#pyspark.sql.functions.expr)\]

| from pyspark.sql.functions import exprdf.select(expr("\<sql-1\>")\[,..\]) |
| :---- |

# Available Preview Functions

The following functions are available in the private preview as of DBR 16.3 released MARCH 2025, see [release notes](https://docs.databricks.com/en/release-notes/index.html) for various supported compute surfaces \- [DBR](https://docs.databricks.com/en/release-notes/runtime/index.html) and [Serverless](https://docs.databricks.com/en/release-notes/serverless/index.html#release-notes) as well as for [DBSQL](https://docs.databricks.com/en/sql/release-notes/index.html#available-databricks-sql-versions). *Note: spatial sql does not appear in release notes while in private preview.*

In the table below, column **DBR** is an as-of version, meaning version the function was first introduced. The column **Photon**  is an as-of version for the functions that have been photonized. *Note: during the preview, spatial functions have been limited to only execute within a photon-enabled compute environment, regardless of whether they have been photonized or not. The goal is to achieve 100% function photonization by public preview; those are identified as PuPr and GA in the column. Similar representations for **PySpark** column. Also, performance improvements will be added on an ongoing basis.*

## New Functions

The following 6 spatial predicates have been introduced in DBR 16.3.

| Function | DBR | Photon | PySpark | Function | DBR | Photon | PySpark |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **st\_covers(geo1, geo2)** | 16.3 | 16.3 | 16.3 | **st\_touches(geo1, geo2)** | 16.3 | 16.3 | 16.3 |
| **st\_disjoint(geo1, geo2)** | 16.3 | 16.3 | 16.3 | **st\_within(geo1, geo2)** | 16.3 | 16.3 | 16.3 |
| **st\_equals(geo1, geo2)** | 16.3 | 16.3 | 16.3 |  |  |  |  |

The following 13 functions have been introduced since the initial guide.

| Function | DBR | Photon | PySpark | Function | DBR | Photon | PySpark |
| ----- | :---- | :---- | :---- | ----- | :---- | :---- | :---- |
| **st\_addpoint(geo1, geo2, n)**  | 16.2 | 16.2 | 16.2 | **st\_pointn(geo)** | 16.1 | 16.1 | 16.1 |
| **st\_asewkt(geo, precision)** | 16.1 | 16.1 | 16.1 | **st\_removepoint(geo, n)** | 16.2 | 16.2 | 16.2 |
| **st\_dimension(geometry)** | 16.0 | 16.0 | 16.1 | **st\_reverse(geo)** | 16.2 | 16.2 | 16.2 |
| **st\_endpoint(geo)** | 16.1 | 16.1 | 16.1 | **st\_setpoint(geo1, index, geo2)** | 16.2 | 16.2 | 16.2 |
| **st\_flipcoordinates(geo)** | 16.0 | 16.0 | 16.1 | **st\_startpoint(geo)** | 16.1 | 16.1 | 16.1 |
| **st\_m(geo)** | 16.1 | 16.1 | 16.1 | **st\_z(geo)**  | 16.1 | 16.1 | 16.1 |
| **st\_multi(geo)** | 16.1 | 16.1 | 16.1 |  |  |  |  |

## Original Functions

The following 66 functions were part of the original guide.

| Function | DBR | Photon | PySpark | Function | DBR | Photon | PySpark |
| ----- | :---- | :---- | :---- | ----- | :---- | :---- | :---- |
| **st\_area(geo)** | 14.2 | 14.2 | 16.1 | **st\_intersects(geo1, geo2)** | 14.2 | 16.1 | 16.1 |
| **st\_asbinary(geo, endianness)** | 14.2 | 14.2 | 16.1 | **st\_isempty(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_asewkb(geo, endianness)** | 14.2 | 14.2 | 16.1 | **st\_isvalid(geo)** | 14.2 | PuPr | 16.1 |
| **st\_asgeojson(geo, precision)** | 14.2 | 14.2 | 16.1 | **st\_length(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_astext(geo, precision)** | 14.2 | 14.2 | 16.1 | **st\_makeline(geoArray)** | 14.2 | GA | 16.1 |
| **st\_aswkb(geo, endianness)** | 14.2 | 14.2 | 16.3 aliases st\_asbinary | **st\_makepolygon(outer\[, innerArray\])** | 14.2 | GA | 16.1 |
| **st\_aswkt(geo, precision)** | 14.2 | 14.2 | 16.3 aliases st\_astext | **st\_ndims(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_buffer(geo, radius)** | 14.2 | GA | 16.1 | **st\_npoints(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_centroid(geo)** | 14.2 | 14.2 | 16.1 | **st\_numgeometries(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_contains(geo1, geo2)** | 14.2 | PuPr | 16.1 | **st\_perimeter(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_convexhull(geo)** | 14.2 | 16.1 | 16.1 | **st\_point(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_difference(geo1, geo2)** | 14.2 | GA | 16.1 | **st\_pointfromgeohash(geohash)** | 14.2 | 14.2 | 16.1 |
| **st\_distance(geo1, geo2)**  | 14.2 | PuPr | 16.1 | **st\_rotate(geo, rotationAngle)** | 14.2 | 14.2 | 16.1 |
| **st\_distancesphere(geo1, geo2)** | 14.2 | 14.2 | 16.1 | **st\_scale(geo, xfactor, yfactor, zfactor)**  | 14.2 | 14.2 | 16.1 |
| **st\_distancespheroid(geo1, geo2)** | 14.2 | 14.2 | 16.1 | **st\_setsrid(geo, srid)** | 14.2 | 14.2 | 16.1 |
| **st\_envelope(geometry)** | 14.2 | 14.2 | 16.1 | **st\_simplify(geo, tolerance)** | 14.2 | PuPr | 16.1 |
| **st\_geogarea(geo)** | 14.2 | 14.2 | 16.1 | **st\_srid(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_geogfromgeojson(geojson)**  | 14.2 | 14.2 | 16.1 | **st\_transform(geo, srid)** | 14.2 | PuPr | 16.1 |
| **st\_geogfromtext(wkt)**  | 14.2 | 14.2 | 16.1 | **st\_translate(geo, xoffset, yoffset, zoffset)** | 14.2 | 14.2 | 16.1 |
| **st\_geogfromwkb(wkb)**  | 14.2 | 14.2 | 16.1 | **st\_union(geo1, geo2)**  | 14.2 | GA | 16.1 |
| **st\_geogfromwkt(wkt)** | 14.2 | 14.2 | 16.3 aliases st\_geogfromtext | **st\_union\_agg(geoCol)**  | 14.2 | GA | 16.1 |
| **st\_geoglength(geo)**  | 14.2 | 14.2 | 16.1 | **st\_x(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_geogperimeter(geo)** | 14.2 | 14.2 | 16.1 | **st\_xmax(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_geohash(geohash)** | 14.2 | 14.2 | 16.1 | **st\_xmin(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_geometryn(geo)** | 14.2 | 14.2 | 16.1 | **st\_y(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_geometrytype(geo)** | 14.2 | 14.2 | 16.1 | **st\_ymax(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_geomfromewkb(geojson)** | 14.2 | 14.2 | 16.1 | **st\_ymin(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_geomfromgeohash(geohash)** | 14.2 | 14.2 | 16.1 | **st\_zmax(geo)** | 14.2 | 14.2 | 16.1 |
| **st\_geomfromgeojson(geojson)**  | 14.2 | 14.2 | 16.1 | **st\_zmin(geo)**  | 14.2 | 14.2 | 16.1 |
| **st\_geomfromtext(wkt)** | 14.2 | 14.2 | 16.1 | **to\_geography(geoFormat)** | 14.2 | 14.2 | 16.1 |
| **st\_geomfromwkb(wkb)** | 14.2 | 14.2 | 16.1 | **to\_geometry(geoFormat)** | 14.2 | 14.2 | 16.1 |
| **st\_geomfromwkt(wkt)** | 14.2 | 14.2 | 16.3 aliases st\_geomfromtext | **try\_to\_geography(geoFormat)** | 14.2 | 14.2 | 16.1 |
| **st\_intersection(geo1, geo2)**  | 14.2 | PuPr | 16.1 | **try\_to\_geometry(geoFormat)** | 14.2 | 14.2 | 16.1 |

# Using Geometry/Geography types

*This section is unchanged from the initial guide. We are targeting persisted types for Public Preview.*

In general, Databricks spatial expressions support reading from Well-known Text (WKT), Well-known Binary (WKB), Extended Well-known Binary (EWKB)  and GeoJSON. You can also convert to native geospatial types (Geography and Geometry) and use these within a query. For the preview, you cannot materialize Geography and Geometry types or return them in a resultset, so you need to convert to your “favorite” columnar interchange format (e.g. WKT, WKB, or GeoJSON) prior to storing or including in final results.

The preview offers a number of overloaded signatures for ST\_ functions to allow convenience when starting from WKT (String), WKB (Binary), and GeoJSON (String) columnar data as those can be passed as parameters. For example if you want to check whether a column with GeoJSON contains 2D spatial data, you can invoke `ST_NDims(<geojson_col>)` directly versus `ST_NDims(ST_GeomFromGeoJSON(<geojson_col))`. However, in a chained query the return type standardized to the in-memory types has benefits, e.g. in the chain `ST_Buffer(ST_Envelope(<geojson_col>))`, both `ST_Buffer` and `ST_Envelope` return a GEOMETRY type, with `ST_Buffer` only accepting GEOMETRY param and `ST_Envelope` supporting overloads, so the standardization to GEOMETRY lends to efficiency since the in-memory type can be reused once generated. 

When you want to work with in-memory GEOGRAPHY types, e.g. for `ST_GeogArea`, `ST_GeogLength`, and `ST_GeogPerimeter` available in the preview, you can construct using calls like `ST_GeogFromWKT`, `ST_GeogFromWKB`, and `ST_GeogFromGeoJSON`, optionally specifying the SRID for WKT and WKB variants, default is 4326 (WGS84).

# Optimizing Spatial Filtering

*This section has been added since the initial guide.*

**Important**: The preview does not automatically generate spatial indexes on GEOMETRY/GEOGRAPHY columns, and spatial filters will not be performant unless customers take explicit actions as described below.

## Data Engineering

While in the private preview phase, table layout can be optimized for geometry-centric spatial filtering by calculating bounds columns (xmin, ymin, xmax, and ymax),  clustering the layout using [Liquid Clustering](https://docs.databricks.com/en/delta/clustering.html).

| *\-- table will use liquid clustering* CREATE OR REPLACE TABLE blg\_boundsCLUSTER BY (geom\_x\_min, geom\_y\_min, geom\_x\_max, geom\_y\_max)AS ( SELECT st\_xmin(geometry) as geom\_x\_min, st\_ymin(geometry) as geom\_y\_min, st\_xmax(geometry) as geom\_x\_max, st\_ymax(geometry) as geom\_y\_max, \*FROM base\_temp);*\-- execute liquid clustering or let databricks auto-manage*OPTIMIZE blg\_bounds; |
| :---- |

![][image2]

## Executing Spatial Filters

While in the private preview phase, customers are on their own to handle bounds for spatial filtering. By default the [range join](https://docs.databricks.com/en/optimizations/range-join.html) bin size is set to 10, in the units of your data. If you are working with 4326 data, that is 10 degrees which is quite expansive, \~1000km^2 area. So, we recommend you reason out a bin size appropriate to the scale of your data, e.g. maybe down to a single degree or even at a decimal value. Here is how you can set the bin size as a spark session config (showing sql). 

| SET spark.databricks.optimizer.rangeJoin.binSize=\<bin\_size\>; |
| :---- |

Note: you cannot supply spark settings in Serverless, so for that you would most likely supply a [range join hint](https://docs.databricks.com/en/optimizations/range-join.html#enable-range-join-using-a-range-join-hint) inline to your query.

| */\*+ RANGE\_JOIN(\<table|view|subquery\>, \<bin\_size\>) \*/* |
| :---- |

Given the following notional search polygon.

| CREATE OR REPLACE TEMPORARY VIEW blg\_env AS ( select   st\_xmin(env) as xmin,   st\_xmax(env) as xmax,   st\_ymin(env) as ymin,   st\_ymax(env) as ymax,   \* from (   select 'POLYGON((-114.99 32.56,-114.98 32.56,-114.98 32.55,-114.99 32.55,-114.99 32.56))' as env )); |
| :---- |

We can filter for any geometries, any part of whose bounds are within the range of the search polygon; this is where range join bin size is applied.

| CREATE OR REPLACE TEMPORARY VIEW blg\_env\_match AS (select \*from blg\_bounds, blg\_envwhere blg\_bounds.geom\_x\_min \<= blg\_env.xmax and blg\_bounds.geom\_x\_max \>= blg\_env.xmin and blg\_bounds.geom\_y\_min \<= blg\_env.ymax and blg\_bounds.geom\_y\_max \>= blg\_env.ymin); |
| :---- |

# Optimizing Spatial Joins with H3 {#optimizing-spatial-joins-with-h3}

*This section is mostly unchanged from the initial guide, the brief discussion on overcoming geographic bounds is new. We are targeting initial spatial performance for Public Preview, with iterative improvements up to GA and beyond.*

**Important**: The preview does not automatically generate spatial indexes on GEOMETRY/GEOGRAPHY columns, and spatial joins will not be performant unless customers take explicit actions as described below.

## Data Engineering

The preview supports, and recommends use of, `INLINE(H3_TessellateAsWKB(<geo>,<res>)` to effectively spatially index spatial data into chips at a given h3 resolution. `INLINE` explodes the resulting array from `H3_TessellateAsWKB`  and shreds, or flattens, the struct fields into three top-level columns: `cellid` containing h3 cell id, `core` to identify a chip being core vs boundary, and `chip` with the WKB. Table layout should be optimized by `cellid` using [Liquid Clustering](https://docs.databricks.com/en/delta/clustering.html).

| *\-- table will use liquid clustering**\-- The use of inline allows us to cluster by 'cellid'**\-- NOTICE: we do not include the full geometry 'g' here;**\--         rather, keep only vector chips per cellid*CREATE OR REPLACE TABLE state\_h3 CLUSTER BY (cellid) AS( SELECT state\_row\_id, INLINE(h3\_tessellateaswkb(g, 6)), \* EXCEPT(state\_row\_id, g) FROM state\_poly);*\-- execute liquid clustering or let databricks auto-manage*OPTIMIZE state\_h3; |
| :---- |

![][image3]

*Below we show a layer with h3 cellids  that cover the geometries as well as a layer with the original geometry. Not shown here, but If the chips were all unioned back together, you would have the original geometry.*

![][image4]

Data engineering for points is “somewhat” straightforward (see next section for more).

| *\-- table will use liquid clustering**\-- for points, we are just adding additional column(s)*CREATE OR REPLACE TABLE trip\_h3 CLUSTER BY (pickup\_cellid) AS( SELECT h3\_longlatash3(pickup\_longitude, pickup\_latitude, 6) AS pickup\_cellid, \* FROM trip);*\-- execute liquid clustering*OPTIMIZE trip\_h3; |
| :---- |

 ![][image5]

**Overcoming Geographic Bounds Challenges**

We say data engineering for points is “somewhat” straightforward because h3 provides geographic bounds for cellids (see [h3\_boundaryaswkb](https://docs.databricks.com/en/sql/language-manual/functions/h3_boundaryaswkb.html) for example). H3 does not use the same cellid geographic bounds for points, meaning the cellid returned from [h3\_longlatash3](https://docs.databricks.com/en/sql/language-manual/functions/h3_longlatash3.html) may place a point in a cellid even when the point is outside the geographic bounds of that cellid. *Note: our photonized functions use the h3 OSS C library, so they have the same behavior.*

To mitigate this inconsistency, we recommend that customers test the cellid returned from h3\_longlatash3 as well as its neighbors (see [h3\_kring](https://docs.databricks.com/en/sql/language-manual/functions/h3_kring.html)) to choose the cell that actually intersects the input point as opposed to the one returned by the h3 library. This way you get the consistency you want. In the extremely unlikely rare case that you have a point lying on the boundary of two cells you can break ties by taking the cell with the smaller or larger ID. This is shown below (favoring the smaller ID):

| WITH points(p) AS (SELECT st\_point(\-91.157627, 33.994067)),cells(p, c) AS (SELECT p, h3\_longlatash3(ST\_X(p), ST\_Y(p), 6) FROM points),rings(p, c) AS (SELECT p, explode(h3\_kring(c, 1)) FROM cells)SELECT min(c) as cell FROM rings WHERE st\_intersects(p, st\_geomfromwkb(h3\_boundaryaswkb(c))); |
| :---- |

This can be used on a routine basis when assigning cellids for points during data engineering, to avoid “surprises”; essentially, customers can consistently avoid using the direct output of `h3_longlatash3` deferring to the assigned cellid instead. 

**Look closer at trips data**

The average cell area for h3 resolution 6 is 36km2, so with a quick plot of NYC, we can see that there are multiple outliers above \~1M which we would want to remain mindful of when considering join skew (especially since this is only a 1% sample). 

| select   pickup\_cellid, count(1) as count from trip\_h3 group by pickup\_cellid order by count desc |
| :---- |

## ![][image6]Executing Spatial Joins 

The preview supports, and recommends use of, additional `WHERE` clauses to first test for h3 `cellid` matches and then only invoke more intensive spatial predicates, such as `ST_Contains` or `ST_Intersects`, when a boundary chip is involved (`core = false`).

The examples below, \[1\] and \[2\], are adapted from, and extend, this [notebook](https://www.databricks.com/wp-content/uploads/notebooks/db-400-h3-geospatial/nb02-compare-h3-geometry-approaches.html), which we used in a previous h3 blog series. The difference is that in the preview, these functions are now provided without requiring any external library. *A more detailed example of accomplishing data engineering steps for performant spatial joins can be found within the Getting Started  Notebook that goes with this guide.*

### \[1\]: H3-Approximate Query

| *\-- Use "pure" H3 for fast and approximate comparisons*create table if not exists approx\_pickup\_zone as (selecttaxi\_zone\_h3\_chip.\*,t.\*from (select */\*+ SKEW('pickup\_cellid') \*/* \* from trip\_h3) as tjoin taxi\_zone\_h3\_chipon cellid \== pickup\_cellid) |
| :---- |

### \[2\]: H3-Precise Query \[Hybrid Pattern\]

| *\-- For precise (using H3 spatial indexing) add clause:**\--  where \<core\_col\> or st\_contains(\<geoExpr1\>,\<geoExpr2\>)*create table if not exists precise\_pickup\_zone as (selecttaxi\_zone\_h3\_chip.\*,t.\*from (select */\*+ SKEW('pickup\_cellid') \*/* \* from trip\_h3) as tjoin taxi\_zone\_h3\_chipon cellid \== pickup\_cellidwhere core or st\_contains(st\_geomfromwkb(chip), st\_point(pickup\_longitude, pickup\_latitude))) |
| :---- |

## H3 “Hybrid” Spatial Queries

For tables prepared with h3 based data engineering, as recommended, non-point geometries will have been exploded into row-per-`cellid`. Assuming the presence of a geo id (in this case ‘state\_row\_id’), you can drop the h3 affiliated fields to coerce a GEOMETRY/GEOGRAPHY centric response. Further, `ST_Union_Agg` can be used to aggregate h3 chips into a single GEOMETRY/GEOGRAPHY, e.g. through use of `Group By` on a geo id.

### \[1\]: Geometry-Centric Results via Chip Union

| *\-- we can union h3 chips back together, e.g. ones involved in the results*select state\_row\_id, state, st\_astext(st\_union\_agg(chip)) as chip\_union from (select distinct state\_row\_id, state, chip from pickup\_state\_h3) group by state\_row\_id, state order by state\_row\_id |
| :---- |

![][image7]

### \[2\]: Geometry-Centric Aggregated Results

| *\-- we can join back the original geometry**\-- most suitable when performing aggregates*SELECT   t.\*,   state\_poly.g FROM (   SELECT     state\_row\_id, state, count(1) as pickup\_cnt,     format\_number(count(1),0) as display\_cnt     FROM pickup\_state\_h3   GROUP BY state\_row\_id, state ) as t join state\_poly on t.state\_row\_id \== state\_poly.state\_row\_id ORDER BY state\_row\_id |
| :---- |

![][image8]

*Here is the aggregate pickup counts from the point-in-polygon join per state.*

![][image9]

### \[3\]: Data-Centric Results

| *\-- we can easily filter out the h3 related fields after a spatial join*select \* except(cellid, core, chip, pickup\_cellid) from pickup\_state\_h3 |
| :---- |

![][image10]

# FAQ

*This section is unchanged from the initial guide; also, see Spatial SQL \- Private Preview \- FAQ that you should also receive as part of onboarding to the Preview.*

#### \[1\] What should I do to fix a query that throws \[ST\_UNSUPPORTED\_RETURN\_TYPE\] error?

There is additional information in the exception stating something like the following: 

*SparkUnsupportedOperationException: \[ST\_UNSUPPORTED\_RETURN\_TYPE\] The GEOGRAPHY and GEOMETRY data types cannot be returned in queries. Use one of the following SQL expressions to convert them to standard interchange formats: "st\_asbinary", "st\_astext", or "st\_asgeojson". SQLSTATE: 0A000.* 

You can correct a query such as `SELECT st_buffer(<geoExpr>)` by converting the return to WKT, WKB, or GeoJSON, e.g. `SELECT st_astext(st_buffer(<geoExpr>))` for WKT.

#### \[2\] How do I initially get my data into the Lakehouse to use spatial sql preview?

 Please reach out to your account team for guidance on getting data into the Databricks Lakehouse. It may involve using our [built-in](https://docs.databricks.com/en/external-data/index.html#interact-with-external-data-on-databricks) readers, available geospatial readers, such as  from one of our [DBLabs](https://www.databricks.com/learn/labs) projects or various 3rd party libraries. The optimal format to target is [Delta Lake](https://docs.databricks.com/en/delta/index.html) which Databricks specially leverages in both [DBR Clusters](https://docs.databricks.com/en/clusters/index.html#databricks-runtime) and [DBSQL](https://www.databricks.com/product/databricks-sql). There are some pretty straightforward patterns to process data from various formats into Delta Lake, so please let us know if you need assistance.

# Functions

*This section has been updated to include all functions available as of DBR 16.2.*

For a quick reference while you are in a notebook or sql warehouse, you can list functions matching a pattern.

| show functions like 'st\_\*'; |
| :---- |

For any function, you can also describe it to get the signature, a summary, and simple examples of usage. *Note: this will also return the classpath which can be useful to understand if you are actually using a databricks provided function or from somewhere else.*

| describe function extended \<fun\>; |
| :---- |

## Function Details

|  | Input type(s) | Output type |
| :---: | ----- | ----- |
| [`h3_tessellateaswkb`](https://docs.databricks.com/en/sql/language-manual/functions/h3_tessellateaswkb.html) | (STRING, INT\[, BOOLEAN\])/(GEOGRAPHY, INT\[, BOOLEAN\])/(BINARY, INT\[, BOOLEAN\]) | STRUCT{BIGINT, BOOLEAN, BINARY} |
| Description | [Tessellate](https://en.wikipedia.org/wiki/Tessellation) input vector data using h3 global grid indexing. Takes as input two required and one optional argument. More on other [productized h3](https://docs.databricks.com/en/sql/language-manual/sql-ref-h3-geospatial-functions.html) functions here. A geography object (either a GEOGRAPHY value, or a STRING/BINARY value representing a geography in GeoJSON, WKB, or WKT format). A resolution value (value between 0 and 15, inclusive). \[Optional\] A boolean value indicating whether the WKB description of core H3 cells should be present in the output, or replaced by NULL values (see below). The function returns an array of named structs. The array represents a tessellation of the input geography using H3 cells at the specified resolution. The tessellation is essentially a decomposition of the geography using a minimal covering set of H3 cells. The structs have three fields: "cellid", "core", "chip". The "cellid" is an H3 cell at the specified resolution that is a member of the minimal covering set of H3 cells of the geography at the specified resolution. The "core" field is a boolean value that indicates whether the H3 cell of the struct is fully contained in the geography (for core cells the intersection of the geography with the cell's polygon is the cell's polygon). The "chip" field is a BINARY value, and corresponds to the intersection of the input geography with the cell's polygon, represented in WKB format. If the case where the cell is fully contained inside the geography and the third optional argument is set to false, the "chip" field value would be NULL instead. |  |
| Notes | *This function is GA but listed for clarity when using the “hybrid” tessellation pattern described in [Optimizing Spatial Joins with H3](#optimizing-spatial-joins-with-h3).* `h3_tessellateaswkb` allows vector chipping of spatial data to establish a spatial index, which is essential to achieve scaled performance. Without H3 indexing, you as a user will have a poor experience even before hitting large scales, see discussion from previous blog on the impact of not having any index. v1 of the preview has no implicit / managed indexing of your spatial data. Read on below to understand more about how to data engineer your base geometry tables to establish spatial indexing. Disregards any Z or M coordinates that the input geography may have. The WKB returned as the third field of the structs is a 2D geography. Does not support geometry collections as input. An error is returned if the input is a geometry collection. Will throw an error if the resolution is outside the valid range. Will throw an error if the input is a STRING/BINARY value that is an invalid GeoJSON, WKB, or WKT representation of a geography. The input geography is expected to have coordinates in the WGS84 coordinate reference system. It is best practice to use INLINE to shred the table, effectively exploding the array of structs and making the 3 fields within the struct (“cellid”, “core”, “chip”) top level columns. |  |
| Example(s) | *\-- THREE OPTIONS **\[\#3 RECOMMENDED\]****\-- These are from the Getting Started Notebook* |  |
|  | *\-- \[1\] tessellating at h3 resolution 6\-- returns an array per geom, so doesn't explode or perform table shredding\-- **\!\!\! This is not recommended for scaled performance \!\!\!**select   h3\_tessellateaswkb(g, 6) as tessellate, \* from state\_poly*  ![][image11] |  |
|  | *\-- \[2\] tessellating at h3 resolution 6 \+ \`explode\`\-- returns a row per h3 cell per geom, but doesn't perform struct shredding\-- **\!\!\! This is not recommended for optimized table ordering, e.g. with liquid clustering \!\!\!**select   explode(h3\_tessellateaswkb(g, 6)) as tessellate,   \* except (g) from state\_poly*  ![][image12] |  |
|  | *\-- \[3\] tessellating at h3 resolution 6 \+ \`inline\`\--     returns a row per h3 cell per geom with struct shredding, meaning:\--     'cellid', 'core', and 'chip' become top-level fields\--     **\!\!\! This is recommended for scaled performance and optimized table ordering \!\!\!**select inline(h3\_tessellateaswkb(g, 6)), \* except (g) from state\_poly where state \= "New York" limit 10*  ![][image13] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_addpoint` | (GEOMETRY, INT) / (GEOGRAPHY, INT) | GEOMETRY / GEOGRAPHY |
| Description | Adds a new point to the n-th position in the input linestring GEOGRAPHY or GEOMETRY. Default value for n is \-1, if not provided. |  |
| Notes | Takes as input a geospatial value and a 1-based index, and adds the provided point before the Nth position in a single linestring. Negative values are counted backwards from the end of the linestring, so that \-1 is the last point. The ST\_AddPoint expression throws an error in the following cases: STInvalidArgumentTypeError, if the first input geospatial value is not a linestring geography or geometry. STInvalidArgumentTypeError, if the second input geospatial value is not a point geography or geometry. STInvalidArgumentError, if the first input geospatial value is an empty linestring geography or geometry. STInvalidIndexValue, if the input geospatial values are a linestring and a point, but the index is 0\. STInvalidIndexValue, if the input geospatial values are a linestring and a point, but the absolute value of the index is greater than the number of points in the linestring (i.e. index is invalid).  |  |
| Example(s) | `SELECT st_astext(st_addpoint(st_geomfromtext('LINESTRING(1 2,3 4)'), st_geomfromtext('POINT(7 8)'), 3));` LINESTRING(1 2,3 4,7 8\)  `SELECT st_asewkt(st_addpoint(st_geomfromtext('LINESTRING(1 2,3 4)', 4326), st_geomfromtext('POINT(7 8)', 4326)));` SRID=4326;LINESTRING(1 2,3 4,7 8\)  `SELECT st_asewkt(st_addpoint(st_geogfromtext('LINESTRING ZM (1 2 3 4,5 6 7 8)'), st_geogfromtext('POINT M (0 9 99)'), -1));` SRID=4326;LINESTRING ZM (1 2 3 4,5 6 7 8,0 9 0 99\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_area` | GEOMETRY / GEOGRAPHY / STRING / BINARY | DOUBLE |
| `st_geogarea` | STRING / BINARY | DOUBLE |
| Description | Returns the area of a polygonal geometry. Accepts STRING or BINARY inputs representing a geography/geometry in GeoJSON, WKB, or WKT format. |  |
| Notes | In the case of `ST_Area` the input value is understood as a geometry.  For points, linestrings, multipoints, and multilinestrings we always return 0\.  For polygons we return the 2D Cartesian area of the polygon.  For multipolygons we return the sum of the areas of the polygons in the multipolygon.  For geometry collections we return the sum of the areas of the elements in the collection.  The units of the result are those of the coordinates of the geometry. In the case of `ST_GeogArea` the input value is understood as a geography.  We expect the input coordinates to be in degrees, and the underlying coordinate system is assumed to be WGS84.  For points, linestrings, multipoints, and multilinestrings we always return 0\.  For polygons we return the 2D geodesic area of the polygon. For multipolygons we return the sum of the areas of the polygons in the multipolygon.  For geometry collections we return the sum of the areas of the elements in the collection.  The units of the result are squared meters. For both expressions we return an error if the input geography/geometry representation is not a valid GeoJSON, WKB, or WKT. |  |
| Example(s) | *\-- NOTICE: difference between \`st\_geogarea\` \[meters\] \--         and \`st\_area\` \[units: e.g. degrees\]*with samp\_wkt as (  select     1 as row\_id,     'POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))' as g ) select    st\_geogarea(g) as dbx\_area\_meters,    st\_area(g) as dbx\_area\_units, \*  from samp\_wkt  ![][image14] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_asbinary / st_aswkb` | (GEOMETRY\[, STRING\]) / (GEOGRAPHY\[, STRING\]) | BINARY |
| Description | Returns Well-Known Binary (WKB) representation of the geometry/geography input. The optional second parameter is to specify endian encoding, either little-endian ('NDR') or big-endian ('XDR'). |  |
| Notes |  |  |
| Example(s) | *\-- we are initially converting from WKT to geometry type for convenience*select st\_asbinary(st\_geomfromtext(     'POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))')) as g  ![][image15] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_asewkb` | (GEOMETRY\[, STRING\]) / (GEOGRAPHY\[, STRING\]) | BINARY |
| Description | Returns Extended Well-Known Binary (EWKB) representation of the geometry/geography input. The optional second parameter is to specify endian encoding, either little-endian ('NDR') or big-endian ('XDR'). More on EWKB [here](https://libgeos.org/specifications/wkb/#extended-wkb). |  |
| Notes |  |  |
| Example(s) | *\-- we are initially converting from WKT to geometry type for convenience**\-- showing optional big-endian param*select st\_asewkb(st\_geomfromtext(    'POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))'), 'XDR') as g  ![][image16] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_asewkt` | (GEOMETRY\[, INT\]) / (GEOGRAPHY\[, INT\]) | GEOMETRY / GEOGRAPHY |
| Description | Returns the geospatial value (value of type GEOGRAPHY or GEOMETRY) in EWKT format using the specified precision, if provided. |  |
| Notes | Takes as input a geometry or geography and returns the Extended Well-Known Text representation (i.e. WKT prefixed with the SRID). The optional precision argument may be used to reduce the maximum number of decimal digits after floating point used in output (defaults to 15). |  |
| Example(s) | `SELECT st_asewkt(st_geomfromtext('POINT Z (2.718281828 3.141592653 100)', 4326));` SRID=4326;POINT Z (2.718281828 3.141592653 100\) `SELECT st_asewkt(st_geomfromtext('POINT Z (2.718281828 3.141592653 100)', 4326), 4);` SRID=4326;POINT Z (2.718 3.142 100\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_asgeojson` | (GEOMETRY\[, INT\]) / (GEOGRAPHY\[, INT\]) | STRING |
| Description | Returns GeoJSON representation of the geometry/geography input.  The optional second parameter is to specify precision. |  |
| Notes |  |  |
|  Example(s) | *\-- we are initially converting from WKT to geometry type for convenience* |  |
|  | *\-- \[1\] without optional precision*select st\_asgeojson(   st\_geomfromtext('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))')) as g  ![][image17] |  |
|  | *\-- \[2\] with optional precision=8*select st\_asgeojson(   st\_geomfromtext('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))'), 8\) as g  ![][image18] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_astext / st_aswkt` | (GEOMETRY\[, INT\]) / (GEOGRAPHY\[, INT\]) | STRING |
| Description | Returns Well-Known Text (WKT) representation of the geometry/geography input. The optional second parameter is to specify precision. |  |
| Notes |  |  |
| Example(s) | *\-- we are initially converting from WKT to geometry type for convenience*select st\_astext(st\_geomfromtext('POINT(-74.0060 40.7128)')) as g  ![][image19] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_buffer` | GEOMETRY, DOUBLE | GEOMETRY |
| Description | Computes a POLYGON or MULTIPOLYGON that represents all points whose distance from a geometry is less than or equal to a given distance.Takes as input a geometry and a radius, and returns the buffered geometry using the input radius.  |  |
| Notes | The buffer of a geometry is the Minkowski sum or difference (depending or whether the radius is positive or negative, respectively) of the geometry with a disk of the given (absolute) radius. Regarding the SRID value of the result: If the input value is of type GEOMETRY, the SRID value of the output GEOMETRY value is equal to that of the input value. If the input value is of type BINARY, the SRID value of the output GEOMETRY value is 0\. If the input value is of type STRING and represents a geometry in WKT format, the SRID value of the output GEOMETRY value is 0\. If the input value is of type STRING and represents a geometry in GeoJSON format, the SRID value of the output GEOMETRY value is 4326\. The result is always a 2D polygon or multipolygon (in other words, we drop the Z and M coordinates from the input geometry). |  |
| Example(s) | *\-- radius 0.001 specified (same units as input)*select st\_astext(st\_buffer(st\_geomfromtext('POINT(-74.0060 40.7128)'), 0.001)) as buf\_g  ![][image20] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_centroid` | GEOMETRY | GEOMETRY |
| Description |  Takes as input a geometry object and returns the centroid of that geometry as a 2D point. |  |
| Notes | A few details about the behavior: If the input geometry is empty, the 2D empty point is returned. If the input geometry consists of points only, the centroid is the average of the X and Y coordinates of the points. If the input geometry contains linear segments (but no areal geometries), the centroid is the weighted average of the midpoints of the linear segments, where the weights are the lengths of the segments. If the input geometry contains polygons, the centroid is the weighted average of the centroids of the polygons, where the weights are the areas of the polygons. In case of mixed topological dimension components, the centroid computation is based on the components of highest topological dimension. If the input is a BINARY value, it is expected to be the WKB description of a geometry. If the input is a STRING value, it is expected to be the GeoJSON or WKT description of a geometry. If the input is GeoJSON, the SRID of the resulting geometry is 4326\. If the input is WKB or WKT, the SRID of the resulting geometry is 0\. If the input is a GEOMETRY value, the SRID value of the output geometry is the same as that of the input value.  |  |
| Example(s) | select st\_astext(st\_centroid(g)) as c, g from samp\_line  ![][image21] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_contains` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | BOOLEAN |
| `st_intersects` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | BOOLEAN |
| Description | Two common spatial predicate expressions: `ST_Contains`: takes as input two geometries and returns true iff the first geometry contains the second. `ST_Intersects`: takes as input two geometries and returns true iff the two geometries intersect. |  |
| Notes | Spatial predicates are more compute intensive. *For performance purposes, we strongly urge customers to spatially index their data using productized functions h3\_lonlatash3 and h3\_tessellateaswkb.* Some requirements or details on the behavior: Both expressions take either two GEOMETRY values as input or any combination of STRING and BINARY inputs. In the latter case the inputs are expected to be standard geospatial representation of geometries (GeoJSON, WKB, or WKB). Both expressions require that both inputs are not geometry collections, otherwise an error is returned. If the two inputs are GEOMETRY values, they are expected to have the same SRID value (otherwise an error is returned). The notion of containment or intersection that is implemented follows the [DE-9IM](https://en.wikipedia.org/wiki/DE-9IM) matrix semantics. |  |
| Example(s) | *\-- assign taxi pickups a state using st\_contains**\-- for simplicity, just showing the \~12.2M result count**\-- h3 is the spatial indexing used to drive performance*selectformat\_number(count(1),0) as countfrom (select */\*+ SKEW('pickup\_cellid') \*/* \* from trip\_h3) as t join state\_h3 on cellid \== pickup\_cellid where   core or   st\_contains(st\_geomfromwkb(chip), st\_point(pickup\_longitude, pickup\_latitude))  ![][image22] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_convexhull` | GEOMETRY / STRING / BINARY | GEOMETRY |
| Description | Takes as input a geometry and returns the convex hull of the input geometry as a GEOMETRY value. |  |
| Notes | Has overloads for BINARY, GEOMETRY, and STRING inputs. In the case of BINARY and STRING inputs we expect the values to be valid representations of a geometry in one of the standard geospatial formats GeoJSON (STRING), WKB (BINARY), and WKT (STRING). |  |
| Example(s) |  select \*, st\_astext(st\_convexhull(g)) as cg from samp\_line  ![][image23] Using map\_render utility function in Getting Started notebook (blue is ‘g’ and tan is ‘cg’):  ![][image24] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_difference` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | GEOMETRY |
| `st_intersection` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | GEOMETRY |
| `st_union` | (GEOMETRY, GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | GEOMETRY |
| Description | Three geospatial expressions related to boolean set operations: `ST_Difference`: takes as input two geometries and returns their point-set difference as a GEOMETRY value. `ST_Intersection`: takes as input two geometries and returns their point-set intersection as a GEOMETRY value. `ST_Union`: takes as input two geometries and returns their point-set union as a GEOMETRY value. |  |
| Notes | All three expressions take either two GEOMETRY values as input or any combination of STRING and BINARY inputs. In the latter case the inputs are expected to be standard geospatial representation of geometries (GeoJSON, WKB, or WKB). All three expressions require that both inputs are not geometry collections, otherwise an error is returned. If the two inputs are GEOMETRY values, they are expected to have the same SRID value (otherwise an error is returned). In this case the SRID value of the returned GEOMETRY value is equal to the common SRID value of the inputs. If the two inputs are STRING/BINARY values then the SRID value of the output GEOMETRY is either 4326 (if both inputs are in GeoJSON format), or 0 (all other cases). |  |
| Example(s) | *\-- difference*select st\_astext(st\_difference('LINESTRING(0 0,0 2)', 'LINESTRING(0 1,0 2)')) as overlap\_lines, st\_astext(st\_difference('LINESTRING(0 0,0 2)', 'LINESTRING(1 2,1 3)')) as non\_overlap\_lines  ![][image25] |  |
|  | *\-- intersection*select st\_astext(st\_intersection('LINESTRING(0 0,0 2)', 'LINESTRING(0 1,0 2)')) as overlap\_lines, st\_astext(st\_intersection('LINESTRING(0 0,0 2)', 'LINESTRING(2 2,2 3)')) as non\_overlap\_lines  ![][image26] |  |
|  | *\-- union*select st\_astext(st\_union('LINESTRING(0 0,0 2)', 'LINESTRING(0 1,0 2)')) as overlap\_lines, st\_astext(st\_union('LINESTRING(0 0,0 2)', 'LINESTRING(2 2,2 3)')) as non\_overlap\_lines  ![][image27] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_dimension` | GEOMETRY / GEOGRAPHY | INT |
| Description | Returns the topological dimension of the 2D projection of the geometry. |  |
| Notes | Takes as input a geometry and returns the topological dimension of the geometry, as follows: It returns 0 if the input is a point or multipoint. It returns 1 if the input is a linestring or multilinestring. It returns 2 if the input is a polygon or multipolygon. It returns 0 if the input is an empty geometry collection. For non-empty geometry collections it returns the maximum of the topological dimensions of the elements in the collection. |  |
| Example(s) | `SELECT st_dimension('MULTIPOINT(EMPTY,-1 0,EMPTY)');` 0 `SELECT st_dimension('LINESTRING(-1 0,0 -1,1 0,0 1,-1 0)');` 1 `SELECT st_dimension('MULTIPOLYGON(EMPTY,((-1 0,0 -1,1 0,0 1,-1 0)))');` 2 `SELECT st_dimension('GEOMETRYCOLLECTION EMPTY');` 0 |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_distance` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | DOUBLE |
| `st_distancesphere` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | DOUBLE |
| `st_distancespheroid` | (GEOMETRY,GEOMETRY) / (STRING, STRING) / (STRING, BINARY) / (BINARY, STRING) / (BINARY, BINARY) | DOUBLE |
| Description | 2D Euclidean `ST_Distance`: takes as input two geometries, or two standard representations of geometries (GeoJSON, WKB, or WKB), and returns the 2D Euclidean distance of the two geometries. The units of the returned distance are those of the input geometries.  WGS84 Ellipsoid `ST_DistanceSphere`: takes as input two point geometries and returns their spherical distance in meters, measured on a sphere whose radius is the *mean radius of the WGS84 ellipsoid*. The expression can also take STRING/BINARY values as input, representing geometries in GeoJSON, WKB, or WKT format. `ST_DistanceSpheroid`: takes as input two point geometries and returns their geodesic distance in meters, *measured on the WGS84 ellipsoid*. The expression can also take STRING/BINARY values as input, representing geometries in GeoJSON, WKB, or WKT format. |  |
| Notes | For `ST_Distance`:   If at least one of the geometries is empty, NULL is returned.   If the input geospatial representations are not valid, a parse error is returned.   If the two inputs are GEOMETRY values with different SRIDs, an error is returned. For both `ST_DistanceSphere` and `ST_DistanceSpheroid`:   The geometries are expected to have coordinates in degrees.   Returns NULL if at least one of the two input point geometries is empty.   Returns an error if at least one of the two input geometries is not a point.   In the case both inputs are GEOMETRY values, they are expected to have the same SRID value. Otherwise an error is returned. |  |
| Example(s) | with samp\_pnts as (  select 'POINT(-74.0060 40.7128)' as nyc, 'POINT(-77.0257 38.9005)' as dc)*\-- notice \~3.5 degrees vs 327K meters* *\-- sphere uses the mean radius of the WGS84 ellipsoid**\-- spheroid uses the full WGS84 ellipsoid datum*select   format\_number(st\_distance(nyc,dc), 8) as dist\_degrees,   format\_number(st\_distancesphere(nyc,dc), 4) as dist\_sphere\_meters,   format\_number(st\_distancespheroid(nyc,dc), 4) as dist\_spheroid\_meters from samp\_pnts  ![][image28] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_endpoint` | GEOMETRY / GEOGRAPHY | GEOMETRY / GEOGRAPHY |
| Description | Returns the last point of the input GEOGRAPHY or GEOMETRY value, if the input geospatial value is a non-empty linestring. Otherwise, returns NULL. The SRID value of the output point geography or geometry is the same as that of the input value. |  |
| Notes |   |  |
| Example(s) | `SELECT st_asewkt(st_endpoint(st_geomfromtext('LINESTRING(1 2,3 4,5 6)', 4326)));` SRID=4326;POINT(5 6\) `SELECT st_asewkt(st_endpoint(st_geogfromtext('LINESTRING ZM (1 2 3 4,5 6 7 8)')));` SRID=4326;POINT ZM (5 6 7 8\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_envelope` | GEOMETRY / STRING / BINARY | GEOMETRY |
| Description | Takes as input a geometry and returns a 2D geometry representing the axis-aligned 2D minimum bounding box of the input geometry (if one exists). |  |
| Notes |  In more detail: If the input geometry is empty, the algorithm returns a copy of the input geometry. If the bounding box of the input geometry degenerates to a point, the algorithm returns that point. If the bounding box degenerates to an axis-aligned segment, the algorithm returns a linestring with 2 points corresponding to the two endpoints of the segment. The points of the linestring are lexicographically ordered. In all other cases the algorithm returns a polygon with a single ring and 4 distinct vertices (the number of points is 5 since the ring needs to be closed). The ring/polygon is clockwise-oriented and its first (and last) point is the lexicographically minimum point of the axis-aligned minimum bounding box. In all cases, the SRID of the returned geometry is equal to the SRID of the input geometry. Although it should be clear from the description above, for non-empty input, the Envelope algorithm: Returns a 2D geometry. The Z and M coordinates of the input geometry are ignored (if they exist). |  |
| Example(s) | select st\_astext(st\_envelope('LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)')) as env\_line, st\_astext(st\_envelope('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))')) as env\_poly  ![][image29] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_flipcoordinates` | GEOMETRY / GEOGRAPHY | GEOMETRY / GEOGRAPHY |
| Description | Swaps X and Y coordinates of the input geometry. |  |
| Notes |  |  |
| Example(s) | `SELECT st_astext(st_flipcoordinates(st_geomfromtext('LINESTRING(1 2,3 4,5 6,7 8)')));` LINESTRING(2 1,4 3,6 5,8 7\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_geogfromgeojson` | STRING | GEOGRAPHY |
| `st_geogfromtext / st_geogfromwkt` | STRING | GEOGRAPHY |
| `st_geogfromwkb` | BINARY | GEOGRAPHY |
| Description | `ST_GeogFromGeoJSON`: reads a geography object from a GeoJSON string. `ST_GeogFromText` / `ST_GeogFromWKT`: reads a geography object from a WKT string. `ST_GeogFromWKB`: reads a geography object from a WKB binary. |  |
| Notes |  |  |
| Example(s) | \<no example provided\> |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_geometrytype` | GEOMETRY / GEOGRAPHY / STRING / BINARY | STRING |
| Description | Takes as input a GEOGRAPHY or GEOMETRY value and returns its type as a string. |  |
| Notes |  |  |
| Example(s) | select st\_geometrytype('POINT(-77.0257 38.9005)') as pnt, st\_geometrytype('LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)') as line, st\_geometrytype('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))') as poly  ![][image30] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_geomfromgeojson` | STRING | GEOMETRY |
| `st_geomfromtext / st_geomfromwkt` | (STRING\[, INT\]) | GEOMETRY |
| `st_geomfromwkb` | (BINARY\[, INT\]) | GEOMETRY |
| `st_geomfromewkb` | (BINARY\[, INT\]) | GEOMETRY |
| Description | `ST_GeomFromGeoJSON`: reads a geometry object from a GeoJSON string. `ST_GeomFromText` / `ST_GeomFromWKT`: reads a geometry object from a WKT string. `ST_GeomFromWKB`: reads a geometry object from a WKB binary. `ST_GeomFromEWKB`: reads a geometry object from a EWKB binary. The optional integer parameter, when present, is for SRID. |  |
| Notes | GeoJSON specifies that SRID must be 4326, so SRID is not provided to `ST_GeomFromGeoJSON`. |  |
| Example(s) | \<no example provided\> |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_isvalid` | GEOMETRY / STRING / BINARY | BOOLEAN |
| Description | Takes as input a geometry object and returns true iff the geometry is valid according to the OGC [Simple Feature Access](https://portal.ogc.org/files/?artifact_id=25355) specification. |  |
| Notes | A few details about the behavior: If the input is a BINARY value, it is expected to be the WKB description of a geometry. If the input is a STRING value, it is expected to be the GeoJSON or WKT description of a geometry. If the input is STRING or BINARY and the description of the geometry is invalid the function returns false (does not throw a parse error). |  |
| Example(s) | select st\_isvalid(null) as null\_test, st\_isvalid('POLYGON EMPTY') as empty\_poly, st\_isvalid('POLYGON((0 0, 10 0, 10 5, 6 \-2, 0 0))') as self\_intersect\_poly, st\_isvalid('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))') as poly  ![][image31] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_length` | GEOMETRY / GEOGRAPHY / STRING / BINARY | DOUBLE |
| `st_geoglength` | STRING / BINARY | DOUBLE |
| Description | `ST_Length`: the input value is understood as a geometry. For points, polygons, multipoints, and multipolygons we always return 0\. For linestrings we return the sum of the 2D Cartesian lengths of the segments in the linestring. For multilinestrings we return the sum of the lengths of the linestrings in the multilinestring. For geometry collections we return the sum of the lengths of the elements in the collection. The units of the result are those of the coordinates of the geometry. `ST_GeogLength`: the input value is understood as a geography. We expect the input coordinates to be in degrees, and the underlying coordinate system is assumed to be WGS84. For points, polygons, multipoints, and multipolygons we always return 0\. For linestrings we return the sum of the 2D geodesic lengths of the segments in the linestring. For multilinestrings we return the sum of the lengths of the linestrings in the multilinestring. For geometry collections we return the sum of the lengths of the elements in the collection. The units of the result are meters. |  |
| Notes | They take as input a STRING or BINARY value representing a geography/geometry in GeoJSON, WKB, or WKT format. More specifically, if the input is a BINARY value we treat it as the WKB representation, whereas if the input is a STRING value we detect whether it is GeoJSON or WKT. |  |
| Example(s) | *\-- NOTICE: difference between \`st\_geoglength\` \[meters\] and \`st\_length\` \[units: e.g. degrees\]*select st\_geoglength(g) as dbx\_length\_meters, st\_length(g) as dbx\_length\_units, \* from (select 'LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)' as g)  ![][image32] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_makeline` | ARRAY{GEOMETRY} | GEOMETRY |
| Description | Takes as input an array of geometries, which are expected to be points, linestrings, or multipoints. It returns a linestring geometry whose points are the non-empty points of the geometries in the input array of geometries. The order of the points is preserved in the output linestring. |  |
| Notes | Errors: If any of the input geometries is not a point, linestring, or multipoint an error is returned. If the input geometries do not have the same SRID value an error is returned. Properties of the output linestring: The SRID value of the output linestring is the common SRID value of the input geometries. The dimension of the returned linestring is the maximum common dimension of the input geometries. Any NULL values in the input array are ignored. Some edge cases: If the input array is empty, the 2D empty linestring is returned. The SRID of the returned linestring is 0 in this case. If all input geometries are empty, the 2D empty linestring is returned. If the total number of non-empty points across all input geometries is one, we return a linestring with two points, both of which are equal to the unique non-empty point in the input. |  |
| Example(s) | *\-- each point from the original linestring*select st\_astext(st\_makeline(array( st\_geomfromtext('POINT(-100.25 50.5)'), st\_geomfromtext('POINT(-100.50 50.40)'), st\_geomfromtext('POINT(-100.25 50.35)'), st\_geomfromtext('POINT(-100.30 50.05)')))) as line  ![][image33] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_makepolygon` | GEOMETRY\[, ARRAY{GEOMETRY}\] | GEOMETRY |
| Description | Takes as input a linestring geometry and an optional array of linestring geometries. It uses these linestrings (which are expected to be closed) to construct a polygon. The outer ring of the polygon is formed from the linestring that is passed as the first argument, whereas any inner rings of the polygon are formed from the linestrings in the second argument. |  |
| Notes | Conditions on the input values: If any of the input geometries is not linestring, an error is returned. If the input geometries do not have the same SRID value an error is returned. If the outer boundary is an empty linestring, the array of inner boundaries is expected to be an empty array. Otherwise, an error is returned. Properties of the output polygon: The SRID value of the output polygon is the common SRID value of the input geometries. The dimension of the returned polygon is the maximum common dimension of the input linestrings. Any NULL values in the inner boundaries array are ignored. |  |
| Example(s) | *\-- add ', \-100.25 50.5' to the original linestring**\-- to close it as a valid polygon*select st\_astext(st\_makepolygon(st\_geomfromtext('LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05, \-100.25 50.5)'))) as poly  ![][image34] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_multi` | GEOMETRY / GEOGRAPHY | GEOMETRY / GEOGRAPHY |
| Description | Returns the input GEOGRAPHY or GEOMETRY value as an equivalent multi geospatial value, keeping the original SRID. |  |
| Notes |  |  |
| Example(s) | `SELECT st_asewkt(st_multi(st_geomfromtext('POINT Z (1 2 100)', 4326)));` SRID=4326;MULTIPOINT Z (1 2 100\) `SELECT st_asewkt(st_multi(st_geomfromtext('MULTIPOINT Z (1 2 100)', 3857)));` SRID=3857;MULTIPOINT Z (1 2 100\) `SELECT st_asewkt(st_multi(st_geogfromtext('POINT Z (1 2 100)')));` SRID=4326;MULTIPOINT Z (1 2 100\) `SELECT st_asewkt(st_multi(st_geogfromtext('MULTIPOINT Z (1 2 100)')));` SRID=4326;MULTIPOINT Z (1 2 100\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_ndims` | GEOMETRY / GEOGRAPHY / STRING / BINARY | INT |
| Description | Returns the number of dimensions (number of coordinates) of the points in a geography/geometry. |  |
| Notes | Takes as input a STRING or BINARY value representing a geography/geometry in GeoJSON, WKB, or WKT format. More specifically, if the input is a BINARY value we treat it as the WKB representation, whereas if the input is a STRING value we detect whether it is GeoJSON or WKT. |  |
| Example(s) | select st\_ndims(null) as nd\_null, st\_ndims('POINT(-77.0257 38.9005)') as nd\_pnt, st\_ndims('LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)') as nd\_line, st\_ndims('POLYGON EMPTY') as nd\_poly\_empty, st\_ndims('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))') as nd\_poly  ![][image35] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_npoints` | GEOMETRY / GEOGRAPHY / STRING / BINARY | INT |
| `st_isempty` | GEOMETRY / GEOGRAPHY / STRING / BINARY | BOOLEAN |
| Description | `ST_NPoints`: Returns the number of non-empty points in a geography/geometry. `ST_IsEmpty`: Returns true if the input geography/geometry does not contain any non-empty points (it is basically equivalent to ST\_NPoints(geo) \= 0\. |  |
| Notes | These expressions take as input a STRING or BINARY value representing a geography/geometry in GeoJSON, WKB, or WKT format. More specifically, if the input is a BINARY value we treat it as the WKB representation, whereas if the input is a STRING value we detect whether it is GeoJSON or WKT. |  |
| Example(s) | select    st\_isempty(null) as empty\_null,    st\_isempty('POINT EMPTY') as empty\_pnt,    st\_isempty('POINT(-77.0257 38.9005)') as pnt  ![][image36] |  |
|  | select    st\_npoints(null) as np\_null,    st\_npoints('POINT(-77.0257 38.9005)') as pnt,    st\_npoints('POLYGON EMPTY') as empty\_poly,    st\_npoints('POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))') as poly  ![][image37] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_perimeter` | GEOMETRY / GEOGRAPHY / STRING / BINARY | DOUBLE |
| `st_geogperimeter` | STRING / BINARY | DOUBLE |
| Description | `ST_Perimeter`: the input value is understood as a geometry. For points, linestrings, multipoints, and multilinestrings we always return 0\. For polygons we return the sum of the 2D Cartesian lengths of the segments in the polygon. For multipolygons we return the sum of the perimeters of the polygons in the multipolygon. For geometry collections we return the sum of the perimeters of the elements in the collection. The units of the result are those of the coordinates of the geometry. `ST_GeogPerimeter`: the input value is understood as a geography. We expect the input coordinates to be in degrees, and the underlying coordinate system is assumed to be WGS84. For points, linestrings, multipoints, and multilinestrings we always return 0\. For polygons we return the sum of the 2D geodesic lengths of the segments in the polygon. For multilpolygons we return the sum of the perimeters of the polygons in the multipolygon. For geometry collections we return the sum of the perimeters of the elements in the collection. The units of the result are meters. |  |
| Notes | These expressions take as input a STRING or BINARY value representing a geography/geometry in GeoJSON, WKB, or WKT format. More specifically, if the input is a BINARY value we treat it as the WKB representation, whereas if the input is a STRING value we detect whether it is GeoJSON or WKT. |  |
| Example(s) | *\-- NOTICE: difference between \`st\_geogperimeter\` \[meters\] and \`st\_perimeter\` \[units: e.g. degrees\]*select   st\_geogperimeter(g) as dbx\_perim\_meters,   st\_perimeter(g) as dbx\_perim\_unitsfrom (  select    'POLYGON((-115.427990375581 32.5700043540444, \-115.427944725767 32.5700982923276, \-115.428003926578 32.5701189200886, \-115.428049576338 32.5700249817816, \-115.427990375581 32.5700043540444))' as g  )  ![][image38] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_point` | (DOUBLE, DOUBLE\[, INT\]) | GEOMETRY |
| Description | Takes as input two double values and an optional SRID integer. It returns a two-dimensional point GEOMETRY.  |  |
| Notes | The order of double values is X (longitude), Y (latitude). The optional SRID of the point is the one provided as third argument: If  negative, SRID is set to 0\. If not provided,  SRID is set to 0\. |  |
| Example(s) | select st\_astext(st\_point(\-77.0257, 38.9005)) as pnt  ![][image39] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_pointn` | (GEOMETRY, INT) / (GEOGRAPHY, INT) | GEOMETRY / GEOGRAPHY |
| Description | Returns the 1-based indexed n-th point of the input GEOGRAPHY or GEOMETRY value, if the index is valid and the input geospatial value is a non-empty linestring. Otherwise, returns NULL. The SRID value of the output point geography or geometry is the same as that of the input value. |  |
| Notes | Takes as input a geometry or geography, and returns the Nth point in a single non-empty linestring. Negative values are counted backwards from the end of the LineString, so that \-1 is the last point. Returns NULL if there is no linestring in the geometry, or if the index is out of bounds (i.e. equal to 0 or larger than the number of points in the linestring). |  |
| Example(s) | `SELECT st_astext(st_pointn(st_geomfromtext('LINESTRING(1 2,3 4,5 6)'), 3));` POINT(5 6\) `SELECT st_astext(st_pointn(st_geogfromtext('LINESTRING(1 2,3 4,5 6)'), -3));` POINT(1 2\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_removepoint` | (GEOMETRY, n) / (GEOGRAPHY, n) | GEOMETRY / GEOGRAPHY |
| Description | Removes the n-th point from the input linestring GEOGRAPHY or GEOMETRY. |  |
| Notes | Takes as input a geospatial value and a 1-based index, and removes the Nth point from a single linestring. Negative values are counted backwards from the end of the linestring, so that \-1 is the last point. The ST\_RemovePoint expression throws an error in the following cases: STInvalidArgumentTypeError, if the input is a non-linestring geography or geometry. STInvalidArgumentError, if the input is a linestring with less than 3 points. STInvalidIndexValue, if the input is a linestring (with 3 or more points) and the index is 0\. STInvalidIndexValue, if the input is a linestring (with 3 or more points) and the absolute value of the index is greater than the number of points in the linestring. |  |
| Example(s) | `SELECT st_astext(st_removepoint(st_geomfromtext('LINESTRING(1 2,3 4,5 6)'), 2));` LINESTRING(1 2,5 6\)  `SELECT st_asewkt(st_removepoint(st_geogfromtext('LINESTRING(1 2,3 4,5 6)'), -1));` SRID=4326;LINESTRING(1 2,3 4\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_reverse` | GEOMETRY / GEOGRAPHY | GEOMETRY / GEOGRAPHY |
| Description | Reverses the order of vertices in the input GEOGRAPHY or GEOMETRY value. |  |
| Notes | Takes as input a geometry or geography, and returns the corresponding geometry or geography value with the order of its vertices reversed (explained in more detail below): If the input geospatial value is punctual (i.e. point or multipoint), it remains unchanged. If the input geospatial value is a linestring, the algorithm returns a new linestring, whose vertices are in reverse order with respect to the input linestring. If the input geospatial value is a polygon, the algorithm returns a new polygon, where for each ring of the polygon the vertices of the ring are in reverse order with respect to the input ring. If the input geospatial value is a multi-geometry (i.e. multilinestring, multipolygon, or a geometry collection), the algorithm returns a new geospatial value (multilinestring, multipolygon, or a geometry collection, respectively), whose individual elements are reversed with respect to the input elements. |  |
| Example(s) | `SELECT st_astext(st_reverse(st_geomfromtext('LINESTRING(1 2,3 4,5 6)')));` LINESTRING(5 6,3 4,1 2\)  `SELECT st_asewkt(st_reverse(st_geogfromtext('MULTIPOINT((1 2),EMPTY,(3 4),(5 6),EMPTY)')));` SRID=4326;MULTIPOINT((1 2),EMPTY,(3 4),(5 6),EMPTY) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_rotate` | (GEOMETRY, DOUBLE) / (STRING, DOUBLE) / (BINARY, DOUBLE) | GEOMETRY |
| Description | Takes as input a geometry and a rotation angle (in radians) and rotates the input geometry by the input angle around the Z axis. |  |
| Notes | Be mindful of the variations of degrees at various latitudes and longitudes. |  |
| Example(s) | *\-- Here we rotate by only 0.0000005 radians* select \*, st\_astext(st\_rotate(g, 0.0000005)) as rg from samp\_wkt  ![][image40] Using map\_render utility function in Getting Started notebook (blue is ‘g’ and tan is ‘rg’):  ![][image41] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_scale` | (GEOMETRY, DOUBLE, DOUBLE\[, DOUBLE\]) / (STRING, DOUBLE, DOUBLE\[, DOUBLE\]) / (BINARY, DOUBLE, DOUBLE\[, DOUBLE\]) | GEOMETRY |
| Description | Takes as input a geometry and scale factors in the X, Y, and Z directions. The expression returns the scaled geometry using the provided scaling factors.  |  |
| Notes | The Z scaling factor is optional. If not specified it defaults to 1. If the geometry does not have any Z coordinates, the Z scaling factor is ignored. |  |
| Example(s) | *\-- Here we scale X and Y each by 1.00003 degrees (assuming 4326).*select \*, st\_astext(st\_scale(g, 1.00003, 1.00003)) as sg from samp\_line  ![][image42] Using map\_render utility function in Getting Started notebook (blue is ‘g’ and tan is ‘sg’):  ![][image43] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_setpoint` | (GEOMETRY, INT, GEOMETRY) / (GEOGRAPHY, INT, GEOGRAPHY)   | GEOMETRY / GEOGRAPHY |
| Description | Sets the n-th point in the input linestring GEOGRAPHY or GEOMETRY. |  |
| Notes | Takes as input a geospatial value and a 1-based index, and sets the Nth point in a single linestring to a new provided point. Negative values are counted backwards from the end of the linestring, so that \-1 is the last point. The ST\_SetPoint expression throws an error in the following cases: STInvalidArgumentTypeError, if the first input geospatial value is not a linestring geography or geometry. STInvalidArgumentTypeError, if the second input geospatial value is not a point geography or geometry. STInvalidIndexValue, if the input geospatial values are a linestring and a point, but the index is 0\. STInvalidIndexValue, if the input geospatial values are a linestring and a point, but the absolute value of the index is greater than the number of points in the linestring. |  |
| Example(s) | `SELECT st_astext(st_setpoint(st_geomfromtext('LINESTRING(1 2,3 4)'), 1, st_geomfromtext('POINT(7 8)')));` LINESTRING(7 8,3 4\) `SELECT st_asewkt(st_setpoint(st_geogfromtext('LINESTRING ZM (1 2 3 4,5 6 7 8)'), -1, st_geogfromtext('POINT M (0 9 99)')));` SRID=4326;LINESTRING ZM (1 2 3 4,0 9 0 99\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_startpoint` |  GEOMETRY / GEOGRAPHY | GEOMETRY / GEOGRAPHY |
| Description | Returns the first point of the input GEOGRAPHY or GEOMETRY value, if the input geospatial value is a non-empty linestring. Otherwise, returns NULL. The SRID value of the output point geography or geometry is the same as that of the input value. |  |
| Notes |  |  |
| Example(s) | `SELECT st_asewkt(st_startpoint(st_geomfromtext('LINESTRING(1 2,3 4,5 6)', 4326)));` SRID=4326;POINT(1 2\) `SELECT st_asewkt(st_startpoint(st_geogfromtext('LINESTRING ZM (1 2 3 4,5 6 7 8)')));` SRID=4326;POINT ZM (1 2 3 4\) |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_simplify` | (GEOMETRY, DOUBLE) / (STRING, DOUBLE) / (BINARY, DOUBLE) | GEOMETRY |
| Description | Takes as input a geometry value and a tolerance value, and simplifies the geometry.  |  |
| Notes | Uses the [Douglas-Peucker algorithm](https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm). Points and multipoints remain unchanged. The expression drops the M coordinates from the input geometry if any. If the input is a STRING value, it is expected to be the GeoJSON or WKT description of a geometry. If the input is GeoJSON, the SRID of the resulting geometry is 4326\. If the input is WKT, the SRID of the resulting geometry is 0\.  If the input is WKB, the SRID of the resulting geometry is 0\.  If the input is a GEOMETRY value, the SRID value of the output geometry is the same as that of  the input value. |  |
| Example(s) | *\-- tolerance set to 0.75*select st\_astext(  st\_simplify('LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)', 0.75)) as g\_simp  ![][image44] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_setsrid` | (GEOMETRY, INT) | GEOMETRY |
| `st_srid` | GEOMETRY / GEOGRAPHY | INT |
| `st_transform` | (GEOMETRY, INT)/(STRING, INT)/(BINARY, INT) | GEOMETRY |
| Description | [SRID](https://en.wikipedia.org/wiki/Spatial_reference_system) (Spatial Reference System Identifier) is initially supported in the preview. |  |
| Notes | GEOGRAPHY data type only supports SRID=4326 H3 assumes SRID=4326 (WGS84 ellipsoid) `ST_Transform` is the main function to reproject from one SRID to another For best interoperability among various spatial data, we recommend standardizing to SRID=4326, then only specially transforming as needed for various analysis or generated data products. For v1 of the preview, except for `ST_Transform` no other function utilizes the information provided by the SRID.  |  |
| Example(s) | *\-- when srid is not set*select st\_srid(st\_geomfromtext(g)) as srid, \* from samp\_wkt  ![][image45] |  |
|  | *\-- when srid is set / inferred**\-- Note: GeoJSON spec requires SRID=4326 \[WGS84\]*with samp\_geojson as (  select \* except (g), st\_asgeojson(st\_geomfromtext(g)) as g from samp\_wkt) select st\_srid(st\_geogfromgeojson(g)) as srid, \* from samp\_geojson  ![][image46] |  |
|  | *\-- let's set srid to 4326**\-- we will first coerce wkt to ewkb and specify SRID*with samp\_ewkb as (  select \* except (g), st\_asewkb(st\_geomfromtext(g, 4326)) as g from samp\_wkt) select st\_srid(st\_geomfromewkb(g)) as srid, \* from samp\_ewkb  ![][image47] |  |
|  | *\-- lets transform from 4326 \[WGS84\] to 3857 \[Web Mercator Projection\]**\-- using the same ewkb from above*with samp\_ewkb as (  select \* except (g), st\_asewkb(st\_geomfromtext(g, 4326)) as g from samp\_wkt) select st\_astext(st\_transform(st\_geomfromewkb(g), 3857)) as g\_3857, st\_astext(st\_geomfromewkb(g)) as g, \* except(g) from samp\_ewkb  ![][image48] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_translate` | (GEOMETRY, DOUBLE, DOUBLE\[, DOUBLE\]) / (STRING, DOUBLE, DOUBLE\[, DOUBLE\]) / (BINARY, DOUBLE, DOUBLE\[, DOUBLE\]) | GEOMETRY |
| Description | Takes as input a geometry and offsets in the X, Y, and Z directions. The expression returns the translated geometry using the provided offsets.   |  |
| Notes | The Z offset is optional. If not specified it defaults to 0. If the geometry does not have any Z coordinates, the Z offset is ignored. |  |
| Example(s) | *\-- Here we translate X (longitude) by 0.00005 degrees (assuming 4326\)*select \*, st\_astext(st\_translate(g, 0.00005, 0.0)) as tg from samp\_wkt  ![][image49] Using map\_render utility function in Getting Started notebook (blue is ‘g’ and tan is ‘sg’):  ![][image50] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_union_agg` | GEOMETRY / STRING / BINARY (aggregate) | GEOMETRY |
| Description | An aggregate expression that computes the point-wise union of all the geometries in the column as a two-dimensional geometry. It takes as input a STRING, GEOMETRY, or BINARY column. In the case of STRING column, we expected the geometries to be in WKT or GeoJSON format. |  |
| Notes | If the input column's type is GEOMETRY, the expression returns an error if we encounter two geometries with different SRID values. Otherwise, the SRID value of the result is equal to the common SRID value of the geometries in the input column.  If the input column's type is STRING or BINARY, the SRID value of the result will be 0, unless all input rows correspond to the same SRID value (0 for WKB and WKT, and 4326 for GeoJSON), in which case that SRID value is used for the resulting geometry. |  |
| Example(s) | *\-- example unioning h3 chips back together**\-- note: this will not be the entire state polygon due to**\-- previously performing INNER join in pickup\_state\_h3*select state\_row\_id, state, st\_astext(st\_union\_agg(chip)) as chip\_union from (select distinct state\_row\_id, state, chip from pickup\_state\_h3) group by state\_row\_id, state order by state\_row\_id  ![][image51] Using map\_render utility function in Getting Started notebook:  ![][image52] |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `st_m` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_x` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_xmax` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_xmin` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_y` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_ymax` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_ymin` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_z` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_zmax` | GEOMETRY / STRING / BINARY | DOUBLE |
| `st_zmin` | GEOMETRY / STRING / BINARY | DOUBLE |
| Description | `ST_X`, `ST_Y`, `ST_Z` and `ST_M` take as input a point geometry, and, in the case of non-empty points, return the X, Y, Z, and M coordinate of the point, respectively. `ST_XMin`, `ST_XMax`, `ST_YMin`, and `ST_YMax,` `ST_ZMax` and `ST_ZMin` take as input a geometry value.  In addition to accepting GEOMETRY values as input, these expressions accept STRING and BINARY values as inputs, which are expected to represent a geometry object in one of the standard geospatial formats: GeoJSON (STRING), WKB (BINARY), or WKT (STRING).  |  |
| Notes | `ST_X`, `ST_Y`, `ST_Z` and `ST_M`: For empty points, the expressions return NULL. If the input geometry is not a point an error (ST\_INVALID\_ARGUMENT\_TYPE) is thrown. `ST_XMin`, `ST_XMax`, `ST_YMin`, and `ST_YMax`  If the input geometry is empty (contains no non-empty points), the expressions return NULL. If the input geometry is not empty (contains at least one non-empty point), the expressions return the minimum X, maximum X, minimum Y, and maximum Y coordinate of the points in the geometry. `ST_ZMax` and `ST_ZMin`: Z coordinates do not exist for 2D or 3DM geometries, as well as for empty geometries. In these cases the expressions return NULL. |  |
| Example(s) | *\-- start from WKT point data, return x and y*with samp\_pnt as (  select 'POINT(-75.5 40.5)' as pnt)select st\_x(pnt) as lon, st\_y(pnt) as lat from samp\_pnt  ![][image53] |  |
|  | *with samp\_line1 as (  select 'LINESTRING(-100.25 50.5,-100.50 50.40,-100.25 50.35,-100.30 50.05)' as g)select   st\_xmin(g) as xmin, st\_ymin(g) as ymin,    st\_xmax(g) as xmax, st\_ymax(g) as ymaxfrom samp\_line1*  ![][image54] `SELECT st_z('POINT Z (1 2 3)');` 3.0 `SELECT st_z('POINT M (2 3 4)');` NULL `SELECT st_z('POINT ZM EMPTY');` NULL `SELECT st_z('POINT ZM (1 2 3 4)');` 3.0 `SELECT st_m('POINT M (2 3 4)');` 4.0 `SELECT st_m('POINT Z (1 2 3)');` NULL `SELECT st_m('POINT ZM EMPTY');` NULL `SELECT st_m('POINT ZM (1 2 3 4)');` 4.0 |  |

|  | Input type(s) | Output type |
| ----- | ----- | ----- |
| `to_geography` | STRING / BINARY | GEOGRAPHY |
| `to_geometry` | STRING / BINARY | GEOMETRY |
| `try_to_geography` | STRING / BINARY | GEOGRAPHY |
| `try_to_geometry` | STRING / BINARY | GEOMETRY |
| Description | `To_Geography`: Takes as input the GeoJSON, WKB, or WKT description of a geography and returns the corresponding GEOGRAPHY value.  `To_Geometry`: Takes as input the GeoJSON, WKB, or WKT description of a geometry and returns the corresponding GEOMETRY value.  `Try_To_Geography`: Takes as input the GeoJSON, WKB, or WKT description of a geography and returns the corresponding GEOGRAPHY value. `Try_To_Geometry`: Takes as input the GeoJSON, WKB, or WKT description of a geometry and returns the corresponding GEOMETRY value. |  |
| Notes | `To_Geography` and `To_Geometry` expressions return an error in case the input is invalid. `Try_To_Geography` and `Try_To_Geometry` expressions return NULL in case the input is invalid. |  |
| Example(s) | \<no example provided\> |  |

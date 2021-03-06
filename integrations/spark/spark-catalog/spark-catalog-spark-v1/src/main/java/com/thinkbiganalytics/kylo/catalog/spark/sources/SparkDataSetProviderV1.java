package com.thinkbiganalytics.kylo.catalog.spark.sources;

/*-
 * #%L
 * Kylo Catalog for Spark 1
 * %%
 * Copyright (C) 2017 - 2018 ThinkBig Analytics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.thinkbiganalytics.kylo.catalog.api.KyloCatalogClient;
import com.thinkbiganalytics.kylo.catalog.spark.KyloCatalogClientV1;
import com.thinkbiganalytics.kylo.catalog.spi.DataSetOptions;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.sources.HadoopFsRelationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import scala.collection.Seq;

/**
 * A data set provider that can read from and write to Spark 1 data sources.
 */
public class SparkDataSetProviderV1 extends AbstractSparkDataSetProvider<DataFrame> {

    @Nonnull
    @Override
    protected DataFrameReader getDataFrameReader(@Nonnull final KyloCatalogClient<DataFrame> client, @Nonnull final DataSetOptions options) {
        return ((KyloCatalogClientV1) client).getSQLContext().read();
    }

    @Nonnull
    @Override
    protected DataFrameWriter getDataFrameWriter(@Nonnull final DataFrame dataSet, @Nonnull final DataSetOptions options) {
        return dataSet.write();
    }

    @Nonnull
    @Override
    protected Configuration getHadoopConfiguration(@Nonnull final KyloCatalogClient<DataFrame> client) {
        return ((KyloCatalogClientV1) client).getSQLContext().sparkContext().hadoopConfiguration();
    }

    @Override
    protected boolean isFileFormat(@Nonnull final Class<?> formatClass) {
        return HadoopFsRelationProvider.class.isAssignableFrom(formatClass);
    }

    @Nonnull
    @Override
    protected DataFrame load(@Nonnull final DataFrameReader reader, @Nullable final Seq<String> paths) {
        return (paths != null) ? reader.load(paths) : reader.load();
    }
}

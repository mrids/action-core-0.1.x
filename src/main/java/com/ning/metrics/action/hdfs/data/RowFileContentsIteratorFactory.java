/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.action.hdfs.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ning.metrics.action.hdfs.data.parser.RowParser;
import com.ning.metrics.action.schema.Registrar;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;

import java.io.IOException;
import java.util.Iterator;

@Singleton
public class RowFileContentsIteratorFactory
{
    private final RowParser rowParser;
    private final Registrar registrar;

    @Inject
    public RowFileContentsIteratorFactory(final RowParser rowParser, final Registrar registrar)
    {
        this.rowParser = rowParser;
        this.registrar = registrar;
    }

    public Iterator<Row> build(final FileSystem fs, final Path path, final boolean raw) throws IOException
    {
        try {
            return new RowSequenceFileContentsIterator(
                path.toUri().getPath(),
                rowParser,
                registrar,
                new SequenceFile.Reader(fs, path, fs.getConf()),
                raw);
        }
        catch (IOException e) {
            // Not a Sequence file?
            final FSDataInputStream input = fs.open(path);

            return new RowTextFileContentsIterator(
                path.toUri().getPath(),
                rowParser,
                registrar,
                input,
                raw);
        }
    }
}

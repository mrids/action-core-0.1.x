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

package com.ning.metrics.action.hdfs.data.parser;

import com.ning.metrics.action.hdfs.data.JsonNodeComparable;
import com.ning.metrics.action.hdfs.data.RowAccessException;
import com.ning.metrics.action.hdfs.data.RowFactory;
import com.ning.metrics.action.hdfs.data.Rows;
import com.ning.metrics.action.hdfs.data.schema.ColumnKey;
import com.ning.metrics.action.hdfs.data.schema.DynamicColumnKey;
import com.ning.metrics.action.hdfs.data.schema.RowSchema;
import com.ning.metrics.action.schema.Registrar;
import com.ning.metrics.goodwill.access.GoodwillSchemaField;
import com.ning.metrics.serialization.event.SmileEnvelopeEvent;
import com.ning.metrics.serialization.smile.SmileEnvelopeEventDeserializer;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Given a file in raw Smile format, extract SmileEnvelopeEvents
 */
public class SmileRowSerializer implements RowSerializer
{
    @Override
    public boolean accept(final Object o)
    {
        return (o instanceof InputStream);
    }

    @Override
    public Rows toRows(final Registrar r, final Object value) throws RowAccessException
    {
        final SmileEnvelopeEventDeserializer deserializer;
        try {
            deserializer = new SmileEnvelopeEventDeserializer((InputStream) value, false);
        }
        catch (IOException e) {
            throw new RowAccessException(e);
        }

        final Rows rows = new Rows();
        while (deserializer.hasNextEvent()) {
            final SmileEnvelopeEvent event;
            try {
                event = deserializer.getNextEvent();
            }
            catch (IOException e) {
                throw new RowAccessException(e);
            }
            final JsonNode node = (JsonNode) event.getData();

            final List<JsonNodeComparable> data = new ArrayList<JsonNodeComparable>(node.size());
            final List<ColumnKey> columnKeyList = new ArrayList<ColumnKey>(node.size());

            final Map<Short, GoodwillSchemaField> schema = r.getSchema(event.getName());

            // TODO Fragile - need field IDs
            int i = 0;
            final Iterator it = node.getElements();
            while (it.hasNext()) {
                i++;
                final JsonNode nodeField = (JsonNode) it.next();

                GoodwillSchemaField schemaField = null;
                if (schema != null) {
                    schemaField = schema.get(i);
                }

                if (schemaField == null) {
                    columnKeyList.add(new DynamicColumnKey(String.format("%d", i)));
                }
                else {
                    columnKeyList.add(new DynamicColumnKey(schemaField.getName()));

                }

                data.add(new JsonNodeComparable(nodeField));
            }

            rows.add(RowFactory.getRow(new RowSchema(event.getName(), columnKeyList), data));
        }

        return rows;
    }
}

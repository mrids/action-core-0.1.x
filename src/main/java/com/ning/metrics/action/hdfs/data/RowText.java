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

import com.ning.metrics.action.hdfs.data.schema.RowSchema;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RowText extends Row<String, Serializable>
{
    /**
     * Create a row with data
     *
     * @param schema RowSchema of this row
     * @param data   row data
     */
    public RowText(RowSchema schema, List<String> data)
    {
        this.schema = schema;
        this.data = data;
    }

    public RowText(RowSchema schema, String data)
    {
        this.schema = schema;
        this.data = new ArrayList<String>();
        this.data.add(data);
    }

    /**
     * Serialize the row into the DataOutput
     *
     * @param out DataOutput to write
     * @throws java.io.IOException generic serialization error
     */
    @Override
    public void write(DataOutput out) throws IOException
    {
        schema.write(out);
        WritableUtils.writeVInt(out, data.size());

        for (String dataItem : data) {
            byte[] bytes = dataItem.getBytes();

            out.writeInt(bytes.length);
            out.write(bytes);
        }
    }

    /**
     * Replace the current row content with a specified DataInput
     *
     * @param in DataInput to read
     * @throws java.io.IOException generic serialization error
     */
    @Override
    public void readFields(DataInput in) throws IOException
    {
        schema.readFields(in);
        int size = WritableUtils.readVInt(in);

        data = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            int length = in.readInt();
            byte[] bytes = new byte[length];

            in.readFully(bytes);
            data.add(new String(bytes));
        }
    }

    /**
     * Get a Jackson-friendly representation of an item
     *
     * @param item data item to represent
     * @return json representation
     */
    @Override
    protected Object getJsonValue(String item)
    {
        return item;
    }
}

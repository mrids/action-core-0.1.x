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

package com.ning.metrics.action.hdfs.reader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ning.metrics.action.hdfs.data.RowFileContentsIteratorFactory;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import java.io.IOException;
import java.util.List;

/**
 * Container for list of {@link HdfsEntry}.
 *
 * @see HdfsEntry
 */
public class HdfsListing
{
    private final Path path;
    private final String parentPath;
    private final ImmutableList<HdfsEntry> entries;
    private final boolean recursive;
    private final boolean raw;
    private final RowFileContentsIteratorFactory rowFileContentsIteratorFactory;

    public static final String JSON_LISTING_PATH = "path";
    public static final String JSON_LISTING_PARENT_PATH = "parentPath";
    public static final String JSON_LISTING_ENTRIES = "entries";

    @JsonCreator
    @SuppressWarnings("unused")
    public HdfsListing(
        @JsonProperty(JSON_LISTING_PATH) String path,
        @JsonProperty(JSON_LISTING_PARENT_PATH) String parentPath,
        @JsonProperty(JSON_LISTING_ENTRIES) List<HdfsEntry> entries
    )
    {
        this.path = new Path(path);
        this.parentPath = parentPath;
        this.entries = ImmutableList.copyOf(entries);

        raw = true;
        recursive = false;
        rowFileContentsIteratorFactory = null;
    }

    public HdfsListing(FileSystem fileSystem, Path path, boolean raw, RowFileContentsIteratorFactory rowFileContentsIteratorFactory, boolean recursive) throws IOException
    {
        this.path = path;
        this.parentPath = "/".equals(path.toUri().toString()) ? null : path.getParent().toUri().toString();
        this.raw = raw;
        this.recursive = recursive;
        this.rowFileContentsIteratorFactory = rowFileContentsIteratorFactory;

        final ImmutableList.Builder<HdfsEntry> entriesBuilder = ImmutableList.builder();
        findEntries(fileSystem, path, entriesBuilder);
        this.entries = entriesBuilder.build();
    }

    private void findEntries(FileSystem fs, Path p, ImmutableList.Builder<HdfsEntry> entriesBuilder) throws IOException
    {
        for (final FileStatus s : fs.listStatus(p)) {
            if (s.isDir() && recursive) {
                findEntries(fs, s.getPath(), entriesBuilder);
            }

            entriesBuilder.add(new HdfsEntry(fs, s, raw, rowFileContentsIteratorFactory));
        }
    }

    /**
     * Returns the path to this listing.
     *
     * @return path of this listing
     */
    public String getPath()
    {
        return path.toUri().getPath();
    }

    /**
     * Returns the directory containing this listing.
     *
     * @return parent path, or null if this is the root
     */
    public String getParentPath()
    {
        return parentPath;
    }

    /**
     * Returns a list of the child files and folders of this listing.
     *
     * @return list of child entries
     */
    public ImmutableList<HdfsEntry> getEntries()
    {
        return entries;
    }

    @JsonValue
    @SuppressWarnings({"unchecked", "unused"})
    public ImmutableMap toMap()
    {
        final String parentPath = getParentPath() == null ? "" : getParentPath();
        return new ImmutableMap.Builder()
            .put(JSON_LISTING_PATH, getPath())
            .put(JSON_LISTING_PARENT_PATH, parentPath)
            .put(JSON_LISTING_ENTRIES, getEntries())
            .build();
    }

    @Override
    public String toString()
    {
        return "HdfsListing{" +
            "path='" + path + '\'' +
            ", parentPath='" + parentPath + '\'' +
            ", entries=" + entries +
            '}';
    }
}

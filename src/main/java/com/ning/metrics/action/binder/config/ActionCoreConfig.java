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

package com.ning.metrics.action.binder.config;

import org.skife.config.Config;

public class ActionCoreConfig
{
    @Config(value = "action.hadoop.namenode.url")
    public String getNamenodeUrl()
    {
        return "hdfs://127.0.0.1:9000";
    }

    @Config(value = "action.hadoop.ugi")
    public String getHadoopUgi()
    {
        return "hadoop,hadoop";
    }

    @Config(value = "action.hadoop.path")
    public String getPath()
    {
        return "/";
    }

    @Config(value = "action.hadoop.io.row.serializations")
    public String getRowSerializations()
    {
        return "";
    }

    @Config(value = "action.hadoop.io.serializations")
    public String getSerializations()
    {
        return "org.apache.hadoop.io.serializer.WritableSerialization";
    }

    @Config(value = "action.registrar.host")
    public String getRegistrarHost()
    {
        return "127.0.0.1";
    }


    @Config(value = "action.registrar.port")
    public int getRegistrarPort()
    {
        return 8081;
    }

    @Config(value = "action.regisrtar.file")
    public String getRegistrarStateFile()
    {
        return "/tmp/action/registrar/cache.ondisk";
    }
}

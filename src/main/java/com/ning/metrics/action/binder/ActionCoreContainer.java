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

package com.ning.metrics.action.binder;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.servlet.ServletException;

@Singleton
public class ActionCoreContainer extends GuiceContainer
{
    @Inject
    public ActionCoreContainer(final Injector injector)
    {
        super(injector);
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}

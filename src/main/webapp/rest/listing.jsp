<%@ page import="com.ning.metrics.action.hdfs.reader.HdfsEntry" %>

<%--
  ~ Copyright 2010-2011 Ning, Inc.
  ~
  ~ Ning licenses this file to you under the Apache License, version 2.0
  ~ (the "License"); you may not use this file except in compliance with the
  ~ License.  You may obtain a copy of the License at:
  ~
  ~    http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  ~ WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
  ~ License for the specific language governing permissions and limitations
  ~ under the License.
  --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>HDFS on JSP</title>
    <script type="text/javascript" src="/static/js/jquery-1.3.2.min.js"></script>
    <link rel="stylesheet" href="/static/css/global.css" type="text/css">
</head>
<body>

<div id="header">
    <div class="wrapper">
        <h1>HDFS browser</h1>
    </div>
</div>


<div id="main">
    <div id="resultsWrapper">
        <table>
            <jsp:useBean id="it"
                         type="com.ning.metrics.action.hdfs.reader.HdfsListing"
                         scope="request">
            </jsp:useBean>

            <tr>
                <th>Name</th>
                <th>Modification Time</th>
                <th>Size</th>
                <th>Replication</th>
            </tr>
            <% if (it.getParentPath() != null) { %>
            <tr>
                <td><a href="?path=<%= it.getParentPath() %>&amp;type=json">..</a></td>
                <td colspan="3"></td>
            </tr>
            <% } %>
            <tr>
                <td><a href="/rest/1.0/json?path=<%= it.getPath() %>" target="_blank">JSON listing</a> (<a
                        href="/rest/1.0/json?path=<%= it.getPath() %>&amp;pretty=true" target="_blank">pretty</a>)
                </td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td><a href="/rest/1.0/json?path=<%= it.getPath() %>&amp;recursive=true&amp;raw=false" target="_blank">Recursive JSON
                    listing</a> (<a
                        href="/rest/1.0/json?path=<%= it.getPath() %>&amp;recursive=true&amp;pretty=true&amp;raw=false"
                        target="_blank">pretty</a>)
                </td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td><a href="/rest/1.0/text?path=<%= it.getPath() %>&amp;recursive=true" target="_blank">Recursive raw
                    listing</a>
                </td>
                <td colspan="3"></td>
            </tr>

            <%
                for (int i = 0; i < it.getEntries().size(); i++) {
                    HdfsEntry e = it.getEntries().get(i);
            %>
            <tr>
                <td>
                    <a href="?path=<%= e.getPath() %><% if (!e.isDirectory()) { %>&amp;range=1-50<% } %>"><%= e.getPath() %>
                    </a>
                    <% if (!e.isDirectory()) { %>
                    <a href="?path=<%= e.getPath() %>&amp;raw=true">(raw)</a>
                    <% } %>
                </td>
                <td><%= e.getModificationDate() %>
                </td>
                <td class="size"><%= e.getPrettySize() %>
                </td>
                <td class="size"><%= e.getReplication() %>
                </td>
            </tr>
            <%
                }
            %></table>

        <div style="clear:both;"></div>
    </div>
</div>
</body>
</html>

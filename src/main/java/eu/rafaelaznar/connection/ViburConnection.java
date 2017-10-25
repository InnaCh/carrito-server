/*
 * Copyright (c) 2017 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 *
 * carrito-server: Helps you to develop easily AJAX web applications
 *               by copying and modifying this Java Server.
 *
 * Sources at https://github.com/rafaelaznar/carrito-server
 *
 * carrito-server is distributed under the MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.rafaelaznar.connection;

import eu.rafaelaznar.helper.ConnectionClassHelper;
import eu.rafaelaznar.helper.Log4j;
import java.sql.Connection;
import java.sql.SQLException;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburDBCPException;

public class ViburConnection implements ConnectionInterface {

    private ViburDBCPDataSource dataSource = null;

    @Override
    public Connection newConnection() throws Exception {
        Connection c = null;
        try {

            dataSource = new ViburDBCPDataSource();

            dataSource.setJdbcUrl(ConnectionClassHelper.getConnectionChain());
            dataSource.setUsername(ConnectionClassHelper.getDatabaseLogin());
            dataSource.setPassword(ConnectionClassHelper.getDatabasePassword());

            dataSource.setPoolInitialSize(10);
            dataSource.setPoolMaxSize(100);

            dataSource.setConnectionIdleLimitInSeconds(30);
            dataSource.setTestConnectionQuery("isValid");

            dataSource.setLogQueryExecutionLongerThanMs(500);
            dataSource.setLogStackTraceForLongQueryExecution(true);

            dataSource.start();
            c = dataSource.getConnection();

        } catch (SQLException | ViburDBCPException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        }
        return c;
    }

    @Override
    public void disposeConnection() throws Exception {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        }
    }
}
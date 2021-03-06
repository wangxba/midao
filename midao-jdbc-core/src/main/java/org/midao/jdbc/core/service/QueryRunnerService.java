/*
 * Copyright 2013 Zakhar Prykhoda
 *
 *    midao.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.midao.jdbc.core.service;

import org.midao.jdbc.core.handlers.input.InputHandler;
import org.midao.jdbc.core.handlers.input.named.AbstractNamedInputHandler;
import org.midao.jdbc.core.handlers.input.query.AbstractQueryInputHandler;
import org.midao.jdbc.core.handlers.xml.AbstractXmlInputOutputHandler;
import org.midao.jdbc.core.handlers.model.CallResults;
import org.midao.jdbc.core.handlers.model.QueryParameters;
import org.midao.jdbc.core.handlers.output.OutputHandler;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Core Service of Midao JDBC.
 * Executes all type of Queries.
 */
public interface QueryRunnerService {

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     *
     * @param sql    The SQL to execute.
     * @param params An array of query replacement parameters.  Each row in
     *               this array is one set of batch replacement values.
     * @return Array with number of rows updated per statement.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int[] batch(String sql, Object[][] params) throws SQLException;

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     *
     * @param inputHandlers Input Handler which would be executed
     * @return Array with number of rows updated per statement.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int[] batch(InputHandler[] inputHandlers) throws SQLException;

    /**
     * Executes the given SELECT SQL query and returns a result object.
     *
     * @param <T>           The type of object that the handler returns
     * @param sql           The SQL statement to execute.
     * @param outputHandler The handler used to create the result object from Query output
     * @param params        Initialize the PreparedStatement's IN parameters with this array.
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T query(String sql, OutputHandler<T> outputHandler, Object... params) throws SQLException;

    /**
     * Executes the given SELECT SQL query and returns a result object.
     *
     * @param <T>           The type of object that the handler returns
     * @param inputHandler  Input Handler which would be executed
     * @param outputHandler The handler used to create the result object from Query output
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T query(InputHandler inputHandler, OutputHandler<T> outputHandler) throws SQLException;

    /**
     * Executes the given SELECT SQL without any replacement parameters.
     *
     * @param <T>           The type of object that the handler returns
     * @param sql           The SQL statement to execute.
     * @param outputHandler The handler used to create the result object from Query output
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T query(String sql, OutputHandler<T> outputHandler) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters.
     *
     * @param sql The SQL statement to execute.
     * @return The number of rows updated.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int update(String sql) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.
     *
     * @param sql   The SQL statement to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int update(String sql, Object param) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.
     *
     * @param sql    The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?') parameters.
     * @return The number of rows updated.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int update(String sql, Object... params) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.
     *
     * @param inputHandler Input Handler which would be executed
     * @return The number of rows updated.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public int update(InputHandler inputHandler) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.
     *
     * @param <T>           The type of object that the handler returns
     * @param inputHandler  Input Handler which would be executed
     * @param outputHandler The handler used to create the result object from Query output
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T update(InputHandler inputHandler, OutputHandler<T> outputHandler) throws SQLException;

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.
     *
     * @param <T>           The type of object that the handler returns
     * @param sql           The SQL statement to execute.
     * @param outputHandler The handler used to create the result object from Query output
     * @param params        Initializes the PreparedStatement's IN (i.e. '?') parameters.
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T update(String sql, OutputHandler<T> outputHandler, Object... params) throws SQLException;

    /**
     * Executes the given CALL SQL statement.
     * Allows execution of Stored Procedures/Functions
     *
     * @param inputHandler Input Handler which would be executed
     * @return Query Output. All input parameters are updated from OUT parameters. Stored Function return is stored there
     * as well. Can be received by invoking {@link org.midao.jdbc.core.handlers.model.QueryParameters#getReturn()}
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public QueryParameters call(AbstractQueryInputHandler inputHandler) throws SQLException;

    /**
     * Executes the given CALL SQL statement.
     * Allows execution of Stored Procedures/Functions
     * <p/>
     * This function reads database metadata and identifies OUT fields. Output from OUT fields is merged with input
     * and returned as function return.
     * <p/>
     * If function fails during reading database metadata please use {@link #call(org.midao.jdbc.core.handlers.input.query.AbstractQueryInputHandler)}
     * as it is possible to specify Direction in QueryParameters class
     *
     * @param <T>          The type of object that the handler returns
     * @param inputHandler Input Handler which would be executed
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T call(AbstractNamedInputHandler<T> inputHandler) throws SQLException;

    /**
     * Executes the given CALL SQL statement.
     * Allows execution of Stored Procedures/Functions
     * <p/>
     * This function reads database metadata and identifies OUT fields. Output from OUT fields is merged with input
     * and returned as function return.
     * <p/>
     * If function fails during reading database metadata please use {@link #call(org.midao.jdbc.core.handlers.input.query.AbstractQueryInputHandler)}
     * as it is possible to specify Direction in QueryParameters class
     *
     * @param <T>          The type of object that the handler returns
     * @param inputHandler Input Handler which would be executed
     * @param catalog      Database Catalog. If null would be specified - this value won't be used in search
     * @param schema       Database Schema. If null would be specified - this value won't be used in search
     * @param useCache     specifies if it should use cache for reading Stored Procedure/Function parameters
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T call(AbstractNamedInputHandler<T> inputHandler, String catalog, String schema, boolean useCache) throws SQLException;

    /**
     * Executes the given CALL SQL statement.
     * Allows execution of Stored Procedures/Functions
     * <p/>
     * This function reads database metadata and identifies OUT fields. Output from OUT fields is merged with input
     * and returned as function return.
     * <p/>
     * If function fails during reading database metadata please use {@link #call(org.midao.jdbc.core.handlers.input.query.AbstractQueryInputHandler)}
     * as it is possible to specify Direction in QueryParameters class
     *
     * @param <T>           The type of object that the input handler clones, updates and returns
     * @param <S>           The type of object that the output handler returns
     * @param inputHandler  Input Handler which would be executed
     * @param outputHandler The handler used to create the result object from Query output
     * @return CallResult which holds updated input and processed Query output (by OutputHandler)
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T, S> CallResults<T, S> call(InputHandler<T> inputHandler, OutputHandler<S> outputHandler) throws SQLException;

    /**
     * Executes the given CALL SQL statement.
     * Allows execution of Stored Procedures/Functions
     * <p/>
     * This function reads database metadata and identifies OUT fields. Output from OUT fields is merged with input
     * and returned as function return.
     * <p/>
     * If function fails during reading database metadata please use {@link #call(org.midao.jdbc.core.handlers.input.query.AbstractQueryInputHandler)}
     * as it is possible to specify Direction in QueryParameters class
     *
     * @param <T>           The type of object that the input handler clones, updates and returns
     * @param <S>           The type of object that the output handler returns
     * @param inputHandler  Input Handler which would be executed
     * @param outputHandler The handler used to create the result object from Query output
     * @param catalog       Database Catalog. If null would be specified - this value won't be used in search
     * @param schema        Database Schema. If null would be specified - this value won't be used in search
     * @param useCache      specifies if it should use cache for reading Stored Procedure/Function parameters
     * @return CallResult which holds updated input and processed Query output (by OutputHandler)
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T, S> CallResults<T, S> call(InputHandler<T> inputHandler, OutputHandler<S> outputHandler, String catalog, String schema, boolean useCache) throws SQLException;

    /**
     * Executes SQL statement provided via {@link AbstractXmlInputOutputHandler}
     * Allows execution of query and update operations
     * <p/>
     * <p>
     * Currently, call operations are not supported via this function
     * </p>
     *
     * @param <T>          type of the output to be returned
     * @param inputHandler Input Handler which would be executed
     * @return An object generated by the handler.
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public <T> T execute(AbstractXmlInputOutputHandler<T> inputHandler) throws SQLException;

    /**
     * Adds override which would be used only once.
     * <p/>
     * Overriders provide wide variety of functionality. For whole list please look at
     * {@link org.midao.jdbc.core.MjdbcConstants} OVERRIDE_* Constants
     *
     * @param operation name of the operation
     * @param value     override value
     * @return this instance of QueryRunner
     */
    public QueryRunnerService overrideOnce(String operation, Object value);

    /**
     * Adds override which would be used until removed.
     * <p/>
     * Overriders provide wide variety of functionality. For whole list please look at
     * {@link org.midao.jdbc.core.MjdbcConstants} OVERRIDE_* Constants
     *
     * @param operation name of the operation
     * @param value     override value
     * @return this instance of QueryRunner
     */
    public QueryRunnerService override(String operation, Object value);

    /**
     * Removes override.
     * <p/>
     * Overriders provide wide variety of functionality. For whole list please look at
     * {@link org.midao.jdbc.core.MjdbcConstants} OVERRIDE_* Constants
     *
     * @param operation name of the operation
     * @return this instance of QueryRunner
     */
    public QueryRunnerService removeOverride(String operation);

    /**
     * Sets Transaction mode.
     * <p/>
     * By default system works in automatic mode. No additional actions is not required as every
     * query execution would be committed right away.
     * <p/>
     * In manual mode every query execution is performed in current transaction. In order to commit changes
     * execution of {@link #commit()} is required.
     *
     * @param manualMode manual mode
     */
    public void setTransactionManualMode(boolean manualMode);

    /**
     * Returns current Transaction mode
     *
     * @return current Transaction mode
     */
    public boolean isTransactionManualMode();

    /**
     * Transaction Isolation level.
     * <p/>
     * For general information please look at Java Tutorials (docs.oracle.com/javase/tutorial/index.html)
     * JDBC basics / Using Transactions
     * <p/>
     * For detailed information please look at vendor JDBC driver description
     *
     * @param level Transaction Isolation level
     */
    public void setTransactionIsolationLevel(Integer level);

    /**
     * Returns current Transaction Isolation level
     *
     * @return current Transaction Isolation level
     */
    public Integer getTransactionIsolationLevel();

    /**
     * Commits current Transaction
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public void commit() throws SQLException;

    /**
     * Rollbacks current Transaction
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public void rollback() throws SQLException;

    /**
     * Creates an unnamed savepoint in the current transaction and returns the new Savepoint object that represents it.
     * {@link java.sql.Connection#setSavepoint()}
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public Savepoint setSavepoint() throws SQLException;

    /**
     * Creates a savepoint with the given name in the current transaction and returns the new Savepoint object that represents it.
     * {@link java.sql.Connection#setSavepoint(String)}
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public Savepoint setSavepoint(String name) throws SQLException;

    /**
     * Undoes all changes made after the given Savepoint object was set.
     * {@link java.sql.Connection#rollback(java.sql.Savepoint)}
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public void rollback(Savepoint savepoint) throws SQLException;

    /**
     * Removes the specified Savepoint and subsequent Savepoint objects from the current transaction.
     * {@link java.sql.Connection#releaseSavepoint(java.sql.Savepoint)}
     * <p/>
     * Usable only when {@link #setTransactionManualMode(boolean)} set as true
     *
     * @throws SQLException if exception would be thrown by Driver/Database
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException;
}

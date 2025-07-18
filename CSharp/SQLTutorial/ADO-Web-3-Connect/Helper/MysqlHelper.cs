﻿using System.Data;
using ADO_Web_3_Connect.Data;
using MySqlConnector;

namespace ADO_Web_3_Connect.Helper;

public class MysqlHelper
{
    private static readonly string? ConnStr;

    static MysqlHelper()
    {
        ConnStr ??= AppConfigurationServices.Configuration.GetConnectionString("BunnyTest");
    }

    /// <summary>
    ///     查询数据库
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static int ExecuteNonQuery(string sql)
    {
        using var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return mySqlCommand.ExecuteNonQuery();
    }

    /// <summary>
    ///     异步查询数据库
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static async Task<int> ExecuteNoeQueryAsync(string sql)
    {
        var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return await mySqlCommand.ExecuteNonQueryAsync();
    }

    /// <summary>
    ///     读取数据库流
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static MySqlDataReader ExecuteReader(string sql)
    {
        var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return mySqlCommand.ExecuteReader(CommandBehavior.CloseConnection);
    }

    /// <summary>
    ///     异步读取数据库流
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static async Task<MySqlDataReader> ExecuteReaderAsync(string sql)
    {
        var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return await mySqlCommand.ExecuteReaderAsync();
    }

    /// <summary>
    ///     获取首行首列的值
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static object? ExecuteScalar(string sql)
    {
        using var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return mySqlCommand.ExecuteScalar();
    }

    /// <summary>
    ///     异步获取首行首列
    /// </summary>
    /// <param name="sql">SQL语句</param>
    /// <returns></returns>
    public static async Task<object?> ExecuteScalarAsync(string sql)
    {
        await using var mySqlConnection = new MySqlConnection(ConnStr);
        mySqlConnection.Open();
        var mySqlCommand = new MySqlCommand(sql, mySqlConnection);
        return await mySqlCommand.ExecuteScalarAsync();
    }
}
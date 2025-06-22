using MySqlConnector;

namespace ADO_1_Start;

public class Program
{
    private static readonly string Host = "rm-bp12z6hlv46vi6g8mro.mysql.rds.aliyuncs.com";
    private static readonly string Database = "bunny_test";
    private static readonly uint Port = 3306;
    private static readonly string Username = "root";
    private static readonly string Password = "0212zkw!";

    public static void Main(string[] args)
    {
        var mySqlConnectionStringBuilder = new MySqlConnectionStringBuilder
        {
            Server = Host, Port = Port,
            Database = Database,
            UserID = Username, Password = Password
        };
        Console.WriteLine(mySqlConnectionStringBuilder.ConnectionString);
        using (var mySqlConnection = new MySqlConnection(mySqlConnectionStringBuilder.ConnectionString))
        {
            mySqlConnection.Open();

            var mySqlCommand = new MySqlCommand("select * from sys_user");
            mySqlCommand.Connection = mySqlConnection;

            var mySqlDataReader = mySqlCommand.ExecuteReader();
            mySqlDataReader.Read();

            for (var i = 0; i < mySqlDataReader.FieldCount; i++)
                Console.Write(mySqlDataReader.GetName(i) + (i < mySqlDataReader.FieldCount - 1 ? "\t" : "\n"));
        }
    }
}
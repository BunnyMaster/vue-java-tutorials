using ADO_Web_3_Connect.Data;
using ADO_Web_3_Connect.Helper;
using ADO_Web_3_Connect.Model;
using Microsoft.AspNetCore.Mvc;

namespace ADO_Web_3_Connect.Controller;

[Route("/api/[controller]")]
public class NewsTypeController : ControllerBase
{
    [HttpGet("Index")]
    public IActionResult Index()
    {
        var connectionString = AppConfigurationServices.Configuration.GetConnectionString("BunnyTest");
        return new OkObjectResult(connectionString);
    }

    [HttpGet]
    public async Task<ActionResult<List<NewType>>> GetAll()
    {
        const string sql = "SELECT * FROM news_type";
        var readerAsync = await MysqlHelper.ExecuteReaderAsync(sql);

        var newTypes = new List<NewType>();
        if (!readerAsync.HasRows) return newTypes;

        while (readerAsync.Read())
            newTypes.Add(new NewType
            {
                Id = readerAsync.GetInt32(0), NewsTypeTitle = readerAsync.GetString(1),
                IsEnabled = readerAsync.GetBoolean(2)
            });

        readerAsync.Close();
        return newTypes;
    }

    [HttpGet("GetById/{id:int}")]
    public async Task<ActionResult<NewType>> GetById(int id)
    {
        var sql = $"SELECT * FROM news_type WHERE id={id}";
        var mySqlDataReader = await MysqlHelper.ExecuteReaderAsync(sql);

        // 是否存在数据
        if (!mySqlDataReader.HasRows) return new NotFoundResult();

        var newType = new NewType();
        while (mySqlDataReader.Read())
            newType = new NewType
            {
                Id = mySqlDataReader.GetInt32(0),
                NewsTypeTitle = mySqlDataReader.GetString(1),
                IsEnabled = mySqlDataReader.GetBoolean(2)
            };

        mySqlDataReader.Close();
        return newType;
    }

    [HttpPost]
    public async Task<int> Insert(NewType? newType)
    {
        if (newType == null) return 0;

        var sql =
            $"INSERT INTO news_type ( `id`, `news_type_title`, `is_enable` ) VALUES  ( {newType.Id}, '{newType.NewsTypeTitle}', {newType.IsEnabled} )";
        return await MysqlHelper.ExecuteNoeQueryAsync(sql);
    }

    [HttpDelete]
    public async Task<int> DeleteById(int id)
    {
        var sql = $"DELETE FROM news_type WHERE id={id}";
        return await MysqlHelper.ExecuteNoeQueryAsync(sql);
    }
}
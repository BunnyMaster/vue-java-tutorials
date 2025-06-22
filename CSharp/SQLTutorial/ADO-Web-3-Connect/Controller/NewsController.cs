using ADO_Web_3_Connect.Helper;
using ADO_Web_3_Connect.Model;
using Microsoft.AspNetCore.Mvc;

namespace ADO_Web_3_Connect.Controller;

[Route("api/[controller]")]
public class NewsController : ControllerBase
{
    [HttpGet]
    public async Task<List<News>> GetNews()
    {
        const string sql = "select * from news";
        var mySqlDataReader = await MysqlHelper.ExecuteReaderAsync(sql);
        var news = new List<News>();

        while (mySqlDataReader.Read())
            news.Add(new News
            {
                Id = mySqlDataReader.GetInt32(0),
                Title = mySqlDataReader.GetString(1),
                Content = mySqlDataReader.GetString(2),
                NewsTypeId = mySqlDataReader.GetInt32(3)
            });
        return news;
    }

    [HttpGet("{id:int}")]
    public async Task<News> GetNewsById(int id)
    {
        var sql =
            $"SELECT  n.id,  n.news_title,  n.content,  n.new_type AS news_type_id,  nt.news_type_title FROM  news n  LEFT JOIN news_type nt ON nt.id = n.new_type WHERE  n.id = {id}";
        var readerAsync = await MysqlHelper.ExecuteReaderAsync(sql);

        var news = new News();
        while (readerAsync.Read())
            news = new News
            {
                Id = readerAsync.GetInt32(0),
                Title = readerAsync.GetString(1),
                Content = readerAsync.GetString(2),
                NewsTypeId = readerAsync.GetInt32(3),
                NewsTypeTitle = readerAsync.GetString(4)
            };

        readerAsync.Close();
        return news;
    }

    [HttpGet("Query")]
    public async Task<List<News>> Query(string keyword)
    {
        var sql =
            $"SELECT  n.id,  n.news_title,  n.content,  n.new_type AS news_type_id,  nt.news_type_title FROM  news n  LEFT JOIN news_type nt ON nt.id = n.new_type WHERE  n.content LIKE '%{keyword}%'  OR n.news_title LIKE '%{keyword}%'";
        var mySqlDataReader = await MysqlHelper.ExecuteReaderAsync(sql);

        var list = new List<News>();
        while (mySqlDataReader.Read())
        {
            var news = new News
            {
                Id = mySqlDataReader.GetInt32(0),
                Title = mySqlDataReader.GetString(1),
                Content = mySqlDataReader.GetString(2),
                NewsTypeId = mySqlDataReader.GetInt32(3),
                NewsTypeTitle = mySqlDataReader.GetString(4)
            };
            list.Add(news);
        }

        mySqlDataReader.Close();
        return list;
    }

    [HttpPost]
    public async Task<int> Create(News? news)
    {
        if (news == null) return 0;

        var sql =
            $"insert into news (id, news_title, content, new_type) values ({news.Id},'{news.Title}','{news.Content}',{news.NewsTypeId})";
        return await MysqlHelper.ExecuteNoeQueryAsync(sql);
    }

    [HttpDelete("{id:int}")]
    public async Task<ActionResult<int>> Delete(int id)
    {
        var executeNoeQueryAsync = await MysqlHelper.ExecuteNoeQueryAsync($"delete from news where id={id}");
        return executeNoeQueryAsync;
    }
}
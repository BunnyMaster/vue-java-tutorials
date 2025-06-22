using System.ComponentModel.DataAnnotations.Schema;

namespace ADO_Web_3_Connect.Model;

public class News
{
    /// <summary>
    ///     主键
    /// </summary>
    [Column("id")]
    public int Id { get; set; }

    /// <summary>
    ///     新闻标题
    /// </summary>
    [Column("news_title")]
    public string? Title { get; set; }

    /// <summary>
    ///     新闻内容
    /// </summary>
    [Column("news_content")]
    public string? Content { get; set; }

    /// <summary>
    ///     新闻类型id
    /// </summary>
    [Column("news_type")]
    public int? NewsTypeId { get; set; }

    [NotMapped] public string? NewsTypeTitle { get; set; }
}
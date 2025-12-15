using System.ComponentModel.DataAnnotations.Schema;

namespace ADO_Web_2_Connect.Model;

public class NewType
{
    /// <summary>
    ///     新闻分类Id
    /// </summary>
    [Column("id")]
    public int Id { get; set; }

    /// <summary>
    ///     新闻分类标题
    /// </summary>
    [Column("news_type_title")]
    public string? NewsTypeTitle { get; set; }

    /// <summary>
    ///     是否启用
    /// </summary>
    [Column("is_enable")]
    public bool? IsEnabled { get; set; }
}
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace EFCore_1_Start.Model;

/// <summary>
///     产品分类
/// </summary>
[Table("p_category")]
public class PCategory
{
    /// <summary>
    ///     主键
    /// </summary>
    [Key]
    public Guid? CId { get; set; } = Guid.NewGuid();

    /// <summary>
    ///     分类标题
    /// </summary>
    [MaxLength(100)]
    public string? CTitle { get; set; }
}
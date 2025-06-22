using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace EFCore_1_Start.Model;

/// <summary>
///     产品
/// </summary>
[Table("product")]
public class Product
{
    /// <summary>
    ///     产品Id
    /// </summary>
    [Key]
    public Guid? PId { get; set; } = Guid.NewGuid();

    /// <summary>
    ///     产品名称
    /// </summary>
    [MaxLength(200)]
    public string? PTitle { get; set; }

    /// <summary>
    ///     产品数量
    /// </summary>
    public int PSum { get; set; }

    /// <summary>
    ///     产品价格
    /// </summary>
    [Column(TypeName = "decimal(18,2)")]
    public decimal PPrice { get; set; }

    /// <summary>
    ///     产品分类Id
    /// </summary>
    public Guid PCategoryId { get; set; }

    /// <summary>
    ///     产品分类信息
    /// </summary>
    [ForeignKey("PCategoryId")]
    public virtual PCategory? PCategory { get; set; }
}
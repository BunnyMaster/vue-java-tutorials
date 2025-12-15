using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace EFCore_2_JWT.Model;

/// <summary>
///     用户模型对象
/// </summary>
[Table("users")]
public class User
{
    /// <summary>
    ///     /主键
    /// </summary>
    [Key]
    public Guid? Id { get; set; } = Guid.NewGuid();

    /// <summary>
    ///     用户名
    /// </summary>
    [Column("username", TypeName = "varchar(100)")]
    public string? Username { get; set; }

    /// <summary>
    ///     用户密码
    /// </summary>
    [Column("password", TypeName = "varchar(100)")]
    public string? Password { get; set; }

    /// <summary>
    ///     邮箱
    /// </summary>
    [Column("email", TypeName = "varchar(100)")]
    public string? Email { get; set; }
}
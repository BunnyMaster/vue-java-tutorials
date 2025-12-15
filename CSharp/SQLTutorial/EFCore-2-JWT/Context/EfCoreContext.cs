using EFCore_2_JWT.Model;
using Microsoft.EntityFrameworkCore;

namespace EFCore_2_JWT.Context;

/// <summary>
///     EFCore 上下文
/// </summary>
public class EfCoreContext : DbContext
{
    /// <summary>
    ///     EfCoreContext 上下文
    /// </summary>
    /// <param name="options">DbContextOptions</param>
    public EfCoreContext(DbContextOptions options) : base(options)
    {
    }

    /// <summary>
    ///     数据库用户集合
    /// </summary>
    public DbSet<User>? Users { get; set; }
}
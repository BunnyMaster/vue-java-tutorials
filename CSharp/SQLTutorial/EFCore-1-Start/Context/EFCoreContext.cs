using EFCore_1_Start.Model;
using Microsoft.EntityFrameworkCore;

namespace EFCore_1_Start.Context;

/// <summary>
///     EFCore上下文对象
/// </summary>
public class EfCoreContext : DbContext
{
    /// <summary>
    ///     EFCore上下文对象
    /// </summary>
    /// <param name="options">DbContextOptions</param>
    public EfCoreContext(DbContextOptions options) : base(options)
    {
    }

    /// <summary>
    ///     产品分类信息
    /// </summary>
    public DbSet<PCategory> PCategories { get; set; }

    /// <summary>
    ///     产品信息
    /// </summary>
    public DbSet<Product> Products { get; set; }
}
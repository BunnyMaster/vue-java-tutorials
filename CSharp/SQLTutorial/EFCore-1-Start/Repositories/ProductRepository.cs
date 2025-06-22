using EFCore_1_Start.Context;
using EFCore_1_Start.Model;
using EFCore_1_Start.Repositories.IRepository;
using Microsoft.EntityFrameworkCore;

namespace EFCore_1_Start.Repositories;

/// <summary>
///     Product数据库查询
/// </summary>
/// <param name="context"></param>
public class ProductRepository(EfCoreContext context) : IProductRepository
{
    /// <summary>
    ///     创建产品
    /// </summary>
    /// <param name="product">产品</param>
    /// <returns></returns>
    public Task<int> Create(Product product)
    {
        context.Products.Add(product);
        return context.SaveChangesAsync();
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>ActionResult - Product</returns>
    public Task<List<Product>> QueryListById(Guid id)
    {
        return context.Products.AsNoTracking()
            .Include("PCategory")
            .Where(product => product.PCategoryId.Equals(id))
            .ToListAsync();
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Product</returns>
    public Task<Product?> GetById(Guid id)
    {
        return context.Products.AsNoTracking().FirstOrDefaultAsync(product => product.PId.Equals(id));
    }

    /// <summary>
    ///     根据Id删除产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Task</returns>
    public Task<int> DeleteProduct(Guid id)
    {
        var product = new Product { PId = id };
        context.Products.Remove(product);
        return context.SaveChangesAsync();
    }

    /// <summary>
    ///     根据分类Id查询产品信息
    /// </summary>
    /// <param name="cId">分类Id</param>
    /// <param name="keyword"></param>
    /// <returns>Product</returns>
    public Task<List<Product>> QueryByKeyword(Guid cId, string keyword)
    {
        return context.Products.AsNoTracking()
            .Include("PCategory")
            .Where(p => p.PCategoryId == cId
                        &&
                        p.PTitle != null
                        &&
                        p.PTitle.Contains(keyword)
            )
            .ToListAsync();
    }
}
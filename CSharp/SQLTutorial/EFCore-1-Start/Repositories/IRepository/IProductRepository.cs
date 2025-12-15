using EFCore_1_Start.Model;

namespace EFCore_1_Start.Repositories.IRepository;

/// <summary>
///     产品数据库查询
/// </summary>
public interface IProductRepository
{
    /// <summary>
    ///     创建产品
    /// </summary>
    /// <param name="product">产品表单</param>
    /// <returns>Task</returns>
    Task<int> Create(Product product);

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>ActionResult - Product</returns>
    Task<List<Product>> QueryListById(Guid id);

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Product</returns>
    Task<Product?> GetById(Guid id);

    /// <summary>
    ///     根据Id删除产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Task</returns>
    Task<int> DeleteProduct(Guid id);

    /// <summary>
    ///     根据分类Id查询产品信息
    /// </summary>
    /// <param name="cId">分类Id</param>
    /// <param name="keyword"></param>
    /// <returns>Product</returns>
    Task<List<Product>> QueryByKeyword(Guid cId, string keyword);
}
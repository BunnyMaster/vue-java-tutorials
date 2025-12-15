using EFCore_1_Start.Model;
using EFCore_1_Start.Repositories.IRepository;
using EFCore_1_Start.Service.IServices;

namespace EFCore_1_Start.Service;

/// <summary>
///     产品服务实现
/// </summary>
public class ProductService : IProductService
{
    private readonly IProductRepository _productRepository;

    /// <summary>
    ///     产品服务构造器
    /// </summary>
    /// <param name="productRepository">IProductRepository</param>
    public ProductService(IProductRepository productRepository)
    {
        _productRepository = productRepository;
    }

    /// <summary>
    ///     创建产品
    /// </summary>
    /// <param name="product">产品</param>
    /// <returns></returns>
    public Task<int> CreateProduct(Product product)
    {
        return _productRepository.Create(product);
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>ActionResult - Product</returns>
    public Task<List<Product>> QueryListById(Guid id)
    {
        return _productRepository.QueryListById(id);
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Product</returns>
    public Task<Product?> GetById(Guid id)
    {
        return _productRepository.GetById(id);
    }

    /// <summary>
    ///     根据Id删除产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Task</returns>
    public Task<int> DeleteProduct(Guid id)
    {
        return _productRepository.DeleteProduct(id);
    }

    /// <summary>
    ///     根据分类Id查询产品信息
    /// </summary>
    /// <param name="cId">分类Id</param>
    /// <param name="keyword"></param>
    /// <returns>Product</returns>
    public Task<List<Product>> QueryByKeyword(Guid cId, string keyword)
    {
        return _productRepository.QueryByKeyword(cId, keyword);
    }
}
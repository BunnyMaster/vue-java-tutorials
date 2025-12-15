using EFCore_1_Start.Model;
using EFCore_1_Start.Service.IServices;
using Microsoft.AspNetCore.Mvc;

namespace EFCore_1_Start.Controller;

/// <summary>
///     产品控制器
/// </summary>
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    private readonly IProductService _productService;

    /// <summary>
    ///     ProductController
    /// </summary>
    /// <param name="productService">IProductService</param>
    public ProductController(IProductService productService)
    {
        _productService = productService;
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>ActionResult - Product</returns>
    [HttpGet("QueryListById/{id:guid}")]
    public async Task<ActionResult<List<Product>>> QueryListById(Guid id)
    {
        return await _productService.QueryListById(id);
    }

    /// <summary>
    ///     根据分类Id查询产品信息
    /// </summary>
    /// <param name="cId">分类Id</param>
    /// <param name="keyword"></param>
    /// <returns>Product</returns>
    [HttpGet("QueryByKeyword")]
    public async Task<ActionResult<List<Product>>> QueryByKeyword(Guid cId, string keyword)
    {
        return await _productService.QueryByKeyword(cId, keyword);
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Product</returns>
    [HttpGet("{id:guid}")]
    public async Task<ActionResult<Product?>> GetById(Guid id)
    {
        return await _productService.GetById(id);
    }

    /// <summary>
    ///     创建产品
    /// </summary>
    /// <param name="product">产品</param>
    /// <returns></returns>
    [HttpPost]
    public async Task<ActionResult<int>> CreateProduct(Product? product)
    {
        if (product == null) return 0;

        return await _productService.CreateProduct(product);
    }

    /// <summary>
    ///     根据Id删除产品
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns>Task</returns>
    [HttpDelete("{id:guid}")]
    public async Task<int> DeleteProduct(Guid id)
    {
        return await _productService.DeleteProduct(id);
    }
}
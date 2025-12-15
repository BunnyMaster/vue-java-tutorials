using EFCore_1_Start.Model;
using EFCore_1_Start.Repositories.IRepository;
using EFCore_1_Start.Service.IServices;

namespace EFCore_1_Start.Service;

/// <summary>
///     产品分类服务
/// </summary>
/// <param name="productRepository">IPCategoryRepository</param>
public class PCategoryService(IPCategoryRepository productRepository) : IPCategoryService
{
    /// <summary>
    ///     创建产品分类
    /// </summary>
    /// <param name="pCategory"></param>
    /// <returns></returns>
    public Task<int> CreatePCategory(PCategory? pCategory)
    {
        return productRepository.CreatePCategory(pCategory);
    }

    /// <summary>
    ///     获取所有产品信息
    /// </summary>
    /// <returns></returns>
    public List<PCategory> GetAll()
    {
        return productRepository.GetAll();
    }

    /// <summary>
    ///     根据id查找分类
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns></returns>
    public PCategory? GetById(Guid id)
    {
        return productRepository.GetById(id);
    }

    /// <summary>
    ///     根据Id删除
    /// </summary>
    /// <param name="id">主键</param>
    /// <returns></returns>
    public Task<int> DeletePCategoryById(Guid? id)
    {
        return productRepository.DeletePCategoryById(id);
    }
}
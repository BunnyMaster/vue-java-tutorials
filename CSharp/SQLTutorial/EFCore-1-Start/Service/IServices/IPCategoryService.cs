using EFCore_1_Start.Model;

namespace EFCore_1_Start.Service.IServices;

/// <summary>
///     产品分类接口服务
/// </summary>
public interface IPCategoryService
{
    /// <summary>
    ///     创建产品分类
    /// </summary>
    /// <param name="pCategory"></param>
    /// <returns></returns>
    Task<int> CreatePCategory(PCategory? pCategory);

    /// <summary>
    ///     获取所有产品信息
    /// </summary>
    /// <returns></returns>
    List<PCategory> GetAll();

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id"></param>
    PCategory? GetById(Guid id);

    /// <summary>
    ///     根据Id删除
    /// </summary>
    /// <param name="id">主键</param>
    /// <returns></returns>
    Task<int> DeletePCategoryById(Guid? id);
}
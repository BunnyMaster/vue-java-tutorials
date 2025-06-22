using EFCore_1_Start.Model;

namespace EFCore_1_Start.Repositories.IRepository;

/// <summary>
///     产品分类数据库
/// </summary>
public interface IPCategoryRepository
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
    ///     根据id查找分类
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns></returns>
    PCategory? GetById(Guid id);

    /// <summary>
    ///     根据Id删除
    /// </summary>
    /// <param name="id">主键</param>
    /// <returns></returns>
    Task<int> DeletePCategoryById(Guid? id);
}
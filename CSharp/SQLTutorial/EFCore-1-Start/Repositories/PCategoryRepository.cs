using EFCore_1_Start.Context;
using EFCore_1_Start.Model;
using EFCore_1_Start.Repositories.IRepository;

namespace EFCore_1_Start.Repositories;

/// <summary>
///     产品分类数据库查询实现
/// </summary>
/// <param name="context">EfCoreContext</param>
public class PCategoryRepository(EfCoreContext context) : IPCategoryRepository
{
    /// <summary>
    ///     创建产品分类
    /// </summary>
    /// <param name="pCategory"></param>
    /// <returns></returns>
    public Task<int> CreatePCategory(PCategory? pCategory)
    {
        if (pCategory == null) return Task.FromResult(0);
        context.PCategories.Add(pCategory);
        var saveChangesAsync = context.SaveChangesAsync();
        return saveChangesAsync;
    }

    /// <summary>
    ///     获取所有产品信息
    /// </summary>
    /// <returns></returns>
    public List<PCategory> GetAll()
    {
        return context.PCategories.ToList();
    }

    /// <summary>
    ///     根据id查找分类
    /// </summary>
    /// <param name="id">Id</param>
    /// <returns></returns>
    public PCategory? GetById(Guid id)
    {
        return context.PCategories.Find(id);
    }

    /// <summary>
    ///     根据Id删除
    /// </summary>
    /// <param name="id">主键</param>
    /// <returns></returns>
    public Task<int> DeletePCategoryById(Guid? id)
    {
        var pCategory = new PCategory { CId = id };
        context.PCategories.Remove(pCategory);
        return context.SaveChangesAsync();
    }
}
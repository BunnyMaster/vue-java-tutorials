using EFCore_1_Start.Model;
using EFCore_1_Start.Service.IServices;
using Microsoft.AspNetCore.Mvc;

namespace EFCore_1_Start.Controller;

/// <summary>
///     PCategory控制器
/// </summary>
[Route("api/[controller]")]
public class PCategoryController : ControllerBase
{
    private readonly IPCategoryService _ipCategoryService;

    /// <summary>
    ///     PCategory服务
    /// </summary>
    /// <param name="ipCategoryService">IPCategoryService</param>
    public PCategoryController(IPCategoryService ipCategoryService)
    {
        _ipCategoryService = ipCategoryService;
    }

    /// <summary>
    ///     获取所有产品信息
    /// </summary>
    /// <returns></returns>
    [HttpGet]
    public ActionResult<List<PCategory>> GetAll()
    {
        return _ipCategoryService.GetAll();
    }

    /// <summary>
    ///     根据id查询产品
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
    [HttpGet("{id:guid}")]
    public ActionResult<PCategory?> GetById(Guid id)
    {
        var pCategory = _ipCategoryService.GetById(id);
        return pCategory;
    }

    /// <summary>
    ///     创建产品分类
    /// </summary>
    /// <param name="pCategory"></param>
    /// <returns></returns>
    [HttpPost]
    public async Task<int> CreatePCategory(PCategory? pCategory)
    {
        if (pCategory == null) return 0;

        pCategory.CId = Guid.NewGuid();
        return await _ipCategoryService.CreatePCategory(pCategory);
    }

    /// <summary>
    ///     根据Id删除
    /// </summary>
    /// <param name="id">主键</param>
    /// <returns></returns>
    [HttpDelete("{id:guid}")]
    public async Task<int> DeletePCategoryById(Guid? id)
    {
        if (id == null) return 0;

        return await _ipCategoryService.DeletePCategoryById(id);
    }
}
using EFCore_2_JWT.Model;
using EFCore_2_JWT.Service.IService;
using Microsoft.AspNetCore.Mvc;

namespace EFCore_2_JWT.Controller;

/// <summary>
///     用户控制器
/// </summary>
[Route("/api/[controller]/[action]")]
[ApiController]
public class UserController : ControllerBase
{
    private readonly IUserService _userService;

    /// <summary>
    ///     UserController构造器
    /// </summary>
    /// <param name="userService"></param>
    public UserController(IUserService userService)
    {
        _userService = userService;
    }

    /// <summary>
    ///     创建用户令牌
    /// </summary>
    /// <param name="username">用户名</param>
    /// <param name="password">密码</param>
    /// <returns>创建完成令牌</returns>
    [HttpGet]
    public async Task<ActionResult> CreateUserToken(string username, string password)
    {
        if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password)) return NotFound();
        var userToken = await _userService.CreateUserToken(username, password);
        return Ok(userToken);
    }

    /// <summary>
    ///     创建用户实体
    /// </summary>
    /// <param name="user">用户实体表单</param>
    /// <returns>创建成功个数</returns>
    [HttpPost]
    public async Task<ActionResult<int>> Create([FromBody] User? user)
    {
        if (user == null) return NotFound();

        return await _userService.Create(user);
    }
}
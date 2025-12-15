using EFCore_2_JWT.Model;

namespace EFCore_2_JWT.Service.IService;

/// <summary>
///     用户服务接口
/// </summary>
public interface IUserService
{
    /// <summary>
    ///     创建用户实体
    /// </summary>
    /// <param name="user">用户实体表单</param>
    /// <returns>创建成功个数</returns>
    Task<int> Create(User user);

    /// <summary>
    ///     创建用户令牌
    /// </summary>
    /// <param name="username">用户名</param>
    /// <param name="password">密码</param>
    /// <returns>创建完成令牌</returns>
    Task<string> CreateUserToken(string username, string password);
}
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using EFCore_2_JWT.Context;
using EFCore_2_JWT.Model;
using EFCore_2_JWT.Service.IService;
using Microsoft.CSharp.RuntimeBinder;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace EFCore_2_JWT.Service;

/// <summary>
///     用户服务对象
/// </summary>
public class UserService : IUserService
{
    private readonly EfCoreContext _context;

    /// <summary>
    ///     用户服务实现对象
    /// </summary>
    /// <param name="context"></param>
    public UserService(EfCoreContext context)
    {
        _context = context;
    }

    /// <summary>
    ///     创建用户实体
    /// </summary>
    /// <param name="user">用户实体表单</param>
    /// <returns>创建成功个数</returns>
    public Task<int> Create(User user)
    {
        _context.Users?.Add(user);
        return _context.SaveChangesAsync();
    }

    /// <summary>
    ///     创建用户令牌
    /// </summary>
    /// <param name="username">用户名</param>
    /// <param name="password">密码</param>
    /// <returns>创建完成令牌</returns>
    public async Task<string> CreateUserToken(string username, string password)
    {
        var user = await _context.Users!.AsNoTracking()
            .FirstOrDefaultAsync(user =>
                user.Username!.Equals(username)
                && user.Password!.Equals(password));

        if (user == null) throw new RuntimeBinderException("用户不存在");

        var claims = new[]
        {
            new Claim(ClaimTypes.Sid, user.Id.ToString()!),
            new Claim(ClaimTypes.Name, user.Username!)
        };

        var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("Bunny4565641145648445564545456454612541"));
        var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
        var jwtSecurityToken = new JwtSecurityToken("issuer", "audience", claims,
            expires: DateTime.Now.AddMinutes(30),
            signingCredentials: credentials);

        return new JwtSecurityTokenHandler().WriteToken(jwtSecurityToken);
    }
}
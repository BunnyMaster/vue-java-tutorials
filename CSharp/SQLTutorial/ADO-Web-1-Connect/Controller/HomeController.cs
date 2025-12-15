using Microsoft.AspNetCore.Mvc;
using WebApplication1.Data;

namespace WebApplication1.Controller;

[Route("api/[controller]")]
public class HomeController
{
    [HttpGet]
    public string? Index()
    {
        var configuration = AppCConfigurationServices.Configuration;
        return configuration.GetConnectionString("BunnyTest");
    }
}
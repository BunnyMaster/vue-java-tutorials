using Microsoft.Extensions.Configuration.Json;

namespace WebApplication1.Data;

public class AppCConfigurationServices
{
    static AppCConfigurationServices()
    {
        Configuration = new ConfigurationBuilder()
            .Add(new JsonConfigurationSource
            {
                Path = "appsettings.json",
                ReloadOnChange = true
            }).Build();
    }

    public static IConfiguration Configuration { get; set; }
}
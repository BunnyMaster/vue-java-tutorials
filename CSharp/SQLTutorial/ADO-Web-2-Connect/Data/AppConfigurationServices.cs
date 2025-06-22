using Microsoft.Extensions.Configuration.Json;

namespace ADO_Web_2_Connect.Data;

public class AppConfigurationServices
{
    static AppConfigurationServices()
    {
        Configuration = new ConfigurationBuilder().Add(new JsonConfigurationSource
        {
            Path = "appsettings.json",
            ReloadOnChange = true
        }).Build();
    }

    public static IConfiguration Configuration { get; set; }
}
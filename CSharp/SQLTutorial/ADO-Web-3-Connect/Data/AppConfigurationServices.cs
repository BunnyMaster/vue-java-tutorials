using Microsoft.Extensions.Configuration.Json;

namespace ADO_Web_3_Connect.Data;

public class AppConfigurationServices
{
    public static IConfiguration Configuration;

    static AppConfigurationServices()
    {
        Configuration = new ConfigurationBuilder().Add(new JsonConfigurationSource
        {
            Path = "appsettings.json",
            ReloadOnChange = true
        }).Build();
    }
}
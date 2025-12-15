using System.Windows;
using Message_MainApplication.Views;
using Message_ModuleA;

namespace Message_MainApplication;

/// <summary>
///     Interaction logic for App.xaml
/// </summary>
public partial class App : PrismApplication
{
    protected override void RegisterTypes(IContainerRegistry containerRegistry)
    {
    }

    protected override Window CreateShell()
    {
        return Container.Resolve<MainView>();
    }

    protected override void ConfigureModuleCatalog(IModuleCatalog moduleCatalog)
    {
        moduleCatalog.AddModule<ModuleAProfile>();
        base.ConfigureModuleCatalog(moduleCatalog);
    }
}
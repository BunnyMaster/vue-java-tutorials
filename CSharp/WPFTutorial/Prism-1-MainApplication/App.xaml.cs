using System.Windows;
using Prism_1_MainApplication.Views;
using Prism_1_ModuleA;
using Prism_1_ModuleB;

namespace Prism_1_MainApplication;

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
        moduleCatalog.AddModule<ModuleBProfile>();
        base.ConfigureModuleCatalog(moduleCatalog);
    }
}
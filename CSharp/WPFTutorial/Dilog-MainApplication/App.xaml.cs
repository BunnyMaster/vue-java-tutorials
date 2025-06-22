using System.Windows;
using Dialog_ModuleA;
using Dialog_ModuleB;
using Dilog_MainApplication.Views;

namespace Dilog_MainApplication;

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
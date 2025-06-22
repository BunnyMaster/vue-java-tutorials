using System.Diagnostics;
using System.Windows;
using WPF_26_MVVM.Views;

namespace WPF_26_MVVM;

/// <summary>
///     Interaction logic for App.xaml
/// </summary>
public partial class App : PrismApplication
{
    protected override void RegisterTypes(IContainerRegistry containerRegistry)
    {
        Debug.WriteLine("Registering views...");
        // 不写名称就是默认的名称 ViewA
        containerRegistry.RegisterForNavigation<ViewA>();
        containerRegistry.RegisterForNavigation<ViewB>();
        containerRegistry.RegisterForNavigation<ViewC>();
    }

    protected override Window CreateShell()
    {
        var mainView = Container.Resolve<MainView>();
        Debug.WriteLine("MainView resolved");
        return mainView;
    }
}
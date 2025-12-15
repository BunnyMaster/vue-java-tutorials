using Dialog_ModuleA.ViewModels;
using Dialog_ModuleA.Views;

namespace Dialog_ModuleA;

public class ModuleAProfile : IModule
{
    public void RegisterTypes(IContainerRegistry containerRegistry)
    {
        containerRegistry.RegisterForNavigation<ViewA, ViewAViewModel>();
        containerRegistry.RegisterForNavigation<ViewC, ViewCViewModel>();
    }

    public void OnInitialized(IContainerProvider containerProvider)
    {
    }
}
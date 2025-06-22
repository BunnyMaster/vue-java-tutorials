using Message_ModuleA.ViewModels;
using Message_ModuleA.Views;

namespace Message_ModuleA;

public class ModuleAProfile : IModule
{
    public void RegisterTypes(IContainerRegistry containerRegistry)
    {
        containerRegistry.RegisterForNavigation<ViewA, ViewAViewModel>();
    }

    public void OnInitialized(IContainerProvider containerProvider)
    {
    }
}
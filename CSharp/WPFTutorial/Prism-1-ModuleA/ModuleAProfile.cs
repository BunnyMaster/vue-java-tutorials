using Prism_1_ModuleA.ViewModels;
using Prism_1_ModuleA.Views;

namespace Prism_1_ModuleA;

public class ModuleAProfile : IModule
{
    public void RegisterTypes(IContainerRegistry containerRegistry)
    {
        containerRegistry.RegisterForNavigation<ViewA, ViewAModel>();
    }

    public void OnInitialized(IContainerProvider containerProvider)
    {
    }
}
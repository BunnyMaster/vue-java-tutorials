using Prism_1_ModuleB.Views;

namespace Prism_1_ModuleB;

public class ModuleBProfile : IModule
{
    public void RegisterTypes(IContainerRegistry containerRegistry)
    {
        containerRegistry.RegisterForNavigation<ViewB>();
    }

    public void OnInitialized(IContainerProvider containerProvider)
    {
    }
}
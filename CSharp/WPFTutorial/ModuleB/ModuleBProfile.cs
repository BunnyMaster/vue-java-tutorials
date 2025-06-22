using ModuleB.Views;

namespace ModuleB;

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
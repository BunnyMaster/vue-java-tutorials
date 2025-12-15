using ModuleA.Views;

namespace ModuleA;

public class ModuleAProfile : IModule
{
    public void RegisterTypes(IContainerRegistry containerRegistry)
    {
        containerRegistry.RegisterForNavigation<ViewA>();
    }

    public void OnInitialized(IContainerProvider containerProvider)
    {
    }
}
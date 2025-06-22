using Dialog_ModuleB.Views;

namespace Dialog_ModuleB;

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
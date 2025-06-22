namespace WPF_26_MVVM.ViewModels;

public class MainViewModel : BindableBase
{
    private readonly IRegionManager _regionManager;

    public MainViewModel(IRegionManager regionManager)
    {
        _regionManager = regionManager;
        OpenCommand = new DelegateCommand<string>(Open);
    }

    public DelegateCommand<string> OpenCommand { get; private set; }

    private void Open(string viewName)
    {
        // _regionManager.RequestNavigate("Body", viewName);
        _regionManager.Regions["ContentRegion"].RequestNavigate(viewName);
    }
}
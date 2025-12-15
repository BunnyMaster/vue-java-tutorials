namespace Message_MainApplication.ViewModels;

public class MainViewModel : BindableBase
{
    private readonly IRegionManager _regionManager;

    public MainViewModel(IRegionManager regionManager)
    {
        _regionManager = regionManager;
        OpenCommand = new DelegateCommand<string>(Open);
    }

    public DelegateCommand<string> OpenCommand { get; private set; }

    public void Open(string viewName)
    {
        _regionManager.Regions["MainRegion"].RequestNavigate(viewName);
    }
}
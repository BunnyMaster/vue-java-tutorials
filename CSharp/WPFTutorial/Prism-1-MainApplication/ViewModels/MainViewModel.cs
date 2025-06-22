namespace Prism_1_MainApplication.ViewModels;

public class MainViewModel : BindableBase
{
    private readonly IRegionManager _regionManager;
    private IRegionNavigationJournal? _journal;

    public MainViewModel(IRegionManager regionManager)
    {
        _regionManager = regionManager;
        OpenCommand = new DelegateCommand<string>(Open);
        BackCommand = new DelegateCommand(GoBack);
    }

    public DelegateCommand<string> OpenCommand { get; private set; }
    public DelegateCommand BackCommand { get; private set; }

    private void GoBack()
    {
        if (_journal is { CanGoBack: true }) _journal.GoBack();
    }

    private void GoForward()
    {
        if (_journal is { CanGoForward: true }) _journal.GoForward();
    }

    private void Open(string obj)
    {
        var keys = new NavigationParameters { { "Title", "标题信息" } };

        _regionManager.Regions["MainRegion"].RequestNavigate(obj, callback =>
        {
            if (callback.Success && callback.Context != null)
                _journal = callback.Context.NavigationService.Journal;
        }, keys);
    }
}
namespace Dilog_MainApplication.ViewModels;

public class MainViewModel : BindableBase
{
    private readonly IDialogService _dialogService;
    private readonly IRegionManager _regionManager;

    public MainViewModel(IRegionManager regionManager, IDialogService dialogService)
    {
        _regionManager = regionManager;
        _dialogService = dialogService;
        OpenCommand = new DelegateCommand<string>(Open);
        OpenDialogCommand = new DelegateCommand<string>(OpenDialog);
    }

    public DelegateCommand<string> OpenCommand { get; private set; }
    public DelegateCommand<string> OpenDialogCommand { get; private set; }

    private void OpenDialog(string obj)
    {
        var keys = new DialogParameters { { "Value", "Hello" } };
        _dialogService.ShowDialog(obj, keys, callback =>
        {
            if (callback.Result != ButtonResult.OK) return;
            var value = callback.Parameters.GetValue<string>("Value");
            Console.WriteLine(value);
        });
    }

    private void Open(string obj)
    {
        _regionManager.Regions["MainRegion"].RequestNavigate(obj);
    }
}
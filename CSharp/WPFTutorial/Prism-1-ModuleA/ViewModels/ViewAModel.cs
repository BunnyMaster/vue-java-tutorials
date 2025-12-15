using System.Windows;

namespace Prism_1_ModuleA.ViewModels;

// public class ViewAModel : BindableBase, INavigationAware
public class ViewAModel : BindableBase, IConfirmNavigationRequest
{
    private string? _title;

    public string? Title
    {
        get => _title;
        set
        {
            _title = value;
            RaisePropertyChanged();
        }
    }

    public void OnNavigatedTo(NavigationContext navigationContext)
    {
        if (navigationContext.Parameters.ContainsKey("Title"))
            Title = navigationContext.Parameters.GetValue<string>("Title");
    }

    /// <summary>
    ///     是否重用原来实例
    /// </summary>
    /// <param name="navigationContext"></param>
    /// <returns></returns>
    public bool IsNavigationTarget(NavigationContext navigationContext)
    {
        return true;
    }

    public void OnNavigatedFrom(NavigationContext navigationContext)
    {
    }

    public void ConfirmNavigationRequest(NavigationContext navigationContext, Action<bool> continuationCallback)
    {
        var result = true;

        var boxResult = MessageBox.Show("确认导航？", "INFO", MessageBoxButton.YesNo);
        if (boxResult == MessageBoxResult.No) result = false;
        continuationCallback(result);
    }
}
using System.Windows;
using WPF_13_INotifyPropertyChanged.Command;

namespace WPF_13_INotifyPropertyChanged.ViewModels;

public class MainWindowViewModel : ViewModelBase
{
    private string? _content;

    private string? _title;

    public MainWindowViewModel()
    {
        ShowCommand = new ShowCommand(Show);
    }

    public string? Title
    {
        get => _title;
        set
        {
            _title = value;
            OnPropertyChanged();
        }
    }

    public string? Content
    {
        get => _content;
        set
        {
            _content = value;
            OnPropertyChanged();
        }
    }

    public ShowCommand ShowCommand { get; set; }

    private void Show()
    {
        Content = "Hello World";
        Title = "Hello World---Title";
        MessageBox.Show($"👼👼👼：{Content}");
    }
}
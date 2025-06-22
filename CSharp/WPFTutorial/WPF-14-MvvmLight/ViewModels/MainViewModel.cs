using System.Windows;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;

namespace WPF_14_MvvmLight.ViewModels;

public class MainViewModel : ViewModelBase
{
    private string? _content;
    private string? _title;

    public MainViewModel()
    {
        // ShowCommand = new ShowCommand(Show);
        ShowCommand = new RelayCommand(Show);
        ShowArgCommand = new RelayCommand<string>(ShowArg);
    }

    // public ShowCommand ShowCommand { get; set; }
    public RelayCommand ShowCommand { get; set; }
    public RelayCommand<string> ShowArgCommand { get; set; }

    public string? Title
    {
        get => _title;
        set
        {
            _title = value;
            RaisePropertyChanged();
        }
    }

    public string? Content
    {
        get => _content;
        set
        {
            _content = value;
            RaisePropertyChanged();
        }
    }

    private void Show()
    {
        Title = "Hello World";
        Content = "Hello World -- ";
        MessageBox.Show("Hello World -- ");
    }

    private void ShowArg(string arg)
    {
        Title = $"Hello World -- {arg}";
        MessageBox.Show($"Hello World -- {arg}");
    }
}
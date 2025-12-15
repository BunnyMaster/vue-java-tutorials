using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows;

namespace WPF_8__DataBindings;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : INotifyPropertyChanged
{
    private string? _boundText;

    public MainWindow()
    {
        DataContext = this;
        InitializeComponent();
    }

    public string? BoundText
    {
        get => _boundText;
        set
        {
            _boundText = value;
            // PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(BoundText)));
            // OnPropertyChanged("BoundText");
            OnPropertyChanged();
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;

    private void SetButton_OnClick(object sender, RoutedEventArgs e)
    {
        BoundText = "set from code";
    }

    private void OnPropertyChanged([CallerMemberName] string? propertyName = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }
}
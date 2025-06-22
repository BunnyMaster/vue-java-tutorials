using System.Windows;
using WPF_10_ICommand.ViewModel;

namespace WPF_10_ICommand;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        DataContext = new MainWindowViewModel();
    }

    private void ButtonBase_OnClick(object sender, RoutedEventArgs e)
    {
        MessageBox.Show("ButtonBase_OnClick");
    }
}
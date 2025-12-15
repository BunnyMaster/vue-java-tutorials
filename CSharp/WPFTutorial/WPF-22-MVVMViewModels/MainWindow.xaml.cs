using System.Windows;
using WPF_23_MVVMViewModels.ViewModel;

namespace WPF_23_MVVMViewModels;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        var windowViewModel = new MainWindowViewModel();
        DataContext = windowViewModel;
    }
}
using System.Windows;
using WPF_12_ICommad.ViewModels;

namespace WPF_12_ICommad;

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
}
using System.Windows;
using WPF_14_MvvmLight.ViewModels;

namespace WPF_14_MvvmLight;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        DataContext = new MainViewModel();
    }
}
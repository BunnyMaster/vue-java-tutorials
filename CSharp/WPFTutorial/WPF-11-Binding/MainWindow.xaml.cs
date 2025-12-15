using System.Windows;
using WPF_11_Binding.ViewModels;

namespace WPF_11_Binding;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        DataContext = new MainWindowViewModel { Title = "XXX" };
    }
}
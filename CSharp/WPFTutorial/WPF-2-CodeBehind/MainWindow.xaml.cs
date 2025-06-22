using System.Windows;

namespace WPF_2_CodeBehind;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    private bool _isRunning;

    public MainWindow()
    {
        InitializeComponent();
    }

    private void ButtonToggleRun_OnClick(object sender, RoutedEventArgs e)
    {
        TbHellBlock.Text = "TB Hello";
        TbHellBlock.VerticalAlignment = VerticalAlignment.Top;

        ButtonToggleRun.Content = _isRunning ? "Stop" : "Running";

        _isRunning = !_isRunning;
    }
}
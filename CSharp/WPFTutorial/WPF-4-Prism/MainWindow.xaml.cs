using System.Windows;
using System.Windows.Input;

namespace WPF_4_Prism;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        MinButton.Click += (_, _) => { WindowState = WindowState.Minimized; };
        MaxButton.Click += (_, _) =>
        {
            WindowState = WindowState == WindowState.Maximized ? WindowState.Normal : WindowState.Maximized;
        };
        CloseButton.Click += (_, _) => { Close(); };

        ColorZone.MouseMove += (_, e) =>
        {
            if (e.LeftButton == MouseButtonState.Pressed) DragMove();
        };

        ColorZone.MouseDoubleClick += (_, e) =>
        {
            WindowState = WindowState == WindowState.Normal ? WindowState.Maximized : WindowState.Normal;
        };
    }
}
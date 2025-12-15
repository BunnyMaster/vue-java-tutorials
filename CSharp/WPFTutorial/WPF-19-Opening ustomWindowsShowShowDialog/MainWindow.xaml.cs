using System.Windows;
using System.Windows.Input;
using WPF_19_Opening_ustomWindowsShowShowDialog.View;

namespace WPF_19_Opening_ustomWindowsShowShowDialog;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow
{
    public MainWindow()
    {
        InitializeComponent();
    }

    private void NormalButton_OnClick(object sender, RoutedEventArgs e)
    {
        var normalWindow = new NormalWindow();
        normalWindow.Show();
    }

    private void ModalButton_OnClick(object sender, RoutedEventArgs e)
    {
        var modalWindow = new ModalWindow(this);
        Opacity = 0.4;
        modalWindow.ShowDialog();
        Opacity = 1;
        if (modalWindow.Success) InputTextBox.Text = modalWindow.Message;
    }

    private void MainWindow_OnMouseLeftButtonDown(object sender, MouseButtonEventArgs e)
    {
        DragMove();
    }
}
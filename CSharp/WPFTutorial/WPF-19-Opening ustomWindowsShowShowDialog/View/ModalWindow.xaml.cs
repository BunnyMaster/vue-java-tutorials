using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace WPF_19_Opening_ustomWindowsShowShowDialog.View;

public partial class ModalWindow
{
    public ModalWindow(Window parentWindow)
    {
        Owner = parentWindow;
        InitializeComponent();
    }

    public bool Success { get; set; }
    public string Message { get; set; } = string.Empty;

    private void OkButton_OnClick(object sender, RoutedEventArgs e)
    {
        Success = true;
        Message = InputTextBox.Text;
        Close();
    }

    private void CancelButton_OnClick(object sender, RoutedEventArgs e)
    {
        Close();
    }

    private void ModalWindow_OnMouseLeftButtonDown(object sender, MouseButtonEventArgs e)
    {
        DragMove();
    }

    private void InputTextBox_OnTextChanged(object sender, TextChangedEventArgs e)
    {
        OkButton.IsEnabled = !string.IsNullOrEmpty(InputTextBox.Text);
    }
}
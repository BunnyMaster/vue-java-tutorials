using System.Windows;
using System.Windows.Controls;

namespace WPF_5_CustomTextboxContro.View.UserControllers;

public partial class ClearableTextBox : UserControl
{
    private string _placeholder;

    public ClearableTextBox()
    {
        InitializeComponent();
    }

    public string Placeholder
    {
        get => _placeholder;
        set
        {
            _placeholder = value;
            // 不要这样写！！！
            TbPlaceholder.Text = value;
        }
    }

    private void ClearButton_OnClick(object sender, RoutedEventArgs e)
    {
        MyTextBox.Clear();
        MyTextBox.Focus();
    }

    private void MyTextBox_OnTextChanged(object sender, TextChangedEventArgs e)
    {
        TbPlaceholder.Visibility = string.IsNullOrEmpty(MyTextBox.Text) ? Visibility.Visible : Visibility.Hidden;
    }
}
using System.Windows;

namespace WPF_9_MessageBox;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }

    private void FireButton_OnClick(object sender, RoutedEventArgs e)
    {
        var result = MessageBox.Show("You Message Here.", "Error", MessageBoxButton.YesNo, MessageBoxImage.Question);
        InfoTextBlock.Text = result == MessageBoxResult.Yes ? "Agree" : "Not Agree";
    }
}
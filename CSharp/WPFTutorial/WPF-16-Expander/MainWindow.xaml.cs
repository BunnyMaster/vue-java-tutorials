using System.Windows;

namespace WPF_16_Expander;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }

    private void DDetailButton_OnClick(object sender, RoutedEventArgs e)
    {
        DetailExpander.IsExpanded = !DetailExpander.IsExpanded;
    }
}
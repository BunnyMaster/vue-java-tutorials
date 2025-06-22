using System.Windows;
using MessageBox = System.Windows.MessageBox;

namespace WPF_11_FolderBrowserDialog;

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
        var folderBrowserDialog = new FolderBrowserDialog();
        folderBrowserDialog.InitialDirectory = @"D:\Project\Study\C#\WPF\6-WPF Tutorial";
        var dialogResult = folderBrowserDialog.ShowDialog();

        if (dialogResult != System.Windows.Forms.DialogResult.OK) return;
        Console.WriteLine(dialogResult);
        MessageBox.Show("Folder selected: " + folderBrowserDialog.SelectedPath);
    }
}
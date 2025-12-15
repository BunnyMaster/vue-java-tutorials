using System.Diagnostics;
using System.Text;
using System.Windows;
using Microsoft.Win32;

namespace WPF_10_OpenFileDialog;

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
       

        var fileDialog = new OpenFileDialog
        {
            Filter = "选择视频MP4 | *.mp4",
            Title = "红红火火恍恍惚惚",
            Multiselect = true
        };

        var success = fileDialog.ShowDialog();


        if (success != true) return;
        var path = fileDialog.FileName;
        var safeFileName = fileDialog.SafeFileName;
        InfoTextBlock.Text = $"{path} -- {safeFileName}";

        foreach (var fileName in fileDialog.FileNames) Debug.WriteLine(fileName);
        foreach (var fileName in fileDialog.SafeFileNames) Debug.WriteLine(fileName);
    }
}
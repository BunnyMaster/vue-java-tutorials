using System.Windows;
using WPF_12_ICommad.Command;

namespace WPF_12_ICommad.ViewModels;

public class MainWindowViewModel
{
    public MainWindowViewModel()
    {
        MainWindowCommand = new MainWindowCommand(Show);
    }

    public MainWindowCommand MainWindowCommand { get; set; }

    private static void Show()
    {
        MessageBox.Show("点击了按钮");
    }
}
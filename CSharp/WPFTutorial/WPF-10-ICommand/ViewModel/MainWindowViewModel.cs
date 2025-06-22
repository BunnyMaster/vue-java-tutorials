using System.Windows;

namespace WPF_10_ICommand.ViewModel;

public class MainWindowViewModel
{
    public MainWindowViewModel()
    {
        Command = new MainWindowCommand(Show);
    }

    public MainWindowCommand Command { get; set; }

    private void Show()
    {
        MessageBox.Show("ButtonBase_OnClick");
    }
}
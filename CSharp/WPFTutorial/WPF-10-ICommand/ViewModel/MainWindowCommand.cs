using System.Windows.Input;

namespace WPF_10_ICommand.ViewModel;

public class MainWindowCommand : ICommand
{
    private readonly Action _execute;

    public MainWindowCommand(Action execute)
    {
        _execute = execute;
    }

    public bool CanExecute(object? parameter)
    {
        return true;
    }

    public void Execute(object? parameter)
    {
        _execute();
    }

    public event EventHandler? CanExecuteChanged;
}
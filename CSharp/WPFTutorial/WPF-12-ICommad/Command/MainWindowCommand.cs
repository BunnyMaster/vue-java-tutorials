using System.Windows.Input;

namespace WPF_12_ICommad.Command;

public class MainWindowCommand : ICommand
{
    private readonly Action _executeAction;

    public MainWindowCommand(Action executeAction)
    {
        _executeAction = executeAction;
    }

    public event EventHandler? CanExecuteChanged;

    public bool CanExecute(object? parameter)
    {
        return true;
    }

    public void Execute(object? parameter)
    {
        _executeAction();
    }
}
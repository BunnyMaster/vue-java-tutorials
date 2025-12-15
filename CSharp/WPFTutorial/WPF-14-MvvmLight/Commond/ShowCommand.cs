using System.Windows.Input;

namespace WPF_14_MvvmLight.Commond;

public class ShowCommand : ICommand
{
    private readonly Action _executeAction;

    public ShowCommand(Action executeAction)
    {
        _executeAction = executeAction;
    }

    public bool CanExecute(object? parameter)
    {
        return true;
    }

    public void Execute(object? parameter)
    {
        _executeAction();
    }

    public event EventHandler? CanExecuteChanged;
}
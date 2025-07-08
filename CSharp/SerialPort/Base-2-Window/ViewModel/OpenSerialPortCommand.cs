using System.Windows.Input;

namespace Base_2_Window.ViewModel;

public class OpenSerialPortCommand:ICommand
{
    public OpenSerialPortCommand(Action execute)
    {
        _execute = execute;
    }

    private readonly Action _execute;
    
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
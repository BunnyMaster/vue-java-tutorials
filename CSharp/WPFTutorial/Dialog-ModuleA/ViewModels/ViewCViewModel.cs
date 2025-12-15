namespace Dialog_ModuleA.ViewModels;

public class ViewCViewModel : IDialogAware
{
    public ViewCViewModel(DialogCloseListener requestClose)
    {
        RequestClose = requestClose;
        ConfirmCommand = new DelegateCommand(Confirm);
        SaveCommand = new DelegateCommand(Save);
    }

    public DelegateCommand ConfirmCommand { get; set; }
    public DelegateCommand SaveCommand { get; set; }

    public string? Title { get; set; }

    public bool CanCloseDialog()
    {
        return true;
    }

    public void OnDialogClosed()
    {
        var keys = new DialogParameters { { "Value", "Hello" } };
        RequestClose.Invoke(keys, ButtonResult.OK);
    }

    public void OnDialogOpened(IDialogParameters parameters)
    {
        Title = parameters.GetValue<string>("Value");
    }

    public DialogCloseListener RequestClose { get; }

    private void Save()
    {
    }

    private void Confirm()
    {
        RequestClose.Invoke(new DialogResult(ButtonResult.OK));
    }
}
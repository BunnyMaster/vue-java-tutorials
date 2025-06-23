using System.Collections.ObjectModel;
using Demo_TODO.Common.Model;
using Prism.Mvvm;

namespace Demo_TODO.ViewModels;

public class MainViewModel : BindableBase
{
    public MainViewModel(ObservableCollection<MenuBar> menuBars)
    {
        MenuBars = menuBars;
    }

    public ObservableCollection<MenuBar> MenuBars { get; set; }

    private void CreateMenuBars()
    {
        MenuBars.Add(new MenuBar { Icon = "Home", Title = "首页", Namespace = "IndexView" });
        MenuBars.Add(new MenuBar { Icon = "NotebookOutline", Title = "代办事项", Namespace = "ToDoView" });
        MenuBars.Add(new MenuBar { Icon = "NotebookPlus", Title = "备忘录", Namespace = "MemoView" });
        MenuBars.Add(new MenuBar { Icon = "Cog", Title = "设置", Namespace = "SettingsView" });
    }
}
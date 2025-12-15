using Prism.Mvvm;

namespace Demo_TODO.Common.Model;

/// <summary>
///     系统导航栏菜单实体类
/// </summary>
public class MenuBar : BindableBase
{
    public string Icon { get; set; }

    public string Title { get; set; }

    public string Namespace { get; set; }
}
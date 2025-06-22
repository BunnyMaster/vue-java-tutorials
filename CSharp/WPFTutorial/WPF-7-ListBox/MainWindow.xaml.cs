using System.Collections;
using System.Windows;

namespace WPF_7_ListBox;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();

        var arrayList = new ArrayList();
        for (var i = 0; i < 10; i++)
        {
            var code = Random.Shared.Next(999, 99999).ToString("x6");
            Console.WriteLine(code);
            arrayList.Add(new Color
            {
                Code = $"#{code}",
                Name = code
            });
        }

        ListBox.ItemsSource = arrayList;
    }
}

public class Color
{
    public string? Code { get; set; }
    public string? Name { get; set; }
}
using System.Collections;
using System.Windows;

namespace WPF_8_DataGrid;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        var arrayList = new ArrayList();

        for (var i = 0; i < 100; i++)
        {
            var randomNum = Random.Shared.NextInt64(0, 999);
            var name = (i * randomNum).ToString("x6");
            Console.WriteLine(name);
            arrayList.Add(new Color
            {
                Name = name,
                Code = $"#{name}"
            });
        }

        Grid.ItemsSource = arrayList;
    }
}

public class Color
{
    public string? Code { get; set; }
    public string? Name { get; set; }
}
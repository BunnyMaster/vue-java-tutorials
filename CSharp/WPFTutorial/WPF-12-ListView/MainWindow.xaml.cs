using System.Collections;
using System.Windows;

namespace WPF_12_ListView;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();

        ListView.Items.Add("a1");
        ListView.Items.Add("a2");
        ListView.Items.Add("a3");
        ListView.Items.Add("a4");
        ListView.Items.Add("a5");
        ListView.Items.Add("a6");
    }

    private void AddButton_OnClick(object sender, RoutedEventArgs e)
    {
        if (string.IsNullOrEmpty(TextBox.Text)) return;
        ListView.Items.Add(TextBox.Text);

        TextBox.Clear();
    }

    private void DeletedButton_OnClick(object sender, RoutedEventArgs e)
    {
        // 通过索引删除
        // var index = ListView.SelectedIndex;
        // ListView.Items.RemoveAt(index);

        // 通过元素删除
        // var item = ListView.SelectedItem;
        // var result = MessageBox.Show($"Are you sure you want to delete this ${(string)item}?",
        //     "Deleted?", MessageBoxButton.YesNo);
        // if (result != MessageBoxResult.Yes) return;

        // 批量删除
        var items = ListView.SelectedItems;
        var list = new ArrayList(items);

        if (list.Count <= 0) return;

        var result = MessageBox.Show($"Are you sure you want to delete this {list.Count}?",
            "Deleted?", MessageBoxButton.YesNo);
        if (result != MessageBoxResult.Yes) return;

        foreach (var item in list) ListView.Items.Remove(item);
    }

    private void ClearButton_OnClick(object sender, RoutedEventArgs e)
    {
        ListView.Items.Clear();
    }
}
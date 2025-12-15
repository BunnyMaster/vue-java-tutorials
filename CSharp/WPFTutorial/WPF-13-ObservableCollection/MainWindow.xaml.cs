using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows;

namespace WPF_13_ObservableCollection;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : INotifyPropertyChanged
{
    private ObservableCollection<string> _entries;

    public MainWindow()
    {
        DataContext = this;
        _entries = ["1", "2"];

        InitializeComponent();
    }

    public ObservableCollection<string> Entries
    {
        get => _entries;
        set
        {
            _entries = value;
            OnPropertyChanged();
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;

    private void OnPropertyChanged([CallerMemberName] string? propertyName = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }

    private void AddButton_OnClick(object sender, RoutedEventArgs e)
    {
        Entries.Add(TextEntryBox.Text);
    }

    private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
    {
        var selectedItem = (string)EntriesListView.SelectedItem;
        Entries.Remove(selectedItem);
    }

    private void ClearButton_OnClick(object sender, RoutedEventArgs e)
    {
        Entries.Clear();
    }
}
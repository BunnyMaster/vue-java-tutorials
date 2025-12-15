using System.Collections.ObjectModel;
using WPF_23_MVVMRelayCommand.Model;
using WPF_23_MVVMRelayCommand.MVVM;

namespace WPF_23_MVVMRelayCommand.ViewModel;

public class MainWindowViewModel : ViewModelBase
{
    private Item? _selectedItem;

    public RelayCommand AddCommand => new(execute => AddItem(), canExecute => true);
    public RelayCommand DeleteCommand => new(execute => DeleteItem(), canExecute => SelectedItem != null);
    public RelayCommand SaveCommand => new(execute => SaveItem(), canExecute => CanSave());

    public ObservableCollection<Item> Items { get; set; } =
    [
        new() { Name = "Product1", SerialNUmber = "0001", Quantity = 5 },
        new() { Name = "Product2", SerialNUmber = "0002", Quantity = 6 }
    ];


    public Item? SelectedItem
    {
        get => _selectedItem;
        set
        {
            _selectedItem = value;
            OnPropertyChanged();
        }
    }

    private void AddItem()
    {
        Items.Add(new Item { Name = "NEW ITEM", SerialNUmber = "0001", Quantity = 5 });
    }

    private void DeleteItem()
    {
        Items.Remove(SelectedItem!);
    }

    private void SaveItem()
    {
        // Save
    }

    private bool CanSave()
    {
        return true;
    }
}
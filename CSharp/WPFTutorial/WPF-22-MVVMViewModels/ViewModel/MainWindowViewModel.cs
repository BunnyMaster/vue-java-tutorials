using System.Collections.ObjectModel;
using WPF_23_MVVMViewModels.Model;
using WPF_23_MVVMViewModels.MVVM;

namespace WPF_23_MVVMViewModels.ViewModel;

public class MainWindowViewModel : ViewModelBase
{
    private Item? _selectedItem;

    public ObservableCollection<Item> Items { get; set; } =
    [
        new() { Name = "Product1", SerialNUmber = "0001", Quantity = 5 },
        new() { Name = "Product2", SerialNUmber = "0002", Quantity = 6 }
    ];


    public Item SelectedItem
    {
        get => _selectedItem!;
        set
        {
            _selectedItem = value;
            OnPropertyChanged();
        }
    }
}
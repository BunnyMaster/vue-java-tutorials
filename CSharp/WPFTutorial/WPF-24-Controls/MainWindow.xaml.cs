using System.Windows;

namespace WPF_24_Controls;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public List<Person> Persons = [];

    public MainWindow()
    {
        InitializeComponent();

        Persons.Add(new Person { FirstName = "W1", LastName = "E1" });
        Persons.Add(new Person { FirstName = "W2", LastName = "E2" });
        Persons.Add(new Person { FirstName = "W3", LastName = "E3" });

        ComboBox.ItemsSource = Persons;
    }

    private void SubmitButton_OnClick(object sender, RoutedEventArgs e)
    {
        MessageBox.Show($"Hello {FirstNameTextBlock.Text}");
    }
}

public class Person
{
    public string? FirstName { get; set; }
    public string? LastName { get; set; }

    public string? FullName => $"{FirstName} {LastName}";
}
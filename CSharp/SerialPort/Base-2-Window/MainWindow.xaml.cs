using System.IO.Ports;
using System.Windows;
using Base_2_Window.Model;
using Base_2_Window.ViewModel;

namespace Base_2_Window;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();

        DataContext = new MainWindowViewModel();
        Command = new OpenSerialPortCommand(OpenPort);
    }

    public OpenSerialPortCommand Command { get; set; }

    public void OpenPort()
    {
        
    }
}
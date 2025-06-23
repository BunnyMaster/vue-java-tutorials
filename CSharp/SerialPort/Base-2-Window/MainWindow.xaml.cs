using System.IO.Ports;
using System.Windows;
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

        DataContext = new MainWindowViewModel
        {
            SerialPortNames = SerialPort.GetPortNames().Select(s => s).ToList(),
            BaudRates = new List<int> { 9600 },
            DataBits = new List<int> { 6, 7, 8 },
            StopBitsList = new List<StopBits> { StopBits.None, StopBits.One, StopBits.Two, StopBits.OnePointFive },
            ParityList = new List<Parity>
                { Parity.None, Parity.Even, Parity.Mark, Parity.None, Parity.Odd, Parity.Space }
        };
    }
}
using System.IO.Ports;
using Base_2_Window.Model;
using Base_2_Window.MVVM;

namespace Base_2_Window.ViewModel;

public class MainWindowViewModel : ViewModelBase
{
    public MainWindowViewModel()
    {
        SerialPortModel =   new SerialPortModel()
        {
            SerialPortNames = SerialPort.GetPortNames().Select(s => s).ToList(),
            BaudRates = new List<int> { 9600 },
            DataBits = new List<int> { 6, 7, 8 },
            StopBitsList = new List<StopBits> { StopBits.None, StopBits.One, StopBits.Two, StopBits.OnePointFive },
            ParityList = new List<Parity>
                { Parity.None, Parity.Even, Parity.Mark, Parity.None, Parity.Odd, Parity.Space }
        };;
    }

    public SerialPortModel SerialPortModel { get; set; }
}
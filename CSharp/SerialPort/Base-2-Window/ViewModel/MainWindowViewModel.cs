using System.IO.Ports;
using Base_2_Window.MVVM;

namespace Base_2_Window.ViewModel;

public class MainWindowViewModel : ViewModelBase
{
    public List<string>? SerialPortNames { get; set; }

    public List<int>? BaudRates { get; set; }

    public List<StopBits>? StopBitsList { get; set; }

    public List<Parity>? ParityList { get; set; }

    public List<int>? DataBits { get; set; }
}
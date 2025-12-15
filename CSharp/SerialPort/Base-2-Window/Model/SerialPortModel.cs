using System.IO.Ports;

namespace Base_2_Window.Model;

public class SerialPortModel
{
    public List<string>? SerialPortNames { get; set; }

    public List<int>? BaudRates { get; set; }

    public List<StopBits>? StopBitsList { get; set; }

    public List<Parity>? ParityList { get; set; }

    public List<int>? DataBits { get; set; }
}
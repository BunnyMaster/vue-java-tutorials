using System.IO.Ports;

namespace Base_2_Window.Model;

public class SerialPortModel
{
    public List<string>? SerialPortNames { get; set; }

    public int? BaudRate { get; set; }

    public int? DataBits { get; set; }

    public StopBits? StopBits { get; set; }
}
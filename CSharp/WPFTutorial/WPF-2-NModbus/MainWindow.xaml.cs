using System.Windows;
using WPF_2_NModbus.NModBus;

namespace WPF_2_NModbus;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow
{
    // 修改为单例模式
    private readonly SerialPortAsync _serialPortInstance;

    public MainWindow()
    {
        InitializeComponent();
        // 使用单例
        _serialPortInstance ??= new SerialPortAsync("COM1");
    }

    private void ConnectButton_OnClick(object sender, RoutedEventArgs e)
    {
        _ = _serialPortInstance.WriteRegistersAsync(1, 1, [1]);
    }

    private void WriteRegistersButton_OnClick(object sender, RoutedEventArgs e)
    {
        var slaveId = byte.Parse(SlaveIdTextBox.Text);
        var startAddress = ushort.Parse(StartAddressTextBox.Text);
        var content = Enumerable.Range(1, ushort.Parse(WirTextBox.Text))
            .Select(x => (ushort)x)
            .ToArray();

        _ = _serialPortInstance.WriteRegistersAsync(slaveId, startAddress, content);
    }
}
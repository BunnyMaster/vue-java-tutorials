using System.Diagnostics;
using System.IO.Ports;
using System.Windows;
using NModbus;
using NModbus.Serial;

namespace WPF_2_NModbus.NModBus;

public class SerialPortAsync : IDisposable
{
    private readonly string _portName;
    private readonly IModbusSerialMaster _serialMaster;
    private bool _disposed;

    public SerialPortAsync(string portName)
    {
        if (string.IsNullOrWhiteSpace(portName))
            throw new ArgumentException("端口名称不能为空", nameof(portName));

        _portName = portName;
        _serialMaster = CreateModbusSerialMaster();
    }

    public void Dispose()
    {
        if (_disposed) return;
        _serialMaster.Dispose();
        _disposed = true;
        GC.SuppressFinalize(this);
    }

    /// <summary>
    ///     异步写入寄存器
    /// </summary>
    /// <param name="slaveId">从站ID</param>
    /// <param name="startAddress">起始地址</param>
    /// <param name="registers">要写入的寄存器值</param>
    /// <param name="cancellationToken">取消令牌</param>
    /// <returns>表示异步操作的任务</returns>
    public async Task WriteRegistersAsync(byte slaveId, ushort startAddress, ushort[] registers,
        CancellationToken cancellationToken = default)
    {
        if (!_disposed)
            try
            {
                await Task.Run(() =>
                {
                    cancellationToken.ThrowIfCancellationRequested();
                    _serialMaster.WriteMultipleRegisters(slaveId, startAddress, registers);
                }, cancellationToken).ConfigureAwait(false);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                Debug.WriteLine(ex);
                MessageBox.Show("写入寄存器失败", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
    }

    /// <summary>
    ///     创建Modbus串口主站
    /// </summary>
    private IModbusSerialMaster CreateModbusSerialMaster()
    {
        var port = new SerialPort(_portName)
        {
            BaudRate = 9600,
            DataBits = 8,
            Parity = Parity.None,
            StopBits = StopBits.One
        };

        try
        {
            port.Open();
            var adapter = new SerialPortAdapter(port)
            {
                ReadTimeout = 1000,
                WriteTimeout = 1000
            };

            var factory = new ModbusFactory();
            return factory.CreateRtuMaster(adapter);
        }
        catch
        {
            port.Dispose();
            throw;
        }
    }
}
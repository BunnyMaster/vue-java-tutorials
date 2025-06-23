using System;
using System.IO.Ports;

namespace Base_1_Create
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var serialPort = new SerialPort
            {
                // 串口名称
                PortName = "COM1",
                // 值越大传输越快，默认9600
                BaudRate = 9600,
                // 数据位
                DataBits = 8,
                // 默认是 1
                StopBits = StopBits.One,
                // 校验位，校验位 1 的个数
                Parity = Parity.Odd,
                // 读取时缓冲区字节数
                ReadBufferSize = 4096
            };

            // 打开串口
            serialPort.Open();

            if (serialPort.IsOpen) Console.WriteLine("串口打开");

            // 关闭串口
            // serialPort.Close();
        }
    }
}
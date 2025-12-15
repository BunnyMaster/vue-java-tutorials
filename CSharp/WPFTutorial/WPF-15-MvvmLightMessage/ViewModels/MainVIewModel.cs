using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Messaging;

namespace WPF_15_MvvmLightMessage.ViewModels;

public class MainVIewModel : ViewModelBase
{
    public MainVIewModel()
    {
        SendMessageCommand = new RelayCommand<string>(SendMessage);
    }

    public RelayCommand<string> SendMessageCommand { get; set; }

    private void SendMessage(string message)
    {
        // MessageBox.Show("Hello World");
        Console.WriteLine("发送消息。。。");
        Messenger.Default.Send(message, "Token1");
    }
}
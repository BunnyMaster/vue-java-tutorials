using System.Windows;
using GalaSoft.MvvmLight.Messaging;
using WPF_15_MvvmLightMessage.ViewModels;

namespace WPF_15_MvvmLightMessage;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        DataContext = new MainVIewModel();
        Messenger.Default.Register<string>(this, "Token1", MessageToken1Sender);
    }

    private void MessageToken1Sender(string value)
    {
        MessageBox.Show(value);
    }
}
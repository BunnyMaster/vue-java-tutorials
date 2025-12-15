using System.Windows;
using System.Windows.Controls;
using Message_ModuleA.Events;

namespace Message_ModuleA.Views;

public partial class ViewA : UserControl
{
    private readonly IEventAggregator _eventAggregator;

    public ViewA(IEventAggregator aggregator)
    {
        InitializeComponent();
        _eventAggregator = aggregator;

        _eventAggregator.GetEvent<MessageEvent>().Subscribe(CloseMessage);
    }

    private void CloseMessage(string message)
    {
        // _eventAggregator.GetEvent<MessageEvent>().Subscribe(arg =>
        // {
        //     var result = MessageBox.Show($"消息：{arg}");
        //     if (result == MessageBoxResult.OK) _eventAggregator.GetEvent<MessageEvent>().Publish("确认");
        // });

        var result = MessageBox.Show($"消息：{message}", "提示", MessageBoxButton.OKCancel);
        if (result == MessageBoxResult.OK) _eventAggregator.GetEvent<MessageEvent>().Publish("确认");
        else
            _eventAggregator.GetEvent<MessageEvent>().Unsubscribe(CloseMessage);
    }
}
using Message_ModuleA.Events;

namespace Message_ModuleA.ViewModels;

public class ViewAViewModel : BindableBase
{
    public ViewAViewModel(DialogCloseListener requestClose, IEventAggregator aggregator)
    {
        // 向Message发送
        aggregator.GetEvent<MessageEvent>().Publish("Hello");
    }
}
using System.Windows;
using System.Windows.Input;

namespace Demo_TODO
{
    /// <summary>
    ///     Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            // 最小化按钮
            MinButton.Click += (_, _) => { WindowState = WindowState.Minimized; };

            // 放大缩小按钮
            MaxButton.Click += (_, _) =>
            {
                WindowState = WindowState == WindowState.Maximized ? WindowState.Minimized : WindowState.Maximized;
            };

            // 关闭按钮
            CloseButton.Click += (_, _) => { Close(); };

            // 拖拽窗口拖动
            ColorZone.MouseDown += (_, eventArgs) =>
            {
                if (eventArgs.LeftButton == MouseButtonState.Pressed) DragMove();
            };

            // 双击放大和缩小
            ColorZone.MouseDoubleClick += (_, _) =>
            {
                WindowState = WindowState == WindowState.Normal ? WindowState.Maximized : WindowState.Normal;
            };
        }
    }
}
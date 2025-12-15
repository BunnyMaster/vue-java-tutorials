using System.Diagnostics;
using System.IO;
using System.Windows;
using System.Windows.Threading;

namespace WPF_25_TreeView;

/// <summary>
///     Interaction logic for App.xaml
/// </summary>
public partial class App
{
    protected override void OnStartup(StartupEventArgs e)
    {
        // 全局未处理异常捕获（UI线程和非UI线程）
        AppDomain.CurrentDomain.UnhandledException += OnUnhandledException;

        // 专为WPF UI线程异常捕获（例如按钮点击事件中的异常）
        DispatcherUnhandledException += OnDispatcherUnhandledException;

        // 异步任务异常捕获
        TaskScheduler.UnobservedTaskException += OnUnobservedTaskException;

        base.OnStartup(e);
    }

    /// <summary>
    ///     处理非UI线程异常
    /// </summary>
    private static void OnUnhandledException(object sender, UnhandledExceptionEventArgs e)
    {
        if (e.ExceptionObject is not Exception exception) return;
        LogException(exception, "全局异常");

        if (e.IsTerminating) // 如果是致命异常
        {
            ShowFatalError(exception);
            Environment.Exit(1); // 确保应用退出
        }
        else
        {
            ShowUserFriendlyError(exception);
        }
    }

    /// <summary>
    ///     处理UI线程异常（可阻止应用崩溃）
    /// </summary>
    private static void OnDispatcherUnhandledException(object sender, DispatcherUnhandledExceptionEventArgs e)
    {
        LogException(e.Exception, "UI线程异常");
        ShowUserFriendlyError(e.Exception);

        // 标记为已处理，阻止应用崩溃（谨慎使用！）
        e.Handled = true;
    }

    /// <summary>
    ///     处理异步任务异常
    /// </summary>
    private static void OnUnobservedTaskException(object? sender, UnobservedTaskExceptionEventArgs e)
    {
        LogException(e.Exception, "异步任务异常");
        e.SetObserved(); // 标记为已处理，避免进程崩溃
    }

    /// <summary>
    ///     记录异常到日志文件
    /// </summary>
    private static void LogException(Exception ex, string category)
    {
        var logMessage = $"[{DateTime.Now}] [{category}]\n{ex}\n\n";

        try
        {
            File.AppendAllText("app_errors.log", logMessage);
            Debug.WriteLine(logMessage); // 输出到调试窗口
        }
        catch
        {
            /* 防止日志记录本身抛出异常 */
        }
    }

    /// <summary>
    ///     显示用户友好的错误提示
    /// </summary>
    private static void ShowUserFriendlyError(Exception ex)
    {
        var message = ex switch
        {
            UnauthorizedAccessException => $"权限不足: {ex.Message}\n请检查文件/文件夹权限。",
            FileNotFoundException => $"文件未找到: {ex.Message}",
            IOException => $"文件读写错误: {ex.Message}",
            _ => $"发生错误: {(ex.InnerException ?? ex).Message}"
        };

        MessageBox.Show(
            message,
            "错误",
            MessageBoxButton.OK,
            MessageBoxImage.Error
        );
    }

    /// <summary>
    ///     显示致命错误并退出
    /// </summary>
    private static void ShowFatalError(Exception ex)
    {
        MessageBox.Show(
            $"程序即将关闭，原因:\n{ex.Message}\n\n详细信息已记录到日志。",
            "致命错误",
            MessageBoxButton.OK,
            MessageBoxImage.Stop
        );
    }

    protected override void OnExit(ExitEventArgs e)
    {
        // 清理事件订阅
        AppDomain.CurrentDomain.UnhandledException -= OnUnhandledException;
        DispatcherUnhandledException -= OnDispatcherUnhandledException;
        TaskScheduler.UnobservedTaskException -= OnUnobservedTaskException;

        base.OnExit(e);
    }
}
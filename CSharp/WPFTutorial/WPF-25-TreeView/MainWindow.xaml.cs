using System.IO;
using System.Windows;
using System.Windows.Controls;

namespace WPF_25_TreeView;

/// <summary>
///     Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow
{
    public MainWindow()
    {
        InitializeComponent();
    }

    private void MainWindow_OnLoaded(object sender, RoutedEventArgs e)
    {
        // 初始化驱动器名称（文件夹）
        foreach (var drive in Directory.GetLogicalDrives())
        {
            var treeView = new TreeViewItem
            {
                Header = drive, Tag = drive
            };

            treeView.Items.Add(null);
            treeView.Expanded += FolderExpanded;
            FolderView.Items.Add(treeView);
        }
    }

    private static void FolderExpanded(object sender, RoutedEventArgs e)
    {
        var parentItem = (TreeViewItem)sender;

        switch (parentItem.Items.Count)
        {
            // 改进的空检查逻辑
            case 1 when parentItem.Items[0] == null:
                parentItem.Items.Clear();
                break;
            case > 0:
                return; // 已经加载过子项
        }

        var fullPath = parentItem.Tag?.ToString();
        if (string.IsNullOrEmpty(fullPath) || !Directory.Exists(fullPath)) return;

        // 添加搜索选项以包含隐藏文件夹（可选）
        var directories = Directory.GetDirectories(fullPath, "*", SearchOption.TopDirectoryOnly);

        foreach (var directoryPath in directories)
        {
            // 跳过无法访问的文件夹
            if (!HasAccess(directoryPath)) continue;

            var subViewItem = new TreeViewItem
            {
                Header = Path.GetFileName(directoryPath),
                Tag = directoryPath
            };

            subViewItem.Items.Add(null); // 添加虚拟子项以显示展开箭头
            subViewItem.Expanded += FolderExpanded;
            parentItem.Items.Add(subViewItem);
        }

        // 添加文件
        var files = Directory.GetFiles(fullPath);
        foreach (var filePath in files)
        {
            var fileItem = new TreeViewItem
            {
                Header = Path.GetFileName(filePath),
                Tag = filePath
            };
            // 文件没有子项，所以不需要添加虚拟项
            parentItem.Items.Add(fileItem);
        }
    }

    /// <summary>
    ///     辅助方法检查目录访问权限
    /// </summary>
    /// <param name="folderPath">文件夹路径</param>
    /// <returns></returns>
    private static bool HasAccess(string folderPath)
    {
        try
        {
            Directory.GetFileSystemEntries(folderPath);
            return true;
        }
        catch
        {
            return false;
        }
    }
}
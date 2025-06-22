using System.Globalization;
using System.IO;
using System.Windows.Data;
using System.Windows.Media.Imaging;

namespace WPF_25_TreeView.Convert;

[ValueConversion(typeof(string), typeof(BitmapImage))]
public class HeaderToImageConverter : IValueConverter
{
    public static readonly HeaderToImageConverter Instance = new();

    public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        var path = (string)value!;
        if (string.IsNullOrEmpty(path)) return null;

        // 判断是否是文件夹
        //  // 方法1：直接使用 Directory.Exists（推荐大多数情况）
        // var isDirectory = Directory.Exists(path);
        // // 方法2：检查文件属性（处理符号链接等特殊情况）
        // var isDirectory = File.GetAttributes(path).HasFlag(FileAttributes.Directory);
        var isDirectory = Directory.Exists(path) ||
                          (File.GetAttributes(path) & FileAttributes.Directory) == FileAttributes.Directory;

        var extension = Path.GetExtension(path).ToLower();
        var image = isDirectory ? "Assets/folder.png" :
            extension is ".jpg" or ".jpeg" ? "Assets/jpg.png" :
            extension is ".java" ? "Assets/code.ico" :
            extension is ".ogg" ? "Assets/ogg.ico" :
            "Assets/file.png";

        return new BitmapImage(new Uri($"pack://application:,,,/{image}"));
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        return new object();
    }
}
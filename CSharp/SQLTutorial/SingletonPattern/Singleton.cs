namespace SingletonPattern;

public class Singleton
{
    private static readonly Lazy<Singleton> LazyInstance = new(() => new Singleton());

    private Singleton()
    {
    }

    public static Singleton Instance => LazyInstance.Value;

    public void SomeMethod()
    {
        Console.WriteLine("Singleton method called");
    }
}
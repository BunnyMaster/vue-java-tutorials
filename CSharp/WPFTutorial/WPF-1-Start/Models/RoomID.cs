namespace WPF_1_Start.Models;

public class RoomId
{
    public int? FloorNumber { get; }

    public int? RoomNumber { get; }

    public override bool Equals(object? obj)
    {
        return obj is RoomId roomId && FloorNumber == roomId.FloorNumber && RoomNumber == roomId.RoomNumber;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(FloorNumber, RoomNumber);
    }

    public override string ToString()
    {
        return $"{FloorNumber}{RoomNumber}";
    }
}
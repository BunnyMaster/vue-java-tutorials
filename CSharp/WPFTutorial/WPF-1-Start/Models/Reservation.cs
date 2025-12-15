namespace WPF_1_Start.Models;

public class Reservation
{
    public Reservation(RoomId roomId, string? username, DateTime startTime, DateTime endTime)
    {
        RoomId = roomId;
        Username = username;
        StartTime = startTime;
        EndTime = endTime;
    }

    public RoomId RoomId { get; set; }
    public string? Username { get; }
    public DateTime StartTime { get; set; }
    public DateTime EndTime { get; set; }
    public TimeSpan Length => EndTime.Subtract(StartTime);

    public bool Conflicts(Reservation reservation)
    {
        if (!reservation.RoomId.Equals(RoomId)) return false;
        return reservation.StartTime < EndTime && reservation.EndTime > StartTime;
    }
}
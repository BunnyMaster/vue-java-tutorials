using WPF_1_Start.Config;

namespace WPF_1_Start.Models;

public class ReservationBook
{
    private readonly List<Reservation> _reservations = [];

    public IEnumerable<Reservation> GetReservations(string username)
    {
        return _reservations.Where(r => r.Username!.Equals(username));
    }

    public void AddReservation(Reservation reservation)
    {
        foreach (var existingReservation in _reservations.Where(existingReservation =>
                     existingReservation.Conflicts(reservation)))
            throw new ReservationException(existingReservation, reservation);

        _reservations.Add(reservation);
    }
}
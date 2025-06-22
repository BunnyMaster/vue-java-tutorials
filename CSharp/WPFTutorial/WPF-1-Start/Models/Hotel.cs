namespace WPF_1_Start.Models;

public class Hotel
{
    private readonly ReservationBook _reservationBook;

    public Hotel(string name)
    {
        Name = name;
        _reservationBook = new ReservationBook();
    }

    public string Name { get; set; }

    public IEnumerable<Reservation> GetReservationsForUser(string username)
    {
        return _reservationBook.GetReservations(username);
    }

    public void MakeReservation(Reservation reservation)
    {
        _reservationBook.AddReservation(reservation);
    }
}
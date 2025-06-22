using System.Runtime.Serialization;
using WPF_1_Start.Models;

namespace WPF_1_Start.Config;

public class ReservationException : Exception
{
    public ReservationException(Reservation existingReservation, Reservation incomingReservation)
    {
        ExistingReservation = existingReservation;
        IncomingReservation = incomingReservation;
    }

    protected ReservationException(SerializationInfo info, StreamingContext context, Reservation existingReservation,
        Reservation incomingReservation) : base(info, context)
    {
        ExistingReservation = existingReservation;
        IncomingReservation = incomingReservation;
    }

    public ReservationException(string? message, Reservation existingReservation, Reservation incomingReservation) :
        base(message)
    {
        ExistingReservation = existingReservation;
        IncomingReservation = incomingReservation;
    }

    public ReservationException(string? message, Exception? innerException, Reservation existingReservation,
        Reservation incomingReservation) : base(message, innerException)
    {
        ExistingReservation = existingReservation;
        IncomingReservation = incomingReservation;
    }

    public Reservation ExistingReservation { get; }
    public Reservation IncomingReservation { get; }
}
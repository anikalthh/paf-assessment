package vttp2023.batch4.paf.assessment.repositories;

public class Queries {
    
    public static final String SQL_INSERT_NEW_USER = """
        INSERT INTO users(email, name)
        VALUE (?, ?);
    """;

    public static final String SQL_USER_COUNT = """
        SELECT COUNT(*) as count
        FROM users
        WHERE email LIKE ?;
    """;

    public static final String SQL_INSERT_NEW_BOOKING = """
        INSERT INTO bookings(booking_id, listing_id, duration, email)
        VALUES (?, ?, ?, ?);
    """;
}

package vttp2023.batch4.paf.assessment.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.exception.BookingException;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;

@Repository
public class BookingsRepository {
	
	// You may add additional dependency injections

	public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like ?";

	@Autowired
	private JdbcTemplate template;

	// You may use this method in your task
	public Optional<User> userExists(String email) {
		SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
		if (!rs.next())
			return Optional.empty();

		return Optional.of(new User(rs.getString("email"), rs.getString("name")));
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newUser(User user) throws BookingException {

		if (userExists(user.email()).isEmpty()) {
			int updateCount = template.update(Queries.SQL_INSERT_NEW_USER, user.email(), user.name());

			if (updateCount != 1) {
				throw new BookingException();
			} 
		}
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newBookings(Bookings bookings) throws BookingException {
		int updateCount = template.update(Queries.SQL_INSERT_NEW_BOOKING, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());

		if (updateCount != 1) {
			throw new BookingException();
		} 
	}

	// public Boolean userExists(User user) {
	// 	SqlRowSet rs = template.queryForRowSet(Queries.SQL_USER_COUNT, user.email());

	// 	rs.next();
	// 	int result = rs.getInt("count");

	// 	return (result == 1) ? true : false;
	// }
}

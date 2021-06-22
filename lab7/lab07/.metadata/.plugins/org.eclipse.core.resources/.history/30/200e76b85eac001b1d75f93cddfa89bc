package isp_app_pack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import isp_app_pack.model.Charge;
import isp_app_pack.model.Installation;
import isp_app_pack.model.Payment;

public class PaymentDao implements Dao<Payment> {
	private Connection conn = null;
	private ChargeDao chargeDao;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public PaymentDao(ChargeDao chDao) {
		chargeDao = chDao;
		String sql = "SELECT COUNT(*) FROM payments;";
		String sql2 = "SELECT * FROM payments ORDER BY payment_id DESC LIMIT 1;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
				Payment.nextPaymentId = rs.getLong(1) + 1;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void createTable() {
		String sql = "CREATE TABLE payments"
				+ "(payment_id INTEGER PRIMARY KEY,"
				+ "charge_id INTEGER NOT NULL," + "date TEXT NOT NULL,"
				+ "amount REAL NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'payments' created!\n");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void dropTable() {
		String sql = "DROP TABLE payments;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'payments' droped!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	private void connect() throws SQLException {
		if (conn != null)
			return;
		String url = "jdbc:sqlite:test.db";
		conn = DriverManager.getConnection(url);
	}

	private void disconnect() throws SQLException {
		if (conn == null)
			return;
		conn.close();
		conn = null;
	}

	@Override
	public Payment get(long id) {
		String sql = "SELECT charge_id, date, amount FROM payments WHERE payment_id=?";
		Payment payment = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				payment = new Payment(id,
						chargeDao.get(rs.getLong("charge_id")),
						LocalDate.parse(rs.getString("date")),
						rs.getDouble("amount"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return payment;
	}

	public List<Payment> getAll(long chargeId) {
		String sql = "SELECT payment_id, charge_id, date, amount FROM payments WHERE charge_id=?";
		List<Payment> list = new ArrayList<Payment>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Payment(rs.getLong("payment_id"),
						chargeDao.get(rs.getLong("charge_id")),
						LocalDate.parse(rs.getString("date")),
						rs.getDouble("amount")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	@Override
	public List<Payment> getAll() {
		String sql = "SELECT payment_id, charge_id, date, amount FROM payments";
		List<Payment> list = new ArrayList<Payment>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Payment(rs.getLong("payment_id"),
						chargeDao.get(rs.getLong("charge_id")),
						LocalDate.parse(rs.getString("date")),
						rs.getDouble("amount")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	@Override
	public boolean add(Payment payment) {
		boolean isgood = false;
		String sql = "INSERT INTO payments(payment_id, charge_id, date, amount) VALUES (?,?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, payment.getId());
			pstmt.setLong(2, payment.getCharge().getId());
			pstmt.setString(3, payment.getDate().toString());
			pstmt.setDouble(4, payment.getAmount());
			pstmt.executeUpdate();
			pstmt.close();
			isgood = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return isgood;
	}

	@Override
	public boolean update(Payment payment) {
		boolean isgood = false;
		String sql = "UPDATE payments SET charge_id = ?, date = ?, "
				+ "amount = ? " + "WHERE payment_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, payment.getCharge().getId());
			pstmt.setString(2, payment.getDate().toString());
			pstmt.setDouble(3, payment.getAmount());
			pstmt.setLong(4, payment.getId());
			pstmt.executeUpdate();
			pstmt.close();
			isgood = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return isgood;
	}

	@Override
	public boolean delete(long id) {
		boolean isgood = false;
		String sql = "DELETE FROM payments WHERE payment_id = ?";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			isgood = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return isgood;
	}

	@Override
	public Object[] getColumnsIdentifiers() {
		return new Object[] { "payment_id", "charge_id", "date", "amount" };
	}

	@Override
	public Object[] instanceToTable(Payment t) {
		return new Object[] { Long.toString(t.getId()),
				Long.toString(t.getCharge().getId()), t.getDate().toString(),
				Double.toString(t.getAmount()) };
	}

	@Override
	public Payment tableToInstance(Object[] t) {
		return new Payment(Long.parseLong((String) t[0]),
				chargeDao.get(Long.parseLong((String) t[1])),
				LocalDate.parse((String) t[2]),
				Double.parseDouble((String) t[3]));
	}

	@Override
	public long getNextId() {
		return Payment.getNextId();
	}
}

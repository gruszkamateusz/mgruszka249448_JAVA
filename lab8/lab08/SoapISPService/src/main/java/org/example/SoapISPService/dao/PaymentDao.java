package org.example.SoapISPService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.SoapISPService.model.Payment;

public class PaymentDao {
	static private Connection conn = null;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	static public void createTable() {
		String sql = "CREATE TABLE payments"
				+ "(payment_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "charge_id INTEGER NOT NULL," + "date TEXT NOT NULL,"
				+ "amount REAL NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'payments' created!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	static public void dropTable() {
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

	static private void connect() throws SQLException {
		if (conn != null)
			return;
		String url = "jdbc:sqlite:test.db";
		conn = DriverManager.getConnection(url);
	}

	static private void disconnect() throws SQLException {
		if (conn == null)
			return;
		conn.close();
		conn = null;
	}

	static public Payment get(long id) {
		String sql = "SELECT charge_id, date, amount FROM payments WHERE payment_id=?";
		Payment payment = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				payment = new Payment(id,
						rs.getLong("charge_id"),
						rs.getString("date"),
						rs.getDouble("amount"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return payment;
	}

	static public List<Payment> getAll(long chargeId) {
		String sql = "SELECT payment_id, charge_id, date, amount FROM payments WHERE charge_id=?";
		List<Payment> list = new ArrayList<Payment>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Payment(rs.getLong("payment_id"),
						rs.getLong("charge_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public List<Payment> getAll() {
		String sql = "SELECT payment_id, charge_id, date, amount FROM payments";
		List<Payment> list = new ArrayList<Payment>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Payment(rs.getLong("payment_id"),
						rs.getLong("charge_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	static public List<Payment> getAllOfChargePayments(long chargeId) {
		String sql = "SELECT payment_id, charge_id, date, amount FROM payments "
					+ "WHERE charge_id = ?";
		List<Payment> list = new ArrayList<Payment>();
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chargeId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Payment(rs.getLong("payment_id"),
						rs.getLong("charge_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public long getNumOfChargePayments(long chargeId) {
		String sql = "SELECT COUNT(*) FROM payments WHERE charge_id = ?";
		long pay_num = 0;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chargeId);
			ResultSet rs = pstmt.executeQuery();
			pay_num = rs.getLong(1);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return pay_num;
	}
	
	static public long insert(Payment payment) {
		long paymentId = -1;
		String sql = "INSERT INTO payments(charge_id, date, amount) VALUES (?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, payment.getChargeId());
			pstmt.setString(2, payment.getDate());
			pstmt.setDouble(3, payment.getAmount());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
            	paymentId = rs.getLong(1);
            }
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return paymentId;
	}

	static public boolean update(Payment payment) {
		boolean isgood = false;
		String sql = "UPDATE payments SET charge_id = ?, date = ?, "
				+ "amount = ? " + "WHERE payment_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, payment.getChargeId());
			pstmt.setString(2, payment.getDate());
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

	static public int delete(long id) {
		int rc = 0;
		String sql = "DELETE FROM payments WHERE payment_id = ?";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			rc |= 2;
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return rc;
	}
}

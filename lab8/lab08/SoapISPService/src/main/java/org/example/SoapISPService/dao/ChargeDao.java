package org.example.SoapISPService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.SoapISPService.model.Charge;

public class ChargeDao {
	static private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	static public void createTable() {
		String sql = "CREATE TABLE charges" + 
					 "(charge_id INTEGER PRIMARY KEY AUTOINCREMENT," + 
					 "install_id INTEGER NOT NULL," + 
					 "date TEXT NOT NULL," + 
					 "amount REAL NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'charges' created!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	static public void dropTable() {
		String sql = "DROP TABLE charges;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'charges' droped!\n");
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

	static public Charge get(long id) {
		String sql = "SELECT install_id, date, amount FROM charges WHERE charge_id=?";
		Charge charge = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				charge = new Charge(id,
						rs.getLong("install_id"),
						rs.getString("date"),
						rs.getDouble("amount"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return charge;
	}
	
	static public List<Charge> getAll() {
		String sql = "SELECT charge_id, install_id, date, amount FROM charges";
		List<Charge> list = new ArrayList<Charge>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Charge(rs.getLong("charge_id"),
						rs.getLong("install_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public List<Charge> getAllOfInstallCharges(long installId) {
		String sql = "SELECT charge_id, install_id, date, amount FROM charges " + 
					 "WHERE install_id = ?";
		List<Charge> list = new ArrayList<Charge>();
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, installId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Charge(rs.getLong("charge_id"),
						rs.getLong("install_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public List<Charge> getChargesOfClient(long clientId, 
			LocalDate beginDate, LocalDate endDate) {
		System.out.println(clientId + " " + beginDate.toString() + " " + endDate.toString());
		String sql = "SELECT charges.charge_id, charges.install_id, "
				+ "charges.date, charges.amount "
				+ "FROM charges INNER JOIN installations "
				+ "ON charges.install_id = installations.install_id "
				+ "INNER JOIN clients "
				+ "ON clients.client_id = installations.client_id "
				+ "WHERE clients.client_id = ? "
				+ "AND (DATE(charges.date) BETWEEN DATE(?) AND DATE(?))";
		List<Charge> list = new ArrayList<Charge>();
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, clientId);
			pstmt.setString(2, beginDate.toString());
			pstmt.setString(3, endDate.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Charge(rs.getLong("charge_id"),
						rs.getLong("install_id"),
						rs.getString("date"),
						rs.getDouble("amount")));
			for(int i = 0; i < list.size(); ++i)
				System.out.println(list.get(i).toString());
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public long getNumOfInstallCharges(long installId) {
		String sql = "SELECT COUNT(*) FROM charges WHERE install_id = ?";
		long ch_num = 0;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, installId);
			ResultSet rs = pstmt.executeQuery();
			ch_num = rs.getLong(1);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return ch_num;
	}
	
	static public long insert(Charge charge) {
		long chargeId = -1;
		String sql = "INSERT INTO charges(install_id, date, amount) "
				+ "VALUES (?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, charge.getInstallationId());
			pstmt.setString(2, charge.getDate());
			pstmt.setDouble(3, charge.getAmount());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				chargeId = rs.getInt(1);
            }			
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return chargeId;
	}

	static public boolean update(Charge charge) {
		boolean isgood = false;
		String sql = "UPDATE charges SET install_id = ?, " + "date = ?, "
				+ "amount = ? " + "WHERE charge_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, charge.getInstallationId());
			pstmt.setString(2, charge.getDate());
			pstmt.setDouble(3, charge.getAmount());
			pstmt.setLong(4, charge.getId());
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
		String sql = "DELETE FROM charges WHERE charge_id = ?";
		try {
			connect();
			long pay_num = PaymentDao.getNumOfChargePayments(id);
			if(pay_num == 0) {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				pstmt.close();
			} else {//Please delete all payments of the charge first!
				rc = 1;
			}
		} catch (SQLException e) {
			rc |= 2;
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return rc;
	}
}

package org.example.SoapISPService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.SoapISPService.model.Tariff;

public class TariffDao {
	static private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	static public void createTable() {
		String sql = "CREATE TABLE tariffs" + 
					 "(tariff_id INTEGER PRIMARY KEY AUTOINCREMENT," + 
				     "service TEXT NULL," + 
					 "price INTEGER NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'tariffs' created!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	static public void dropTable() {
		String sql = "DROP TABLE tariffs;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'tariffs' droped!\n");
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

	static public Tariff get(long id) {
		String sql = "SELECT service, price FROM tariffs WHERE tariff_id=?";
		Tariff tariff = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				tariff = new Tariff(id, 
						rs.getString("service"),
						rs.getDouble("price"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return tariff;
	}

	static public List<Tariff> getAll() {
		String sql = "SELECT tariff_id, service, price FROM tariffs";
		List<Tariff> list = new ArrayList<Tariff>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Tariff(rs.getLong("tariff_id"), 
						rs.getString("service"),
						rs.getDouble("price")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	static public long insert(Tariff tariff) {
		long tariffId = -1;
		String sql = "INSERT INTO tariffs(service, price) VALUES (?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tariff.getService());
			pstmt.setDouble(2, tariff.getPrice());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
            	tariffId = rs.getInt(1);
            }
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return tariffId;
	}

	static public boolean update(Tariff tariff) {
		boolean isgood = false;
		String sql = "UPDATE tariffs SET service = ?, " + "price = ? " + "WHERE tariff_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tariff.getService());
			pstmt.setDouble(2, tariff.getPrice());
			pstmt.setLong(3, tariff.getId());
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
		String sql = "DELETE FROM tariffs WHERE tariff_id = ?";
		try {
			connect();
			long ins_num = ChargeDao.getNumOfInstallCharges(id);
			if(ins_num == 0) {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				pstmt.close();
			} else {//Please delete all charges that use the tariff.
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

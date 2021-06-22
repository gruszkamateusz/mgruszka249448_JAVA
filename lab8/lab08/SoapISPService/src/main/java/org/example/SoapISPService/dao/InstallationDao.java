package org.example.SoapISPService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.SoapISPService.model.Installation;

public class InstallationDao {
	static private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	static public void createTable() {
		String sql = "CREATE TABLE installations"
				+ "(install_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "client_id INTEGER NOT NULL," + "address TEXT NOT NULL,"
				+ "tariff_id INTEGER NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'installations' created!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	static public void dropTable() {
		String sql = "DROP TABLE installations;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'installations' droped!\n");
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

	static public Installation get(long id) {
		String sql = "SELECT client_id, address, tariff_id FROM "
				+ "installations WHERE install_id=?";
		Installation install = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				install = new Installation(id,
						rs.getLong("client_id"),
						rs.getString("address"),
						rs.getLong("tariff_id"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return install;
	}

	static public List<Installation> getAll() {
		String sql = "SELECT install_id, client_id, address, tariff_id FROM installations";
		List<Installation> list = new ArrayList<Installation>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Installation(rs.getLong("install_id"),
						rs.getLong("client_id"),
						rs.getString("address"),
						rs.getLong("tariff_id")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	static public List<Installation> getAllOfClientInstalls(long clientId) {
		String sql = "SELECT install_id, client_id, address, tariff_id " + 
					 "FROM installations WHERE client_id = ?";
		List<Installation> list = new ArrayList<Installation>();
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, clientId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Installation(rs.getLong("install_id"),
						rs.getLong("client_id"),
						rs.getString("address"),
						rs.getLong("tariff_id")));
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}
	
	static public long getNumOfClientInstalls(long clientId) {
		String sql = "SELECT COUNT(*) FROM installations WHERE client_id = ?";
		long ins_num = 0;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, clientId);
			ResultSet rs = pstmt.executeQuery();
			ins_num = rs.getLong(1);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return ins_num;
	}
	
	static public long insert(Installation install) {
		long installationId = -1;
		String sql = "INSERT INTO installations(client_id, address, tariff_id) "
				+ "VALUES (?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, install.getClientId());
			pstmt.setString(2, install.getAddress());
			pstmt.setLong(3, install.getTariffId());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
            	installationId = rs.getLong(1);
            }
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return installationId;
	}
	
	static public boolean update(Installation install) {
		boolean isgood = false;
		String sql = "UPDATE installations SET client_id = ?, tariff_id = ?, "
				+ "address = ? " + "WHERE install_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, install.getClientId());
			pstmt.setLong(2, install.getTariffId());
			pstmt.setString(3, install.getAddress());
			pstmt.setLong(4, install.getId());
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
		String sql = "DELETE FROM installations WHERE install_id = ?";
		try {
			connect();
			long ins_num = ChargeDao.getNumOfInstallCharges(id);
			if(ins_num == 0) {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				pstmt.close();
			} else {//Please delete all charges of the installation first!
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

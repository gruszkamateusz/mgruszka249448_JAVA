package org.example.SoapISPService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.SoapISPService.model.Client;

public class ClientDao {
	static private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	static public void createTable() {
		String sql = "CREATE TABLE clients" + 
					"(client_id INTEGER PRIMARY KEY AUTOINCREMENT," + 
					"firstname TEXT NOT NULL," + 
					"name TEXT NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'clients' created!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	static public void dropTable() {
		String sql = "DROP TABLE clients;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'clients' droped!\n");
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

	static public Client get(long id) {
		String sql = "SELECT firstname, name FROM clients WHERE client_id=?";
		Client client = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				client = new Client(id, rs.getString("firstname"),
						rs.getString("name"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return client;
	}

	static public List<Client> getAll() {
		String sql = "SELECT client_id, firstname, name FROM clients";
		List<Client> list = new ArrayList<Client>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(new Client(rs.getLong("client_id"),
						rs.getString("firstname"), rs.getString("name")));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	static public long insert(Client client) {
		long clientId = -1;
		String sql = "INSERT INTO clients(firstname, name) VALUES (?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getName());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
            	clientId = rs.getLong(1);
            }
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return clientId;
	}

	static public boolean update(Client client) {
		boolean isgood = false;
		String sql = "UPDATE clients SET firstname = ?, " + "name = ? "
				+ "WHERE client_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getName());
			pstmt.setLong(3, client.getId());
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
		String sql = "DELETE FROM clients WHERE client_id = ?";
		try {
			connect();
			long inst_num = InstallationDao.getNumOfClientInstalls(id);
			if(inst_num == 0) {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				pstmt.close();
			} else {//Please delete all installations of the client first!
				rc = 1;
			}
		} catch (SQLException e) {
			rc |= 2;
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return rc;
	}
	
	static public boolean isDatabaseEmpty() {
		String sql = "SELECT COUNT(*) FROM clients;";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}

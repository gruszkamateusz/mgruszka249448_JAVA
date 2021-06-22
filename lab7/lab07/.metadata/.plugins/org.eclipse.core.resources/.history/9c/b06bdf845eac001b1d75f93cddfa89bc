package isp_app_pack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import isp_app_pack.model.Client;

public class ClientDao implements Dao<Client> {
	private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public ClientDao() {
		String sql = "SELECT COUNT(*) FROM clients;";
		String sql2 = "SELECT * FROM clients ORDER BY client_id DESC LIMIT 1;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
				Client.nextClientId = rs.getLong(1) + 1;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void createTable() {
		String sql = "CREATE TABLE clients" + "(client_id INTEGER PRIMARY KEY,"
				+ "firstname TEXT NOT NULL," + "name TEXT NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'clients' created!\n");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void dropTable() {
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
	public Client get(long id) {
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

	@Override
	public List<Client> getAll() {
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

	@Override
	public boolean add(Client client) {
		boolean isgood = false;
		String sql = "INSERT INTO clients(client_id, firstname, name) VALUES (?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, client.getId());
			pstmt.setString(2, client.getFirstName());
			pstmt.setString(3, client.getName());
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
	public boolean update(Client client) {
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

	@Override
	public boolean delete(long id) {
		boolean isgood = false;
		String sql1 = "SELECT COUNT(*) FROM installations WHERE client_id = ?";
		String sql2 = "DELETE FROM clients WHERE client_id = ?";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			long ins_num = rs.getLong(1);
			if(ins_num == 0) {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
				isgood = true;
			} else {
				JOptionPane.showMessageDialog(null, 
						"Please delete all installations of the client first!",
						"Delete error", JOptionPane.ERROR_MESSAGE);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return isgood;
	}

	@Override
	public Object[] getColumnsIdentifiers() {
		return new Object[] { "client_id", "firstname", "name" };
	}

	@Override
	public Object[] instanceToTable(Client cl) {
		return new Object[] { Long.toString(cl.getId()), cl.getFirstName(),
				cl.getName() };
	}

	@Override
	public Client tableToInstance(Object[] t) {
		return new Client(Long.parseLong((String) t[0]),
				(String) t[1], (String) t[2]);
	}

	@Override
	public long getNextId() {
		return Client.getNextId();
	}
	
	public boolean dbIsEmpty() {
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

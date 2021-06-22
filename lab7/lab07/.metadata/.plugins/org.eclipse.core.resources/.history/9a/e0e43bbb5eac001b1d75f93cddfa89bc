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

import isp_app_pack.model.Tariff;

public class TariffDao implements Dao<Tariff> {
	private Connection conn = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public TariffDao() {
		String sql = "SELECT COUNT(*) FROM tariffs;";
		String sql2 = "SELECT * FROM tariffs ORDER BY tariff_id DESC LIMIT 1;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
				Tariff.nextTariffId = rs.getInt(1) + 1;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void createTable() {
		String sql = "CREATE TABLE tariffs" + 
					 "(tariff_id INTEGER PRIMARY KEY," + 
				     "service TEXT NULL," + 
					 "price INTEGER NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'tariffs' created!\n");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void dropTable() {
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
	public Tariff get(long id) {
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

	@Override
	public List<Tariff> getAll() {
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

	@Override
	public boolean add(Tariff tariff) {
		boolean isgood = false;
		String sql = "INSERT INTO tariffs(tariff_id, service, price) VALUES (?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, tariff.getId());
			pstmt.setString(2, tariff.getService());
			pstmt.setDouble(3, tariff.getPrice());
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
	public boolean update(Tariff tariff) {
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

	@Override
	public boolean delete(long id) {
		boolean isgood = false;
		String sql1 = "SELECT COUNT(*) FROM installations WHERE tariff_id = ?";
		String sql2 = "DELETE FROM tariffs WHERE tariff_id = ?";
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
						"Please delete all installations that use the tariff first!",
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
		return new Object[] { "tariff_id", "service", "price" };
	}

	@Override
	public Object[] instanceToTable(Tariff t) {
		return new Object[] { Long.toString(t.getId()), 
							  t.getService(), 
							  Double.toString(t.getPrice()) };
	}

	@Override
	public Tariff tableToInstance(Object[] t) {
		return new Tariff(Long.parseLong((String)t[0]), 
				(String)t[1], 
				Double.parseDouble((String)t[2]));
	}

	@Override
	public long getNextId() {
		return Tariff.getNextId();
	}
}

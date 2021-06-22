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

import isp_app_pack.model.Installation;

public class InstallationDao implements Dao<Installation> {
	private Connection conn = null;
	private ClientDao clientDao;
	private TariffDao tariffDao;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public InstallationDao(ClientDao clDao, TariffDao tarDao) {
		String sql = "SELECT COUNT(*) FROM installations;";
		String sql2 = "SELECT * FROM installations ORDER BY install_id DESC LIMIT 1;";
		this.clientDao = clDao;
		this.tariffDao = tarDao;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
				Installation.nextRouterId = rs.getLong(1) + 1;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void createTable() {
		String sql = "CREATE TABLE installations"
				+ "(install_id INTEGER PRIMARY KEY,"
				+ "client_id INTEGER NOT NULL," + "address TEXT NOT NULL,"
				+ "tariff_id INTEGER NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'installations' created!\n");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void dropTable() {
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
	public Installation get(long id) {
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
						clientDao.get(rs.getLong("client_id")),
						rs.getString("address"),
						tariffDao.get(rs.getLong("tariff_id")));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return install;
	}

	@Override
	public List<Installation> getAll() {
		String sql = "SELECT install_id, client_id, address, tariff_id FROM installations";
		List<Installation> list = new ArrayList<Installation>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Installation(rs.getLong("install_id"),
						clientDao.get(rs.getLong("client_id")),
						rs.getString("address"),
						tariffDao.get(rs.getLong("tariff_id"))));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return list;
	}

	@Override
	public boolean add(Installation install) {
		boolean isgood = false;
		String sql = "INSERT INTO installations(install_id, client_id, address, "
				+ "tariff_id) VALUES (?,?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, install.getId());
			pstmt.setLong(2, install.getClient().getId());
			pstmt.setString(3, install.getAddress());
			pstmt.setLong(4, install.getTariff().getId());
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
	public boolean update(Installation install) {
		boolean isgood = false;
		String sql = "UPDATE installations SET client_id = ?, tariff_id = ?, "
				+ "address = ? " + "WHERE install_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, install.getClient().getId());
			pstmt.setLong(2, install.getTariff().getId());
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

	@Override
	public boolean delete(long id) {
		boolean isgood = false;
		String sql1 = "SELECT COUNT(*) FROM charges WHERE install_id = ?";
		String sql2 = "DELETE FROM installations WHERE install_id = ?";
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
						"Please delete all charges of the installation first!",
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
		return new Object[] { "install_id", "client_id", "address",
				"tariff_id" };
	}

	@Override
	public Object[] instanceToTable(Installation t) {
		return new Object[] { Long.toString(t.getId()),
				Long.toString(t.getClient().getId()), t.getAddress(),
				Long.toString(t.getTariff().getId()) };
	}

	@Override
	public Installation tableToInstance(Object[] t) {
		return new Installation(Long.parseLong((String) t[0]),
				clientDao.get(Long.parseLong((String) t[1])), (String) t[2],
				tariffDao.get(Long.parseLong((String) t[3])));
	}

	@Override
	public long getNextId() {
		return Installation.getNextId();
	}
}

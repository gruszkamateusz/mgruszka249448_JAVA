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

import javax.swing.JOptionPane;

import isp_app_pack.model.Charge;
import isp_app_pack.model.Client;
import isp_app_pack.model.Payment;

public class ChargeDao implements Dao<Charge> {
	private Connection conn = null;
	private InstallationDao installationDao;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public ChargeDao(InstallationDao insDao) {
		installationDao = insDao;
		String sql = "SELECT COUNT(*) FROM charges;";
		String sql2 = "SELECT * FROM charges ORDER BY charge_id DESC LIMIT 1;";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getLong(1) > 0) {
				pstmt = conn.prepareStatement(sql2);
				rs = pstmt.executeQuery();
				Charge.nextChargeId = rs.getLong(1) + 1;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void createTable() {
		String sql = "CREATE TABLE charges" + "(charge_id INTEGER PRIMARY KEY,"
				+ "install_id INTEGER NOT NULL," + "date TEXT NOT NULL,"
				+ "amount REAL NOT NULL);";
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Table 'charges' created!\n");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
	}

	public void dropTable() {
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
	public Charge get(long id) {
		String sql = "SELECT install_id, date, amount FROM charges WHERE charge_id=?";
		Charge charge = null;
		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				charge = new Charge(id,
						installationDao.get(rs.getLong("install_id")),
						LocalDate.parse(rs.getString("date")),
						rs.getDouble("amount"));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try { disconnect();	} catch (SQLException e) { e.printStackTrace();	}
		return charge;
	}
	
	@Override
	public List<Charge> getAll() {
		String sql = "SELECT charge_id, install_id, date, amount FROM charges";
		List<Charge> list = new ArrayList<Charge>();
		try {
			connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(new Charge(rs.getLong("charge_id"),
						installationDao.get(rs.getLong("install_id")),
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
	public boolean add(Charge charge) {
		boolean isgood = false;
		String sql = "INSERT INTO charges(charge_id, install_id, date, amount) "
				+ "VALUES (?,?,?,?)";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, charge.getId());
			pstmt.setLong(2, charge.getInstallation().getId());
			pstmt.setString(3, charge.getDate().toString());
			pstmt.setDouble(4, charge.getAmount());
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
	public boolean update(Charge charge) {
		boolean isgood = false;
		String sql = "UPDATE charges SET install_id = ?, " + "date = ?, "
				+ "amount = ? " + "WHERE charge_id = ?";

		try {
			connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, charge.getInstallation().getId());
			pstmt.setString(2, charge.getDate().toString());
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

	@Override
	public boolean delete(long id) {
		boolean isgood = false;
		String sql1 = "SELECT COUNT(*) FROM payments WHERE charge_id = ?";
		String sql2 = "DELETE FROM charges WHERE charge_id = ?";
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
						"Please delete all payments of the charge first!",
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
		return new Object[] { "charge_id", "install_id", "date", "amount" };
	}

	@Override
	public Object[] instanceToTable(Charge ch) {
		return new Object[] { Long.toString(ch.getId()),
				Long.toString(ch.getInstallation().getId()),
				ch.getDate().toString(), Double.toString(ch.getAmount()) };
	}

	@Override
	public Charge tableToInstance(Object[] t) {
		return new Charge(Long.parseLong((String) t[0]),
				installationDao.get(Long.parseLong((String) t[1])),
				LocalDate.parse((String) t[2]),
				Double.parseDouble((String) t[3]));
	}

	@Override
	public long getNextId() {
		return Charge.getNextId();
	}
}

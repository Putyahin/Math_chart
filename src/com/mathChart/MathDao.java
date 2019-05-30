package com.mathChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Provides management of the database*/
public class MathDao {
	private Connection connection;
	
	public MathDao() {
		connection = DbUtil.getConnection();
	}

	/** Adds a row in the database*/
	public void addChart(int hash,String url) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into charts(hash,url) values (?, ?)");
			preparedStatement.setInt(1, hash);
			preparedStatement.setString(2, url);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Deletes a row in the database*/
	public void deleteChart(int hash) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("delete from charts where hash=?");
			preparedStatement.setInt(1, hash);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/** Updates a row in the database*/
	public void updateChart(int hash, String url) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("update charts set url=? where hash=?");
			preparedStatement.setString(1, url);
			preparedStatement.setInt(2, hash);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Gets all rows from the database*/
	public void getAllCharts() {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from charts");
			while (rs.next()) {
				System.out.println(rs.getInt("hash"));
				System.out.println(rs.getString("url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/** Gets a url by a hash from the database*/
	public String getUrl(int hash) {
		String url=null;
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("select * from charts where hash=?");
			preparedStatement.setInt(1, hash);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				url=rs.getString("url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return url;
	}
}

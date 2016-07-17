package edu.sofia.fmi.audiorec.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.sofia.fmi.audiorec.config.AppConfiguration;

public class MainConsole {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
		DataSource ds = ctx.getBean(DataSource.class);
		try {
			ResultSet rs = ds.getConnection().prepareStatement("SELECT SYSDATE FROM DUAL").executeQuery();
			while (rs.next()) {
				System.out.println(rs.getDate(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ctx.close();
		}
	}

}

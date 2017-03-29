package edu.ncsu.csc.itrust.controller.obgyn;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;

/*
 * Adapted from http://stackoverflow.com/questions/15074465/how-to-populate-hgraphicimage-value-with-image-content-from-database
 */
@WebServlet("/ultrasoundImageServlet")
public class ConvertImage extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long patientId = Long.valueOf(request.getParameter("id"));
		long visitId = Long.valueOf(request.getParameter("visitId"));
		int fetusId = Integer.valueOf(request.getParameter("fetus"));
		DataSource ds;
		try {
			Context ctx = new InitialContext();
			ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new ServletException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}

		try {
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("SELECT file, LENGTH(file) AS imageContentLength FROM ultrasounds WHERE id="
							+ patientId + " AND visitID=" + visitId + " AND fetus=" + fetusId);
			ResultSet results = stmt.executeQuery();
			String imgLen = "";
			if (results.next()) {
				imgLen = results.getString(1);
				System.out.println(imgLen.length());
			}
			results = stmt.executeQuery();
			if (results.next()) {
				int len = imgLen.length();
				byte[] rb = new byte[len];
				InputStream file = results.getBlob("file").getBinaryStream();
				int index = file.read(rb, 0, len);
				System.out.println("index" + index);
				stmt.close();
				//response.reset();
				response.setContentType("image/jpg");
				response.getOutputStream().write(rb, 0, len);
				response.getOutputStream().flush();
				//response.reset();
				//response.setContentType("image/jpg");

			}
		} catch (SQLException e) {
			throw new ServletException("Something failed at SQL/DB level.", e);
		}
	}

}
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;
	
	public  DepartmentDaoJDBC(Connection conn){
		this.conn = conn;
	}
	

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(
					"INSERT INTO department (Name) "
					+"VALUES (?)", Statement.RETURN_GENERATED_KEYS);
					
			st.setString(1, obj.getName());
			
			int rowsAffecteds = st.executeUpdate();
			
			if (rowsAffecteds > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if(rs.next()) {
					int id = rs.getInt(1);
					
					obj.setId(id);
					
					conn.commit();
				}
				
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro Inesperado, Nenhuma linha foi alterada");
			}
				
					
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				
				throw new DbException("Transa��o N�o Concluida Causada por "+e.getMessage());
				
			} catch (SQLException e1) {
				throw new DbException("Erro ao Tentar Realizar o rollback "+e.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE department SET Name = ? WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();		
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM department  WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();		
			
		}
		catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT department.Id,department.Name FROM department WHERE department.id = ? "
					);
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				return instantiateDepartment(rs);
			}
			
			return null;
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
				
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT Id,Name FROM department"
					);
			
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();			
			while(rs.next()) {
				
				Department dep = instantiateDepartment(rs);				
				
				list.add(dep);
			}
			
			return list;
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();

		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));

		return dep;
	}
	
	
	
}

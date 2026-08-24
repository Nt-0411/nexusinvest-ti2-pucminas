package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Metas;

public class MetasDao extends DAO {
	
	public MetasDao() {
		super();
		conectar();
	}
	
	public void finalize() {
		close();
	}
	
	public boolean insert(Metas meta) {
		boolean status = false;
		try {

			String sql = "INSERT INTO \"Metas\" (id_user, nome, \"valorTotal\", \"valorAlcancado\", descricao, status, \"dataPrazo\") "
		               + "VALUES (?, ?, ?, ?, ?, ?, ?);";
			
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, meta.getId_user());
			st.setString(2, meta.getNome());
			st.setDouble(3, meta.getValorTotal());
			st.setDouble(4, meta.getValorAlcancado());
			st.setString(5, meta.getDescricao());
			st.setBoolean(6, meta.isStatus());
			st.setDate(7, Date.valueOf(meta.getDataPrazo())); 
			
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	public Metas get(int id_meta) {
		Metas meta = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM \"Metas\" WHERE id_meta=" + id_meta;
			ResultSet rs = st.executeQuery(sql);	
			
	        if(rs.next()){            
	        	 meta = new Metas();
	        	 meta.setId_meta(rs.getInt("id_meta"));
	        	 meta.setId_user(rs.getInt("id_user"));
	        	 meta.setNome(rs.getString("nome"));
	        	 meta.setValorTotal(rs.getDouble("valorTotal"));
	        	 meta.setValorAlcancado(rs.getDouble("valorAlcancado"));
	        	 meta.setDescricao(rs.getString("descricao"));
	        	 meta.setStatus(rs.getBoolean("status"));
	        	 
	        	 
	        	 meta.setDataPrazo(rs.getDate("dataPrazo").toLocalDate());
	        	 meta.setDataCriacao(rs.getObject("dataCriacao", OffsetDateTime.class));
	        	 meta.setDataAtualizado(rs.getObject("dataAtualizado", OffsetDateTime.class));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return meta;
	}
	
	public List<Metas> get() {
		return get("");
	}

	public List<Metas> getOrderById() {
		return get("id_meta");		
	}
	
	public List<Metas> getOrderByNome() {
		return get("nome");		
	}
	
	public List<Metas> getOrderByValorTotal() {
		return get("\"valorTotal\"");		
	}
	
	private List<Metas> get(String orderBy) {
		List<Metas> listaMetas = new ArrayList<Metas>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM \"Metas\"" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	
			
	        while(rs.next()) {	            	
	        	 Metas meta = new Metas();
	        	 meta.setId_meta(rs.getInt("id_meta"));
	        	 meta.setId_user(rs.getInt("id_user"));
	        	 meta.setNome(rs.getString("nome"));
	        	 meta.setValorTotal(rs.getDouble("valorTotal"));
	        	 meta.setValorAlcancado(rs.getDouble("valorAlcancado"));
	        	 meta.setDescricao(rs.getString("descricao"));
	        	 meta.setStatus(rs.getBoolean("status"));
	        	 meta.setDataPrazo(rs.getDate("dataPrazo").toLocalDate());
	        	 meta.setDataCriacao(rs.getObject("dataCriacao", OffsetDateTime.class));
	        	 meta.setDataAtualizado(rs.getObject("dataAtualizado", OffsetDateTime.class));
	        	 
	             listaMetas.add(meta);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return listaMetas;
	}
	
	public boolean update(Metas meta) {
		boolean status = false;
		try {  
			String sql = "UPDATE \"Metas\" SET id_user = ?, nome = ?, \"valorTotal\" = ?, \"valorAlcancado\" = ?, "
					   + "descricao = ?, status = ?, \"dataPrazo\" = ?, \"dataAtualizado\" = ? WHERE id_meta = ?";
			
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, meta.getId_user());
			st.setString(2, meta.getNome());
			st.setDouble(3, meta.getValorTotal());
			st.setDouble(4, meta.getValorAlcancado());
			st.setString(5, meta.getDescricao());
			st.setBoolean(6, meta.isStatus());
			st.setDate(7, Date.valueOf(meta.getDataPrazo()));
			st.setObject(8, OffsetDateTime.now()); 
			st.setInt(9, meta.getId_meta());
			
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean delete(int id_meta) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM \"Metas\" WHERE id_meta = " + id_meta);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}
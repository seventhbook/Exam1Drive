package fes.aragon.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.primefaces.PrimeFaces;

import fes.aragon.modelo.Usuario;
import fes.aragon.utilerias.Utilerias;

public class Conexion {
	DataSource ds;
	public Conexion() throws NamingException {
		InitialContext contexto=new InitialContext();
		ds=(DataSource)contexto.lookup("jdbc/mysql3");
		
	}
	public Usuario existe(String usuario, String clave) throws Exception {
		Usuario datos=null;
		if (ds == null) {
			throw new Exception("No hay conexion a la base de datos");
		} else {
			Connection cnn = ds.getConnection();
			if (cnn == null) {
				throw new Exception("No hay conexion a la base de datos");
			} else {
				try {
					//Consultamos primero usuarios para ver que exista
					PreparedStatement loginQuery = cnn
							.prepareStatement("select u.id_usuario,usuario,rol,carpeta,nombre_archivo imagenes from usuario u left join "
									+ "archivos_usuario a on(u.id_usuario=a.id_usuario) where usuario=? and clave=?"
									+ " order by nombre_archivo");
					loginQuery.setString(1, usuario);
					loginQuery.setString(2, clave);
					ResultSet resultado = loginQuery.executeQuery();
					if (resultado.next()) {
						System.out.println(resultado.getString(2));
						datos=new Usuario();
						datos.setId(resultado.getString(1));
						datos.setUsuario(resultado.getString(2));
						datos.setRol(resultado.getString(3));
						datos.setCarpeta(resultado.getString(4));
						do {
							if(resultado.getString(5)!=null)
								datos.getArchivos().add(resultado.getString(5));
						}while(resultado.next());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}finally {
					cnn.close();
				}
			}
		}
		return datos;
	}
	
	public Usuario registro(String usuario, String clave, String rol) throws Exception {
		Usuario datos=null;
		if (ds == null) {
			throw new Exception("No hay conexion a la base de datos");
		} else {
			Connection cnn = ds.getConnection();
			if (cnn == null) {
				throw new Exception("No hay conexion a la base de datos");
			} else {
				try {
					PreparedStatement regisQuery = cnn
							.prepareStatement("INSERT INTO usuario values" //
									+ "(null,?,?,?,?)"); //(null para autoincrementar,usuario,clave,rol,carpeta)
					regisQuery.setString(1, usuario);
					regisQuery.setString(2, clave);
					regisQuery.setString(3,rol);
					regisQuery.setString(4,usuario);//la carpeta se llamara como el usuario	
					regisQuery.executeUpdate();
					
					//Al haber creado el usuario avisamos mediante consola
					System.out.println("Usuario Registrado con exito");
					
					//Creamos el usuario mandando a llamar el metodo existe
					datos=this.existe(usuario, clave);
					
				}catch(SQLIntegrityConstraintViolationException ex2){
					FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Usuario Existente"));
				}catch (Exception ex) {
					ex.printStackTrace();
				}finally {
					cnn.close();
				}
			}
		}
		return datos;
	}
	
	public Boolean registrarImagen(String id_usuario, String nombre) throws Exception {
		Boolean regis=false; //para registrar si ha sido registrado
		
		if (ds == null) {
			throw new Exception("No hay conexion a la base de datos");
		} else {
			Connection cnn = ds.getConnection();
			if (cnn == null) {
				throw new Exception("No hay conexion a la base de datos");
			} else {
				try {
					PreparedStatement regisQuery = cnn
							.prepareStatement("INSERT INTO archivos_usuario values" //
									+ "(null,?,?)"); //(null para autoincrementar,usuario,clave,rol,carpeta)
					regisQuery.setString(1, id_usuario);
					regisQuery.setString(2, nombre);
					regisQuery.executeUpdate();
					
					//Al haber creado el usuario avisamos mediante el growl
					System.out.println("Usuario con id: "+id_usuario+" ha subido la imagen "+nombre);
										
				}catch(SQLIntegrityConstraintViolationException ex2){
					FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Fallo por un constraint revisar id_usuario o nombreImagen"));
				}catch (Exception ex) {
					ex.printStackTrace();
					FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Fallo la subida de la imagen"));
				}finally {
					cnn.close();
				}
			}
		}
		return regis;
	}
	
}
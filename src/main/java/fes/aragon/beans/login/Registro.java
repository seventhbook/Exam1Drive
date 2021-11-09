package fes.aragon.beans.login;

import java.io.File;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

import org.primefaces.PrimeFaces;

import fes.aragon.conexion.Conexion;
import fes.aragon.modelo.Usuario;
import fes.aragon.utilerias.Utilerias;

@Named("datosRegis")
@RequestScoped
public class Registro implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Pattern(regexp="^\\w{1,13}$",message = "Usuario no valido, solo se aceptan números y letras")
	private String usuario;
	private String clave;
	private String claveAdmin="12345678as";
	private String rol;
	public Registro() {
		// TODO Auto-generated constructor stub
		this.usuario="";
		this.clave="";
		this.rol="";
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public void setRol(String rol) {
		if(rol.equals(claveAdmin)) {
			this.rol="ADM";
		}else {
			this.rol="USR";
		}
	}
	public String getRol() {
		return this.rol;
	}
	
	public String Registrar() {
		String ruta="Login";
		try {
			Conexion consulta=new Conexion();
			Usuario usr=consulta.registro(usuario, clave,rol);			
			if(usr!=null){
				String sesionNavegador = Utilerias.getHttpSession(false).getId();
				usr.setSesionNavegdor(sesionNavegador);
				Utilerias.setManagedBeanInSession("usuario",usr);
				
				//Creamos la carpeta del usuario en el sistema de archivos del servidor
				//Cada Carpeta se Creara dentro del directorio destino que debemos tener en C:
				//Cambiar destino o crar en C: la carpeta AppServers y dentro la carpeta temp
				File destino=new File("C:\\AppServers\\temp\\"+usr.getCarpeta()+"\\");
				if(!destino.exists()) {
					destino.mkdir();
					FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Carpeta Creada"));	
				}else {
					System.out.println("No se pudo crear la carpeta");
					FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("No se pudo crear la carpeta"));	
				}
				
				ruta="/xhtml/Principal.xhtml?faces-redirect=true";
			}else {
				System.out.println("No hay datos");
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("RegistroFallido"));
				PrimeFaces.current().executeScript("PF('dlgRegis').hide()");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Intente mas tarde"));
			PrimeFaces.current().executeScript("PF('dlgRegis').hide()");
		}
		
		return ruta;
	}
	
	
}

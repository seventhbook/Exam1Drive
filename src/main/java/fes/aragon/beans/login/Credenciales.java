package fes.aragon.beans.login;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

import fes.aragon.conexion.Conexion;
import fes.aragon.modelo.Usuario;
import fes.aragon.utilerias.Utilerias;

@Named("datosLogin")
@RequestScoped
public class Credenciales implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String usuario;
	private String clave;
	public Credenciales() {
		// TODO Auto-generated constructor stub
		this.usuario="";
		this.clave="";
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
	
	public String iniciarSesion() {
		String ruta="Login";
		try {
			Conexion consulta=new Conexion();
			Usuario usr=consulta.existe(usuario, clave);			
			if(usr!=null){
				String sesionNavegador = Utilerias.getHttpSession(false).getId();
				usr.setSesionNavegdor(sesionNavegador);
				Utilerias.setManagedBeanInSession("usuario",usr);
				
				ruta="/xhtml/Principal.xhtml?faces-redirect=true";
			}else {
				System.out.println("No hay datos");
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Credenciales no validas"));
				PrimeFaces.current().executeScript("PF('dlg').hide()");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Intente mas tarde"));
			PrimeFaces.current().executeScript("PF('dlg').hide()");
		}
		
		return ruta;
	}
	
	
}

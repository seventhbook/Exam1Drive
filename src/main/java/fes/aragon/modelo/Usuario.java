package fes.aragon.modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String usuario;
	private String clave;
	private String rol;
	private String carpeta;
	private List<String> archivos;
	private Map<String,StreamedContent> imagenes;
	private String sesionNavegdor;

	public Usuario() {
		// TODO Auto-generated constructor stub
		archivos=new ArrayList<String>();
		
	}
	public void rellenaImgs() {
		imagenes =new HashMap<String,StreamedContent>();
		String destino="C:\\AppServers\\temp\\";
		
		for(String i:this.archivos) {
			imagenes.put(i,
				DefaultStreamedContent.builder().contentType("image/jpeg")
				.name(i).stream(() -> {
					try {
						return new FileInputStream(destino+carpeta+"\\"+i);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}).build()
			);
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}
	
	public List<String> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<String> archivos) {
		this.archivos = archivos;
	}

	public String getSesionNavegdor() {
		return sesionNavegdor;
	}

	public void setSesionNavegdor(String sesionNavegdor) {
		this.sesionNavegdor = sesionNavegdor;
	}
	public String cerrarSesion() {		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}
	public Map<String, StreamedContent> getImagenes() {
		return imagenes;
	}
	public void setImagenes(Map<String, StreamedContent> imagenes) {
		this.imagenes = imagenes;
	}
	
	
}

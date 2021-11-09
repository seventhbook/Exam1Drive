package fes.aragon.paginas.etiquetas;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;


import javax.annotation.PostConstruct;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fes.aragon.modelo.Usuario;
import fes.aragon.utilerias.Utilerias;

@Named("tablaBean")
@RequestScoped
public class TablaBean {

	private StreamedContent imagen = null;
	private String destino = "C:\\AppServers\\temp\\";
	private StreamedContent bajar;
	private Usuario usr=null;
	private String nombre;

	// private StreamedContent imagen;
	@PostConstruct
	public void inicio() {
		usr = (Usuario) Utilerias.getManagedBean("usuario", Usuario.class);
		//ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	public TablaBean() {
		
	}
	
	public void setNombre(String nombre) {
		Utilerias.setManagedBeanInSession("archivo", nombre);
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void ponerN(String nombre) {
		Utilerias.setManagedBeanInSession("archivo", nombre);
		this.nombre = nombre;
	}

	public StreamedContent bajar(String nombre) {
		bajar = imagen = DefaultStreamedContent.builder().contentType("image/jpeg")
				.name(nombre).stream(() -> {
					try {
						return new FileInputStream(destino+usr.getCarpeta()+"\\"+nombre);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}).build();
		
		return bajar;
	}

	public StreamedContent getImagen() {
		this.nombre = (String) Utilerias.getManagedBean("archivo", String.class);
		final String nombre=this.nombre;
		if(nombre!=null) {
			imagen = DefaultStreamedContent.builder().contentType("image/jpeg")
					.stream(() -> {
						try {
							return new FileInputStream(destino+usr.getCarpeta()+"\\"+nombre);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}).build();
			return imagen;
		}
		return null;
	}
	
	
}

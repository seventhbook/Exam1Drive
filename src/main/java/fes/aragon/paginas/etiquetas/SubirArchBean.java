package fes.aragon.paginas.etiquetas;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.NamingException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import fes.aragon.conexion.Conexion;
import fes.aragon.modelo.Usuario;
import fes.aragon.utilerias.Utilerias;

@Named("subirArchBean")
@RequestScoped
public class SubirArchBean {

	private UploadedFile file;
	private StreamedContent imagen = null;
	private String destino = "C:\\AppServers\\temp\\";
	private String dato;
	private boolean descarga = false;
	private StreamedContent bajar;

	// private StreamedContent imagen;
	@PostConstruct
	public void inicio() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		dato = (String) Utilerias.getManagedBean("archivoSubido", String.class);
		if (dato == null) {
			dato = servletContext.getRealPath("") + "\\resources\\tema1\\img\\logofesaragon.png";
		} else {
			descarga = true;
		}
		imagen = DefaultStreamedContent.builder().contentType("image/jpeg").stream(() -> {
			try {
				return new FileInputStream(dato);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).build();
	}

	public SubirArchBean() {
	}

	public void archivoArriba(FileUploadEvent event) {
		String nombreArchivo;
		Usuario usr = (Usuario) Utilerias.getManagedBean("usuario", Usuario.class);
		try {
			this.copiar(event.getFile().getFileName(), event.getFile().getInputStream());
			nombreArchivo = destino + usr.getCarpeta()+"\\"+event.getFile().getFileName(); //ubicacion completa en servdor
			File arch= new File(nombreArchivo);
			if (arch.exists()) {
				Utilerias.setManagedBeanInSession("archivoSubido", nombreArchivo); //creamos el bean archivo
				imagen = DefaultStreamedContent.builder().contentType("image/jpeg").stream(() -> { //guardamos la imagen actual en el bean
							try {
								return new FileInputStream(nombreArchivo);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}).build();
			}
			
			//Registramos la imagen en la base de datos
			Conexion cnn=new Conexion();
			cnn.registrarImagen(usr.getId(),event.getFile().getFileName());
			
			//Agregamos a la lista de archivos del bean usuario el archivo o imagen subida
			//evitando tener que estar consultando a la base de datos, que sabemos ya se actualizo arriba.
			usr.getArchivos().add(event.getFile().getFileName());
			usr.rellenaImgs();//rellenamos denuevo las imagenes por subir una mas
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ex2) {
			ex2.printStackTrace();
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void copiar(String nombreArchivo, InputStream datos) {
		Usuario usr = (Usuario) Utilerias.getManagedBean("usuario", Usuario.class);
		try {
			OutputStream salida = new FileOutputStream(new File("C:\\AppServers\\temp\\"+usr.getCarpeta()+"\\"+ nombreArchivo));
			int leer = 0;
			byte[] bytes = new byte[1024];
			while ((leer = datos.read(bytes)) != -1) {
				salida.write(bytes, 0, leer);
			}
			datos.close();
			salida.flush();
			salida.close();
			System.out.println("Archivo Subido Copiado bien");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public StreamedContent getImagen() {
		return imagen;
	}

	public void setImagen(StreamedContent imagen) {
		this.imagen = imagen;
	}

	public StreamedContent getBajar() {
		if (dato != null) {
			bajar = imagen = DefaultStreamedContent.builder().contentType("image/jpeg")
					.name("imagen.jpg").stream(() -> {
						try {
							return new FileInputStream(dato);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}).build();
		}
		return bajar;
	}

	public boolean isDescarga() {
		return descarga;
	}

	public void setDescarga(boolean descarga) {
		this.descarga = descarga;
	}
}

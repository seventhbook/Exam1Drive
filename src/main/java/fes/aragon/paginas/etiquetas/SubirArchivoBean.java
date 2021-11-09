package fes.aragon.paginas.etiquetas;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import fes.aragon.utilerias.Utilerias;

@Named("subirArchivoBean")
@RequestScoped
public class SubirArchivoBean {

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

		dato = (String) Utilerias.getManagedBean("archivo", String.class);
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

	public SubirArchivoBean() {
	}

	public void archivoArriba(FileUploadEvent event) {
		BufferedImage bufimg = null;
		String nombreArchivo;
		try {
			this.copiar(event.getFile().getFileName(), event.getFile().getInputStream());
			nombreArchivo = destino + event.getFile().getFileName();
			final String nuevoArchivo = nombreArchivo.substring(0,
					nombreArchivo.length() - (nombreArchivo.length() -nombreArchivo.indexOf("."))
					)+ "G.jpg";

			File arch = new File(nombreArchivo);
			if (arch.exists()) {
				bufimg = ImageIO.read(arch);
				cambioGris(bufimg);
				ImageIO.write(bufimg, "jpg", new File(nuevoArchivo));
				Utilerias.setManagedBeanInSession("archivo", nuevoArchivo);
				imagen = DefaultStreamedContent.builder().contentType("image/jpeg").stream(() -> {

							try {
								return new FileInputStream(nuevoArchivo);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}).build();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void copiar(String nombreArchivo, InputStream datos) {
		try {
			OutputStream salida = new FileOutputStream(new File("C:\\AppServers\\temp\\" + nombreArchivo));
			int leer = 0;
			byte[] bytes = new byte[1024];
			while ((leer = datos.read(bytes)) != -1) {
				salida.write(bytes, 0, leer);
			}
			datos.close();
			salida.flush();
			salida.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cambioGris(BufferedImage bufimg) {
		// Variables que almacenarán los píxeles
		int mediaPixel, colorSRGB;
		Color colorAux;
		for (int i = 0; i < bufimg.getWidth(); i++) {
			for (int j = 0; j < bufimg.getHeight(); j++) {
				colorAux = new Color(bufimg.getRGB(i, j));
				mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() +

						colorAux.getBlue()) / 3);

				colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
				bufimg.setRGB(i, j, colorSRGB);
			}
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

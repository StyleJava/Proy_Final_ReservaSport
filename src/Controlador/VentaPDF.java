
package Controlador;


import Vista.FrmReserva;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import conexion.Conexion;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author edison
 */
public class VentaPDF {
      private String DNICliente;
    private String nombreCliente;
    private String telefonoCliente;
    private String direccionCliente;

    private String fechaActual = "";
    private String nombreArchivoPDFVenta = "";

    //metodo para obtener datos del cliente
    public void DatosCliente(int idCliente) {
        Connection cn = Conexion.conectar();
        String sql = "select * from tb_cliente where estado in ('1','2') and idCliente = '" + idCliente + "'";
        Statement st;
        try {

            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                DNICliente = rs.getString("DNI");
                nombreCliente = rs.getString("nombre");
                telefonoCliente = rs.getString("telefono");
                direccionCliente = rs.getString("direccion");
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener datos del cliente: " + e);
        }
    }
    
        public void generarFacturaPDF() {
        try {

            //cargar la fecha actual
            Date date = new Date();
            fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);
            
            //cambiar el formato de la fecha de "/" a "_" porque el pdf no acepta la primera 
            String fechaNueva = "";
            for (int i = 0; i < fechaActual.length(); i++) {
                //si  se encuentra con un slash lo convierte a _ en cada recorrido
                if (fechaActual.charAt(i) == '/') {
                    fechaNueva = fechaActual.replace("/", "_");
                }
            }
            
            //nombre que aparecerá como nombre de descarga del pdf
            
            nombreArchivoPDFVenta = "Reserva_" + nombreCliente + "_" + fechaNueva + ".pdf";//Reserva_JuanAreste_2023_12_03
            
            
            //para crear el pdf
            FileOutputStream archivo;
            File file = new File("src/Pdf/" + nombreArchivoPDFVenta); //es la ruta donde se registrara el pdf 
            archivo = new FileOutputStream(file);

            //crear nuevo documento, llamamos a la clase documente y creamos un objeto doc
            Document doc = new Document();
            //llamamos a la clase pdfwriter y le mandamos el doc y archive
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            
            Image img = Image.getInstance("src/Images/key.png");
            //parrafo
            Paragraph fecha = new Paragraph();
            //poner el formato en negrita y con color azul y estilo de texto
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            
            fecha.add(Chunk.NEWLINE); //agregar nueva linea
            fecha.add( "\nFecha: " + fechaActual + "\n\n");

            PdfPTable Encabezado = new PdfPTable(4);//cantidad de columnas
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);//quitar el borde de la tabla
            //tamaño de las celdas
            float[] ColumnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            //agregar celdas
            Encabezado.addCell(img);

            String socio = "PANDARESTE";
            String nombre = "Reserva Sport";
            String telefono = "920845751";
            String direccion = "Lima Norte";
            String razon = "Never give up";

            Encabezado.addCell("");//celda vacia
            Encabezado.addCell("Ingeniero: " + socio + "\n\n Negocio: " + nombre + "\n\nTELEFONO: " + telefono + "\n\nDIRECCION: " + direccion + "\n\nLema: " + razon);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);

            //CUERPO
            Paragraph cliente = new Paragraph();
            cliente.add(Chunk.NEWLINE);//nueva linea
            cliente.add(Chunk.NEWLINE);
            cliente.add(Chunk.NEWLINE);
            cliente.add(Chunk.NEWLINE);
            cliente.add(Chunk.NEWLINE);
            cliente.add(Chunk.NEWLINE);
            
            cliente.add("Datos del cliente: " + "\n\n");
            doc.add(cliente);

            //DATOS DEL CLIENTE
            PdfPTable tablaCliente = new PdfPTable(4);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.getDefaultCell().setBorder(0);//quitar bordes
            //tamaño de las celdas
            float[] ColumnaCliente = new float[]{25f, 45f, 30f, 40f};
            tablaCliente.setWidths(ColumnaCliente);
            tablaCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cliente1 = new PdfPCell(new Phrase("DNI: ", negrita));
            PdfPCell cliente2 = new PdfPCell(new Phrase("Nombre: ", negrita));
            PdfPCell cliente3 = new PdfPCell(new Phrase("Telefono: ", negrita));
            PdfPCell cliente4 = new PdfPCell(new Phrase("Direccion: ", negrita));
            //quitar bordes 
            cliente1.setBorder(0);
            cliente2.setBorder(0);
            cliente3.setBorder(0);
            cliente4.setBorder(0);
            //agg celda a la tabla
            tablaCliente.addCell(cliente1);
            tablaCliente.addCell(cliente2);
            tablaCliente.addCell(cliente3);
            tablaCliente.addCell(cliente4);
            tablaCliente.addCell(DNICliente);
            tablaCliente.addCell(nombreCliente);
            tablaCliente.addCell(telefonoCliente);
            tablaCliente.addCell(direccionCliente);
            //agregar al documento
            doc.add(tablaCliente);
            
            //ESPACIO EN BLANCO
            Paragraph espacio = new Paragraph();
            espacio.add(Chunk.NEWLINE);
            espacio.add(Chunk.NEWLINE);       
            espacio.add("");
            espacio.setAlignment(Element.ALIGN_CENTER);
            doc.add(espacio);
            
            //AGREGAR LOS PRODUCTOS
            PdfPTable tablaProducto = new PdfPTable(7);
         
            
            //tamaño de celdas
        
             tablaProducto.setHorizontalAlignment(Element.ALIGN_LEFT);
             PdfPCell producto1 = new PdfPCell(new Phrase("Código: ", negrita));
             PdfPCell producto2 = new PdfPCell(new Phrase("Campo: ", negrita));
             PdfPCell producto3 = new PdfPCell(new Phrase("Descripción: ", negrita));
             PdfPCell producto4 = new PdfPCell(new Phrase("Hora Inicio: ", negrita));
             PdfPCell producto5 = new PdfPCell(new Phrase("Hora Fin: ", negrita));
             PdfPCell producto6 = new PdfPCell(new Phrase("Precio Total: ", negrita));
             PdfPCell producto7 = new PdfPCell(new Phrase("Estado: ", negrita));
             //quitar bordes
             producto1.setBorder(0);
             producto2.setBorder(0);
             producto3.setBorder(0);
             producto4.setBorder(0);
             producto5.setBorder(0);
             producto6.setBorder(0);
             producto7.setBorder(0);
             //agregar color al encabezadi del producto
             producto1.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto2.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto3.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto4.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto5.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto6.setBackgroundColor(BaseColor.LIGHT_GRAY);
             producto7.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //agg celda a la tabla
            tablaProducto.addCell(producto1);
            tablaProducto.addCell(producto2);
            tablaProducto.addCell(producto3);
            tablaProducto.addCell(producto4);
            tablaProducto.addCell(producto5);
            tablaProducto.addCell(producto6);
            tablaProducto.addCell(producto7);
            
            for(int i = 0; i < (FrmReserva.tblReserva.getRowCount()); i++){
                String idreserva = FrmReserva.tblReserva.getValueAt(i, 1).toString();
                String nombreCampo = FrmReserva.tblReserva.getValueAt(i, 3).toString();
                String nombreDeporte  = FrmReserva.tblReserva.getValueAt(i, 4).toString();
                String horaInicio  = FrmReserva.tblReserva.getValueAt(i, 6).toString();
                String horaFinal  = FrmReserva.tblReserva.getValueAt(i, 7).toString();
                String total = FrmReserva.tblReserva.getValueAt(i, 11).toString();
                String estado = FrmReserva.tblReserva.getValueAt(i, 12).toString();
                
                tablaProducto.addCell(idreserva);
                tablaProducto.addCell(nombreCampo);
                tablaProducto.addCell(nombreDeporte);
                tablaProducto.addCell(horaInicio);
                tablaProducto.addCell(horaFinal);
                tablaProducto.addCell(total);
                tablaProducto.addCell(estado);
            }
            
            //agregar al documento
            doc.add(tablaProducto);
            
            //Total pagar
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a pagar: " + FrmReserva.txtMontoFinal.getText());
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);
            
            //Firma
           Paragraph firma = new Paragraph();
           firma.add(Chunk.NEWLINE);
            firma.add(Chunk.NEWLINE);
             
           firma.add("Cancelacion y firma\n\n\n\n");
           firma.add("_______________________");
           firma.setAlignment(Element.ALIGN_CENTER);
           doc.add(firma);
           
            //Mensaje
           Paragraph mensaje = new Paragraph();
           mensaje.add(Chunk.NEWLINE);
           mensaje.add("¡Gracias por su preferencia!");
           mensaje.setAlignment(Element.ALIGN_CENTER);
           doc.add(mensaje);
           
           //cerrar el documento y el archivo
           doc.close();
           archivo.close();
           
           //abrir el documento al ser generado automaticamente
            Desktop.getDesktop().open(file);
            
            
        } catch (DocumentException | IOException e) {
            System.out.println("Error en: " + e);
        }
    }
    
    
   
    
    
   
}

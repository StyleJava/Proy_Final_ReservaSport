
package Controlador;

import Modelo.Reserva;
import com.mysql.jdbc.Statement;
import java.sql.Date;
import java.time.LocalTime;
import Formatos.Mensajes;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;




public class Ctrl_Reserva {
    
ResultSet rs ;  
Statement st;
       
  public boolean Insertar(Reserva rsa) {
        boolean respuesta = false;
        Connection cn = conexion.Conexion.conectar();
        try {
            PreparedStatement ps = cn.prepareStatement("INSERT INTO tb_reserva (idCliente,idCampo,idCancha,fecha"
                    + ",horaInicio,horaFin,duracion,primerPago,segundoPago,pagototal,estado) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
            
            
            ps.setInt(1, rsa.getIdCliente());
            ps.setInt(2, rsa.getIdCampo());
            ps.setInt(3, rsa.getIdCancha());
             ps.setDate(4, new java.sql.Date(rsa.getFechaReserva().getTime()));
            ps.setTime(5, java.sql.Time.valueOf(rsa.getHoraInicio()));
        ps.setTime(6, java.sql.Time.valueOf(rsa.getHoraFin()));
            ps.setInt(7, rsa.getDuracion());
            ps.setFloat(8, rsa.getPrimerPago());
            ps.setFloat(9, rsa.getSegundoPago());
            ps.setFloat(10, rsa.getPagototal());
            ps.setFloat(11, rsa.getEstado());


            if (ps.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            Mensajes.M1("Error al guardar la reserva: " + e);
        }
        return respuesta;
    }/*
  public void MostrarReservasEnTabla(JTable tabla,String consulta){
    
    String titulos[]={"Nro","ID producto","Nombre de producto","Proveedor","Categoría","Cantidad",
                      "Precio","Vencimiento","Stock"};
    
    DefaultTableModel modelo =  new DefaultTableModel(null,titulos);
    tabla.setModel(modelo);
    int cantreg=0;
    try{
          rs = st.executeQuery(consulta);
         while(rs.next()){
             cantreg++;
             Reserva rsa = new Reserva();
             prod.setIdprod(rs.getInt(1));
             prod.setNomprod(rs.getString(2));
             prod.setIdprov(rs.getInt(3));
             prod.setIdcat(rs.getInt(4));
             prod.setCantprod(rs.getString(5));
             prod.setPrecio(rs.getDouble(6));
             prod.setFechaven(rs.getDate(7));
             prod.setStock(rs.getInt(8));
             modelo.addRow(prod.Registro(cantreg));
         }
        conexion.close();
    }catch(Exception e){
        Mensajes.M1("ERROR no se puede mostrar productos en tabla..."+e);
    }    
}
  
  
  */
     //metodo que recupera un registro de la tabla categoria mediante su id
 public Reserva ConsultarRegistro(int idReserva){
     Reserva rsa = null;
     Connection con = Conexion.conectar();
      Statement st;
      String sql = "select idReserva,estado from tb_reserva where estado='S' and idReserva="+idReserva+";";

     try{
           st = (Statement) con.createStatement();
            ResultSet rs = st.executeQuery(sql);
         
         if(rs.next()){
             rsa = new Reserva();
             rsa.setIdCliente(rs.getInt(1));
             rsa.setEstado(rs.getInt(2));
         }
         rs.close();
     }catch(Exception e){
         Mensajes.M1("ERROR no se puede consultar el registro ..."+e);
     }
     return rsa;
 }
  

 public int auto_increm(String sql){
        int id = 1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion db = new Conexion();
        try{    
                ps = db.conectar().prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                    id = rs.getInt(1)+1;
                }
        }catch(Exception ex){
            System.out.println("idmaximo"+ex.getMessage());
            id = 1;
        }
        finally{
            try{
                ps.close();
                rs.close();
               
            }catch(Exception ex){}
        }
        return id;
    }
 
 
 
   public int id_reserva_auto(){
        
       
        int id_max2 = 1;
        try{
            id_max2 = auto_increm("SELECT MAX(idReserva) FROM tb_reserva;");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return id_max2;
    }
 
}

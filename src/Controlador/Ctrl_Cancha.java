    
package Controlador;
import Formatos.Mensajes;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Modelo.Cancha;



public class Ctrl_Cancha {
    
    public boolean registrarCancha(Cancha can) {
        boolean respuesta = false;
        Connection cn = conexion.Conexion.conectar();
        try {

            PreparedStatement ps = cn.prepareStatement("insert into tb_cancha(descripcion,precioMinuto,estado) values (?,?,?);");
            
            
            ps.setString(1,can.getDescripcion());
            ps.setFloat(2,can.getPrecioMinuto());
            can.setEstadoDisponible();
            ps.setInt(3, can.getEstado());
            
            if (ps.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
  

        } catch (SQLException e) {
            System.out.println("Error al guardar la canchita: " + e);
        }
            return respuesta;   
    }
        
        
         public boolean existeCancha(String descripcion) {
        boolean respuesta = false;
        String sql = "select descripcion from tb_cancha where descripcion = '" + descripcion + "';";
        Statement st;
        try {
            Connection cn = Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return respuesta;
    }
        
       
   //metodo que recupera un registro de la tabla categoria mediante su id
 public Cancha ConsultarIDCancha(int idCancha){
     Cancha can = null;
     Connection con = Conexion.conectar();
      Statement st; 
      String sql = "select idCancha,descripcion,precioMinuto,"
                 + "estado from tb_cancha where estado in ('1','2') and idCancha="+idCancha+";";

     try{
           st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
         
         if(rs.next()){
             can = new Cancha();
             can.setIdCancha(rs.getInt(1));
             can.setDescripcion(rs.getString(2));
             can.setPrecioMinuto(rs.getFloat(3));
             can.setEstado(rs.getInt(4));
         }
         rs.close();
     }catch(Exception e){
         Mensajes.M1("ERROR no se puede consultar el registro ..."+e);
     }
     return can;
 }
 
//metodo para que se extraigan datos en la reserva
  public Cancha ConsultarDeporteReserva(String descripcion){
     Cancha depor = null;
     Connection con = Conexion.conectar();
      Statement st;
      String sql ="select * from tb_cancha where estado='1' and descripcion='"+descripcion+"';";

     try{
           st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
         
         if(rs.next()){
             depor = new Cancha();
             depor.setIdCancha(rs.getInt(1));
             depor.setDescripcion(rs.getString(2));
             depor.setPrecioMinuto(Float.parseFloat(rs.getString(3)));
            
            
         }
         rs.close();
     }catch(Exception e){
         Mensajes.M1("ERROR no se puede consultar el registro ..."+e);
     }
     return depor;
 }
 
 //método que actualiza un registro  de la tabla categoria por medio de su id
 public void ActualizarCancha(Cancha depor){
     Connection cn = conexion.Conexion.conectar();
     try{
        PreparedStatement ps = cn.prepareStatement("update tb_cancha set descripcion=?,precioMinuto=?,estado=?  where idCancha=?;");

     
         ps.setString(1,depor.getDescripcion());
         ps.setFloat(2,depor.getPrecioMinuto());
         ps.setInt(3, depor.getEstado());
         ps.setInt(4,depor.getIdCancha());
         ps.executeUpdate();
         Mensajes.M1("Cancha actualizado correctamente :3 ...");
         ps.close();
     }catch(Exception  ex){
         Mensajes.M1("ERROR no se puede actualizar la cancha :c ..."+ex);
     }
 }

 
}

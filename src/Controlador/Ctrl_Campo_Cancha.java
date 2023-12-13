package Controlador;
import Formatos.Mensajes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.Campo_Cancha;
import Modelo.Cancha;
import java.util.ArrayList;
import java.util.List;


public class Ctrl_Campo_Cancha {
    
       
  public boolean Enlazar(Campo_Cancha campo_deporte) {
        boolean respuesta = false;
        Connection cn = conexion.Conexion.conectar();
        try {
            PreparedStatement ps = cn.prepareStatement("INSERT INTO tb_campodeportivo_cancha (idCampo, idCancha) VALUES (?, ?);");
            ps.setInt(1, campo_deporte.getIdCampo());
            ps.setInt(2, campo_deporte.getIdCancha());

            if (ps.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            Mensajes.M1("Error al guardar relación campo-cancha: " + e);
        }
        return respuesta;
    }
 
 public List<Cancha> obtenerCanchaPorCampo(String nombreCampo) {
        List<Cancha> deportes = new ArrayList<>();
        Connection cn = conexion.Conexion.conectar();
        try {
            
            
            String sql  = "SELECT d.* FROM tb_cancha d " 
                        + "INNER JOIN tb_campodeportivo_cancha cd ON d.idCancha = cd.idCancha " 
                         + "INNER JOIN tb_campodeportivo c ON c.idCampo = cd.idCampo " 
                        + "WHERE c.nombre = ? AND c.estado = 1;";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, nombreCampo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cancha can = new Cancha();
                can.setIdCancha(rs.getInt("idCancha"));
                can.setDescripcion(rs.getString("descripcion"));
                can.setPrecioMinuto(rs.getFloat("precioMinuto"));
                can.setEstado(rs.getInt("estado"));

                deportes.add(can);
            }
            cn.close();
        } catch (SQLException e) {
            Mensajes.M1("Error al obtener canchas por campo: " + e);
        }
        return deportes;
    }
 

 
}

package TPF.Persistencia;

import TPF.Modelo.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PersistenciaUsuarios {

        public static HashSet<Usuario> leerUsuarios(){
            HashSet<Usuario> usuarios = null;
            try{
            File file = new File("\\Users\\franc\\Desktop\\TPFINAL\\files\\users.json");
            ObjectMapper mapper = new ObjectMapper();
            if(file.exists())
                usuarios = mapper.readValue(file, new TypeReference<HashSet<Usuario>>(){});
            }

            catch (IOException e){
            System.err.println("No se pudo leer el archivo de usuarios");
            }

            return usuarios;
        }

}

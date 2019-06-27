package es.usc.citius.zebrafish.modelo.usuarios;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class UserHelper {
    //List<String> listaCookies;

    /*public UserHelper(){
        listaCookies = new ArrayList<String>();
    }*/

    public static final String uuid() {
        String result = java.util.UUID.randomUUID().toString();
        result.replaceAll("-", "");
        result.substring(0, 32);

        return result;
    }

    public static String createFolderUser(String filesPath, String idUser) {
        String carpeta = filesPath + "/" + idUser;

        File directorio = new File(carpeta + File.separator + "Procesado2D");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        return carpeta;
    }

   /* public static void saveCookie(String cookieName, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(value, value);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
        //this.listaCookies.add(value);
    }

    public static String getCookieValue(String cookieName, HttpServletRequest request) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int i = 0;
            boolean cookieExists = false;
            while (!cookieExists && i < cookies.length) {
                if (cookies[i].getName().equals(cookieName)) {
                    cookieExists = true;
                    value = cookies[i].getValue();
                } else {
                    i++;
                }
            }
        }
        return value;
    }*/


    /*Borra directorio con archivos dentro*/
    /*public void borrarCarpeta(File directorio){
        File[] ficheros = directorio.listFiles();
        for (int i=0;i<ficheros.length;i++){
            ficheros[i].delete();
        }
        directorio.delete();
    }

    @Scheduled(fixedRate = 3000)
    public void tarea1() {
        System.out.println("Tarea usando fixedRate cada 3 segundos - " + System.currentTimeMillis() / 1000);
    }

    public String[] listarCarpetasDentroDirectorio(File directorio){
        String[] subdirectorios=directorio.list();

        return subdirectorios;
    }

    public List<String> cookiesCaducadas(HttpServletRequest request) {

        List<String> listaCookiesCaducadas = new ArrayList<String>();
        for (String cookie :this.listaCookies) {
            if(getCookieValue(cookie,request)==null){
                listaCookiesCaducadas.add(cookie);
            }
        }
        return listaCookiesCaducadas;
    }


    public void borrarCarpetasUsuarios(String filesPath, HttpServletRequest request){
        List<String> listaCookiesCaducadas=cookiesCaducadas(request);
        String carpeta;
        for (String cookie :listaCookiesCaducadas) {
            carpeta=filesPath+"/"+cookie;
            try {
                Process p = Runtime.getRuntime().exec("rm -rf "+carpeta);
            }
            catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }*/
}

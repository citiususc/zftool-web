package es.usc.citius.zebrafish.modelo.validadores;

import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

public class Procesado3DValidador {

    private static String validarImaxes(MultipartFile[] imagenes3D_0h, MultipartFile[] imagenes3D_48h) {
        String regExp = "(pez\\d{1,6}_\\d{2,3}\\.tif)";
        for (int i = 0; i < imagenes3D_0h.length; i++) {
            String name = imagenes3D_0h[i].getOriginalFilename();
            if (!Pattern.matches(regExp, name)) {
                return "Alguna de las imágenes de las 0h no tiene un nombre correcto! Debe ser \"pez<numero_pez>_<corte_imagen>.tif\"";
            }
        }
        for (int j = 0; j < imagenes3D_48h.length; j++) {
            String name = imagenes3D_48h[j].getOriginalFilename();
            if (!Pattern.matches(regExp, name)) {
                return "Alguna de las imágenes de las 24-48-72h no tiene un nombre correcto! Debe ser \"pez<numero_pez>_<corte_imagen>.tif\"";
            }
        }
        return null;
    }


    private static String validarParametrosUmbral(int fin, int paso) {
        if (fin <= 0) {
            return "El valor de fin (" + fin + ") no puede ser un número negativo o menor o igual que 0";
        } else if (paso <= 0) {
            return "El valor de paso (" + paso + ") no puede ser un número negativo o menor o igual que 0";
        } else if (fin < paso) {
            return "El valor de fin (" + fin + ") no puede ser menor que el de paso (" + paso + ")";
        } else if (fin % paso != 0) {
            return "El valor de paso (" + paso + ") debe ser un divisor del escogido para fin (" + fin + ")";
        } else {
            return null;
        }
    }


    public static String validarProcesado(MultipartFile[] imagenes3D_0h, MultipartFile[] imagenes3D_48h, int fin, int paso) {
        String error;
        error=validarImaxes(imagenes3D_0h,imagenes3D_48h);
        if(error!=null){
            return error;
        }else{
        return validarParametrosUmbral(fin, paso);
        }
    }

    public static String validarReprocesado(int fin, int paso, int umbral) {
        if (umbral < 0) {
            return "El valor del umbral no puede ser un número negativo o menor que 0";
        } else if (umbral > fin) {
            return "El valor del umbral no puede ser mayor que el seleccionado anteriormente como fin (" + fin + ")";
        } else if (umbral % paso != 0) {
            return "El valor del umbral debe estar entre [0," + fin + "], y ser múltiplo del paso (" + paso + ")";
        } else {
            return null;
        }
    }
}

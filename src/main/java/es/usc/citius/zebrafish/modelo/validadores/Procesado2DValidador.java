package es.usc.citius.zebrafish.modelo.validadores;

import java.util.regex.Pattern;

public class Procesado2DValidador {


    private static String validarImaxes(String nombreImg0hg, String nombreImg0hf, String nombreImg48hg, String nombreImg48hf) {
        String error = null;
        String h1, h3;
        String regExpGris0h = "(\\d{1,6} - 0h bn\\.tif)";
        String regExpGris48h = "(\\d{1,6} - (24|48|72)h bn\\.tif)";
        String regExpFluorescente0h = "(\\d{1,6} - 0h gfp\\.tif)";
        String regExpFluorescente48h = "(\\d{1,6} - (24|48|72)h gfp\\.tif)";
        boolean todasImg = !nombreImg0hg.equals("") && !nombreImg48hg.equals("");
        if (todasImg) {
            if (Pattern.matches(regExpGris0h, nombreImg0hg) && Pattern.matches(regExpGris48h, nombreImg48hg) && Pattern.matches(regExpFluorescente0h, nombreImg0hf) && Pattern.matches(regExpFluorescente48h, nombreImg48hf)) {
                String[] image0 = nombreImg0hf.split(" ");
                String[] image1 = nombreImg48hf.split(" ");
                String[] image2 = nombreImg0hg.split(" ");
                String[] image3 = nombreImg48hg.split(" ");
                h1 = image1[2].substring(0, image1[2].length() - 1);
                h3 = image3[2].substring(0, image3[2].length() - 1);
                if (image0[0].equals(image1[0]) && image2[0].equals(image3[0]) && image0[0].equals(image2[0])) {
                    if (!h1.equals(h3)) {
                        error = "Las imágenes post inyección tiene diferentes horas de procesamiento!";
                    }
                } else {
                    error = "Las imágenes no pertenecen al mismo pez!";
                }
            } else {
                error = "Los nombres de las imágenes deben ser \"<numero_pez> - <hora_procesado>h bn.tif\" o \"<numero_pez> - <hora_procesado>h gfp.tif\"";
            }
        } else {
            if (Pattern.matches(regExpFluorescente0h, nombreImg0hf) && Pattern.matches(regExpFluorescente48h, nombreImg48hf)) {
                String[] image0 = nombreImg0hf.split(" ");
                String[] image1 = nombreImg48hf.split(" ");
                if (!image0[0].equals(image1[0])) {
                    error = "Las imágenes no pertenecen al mismo pez!";
                }
            } else {
                error = "Los nombres de las imágenes deben ser \"<numero_pez> - <hora_procesado>h bn.tif\" o \"<numero_pez> - <hora_procesado>h gfp.tif\"";
            }
        }

        return error;
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


    public static String validarProcesado(String nombreImg0hg, String nombreImg0hf, String nombreImg48hg, String nombreImg48hf, int fin, int paso) {
        String error;
        error = validarImaxes(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf);
        if (error != null) {
            return error;
        } else {
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

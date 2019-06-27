package es.usc.citius.zebrafish.modelo.procesamiento;

import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Procesado3D {

    public Procesado3D() { }

    public static List<String> guardarImagenes0hProcesado3D(MultipartFile[] imagenes3D_0h, String rutaCarpetaUsuario0h) {
        List<String> listaImg0h = new ArrayList<String>();
        try {
            // Crear el directorio para almacenar el archivo
            File dir_0h = new File(rutaCarpetaUsuario0h + File.separator + "Procesado3D" + File.separator + "0h");
            if (!dir_0h.exists()) {
                dir_0h.mkdirs();
            }
            for (int i = 0; i < imagenes3D_0h.length; i++) {
                MultipartFile file = imagenes3D_0h[i];
                String name = imagenes3D_0h[i].getOriginalFilename();
                byte[] bytes = file.getBytes();
                // Crear documento en el servidor
                File serverFile = new File(dir_0h.getAbsolutePath() + File.separator + name);

                listaImg0h.add(dir_0h.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al subir las imágenes de las 0h " + e.getMessage());
        }
        return listaImg0h;
    }

    public static List<String> guardarImagenes24_48_72hProcesado3D(MultipartFile[] imagenes3D_48h, String rutaCarpetaUsuario48h) {
        List<String> listaImg24_48_72h = new ArrayList<String>();
        try {
            // Crear el directorio para almacenar el archivo
            File dir_48h = new File(rutaCarpetaUsuario48h + File.separator + "Procesado3D" + File.separator + "48h");
            if (!dir_48h.exists()) {
                dir_48h.mkdirs();
            }
            for (int i = 0; i < imagenes3D_48h.length; i++) {
                MultipartFile file = imagenes3D_48h[i];
                String name = imagenes3D_48h[i].getOriginalFilename();
                byte[] bytes = file.getBytes();
                // Crear documento en el servidor
                File serverFile = new File(dir_48h.getAbsolutePath() + File.separator + name);
                listaImg24_48_72h.add(dir_48h.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al subir las imágenes de las 24-48-72h " + e.getMessage());
        }
        return listaImg24_48_72h;
    }


    public static void realizarProcesado3D(int fin, int paso, String filesPath, String rutaCarpetaUsuario, List<String> listaImg0h, List<String> listaImg24_48_72h, Analisis resultados) {
        int[] nGFP_0_3D = new int[fin / paso + 1];
        int[] nGFP_1_3D = new int[fin / paso + 1];
        double[] meanGFP_0_3D = new double[fin / paso + 1];
        double[] meanGFP_1_3D = new double[fin / paso + 1];
        int umbral0 = 0, umbral1 = 0, umbral;

        resultados.setPaso(paso);
        resultados.setFin(fin);
        resultados.calcularRange();

        String urlDatos1 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "0h" + File.separator + "datos1.txt";
        String urlObj1 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "0h" + File.separator + "modelo0h.obj";
        String parametros0h;
        parametros0h = " " + urlDatos1;
        parametros0h += " " + fin;
        parametros0h += " " + paso;
        parametros0h += " " + urlObj1;
        for (String img0h : listaImg0h) {
            parametros0h += " " + img0h;
        }
        System.out.println("Java UrlDatos1-" + urlDatos1);
        System.out.println("Java UrlParametros1-" + parametros0h);
        String s = null;
        try {
            String archivoEjecutar = filesPath + File.separator + "0h_3D.py";
            Process p = Runtime.getRuntime().exec("python2 " + archivoEjecutar + parametros0h);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File(urlDatos1);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea = br.readLine();
            String[] l0 = linea.split(" ");
            int i = 0;
            for (String n0 : l0) {
                nGFP_0_3D[i] = Integer.parseInt(n0);
                i++;
            }
            linea = br.readLine();
            l0 = linea.split(" ");
            i = 0;
            for (String n0 : l0) {
                meanGFP_0_3D[i] = Double.parseDouble(n0);
                i++;
            }
            linea = br.readLine();
            umbral0 = Integer.parseInt(linea);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        String urlDatos2 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "48h" + File.separator + "datos2.txt";
        String urlObj2 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "48h" + File.separator + "modelo48h.obj";
        String parametros24_48_72h;
        parametros24_48_72h = " " + urlDatos2;
        parametros24_48_72h += " " + fin;
        parametros24_48_72h += " " + paso;
        parametros24_48_72h += " " + urlObj2;
        for (String img24_48_72h : listaImg24_48_72h) {
            parametros24_48_72h += " " + img24_48_72h;
        }

        System.out.println("Java UrlDatos2-" + urlDatos2);
        System.out.println("Java UrlParametros2-" + parametros24_48_72h);
        String s1 = null;
        try {
            String archivoEjecutar = filesPath + File.separator + "48h_3D.py";
            Process p = Runtime.getRuntime().exec("python2 " + archivoEjecutar + parametros24_48_72h);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s1 = stdInput.readLine()) != null) {
                System.out.println(s1);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s1 = stdError.readLine()) != null) {
                System.out.println(s1);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

        File archivo1 = null;
        FileReader fr1 = null;
        BufferedReader br1 = null;
        try {
            archivo1 = new File(urlDatos2);
            fr1 = new FileReader(archivo1);
            br1 = new BufferedReader(fr1);

            // Lectura del fichero
            String linea = br1.readLine();
            String[] l0 = linea.split(" ");
            int i = 0;
            for (String n0 : l0) {
                nGFP_1_3D[i] = Integer.parseInt(n0);
                i++;
            }
            linea = br1.readLine();
            l0 = linea.split(" ");
            i = 0;
            for (String n0 : l0) {
                meanGFP_1_3D[i] = Double.parseDouble(n0);
                i++;
            }
            linea = br1.readLine();
            umbral1 = Integer.parseInt(linea);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero
            try {
                if (null != fr1) {
                    fr1.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        umbral = umbral0;
        if (umbral < umbral1) {
            umbral = umbral1;
        }
        resultados.setUmbral(umbral);
        resultados.setNGFP_0(nGFP_0_3D);
        resultados.setNGFP_1(nGFP_1_3D);
        resultados.setMeanGFP_0(meanGFP_0_3D);
        resultados.setMeanGFP_1(meanGFP_1_3D);
        resultados.setPosicionUmbral(resultados.getUmbral() / resultados.getPaso());
        resultados.calcularFactorProliferacion();
        resultados.calcularArrayUmbralElegido();

    }


    public static void reprocesadoRepresentacion3D(int umbralManual, String filesPath, String rutaCarpetaUsuario, List<String> listaImg0h, List<String> listaImg24_48_72h) {

        String urlObj1 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "0h" + File.separator + "modelo0h.obj";
        String parametros0h;
        parametros0h = " " + urlObj1;
        parametros0h += " " + umbralManual;
        for (String img0h : listaImg0h) {
            parametros0h += " " + img0h;
        }
        System.out.println("Java UrlParametros1-" + parametros0h);
        String s = null;
        try {
            String archivoEjecutar = filesPath + File.separator + "0h_reprocesar3D.py";
            Process p = Runtime.getRuntime().exec("python2 " + archivoEjecutar + parametros0h);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }


        String urlObj2 = rutaCarpetaUsuario + File.separator + "Procesado3D" + File.separator + "48h" + File.separator + "modelo48h.obj";
        String parametros24_48_72h;
        parametros24_48_72h = " " + urlObj2;
        parametros24_48_72h += " " + umbralManual;
        for (String img24_48_72h : listaImg24_48_72h) {
            parametros24_48_72h += " " + img24_48_72h;
        }
        System.out.println("Java UrlParametros2-" + parametros24_48_72h);
        String s1 = null;
        try {
            String archivoEjecutar = filesPath + File.separator + "48h_reprocesar3D.py";
            Process p = Runtime.getRuntime().exec("python2 " + archivoEjecutar + parametros24_48_72h);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s1 = stdInput.readLine()) != null) {
                System.out.println(s1);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s1 = stdError.readLine()) != null) {
                System.out.println(s1);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static void reProcesadoManual(int umbralManual, Analisis resultados) {
        resultados.setUmbral(umbralManual);
        resultados.calcularFactorProliferacion();
        resultados.calcularArrayUmbralElegido();
        resultados.setPosicionUmbral(resultados.getUmbral() / resultados.getPaso());
    }

}

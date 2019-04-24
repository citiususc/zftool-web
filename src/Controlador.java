import org.opencv.core.Core;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.DecimalFormat;
import java.util.*;
import au.com.bytecode.opencsv.CSVWriter;

public class Controlador extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void  doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("resultados") == null) {
            sesion.setAttribute("resultados", new Analise());
        }

        if (request.getParameter("carga-imaxes2d") != null) {
            Analise resultados = (Analise)sesion.getAttribute("resultados");
            int fin=Integer.parseInt(request.getParameter("Fin"));
            int paso=Integer.parseInt(request.getParameter("Paso"));
            sesion.setAttribute("fin",fin );
            sesion.setAttribute("paso",paso );
            int longitud=(fin/paso)+1;
            int[] rango=new int[longitud];
            int count=0;
            for(int i=0;i<=fin; i=i+paso){
                rango[count]=i;
                count++;
            }
            resultados.setRange(rango);

            String pez,h0,h1,h2,h3;
            String[] image0=request.getParameter("imagen0hg").split(" "); h0=image0[2].substring(0,image0[2].length()-1);
            String[] image1=request.getParameter("imagen72hg").split(" ");h1=image1[2].substring(0,image1[2].length()-1);
            boolean todasImg=!request.getParameter("imagen0hf").equals("") && !request.getParameter("imagen72hf").equals("");
            if(todasImg){
                String[] image2=request.getParameter("imagen0hf").split(" "); h2=image2[2].substring(0,image2[2].length()-1);
                String[] image3=request.getParameter("imagen72hf").split(" ");h3=image3[2].substring(0,image3[2].length()-1);
                if(image0[0].equals(image1[0]) && image2[0].equals(image3[0]) && image0[0].equals(image2[0]) ){
                    pez=image0[0];
                    if(h0.equals("0") && h2.equals("0")){
                        if(h1.equals(h3) && (h1.equals("24") || h1.equals("48")|| h1.equals("72"))){
                            String carpeta="Images";
                            Procesado inicial =new Procesado();
                            inicial.procesado2D(carpeta,pez,h0,h1,fin, paso,resultados);
                            /*int[] n0={28259, 15666, 8166, 4367, 2869, 1975, 1711, 1602, 1519, 1453, 1396};
                            int[] n1={13870, 7102, 4295, 2648, 2136, 1959, 1834, 1729, 1639, 1536, 1430};
                            double[] mean0={13.4149769585253, 20.050491510277, 31.3622336517267, 47.6354476757499, 63.196584175671, 81.5113924050633, 89.8684979544126,
                                    93.7484394506867, 96.7952600394997, 99.2463867859601, 101.352435530086};
                            double[] mean1={18.0431320504313, 28.4291748803154, 41.8873108265425, 60.0853474320242, 70.2930711610487, 74.5752935171006, 77.7486368593239,
                                    80.4522845575477, 82.7742525930445, 85.4479166666667, 88.2356643356643};
                            double fP=0.9778; int umbral=35;
                            resultados.setMeanGFP_0(mean0);
                            resultados.setMeanGFP_1(mean1);
                            resultados.setNGFP_0(n0);
                            resultados.setNGFP_1(n1);
                            resultados.setFactorProliferacion(fP);
                            resultados.setUmbral(umbral);*/

                            sesion.setAttribute("posicionUmbral",resultados.getUmbral()/paso);
                            sesion.setAttribute("pez",pez);
                            sesion.setAttribute("hInicial",h0);
                            sesion.setAttribute("hFinal",h1);
                            sesion.setAttribute("resultados",resultados);
                            goToPage("procesado2D.jsp", request, response);
                        }else{
                            request.setAttribute("erro","Alguna de las imágenes no se corresponde a las 24-48-72h!");
                            goToPage("carga2D.jsp", request, response);
                        }
                    }else{
                        request.setAttribute("erro","Alguna de las imágenes no se corresponde a las 0h!");
                        goToPage("carga2D.jsp", request, response);
                    }
                }else{
                    request.setAttribute("erro","Las imágenes no pertenecen al mismo pez!");
                    goToPage("carga2D.jsp", request, response);
                }
            }else{
                if(image0[0].equals(image1[0])){
                    pez=image0[0];
                    if(h0.equals("0")){
                        if(h1.equals("24") || h1.equals("48") ||h1.equals("72")){
                            /*String carpeta="Images";
                            Procesado.procesado2D(carpeta,pez,h0,h1,fin, paso,resultados);*/
                            int[] n0={28259, 15666, 8166, 4367, 2869, 1975, 1711, 1602, 1519, 1453, 1396};
                            int[] n1={13870, 7102, 4295, 2648, 2136, 1959, 1834, 1729, 1639, 1536, 1430};
                            double[] mean0={13.4149769585253, 20.050491510277, 31.3622336517267, 47.6354476757499, 63.196584175671, 81.5113924050633, 89.8684979544126,
                                    93.7484394506867, 96.7952600394997, 99.2463867859601, 101.352435530086};
                            double[] mean1={18.0431320504313, 28.4291748803154, 41.8873108265425, 60.0853474320242, 70.2930711610487, 74.5752935171006, 77.7486368593239,
                                    80.4522845575477, 82.7742525930445, 85.4479166666667, 88.2356643356643};
                            double fP=0.9778; int umbral=35;
                            resultados.setMeanGFP_0(mean0);
                            resultados.setMeanGFP_1(mean1);
                            resultados.setNGFP_0(n0);
                            resultados.setNGFP_1(n1);
                            resultados.setFactorProliferacion(fP);
                            resultados.setUmbral(umbral);

                            sesion.setAttribute("posicionUmbral",resultados.getUmbral()/paso);
                            sesion.setAttribute("pez",pez);
                            sesion.setAttribute("hInicial",h0);
                            sesion.setAttribute("hFinal",h1);
                            sesion.setAttribute("resultados",resultados);
                            goToPage("procesado2D.jsp", request, response);
                        }else{
                            request.setAttribute("erro","La imagen no se corresponde a las 24-48-72h!");
                            goToPage("carga2D.jsp", request, response);
                        }
                    }else{
                        request.setAttribute("erro","La imagen no se corresponde a las 0h!");
                        goToPage("carga2D.jsp", request, response);
                    }
                }else{
                    request.setAttribute("erro","Las imágenes no pertenecen al mismo pez!");
                    goToPage("carga2D.jsp", request, response);
                }
            }

        } else if (request.getParameter("procesado-2D") != null) {
            goToPage("procesado2D.jsp", request, response);

        } else if (request.getParameter("exportar-2D") != null) {
            Analise resultados = (Analise)sesion.getAttribute("resultados");
            Integer paso=(Integer)sesion.getAttribute("paso");
            Integer fin=(Integer)sesion.getAttribute("fin");;
            int pos=(int)resultados.getUmbral()/paso;
            int[] escogido=new int[fin/paso+1];
            int count=0;
            for(int i=0;i<=fin; i=i+paso){
                escogido[count]=0;
                count++;
            }
            escogido[pos]=1;
            try {
                resultados.imprimirFicheroCSV("resultados2D.csv", escogido);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            goToPage("procesado2D.jsp", request, response);
        }
        else if (request.getParameter("carga-imaxes3d") != null) {
            Analise resultados = (Analise)sesion.getAttribute("resultados");
            int fin=Integer.parseInt(request.getParameter("Fin_3D"));
            int paso=Integer.parseInt(request.getParameter("Paso_3D"));
            int[] nGFP_0_3D=new int[fin/paso+1];
            int[] nGFP_1_3D=new int[fin/paso+1];
            double[] meanGFP_0_3D=new double[fin/paso+1];
            double[] meanGFP_1_3D=new double[fin/paso+1];
            ArrayList<Punto> nube3D0h=new ArrayList<>();
            ArrayList<Punto> nube3D48h_72h=new ArrayList<>();
            int umbral0=0,umbral1=0,umbral;

            int longitud=(fin/paso)+1;
            int[] rango=new int[longitud];
            int pos=0;
            for(int i=0;i<=fin; i=i+paso){
                rango[pos]=i;
                pos++;
            }
            resultados.setRange(rango);
            String s = null;
            try {
                // run the Unix "ps -ef" command
                // using the Runtime exec method:
                Process p = Runtime.getRuntime().exec("python /home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/48_0h/48_0_3D.py");

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

            }
            catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
            }
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            try {
                // Apertura del fichero y creacion de BufferedReader para poder
                // hacer una lectura comoda (disponer del metodo readLine()).
                archivo = new File ("/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/48_0h/datos1.txt");
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);

                // Lectura del fichero
                String linea; int count=0; double auxmean0;
                while((linea=br.readLine())!=null){
                    if(count==0){
                        String[] l0 = linea.split(" ");
                        int i=0;
                        for(String n0:l0){
                            nGFP_0_3D[i]=Integer.parseInt(n0);
                            System.out.println("N0-"+nGFP_0_3D[i]);
                            i++;
                        }
                    }
                    if(count==1){
                        String[] l0 = linea.split(" ");
                        int i=0;
                        for(String n0:l0){
                            meanGFP_0_3D[i]=Double.parseDouble(n0);
                            System.out.println("M0-"+meanGFP_0_3D[i]);
                            i++;
                        }
                    }
                    if(count==2){
                      umbral0=Integer.parseInt(linea);
                      System.out.println("umbral-"+umbral0);
                    }
                    if(count==3){
                        String modLinea=linea.replace("[","");
                        String modLinea1=modLinea.replace("]","");
                        String modLinea2=modLinea1.replace("(","");
                        String modLinea3=modLinea2.replace(")","");
                        String modLinea4=modLinea3.replace(",","");
                        String[] l0 = modLinea4.split(" ");
                        int cont=0, valor1=0,valor2=0,valor3=0,num=0;
                        for(String coordenada: l0){
                            if(cont==0){
                                valor1=Integer.parseInt(coordenada);
                            }
                            if(cont==1){
                                valor2=Integer.parseInt(coordenada);
                            }
                            if(cont==2){
                                num++;
                                valor3=Integer.parseInt(coordenada)*4;
                               //if(num<1000){
                                    Punto p=new Punto(valor1,valor2,valor3);
                                    nube3D0h.add(p);
                                //}
                                cont=-1;
                            }
                            cont++;
                        }
                    }
                    count++;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
                // En el finally cerramos el fichero
                try{
                    if( null != fr ){
                        fr.close();
                    }
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }




            String s1 = null;
            try {
                // run the Unix "ps -ef" command
                // using the Runtime exec method:
                Process p = Runtime.getRuntime().exec("python /home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/48_48h/48_48_3D.py");

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(p.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command:\n");
                while ((s1 = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s1 = stdError.readLine()) != null) {
                    System.out.println(s);
                }

            }
            catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
            }

            File archivo1 = null;
            FileReader fr1 = null;
            BufferedReader br1 = null;
            try {
                // Apertura del fichero y creacion de BufferedReader para poder
                // hacer una lectura comoda (disponer del metodo readLine()).
                archivo1 = new File ("/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/48_48h/datos2.txt");
                fr1 = new FileReader (archivo1);
                br1 = new BufferedReader(fr1);

                // Lectura del fichero
                String linea; int count=0;
                while((linea=br1.readLine())!=null){
                    if(count==0){
                        String[] l0 = linea.split(" ");
                        int i=0;
                        for(String n0:l0){
                            nGFP_1_3D[i]=Integer.parseInt(n0);
                            System.out.println("N0-"+nGFP_1_3D[i]);
                            i++;
                        }
                    }
                    if(count==1){
                        String[] l0 = linea.split(" ");
                        int i=0;
                        for(String n0:l0){
                            meanGFP_1_3D[i]=Double.parseDouble(n0);
                            System.out.println("M0-"+meanGFP_1_3D[i]);
                            i++;
                        }
                    }
                    if(count==2){
                        umbral1=Integer.parseInt(linea);
                        System.out.println("umbral-"+umbral1);
                    }
                    if(count==3){
                        String modLinea=linea.replace("[","");
                        String modLinea1=modLinea.replace("]","");
                        String modLinea2=modLinea1.replace("(","");
                        String modLinea3=modLinea2.replace(")","");
                        String modLinea4=modLinea3.replace(",","");
                        String[] l0 = modLinea4.split(" ");
                        int cont=0, valor1=0,valor2=0,valor3=0,num=0;
                        for(String coordenada: l0){
                            if(cont==0){
                                valor1=Integer.parseInt(coordenada);
                            }
                            if(cont==1){
                                valor2=Integer.parseInt(coordenada);
                            }
                            if(cont==2){
                                num++;
                                valor3=Integer.parseInt(coordenada)*4;
                                if(num<1000){
                                    Punto p=new Punto(valor1,valor2,valor3);
                                    nube3D48h_72h.add(p);
                                }
                                cont=-1;
                            }
                            cont++;
                        }
                    }
                    count++;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
                // En el finally cerramos el fichero
                try{
                    if( null != fr1 ){
                        fr1.close();
                    }
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }

            umbral=umbral0;
            if(umbral<umbral1){
               umbral=umbral1;
            }
            int posUmbral=(int)umbral/paso;
            double FP=(nGFP_1_3D[posUmbral]*meanGFP_1_3D[posUmbral])/(nGFP_0_3D[posUmbral]*meanGFP_0_3D[posUmbral]);
            resultados.setFactorProliferacion(FP);
            resultados.setUmbral(umbral);
            resultados.setNGFP_0(nGFP_0_3D);
            resultados.setNGFP_1(nGFP_1_3D);
            resultados.setMeanGFP_0(meanGFP_0_3D);
            resultados.setMeanGFP_1(meanGFP_1_3D);
            System.out.println("Nube de puntos:");
            for(Punto x:nube3D0h){
                System.out.println("("+x.getX()+" "+x.getY()+" "+x.getZ()+")-"+nube3D0h.size());
            }
            System.out.println("Tamaño-"+nube3D0h.size());
            sesion.setAttribute("nubePtos3D0h",nube3D0h);
            sesion.setAttribute("nubePtos3D48h_72h",nube3D48h_72h);
            sesion.setAttribute("resultados",resultados);
            sesion.setAttribute("posicionUmbral",posUmbral);
            sesion.setAttribute("fin",fin);
            sesion.setAttribute("paso",paso);
            goToPage("procesado3D.jsp", request, response);

        } else if (request.getParameter("procesado-3D") != null) {
            goToPage("procesado3D.jsp", request, response);

        } else {
            goToPage("vista-erro.jsp", request, response);
        }
    }

    public void goToPage(String address, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*String url = "";
        if (!address.equals("index.jsp")) {
            url = "vistas/";
        }
        RequestDispatcher view = request.getRequestDispatcher(url + address);*/
        RequestDispatcher view = request.getRequestDispatcher(address);
        view.forward(request, response);
    }

}

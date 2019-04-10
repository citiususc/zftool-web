import org.opencv.core.Core;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
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

        if (request.getParameter("carga-imaxes") != null) {
            Analise resultados = (Analise)sesion.getAttribute("resultados");
            sesion.setAttribute("fin",request.getParameter("Fin") );
            sesion.setAttribute("paso",request.getParameter("Paso") );
            int fin=Integer.parseInt(request.getParameter("Fin"));
            int paso=Integer.parseInt(request.getParameter("Paso"));
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
                           /* Procesado inicial=new Procesado(); String carpeta="Images";
                            Analise aux=inicial.procesado2D(carpeta,pez,h0,h1,fin, paso);
                            resultados.setMeanGFP_0(aux.getMeanGFP_0());
                            resultados.setMeanGFP_1(aux.getMeanGFP_1());
                            resultados.setNGFP_0(aux.getNGFP_0());
                            resultados.setNGFP_1(aux.getNGFP_1());
                            resultados.setFactorProliferacion(aux.getFactorProliferacion());
                            resultados.setUmbral(aux.getUmbral());*/
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
                            Procesado inicial=new Procesado(); String carpeta="Images";
                            Analise aux=inicial.procesado2D(carpeta,pez,h0,h1,fin, paso);
                            resultados.setMeanGFP_0(aux.getMeanGFP_0());
                            resultados.setMeanGFP_1(aux.getMeanGFP_1());
                            resultados.setNGFP_0(aux.getNGFP_0());
                            resultados.setNGFP_1(aux.getNGFP_1());
                            resultados.setFactorProliferacion(aux.getFactorProliferacion());
                            resultados.setUmbral(aux.getUmbral());
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

import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Procesado {



    public Analise  procesado2D(String ruta, String pez, String hInicial, String hFinal, int fin, int paso) {
            System.loadLibrary("/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/TFG_ZebraFish/src/main/webapp/WEB-INF/lib/libopencv_java249.so");
            Analise resultados=new Analise();
            String inputfolder, fish, hour_in, hour_end;
            inputfolder=ruta;
            fish=pez;
            hour_in=hInicial;
            hour_end=hFinal;

            String image_bw_0, image_gfp_0, image_bw_1, image_gfp_1;

            image_bw_0=inputfolder+"/"+fish+" - "+hour_in+"h bn.tif";
            image_gfp_0=inputfolder+"/"+fish+" - "+hour_in+"h gfp.tif";
            image_bw_1=inputfolder+"/"+fish+" - "+hour_end+"h bn.tif";
            image_gfp_1=inputfolder+"/"+fish+" - "+hour_end+"h gfp.tif";

            Mat aux_f_bw_0 = Highgui.imread(image_bw_0);
            Mat f_bw_0=new Mat();
            aux_f_bw_0.convertTo(f_bw_0, CvType.CV_8UC3);
            //Core.normalize(f_bw_0, f_bw_0, 0.0, 255.0, Core.NORM_MINMAX);

            Mat aux_f_gfp_0 = Highgui.imread(image_gfp_0);
            Mat f_gfp_0=new Mat();
            aux_f_gfp_0.convertTo(f_gfp_0, CvType.CV_8UC3);
            //Core.normalize(f_gfp_0, f_gfp_0, 0.0, 255.0, Core.NORM_MINMAX);

            Mat aux_f_bw_1 = Highgui.imread(image_bw_1);
            Mat f_bw_1=new Mat();
            aux_f_bw_1.convertTo(f_bw_1, CvType.CV_8UC3);
            //Core.normalize(f_bw_1, f_bw_1, 0.0, 255.0, Core.NORM_MINMAX);

            Mat aux_f_gfp_1 = Highgui.imread(image_gfp_1);
            Mat f_gfp_1=new Mat();
            aux_f_gfp_1.convertTo(f_gfp_1, CvType.CV_8UC3);
            //Core.normalize(f_gfp_1, f_gfp_1, 0.0, 255.0, Core.NORM_MINMAX);

            double correc=1;
            Mat f_gfp_0_c = new Mat(f_gfp_0.rows(),f_gfp_0.cols(), CvType.CV_8UC1);
            Core.multiply(f_gfp_0, new Scalar(correc), f_gfp_0_c);
            Mat f_gfp_1_c = new Mat(f_gfp_1.rows(),f_gfp_1.cols(), CvType.CV_8UC1);
            Core.multiply(f_gfp_1, new Scalar(correc), f_gfp_1_c);

            int ndata=0;
            double[] thresholds=new double[2];
            int[] nGFP_0=new int[fin/paso+1];
            int[] nGFP_1=new int[fin/paso+1];
            double[] meanGFP_0=new double[fin/paso+1];
            double[] meanGFP_1=new double[fin/paso+1];

            for(int threshold=0;threshold<=fin; threshold+=paso)
            {
                Mat  aux_f_gfp_0_c= new Mat();
                f_gfp_0_c.copyTo(aux_f_gfp_0_c);
                Mat  maskG_0= new Mat();
                Imgproc.threshold(aux_f_gfp_0_c,  maskG_0, threshold, 1 , Imgproc.THRESH_BINARY);


                Mat  aux_f_gfp_1_c= new Mat();
                f_gfp_1_c.copyTo(aux_f_gfp_1_c);
                Mat  maskG_1= new Mat();
                Imgproc.threshold(aux_f_gfp_1_c,  maskG_1, threshold, 1 , Imgproc.THRESH_BINARY);
                ndata=ndata+1;

                Mat  mod_maskG_0= new Mat();Mat  aux_maskG_0= new Mat();Mat  aux1_maskG_0= new Mat();
                maskG_0.copyTo(aux_maskG_0);
                Core.normalize(aux_maskG_0, aux1_maskG_0, 0.0, 255.0, Core.NORM_MINMAX);
                Imgproc.cvtColor(aux1_maskG_0,mod_maskG_0, Imgproc.COLOR_RGB2GRAY);

                Mat  mod_maskG_1= new Mat();Mat  aux_maskG_1= new Mat();Mat  aux1_maskG_1= new Mat();
                maskG_1.copyTo(aux_maskG_1);
                Core.normalize(aux_maskG_1,aux1_maskG_1, 0.0, 255.0, Core.NORM_MINMAX);
                Imgproc.cvtColor(aux1_maskG_1,mod_maskG_1, Imgproc.COLOR_RGB2GRAY);

                int sum_maskG_0= Core.countNonZero(mod_maskG_0);
                nGFP_0[ndata-1]=sum_maskG_0;
                int sum_maskG_1= Core.countNonZero(mod_maskG_1);
                nGFP_1[ndata-1]=sum_maskG_1;

                Mat fGmascara_0= new Mat();
                Core.multiply(maskG_0,aux_f_gfp_0_c,fGmascara_0);

                Mat  aux_fGmascara_0= new Mat();
                Imgproc.cvtColor(fGmascara_0,aux_fGmascara_0, Imgproc.COLOR_RGB2GRAY);
                Scalar suma=Core.mean(fGmascara_0,aux_fGmascara_0);
                double media_fGmascara_0=suma.val[0];
                meanGFP_0[ndata-1]=media_fGmascara_0;

                Mat fGmascara_1= new Mat();
                Core.multiply(maskG_1,aux_f_gfp_1_c,fGmascara_1);

                Mat  aux_fGmascara_1= new Mat();
                Imgproc.cvtColor(fGmascara_1,aux_fGmascara_1, Imgproc.COLOR_RGB2GRAY);
                Scalar suma1=Core.mean(fGmascara_1,aux_fGmascara_1);
                double media_fGmascara_1=suma1.val[0];
                meanGFP_1[ndata-1]=media_fGmascara_1;
                //System.out.println("threshold-"+threshold+" meanGFP_1-"+media_fGmascara_1);


                Mat aux_0=fGmascara_0.clone();
                Imgproc.cvtColor(fGmascara_0,aux_0, Imgproc.COLOR_RGB2GRAY);
                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                Mat dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
                Scalar red = new Scalar(0,0,255);
                Mat hierarchy=new Mat();
                Imgproc.findContours(aux_0, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
                aux_0.release();
                Imgproc.drawContours(dest_0, contours, -1, red);
            /*for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(dest_0, contours, i,red,2,8,hierarchy,0,new Point());
            }*/

                Mat destino_0=new Mat();
                Mat tipo0_1=new Mat();Mat tipo0_2=new Mat();Mat tipo0_3=new Mat();
                Mat mult0=new Mat();
                Core.multiply(aux_f_gfp_0_c,new Scalar(0),mult0);
                List<Mat>channels1=new ArrayList<Mat>();
                List<Mat>channels2=new ArrayList<Mat>();
                List<Mat>channels3=new ArrayList<Mat>();
                // Imgproc.cvtColor(dest_0,tipo0_1, Imgproc.COLOR_RGB2GRAY);
                //Imgproc.cvtColor(aux_f_gfp_0_c,tipo0_2, Imgproc.COLOR_RGB2GRAY);
                //Imgproc.cvtColor(mult0,tipo0_3, Imgproc.COLOR_RGB2GRAY);
                Core.split(dest_0,channels1);tipo0_1=channels1.get(2).clone();
                Core.split(aux_f_gfp_0_c,channels2);tipo0_2=channels2.get(0).clone();
                Core.split(mult0,channels3);tipo0_3=channels3.get(0).clone();
                List<Mat>lista0=new ArrayList<Mat>();
                lista0.add(tipo0_3);
                lista0.add(tipo0_2);
                lista0.add(tipo0_1);
                Core.merge(lista0,destino_0);
                String title_0="PerimeterGFP>"+threshold+"at0hpi.jpg";
                Highgui.imwrite(title_0,destino_0);

                Mat aux_1=fGmascara_1.clone();
                Imgproc.cvtColor(fGmascara_1,aux_1, Imgproc.COLOR_RGB2GRAY);
                List<MatOfPoint> regions = new ArrayList<MatOfPoint>();
                Mat dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
                Mat hierarchy2=new Mat();
                Imgproc.findContours(aux_1, regions, hierarchy2, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
                aux_1.release();
                Imgproc.drawContours(dest_1, regions, -1, red);
            /*for (int j = 0; j < regions.size(); j++) {
                Imgproc.drawContours(dest_1, regions, j,red,2,8,hierarchy2,0,new Point());
            }*/

                Mat destino_1=new Mat();
                Mat tipo1_1=new Mat();Mat tipo1_2=new Mat();Mat tipo1_3=new Mat();
                Mat mult1=new Mat();
                Core.multiply(aux_f_gfp_1_c,new Scalar(0),mult1);
                List<Mat>channels4=new ArrayList<Mat>();
                List<Mat>channels5=new ArrayList<Mat>();
                List<Mat>channels6=new ArrayList<Mat>();
                Core.split(dest_1,channels4);tipo1_1=channels4.get(2).clone();
                Core.split(aux_f_gfp_1_c,channels5);tipo1_2=channels5.get(0).clone();
                Core.split(mult1,channels6);tipo1_3=channels6.get(0).clone();
                //Imgproc.cvtColor(dest_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
                //Imgproc.cvtColor(aux_f_gfp_1_c,tipo1_2, Imgproc.COLOR_RGB2GRAY);
                //Imgproc.cvtColor(mult1,tipo1_3, Imgproc.COLOR_RGB2GRAY);
                List<Mat>lista1=new ArrayList<Mat>();
                lista1.add(tipo1_3);
                lista1.add(tipo1_2);
                lista1.add(tipo1_1);
                Core.merge(lista1,destino_1);
                String title_1="PerimeterGFP>"+threshold+"at"+hour_end+"hpi.jpg";
                Highgui.imwrite(title_1,destino_1);
            }
            resultados.setNGFP_0(nGFP_0);
            resultados.setNGFP_1(nGFP_1);
            resultados.setMeanGFP_0(meanGFP_0);
            resultados.setMeanGFP_1(meanGFP_1);


            double[] comparation_0=new double[ndata-1];
            double[] comparation_1=new double[ndata-1];
            for(int k=0;k<ndata-1; k++){
                comparation_0[k]=(double)nGFP_0[k+1]/nGFP_0[k];
                comparation_1[k]=(double) nGFP_1[k+1]/nGFP_1[k];
            }
            double difthresh=1;

            double[] found=new double[2];
            found[0]=0;found[1]=0;
            while(!(found[0]!=0 && found[1]!=0)){
                found[0]=0;found[1]=0;
                difthresh=difthresh-0.005;
                int counter_0=0; int counter_1=0;
                for(int k=0;k<ndata-1; k++){
                    if(comparation_0[k]>difthresh){
                        counter_0=counter_0+1;
                        if(counter_0==3){
                            thresholds[0]=(k-1)*5;
                            found[0]=1;
                        }
                    }else{
                        counter_0=0;
                    }
                    if(comparation_1[k]>difthresh){
                        counter_1=counter_1+1;
                        if(counter_1==3){
                            thresholds[1]=(k-1)*5;
                            found[1]=1;
                        }
                    }else{
                        counter_1=0;
                    }
                }
            }

            System.out.println("% of change between thresholds: "+difthresh*100+"%\n");
            double maximo=thresholds[0];
            if(thresholds[1]>thresholds[0]){
                maximo=thresholds[1];
            }
            System.out.println("Final common threshold = "+maximo+" (maximum of ["+thresholds[0]+","+thresholds[1]+"])\n");
            double finalthreshold=maximo;
            resultados.setUmbral((int)finalthreshold);

            int pos=(int)finalthreshold/paso;
            double PI=(nGFP_1[pos]*meanGFP_1[pos])/(nGFP_0[pos]*meanGFP_0[pos]);
            System.out.println("Factor de proliferación: "+ PI);

            resultados.setFactorProliferacion(PI);

            double threshold=0;

            Mat maskG_0h_t0=new Mat();
            Imgproc.cvtColor(f_gfp_0_c, maskG_0h_t0, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_0h_t0,  maskG_0h_t0, threshold, 1 , Imgproc.THRESH_BINARY);

            Mat maskG_1h_t0= new Mat();
            Imgproc.cvtColor(f_gfp_1_c, maskG_1h_t0, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_1h_t0,  maskG_1h_t0, threshold, 1 , Imgproc.THRESH_BINARY);

            Mat aux_0=maskG_0h_t0.clone();
            List<MatOfPoint> perimR_0h = new ArrayList<MatOfPoint>();
            Mat dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
            Scalar red = new Scalar(0,0,255);
            Imgproc.findContours(aux_0, perimR_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_0.release();
            Imgproc.drawContours(dest_0, perimR_0h, -1, red);
        /*for (int j = 0; j < perimR_0h.size(); j++) {
            Imgproc.drawContours(dest_0, perimR_0h, j,red,2);
        }*/

            threshold=finalthreshold;
            Mat maskG_0h_tf=new Mat();
            Imgproc.cvtColor(f_gfp_0_c, maskG_0h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_0h_tf,  maskG_0h_tf, threshold, 1 , Imgproc.THRESH_BINARY);

            Mat maskG_1h_tf= new Mat();
            Imgproc.cvtColor(f_gfp_1_c, maskG_1h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_1h_tf,  maskG_1h_tf, threshold, 1 , Imgproc.THRESH_BINARY);

            Mat aux_1=maskG_1h_t0.clone();
            List<MatOfPoint> perimR_1h = new ArrayList<MatOfPoint>();
            Mat dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
            Imgproc.findContours(aux_1, perimR_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_1.release();
            Imgproc.drawContours(dest_1, perimR_1h, -1, red);
        /*for (int i = 0; i < perimR_1h.size(); i++) {
            Imgproc.drawContours(dest_1, perimR_1h, i,red,2);
        }*/
            Mat resta0=new Mat();
            Core.subtract(maskG_0h_t0,maskG_0h_tf,resta0);
            List<MatOfPoint> perimB_0h = new ArrayList<MatOfPoint>();
            Mat dest_2 = Mat.zeros(resta0.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta0, perimB_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta0.release();
            Imgproc.drawContours(dest_2, perimB_0h, -1, red);
        /*for (int j = 0; j < perimB_0h.size(); j++) {
            Imgproc.drawContours(dest_2, perimB_0h, j,red,2);
        }*/

            Mat resta1=new Mat();
            Core.subtract(maskG_1h_t0,maskG_1h_tf,resta1);
            List<MatOfPoint> perimB_1h = new ArrayList<MatOfPoint>();
            Mat dest_3 = Mat.zeros(resta1.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta1, perimB_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta1.release();
            Imgproc.drawContours(dest_3, perimB_1h, -1, red);
        /*for (int j = 0; j < perimB_1h.size(); j++) {
            Imgproc.drawContours(dest_3, perimB_1h, j,red,2);
        }*/

            Mat destino_1=new Mat();
            Mat tipo0_1=new Mat();Mat tipo0_2=new Mat();Mat tipo0_3=new Mat();
            Mat suma0_1=new Mat();Mat suma0_2=new Mat();Mat suma0_3=new Mat();
            List<Mat>channels7=new ArrayList<Mat>();
            List<Mat>channels8=new ArrayList<Mat>();
            List<Mat>channels9=new ArrayList<Mat>();
            Core.add(f_bw_0,dest_0,suma0_1);
            Core.add(f_bw_0,f_gfp_0_c,suma0_2);
            Core.add(f_bw_0,dest_2,suma0_3);
            Core.split(suma0_1,channels7);tipo0_1=channels7.get(2).clone();
            Core.split(suma0_2,channels8);tipo0_2=channels8.get(0).clone();
            Core.split(suma0_3,channels9);tipo0_3=channels9.get(2).clone();
            //Imgproc.cvtColor(suma0_1,tipo0_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_2,tipo0_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_3,tipo0_3, Imgproc.COLOR_RGB2GRAY);
            List<Mat>lista1=new ArrayList<Mat>();
            lista1.add(tipo0_3);
            lista1.add(tipo0_2);
            lista1.add(tipo0_1);
            Core.merge(lista1,destino_1);
            String title_1="0hpi-threshold=0(pink)-finalthreshold(blue).jpg";
            Highgui.imwrite(title_1,destino_1);

            Mat destino_2=new Mat();
            Mat tipo1_1=new Mat();Mat tipo1_2=new Mat();Mat tipo1_3=new Mat();
            Mat suma1_1=new Mat();Mat suma1_2=new Mat();Mat suma1_3=new Mat();
            List<Mat>channels10=new ArrayList<Mat>();
            List<Mat>channels11=new ArrayList<Mat>();
            List<Mat>channels12=new ArrayList<Mat>();
            Core.add(f_bw_1,dest_1,suma1_1);
            Core.add(f_bw_1, f_gfp_1_c,suma1_2);
            Core.add(f_bw_1,dest_3,suma1_3);
            Core.split(suma1_1,channels10);tipo1_1=channels10.get(2).clone();
            Core.split(suma1_2,channels11);tipo1_2=channels11.get(0).clone();
            Core.split(suma1_3,channels12);tipo1_3=channels12.get(2).clone();
            //Imgproc.cvtColor(suma1_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_2,tipo1_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_3,tipo1_3, Imgproc.COLOR_RGB2GRAY);
            List<Mat>lista2=new ArrayList<Mat>();
            lista2.add(tipo1_3);
            lista2.add(tipo1_2);
            lista2.add(tipo1_1);
            Core.merge(lista2,destino_2);
            String title_2="24-48-72hpi-threshold=0(pink)-finalthreshold(blue).jpg";
            Highgui.imwrite(title_2,destino_2);
            return resultados;
    }
}
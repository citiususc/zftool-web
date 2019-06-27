package es.usc.citius.zebrafish.modelo.procesamiento;


import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Procesado2D {
    private Mat aux_f_bw_0, f_bw_0, aux_f_gfp_0, f_gfp_0, aux_f_bw_1, f_bw_1, aux_f_gfp_1, f_gfp_1, f_gfp_0_c, f_gfp_1_c, maskG_1, aux_f_gfp_0_c, maskG_0, aux_f_gfp_1_c, mod_maskG_0, aux_maskG_0, aux1_maskG_0, mod_maskG_1;
    private Mat aux_maskG_1, aux1_maskG_1, fGmascara_0, fGmascara_1, aux_fGmascara_0, aux_fGmascara_1, maskG_0h_t0, maskG_1h_t0, aux_0, dest_0, maskG_0h_tf, maskG_1h_tf, aux_1, dest_1, resta0, dest_2, resta1, dest_3, destino_1;
    private Mat suma0_1, suma0_2, suma0_3, destino_2, suma1_1, suma1_2, suma1_3, tipo0_1, tipo0_2, tipo0_3, tipo1_1, tipo1_2, tipo1_3;
    private Scalar suma, suma1, red;
    private List<MatOfPoint> perimR_0h, perimR_1h, perimB_0h, perimB_1h;
    private List<Mat> channels7, channels8, channels9, channels10, channels11, channels12, lista1, lista2;
    private double correc, media_fGmascara_0, media_fGmascara_1, difthresh, maximo, finalthreshold, PI;
    private int ndata, sum_maskG_0, sum_maskG_1, pos;
    private double[] thresholds, meanGFP_0, meanGFP_1, comparation_0, comparation_1, found;
    private int[] nGFP_0, nGFP_1;
    private String title_1, title_2;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Procesado2D() { }

    public void realizarProcesado2D(byte[] bytes0hg, byte[] bytes0hf, byte[] bytes72hg, byte[] bytes72hf, int fin, int paso, String rutaCarpetaImagenes, Analisis resultados) {
        resultados.setFin(fin);
        resultados.setPaso(paso);
        resultados.calcularRange();

        aux_f_bw_0 = Highgui.imdecode(new MatOfByte(bytes0hg), Highgui.CV_LOAD_IMAGE_COLOR);
        f_bw_0 = new Mat();
        aux_f_bw_0.convertTo(f_bw_0, CvType.CV_8UC3);
        //Core.normalize(f_bw_0, f_bw_0, 0.0, 255.0, Core.NORM_MINMAX);

        aux_f_gfp_0 = Highgui.imdecode(new MatOfByte(bytes0hf), Highgui.CV_LOAD_IMAGE_COLOR);
        f_gfp_0 = new Mat();
        aux_f_gfp_0.convertTo(f_gfp_0, CvType.CV_8UC3);
        //Core.normalize(f_gfp_0, f_gfp_0, 0.0, 255.0, Core.NORM_MINMAX);

        aux_f_bw_1 = Highgui.imdecode(new MatOfByte(bytes72hg), Highgui.CV_LOAD_IMAGE_COLOR);
        f_bw_1 = new Mat();
        aux_f_bw_1.convertTo(f_bw_1, CvType.CV_8UC3);
        //Core.normalize(f_bw_1, f_bw_1, 0.0, 255.0, Core.NORM_MINMAX);

        aux_f_gfp_1 = Highgui.imdecode(new MatOfByte(bytes72hf), Highgui.CV_LOAD_IMAGE_COLOR);
        f_gfp_1 = new Mat();
        aux_f_gfp_1.convertTo(f_gfp_1, CvType.CV_8UC3);
        //Core.normalize(f_gfp_1, f_gfp_1, 0.0, 255.0, Core.NORM_MINMAX);

        correc = 1;
        f_gfp_0_c = new Mat(f_gfp_0.rows(), f_gfp_0.cols(), CvType.CV_8UC1);
        Core.multiply(f_gfp_0, new Scalar(correc), f_gfp_0_c);
        f_gfp_1_c = new Mat(f_gfp_1.rows(), f_gfp_1.cols(), CvType.CV_8UC1);
        Core.multiply(f_gfp_1, new Scalar(correc), f_gfp_1_c);

        ndata = 0;
        thresholds = new double[2];
        nGFP_0 = new int[fin / paso + 1];
        nGFP_1 = new int[fin / paso + 1];
        meanGFP_0 = new double[fin / paso + 1];
        meanGFP_1 = new double[fin / paso + 1];

        for (int threshold = 0; threshold <= fin; threshold += paso) {
            aux_f_gfp_0_c = new Mat();
            f_gfp_0_c.copyTo(aux_f_gfp_0_c);
            maskG_0 = new Mat();
            Imgproc.threshold(aux_f_gfp_0_c, maskG_0, threshold, 1, Imgproc.THRESH_BINARY);

            aux_f_gfp_1_c = new Mat();
            f_gfp_1_c.copyTo(aux_f_gfp_1_c);
            maskG_1 = new Mat();
            Imgproc.threshold(aux_f_gfp_1_c, maskG_1, threshold, 1, Imgproc.THRESH_BINARY);
            ndata = ndata + 1;

            mod_maskG_0 = new Mat();
            aux_maskG_0 = new Mat();
            aux1_maskG_0 = new Mat();
            maskG_0.copyTo(aux_maskG_0);
            Core.normalize(aux_maskG_0, aux1_maskG_0, 0.0, 255.0, Core.NORM_MINMAX);
            Imgproc.cvtColor(aux1_maskG_0, mod_maskG_0, Imgproc.COLOR_RGB2GRAY);

            mod_maskG_1 = new Mat();
            aux_maskG_1 = new Mat();
            aux1_maskG_1 = new Mat();
            maskG_1.copyTo(aux_maskG_1);
            Core.normalize(aux_maskG_1, aux1_maskG_1, 0.0, 255.0, Core.NORM_MINMAX);
            Imgproc.cvtColor(aux1_maskG_1, mod_maskG_1, Imgproc.COLOR_RGB2GRAY);

            sum_maskG_0 = Core.countNonZero(mod_maskG_0);
            nGFP_0[ndata - 1] = sum_maskG_0;
            sum_maskG_1 = Core.countNonZero(mod_maskG_1);
            nGFP_1[ndata - 1] = sum_maskG_1;

            fGmascara_0 = new Mat();
            Core.multiply(maskG_0, aux_f_gfp_0_c, fGmascara_0);

            aux_fGmascara_0 = new Mat();
            Imgproc.cvtColor(fGmascara_0, aux_fGmascara_0, Imgproc.COLOR_RGB2GRAY);
            suma = Core.mean(fGmascara_0, aux_fGmascara_0);
            media_fGmascara_0 = suma.val[0];
            meanGFP_0[ndata - 1] = media_fGmascara_0;

            fGmascara_1 = new Mat();
            Core.multiply(maskG_1, aux_f_gfp_1_c, fGmascara_1);

            aux_fGmascara_1 = new Mat();
            Imgproc.cvtColor(fGmascara_1, aux_fGmascara_1, Imgproc.COLOR_RGB2GRAY);
            suma1 = Core.mean(fGmascara_1, aux_fGmascara_1);
            media_fGmascara_1 = suma1.val[0];
            meanGFP_1[ndata - 1] = media_fGmascara_1;
            //System.out.println("threshold-"+threshold+" meanGFP_1-"+media_fGmascara_1);


           /* Mat aux_0 = fGmascara_0.clone();
            Imgproc.cvtColor(fGmascara_0, aux_0, Imgproc.COLOR_RGB2GRAY);
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
            Scalar red = new Scalar(0, 0, 255);
            Mat hierarchy = new Mat();
            Imgproc.findContours(aux_0, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_0.release();
            Imgproc.drawContours(dest_0, contours, -1, red);

            Mat destino_0 = new Mat();
            Mat tipo0_1 = new Mat();
            Mat tipo0_2 = new Mat();
            Mat tipo0_3 = new Mat();
            Mat mult0 = new Mat();
            Core.multiply(aux_f_gfp_0_c, new Scalar(0), mult0);
            List<Mat> channels1 = new ArrayList<Mat>();
            List<Mat> channels2 = new ArrayList<Mat>();
            List<Mat> channels3 = new ArrayList<Mat>();
            // Imgproc.cvtColor(dest_0,tipo0_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(aux_f_gfp_0_c,tipo0_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(mult0,tipo0_3, Imgproc.COLOR_RGB2GRAY);
            Core.split(dest_0, channels1);
            tipo0_1 = channels1.get(2).clone();
            Core.split(aux_f_gfp_0_c, channels2);
            tipo0_2 = channels2.get(0).clone();
            Core.split(mult0, channels3);
            tipo0_3 = channels3.get(0).clone();
            List<Mat> lista0 = new ArrayList<Mat>();
            lista0.add(tipo0_3);
            lista0.add(tipo0_2);
            lista0.add(tipo0_1);
            Core.merge(lista0, destino_0);
            String title_0 = "PerimeterGFP>" + threshold + "at0hpi.jpg";
            //Highgui.imwrite(title_0, destino_0);

            Mat aux_1 = fGmascara_1.clone();
            Imgproc.cvtColor(fGmascara_1, aux_1, Imgproc.COLOR_RGB2GRAY);
            List<MatOfPoint> regions = new ArrayList<MatOfPoint>();
            Mat dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
            Mat hierarchy2 = new Mat();
            Imgproc.findContours(aux_1, regions, hierarchy2, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_1.release();
            Imgproc.drawContours(dest_1, regions, -1, red);

            Mat destino_1 = new Mat();
            Mat tipo1_1 = new Mat();
            Mat tipo1_2 = new Mat();
            Mat tipo1_3 = new Mat();
            Mat mult1 = new Mat();
            Core.multiply(aux_f_gfp_1_c, new Scalar(0), mult1);
            List<Mat> channels4 = new ArrayList<Mat>();
            List<Mat> channels5 = new ArrayList<Mat>();
            List<Mat> channels6 = new ArrayList<Mat>();
            Core.split(dest_1, channels4);
            tipo1_1 = channels4.get(2).clone();
            Core.split(aux_f_gfp_1_c, channels5);
            tipo1_2 = channels5.get(0).clone();
            Core.split(mult1, channels6);
            tipo1_3 = channels6.get(0).clone();
            //Imgproc.cvtColor(dest_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(aux_f_gfp_1_c,tipo1_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(mult1,tipo1_3, Imgproc.COLOR_RGB2GRAY);
            List<Mat> lista1 = new ArrayList<Mat>();
            lista1.add(tipo1_3);
            lista1.add(tipo1_2);
            lista1.add(tipo1_1);
            Core.merge(lista1, destino_1);
            String title_1 = "PerimeterGFP>" + threshold + "at24-48-72hpi.jpg";*/
            //Highgui.imwrite(title_1, destino_1);
        }
        resultados.setNGFP_0(nGFP_0);
        resultados.setNGFP_1(nGFP_1);
        resultados.setMeanGFP_0(meanGFP_0);
        resultados.setMeanGFP_1(meanGFP_1);


        comparation_0 = new double[ndata - 1];
        comparation_1 = new double[ndata - 1];
        for (int k = 0; k < ndata - 1; k++) {
            comparation_0[k] = (double) nGFP_0[k + 1] / nGFP_0[k];
            comparation_1[k] = (double) nGFP_1[k + 1] / nGFP_1[k];
        }
        difthresh = 1;

        found = new double[2];
        found[0] = 0;
        found[1] = 0;
        while (!(found[0] != 0 && found[1] != 0)) {
            found[0] = 0;
            found[1] = 0;
            difthresh = difthresh - 0.005;
            int counter_0 = 0;
            int counter_1 = 0;
            for (int k = 0; k < ndata - 1; k++) {
                if (comparation_0[k] > difthresh) {
                    counter_0 = counter_0 + 1;
                    if (counter_0 == 3) {
                        thresholds[0] = (k - 1) * resultados.getPaso();
                        found[0] = 1;
                    }
                } else {
                    counter_0 = 0;
                }
                if (comparation_1[k] > difthresh) {
                    counter_1 = counter_1 + 1;
                    if (counter_1 == 3) {
                        thresholds[1] = (k - 1) * resultados.getPaso();
                        found[1] = 1;
                    }
                } else {
                    counter_1 = 0;
                }
            }
        }

        System.out.println("% of change between thresholds: " + difthresh * 100 + "%\n");
        maximo=Math.max(thresholds[0],thresholds[1]);
        System.out.println("Final common threshold = " + maximo + " (maximum of [" + thresholds[0] + "," + thresholds[1] + "])\n");
        finalthreshold = maximo;
        resultados.setUmbral((int) finalthreshold);
        resultados.calcularArrayUmbralElegido();
        resultados.setPosicionUmbral(resultados.getUmbral() / resultados.getPaso());

        pos = (int) finalthreshold / paso;
        PI = (nGFP_1[pos] * meanGFP_1[pos]) / (nGFP_0[pos] * meanGFP_0[pos]);
        System.out.println("Factor de proliferación: " + PI);
        resultados.setFactorProliferacion(PI);

        int threshold = 0;
        maskG_0h_t0 = new Mat();
        Imgproc.cvtColor(f_gfp_0_c, maskG_0h_t0, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(maskG_0h_t0, maskG_0h_t0, threshold, 1, Imgproc.THRESH_BINARY);

        maskG_1h_t0 = new Mat();
        Imgproc.cvtColor(f_gfp_1_c, maskG_1h_t0, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(maskG_1h_t0, maskG_1h_t0, threshold, 1, Imgproc.THRESH_BINARY);

        aux_0 = maskG_0h_t0.clone();
        perimR_0h = new ArrayList<MatOfPoint>();
        dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
        red = new Scalar(0, 0, 255);
        Imgproc.findContours(aux_0, perimR_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        aux_0.release();
        Imgproc.drawContours(dest_0, perimR_0h, -1, red);

        //threshold = finalthreshold;
        for (threshold = 0; threshold <= fin; threshold += paso) {
            maskG_0h_tf = new Mat();
            Imgproc.cvtColor(f_gfp_0_c, maskG_0h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_0h_tf, maskG_0h_tf, threshold, 1, Imgproc.THRESH_BINARY);

            maskG_1h_tf = new Mat();
            Imgproc.cvtColor(f_gfp_1_c, maskG_1h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_1h_tf, maskG_1h_tf, threshold, 1, Imgproc.THRESH_BINARY);

            aux_1 = maskG_1h_t0.clone();
            perimR_1h = new ArrayList<MatOfPoint>();
            dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
            Imgproc.findContours(aux_1, perimR_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_1.release();
            Imgproc.drawContours(dest_1, perimR_1h, -1, red);

            resta0 = new Mat();
            Core.subtract(maskG_0h_t0, maskG_0h_tf, resta0);
            perimB_0h = new ArrayList<MatOfPoint>();
            dest_2 = Mat.zeros(resta0.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta0, perimB_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta0.release();
            Imgproc.drawContours(dest_2, perimB_0h, -1, red);

            resta1 = new Mat();
            Core.subtract(maskG_1h_t0, maskG_1h_tf, resta1);
            perimB_1h = new ArrayList<MatOfPoint>();
            dest_3 = Mat.zeros(resta1.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta1, perimB_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta1.release();
            Imgproc.drawContours(dest_3, perimB_1h, -1, red);

            destino_1 = new Mat();
            suma0_1 = new Mat();
            suma0_2 = new Mat();
            suma0_3 = new Mat();
            channels7 = new ArrayList<Mat>();
            channels8 = new ArrayList<Mat>();
            channels9 = new ArrayList<Mat>();
            Core.add(f_bw_0, dest_0, suma0_1);
            Core.add(f_bw_0, f_gfp_0_c, suma0_2);
            Core.add(f_bw_0, dest_2, suma0_3);
            Core.split(suma0_1, channels7);
            tipo0_1 = channels7.get(2).clone();
            Core.split(suma0_2, channels8);
            tipo0_2 = channels8.get(0).clone();
            Core.split(suma0_3, channels9);
            tipo0_3 = channels9.get(2).clone();
            //Imgproc.cvtColor(suma0_1,tipo0_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_2,tipo0_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_3,tipo0_3, Imgproc.COLOR_RGB2GRAY);
            lista1 = new ArrayList<Mat>();
            lista1.add(tipo0_3);
            lista1.add(tipo0_2);
            lista1.add(tipo0_1);
            Core.merge(lista1, destino_1);
            title_1 = rutaCarpetaImagenes + File.separator + "Procesado2D" + File.separator + "Procesado0hpi-threshold=" + threshold + ".jpg";
            Highgui.imwrite(title_1, destino_1);

            destino_2 = new Mat();
            suma1_1 = new Mat();
            suma1_2 = new Mat();
            suma1_3 = new Mat();
            channels10 = new ArrayList<Mat>();
            channels11 = new ArrayList<Mat>();
            channels12 = new ArrayList<Mat>();
            Core.add(f_bw_1, dest_1, suma1_1);
            Core.add(f_bw_1, f_gfp_1_c, suma1_2);
            Core.add(f_bw_1, dest_3, suma1_3);
            Core.split(suma1_1, channels10);
            tipo1_1 = channels10.get(2).clone();
            Core.split(suma1_2, channels11);
            tipo1_2 = channels11.get(0).clone();
            Core.split(suma1_3, channels12);
            tipo1_3 = channels12.get(2).clone();
            //Imgproc.cvtColor(suma1_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_2,tipo1_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_3,tipo1_3, Imgproc.COLOR_RGB2GRAY);
            lista2 = new ArrayList<Mat>();
            lista2.add(tipo1_3);
            lista2.add(tipo1_2);
            lista2.add(tipo1_1);
            Core.merge(lista2, destino_2);
            title_2 = rutaCarpetaImagenes + File.separator + "Procesado2D" + File.separator + "Procesado24-48-72hpi-threshold=" + threshold + ".jpg";
            Highgui.imwrite(title_2, destino_2);
        }
    }


    public void realizarProcesado2D(byte[] bytes0hf, byte[] bytes72hf, int fin, int paso, String rutaCarpetaImagenes, Analisis resultados) {
        //Analisis resultados=new Analisis();
        resultados.setFin(fin);
        resultados.setPaso(paso);
        resultados.calcularRange();

        aux_f_gfp_0 = Highgui.imdecode(new MatOfByte(bytes0hf), Highgui.CV_LOAD_IMAGE_COLOR);
        f_gfp_0 = new Mat();
        aux_f_gfp_0.convertTo(f_gfp_0, CvType.CV_8UC3);
        //Core.normalize(f_gfp_0, f_gfp_0, 0.0, 255.0, Core.NORM_MINMAX);

        aux_f_gfp_1 = Highgui.imdecode(new MatOfByte(bytes72hf), Highgui.CV_LOAD_IMAGE_COLOR);
        f_gfp_1 = new Mat();
        aux_f_gfp_1.convertTo(f_gfp_1, CvType.CV_8UC3);
        //Core.normalize(f_gfp_1, f_gfp_1, 0.0, 255.0, Core.NORM_MINMAX);

        correc = 1;
        f_gfp_0_c = new Mat(f_gfp_0.rows(), f_gfp_0.cols(), CvType.CV_8UC1);
        Core.multiply(f_gfp_0, new Scalar(correc), f_gfp_0_c);
        f_gfp_1_c = new Mat(f_gfp_1.rows(), f_gfp_1.cols(), CvType.CV_8UC1);
        Core.multiply(f_gfp_1, new Scalar(correc), f_gfp_1_c);

        ndata = 0;
        thresholds = new double[2];
        nGFP_0 = new int[fin / paso + 1];
        nGFP_1 = new int[fin / paso + 1];
        meanGFP_0 = new double[fin / paso + 1];
        meanGFP_1 = new double[fin / paso + 1];

        for (int threshold = 0; threshold <= fin; threshold += paso) {
            aux_f_gfp_0_c = new Mat();
            f_gfp_0_c.copyTo(aux_f_gfp_0_c);
            maskG_0 = new Mat();
            Imgproc.threshold(aux_f_gfp_0_c, maskG_0, threshold, 1, Imgproc.THRESH_BINARY);


            aux_f_gfp_1_c = new Mat();
            f_gfp_1_c.copyTo(aux_f_gfp_1_c);
            maskG_1 = new Mat();
            Imgproc.threshold(aux_f_gfp_1_c, maskG_1, threshold, 1, Imgproc.THRESH_BINARY);
            ndata = ndata + 1;

            mod_maskG_0 = new Mat();
            aux_maskG_0 = new Mat();
            aux1_maskG_0 = new Mat();
            maskG_0.copyTo(aux_maskG_0);
            Core.normalize(aux_maskG_0, aux1_maskG_0, 0.0, 255.0, Core.NORM_MINMAX);
            Imgproc.cvtColor(aux1_maskG_0, mod_maskG_0, Imgproc.COLOR_RGB2GRAY);

            mod_maskG_1 = new Mat();
            aux_maskG_1 = new Mat();
            aux1_maskG_1 = new Mat();
            maskG_1.copyTo(aux_maskG_1);
            Core.normalize(aux_maskG_1, aux1_maskG_1, 0.0, 255.0, Core.NORM_MINMAX);
            Imgproc.cvtColor(aux1_maskG_1, mod_maskG_1, Imgproc.COLOR_RGB2GRAY);

            sum_maskG_0 = Core.countNonZero(mod_maskG_0);
            nGFP_0[ndata - 1] = sum_maskG_0;
            sum_maskG_1 = Core.countNonZero(mod_maskG_1);
            nGFP_1[ndata - 1] = sum_maskG_1;

            fGmascara_0 = new Mat();
            Core.multiply(maskG_0, aux_f_gfp_0_c, fGmascara_0);

            aux_fGmascara_0 = new Mat();
            Imgproc.cvtColor(fGmascara_0, aux_fGmascara_0, Imgproc.COLOR_RGB2GRAY);
            suma = Core.mean(fGmascara_0, aux_fGmascara_0);
            media_fGmascara_0 = suma.val[0];
            meanGFP_0[ndata - 1] = media_fGmascara_0;

            fGmascara_1 = new Mat();
            Core.multiply(maskG_1, aux_f_gfp_1_c, fGmascara_1);

            aux_fGmascara_1 = new Mat();
            Imgproc.cvtColor(fGmascara_1, aux_fGmascara_1, Imgproc.COLOR_RGB2GRAY);
            suma1 = Core.mean(fGmascara_1, aux_fGmascara_1);
            media_fGmascara_1 = suma1.val[0];
            meanGFP_1[ndata - 1] = media_fGmascara_1;
            //System.out.println("threshold-"+threshold+" meanGFP_1-"+media_fGmascara_1);


            /*Mat aux_0 = fGmascara_0.clone();
            Imgproc.cvtColor(fGmascara_0, aux_0, Imgproc.COLOR_RGB2GRAY);
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
            Scalar red = new Scalar(0, 0, 255);
            Mat hierarchy = new Mat();
            Imgproc.findContours(aux_0, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_0.release();
            Imgproc.drawContours(dest_0, contours, -1, red);

            Mat destino_0 = new Mat();
            Mat tipo0_1 = new Mat();
            Mat tipo0_2 = new Mat();
            Mat tipo0_3 = new Mat();
            Mat mult0 = new Mat();
            Core.multiply(aux_f_gfp_0_c, new Scalar(0), mult0);
            List<Mat> channels1 = new ArrayList<Mat>();
            List<Mat> channels2 = new ArrayList<Mat>();
            List<Mat> channels3 = new ArrayList<Mat>();
            // Imgproc.cvtColor(dest_0,tipo0_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(aux_f_gfp_0_c,tipo0_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(mult0,tipo0_3, Imgproc.COLOR_RGB2GRAY);
            Core.split(dest_0, channels1);
            tipo0_1 = channels1.get(2).clone();
            Core.split(aux_f_gfp_0_c, channels2);
            tipo0_2 = channels2.get(0).clone();
            Core.split(mult0, channels3);
            tipo0_3 = channels3.get(0).clone();
            List<Mat> lista0 = new ArrayList<Mat>();
            lista0.add(tipo0_3);
            lista0.add(tipo0_2);
            lista0.add(tipo0_1);
            Core.merge(lista0, destino_0);
            String title_0 = "PerimeterGFP>" + threshold + "at0hpi.jpg";
            //Highgui.imwrite(title_0, destino_0);

            Mat aux_1 = fGmascara_1.clone();
            Imgproc.cvtColor(fGmascara_1, aux_1, Imgproc.COLOR_RGB2GRAY);
            List<MatOfPoint> regions = new ArrayList<MatOfPoint>();
            Mat dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
            Mat hierarchy2 = new Mat();
            Imgproc.findContours(aux_1, regions, hierarchy2, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_1.release();
            Imgproc.drawContours(dest_1, regions, -1, red);

            Mat destino_1 = new Mat();
            Mat tipo1_1 = new Mat();
            Mat tipo1_2 = new Mat();
            Mat tipo1_3 = new Mat();
            Mat mult1 = new Mat();
            Core.multiply(aux_f_gfp_1_c, new Scalar(0), mult1);
            List<Mat> channels4 = new ArrayList<Mat>();
            List<Mat> channels5 = new ArrayList<Mat>();
            List<Mat> channels6 = new ArrayList<Mat>();
            Core.split(dest_1, channels4);
            tipo1_1 = channels4.get(2).clone();
            Core.split(aux_f_gfp_1_c, channels5);
            tipo1_2 = channels5.get(0).clone();
            Core.split(mult1, channels6);
            tipo1_3 = channels6.get(0).clone();
            //Imgproc.cvtColor(dest_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(aux_f_gfp_1_c,tipo1_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(mult1,tipo1_3, Imgproc.COLOR_RGB2GRAY);
            List<Mat> lista1 = new ArrayList<Mat>();
            lista1.add(tipo1_3);
            lista1.add(tipo1_2);
            lista1.add(tipo1_1);
            Core.merge(lista1, destino_1);
            String title_1 = "PerimeterGFP>" + threshold + "at24-48-72hpi.jpg";*/
            //Highgui.imwrite(title_1, destino_1);
        }
        resultados.setNGFP_0(nGFP_0);
        resultados.setNGFP_1(nGFP_1);
        resultados.setMeanGFP_0(meanGFP_0);
        resultados.setMeanGFP_1(meanGFP_1);


        comparation_0 = new double[ndata - 1];
        comparation_1 = new double[ndata - 1];
        for (int k = 0; k < ndata - 1; k++) {
            comparation_0[k] = (double) nGFP_0[k + 1] / nGFP_0[k];
            comparation_1[k] = (double) nGFP_1[k + 1] / nGFP_1[k];
        }
        difthresh = 1;

        found = new double[2];
        found[0] = 0;
        found[1] = 0;
        while (!(found[0] != 0 && found[1] != 0)) {
            found[0] = 0;
            found[1] = 0;
            difthresh = difthresh - 0.005;
            int counter_0 = 0;
            int counter_1 = 0;
            for (int k = 0; k < ndata - 1; k++) {
                if (comparation_0[k] > difthresh) {
                    counter_0 = counter_0 + 1;
                    if (counter_0 == 3) {
                        thresholds[0] = (k - 1) * resultados.getPaso();
                        found[0] = 1;
                    }
                } else {
                    counter_0 = 0;
                }
                if (comparation_1[k] > difthresh) {
                    counter_1 = counter_1 + 1;
                    if (counter_1 == 3) {
                        thresholds[1] = (k - 1) * resultados.getPaso();
                        found[1] = 1;
                    }
                } else {
                    counter_1 = 0;
                }
            }
        }

        System.out.println("% of change between thresholds: " + difthresh * 100 + "%\n");
        maximo=Math.max(thresholds[0],thresholds[1]);
        System.out.println("Final common threshold = " + maximo + " (maximum of [" + thresholds[0] + "," + thresholds[1] + "])\n");
        finalthreshold = maximo;
        resultados.setUmbral((int) finalthreshold);
        resultados.calcularArrayUmbralElegido();
        resultados.setPosicionUmbral(resultados.getUmbral() / resultados.getPaso());

        pos = (int) finalthreshold / paso;
        PI = (nGFP_1[pos] * meanGFP_1[pos]) / (nGFP_0[pos] * meanGFP_0[pos]);
        resultados.setFactorProliferacion(PI);
        System.out.println("Factor de proliferación: " + PI);

        int threshold = 0;
        maskG_0h_t0 = new Mat();
        Imgproc.cvtColor(f_gfp_0_c, maskG_0h_t0, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(maskG_0h_t0, maskG_0h_t0, threshold, 1, Imgproc.THRESH_BINARY);

        maskG_1h_t0 = new Mat();
        Imgproc.cvtColor(f_gfp_1_c, maskG_1h_t0, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(maskG_1h_t0, maskG_1h_t0, threshold, 1, Imgproc.THRESH_BINARY);

        aux_0 = maskG_0h_t0.clone();
        perimR_0h = new ArrayList<MatOfPoint>();
        dest_0 = Mat.zeros(aux_0.size(), CvType.CV_8UC3);
        red = new Scalar(0, 0, 255);
        Imgproc.findContours(aux_0, perimR_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        aux_0.release();
        Imgproc.drawContours(dest_0, perimR_0h, -1, red);

        //threshold = finalthreshold;
        for (threshold = 0; threshold <= fin; threshold += paso) {
            maskG_0h_tf = new Mat();
            Imgproc.cvtColor(f_gfp_0_c, maskG_0h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_0h_tf, maskG_0h_tf, threshold, 1, Imgproc.THRESH_BINARY);

            maskG_1h_tf = new Mat();
            Imgproc.cvtColor(f_gfp_1_c, maskG_1h_tf, Imgproc.COLOR_RGB2GRAY);
            Imgproc.threshold(maskG_1h_tf, maskG_1h_tf, threshold, 1, Imgproc.THRESH_BINARY);

            aux_1 = maskG_1h_t0.clone();
            perimR_1h = new ArrayList<MatOfPoint>();
            dest_1 = Mat.zeros(aux_1.size(), CvType.CV_8UC3);
            Imgproc.findContours(aux_1, perimR_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            aux_1.release();
            Imgproc.drawContours(dest_1, perimR_1h, -1, red);

            resta0 = new Mat();
            Core.subtract(maskG_0h_t0, maskG_0h_tf, resta0);
            perimB_0h = new ArrayList<MatOfPoint>();
            dest_2 = Mat.zeros(resta0.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta0, perimB_0h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta0.release();
            Imgproc.drawContours(dest_2, perimB_0h, -1, red);

            resta1 = new Mat();
            Core.subtract(maskG_1h_t0, maskG_1h_tf, resta1);
            perimB_1h = new ArrayList<MatOfPoint>();
            dest_3 = Mat.zeros(resta1.size(), CvType.CV_8UC3);
            Imgproc.findContours(resta1, perimB_1h, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
            resta1.release();
            Imgproc.drawContours(dest_3, perimB_1h, -1, red);

            destino_1 = new Mat();
            channels7 = new ArrayList<Mat>();
            channels8 = new ArrayList<Mat>();
            channels9 = new ArrayList<Mat>();
            Core.split(dest_0, channels7);
            tipo0_1 = channels7.get(2).clone();
            Core.split(f_gfp_0_c, channels8);
            tipo0_2 = channels8.get(0).clone();
            Core.split(dest_2, channels9);
            tipo0_3 = channels9.get(2).clone();
            //Imgproc.cvtColor(suma0_1,tipo0_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_2,tipo0_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma0_3,tipo0_3, Imgproc.COLOR_RGB2GRAY);
            lista1 = new ArrayList<Mat>();
            lista1.add(tipo0_3);
            lista1.add(tipo0_2);
            lista1.add(tipo0_1);
            Core.merge(lista1, destino_1);
            title_1 = rutaCarpetaImagenes + File.separator + "Procesado2D" + File.separator + "Procesado0hpi-threshold=" + threshold + ".jpg";
            Highgui.imwrite(title_1, destino_1);

            destino_2 = new Mat();
            channels10 = new ArrayList<Mat>();
            channels11 = new ArrayList<Mat>();
            channels12 = new ArrayList<Mat>();
            Core.split(dest_1, channels10);
            tipo1_1 = channels10.get(2).clone();
            Core.split(f_gfp_1_c, channels11);
            tipo1_2 = channels11.get(0).clone();
            Core.split(dest_3, channels12);
            tipo1_3 = channels12.get(2).clone();
            //Imgproc.cvtColor(suma1_1,tipo1_1, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_2,tipo1_2, Imgproc.COLOR_RGB2GRAY);
            //Imgproc.cvtColor(suma1_3,tipo1_3, Imgproc.COLOR_RGB2GRAY);
            lista2 = new ArrayList<Mat>();
            lista2.add(tipo1_3);
            lista2.add(tipo1_2);
            lista2.add(tipo1_1);
            Core.merge(lista2, destino_2);
            title_2 = rutaCarpetaImagenes + File.separator + "Procesado2D" + File.separator + "Procesado24-48-72hpi-threshold=" + threshold + ".jpg";
            Highgui.imwrite(title_2, destino_2);
        }
    }

    public static void reProcesadoManual(int umbralManual, Analisis resultados) {
        resultados.setUmbral(umbralManual);
        resultados.calcularFactorProliferacion();
        resultados.calcularArrayUmbralElegido();
        resultados.setPosicionUmbral(resultados.getUmbral() / resultados.getPaso());
    }

}

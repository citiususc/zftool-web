import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Analise {
    private Integer umbral;
    private Double factorProliferacion;
    private  int[] range;
    private  int[] nGFP_0;
    private int[] nGFP_1;
    private double[] meanGFP_0;
    private double[] meanGFP_1;

    public Analise() { }

    public Integer getUmbral() {
        return umbral;
    }
    public Double getFactorProliferacion() {
        return factorProliferacion;
    }
    public int[]  getRange() {
        return range;
    }
    public int[]  getNGFP_0() {
        return nGFP_0;
    }
    public int[]  getNGFP_1() {
        return nGFP_1;
    }
    public double[]  getMeanGFP_0() {
        return meanGFP_0;
    }
    public double[]  getMeanGFP_1() { return meanGFP_1; }


    public void setUmbral(Integer umbral) {
        this.umbral = umbral;
    }
    public void setFactorProliferacion(Double factorProliferacion) {
        this.factorProliferacion = factorProliferacion;
    }
    public void setRange(int[] range) {
        this.range = range;
    }
    public void setNGFP_0(int[] nGFP_0) {
        this.nGFP_0 = nGFP_0;
    }
    public void setNGFP_1(int[] nGFP_1) {
        this.nGFP_1 = nGFP_1;
    }
    public void setMeanGFP_0(double[] meanGFP_0) { this.meanGFP_0 = meanGFP_0; }
    public void setMeanGFP_1(double[] meanGFP_1) {
        this.meanGFP_1 = meanGFP_1;
    }


    public void imprimirFicheroCSV(String fichero, int[] elegido) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fichero));
        String[] header= new String[]{"Thresholds","nGFP_0","nGFP_1","meanGFP_0","meanGFP_1","Chosen"};
        writer.writeNext(header);
        List<String[]> allData = new ArrayList<String[]>();
        for(int i=0;i<this.nGFP_0.length;i++) {
            String[] data = new String[]{Integer.toString(this.range[i]),Integer.toString(this.nGFP_0[i]),Integer.toString(this.nGFP_1[i]),Double.toString(this.meanGFP_0[i]),Double.toString(this.meanGFP_1[i]),Integer.toString(elegido[i])};
            allData.add(data);
        }
        writer.writeAll(allData);
        writer.close();
    }

}

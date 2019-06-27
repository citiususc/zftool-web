package es.usc.citius.zebrafish.modelo.entidades;


public class Analisis {
    private Integer umbral;
    private Double factorProliferacion;
    private int[] nGFP_0;
    private int[] nGFP_1;
    private double[] meanGFP_0;
    private double[] meanGFP_1;
    private int[] umbralElegido;
    private int[] range;
    private Integer fin;
    private Integer paso;
    private Integer posicionUmbral;

    public Analisis() { }

    public Analisis(Analisis nuevosResultados) {
        this.umbral = nuevosResultados.getUmbral();
        this.factorProliferacion = nuevosResultados.getFactorProliferacion();
        this.nGFP_0 = nuevosResultados.getNGFP_0();
        this.nGFP_1 = nuevosResultados.getNGFP_1();
        this.meanGFP_0 = nuevosResultados.getMeanGFP_0();
        this.meanGFP_1 = nuevosResultados.getMeanGFP_1();
        this.umbralElegido = nuevosResultados.getUmbralElegido();
        this.range = nuevosResultados.getRange();
        this.fin = nuevosResultados.getFin();
        this.paso = nuevosResultados.getPaso();
        this.posicionUmbral = nuevosResultados.getPosicionUmbral();
    }

    public Integer getUmbral() { return umbral; }

    public Double getFactorProliferacion() {
        return factorProliferacion;
    }

    public int[] getNGFP_0() {
        return nGFP_0;
    }

    public int[] getNGFP_1() { return nGFP_1; }

    public double[] getMeanGFP_0() {
        return meanGFP_0;
    }

    public double[] getMeanGFP_1() {
        return meanGFP_1;
    }

    public int[] getUmbralElegido() {
        return umbralElegido;
    }

    public int[] getRange() {
        return range;
    }

    public Integer getFin() {
        return fin;
    }

    public Integer getPaso() {
        return paso;
    }

    public Integer getPosicionUmbral() { return posicionUmbral; }


    public void setUmbral(Integer umbral) { this.umbral = umbral; }

    public void setFactorProliferacion(Double factorProliferacion) {
        this.factorProliferacion = factorProliferacion;
    }

    public void setNGFP_0(int[] nGFP_0) {
        this.nGFP_0 = nGFP_0;
    }

    public void setNGFP_1(int[] nGFP_1) {
        this.nGFP_1 = nGFP_1;
    }

    public void setMeanGFP_0(double[] meanGFP_0) {
        this.meanGFP_0 = meanGFP_0;
    }

    public void setMeanGFP_1(double[] meanGFP_1) {
        this.meanGFP_1 = meanGFP_1;
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public void setUmbralElegido(int[] umbralElegido) {
        this.umbralElegido = umbralElegido;
    }

    public void setFin(Integer fin) { this.fin = fin; }

    public void setPaso(Integer paso) { this.paso = paso; }

    public void setPosicionUmbral(Integer posicionUmbral) { this.posicionUmbral = posicionUmbral; }

    public void calcularRange() {
        int longitud = (this.fin / this.paso) + 1;
        int[] rango = new int[longitud];
        int count = 0;
        for (int i = 0; i <= this.fin; i = i + this.paso) {
            rango[count] = i;
            count++;
        }
        this.range = rango;
    }

    public void calcularFactorProliferacion() {
        int pos = (int) this.umbral / this.paso;
        double FP = (this.nGFP_1[pos] * this.meanGFP_1[pos]) / (this.nGFP_0[pos] * this.meanGFP_0[pos]);
        this.factorProliferacion = FP;
    }

    public void calcularArrayUmbralElegido() {
        int pos = (int) this.umbral / this.paso;
        int[] escogido = new int[this.fin / this.paso + 1];
        int count = 0;
        for (int i = 0; i <= this.fin; i = i + this.paso) {
            escogido[count] = 0;
            count++;
        }
        escogido[pos] = 1;
        this.umbralElegido = escogido;
    }

}

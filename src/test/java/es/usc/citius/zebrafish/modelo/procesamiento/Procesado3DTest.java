package es.usc.citius.zebrafish.modelo.procesamiento;

import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.opencv.core.Core;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;

//@RunWith(MockitoJUnitRunner.class)
public class Procesado3DTest {
    /*static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Analisis analisis;
    private Analisis analisisComparacion;

    @Before
    public void inicializar(){
        this.analisis=new Analisis();
        this.analisisComparacion=new Analisis();
    }

    @Test
    public void ReProcesadoManual(){
        //analisis y analisisComparacion se establecen con los mismos valores
        int [] nGFP_0 = {28259,15666,8166,4367,2869,1975,1711,1602,1519,1453,1396};
        int [] nGFP_1 = {13870,7102,4295,2648,2136,1959,1834,1729,1639,1536,1430};
        double [] meanGFP_0 = {13.41,20.05,31.36,47.63,63.19,81.51,89.86,93.74,96.79,99.24,101.35};
        double [] meanGFP_1 = {18.04,28.42,41.88,60.08,70.29,74.57,77.74,80.45,82.77,85.44,88.23};
        int [] umbralElegido = {0,0,0,0,0,0,0,1,0,0,0};
        int [] range = {0,5,10,15,20,25,30,35,40,45,50};
        int umbral=35,fin=50,paso=5,posicionUmbral=7;
        double FP=0.9262614312651113;
        this.analisis.setUmbral(umbral);
        this.analisis.setFactorProliferacion(FP);
        this.analisis.setNGFP_0(nGFP_0);
        this.analisis.setNGFP_1(nGFP_1);
        this.analisis.setMeanGFP_0(meanGFP_0);
        this.analisis.setMeanGFP_1(meanGFP_1);
        this.analisis.setUmbralElegido(umbralElegido);
        this.analisis.setRange(range);
        this.analisis.setFin(fin);
        this.analisis.setPaso(paso);
        this.analisis.setPosicionUmbral(posicionUmbral);
        this.analisisComparacion=new Analisis(this.analisis);

        //Se modifica el valor de analisis manualmente
        int umbralManual=5;
        analisis.setUmbral(umbralManual);
        analisis.setPosicionUmbral(analisis.getPosicionUmbral()/analisis.getPaso());
        int [] umbralElegidoManualmente = {0,1,0,0,0,0,0,0,0,0,0};
        analisis.setUmbralElegido(umbralElegidoManualmente);
        analisis.setFactorProliferacion(0.899889);
        //Se modifica analisisComparacion empleando Procesado2D
        Procesado3D.reProcesadoManual(umbralManual,this.analisisComparacion);


        Assert.assertEquals(this.analisisComparacion.getUmbral(), this.analisis.getUmbral());
        Assert.assertEquals(this.analisisComparacion.getFactorProliferacion(), this.analisis.getFactorProliferacion());
        Assert.assertArrayEquals(this.analisisComparacion.getNGFP_0(), this.analisis.getNGFP_0());
        Assert.assertArrayEquals(this.analisisComparacion.getNGFP_1(), this.analisis.getNGFP_1());
        assertTrue(Arrays.equals(this.analisisComparacion.getMeanGFP_0(), this.analisis.getMeanGFP_0() ));
        assertTrue(Arrays.equals(this.analisisComparacion.getMeanGFP_1(), this.analisis.getMeanGFP_1() ));
        Assert.assertArrayEquals(this.analisisComparacion.getUmbralElegido(), this.analisis.getUmbralElegido());
        Assert.assertArrayEquals(this.analisisComparacion.getRange(), this.analisis.getRange());
        Assert.assertEquals(this.analisisComparacion.getFin(), this.analisis.getFin());
        Assert.assertEquals(this.analisisComparacion.getPaso(), this.analisis.getPaso());
        Assert.assertEquals(this.analisisComparacion.getPosicionUmbral(), this.analisis.getPosicionUmbral());
    }
    @After
    public void finalizar(){
        this.analisis=null;
        this.analisisComparacion=null;
    }*/
}

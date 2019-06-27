package es.usc.citius.zebrafish.modelo.procesamiento;

import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import es.usc.citius.zebrafish.modelo.usuarios.UserHelper;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;

//@RunWith(MockitoJUnitRunner.class)
public class Procesado2DTest {

  /*  static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Autowired
    private Properties properties;
    private Path imagePathGris0h = null;
    private Path imagePathGris48h = null;
    private Path imagePathGFP0h = null;
    private Path imagePathGFP48h = null;
    private Analisis analisis;
    private Analisis analisisComparacion;
    private Procesado2D pZF;
    @Before
    public void inicializar() throws URISyntaxException {
        this.imagePathGris0h = Paths.get(Procesado2D.class.getClassLoader().getResource("img/2 - 0h bn.tif").toURI());
        this.imagePathGris48h = Paths.get(Procesado2D.class.getClassLoader().getResource("img/2 - 48h bn.tif").toURI());
        this.imagePathGFP0h = Paths.get(Procesado2D.class.getClassLoader().getResource("img/2 - 0h gfp.tif").toURI());
        this.imagePathGFP48h = Paths.get(Procesado2D.class.getClassLoader().getResource("img/2 - 48h gfp.tif").toURI());
        this.analisis=new Analisis();
        this.analisisComparacion=new Analisis();
        this.pZF=new Procesado2D();
    }

    @Test
    public void testCalculoLimiarOptimoFluorescencia() throws IOException {
        int umbral=35,fin=50,paso=5;
        byte[] f_bw_0 = FileUtils.readFileToByteArray(new File(imagePathGris0h.getFileName().toString()));
        byte[] f_bw_1 = FileUtils.readFileToByteArray(new File(imagePathGris48h.getFileName().toString()));
        byte[] f_gfp_0 = FileUtils.readFileToByteArray(new File(imagePathGFP0h.getFileName().toString()));
        byte[] f_gfp_1 = FileUtils.readFileToByteArray(new File(imagePathGFP48h.getFileName().toString()));
        String identificadorUsuario = UserHelper.uuid();
        String carpeta = UserHelper.createFolderUser(properties.getProperty("appDirectory"), identificadorUsuario);

        pZF.realizarProcesado2D(f_bw_0,f_gfp_0,f_bw_1,f_gfp_1,fin,paso,carpeta,analisis);
        analisisComparacion.setUmbral(umbral);
        Assert.assertEquals(this.analisisComparacion.getUmbral(), this.analisis.getUmbral());
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
        Procesado2D.reProcesadoManual(umbralManual,this.analisisComparacion);


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

    @Test
    public void testCalculoContornaMasaTumoral() throws IOException {

        int umbral=30,fin=50,paso=5;
        byte[] f_bw_0 = FileUtils.readFileToByteArray(new File(imagePathGris0h.getFileName().toString()));
        byte[] f_bw_1 = FileUtils.readFileToByteArray(new File(imagePathGris48h.getFileName().toString()));
        byte[] f_gfp_0 = FileUtils.readFileToByteArray(new File(imagePathGFP0h.getFileName().toString()));
        byte[] f_gfp_1 = FileUtils.readFileToByteArray(new File(imagePathGFP48h.getFileName().toString()));
        String identificadorUsuario = UserHelper.uuid();
        String carpeta = UserHelper.createFolderUser(properties.getProperty("appDirectory"), identificadorUsuario);

        pZF.realizarProcesado2D(f_bw_0,f_gfp_0,f_bw_1,f_gfp_1,fin,paso,carpeta,analisis);
        String  rutaImg= carpeta + File.separator + "Procesado2D" + File.separator + "Procesado24-48-72hpi-threshold=" + umbral + ".jpg";
        File imgLimiar30_0h = new File(rutaImg);
        boolean existe=imgLimiar30_0h.exists();
        Assert.assertEquals(existe,true);
    }


    @After
    public void finalizar(){
        this.pZF=null;
        this.analisis=null;
        this.analisisComparacion=null;
        this.imagePathGris0h=null;
        this.imagePathGris48h=null;
        this.imagePathGFP0h=null;
        this.imagePathGFP48h=null;
    }*/
}

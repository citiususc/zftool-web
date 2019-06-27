package es.usc.citius.zebrafish.modelo.validadores;

import es.usc.citius.zebrafish.modelo.procesamiento.Procesado2D;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Procesado2DValidadorTest {

    @Test
    public void testValidarProcesado(){

        String nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf;
        int fin, paso;
        //Correcto
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif"; fin=50; paso=5;
        Assert.assertNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        //Incorrectos fin y/o paso
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif"; fin=5; paso=50;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif"; fin=0; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif"; fin=50; paso=0;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=-100; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=-10;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        //Incorrectos nombres de las imágenes
        nombreImg0hg="2 - 48h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 48h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 0h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 100h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2-0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0hgfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2- 48hbn.tif"; nombreImg48hf="2 - 48h gfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
        nombreImg0hg="2 - 0h bn.tif"; nombreImg0hf="2 - 0h gfp.tif"; nombreImg48hg="2 - 48h bn.tif"; nombreImg48hf="2-48hgfp.tif";fin=50; paso=5;
        Assert.assertNotNull(Procesado2DValidador.validarProcesado(nombreImg0hg, nombreImg0hf, nombreImg48hg, nombreImg48hf,fin,paso));
    }
    @Test
    public void testValidarReprocesado(){
        //Correctos
        int fin=50, paso=5,umbral=50;
        Assert.assertNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=0;
        Assert.assertNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=15;
        Assert.assertNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));
        //Incorrectos
        fin=50; paso=5;umbral=100;
        Assert.assertNotNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=-10;
        Assert.assertNotNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=11;
        Assert.assertNotNull(Procesado2DValidador.validarReprocesado(fin,paso,umbral));

    }

}

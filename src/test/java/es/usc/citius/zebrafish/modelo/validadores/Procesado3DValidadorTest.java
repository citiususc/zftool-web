package es.usc.citius.zebrafish.modelo.validadores;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Procesado3DValidadorTest {
    @Test
    public void testValidarReprocesado(){
        //Correctos
        int fin=50, paso=5,umbral=50;
        Assert.assertNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=0;
        Assert.assertNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=15;
        Assert.assertNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
        //Incorrectos
        fin=50; paso=5;umbral=100;
        Assert.assertNotNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=-10;
        Assert.assertNotNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
        fin=50; paso=5;umbral=11;
        Assert.assertNotNull(Procesado3DValidador.validarReprocesado(fin,paso,umbral));
    }
}

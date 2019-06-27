package es.usc.citius.zebrafish.modelo.informes;

import au.com.bytecode.opencsv.CSVWriter;
import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import junitx.framework.FileAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExportFileTest {

    private Analisis resultados;

    @Before
    public void inicializar() {
        this.resultados = new Analisis();
    }

    @Test
    public void testGenerarFicheroCSV() throws IOException {

        int [] nGFP_0 = {28259,15666,8166,4367,2869,1975,1711,1602,1519,1453,1396};
        int [] nGFP_1 = {13870,7102,4295,2648,2136,1959,1834,1729,1639,1536,1430};
        double [] meanGFP_0 = {13.41,20.05,31.36,47.63,63.19,81.51,89.86,93.74,96.79,99.24,101.35};
        double [] meanGFP_1 = {18.04,28.42,41.88,60.08,70.29,74.57,77.74,80.45,82.77,85.44,88.23};
        int [] umbralElegido = {0,0,0,0,0,0,0,1,0,0,0};
        int [] range = {0,5,10,15,20,25,30,35,40,45,50};
        File fp = File.createTempFile("ResultadosProcesado2D", ".csv");
        CSVWriter writer = new CSVWriter(new PrintWriter(fp));
        String[] header = new String[]{"Thresholds", "nGFP_0", "nGFP_1", "meanGFP_0", "meanGFP_1", "Chosen"};
        writer.writeNext(header);
        List<String[]> allData = new ArrayList<String[]>();
        for (int i = 0; i < nGFP_0.length; i++) {
            String[] data = new String[]{Integer.toString(range[i]), Integer.toString(nGFP_0[i]), Integer.toString(nGFP_1[i]), Double.toString(meanGFP_0[i]), Double.toString(meanGFP_1[i]), Integer.toString(umbralElegido[i])};
            allData.add(data);
        }
        writer.writeAll(allData);
        writer.close();

        resultados.setNGFP_0(nGFP_0);
        resultados.setNGFP_1(nGFP_1);
        resultados.setMeanGFP_0(meanGFP_0);
        resultados.setMeanGFP_1(meanGFP_1);
        resultados.setRange(range);
        resultados.setUmbralElegido(umbralElegido);
        File file=ExportFile.generarFicheroCSV("ResultadosProcesado2D",resultados);
        FileAssert.assertEquals(fp, file);
        file.delete();
        fp.delete();
    }

    @After
    public void finalizar() {
        this.resultados = null;
    }
}

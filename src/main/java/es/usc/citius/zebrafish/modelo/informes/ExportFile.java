package es.usc.citius.zebrafish.modelo.informes;

import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import au.com.bytecode.opencsv.CSVWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ExportFile {

    public static File generarFicheroCSV(String fichero, Analisis resultados) throws IOException {
        File fp = File.createTempFile(fichero, ".csv");
        CSVWriter writer = new CSVWriter(new PrintWriter(fp));
        String[] header = new String[]{"Thresholds", "nGFP_0", "nGFP_1", "meanGFP_0", "meanGFP_1", "Chosen"};
        writer.writeNext(header);
        List<String[]> allData = new ArrayList<String[]>();
        for (int i = 0; i < resultados.getNGFP_0().length; i++) {
            String[] data = new String[]{Integer.toString(resultados.getRange()[i]), Integer.toString(resultados.getNGFP_0()[i]), Integer.toString(resultados.getNGFP_1()[i]), Double.toString(resultados.getMeanGFP_0()[i]), Double.toString(resultados.getMeanGFP_1()[i]), Integer.toString(resultados.getUmbralElegido()[i])};
            allData.add(data);
        }
        writer.writeAll(allData);
        writer.close();

        return fp;
    }

    public static HttpEntity<byte[]> generateSpreadsheetBytes(File file) throws IOException {
        byte[] document = FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "x-download"));
        header.set("Content-Disposition", "attachment; filename=" + file.getName());
        header.setContentLength(document.length);
        return new HttpEntity<byte[]>(document, header);
    }
}

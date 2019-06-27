package es.usc.citius.zebrafish.controladores;

import es.usc.citius.zebrafish.modelo.entidades.Analisis;
import es.usc.citius.zebrafish.modelo.informes.ExportFile;
import es.usc.citius.zebrafish.modelo.procesamiento.Procesado2D;
import es.usc.citius.zebrafish.modelo.procesamiento.Procesado3D;
import es.usc.citius.zebrafish.modelo.usuarios.UserHelper;
import es.usc.citius.zebrafish.modelo.validadores.Procesado2DValidador;
import es.usc.citius.zebrafish.modelo.validadores.Procesado3DValidador;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Properties;

@Controller
public class UserController {
    private static final String APPLICATION_EXCEL = "application/x-download";

    @Autowired
    private Properties properties;
    private Analisis resultados;
    private Analisis nuevosResultados;
    private Analisis resultados3D;
    private Analisis nuevosResultados3D;
    private Procesado2D pZF_2D;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView toIndex(HttpSession session) {
        String identificadorUsuario = UserHelper.uuid();
        session.setAttribute("idUsuario", identificadorUsuario);
        String carpeta = UserHelper.createFolderUser(properties.getProperty("appDirectory"), identificadorUsuario);
        session.setAttribute("carpetaUsuario", carpeta);
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/carga2D", method = RequestMethod.GET)
    public ModelAndView toCarga2D(Model model) {
        return new ModelAndView("carga2D");
    }

    @RequestMapping(value = "/procesar2D", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public ModelAndView toProcess2D(
            @RequestPart(value = "imagen0hg") MultipartFile imagen0hg,
            @RequestPart(value = "imagen72hg") MultipartFile imagen72hg,
            @RequestPart(value = "imagen0hf") MultipartFile imagen0hf,
            @RequestPart(value = "imagen72hf") MultipartFile imagen72hf,
            @RequestParam("Inicio") Integer inicio,
            @RequestParam("Fin") Integer fin,
            @RequestParam("Paso") Integer paso,
            HttpSession session
    ) throws IOException {
        ModelAndView miMAV = new ModelAndView();
        miMAV.addObject("tipoProcesado2D", "1");
        String validacion = Procesado2DValidador.validarProcesado(imagen0hg.getOriginalFilename(), imagen0hf.getOriginalFilename(), imagen72hg.getOriginalFilename(), imagen72hf.getOriginalFilename(), fin, paso);
        if (validacion != null) {
            miMAV.addObject("error", validacion);
            miMAV.setViewName("carga2D");
            return miMAV;
        }
        byte[] bytes0hf = imagen0hf.getBytes();
        byte[] bytes72hf = imagen72hf.getBytes();
        String rutaCarpetaUsuario = (String) session.getAttribute("carpetaUsuario");
        if (!imagen0hg.isEmpty() && !imagen72hg.isEmpty()) {
            byte[] bytes0hg = imagen0hg.getBytes();
            byte[] bytes72hg = imagen72hg.getBytes();
            resultados = new Analisis();
            pZF_2D = new Procesado2D();
            pZF_2D.realizarProcesado2D(bytes0hg, bytes0hf, bytes72hg, bytes72hf, fin, paso, rutaCarpetaUsuario, resultados);
        } else {
            resultados = new Analisis();
            pZF_2D = new Procesado2D();
            pZF_2D.realizarProcesado2D(bytes0hf, bytes72hf, fin, paso, rutaCarpetaUsuario, resultados);
        }
        session.setAttribute("resultados", resultados);
        session.setAttribute("resultadosAutomatico_copia", resultados);
        miMAV.setViewName("procesado2D");
        return miMAV;
    }

    @RequestMapping(value = "/reprocesar2D", method = RequestMethod.POST)
    public ModelAndView toReProcess2D(@RequestParam("selUmbral") Integer tipoProcesado,
                                      @RequestParam("umbralManual") Integer umbralManual,
                                      HttpSession session) {
        ModelAndView miMAV = new ModelAndView();
        miMAV.addObject("tipoProcesado2D", tipoProcesado);
        resultados = (Analisis) session.getAttribute("resultadosAutomatico_copia");
        if (tipoProcesado == 2) { //Manual
            String validacion = Procesado2DValidador.validarReprocesado(resultados.getFin(), resultados.getPaso(), umbralManual);
            if (validacion != null) {
                miMAV.addObject("error", validacion);
                miMAV.setViewName("procesado2D");
                return miMAV;
            }
            nuevosResultados = new Analisis(resultados);
            Procesado2D.reProcesadoManual(umbralManual, nuevosResultados);
            session.setAttribute("resultados", nuevosResultados);
        } else { //Automático
            session.setAttribute("resultados", resultados);
        }
        miMAV.setViewName("procesado2D");
        return miMAV;
    }

    @RequestMapping(value = "/{idUsuario}/0h/{valorUmbral}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getPhoto0h(@PathVariable String idUsuario, @PathVariable String valorUmbral) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado2D" + File.separator + "Procesado0hpi-threshold=" + valorUmbral + ".jpg";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/{idUsuario}/24-48-72h/{valorUmbral}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getPhoto24_48_72h(@PathVariable String idUsuario, @PathVariable String valorUmbral) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado2D" + File.separator + "Procesado24-48-72hpi-threshold=" + valorUmbral + ".jpg";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/exportarGraficos2D", method = RequestMethod.GET, produces = APPLICATION_EXCEL)
    public @ResponseBody
    HttpEntity<byte[]> toExportGraficos2D(HttpSession session) throws IOException {
        Analisis resultados = (Analisis) session.getAttribute("resultados");
        File file = ExportFile.generarFicheroCSV("ResultadosProcesado2D", resultados);
        HttpEntity<byte[]> downloadedFile = ExportFile.generateSpreadsheetBytes(file);
        return downloadedFile;
    }

    @RequestMapping(value = "/carga3D", method = RequestMethod.GET)
    public ModelAndView toCarga3D(Model model) {
        return new ModelAndView("carga3D");
    }

    @RequestMapping(value = "/procesar3D", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public ModelAndView toProcess3D(
            @RequestPart(value = "imagenes0h3D") MultipartFile[] imagenes3D_0h,
            @RequestPart(value = "imagenes48h3D") MultipartFile[] imagenes3D_48h,
            @RequestParam("Inicio_3D") Integer inicio,
            @RequestParam("Fin_3D") Integer fin,
            @RequestParam("Paso_3D") Integer paso,
            HttpSession session
    ) throws IOException {
        ModelAndView miMAV = new ModelAndView();
        miMAV.addObject("tipoProcesado3D", "1");
        String validacion = Procesado3DValidador.validarProcesado(imagenes3D_0h, imagenes3D_48h, fin, paso);
        if (validacion != null) {
            miMAV.addObject("error", validacion);
            miMAV.setViewName("carga3D");
            return miMAV;
        }
        String folderUser = (String) session.getAttribute("carpetaUsuario");
        List<String> listaImg0h = Procesado3D.guardarImagenes0hProcesado3D(imagenes3D_0h, folderUser);
        session.setAttribute("imagenesProcesado3D_0h", listaImg0h);
        List<String> listaImg24_48_72h = Procesado3D.guardarImagenes24_48_72hProcesado3D(imagenes3D_48h, folderUser);
        session.setAttribute("imagenesProcesado3D_48h", listaImg24_48_72h);
        resultados3D = new Analisis();
        Procesado3D.realizarProcesado3D(fin, paso, properties.getProperty("appDirectory"), folderUser, listaImg0h, listaImg24_48_72h, resultados3D);
        session.setAttribute("resultados3D", resultados3D);
        session.setAttribute("resultados3DAutomatico_copia", resultados3D);

        miMAV.setViewName("procesado3D");
        return miMAV;
    }

    @RequestMapping(value = "/reprocesar3D", method = RequestMethod.POST)
    public ModelAndView toReProcess3D(@RequestParam("selTipoProcesado") Integer tipoProcesado3D,
                                      @RequestParam("valorUmbral") Integer umbralManual,
                                      HttpSession session) {
        resultados3D = (Analisis) session.getAttribute("resultados3DAutomatico_copia");
        String folderUser = (String) session.getAttribute("carpetaUsuario");
        List<String> listaImg0h = (List<String>) session.getAttribute("imagenesProcesado3D_0h");
        List<String> listaImg24_48_72h = (List<String>) session.getAttribute("imagenesProcesado3D_48h");

        ModelAndView miMAV = new ModelAndView();
        miMAV.addObject("tipoProcesado3D", tipoProcesado3D);
        if (tipoProcesado3D == 2) { //Manual
            String validacion = Procesado3DValidador.validarReprocesado(resultados3D.getFin(), resultados3D.getPaso(), umbralManual);
            if (validacion != null) {
                miMAV.addObject("error", validacion);
                miMAV.setViewName("procesado3D");
                return miMAV;
            }
            nuevosResultados3D = new Analisis(resultados3D);
            Procesado3D.reProcesadoManual(umbralManual, nuevosResultados3D);
            Procesado3D.reprocesadoRepresentacion3D(umbralManual, properties.getProperty("appDirectory"), folderUser, listaImg0h, listaImg24_48_72h);
            session.setAttribute("resultados3D", nuevosResultados3D);
        } else { //Automático
            Procesado3D.reprocesadoRepresentacion3D(resultados3D.getUmbral(), properties.getProperty("appDirectory"), folderUser, listaImg0h, listaImg24_48_72h);
            session.setAttribute("resultados3D", resultados3D);
        }
        miMAV.setViewName("procesado3D");
        return miMAV;
    }

    @RequestMapping(value = "/exportarGraficos3D", method = RequestMethod.GET, produces = APPLICATION_EXCEL)
    public @ResponseBody
    HttpEntity<byte[]> toExportGraficos3D(HttpSession session) throws IOException {
        Analisis resultados = (Analisis) session.getAttribute("resultados3D");
        File file = ExportFile.generarFicheroCSV("ResultadosProcesado3D", resultados);
        HttpEntity<byte[]> downloadedFile = ExportFile.generateSpreadsheetBytes(file);
        return downloadedFile;
    }

    @RequestMapping(value = "/{idUsuario}/obj_0h", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] getOBJ0h(@PathVariable String idUsuario) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado3D" + File.separator + "0h" + File.separator + "modelo0h.obj";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/{idUsuario}/obj_48h", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] getOBJ48h(@PathVariable String idUsuario) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado3D" + File.separator + "48h" + File.separator + "modelo48h.obj";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/{idUsuario}/mtl_0h", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] getMTL0h(@PathVariable String idUsuario) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado3D" + File.separator + "0h" + File.separator + "modelo0h.mtl";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/{idUsuario}/mtl_48h", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] getMTL48h(@PathVariable String idUsuario) throws IOException {
        String ruta = properties.getProperty("appDirectory") + File.separator + idUsuario + File.separator + "Procesado3D" + File.separator + "48h" + File.separator + "modelo48h.mtl";
        InputStream in = new FileInputStream(ruta);
        return IOUtils.toByteArray(in);
    }
}

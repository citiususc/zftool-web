<%@ page contentType="text/html;charset=UTF-8" language="Java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Procesado 2D</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
          integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/lightbox.css"/>"/>

    <script src="<c:url value="/resources/js/lightbox-plus-jquery.js"/>"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
</head>
<body>

<!--SCRIPTS-->
<!--Elegir tipo de procesado en el select y dejar habilitado el campo umbral si el procesado es manual-->
<script>
    $(document).ready(function () {
        var valor = $('input:hidden[name=modoProcesado2D]').val();
        $("#selUmbral").val(valor);
        if (valor != "1") {
            $("#umbralManual").removeAttr("readonly");
        }
    });
</script>

<!--Bloquear cuando se produce algún cambio el campo umbral si el procesado es automático y habilitar si es manual para definir el valor -->
<script>
    $(function () {
        $("#selUmbral").on('change', function () {
            if ($(this).val() === "1") {
                $('input[name=umbralManual]').val(' ');
                $("#umbralManual").attr("readonly", "readonly");
            } else {
                $("#umbralManual").removeAttr("readonly");
            }
        });
    });
</script>

<!--Cambiar las imágenes en función del umbral elegido -->
<script>
    function mostrarImagenes0h() {
        document.getElementById("text1").value = document.getElementById("range1").value;

        var urlCarpetaImg1 = "/zebrafish/" + document.getElementById("identificadorUsuario").value + "/0h/" + document.getElementById("range1").value;

        document.getElementById('imgProcesadoRg1').src = urlCarpetaImg1;
        document.getElementById('aProcesadoRg1').href = urlCarpetaImg1;
    }

    function mostrarImagenes24_48_72h() {
        document.getElementById("text2").value = document.getElementById("range2").value;

        var urlCarpetaImg2 = "/zebrafish/" + document.getElementById("identificadorUsuario").value + "/24-48-72h/" + document.getElementById("range2").value;

        document.getElementById('imgProcesadoRg2').src = urlCarpetaImg2;
        document.getElementById('aProcesadoRg2').href = urlCarpetaImg2;
    }
</script>

<div class="container">
    <!--Logo-->
    <div class="row justify-content-center mt-3">
        <div class="col-8 col-md-6 col-lg-4">
            <img src="<c:url value="/resources/img/logoNovo_citius.svg"/>" class="img-fluid"/>
        </div>
    </div>
    <!-- Menú-->
    <div class="row mt-1">
        <div class="col-12">
            <nav class="navbar navbar-expand-md navbar-light orange text-white">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
                        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarText">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="<c:url value='/carga2D'/>">Carga 2D</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Procesado 2D <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="<c:url value='/carga3D'/>">Carga 3D</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>

    <div class="row justify-content-center mt-1">
        <div class="col-11 extgrande">
            <div class="row">
                <div class="col-md-6 muygrande">
                    <div id="grafico1" style="width: 500px; height: 400px; margin: 0 auto"></div>
                    <script>
                        $(document).ready(function () {
                            var title = {
                                text: 'Evolution of #pixels with GFP threshold (B=0hpi; R=48hpi)'
                            };
                            var xAxis = {
                                gridLineWidth: 1,
                                title: {
                                    text: 'GFP thresholds'
                                },
                                categories: [<c:forEach items="${resultados.range}" var="rg0" varStatus="rg0Status">
                                    ${rg0}
                                    <c:if test="${!rg0Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>],

                                plotLines: [{
                                    label: {
                                        text: 'threshold'
                                    },
                                    color: '#FF0000',
                                    width: 2,
                                    value:${resultados.posicionUmbral}
                                }]
                            };
                            var yAxis = {
                                title: {
                                    text: '#pixels with GFP > threshold'
                                },
                                plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                            };
                            var legend = {
                                layout: 'vertical',
                                align: 'right',
                                verticalAlign: 'middle',
                                borderWidth: 0
                            };
                            var series = [{
                                name: 'nGFP_0',
                                data: [<c:forEach items="${resultados.NGFP_0}" var="n0" varStatus="n0Status">
                                    ${n0}
                                    <c:if test="${!n0Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>]
                            },
                                {
                                    name: 'nGFP_1',
                                    data: [<c:forEach items="${resultados.NGFP_1}" var="n1" varStatus="n1Status">
                                        ${n1}
                                        <c:if test="${!n1Status.last}">
                                        ,
                                        </c:if>
                                        </c:forEach>]
                                }
                            ];

                            var json = {};
                            json.title = title;
                            json.xAxis = xAxis;
                            json.yAxis = yAxis;
                            json.legend = legend;
                            json.series = series;
                            $('#grafico1').highcharts(json);
                        });
                    </script>
                </div>
                <div class="col-md-6  muygrande">
                    <div id="grafico2" style="width: 500px; height: 400px; margin: 0 auto"></div>
                    <script language="JavaScript">
                        $(document).ready(function () {
                            var title = {
                                text: 'Evolution of mean intensity with GFP threshold (B=0hpi; R=48hpi)'
                            };
                            var xAxis = {
                                gridLineWidth: 1,
                                title: {
                                    text: 'GFP thresholds'
                                },
                                categories: [<c:forEach items="${resultados.range}" var="rg1" varStatus="rg1Status">
                                    ${rg1}
                                    <c:if test="${!rg1Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>],

                                plotLines: [{
                                    label: {
                                        text: 'threshold'
                                    },
                                    color: '#FF0000',
                                    width: 2,
                                    value:${resultados.posicionUmbral}
                                }]
                            };
                            var yAxis = {
                                title: {
                                    text: 'Mean intensity of pixels with GFP > threshold'
                                },
                                plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                            };
                            var legend = {
                                layout: 'vertical',
                                align: 'right',
                                verticalAlign: 'middle',
                                borderWidth: 0
                            };
                            var series = [{
                                name: 'meanGFP_0',
                                data: [<c:forEach items="${resultados.meanGFP_0}" var="mn0" varStatus="mn0Status">
                                    ${mn0}
                                    <c:if test="${!mn0Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>]
                            },
                                {
                                    name: 'meanGFP_1',
                                    data: [<c:forEach items="${resultados.meanGFP_1}" var="mn1" varStatus="mn1Status">
                                        ${mn1}
                                        <c:if test="${!mn1Status.last}">
                                        ,
                                        </c:if>
                                        </c:forEach>]
                                }
                            ];

                            var json = {};
                            json.title = title;
                            json.xAxis = xAxis;
                            json.yAxis = yAxis;
                            json.legend = legend;
                            json.series = series;

                            $('#grafico2').highcharts(json);
                        });
                    </script>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 muygrande">
                    <figure class="figure">
                        <a href="<c:url value='/${idUsuario}/0h/${resultados.umbral}'/>" id="aProcesadoRg1"
                           data-lightbox="example-2"><img src="<c:url value='/${idUsuario}/0h/${resultados.umbral}'/>"
                                                          id="imgProcesadoRg1" class="img-fluid rounded"></a>
                        <figcaption class="figure-caption text-center">Image at 0hpi</figcaption>
                    </figure>
                    <label>Umbral</label>
                    <input type="text" id="text1" value="${resultados.umbral}" readonly>
                    <input type="range" id="range1" min="0" max="${resultados.fin}" step="${resultados.paso}"
                           value="${resultados.umbral}" onchange="mostrarImagenes0h()">
                </div>
                <div class="col-md-6 muygrande">
                    <figure class="figure">
                        <a href="<c:url value='/${idUsuario}/24-48-72h/${resultados.umbral}'/>" id="aProcesadoRg2"
                           data-lightbox="example-4"><img
                                src="<c:url value='/${idUsuario}/24-48-72h/${resultados.umbral}'/>" id="imgProcesadoRg2"
                                class="img-fluid rounded"></a>
                        <figcaption class="figure-caption text-center">Image at 24-48-72hpi</figcaption>
                    </figure>
                    <label>Umbral</label>
                    <input type="text" id="text2" value="${resultados.umbral}" readonly>
                    <input type="range" id="range2" min="0" max="${resultados.fin}" step="${resultados.paso}"
                           value="${resultados.umbral}" onchange="mostrarImagenes24_48_72h()">
                </div>
            </div>
        </div>
    </div>

    <p class="text-center text-danger"><c:out value="${error}"/></p>

    <div class="row mt-5 justify-content-center">
        <div class="col-11 mediana">
            <div class="form-group">
                <div class="row">
                    <div class="col-md-6">
                        <form action="<c:url value='/exportarGraficos2D'/>" method="GET">
                            <label>Guardar datos gráficas nGFP & meanGFP:</label> <br>
                            <input class="btn orange text-white" type="submit" name="exportar-2D" value="Exportar"/>
                        </form>
                    </div>
                    <div class="col-md-6">
                        <label>Factor de proliferación:</label>
                        <input class="form-control" type="text" name="FactProliferacion" value="<fmt:formatNumber type = "number"
         maxFractionDigits = "3" value = "${resultados.factorProliferacion}" />" readonly>
                    </div>
                </div>
                <form id="reProcesado2D" action="<c:url value='/reprocesar2D'/>" method="POST">
                    <input type="hidden" name="modoProcesado2D" id="modoProcesado2D" value="${tipoProcesado2D}">
                    <input type="hidden" name="identificadorUsuario" id="identificadorUsuario" value="${idUsuario}">
                    <div class="row">
                        <div class="col-md-6">
                            <label>Elija el tipo de procesado:</label>
                            <select class="form-control" id="selUmbral" name="selUmbral">
                                <option id="procAuto" value="1">Automático</option>
                                <option id="procManual" value="2">Manual</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label>Umbral:</label>
                            <input class="form-control" type="number" name="umbralManual" id="umbralManual" min="0"
                                   max="${resultados.fin}" step="${resultados.paso}" data-bind="value:replyNumber"
                                   value="${resultados.umbral}" readonly>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col text-right">
                            <input class="btn orange text-white" type="submit" name="reprocesado-2D" value="Procesar"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="row mt-1 mb-5 justify-content-center p-1">
        <div class="col-12 pequena orange">
        </div>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js"
        integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"
        integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ"
        crossorigin="anonymous"></script>
</body>
</html>

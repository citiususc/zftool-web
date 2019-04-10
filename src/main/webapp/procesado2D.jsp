<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page language="java" import="java.util.*" %>
<%@page language="java" import="java.io.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<html lang="es">
  <head>
    <title>Procesado 2D</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="./css/styles.css">
    <link rel="stylesheet" href="./css/lightbox.css">

    <script src="./js/lightbox-plus-jquery.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>-->
    <script src = "https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src = "https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
  </head>
  <body>

  <!--SCRIPTS-->
  <script>
      $( function() {
        $("#selUmbral").on('change', function() {
          if ($(this).val() === "1") {
              $('input[name=umbralManual]').prop("disabled", true);
              $('input[name=umbralManual]').val('');
            } else {
              $('input[name=umbralManual]').prop("disabled", false);
            }
          });
        });
    </script>
    <script>
    function mostrar1() {
      document.getElementById("text1").value = document.getElementById("range1").value;
      var cadena1="img/procesado/Procesado";
      cadena1+=document.getElementById("range1").value;
      cadena1+="at0hpi.jpg";
      document.getElementById('imgProcesadoRg1').src=cadena1;
      document.getElementById('aProcesadoRg1').href=cadena1;
    }
    function mostrar2() {
      document.getElementById("text2").value = document.getElementById("range2").value;
      var cadena2="img/procesado/Procesado";
      cadena2+=document.getElementById("range2").value;
      cadena2+="at48hpi.jpg";
      document.getElementById('imgProcesadoRg2').src=cadena2;
      document.getElementById('aProcesadoRg2').href=cadena2;
    }
    </script>
   <c:set var="rango" value="${sessionScope.resultados.getRange()}"/>
   <c:set var="nGFP0" value="${sessionScope.resultados.getNGFP_0()}"/>
   <c:set var="nGFP1" value="${sessionScope.resultados.getNGFP_1()}"/>
   <c:set var="meanGFP0" value="${sessionScope.resultados.getMeanGFP_0()}"/>
   <c:set var="meanGFP1" value="${sessionScope.resultados.getMeanGFP_1()}"/>
   <c:set var="factorProliferacion" value="${sessionScope.resultados.getFactorProliferacion()}"/>
   <c:set var="umbral" value="${sessionScope.resultados.getUmbral()}"/>
   <c:set var="fin" value="${sessionScope.fin}"/>
   <c:set var="paso" value="${sessionScope.paso}"/>
   <c:set var="pez" value="${sessionScope.pez}"/>
   <c:set var="hInicial" value="${sessionScope.hInicial}"/>
   <c:set var="hFinal" value="${sessionScope.hFinal}"/>
   <c:set var="posicionUmbral" value="${sessionScope.posicionUmbral}"/>

  <div class="container">
   <!--Logo-->
      <div class="row justify-content-center mt-3">
        <div class="col-4 col-md-3 col-lg-2">
          <img src="img/logo_citius.png" class="img-fluid" />
        </div>
      </div>
      <!-- Menú-->
      <div class="row mt-1">
        <div class="col-12">
          <nav class="navbar navbar-expand-md navbar-light orange text-white">
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarText">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item">
              <a class="nav-link text-white" href="./carga2D.jsp">Carga 2D</a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="#">Procesado 2D <span class="sr-only">(current)</span></a>
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
              <div id = "grafico1" style = "width: 500px; height: 400px; margin: 0 auto"></div>
              <script>
                  $(document).ready(function() {
                    var title = {
                       text: 'Evolution of #pixels with GFP threshold (B=0hpi; R=48hpi)'
                    };
                    var xAxis = {
                       gridLineWidth: 1,
                      title: {
                         text: 'GFP thresholds'
                      },
                       categories: [<c:forEach items="${rango}" var="rg0" varStatus="rg0Status">
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
                            value:${posicionUmbral}
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
                        data: [<c:forEach items="${nGFP0}" var="n0" varStatus="n0Status">
                                ${n0}
                                <c:if test="${!n0Status.last}">
                                ,
                                </c:if>
                              </c:forEach>]
                    },
                        {
                            name: 'nGFP_1',
                            data: [<c:forEach items="${nGFP1}" var="n1" varStatus="n1Status">
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
            <div id = "grafico2" style = "width: 500px; height: 400px; margin: 0 auto"></div>
            <script language = "JavaScript">
               $(document).ready(function() {
                  var title = {
                     text: 'Evolution of mean intensity with GFP threshold (B=0hpi; R=48hpi)'
                  };
                  var xAxis = {
                     gridLineWidth: 1,
                    title: {
                       text: 'GFP thresholds'
                    },
                     categories: [<c:forEach items="${rango}" var="rg1" varStatus="rg1Status">
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
                          value:${posicionUmbral}
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
                  var series =  [{
                        name: 'meanGFP_0',
                        data:  [<c:forEach items="${meanGFP0}" var="mn0" varStatus="mn0Status">
                                ${mn0}
                                <c:if test="${!mn0Status.last}">
                                      ,
                                  </c:if>
                                </c:forEach>]
                     },
                     {
                        name: 'meanGFP_1',
                        data: [<c:forEach items="${meanGFP1}" var="mn1" varStatus="mn1Status">
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
                <a href="img/Pez0h.jpg" data-lightbox="example-2"><img src="img/Pez0h.jpg" class="img-fluid rounded"></a>
                <figcaption class="figure-caption text-center">Image at 0hpi</figcaption>
              </figure>
            </div>
            <div class="col-md-6 muygrande">
              <figure class="figure">
                <a href="img/Pez24-48-72h.jpg" data-lightbox="example-4"><img src="img/Pez24-48-72h.jpg" class="img-fluid rounded"></a>
                <figcaption class="figure-caption text-center">Image at 24-48-72hpi</figcaption>
              </figure>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6 muygrande">
              <figure class="figure">
                <a href="img/procesado/Procesado25at48hpi.jpg" id="aProcesadoRg1" data-lightbox="example-2"><img src="img/procesado/Procesado25at0hpi.jpg" id="imgProcesadoRg1" class="img-fluid rounded"></a>
              </figure>
              <label >Umbral</label>
              <input type="text" id="text1" value="${umbral}" readonly>
              <input type="range" id="range1" min="0" max="${fin}" step="${paso}" value="${umbral}" onchange="mostrar1()">
            </div>
            <div class="col-md-6 muygrande">
              <figure class="figure">
                <a href="img/procesado/Procesado25at48hpi.jpg" id="aProcesadoRg2" data-lightbox="example-4"><img src="img/procesado/Procesado25at48hpi.jpg" id="imgProcesadoRg2" class="img-fluid rounded"></a>
              </figure>
              <label>Umbral</label>
              <input type="text" id="text2" value="${umbral}" readonly>
              <input type="range" id="range2" min="0" max="${fin}" step="${paso}" value="${umbral}" onchange="mostrar2()">
            </div>
          </div>
        </div>
      </div>

      <div class="row mt-5 justify-content-center">
        <div class="col-11 muygrande">
          <div class="form-group">
            <form action="./servlet" method="post">
              <div class="row">
                <div class="col-md-6">
                  <label>Factor de proliferación:</label>
                  <input class="form-control" type="text" name="FactProliferacion" value="${factorProliferacion}" readonly>
                </div>
                <div class="col-md-6"></div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <label>Elija el tipo de umbral:</label>
                  <select class="form-control" id="selUmbral" name="selUmbral">
                    <option value="1" selected>Automático</option>
                    <option value="2">Manual</option>
                  </select>
                </div>
                <div class="col-md-6">
                    <label>Umbral:</label>
                    <input class="form-control" type="text" name="umbralManual" id="umbralManual" value="${umbral}" disabled>
                </div>
              </div>
              <div class="row">
                <div class="col text-right">
                  <input class="btn orange text-white"  type="submit" name="procesado-2D" value="Procesar"/>
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
  </body>
</html>

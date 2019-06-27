<%@ page contentType="text/html;charset=UTF-8" language="Java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Procesado 3D</title>
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
    <script src="https://threejsfundamentals.org/threejs/resources/threejs/r105/three.min.js"></script>
    <script src="https://threejsfundamentals.org/threejs/resources/threejs/r105/js/controls/OrbitControls.js"></script>
    <script src="https://threejsfundamentals.org/threejs/resources/threejs/r105/js/loaders/LoaderSupport.js"></script>
    <script src="https://threejsfundamentals.org/threejs/resources/threejs/r105/js/loaders/OBJLoader2.js"></script>
    <script src="https://threejsfundamentals.org/threejs/resources/threejs/r105/js/loaders/MTLLoader.js"></script>
    <style>#c1, #c2 {
        width: 100%;
        height: 100%;
        display: block;
        border: 1px solid #F36026;
    }</style>
</head>
<body>

<!--SCRIPTS-->
<!--Elegir tipo de procesado en el select y dejar habilitado el campo umbral si el procesado es manual-->
<script>
    $(document).ready(function () {
        var valor = $('input:hidden[name=modoProcesado3D]').val();
        $("#selTipoProcesado").val(valor);
        if (valor != "1") {
            $("#valorUmbral").removeAttr("readonly");
        }
    });
</script>

<!--Bloquear cuando se produce algún cambio el campo umbral si el procesado es automático y habilitar si es manual para definir el valor -->
<script>
    $(function () {
        $("#selTipoProcesado").on('change', function () {
            if ($(this).val() === "1") {
                $('input[name=valorUmbral]').val(' ');
                $("#valorUmbral").attr("readonly", "readonly");
            } else {
                $("#valorUmbral").removeAttr("readonly");
            }
        });
    });
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
                        <li class="nav-item">
                            <a class="nav-link text-white" href="<c:url value='/carga3D'/>">Carga 3D</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Procesado 3D <span class="sr-only">(current)</span></a>
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
                                categories: [<c:forEach items="${resultados3D.range}" var="rg0" varStatus="rg0Status">
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
                                    value:${resultados3D.posicionUmbral}
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
                                data: [<c:forEach items="${resultados3D.NGFP_0}" var="n0" varStatus="n0Status">
                                    ${n0}
                                    <c:if test="${!n0Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>]
                            },
                                {
                                    name: 'nGFP_1',
                                    data: [<c:forEach items="${resultados3D.NGFP_1}" var="n1" varStatus="n1Status">
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
                                categories: [<c:forEach items="${resultados3D.range}" var="rg1" varStatus="rg1Status">
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
                                    value:${resultados3D.posicionUmbral}
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
                                data: [<c:forEach items="${resultados3D.meanGFP_0}" var="mn0" varStatus="mn0Status">
                                    ${mn0}
                                    <c:if test="${!mn0Status.last}">
                                    ,
                                    </c:if>
                                    </c:forEach>]
                            },
                                {
                                    name: 'meanGFP_1',
                                    data: [<c:forEach items="${resultados3D.meanGFP_1}" var="mn1" varStatus="mn1Status">
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
                    <canvas id="c1"></canvas>
                    <input type="hidden" name="identificadorUsuario" id="identificadorUsuario" value="${idUsuario}">
                    <script>
                        'use strict';

                        //global THREE
                        function main() {
                            var canvas = document.querySelector('#c1');
                            var renderer = new THREE.WebGLRenderer({canvas: canvas});

                            var fov = 45;
                            var aspect = 2;  // the canvas default
                            var near = 0.1;
                            var far = 100;
                            var camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
                            camera.position.set(0, 10, 20);

                            var controls = new THREE.OrbitControls(camera, canvas);
                            controls.target.set(0, 5, 0);
                            controls.update();

                            var scene = new THREE.Scene();
                            scene.background = new THREE.Color('#D7D7D7');

                            {
                                var skyColor = 0xB1E1FF;  // light blue
                                var groundColor = 0xB97A20;  // brownish orange
                                var intensity = 1;
                                var light = new THREE.HemisphereLight(skyColor, groundColor, intensity);
                                scene.add(light);
                            }

                            {
                                var color = 0xFFFFFF;
                                var intensity = 1;
                                var light = new THREE.DirectionalLight(color, intensity);
                                light.position.set(0, 10, 0);
                                light.target.position.set(-5, 0, 0);
                                scene.add(light);
                                scene.add(light.target);
                            }

                            function frameArea(sizeToFitOnScreen, boxSize, boxCenter, camera) {
                                var halfSizeToFitOnScreen = sizeToFitOnScreen * 0.5;
                                var halfFovY = THREE.Math.degToRad(camera.fov * .5);
                                var distance = halfSizeToFitOnScreen / Math.tan(halfFovY);
                                // compute a unit vector that points in the direction the camera is now
                                // from the center of the box
                                var direction = (new THREE.Vector3()).subVectors(camera.position, boxCenter).normalize();

                                // move the camera to a position distance units way from the center
                                // in whatever direction the camera was from the center already
                                camera.position.copy(direction.multiplyScalar(distance).add(boxCenter));

                                // pick some near and far values for the frustum that
                                // will contain the box.
                                camera.near = boxSize / 100;
                                camera.far = boxSize * 100;

                                camera.updateProjectionMatrix();

                                // point the camera to look at the center of the box
                                camera.lookAt(boxCenter.x, boxCenter.y, boxCenter.z);
                            }


                            {
                                var usuarioId = document.getElementById("identificadorUsuario").value;
                                var objLoader = new THREE.OBJLoader2();
                                objLoader.load('/zebrafish/' + usuarioId + '/obj_0h', function (event) {
                                    var root = event.detail.loaderRootNode;
                                    scene.add(root);
                                    // compute the box that contains all the stuff
                                    // from root and below

                                    var box = new THREE.Box3().setFromObject(root);

                                    var boxSize = box.getSize(new THREE.Vector3()).length();
                                    var boxCenter = box.getCenter(new THREE.Vector3());

                                    // set the camera to frame the box
                                    frameArea(boxSize * 1.2, boxSize, boxCenter, camera);

                                    // update the Trackball controls to handle the new size
                                    controls.maxDistance = boxSize * 10;
                                    controls.target.copy(boxCenter);
                                    controls.update();
                                });
                            }

                            function resizeRendererToDisplaySize(renderer) {
                                var canvas = renderer.domElement;
                                var width = canvas.clientWidth;
                                var height = canvas.clientHeight;
                                var needResize = canvas.width !== width || canvas.height !== height;
                                if (needResize) {
                                    renderer.setSize(width, height, false);
                                }
                                return needResize;
                            }

                            function render() {

                                if (resizeRendererToDisplaySize(renderer)) {
                                    var canvas = renderer.domElement;
                                    camera.aspect = canvas.clientWidth / canvas.clientHeight;
                                    camera.updateProjectionMatrix();
                                }

                                renderer.render(scene, camera);

                                requestAnimationFrame(render);
                            }

                            requestAnimationFrame(render);
                        }

                        main();

                    </script>
                </div>
                <div class="col-md-6 muygrande">
                    <canvas id="c2"></canvas>
                    <script>
                        'use strict';

                        //global THREE
                        function main() {
                            var canvas = document.querySelector('#c2');
                            var renderer = new THREE.WebGLRenderer({canvas: canvas});

                            var fov = 45;
                            var aspect = 2;  // the canvas default
                            var near = 0.1;
                            var far = 100;
                            var camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
                            camera.position.set(0, 10, 20);

                            var controls = new THREE.OrbitControls(camera, canvas);
                            controls.target.set(0, 5, 0);
                            controls.update();

                            var scene = new THREE.Scene();
                            scene.background = new THREE.Color('#D7D7D7');

                            {
                                var skyColor = 0xB1E1FF;  // light blue
                                var groundColor = 0xB97A20;  // brownish orange
                                var intensity = 1;
                                var light = new THREE.HemisphereLight(skyColor, groundColor, intensity);
                                scene.add(light);
                            }

                            {
                                var color = 0xFFFFFF;
                                var intensity = 1;
                                var light = new THREE.DirectionalLight(color, intensity);
                                light.position.set(0, 10, 0);
                                light.target.position.set(-5, 0, 0);
                                scene.add(light);
                                scene.add(light.target);
                            }

                            function frameArea(sizeToFitOnScreen, boxSize, boxCenter, camera) {
                                var halfSizeToFitOnScreen = sizeToFitOnScreen * 0.5;
                                var halfFovY = THREE.Math.degToRad(camera.fov * .5);
                                var distance = halfSizeToFitOnScreen / Math.tan(halfFovY);
                                // compute a unit vector that points in the direction the camera is now
                                // from the center of the box
                                var direction = (new THREE.Vector3()).subVectors(camera.position, boxCenter).normalize();

                                // move the camera to a position distance units way from the center
                                // in whatever direction the camera was from the center already
                                camera.position.copy(direction.multiplyScalar(distance).add(boxCenter));

                                // pick some near and far values for the frustum that
                                // will contain the box.
                                camera.near = boxSize / 100;
                                camera.far = boxSize * 100;

                                camera.updateProjectionMatrix();

                                // point the camera to look at the center of the box
                                camera.lookAt(boxCenter.x, boxCenter.y, boxCenter.z);
                            }


                            {
                                var usuarioId = document.getElementById("identificadorUsuario").value;
                                var objLoader = new THREE.OBJLoader2();
                                objLoader.load('/zebrafish/' + usuarioId + '/obj_48h', function (event) {
                                    var root = event.detail.loaderRootNode;
                                    scene.add(root);
                                    // compute the box that contains all the stuff
                                    // from root and below
                                    var box = new THREE.Box3().setFromObject(root);

                                    var boxSize = box.getSize(new THREE.Vector3()).length();
                                    var boxCenter = box.getCenter(new THREE.Vector3());

                                    // set the camera to frame the box
                                    frameArea(boxSize * 1.2, boxSize, boxCenter, camera);

                                    // update the Trackball controls to handle the new size
                                    controls.maxDistance = boxSize * 10;
                                    controls.target.copy(boxCenter);
                                    controls.update();
                                });
                            }

                            function resizeRendererToDisplaySize(renderer) {
                                var canvas = renderer.domElement;
                                var width = canvas.clientWidth;
                                var height = canvas.clientHeight;
                                var needResize = canvas.width !== width || canvas.height !== height;
                                if (needResize) {
                                    renderer.setSize(width, height, false);
                                }
                                return needResize;
                            }

                            function render() {

                                if (resizeRendererToDisplaySize(renderer)) {
                                    var canvas = renderer.domElement;
                                    camera.aspect = canvas.clientWidth / canvas.clientHeight;
                                    camera.updateProjectionMatrix();
                                }

                                renderer.render(scene, camera);

                                requestAnimationFrame(render);
                            }

                            requestAnimationFrame(render);
                        }

                        main();

                    </script>
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
                        <form action="<c:url value='/exportarGraficos3D'/>" method="GET">
                            <label>Guardar datos gráficas nGFP & meanGFP:</label> <br>
                            <input class="btn orange text-white" type="submit" name="exportar-3D" value="Exportar"/>
                        </form>
                    </div>
                    <div class="col-md-6">
                        <label>Factor de proliferación:</label>
                        <input class="form-control" type="text" name="FactProliferacion"
                               value="<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${resultados3D.factorProliferacion}" />"
                               readonly>
                    </div>
                </div>
                <form id="reProcesado3D" action="<c:url value='/reprocesar3D'/>" method="POST">
                    <input type="hidden" name="modoProcesado3D" id="modoProcesado3D" value="${tipoProcesado3D}">
                    <div class="row">
                        <div class="col-md-6">
                            <label>Elija el tipo de procesado:</label>
                            <select class="form-control" id="selTipoProcesado" name="selTipoProcesado">
                                <option id="procAuto" value="1">Automático</option>
                                <option id="procManual" value="2">Manual</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label>Umbral:</label>
                            <input class="form-control" type="number" name="valorUmbral" id="valorUmbral" min="0"
                                   max="${resultados3D.fin}" step="${resultados3D.paso}" data-bind="value:replyNumber"
                                   value="${resultados3D.umbral}" readonly>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col text-right">
                            <input class="btn orange text-white" type="submit" name="reprocesado-3D" value="Procesar"/>
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

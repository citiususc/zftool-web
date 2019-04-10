<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page language="java" import="java.util.*" %>
<%@page language="java" import="java.io.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<html lang="es">
  <head>
    <title>Carga Imagenes 2D</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="./css/styles.css">
  </head>
  <body>

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
            <li class="nav-item active">
              <a class="nav-link" href="#">Carga 2D <span class="sr-only">(current)</span></a>
            </li>

          </ul>
          </div>
          </nav>
        </div>
      </div>

      <!-- Carga de imágenes-->
      <p class="text-center orange text-white">
        Carga de imágenes
      </p>
        <form id="cargarImaxes" action="./servlet" method="post">
      <div class="row justify-content-center ">
        <div class="col-md-11 inter">
          <div class="row mt-3">
            <div class="col-6">
                  <label for="exampleFormControlFile1">Imagen 0h gris</label>
                  <input type="file" class="form-control-file" name="imagen0hg" id="exampleFormControlFile1" accept="image/*" required>
            </div>
            <div class="col-6">
                  <label for="exampleFormControlFile1">Imagen 24-48-72h gris</label>
                  <input type="file" class="form-control-file" name="imagen72hg" id="exampleFormControlFile2" accept="image/*" required>
            </div>
          </div>
          <div class="row mt-5">
            <div class="col-6">
                  <label for="exampleFormControlFile1">Imagen 0h fluorescencia</label>
                  <input type="file" class="form-control-file" name="imagen0hf" id="exampleFormControlFile3" accept="image/*">
            </div>
            <div class="col-6">
                  <label for="exampleFormControlFile1">Imagen 24-48-72h fluorescencia</label>
                  <input type="file" class="form-control-file" name="imagen72hf" id="exampleFormControlFile4" accept="image/*">
            </div>
          </div>
        </div>
      </div>

      <p class="text-center text-danger"><c:out value="${requestScope.erro}" /></p>

          <p class="text-center orange text-white">Fijar umbrales</p>
      <div class="row mt-1">
        <div class="col-12 mediun">
          <div class="form-group">
              <div class="row">
                <div class="col">
                  <label>Inicio:</label>
                  <input class="form-control" type="text" name="Inicio" value="0" readonly>
                </div>
                <div class="col">
                  <label>Fin:</label>
                  <input class="form-control" type="text" name="Fin" value="50" required>
                </div>
                <div class="col">
                  <label>Paso:</label>
                  <input class="form-control" type="text" name="Paso" value="5" required>
                </div>

              </div>
              <div class="row">
                <div class="col text-right">
                  <input class="btn orange text-white" name="carga-imaxes"  type="submit" value="Procesar"/>
                </div>
              </div>
          </div>
        </div>
      </div>
        </form>
      <div class="row mt-1 justify-content-center p-1">
        <div class="col-12 pequena orange">
        </div>
      </div>

    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
  </body>
</html>

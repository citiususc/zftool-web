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
    var usuarioId=document.getElementById("identificadorUsuario").value;
    var objLoader = new THREE.OBJLoader2();
    objLoader.load('/zebrafish/'+usuarioId+'/obj_0h', function (event) {
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
# -*- coding: utf-8 -*-

from skimage import io
import sys        
import numpy as np 
import matplotlib.pyplot as plt    
"""cargamos las imágenes, teniendo el archivo .py dentro de la carpeta"""
print "Num argumento:",len(sys.argv)-3
print "Entrada:",str(sys.argv[3:])
data_0= np.array([io.imread(i) for i in sys.argv[3:]])
data_0=data_0[:,:,:,1]
data_0=data_0.T
"""quitamos la parte de la escala que aparece en las fotos"""
data_0[850:1024,995:1024,:]=0

from mayavi import mlab
from tvtk.api import tvtk
from tvtk.common import configure_input
"""función para hallar el volumen en voxels con el mismo algoritmo de 2D, 
devolviendo la máscara con el umbral óptimo y de paso la intensidad media"""
def volumen_voxels(imagen):
    mask3d=imagen>int(sys.argv[2])
    mask3d=mask3d.astype(int)
    objeto=tvtk.ImageData(spacing=(1,1,1),origin=(0,0,0))
    objeto.point_data.scalars=mask3d.ravel()
    objeto.point_data.scalars.name = 'scalars'
    objeto.dimensions=imagen.shape
    iso=tvtk.ImageMarchingCubes()
    configure_input(iso, objeto)
    mass=tvtk.MassProperties()
    configure_input(mass, iso)	
    volumen3d=mass.volume
    
    print "volumen en voxels por tratamiento 3d:",volumen3d
    
    nGFPfinal=(mask3d==1).sum() 
    fGmascarafinal=np.multiply(mask3d,imagen)
    intensidadmedia=float((fGmascarafinal).sum())/volumen3d        
    print "la intensidad media con el tratamiento 3d es:",intensidadmedia
    print "el volumen haciendo la suma de los voxels de la mask es:",nGFPfinal
    intensidadmedia=float((fGmascarafinal).sum())/nGFPfinal
    print "la intensidad media con la suma de voxels de la mask es:",intensidadmedia	
    
    return mask3d
"""mostramos las imágenes 3d"""
vol=volumen_voxels(data_0)
vol=np.multiply(vol,data_0)
"""fig1 = mlab.figure(figure='Superficie',bgcolor=(0, 0, 0), size=(400, 500))
fig1.scene.disable_render = True
src1= mlab.pipeline.scalar_field(vol)
representacion_1=mlab.pipeline.iso_surface(src1,color=(1,0,0))
fig2 = mlab.figure(figure='Volumen',bgcolor=(0, 0, 0), size=(400, 500))
fig2.scene.disable_render = True
src2= mlab.pipeline.scalar_field(vol)
representacion_0=mlab.pipeline.volume(src2,color=(0,1,0))
fig3 = mlab.figure(figure='Superficie y Volumen',bgcolor=(0, 0, 0), size=(400, 500))
fig3.scene.disable_render = True
src3= mlab.pipeline.scalar_field(vol)
representacion_2=mlab.pipeline.iso_surface(src3,color=(1,0,0))
representacion_3=mlab.pipeline.volume(src3,color=(0,1,0))"""
mlab.options.offscreen=True
mlab.pipeline.iso_surface(mlab.pipeline.scalar_field(vol),color=(1,0,0),opacity=0.5)
mlab.savefig(sys.argv[1])
"""mlab.close(all=True)
mlab.show()"""

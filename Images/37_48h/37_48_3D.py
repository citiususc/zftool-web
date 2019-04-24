# -*- coding: utf-8 -*-
"""
Created on Wed Jul 20 09:58:24 2016

@author: Rodrigo
"""

from skimage import io       
import numpy as np 
import matplotlib.pyplot as plt    
"""cargamos las imágenes, teniendo el archivo .py dentro de la carpeta"""
"""utilizamos concatenate porque el nombre de las carpetas cambia de 0i a i"""

data_0= np.array([io.imread('/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/37_48h/pez 37_0%i.tif' % i) for i in range(0, 10)])
data_0= np.concatenate((data_0,np.array([io.imread('/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/37_48h/pez 37_%i.tif' % i) for i in range(10, 39)])),axis=0)
data_0=data_0[:,:,:,1]
data_0=data_0.T
"""quitamos la parte de la escala que aparece en las fotos"""
data_0[850:1024,995:1024,:]=0
"""función para calcular el área en pixels de cada corte, utilizando el algoritmo de imágenes 2D"""
def pixels_de_cada_imagen(imagen):
    nGFP=[0 for x in range(0,50/5+1)]
    meanGFP=[0 for x in range(0,50/5+1)]
    ndatos=0
    for umbral in range(0,55,5):
        mascaraG=imagen>umbral
        nGFP[ndatos]=(mascaraG==1).sum() 
        if nGFP[ndatos]!=0:
            fGmascara=np.multiply(mascaraG,imagen)
            meanGFP[ndatos]=float((fGmascara).sum())/nGFP[ndatos]
        else:
            meanGFP[ndatos]=0
            
        ndatos+=1
      
   
    ndatosmenos1=ndatos-1
    comparacion=np.zeros((ndatosmenos1)) 
    
    for i in range(ndatosmenos1):
        if nGFP[i]!=0:
            comparacion[i]=float(nGFP[i+1])/nGFP[i]
        else:
            comparacion[i]=0
    difumb=1.0
    encontrado=0
    while encontrado==0:
        encontrado=0
        difumb=difumb-0.005
        contador=0
        for i in range(ndatosmenos1):
       
            if comparacion[i]>difumb:           
                contador+=1
            
                if contador==3:
                    umbrales=(i-1)*5 #el umbral está en la primera posición de las tres
                    encontrado=1
            else:
                contador=0
                   
    mascarafinal=imagen>umbrales
    area=(mascarafinal==1).sum()
    return area
"""volumen a partir de los pixels"""    
"""este for va en función del número de imágenes:39"""  
def volumen(resolucion_pixel=1,altura=1): 
    suma=0   
    for n in range (0,39): 
        suma+=pixels_de_cada_imagen(data_0[:,:,n])*resolucion_pixel*altura
    
    return suma
    
vol=volumen()
print "el volumen con los cortes es(dato orientativo):",vol

from mayavi import mlab
from tvtk.api import tvtk
from tvtk.common import configure_input
"""función para hallar el volumen en voxels con el mismo algoritmo de 2D, 
devolviendo la máscara con el umbral óptimo y de paso la intensidad media"""
def volumen_voxels(imagen):
    nGFP=[0 for x in range(0,50/5+1)]
    meanGFP=[0 for x in range(0,50/5+1)]
    ndatos=0
    for umbral in range(0,55,5):
        mascaraG=imagen>umbral
        nGFP[ndatos]=(mascaraG==1).sum() 
        if nGFP[ndatos]!=0:
            fGmascara=np.multiply(mascaraG,imagen)
            meanGFP[ndatos]=float((fGmascara).sum())/nGFP[ndatos]
        else:
            meanGFP[ndatos]=0
            
        ndatos+=1
        """representaciones gráficas"""   
    plt.figure("grafica1")
    plt.suptitle('Evolution of voxels with GFP threshold')
    plt.figure("grafica2")
    plt.suptitle('Evolution of mean intensity with GFP threshold')
    plt.figure("grafica1")
    x=[x for x in range(0,55,5) ]
    plt.plot(x,nGFP,'b*-')
    plt.grid(True)
    plt.ylabel('voxels with GFP>threshold')
    plt.xlabel('GFP threshold')
    plt.figure("grafica2")
    plt.plot(x,meanGFP,'b*-')
    plt.ylabel('Mean intensity of voxels with GFP>threshold')
    plt.xlabel('GFP threshold')
    plt.grid(True) 
   

    f=open("/home/marcos/Escritorio/TFG_peixe_cebra/Codigo/AppZebraFish/Images/37_48h/datos2.txt","w")
    for nG in nGFP:	
    	f.write(str(nG))
    	f.write(" ")
    f.write("\n")	
    for mG in meanGFP:	
    	f.write(str(mG))
    	f.write(" ")
    f.write("\n")    
	
    ndatosmenos1=ndatos-1
    comparacion=np.zeros((ndatosmenos1)) 
    
    for i in range(ndatosmenos1):
        if nGFP[i]!=0:
            comparacion[i]=float(nGFP[i+1])/nGFP[i]
        else:
            comparacion[i]=0
    difumb=1.0
    encontrado=0
    while encontrado==0:
        encontrado=0
        difumb=difumb-0.005
        contador=0
        for i in range(ndatosmenos1):
       
            if comparacion[i]>difumb:           
                contador+=1
            
                if contador==3:
                    umbrales=(i-1)*5 
                    encontrado=1
            else:
                contador=0
                   
    mask3d=imagen>umbrales
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
    f.write(str(umbrales))
    f.write("\n")    
    f.close()
		
    print "el umbral final es:",umbrales
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
representacion_3=mlab.pipeline.volume(src3,color=(0,1,0))
mlab.show()"""

ImageProcessing
===============
Sampling:
The source code is present in   "imageSampling.java"
The code can be run by using any image of the size 352*288 as below,
imageSampling.java <ImageFileName>.rgb Y U V Q
where 
Y= subsampling parameter for Y,
U=subsampling parameter for U, and 
V=subsampling parameter for V,
Q=quantization parameter (1-256)
The Analysis is described in the report file "Sampling Analysis.pdf"


Vector Quantization:
The source code is present in   "VectorQuantize.java"
The code can be run by using any image of the size 352*288 as below,
VectorQuantize.java <ImageFileName>.rgb N
where:
N=quantization parameter (2-256) which is a power of2
the program works efficiently for all the values of N between 2-256

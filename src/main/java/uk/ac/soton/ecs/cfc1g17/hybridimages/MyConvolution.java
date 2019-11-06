package uk.ac.soton.ecs.cfc1g17.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private float[][] kernel;

    public MyConvolution(float[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public void processImage(FImage image) {
        //Create a new image with the same dimensions as the original.All pixels will be black.
        FImage tempImage = new FImage(image.width, image.height);

        //extract the height and width of the kernel from the array it represents;
        int kernelWidth = kernel[0].length;
        int kernelHeight = kernel.length;

        //iterate over every pixel in the image, but exclude the edges as they cannot be sampled.
        for(int y = kernelHeight/2; y < tempImage.getHeight() - kernelHeight/2; y++){
            for (int x = kernelWidth/2; x < tempImage.getWidth() - kernelWidth/2; x++){
                //create a variable "sum" to sum the values of the original image according to the weights in the kernel.
                float sum = 0f;
                //iterate over every row and column of the kernel template.
                for(int kernelRow = 0; kernelRow < kernelHeight; kernelRow++){
                    for(int kernelCol = 0; kernelCol < kernelWidth; kernelCol++){
                        //multiply the value of the relevant pixel with its corresponding value in the matrix and add it to the sum.
                        sum += image.getPixel(x + kernelCol - kernelWidth/2, y + kernelRow - kernelHeight/2) * kernel[kernelRow][kernelCol];
                    }
                }
                //change the value of the pixel in tempImage to reflect the weighted sum of pixels in the original image.
                tempImage.setPixel(x,y,sum);
            }
        }

        //store the result back in the image object which was passed to the method.
        image.internalAssign(tempImage);
    }
}
package uk.ac.soton.ecs.cfc1g17.hybridimages;

import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;

public class MyHybridImages {
    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage
     *            the image to which apply the low pass filter
     * @param lowSigma
     *            the standard deviation of the low-pass filter
     * @param highImage
     *            the image to which apply the high pass filter
     * @param highSigma
     *            the standard deviation of the low-pass component of computing the
     *            high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
       //compute the size of the kernels which will be used for the low and high pass according to "best practice". If it's even then add one.
        int lowSize = (int)(8.0f + lowSigma + 1.0f);
        lowSize = (lowSize % 2 == 0) ? lowSize + 1 : lowSize;
        int highSize  = (int)(8.0f + highSigma + 1.0f);
        highSize = (highSize % 2 == 0) ? highSize + 1 : highSize;

        //Create a kernel from the lowSigma ans size value.
        float[][] lowPassKernel = Gaussian2D.createKernelImage(lowSize,lowSigma).pixels;
        float[][] highPassKernel = Gaussian2D.createKernelImage(highSize,highSigma).pixels;

        //Use the MyConvolution class to convolve the low image using lowPassKernel.
        lowImage.processInplace(new MyConvolution(lowPassKernel));

        //Use the MyConvolution class to convolve the high image using the highPassKernel, then subtract this image from the original.
        MBFImage processedHighImage = highImage.process(new MyConvolution(highPassKernel));
        highImage.subtractInplace(processedHighImage);

        //Sum both images to create the final hybrid image.
        MBFImage finalImage = lowImage.add(highImage);

        //create a cropping kernel based on the larger of the lowPassKernel and HighPassKernel, process the final image in place to add a 0-padding border.
        int maxSize = Math.max(lowSize,highSize);
        float[][] cropKernel = new float[maxSize][maxSize];
        cropKernel[maxSize/2][maxSize/2] = 1f;
        finalImage.processInplace(new MyConvolution(cropKernel));

        //return the final image.
        return finalImage;
    }
}
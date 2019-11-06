package uk.ac.soton.ecs.cfc1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.resize.ResizeProcessor;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        MBFImage source1 = ImageUtilities.readMBF(new File("src/main/resources/source1.png"));
        MBFImage source2 = ImageUtilities.readMBF(new File("src/main/resources/source2.png"));

        int sigmaCutoff = 6;
        MBFImage result = MyHybridImages.makeHybrid(source1,2,source2,3);

        MBFImage finalImage = new MBFImage((31*result.getWidth())/16 + 4*10 + 20, result.getHeight()+20);
        finalImage.fill(new Float[]{1f,1f,1f});
        int offset = 10;
        for (int i = 0; i < 5; i++){
            finalImage.drawImage(result,offset,(finalImage.getHeight() - result.getHeight())/2);
            offset += result.getWidth() + 10;
            result = ResizeProcessor.halfSize(result);
        }
        ImageUtilities.write(finalImage,new File("C:\\Users\\chris\\IdeaProjects\\HybridImages\\output.png"));
        DisplayUtilities.display(finalImage);
    }
}

package ar.com.test.loaddata;

import com.luciad.imageio.webp.WebPWriteParam;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ImageProcessor {
    private static final String BASE_URL = "https://api.myl.cl/static/cards/";
    private static final Region REGION = Region.US_EAST_1;
    private static final String BUCKET_NAME = "myl-images-bucket";
    private static final String AWS_FOLDER = "images/";
    private final S3Client s3Client;
    private final PutObjectRequest.Builder baseRequestBuilder;


    public ImageProcessor() {
        this.s3Client = S3Client.builder()
                .region(REGION)
                .build();
        this.baseRequestBuilder = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .contentType("image/webp");
    }

    public BufferedImage getImageFromApi(String collectionId, String cardId) {
        String url = BASE_URL + collectionId + "/" + cardId + ".png";
        try (InputStream inputStream = new URL(url).openStream()) {
            return ImageIO.read(inputStream);
        } catch (MalformedURLException e) {
            System.err.println("Error building url " + url);
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Error parsing data from the api");
        }
        return null;
    }

    public byte[] compressPngToWebp(BufferedImage srcImage, float quality) {
        try {
            ImageIO.scanForPlugins();
            BufferedImage rgb = new BufferedImage(
                    srcImage.getWidth(),
                    srcImage.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR
            );
            Graphics2D g = rgb.createGraphics();
            g.drawImage(srcImage, 0, 0, null);
            g.dispose();


            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp")
                    .next();
            WebPWriteParam param = new WebPWriteParam(writer.getLocale());
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            param.setCompressionType(param.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            param.setCompressionQuality(quality);

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream)) {

                writer.setOutput(ios);
                writer.write(null, new IIOImage(rgb, null, null), param);
                ios.flush();
                writer.dispose();

                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean imageUpload(byte[] imageData, UUID imageUUID) {
        try {
            String imageKey = AWS_FOLDER + imageUUID.toString() + ".webp";
            PutObjectRequest putObjectRequest = baseRequestBuilder.key(imageKey).build();

//            TODO uncomment to send image to aws
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));
            // Check response
            int statusCode = response.sdkHttpResponse().statusCode();
            if (response.sdkHttpResponse().isSuccessful()) {
                System.out.printf("\nUpload succeeded (HTTP %d), ETag: %s%n", statusCode, response.eTag());
                System.out.println("Successfully uploaded to " + BUCKET_NAME + "/" + AWS_FOLDER + imageUUID + ".webp");
                return true;
            } else {
                throw new Exception("Error upload returned " + statusCode);
            }
            // TODO comment to use the real implementation
//            card.setImageUuid(imageUUID);
//            return true;
        } catch (Exception e) {
            System.err.println("Error uploading image " + e.getMessage());
            return false;
        }
    }

    private boolean deleteImageFromAws(String imageKey) {
        DeleteObjectRequest dor = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(imageKey)
                .build();

        DeleteObjectResponse resp = s3Client.deleteObject(dor);
        int deleteStatusCode = resp.sdkHttpResponse().statusCode();
        if (resp.sdkHttpResponse().isSuccessful()) {
            System.out.printf("Successfully deleted s3://%s/%s (HTTP %d)%n", BUCKET_NAME, imageKey, deleteStatusCode);
            return true;
        } else {
            System.err.printf("Delete returned error (HTTP %d)%n", deleteStatusCode);
            return false;
        }
    }

    public boolean imageExists(UUID imageUUID) {
        try {
            String imageKey = AWS_FOLDER + imageUUID.toString() + ".webp";

            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(imageKey)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true; // If headObject doesn't throw an exception, the object exists
        } catch (NoSuchKeyException e) {
            return false; // Object not found
        } catch (S3Exception | SdkClientException e) {
            // Handle other S3 exceptions (e.g., network issues, invalid credentials)
            System.err.println("Error checking file existence: " + e.getMessage());
            return false;
        }
    }
}

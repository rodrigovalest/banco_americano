package com.rodrigovalest.ms_costumer.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rodrigovalest.ms_costumer.exceptions.AWSErrorException;
import com.rodrigovalest.ms_costumer.exceptions.FileSizeException;
import com.rodrigovalest.ms_costumer.utils.NameGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class AWSServiceTest {

    @Mock
    private AmazonS3 s3Instance;

    @Spy
    @InjectMocks
    private AWSService awsService;

    @Test
    public void upload_WithValidData_ReturnsStringWithPhotoUrl() throws Exception {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String filename = "currentTimeMillis__randomuuid.jpg";
        String photoUrl = "https://example.com/" + filename;

        MockedStatic<NameGenerator> mockedNameGenerator = Mockito.mockStatic(NameGenerator.class);
        mockedNameGenerator.when(NameGenerator::generate).thenReturn("currentTimeMillis__randomuuid");

        when(this.s3Instance.getUrl(any(), any())).thenReturn(new URL(photoUrl));

        // Act
        String sut = awsService.upload(base64Photo);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut).isEqualTo(photoUrl);

        verify(this.s3Instance, times(1)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(1)).getUrl(any(), eq(filename));

        mockedNameGenerator.close();
    }

    @Test
    public void upload_WithLargeBase64Photo_ThrowsFileSizeException() throws Exception {
        // Arrange
        StringBuilder sb = new StringBuilder();
        String base64Chunk = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        int targetSize = 7_000_000; // 7 MB image

        while (sb.length() < targetSize)
            sb.append(base64Chunk);

        String largeBase64Photo = sb.toString();

        // Act
        Assertions.assertThatThrownBy(() -> this.awsService.upload(largeBase64Photo)).isInstanceOf(FileSizeException.class);

        // Assert
        verify(this.s3Instance, times(0)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(0)).getUrl(any(), any());
    }

    @Test
    public void upload_WithPutObjectErrorThrowingSdkClientException_ThrowsAWSErrorException() throws Exception {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String filename = "currentTimeMillis__randomuuid.jpg";
        String photoUrl = "https://example.com/" + filename;

        MockedStatic<NameGenerator> mockedNameGenerator = Mockito.mockStatic(NameGenerator.class);
        mockedNameGenerator.when(NameGenerator::generate).thenReturn("currentTimeMillis__randomuuid");

        when(this.s3Instance.putObject(any(PutObjectRequest.class))).thenThrow(SdkClientException.class);

        // Act
        Assertions.assertThatThrownBy(() -> this.awsService.upload(base64Photo)).isInstanceOf(AWSErrorException.class);

        // Assert
        verify(this.s3Instance, times(1)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(0)).getUrl(any(), eq(filename));

        mockedNameGenerator.close();
    }

    @Test
    public void upload_WithGetUrlThrowingSdkClientException_ThrowsAWSErrorException() throws Exception {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String filename = "currentTimeMillis__randomuuid.jpg";
        String photoUrl = "https://example.com/" + filename;

        MockedStatic<NameGenerator> mockedNameGenerator = Mockito.mockStatic(NameGenerator.class);
        mockedNameGenerator.when(NameGenerator::generate).thenReturn("currentTimeMillis__randomuuid");

        when(this.s3Instance.getUrl(any(), any())).thenThrow(SdkClientException.class);

        // Act
        Assertions.assertThatThrownBy(() -> this.awsService.upload(base64Photo)).isInstanceOf(AWSErrorException.class);

        // Assert
        verify(this.s3Instance, times(1)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(1)).getUrl(any(), eq(filename));

        mockedNameGenerator.close();
    }

    @Test
    public void upload_WithPutObjectErrorThrowingAmazonS3Exception_ThrowsAWSErrorException() throws Exception {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String filename = "currentTimeMillis__randomuuid.jpg";
        String photoUrl = "https://example.com/" + filename;

        MockedStatic<NameGenerator> mockedNameGenerator = Mockito.mockStatic(NameGenerator.class);
        mockedNameGenerator.when(NameGenerator::generate).thenReturn("currentTimeMillis__randomuuid");

        when(this.s3Instance.putObject(any(PutObjectRequest.class))).thenThrow(AmazonS3Exception.class);

        // Act
        Assertions.assertThatThrownBy(() -> this.awsService.upload(base64Photo)).isInstanceOf(AWSErrorException.class);

        // Assert
        verify(this.s3Instance, times(1)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(0)).getUrl(any(), eq(filename));

        mockedNameGenerator.close();
    }

    @Test
    public void upload_WithGetUrlThrowingAmazonS3Exception_ThrowsAWSErrorException() throws Exception {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String filename = "currentTimeMillis__randomuuid.jpg";
        String photoUrl = "https://example.com/" + filename;

        MockedStatic<NameGenerator> mockedNameGenerator = Mockito.mockStatic(NameGenerator.class);
        mockedNameGenerator.when(NameGenerator::generate).thenReturn("currentTimeMillis__randomuuid");

        when(this.s3Instance.getUrl(any(), any())).thenThrow(AmazonS3Exception.class);

        // Act
        Assertions.assertThatThrownBy(() -> this.awsService.upload(base64Photo)).isInstanceOf(AWSErrorException.class);

        // Assert
        verify(this.s3Instance, times(1)).putObject(any(PutObjectRequest.class));
        verify(this.s3Instance, times(1)).getUrl(any(), eq(filename));

        mockedNameGenerator.close();
    }

    @Test
    public void delete_WithValidUrlPhoto_ReturnsVoid() {
        // Arrange
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        // Act
        this.awsService.delete(urlPhoto);

        // Assert
        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
    }

    @Test
    public void delete_WithInvalidDataThrowingAmazonS3Exception_ThrowsException() {
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        doThrow(new AmazonS3Exception("AWS Error")).when(this.s3Instance).deleteObject(any(), eq(filename));

        Assertions.assertThatThrownBy(() -> this.awsService.delete(urlPhoto)).isInstanceOf(AWSErrorException.class);
        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
    }

    @Test
    public void delete_WithInvalidDataThrowingSdkClientException_ThrowsException() {
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        doThrow(new SdkClientException("AWS Error")).when(this.s3Instance).deleteObject(any(), eq(filename));

        Assertions.assertThatThrownBy(() -> this.awsService.delete(urlPhoto)).isInstanceOf(AWSErrorException.class);
        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
    }

    @Test
    public void update_WithValidData_ReturnsNewUrlPhotoString() {
        // Arrange
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";
        String newUrlPhoto = "https://example.com/newrandomurlphoto.jpg";

        doReturn(newUrlPhoto).when(this.awsService).upload(base64Photo);

        // Act
        String sut = this.awsService.update(urlPhoto, base64Photo);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut).isEqualTo(newUrlPhoto);

        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
        verify(this.awsService, times(1)).upload(base64Photo);
    }

    @Test
    public void update_WithInvalidDataThrowingAmazonS3Exception_ReturnsNewUrlPhotoString() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        doThrow(new SdkClientException("AWS Error")).when(this.s3Instance).deleteObject(any(), eq(filename));

        Assertions.assertThatThrownBy(() -> this.awsService.update(urlPhoto, base64Photo)).isInstanceOf(AWSErrorException.class);

        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
        verify(this.awsService, times(0)).upload(base64Photo);
    }

    @Test
    public void update_WithInvalidDataThrowingSdkClientException_ReturnsNewUrlPhotoString() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        doThrow(new AmazonS3Exception("AWS Error")).when(this.s3Instance).deleteObject(any(), eq(filename));

        Assertions.assertThatThrownBy(() -> this.awsService.update(urlPhoto, base64Photo)).isInstanceOf(AWSErrorException.class);

        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
        verify(this.awsService, times(0)).upload(base64Photo);
    }

    @Test
    public void update_WithUploadThrowingAWSErrorException_ReturnsNewUrlPhotoString() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String urlPhoto = "https://example.com/randomphotoname.jpg";
        String filename = "randomphotoname.jpg";

        doThrow(new AWSErrorException("AWS Error")).when(this.awsService).upload(base64Photo);

        Assertions.assertThatThrownBy(() -> this.awsService.update(urlPhoto, base64Photo)).isInstanceOf(AWSErrorException.class);

        verify(this.s3Instance, times(1)).deleteObject(any(), eq(filename));
        verify(this.awsService, times(1)).upload(base64Photo);
    }
}

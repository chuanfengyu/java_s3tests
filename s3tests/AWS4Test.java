import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.StringUtils;
import com.sun.org.glassfish.gmbal.Description;

public class AWS4Test {
	
	//To Do... provide singleton to these instances
	private static S3 utils =  new S3();
	AmazonS3 svc = utils.getAWS4CLI();
	
	@AfterMethod
	public  void tearDownAfterClass() throws Exception {
		
		utils.tearDown();	
	}

	@BeforeMethod
	public void setUp() throws Exception {
	}
	
	@Test
	@Description("create w/x-amz-date after 9999, fails")
	public void testObjectCreateBadamzDateAfterEndAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "99990707T215304Z";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/date after 9999, fails")
	public void testObjectCreateBadDateAfterEndAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Tue, 07 Jul 9999 21:53:04 GMT";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/x-amz-date before epoch, fails")
	public void testObjectCreateBadamzDateBeforeEpochAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "9500707T215304Z";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/date before epoch, suceeds")
	public void testObjectCreateBadDateBeforeEpochAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Tue, 07 Jul 1950 21:53:04 GMT";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
	}
	
	@Test
	@Description("create w/x-amz-date in future, fails")
	public void testObjectCreateBadAmzDateAfterTodayAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "20300707T215304Z";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/date in future, suceeds")
	public void testObjectCreateBadDateAfterToday4AWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Tue, 07 Jul 2030 21:53:04 GMT";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		
	}
	
	@Test
	@Description("create w/x-amz-date in the past, fails")
	public void testObjectCreateBadAmzDateBeforeTodayAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "20100707T215304Z";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/date in past, suceeds")
	public void testObjectCreateBadDateBeforeToday4AWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Tue, 07 Jul 2010 21:53:04 GMT";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		
	}
	
	@Test
	@Description("create w/no x-amz-date, fails")
	public void testObjectCreateBadAmzDateNoneAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/no date, suceeds")
	public void testObjectCreateBadDateNoneAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		
	}
	
	@Test
	@Description("create w/non-graphic x-amz-date, fails")
	public void testObjectCreateBadamzDateUnreadableAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "\\x07";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/non-graphic date, fails")
	public void testObjectCreateBadDateUnreadableAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "\\x07";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "RequestTimeTooSkewed");
		}
	}
	
	@Test
	@Description("create w/empty x-amz-date, fails")
	public void testObjectCreateBadamzDateEmptyAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/empty date, suceeds")
	public void testObjectCreateBadDateEmptyAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		
	}
	
	@Test
	@Description("create w/invalid x-amz-date, fails")
	public void testObjectCreateBadamzDateInvalidAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Bad date";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("X-Amz-Date", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/invalid date, suceeds")
	public void testObjectCreateBadDateInvalidAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "Bad date";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Date", value);
		
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		
	}
	
	@Test
	@Description("create w/no user agent, fails")
	public void testObjectCreateBadUANoneAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("User-Agent", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/non-graphic user agent, fails")
	public void testObjectCreateBadUAUnreadableAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "\\x07";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("User-Agent", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/empty user agent, fails")
	public void testObjectCreateBadUAEmptyAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("User-Agent", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "SignatureDoesNotMatch");
		}
	}
	
	@Test
	@Description("create w/invalid authorization, fails")
	public void testObjectCreateBadAuthorizationInvalidAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "AWS4-HMAC-SHA256 Credential=HAHAHA";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Authorization", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "400 Bad Request");
		}
	}
	
	@Test
	@Description("create w/incorrect authorization, fails")
	public void testObjectCreateBadAuthorizationIncorrectAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "AWS4-HMAC-SHA256 Credential=HAHAHA";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Authorization", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "400 Bad Request");
		}
	}
	
	@Test
	@Description("create w/content length too short, fails")
	public void testObjectCreateBadContentlengthMismatchBelowAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		long contlength = 2;
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Content-Length", contlength);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "XAmzContentSHA256Mismatch");
		}
	}
	
	@Test
	@Description("create w/invalid MD5, fails")
	public void testObjectCreateBadMd5InvalidGarbageAWS4() {
		
		String bucket_name = utils.getBucketName();
		String key = "key1";
		String content = "echo lima golf";
		String value = "AWS4 HAHAHA";
		
		svc.createBucket(new CreateBucketRequest(bucket_name));

		byte[] contentBytes = content.getBytes(StringUtils.UTF8);
		InputStream is = new ByteArrayInputStream(contentBytes);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBytes.length);
		metadata.setHeader("Content-MD5", value);
		
		try {
		svc.putObject(new PutObjectRequest(bucket_name, key, is, metadata));
		}catch (AmazonServiceException err) {
			AssertJUnit.assertEquals(err.getErrorCode(), "InvalidDigest");
		}
	}

}

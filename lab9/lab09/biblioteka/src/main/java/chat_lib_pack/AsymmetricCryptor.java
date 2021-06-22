package chat_lib_pack;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricCryptor {
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public AsymmetricCryptor() {
		publicKey = null;
		privateKey = null;
	}


	public void generateKeys(int keyLength) throws InvalidParameterException {
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(keyLength);
			KeyPair pair = keyGen.generateKeyPair();
			publicKey = pair.getPublic();
			privateKey = pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}


	public PublicKey getPublicKey() {
		return publicKey;
	}


	public PrivateKey getPrivateKey() {
		return privateKey;
	}


	public void setEncodedPublicKey(byte[] pubKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec ks = new X509EncodedKeySpec(pubKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		this.publicKey = kf.generatePublic(ks);
	}


	public void setEncodedPrivateKey(byte[] privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		this.privateKey = kf.generatePrivate(spec);
	}


	public byte[] encrypt(byte[] data) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		byte[] encryptedData = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedData = cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return encryptedData;
	}


	public byte[] decrypt(byte[] data) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		byte[] decryptedData = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			decryptedData = cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return decryptedData;
	}
}

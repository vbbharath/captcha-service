package org.bharath.captchaservice.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {

	private static final String SECRET = "ciklcusmbnlqevmltisxaqnwfsveasar";

	public static String createJWT(Map<String, Object> claims) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(now).setSubject("anonymous")
				.setIssuer("org.bharath.captchaservice").setClaims(claims).signWith(signatureAlgorithm, signingKey);

		// 1 min validity
		long ttlMillis = 60000;
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		return builder.compact();
	}

	public static String getClaim(String jwt, String claimName) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(jwt)
				.getBody();
		return claims.get(claimName) != null ? claims.get(claimName).toString() : "";
	}
}

package familyhealth.component;

import familyhealth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;


    public String generateToken(User user){
        //properties => claims
        //JWT có 3 phần : header, payload, signature

        //Header sẽ tự sinh ra khi gọi Jwts.builder() và .signWith(...).

        //Claims chính là phần payload, nó sẽ chứa thông tin
        //Claims là phần chứa thông tin của user
        Map<String, Object> claims = new HashMap<>();
        //Chỉ chạy 1 lần để tạo secretKey
        //this.generateSecretKey();

        //Chỉ nên đưa trường có tính unique vào đề phân biệt thôi, không nên đưa hết thông tin vào
        claims.put("phoneNumber",user.getPhone());
        try{
            //Bắt đầu xây token
            String token = Jwts.builder()
                    //Gán claims
                    .setClaims(claims)
                    //Giá trị định danh của user, thường là: username, phonenumber,...
                    //Khi validate token ta sẽ gọi ngược lại: extractPhoneNumber(token) → trả về subject
                    .setSubject(user.getPhone())
                    //Thời gian hết hạn token = ngày hôm nay + expiration * 1000L (nhân với 1000l vì đổi sang mili giây)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    //Ký token bằng secretKey
                    //Như đã nói ở trên, JWT gồm 3 phần: header, payload, signature
                    //Signature chính là hash được tạo bằng secretKey
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    //Tạo token dạng string
                    .compact();
            //Sẽ tạo ra chuỗi như: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9....
            return token;
        } catch (Exception e) {
            System.out.println("Cannot create jwt token , error");
            return null;
        }
    }

    //Chuyển chuỗi secretKey thành Key để ký signature cho JWT
    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("NoGqvbiTkYcVvArxFI36DjCPjgQOIFo/962T4QO3fm8="))
        return Keys.hmacShaKeyFor(bytes);
    }

    //Tạo ra secretKey, đề Key getSignInKey() chuyển thành Key để ký signature cho JWT
    //Chỉ chạy 1 lần để tạo ra secretKey
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    //Kiểm tra token, lấy dữ liệu bên trong token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Lấy dữ liệu cụ thể từ Claims thây vì lấy hết toàn bộ dữ liệu
    //T : trả về bất cứ dữ liệu gì như String, Long, Integer (vì trong user  các thuộc tính có nhiều loại dữ liệu)
    //Function<Claims, T> claimsResolver là hầm nhận claims tar về T
    //Ví dụ, muốn lấy phoneNumber → sẽ truyền Claims::getSubject
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        //Gọi hàm extractAllClaims(token) để láy dữ liệu từ token
        final Claims claims = this.extractAllClaims(token);
        //Áp dụng hàm claimsResolver lên Claims → lấy ra giá trị mong muốn
        return claimsResolver.apply(claims);
    }

    //Xem token có hết hạn chưa
    public boolean isTokenExpired(String token){
        Date exprirationDate = this.extractClaims(token, Claims:: getExpiration);
        return exprirationDate.before(new Date());
    }

    //Lấy dữ liệu phone
    public String extractPhone(String token){
        return extractClaims(token, Claims:: getSubject);
    }

    //Dùng 2 hàm trên xem có đúng username và token còn thời hạn không
    public boolean validateToken(String token, UserDetails userDetails){
        String phone = extractPhone(token);
        return (phone.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}

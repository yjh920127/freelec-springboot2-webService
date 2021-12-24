package com.jojoIdu.book.springboot.config.auth.dto;

import com.jojoIdu.book.springboot.domain.user.Role;
import com.jojoIdu.book.springboot.domain.user.Users;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object>attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey =nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    //OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값하나하나를 반환해야함
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){

        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }

        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                                .name((String) attributes.get("name"))
                                .email((String) attributes.get("email"))
                                .picture((String) attributes.get("picture"))
                                .attributes(attributes)
                                .nameAttributeKey(userNameAttributeName)
                                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String,Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //userEntity 생성
    //OAuthAttributes에서 entity 생성 시점은 첫 가입시
    //가입시 기본 권한을 GUEST를 주기 위햇 Role 빌더값 GUEST
    public Users toEntity(){
        return Users.builder()
                    .name(name)
                    .email(email)
                    .picture(picture)
                    .role(Role.GUEST)
                    .build();
    }

}
